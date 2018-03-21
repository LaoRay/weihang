package cn.com.clubank.weihang.manage.repair.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.repair.dao.WorkHourMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkOperationMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkOrderMaterialMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkRepairMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkHour;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOperation;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOrderMaterial;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;
import cn.com.clubank.weihang.manage.repair.service.IRepairOrderFlowService;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 维修单流转业务管理
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class RepairOrderFlowServiceImpl implements IRepairOrderFlowService {

	@Resource
	private WorkRepairMapper workRepairMapper;

	@Resource
	private WorkOperationMapper workOperationMapper;

	@Resource
	private WorkPickupOrderMapper workPickupOrderMapper;

	@Resource
	private WorkHourMapper workHourMapper;

	@Resource
	private WorkOrderMaterialMapper workOrderMaterialMapper;

	@Resource
	private IWorkRepairService iWorkRepairService;

	@Resource
	private BaseUserMapper baseUserMapper;

	@Resource
	private IMessageService iMessageService;

	@Resource
	private BaseCodeRuleService baseCodeRuleService;

	@Override
	public String supervisorAssign(String wpoId, String consultant) {
		log.info("服务主管派单，接车单：{}, 服务顾问：{}", wpoId, consultant);
		JSONObject result = new JSONObject();
		WorkPickupOrder info = workPickupOrderMapper.selectByPrimaryKey(wpoId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "接车单不存在！");
			return result.toString();
		}
		// 修改服务顾问
		info.setConsultantBy(consultant);
		info.setStatus(WorkPickupOrder.STATUS_NO_SERVE);// 状态为待服务-服务主管派单时修改接车单状态为服务中
		workPickupOrderMapper.updateByPrimaryKey(info);

		// 创建业务流水表数据-验证唯一性
		WorkOperation wo = workOperationMapper.selectPickupByReceiveOrder(consultant, info.getWpoNo());
		if (wo == null) {
			wo = new WorkOperation();
		}
		wo.setAllocationBy(info.getReceiveBy());// 派单人
		wo.setAllocationName(baseUserMapper.selectNameById(info.getReceiveBy()));
		wo.setReceiveBy(consultant);// 接单人
		wo.setReceiveName(baseUserMapper.selectNameById(consultant));
		wo.setCustomerId(info.getCustomerId());// 客户id
		wo.setStatus(WorkOperation.STATUS_WAIT_RECEIVE);// 状态：待接车
		wo.setCarId(info.getCarId());// 车辆id
		wo.setCarNo(info.getCarNo());
		wo.setWpoId(info.getWpoId()); // 此处为接车单的业务逻辑数据
		wo.setWorkType(WorkOperation.WORK_TYPE_RECEIVE);// 类型：接车单
		wo.setWpoNo(info.getWpoNo());
		if (StringUtil.isBlank(wo.getWoId())) {
			workOperationMapper.insert(wo);
		} else {
			workOperationMapper.updateByPrimaryKey(wo);
		}

		// 推送消息给服务顾问
		String title = String.format("接车单号：%s", info.getWpoNo());
		String content = String.format("【%s】 请您尽快接车派单！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_RECEIVE, title, content, wpoId), consultant);

		result.put("apiStatus", ResultCode.SUCC.getValue());
		return result.toString();
	}

	@Override
	public String assign(String repairId, String repairSupervisor) {
		log.info("服务顾问-维修派单，维修单：{}, 车间组长：{}", repairId, repairSupervisor);
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}
		// 修改状态:待接车; 修改信息：车间组长
		info.setStatus(WorkRepair.STATUS_WAIT_RECEIVE);
		info.setRepairSupervisor(repairSupervisor);
		workRepairMapper.updateByPrimaryKey(info);

		// 创建业务流水表数据
		WorkOperation wo = new WorkOperation();
		wo.setAllocationBy(info.getConsultantBy());// 派单人
		wo.setAllocationName(baseUserMapper.selectNameById(info.getConsultantBy()));
		wo.setReceiveBy(repairSupervisor);// 接单人
		wo.setReceiveName(baseUserMapper.selectNameById(repairSupervisor));
		wo.setCustomerId(info.getCustomerId());// 客户id
		wo.setStatus(WorkOperation.STATUS_WAIT_RECEIVE);// 状态：待接车
		wo.setWorkOrderId(repairId);// 工单id
		wo.setWorkType(WorkOperation.WORK_TYPE_REPAIR);// 类型：维修单
		wo.setCarId(info.getCarId());// 车辆id
		wo.setCarNo(info.getCarNo());
		workOperationMapper.insert(wo);

		// 接车单的业务逻辑：已接车-已完成
		WorkOperation wop = workOperationMapper.selectPickupByReceiveOrder(info.getConsultantBy(), info.getWpoNo());
		if (wop != null) {
			wop.setReceiveDate(new Date());
			wop.setStatus(WorkOperation.STATUS_COMPLETE);
			workOperationMapper.updateByPrimaryKey(wop);
		}

		// 服务顾问派单时处理接车单
		WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
		wpo.setStatus(WorkPickupOrder.STATUS_IN_SERVE); // 修改接车单状态为-服务中
		wpo.setWorkType(WorkOperation.WORK_TYPE_REPAIR); // 接车单类型：维修单
		workPickupOrderMapper.updateByPrimaryKey(wpo);

		// 推送消息给车间组长
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 服务顾问已派单，请您尽快接车！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId), repairSupervisor);

		json.put("wrId", info.getWrId());
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String receive(String repairId, String engineer) {
		log.info("服务顾问-维修派单，维修单：{}, 技师：{}", repairId, engineer);
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}

		// 修改信息：技师,状态:已接车
		info.setStatus(WorkRepair.STATUS_ALREADY_RECEIVE);
		info.setSupervisorBy(engineer);
		workRepairMapper.updateByPrimaryKey(info);

		// 修改业务逻辑单状态：待服务
		WorkOperation wop = workOperationMapper.selectByReceiveOrder(info.getRepairSupervisor(), info.getWrId());
		if (wop != null) {
			wop.setReceiveDate(new Date());
			wop.setStatus(WorkOperation.STATUS_WAIT_SERVE);
			workOperationMapper.updateByPrimaryKey(wop);
		}

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String submit(String repairId, JSONArray material, JSONArray workHour) {
		log.info("车间组长提交维修单，维修单：{}", repairId);
		log.info("车间组长提交维修单，材料信息：{}", material.toString());
		log.info("车间组长提交维修单，工时费用：{}", workHour.toString());
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}

		// 保存工时费用（维修项目）信息
		iWorkRepairService.saveWorkHour(info, workHour);

		// 保存维修材料信息
		iWorkRepairService.saveMaterial(info, material);

		info.setTotal(info.getBillMaterialTotal().add(info.getManHaurTotal()));
		// 修改状态
		if (info.getStatus() == WorkRepair.STATUS_ALREADY_RECEIVE) {
			// 如果当前状态是已接车，则修改为库房待确认
			info.setStatus(WorkRepair.STATUS_STORE_WAIT_CONFIRM);
			// 推送消息给库房确认价格（pc端）

		} else {
			// 如果当前状态是车间组长-修改项目，则修改为服务顾问待确认
			info.setStatus(WorkRepair.STATUS_CONSULTANT_WAIT_CONFIRM);
			// 推送消息给服务顾问待确认
			String title = String.format("维修单号：%s", info.getWorkRepairNo());
			String content = String.format("【%s】 维修项目已修改，请您尽快确认！", info.getCarNo());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId),
					info.getRepairSupervisor());
		}
		workRepairMapper.updateByPrimaryKey(info);

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String consultantOK(String repairId, JSONArray material, JSONArray workHour) {
		log.info("服务顾问确认项目，维修单：{}", repairId);
		log.info("服务顾问确认项目，工时费用：{}", workHour.toString());
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}

		BigDecimal materialTotal = info.getBillMaterialTotal(); // 用料总价
		BigDecimal hourTotal = new BigDecimal(0); // 工时总价
		// 修改工时费用（维修项目）信息
		WorkHour wh = null;
		for (int i = 0; i < workHour.size(); i++) {
			wh = workHourMapper.selectByPrimaryKey(workHour.getJSONObject(i).getString("whId"));
			wh.setPrice1(workHour.getJSONObject(i).getBigDecimal("price1"));
			wh.setPrice2(workHour.getJSONObject(i).getBigDecimal("price2"));
			wh.setPrice3(workHour.getJSONObject(i).getBigDecimal("price3"));
			workHourMapper.updateByPrimaryKey(wh);
			hourTotal = hourTotal.add(wh.getPrice1());
		}

		if (material != null) {
			// 服务顾问确认步骤不需要编辑材料信息

			/*
			 * materialTotal = new BigDecimal(0); //修改维修材料信息 WorkOrderMaterial
			 * wom = null; for (int i = 0; i < material.size(); i++) {
			 * JSONObject womJson = material.getJSONObject(i); wom =
			 * workOrderMaterialMapper.selectByPrimaryKey(womJson.getString(
			 * "womId")); wom.setPrice1(womJson.getBigDecimal("price1"));
			 * wom.setTotal(womJson.getBigDecimal("price1").multiply(womJson.
			 * getBigDecimal("quantity")));
			 * workOrderMaterialMapper.updateByPrimaryKey(wom); materialTotal =
			 * materialTotal.add(wom.getTotal()); }
			 */
		}

		info.setBillMaterialTotal(materialTotal);
		info.setManHaurTotal(hourTotal);
		info.setTotal(materialTotal.add(hourTotal));
		// 修改状态：待支付
		info.setStatus(WorkRepair.STATUS_WAIT_PAY);
		workRepairMapper.updateByPrimaryKey(info);

		// 推送消息给客户待支付
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 维修项目及费用已生成，请您尽快确认！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId),
				info.getCustomerId());

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String customerUpdate(String repairId) {
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}

		// 修改状态:车间组长-修改项目
		info.setStatus(WorkRepair.STATUS_UPDATE);
		workRepairMapper.updateByPrimaryKey(info);

		// 推送至车间组长
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 客户需要修改维修项目，请您尽快与客户确认并修改！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId),
				info.getRepairSupervisor());

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String inService(String repairId) {
		log.info("服务顾问开始（返工）维修，维修单：{}", repairId);
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}
		// 修改状态:维修中
		workRepairMapper.updateStatusByPrimaryKey(WorkRepair.STATUS_IN_REPAIR, repairId);

		// 修改业务逻辑单状态：服务中
		workOperationMapper.updateStatusByReceiveOrder(info.getRepairSupervisor(), repairId,
				WorkOperation.STATUS_IN_SERVE);

		// 推送消息给客户
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 维修工作已开始！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId),
				info.getCustomerId());

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String complete(String repairId) {
		log.info("车间组长完成维修，维修单：{}", repairId);
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "维修单不存在！");
			return json.toString();
		}

		// 修改状态:客户确认
		info.setStatus(WorkRepair.STATUS_CUSTOMER_CONFIRM);
		workRepairMapper.updateByPrimaryKey(info);

		// 推送至客户确认
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 维修工作已完成，请您尽快确认！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, repairId),
				info.getCustomerId());

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String result(String repairId, Integer type, String opinion) {
		log.info("客户确认维修结果，维修单：{}，type：{}，意见：{}", repairId, type, opinion);
		JSONObject json = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}

		// 接车单状态：已完成
		workPickupOrderMapper.updateStatusByNO(info.getWpoNo(), WorkPickupOrder.STATUS_COMPLETE);
		// 修改业务逻辑单状态：已完成
		workOperationMapper.updateStatusByReceiveOrder(info.getRepairSupervisor(), repairId,
				WorkOperation.STATUS_COMPLETE);

		// 修改状态；不同意时追加子订单，
		if (type == 0) {
			// 不同意
			info.setRemarks(opinion);// 备注：客户意见
			info.setStatus(WorkRepair.STATUS_DISAGREE);
			workRepairMapper.updateByPrimaryKey(info);
			// 追加子订单
			WorkPickupOrder wpo = new WorkPickupOrder(); //接车单
			wpo.setWpoNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_JC)); // 接车单号
			wpo.setReservationOrderNo(info.getReservationOrderNo());
			wpo.setReceiveBy(info.getConsultantBy());// 接车人员
			wpo.setCarId(info.getCarId());// 车辆id
			wpo.setCarNo(info.getCarNo());
			wpo.setCustomerId(info.getCustomerId());// 客户id
			wpo.setFromType(info.getFromType());// 到店方式
			wpo.setLinkman(info.getLinkman());// 联系人
			wpo.setLinkmanMobile(info.getLinkmanMobile());// 联系人手机
			wpo.setWorkType(2); 
			wpo.setStatus(WorkPickupOrder.STATUS_NO_SERVE);
			wpo.setConsultantBy(info.getConsultantBy());// 服务顾问
			workPickupOrderMapper.insert(wpo);
			
			WorkRepair childOrder = new WorkRepair();
			childOrder.setWpoNo(wpo.getWpoNo());
			childOrder.setReservationOrderNo(info.getReservationOrderNo());
			childOrder.setWorkRepairNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_WX)); // 维修单号

			childOrder.setReceiveDate(info.getReceiveDate());
			childOrder.setCarId(info.getCarId());
			childOrder.setCarNo(info.getCarNo());
			childOrder.setCustomerId(info.getCustomerId());
			childOrder.setLinkman(info.getLinkman());
			childOrder.setLinkmanMobile(info.getLinkmanMobile());
			childOrder.setFromType(info.getFromType());
			childOrder.setKilometre(info.getKilometre());
			childOrder.setBunkers(info.getBunkers());
			childOrder.setStatus(WorkRepair.STATUS_SAVE);
			childOrder.setParentId(info.getWrId());
			childOrder.setConsultantBy(info.getConsultantBy());
			childOrder.setRemarks(opinion);// 备注：客户意见
			workRepairMapper.insert(childOrder);
			log.info("客户确认维修结果-不同意，生成子订单：{}", childOrder.getWorkRepairNo());

			// 推送消息给服务顾问
			String title = String.format("维修单号：%s", childOrder.getWorkRepairNo());
			String content = String.format("【%s】 客户不同意维修完成，请您尽快与客户联系并重新派单！", childOrder.getCarNo());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, childOrder.getWrId()),
					info.getConsultantBy());

		} else {
			// 确定
			workRepairMapper.updateStatusByPrimaryKey(WorkRepair.STATUS_COMPLETE, repairId);
			// 推送已完成消息给服务顾问和车间组长
			String title = String.format("维修单号：%s", info.getWorkRepairNo());
			String content = String.format("【%s】 客户已确认维修完成！", info.getCarNo());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()),
					info.getRepairSupervisor());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()),
					info.getConsultantBy());
			
			WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
			if (wpo != null && !wpo.getReceiveBy().equals(wpo.getConsultantBy())) {
				//推送给服务顾问
				iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()), wpo.getReceiveBy());
			}
		}

		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String clientLibraryConfirm(JSONObject param) {
		JSONObject result = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(param.getString("wrId"));
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "维修单不存在！");
			return result.toString();
		}
		
		// 计算用料总价
		BigDecimal materialTotal = new BigDecimal(0); // 用料总价
		List<WorkOrderMaterial> list = workOrderMaterialMapper.selectByOrderId(info.getWrId());
		for (WorkOrderMaterial wom : list) {
			materialTotal = materialTotal.add(wom.getTotal());
		}
		info.setBillMaterialTotal(materialTotal);
		info.setTotal(info.getBillMaterialTotal().add(info.getManHaurTotal())); //用料和工时的总价
		info.setStatus(WorkRepair.STATUS_CONSULTANT_WAIT_CONFIRM); // 修改状态为服务顾问待确认
		workRepairMapper.updateByPrimaryKey(info);

		// 推送消息给服务顾问
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 维修项目及材料费用已生成，请您尽快确认！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()),
				info.getConsultantBy());

		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "操作成功");
		return result.toString();
	}

	@Override
	public String clientUpdateOrderMaterial(JSONObject param) {
		JSONObject result = new JSONObject();
		WorkOrderMaterial info = workOrderMaterialMapper.selectByPrimaryKey(param.getString("womId"));
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "用料对象不存在！");
			return result.toString();
		}
		info.setPrice1(param.getBigDecimal("price1"));
		info.setPrice2(param.getBigDecimal("price2"));
		info.setPrice3(param.getBigDecimal("price3"));
		// 计算用料总价时应该根据会员等级计算
		info.setTotal(info.getPrice1().multiply(new BigDecimal(info.getQuantity())));
		workOrderMaterialMapper.updateByPrimaryKey(info);

		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "修改成功");
		return result.toString();
	}

}
