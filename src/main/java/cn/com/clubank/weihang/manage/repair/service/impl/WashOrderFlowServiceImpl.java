package cn.com.clubank.weihang.manage.repair.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.repair.dao.WorkOperationMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkWashMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOperation;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.repair.pojo.WorkWash;
import cn.com.clubank.weihang.manage.repair.service.IWashOrderFlowService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 洗车单流转业务管理
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class WashOrderFlowServiceImpl implements IWashOrderFlowService {
	
	@Resource
	private WorkWashMapper workWashMapper;
	
	@Resource
	private WorkPickupOrderMapper workPickupOrderMapper;
	
	@Resource
	private WorkOperationMapper workOperationMapper;
	
	@Resource
	private BaseUserMapper BaseUserMapper;
	
	@Resource
	private IMessageService iMessageService;
	
	@Resource
	private BaseCodeRuleService baseCodeRuleService;
	
	@Resource
	private OrderService orderService;

	@Override
	public String washAssign(String wpoId, String washLeader, String orderDetailIds) {
		log.info("服务顾问-洗车派单，接车单：{}, 洗车组长：{}", wpoId, washLeader);
		JSONObject result = new JSONObject();
		WorkPickupOrder wpo = workPickupOrderMapper.selectByPrimaryKey(wpoId);
		if (wpo == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "接车单不存在！");
			return result.toString();
		}
		List<WorkWash> wws = workWashMapper.selectByWpo(wpoId);
		if (wws.size() > 0) {
			result.put("apiStatus", ResultCode.DATA_EXIST.getValue());
			result.put("msg", "此接车单已经洗车派单！");
			return result.toString();
		}
		// 创建洗车单
		WorkWash info = new WorkWash();
		info.setWpoNo(wpo.getWpoNo());// 接车单号
		info.setReservationOrderNo(wpo.getReservationOrderNo());// 预约单号
		info.setWorkWashNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_XC));// 洗车单号
		info.setReceiveDate(wpo.getCreateDate());// 接车日期
		info.setConsultantBy(wpo.getConsultantBy());// 服务顾问
		info.setGroupLeader(washLeader);// 洗车组长
		info.setCarId(wpo.getCarId());// 车辆id
		info.setCarNo(wpo.getCarNo());
		info.setStatus(WorkWash.STATUS_WAIT_RECEIVE);// 状态：待接车
		info.setFromType(wpo.getFromType());// 到店方式
		info.setLinkman(wpo.getLinkman());// 联系人
		info.setLinkmanMobile(wpo.getLinkmanMobile());// 联系人手机
		info.setCustomerId(wpo.getCustomerId());
		workWashMapper.insert(info);
		
		//修改接车单信息
		if (StringUtil.isNotBlank(orderDetailIds)) {
			wpo.setOrderDetailIds(orderDetailIds); // 使用所购买的商品
		}
		wpo.setWorkType(WorkOperation.WORK_TYPE_WASH); //接车单类型：洗车单
		wpo.setStatus(WorkPickupOrder.STATUS_IN_SERVE); //修改接车单状态：服务中
		workPickupOrderMapper.updateByPrimaryKey(wpo);
		
		//修改订单中使用的商品的状态至已使用
		if (StringUtil.isNotBlank(wpo.getOrderDetailIds())) {
			for (String orderDetailId : wpo.getOrderDetailIds().split(",")) {
				orderService.memberConfirmOrderList(orderDetailId);
			}
		}
		
		//创建业务流水表数据
		WorkOperation wo = new WorkOperation();
		wo.setAllocationBy(info.getConsultantBy());// 派单人
		wo.setAllocationName(BaseUserMapper.selectNameById(info.getConsultantBy()));
		wo.setReceiveBy(washLeader);// 接单人
		wo.setReceiveName(BaseUserMapper.selectNameById(washLeader));
		wo.setCustomerId(wpo.getCustomerId());// 客户id
		wo.setStatus(WorkOperation.STATUS_WAIT_RECEIVE);// 状态：待接车
		wo.setWorkOrderId(info.getWwId());// 工单id
		wo.setWorkType(WorkOperation.WORK_TYPE_WASH);// 类型：洗车单
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
		
		// 推送消息给洗车组长
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 请您尽快接车！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getGroupLeader());
		
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return result.toString();
	}
	
	@Override
	public String receive(String washId, String engineer) {
		log.info("洗车组长接车，洗车单：{}, 洗车师：{}", washId, engineer);
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		//修改信息：洗车师;状态:已接车
		info.setSupervisorBy(engineer);
		info.setStatus(WorkWash.STATUS_ALREADY_RECEIVE);
		workWashMapper.updateByPrimaryKey(info);
		
		//修改业务逻辑单状态：待服务
		WorkOperation wop = workOperationMapper.selectByReceiveOrder(info.getGroupLeader(), info.getWwId());
		if (wop != null) {
			wop.setReceiveDate(new Date());
			wop.setStatus(WorkOperation.STATUS_WAIT_SERVE);
			workOperationMapper.updateByPrimaryKey(wop);
		}
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String submit(String washId, Integer washType, BigDecimal washCost) {
		log.info("洗车组长提交洗车单：{}, 洗车项目：{}，价格：{}", washId, washType, washCost);
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		//修改状态，保存洗车项目信息；
		info.setStatus(WorkWash.STATUS_WAIT_PAY); //待支付
		info.setWashType(washType);
		info.setWashCost(washCost);
		workWashMapper.updateByPrimaryKey(info);
		
		// 推送至客户付款
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 洗车项目及费用已生成，请您尽快确认！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getCustomerId());
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String customerUpdate(String washId) {
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		
		//修改状态:已接车状态，车间组长重新修改项目
		info.setStatus(WorkWash.STATUS_ALREADY_RECEIVE);
		workWashMapper.updateByPrimaryKey(info);
		
		// 推送至洗车组长
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 洗车项目修改，请您尽快确认并开始洗车！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getCustomerId());
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String start(String washId) {
		log.info("洗车组长开始洗车，洗车单：{}", washId);
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		//修改状态:洗车中
		workWashMapper.updateStatusByPrimaryKey(washId, WorkWash.STATUS_IN_WASH);
		
		//修改业务逻辑单状态：服务中
		workOperationMapper.updateStatusByReceiveOrder(info.getGroupLeader(), info.getWwId(), WorkOperation.STATUS_IN_SERVE);
		
		// 推送消息给客户
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 洗车工作已开始！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getCustomerId());
		
		//推送消息给洗车师
		title = String.format("洗车单号：%s", info.getWorkWashNo());
		content = String.format("【%s】 洗车工作已开始，请您开始洗车！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()),
				info.getSupervisorBy());
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String complete(String washId) {
		log.info("洗车组长完成洗车，洗车单：{}", washId);
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		
		//修改状态:客户确认结果
		info.setStatus(WorkWash.STATUS_CUSTOMER_CONFIRM);
		workWashMapper.updateByPrimaryKey(info);
		
		// 推送消息至客户确认
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 洗车工作已完成，请您尽快确认！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getCustomerId());
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String rework(String washId) {
		log.info("洗车组长返工清洗，洗车单：{}", washId);
		JSONObject json = new JSONObject();
		//修改状态:洗车中
		int num = workWashMapper.updateStatusByPrimaryKey(washId, WorkWash.STATUS_IN_WASH);
		if (num == 0) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	@Override
	public String result(String washId, Integer type, String opinion) {
		log.info("客户确认洗车结果，洗车单：{}，type：{}，意见：{}", washId, type, opinion);
		JSONObject json = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "洗车单不存在！");
			return json.toString();
		}
		if (type == 0) {
			//不同意
			info.setDescription(opinion);// 备注：客户意见
			info.setStatus(WorkWash.STATUS_DISAGREE);
			workWashMapper.updateByPrimaryKey(info);
		} else {
			//确定
			workWashMapper.updateStatusByPrimaryKey(washId, WorkWash.STATUS_COMPLETE);
			
			//接车单状态：已完成
			WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
			if (wpo != null) {
				wpo.setStatus(WorkPickupOrder.STATUS_COMPLETE);
				workPickupOrderMapper.updateByPrimaryKey(wpo);
			}
			
			//修改业务逻辑单状态：已完成
			workOperationMapper.updateStatusByReceiveOrder(info.getGroupLeader(), info.getWwId(), WorkOperation.STATUS_COMPLETE);
		}
		
		//推送消息
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		if (type == 0) {
			// 不同意：推送消息至洗车组长-返工清洗
			String content = String.format("【%s】 客户不同意洗车完成，请您尽快检查！", info.getCarNo());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getGroupLeader());
		} else {
			// 推送消息
			String content = String.format("【%s】 客户已确认洗车完成！", info.getCarNo());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getGroupLeader());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), info.getConsultantBy());
			
			WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
			if (wpo != null && !wpo.getReceiveBy().equals(wpo.getConsultantBy())) {
				//推送给服务顾问
				iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()), wpo.getReceiveBy());
			}
		}
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

}
