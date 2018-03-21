package cn.com.clubank.weihang.manage.repair.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialStorageMapper;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;
import cn.com.clubank.weihang.manage.member.dao.AccountBenefitMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkCarPicMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkEvaluateMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkHourMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkOperationMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkOrderMaterialMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkPayInfoMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkQueryMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkRepairMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkWashMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkCarPic;
import cn.com.clubank.weihang.manage.repair.pojo.WorkEvaluate;
import cn.com.clubank.weihang.manage.repair.pojo.WorkHour;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOperation;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOrderMaterial;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPayInfo;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;
import cn.com.clubank.weihang.manage.repair.pojo.WorkWash;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 维修单业务逻辑层
 * 
 * @author Liangwl
 *
 */
@Slf4j
@Service
public class WorkRepairServiceImpl implements IWorkRepairService {

	@Resource
	private WorkRepairMapper workRepairMapper;

	@Resource
	private WorkHourMapper workHourMapper;

	@Resource
	private WorkOrderMaterialMapper workOrderMaterialMapper;

	@Resource
	private WorkPickupOrderMapper workPickupOrderMapper;

	@Resource
	private CarInfoMapper carInfoMapper;

	@Resource
	private IReservationService iReservationService;

	@Resource
	private CustomerInfoMapper customerInfoMapper;

	@Resource
	private AccountBenefitMapper accountBenefitMapper;

	@Resource
	private WorkQueryMapper workQueryMapper;

	@Resource
	private WorkWashMapper workWashMapper;

	@Resource
	private BaseCodeRuleService baseCodeRuleService;

	@Resource
	private WorkOperationMapper workOperationMapper;

	@Resource
	private WorkCarPicMapper workCarPicMapper;

	@Resource
	private BaseUserMapper baseUserMapper;

	@Resource
	private IMessageService iMessageService;

	@Resource
	private WorkMaterialStorageMapper workMaterialStorageMapper;

	@Resource
	private WorkPayInfoMapper workPayInfoMapper;

	@Resource
	private WorkEvaluateMapper workEvaluateMapper;

	@Resource
	private OrderDetailMapper orderDetailMapper;

	@Override
	public String scanReceive(String carNo, String userId, Integer dutyType) {
		log.info("扫一扫接车：carNo：{}, userId：{}, dutyType：{}", carNo, userId, dutyType);
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<>();
		// 未查询到车辆信息，返回未注册
		CarInfo carInfo = carInfoMapper.selectByCarNo(carNo);
		if (carInfo == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "车牌号没有注册！");
			return result.toString();
		}
		// 判断是否存在还未完成的订单
		List<WorkPickupOrder> wpos = workPickupOrderMapper.findUndoneByCarNo(carNo);
		if (wpos.size() > 0) {
			if ((dutyType == BaseUser.DUTY_SERVICE_CONSULTANT && !userId.equals(wpos.get(0).getConsultantBy()))
					|| (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR)) {
				// 服务顾问查询到未完成的接车单，如果不是自己的返回此提示信息；服务主管查询到未完成的接车单返回此提示信息
				result.put("apiStatus", ResultCode.DATA_EXIST.getValue());
				result.put("msg", "此车辆已接车，订单正在服务中！");
				return result.toString();
			}
		}

		// 获取预约单，修改状态
		WorkReservationOrder wro = iReservationService.selectUndoneByCarNo(carNo);
		if (wro != null) {
			wro.setFollowServiceBy(userId); // 跟进客服
			wro.setReservationStatus(WorkReservationOrder.STATUS_COMPLETE);// 预约单状态：已完成
			iReservationService.updateByPrimaryKey(wro);
		}
		// 创建接车单
		WorkPickupOrder wpo = null;
		List<WorkPickupOrder> list = new ArrayList<WorkPickupOrder>();
		if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			// 服务顾问查询当前未完成的接车单
			list = workPickupOrderMapper.findUndoneByConsultantBy(userId, carInfo.getCarId());
		} else if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
			// 服务主管查询当前未完成的接车单
			list = workPickupOrderMapper.findUndoneByReceiveBy(userId, carInfo.getCarId());
		}
		if (list.size() > 0) {
			wpo = list.get(0);
		} else {
			// 如果不存在则新增
			wpo = new WorkPickupOrder();
			wpo.setWpoNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_JC)); // 接车单号
			wpo.setReceiveBy(userId);// 接车人员
			wpo.setCarId(carInfo.getCarId());// 车辆id
			wpo.setCarNo(carInfo.getCarNo());
			wpo.setCustomerId(carInfo.getCustomerId());// 客户id

			if (wro != null) {
				wpo.setReservationOrderNo(wro.getRoId());// 预约单号
				wpo.setWorkType(wro.getReservationType() == WorkReservationOrder.TYPE_WASH ? 1 : 2); // 根据预约单预约类型判断是洗车还是维修
				wpo.setFromType(wro.getFromType());// 到店方式
				wpo.setLinkman(wro.getContacts());// 联系人
				wpo.setLinkmanMobile(wro.getContactsMobile());// 联系人手机
			}
			if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
				wpo.setConsultantBy(userId);// 如果当前角色是服务顾问，则直接保存服务顾问信息
				// wpo.setStatus(WorkPickupOrder.STATUS_IN_SERVE);//
				// 如果当前角色是服务顾问，状态为服务中
			} else if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
				// 服务主管扫一扫，如果是新增的接车单则状态为0-服务主管未派单
				wpo.setStatus(WorkPickupOrder.STATUS_NO_SEND);
			}
			workPickupOrderMapper.insert(wpo);
		}

		// 返回数据
		CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(carInfo.getCustomerId());
		if (cus != null) {
			map.put("realname", cus.getRealname());
			map.put("headicon", cus.getHeadicon());// 头像
			map.put("recommendCode", cus.getRecommendCode());// 推荐人帐号
		} else {
			map.put("realname", Constants.NULL);
			map.put("headicon", Constants.NULL);// 头像
			map.put("recommendCode", Constants.NULL);// 推荐人帐号
		}

		map.put("vipLevel", StringUtil.isBlank(carInfo.getAbId()) ? "普通"
				: accountBenefitMapper.selectByPrimaryKey(carInfo.getAbId()).getBenefitName());
		map.put("carNo", carInfo.getCarNo());
		map.put("carVin", StringUtil.isBlank(carInfo.getCarVin()) ? Constants.NULL : carInfo.getCarVin());// vin
		map.put("carTypeInfo",
				StringUtil.isBlank(carInfo.getCarTypeInfo()) ? Constants.NULL : carInfo.getCarTypeInfo());// 车型
		map.put("wpoId", wpo.getWpoId());// 接车单id
		map.put("status", wpo.getStatus());// 接车单状态
		map.put("workType", wro == null ? Constants.NULL : wpo.getWorkType());// 接车单类型
		// 预约单
		map.put("reservation", wro == null ? Constants.NULL : wro);
		map.put("reservationIsNull", wro == null ? Constants.YES : Constants.NO);

		// 购买的服务类和汽车类商品
		List<Map<String, String>> serviceProduct = orderDetailMapper
				.selectWaitReceiveServiceOrder(carInfo.getCustomerId());
		map.put("serviceProducts", serviceProduct.size() == 0 ? Constants.NULL : serviceProduct);
		map.put("isHaveProduct", serviceProduct.size() == 0 ? Constants.NO : Constants.YES);

		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "成功");
		result.put("data", map);
		return result.toString();
	}

	@Override
	public CommonResult CheckCostInfo(String wrId) {
		try {
			List<WorkHour> workManHaurs = workHourMapper.selectByOrderId(wrId);
			List<WorkOrderMaterial> workOrderMaterials = workOrderMaterialMapper.selectByOrderId(wrId);
			List<Object> list = new ArrayList<>();
			list.add(workManHaurs);
			list.add(workOrderMaterials);
			return CommonResult.result(ResultCode.SUCC.getValue(), "查看费用详情成功", list);
		} catch (Exception e) {
			log.error("服务器异常", e);
			return CommonResult.result(ResultCode.SERVER_ERROR.getValue(), "服务器内部错误，查看费用详情失败");
		}

	}

	@Override
	public String saveOrderInfo(JSONObject param) {
		log.info("保存维修单信息：{}", param.toString());
		JSONObject json = new JSONObject();
		WorkPickupOrder wpo = workPickupOrderMapper.selectByPrimaryKey(param.getString("wpoId"));
		if (wpo == null) {
			json.put("msg", "接车单不存在！");
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			return json.toString();
		}
		WorkRepair info = null;
		try {
			info = JSONObject.toJavaObject(param, WorkRepair.class);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "参数不正确或者输入参数格式错误！");
			json.put("apiStatus", ResultCode.FAIL.getValue());
			return json.toString();
		}
		// 查询是否已经存在派单的维修单
		List<WorkRepair> wrs = workRepairMapper.selectByWpo(wpo.getWpoNo());
		if (wrs.size() > 0) {
			if (wrs.get(0).getStatus() == WorkRepair.STATUS_SAVE) {
				// 如果存在暂存的维修单则执行更新操作
				info.setWrId(wrs.get(0).getWrId());
			} else {
				json.put("msg", "此接车单已经维修派单！");
				json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
				return json.toString();
			}
		}
		info.setWpoNo(wpo.getWpoNo());
		info.setReservationOrderNo(wpo.getWpoNo());
		info.setReceiveDate(wpo.getCreateDate());
		info.setCarId(wpo.getCarId());
		info.setCarNo(wpo.getCarNo());
		info.setConsultantBy(wpo.getConsultantBy());
		info.setCustomerId(wpo.getCustomerId());
		info.setFromType(wpo.getFromType());
		info.setLinkman(wpo.getLinkman());
		info.setLinkmanMobile(wpo.getLinkmanMobile());
		info.setWorkRepairNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_WX)); // 维修单号
		if (StringUtil.isBlank(info.getWrId())) {
			workRepairMapper.insert(info);
		} else {
			workRepairMapper.updateByPrimaryKey(info);
		}

		// 修改车辆的vin
		CarInfo carInfo = carInfoMapper.selectByPrimaryKey(wpo.getCarId());
		carInfo.setCarVin(param.getString("carVin"));
		carInfoMapper.updateByPrimaryKey(carInfo);

		// 保存维修项目
		this.saveWorkHour(info, param.getJSONArray("item"));

		// 保存状态图片
		this.saveCarPic(info.getWrId(), param.getString("urls"));

		json.put("repairId", info.getWrId());
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public void saveWorkHour(WorkRepair info, JSONArray workHour) {
		// 先删除工时费用信息
		workHourMapper.deleteByWorkOrderId(info.getWrId());

		WorkHour wh = null;
		BigDecimal hourTotal = new BigDecimal(0); // 工时总价
		for (int i = 0; i < workHour.size(); i++) {
			wh = workHour.getObject(i, WorkHour.class);
			wh.setWorkOrderId(info.getWrId());
			wh.setCarId(info.getCarId());
			wh.setCustomerId(info.getCustomerId());
			workHourMapper.insert(wh);
			if (wh.getPrice1() != null) {
				hourTotal = hourTotal.add(wh.getPrice1());
			}
		}
		info.setManHaurTotal(hourTotal);
		workRepairMapper.updateByPrimaryKey(info);
	}

	@Override
	public void saveMaterial(WorkRepair info, JSONArray material) {
		// 先删除材料信息
		workOrderMaterialMapper.deleteByWorkOrderId(info.getWrId());

		WorkOrderMaterial wom = null;
		BigDecimal materialTotal = new BigDecimal(0); // 用料总价
		for (int i = 0; i < material.size(); i++) {
			wom = material.getObject(i, WorkOrderMaterial.class);
			wom.setWorkOrderId(info.getWrId());
			wom.setCarId(info.getCarId());
			wom.setCustomerId(info.getCustomerId());
			// 获取物料管理里面的价格
			WorkMaterialStorage wws = workMaterialStorageMapper.selectByCode(wom.getItemCode());
			if (wws != null) {
				wom.setPrice1(wws.getPrice1());
				wom.setPrice2(wws.getPrice2());
				wom.setPrice3(wws.getPrice3());
			}
			wom.setTotal(wom.getPrice1().multiply(new BigDecimal(wom.getQuantity())));
			workOrderMaterialMapper.insert(wom);
			if (wom.getTotal() != null) {
				materialTotal = materialTotal.add(wom.getTotal());
			}
		}
		info.setBillMaterialTotal(materialTotal);
		workRepairMapper.updateByPrimaryKey(info);
	}

	@Override
	public int deleteWorkHourByKey(String whId) {
		return workHourMapper.deleteByPrimaryKey(whId);
	}

	@Override
	public int deleteMaterialByKey(String womId) {
		return workOrderMaterialMapper.deleteByPrimaryKey(womId);
	}

	@Override
	public String findOrderListAll(String carNo, String userId, Integer dutyType, Integer pageIndex, Integer pageSize) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(userId) || StringUtil.isBlank(dutyType)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数【userId】和【dutyType】不能为空！");
			return result.toString();
		}
		if (StringUtil.isBlank(carNo)) {
			carNo = "";
		}
		if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
			// 服务主管查询接车单
			result.put("data", workQueryMapper.serveSupervisorQueryAllOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			// 服务顾问查询接车单
			result.put("data", workQueryMapper.consultantQueryAllOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WORKSHOP_LEADER) {
			// 车间主管查询
			result.put("data", workQueryMapper.shopSupervisorQueryAllOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_LEADER) {
			// 洗车组长查询
			result.put("data", workQueryMapper.washLeaderQueryAllOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		}

		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String findOrderListUndone(String carNo, String userId, Integer dutyType, Integer pageIndex,
			Integer pageSize) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(userId) || StringUtil.isBlank(dutyType)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数【userId】和【dutyType】不能为空！");
			return result.toString();
		}
		if (StringUtil.isBlank(carNo)) {
			carNo = "";
		}
		if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
			// 服务主管查询接车单
			result.put("data", workQueryMapper.serveSupervisorQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			// 服务顾问查询接车单
			result.put("data", workQueryMapper.consultantQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WORKSHOP_LEADER) {
			// 车间主管查询
			result.put("data", workQueryMapper.shopSupervisorQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_LEADER) {
			// 洗车组长查询
			result.put("data", workQueryMapper.washLeaderQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_DIVISION) {
			//洗车师
			result.put("data", workQueryMapper.washEngineerQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_TECHNICIAN) {
			//技师
			result.put("data", workQueryMapper.repairEngineerQueryUndoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		}

		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String findOrderListDone(String carNo, String userId, Integer dutyType, Integer pageIndex,
			Integer pageSize) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(userId) || StringUtil.isBlank(dutyType)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数【userId】和【dutyType】不能为空！");
			return result.toString();
		}
		if (StringUtil.isBlank(carNo)) {
			carNo = "";
		}
		if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
			// 服务主管查询接车单
			result.put("data", workQueryMapper.serveSupervisorQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			// 服务顾问查询接车单
			result.put("data", workQueryMapper.consultantQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WORKSHOP_LEADER) {
			// 车间主管查询
			result.put("data", workQueryMapper.shopSupervisorQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_LEADER) {
			// 洗车组长查询
			result.put("data", workQueryMapper.washLeaderQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_DIVISION) {
			//洗车师
			result.put("data", workQueryMapper.washEngineerQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		} else if (dutyType == BaseUser.DUTY_TECHNICIAN) {
			//技师
			result.put("data", workQueryMapper.repairEngineerQueryDoneOrder(userId, carNo,
					PageObject.getStart(pageIndex, pageSize), pageSize));
		}

		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getReapirDetailByKey(String repairId) {
		JSONObject result = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(repairId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "维修单不存在！");
			return result.toString();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("repairDetail", getRepairDetail(info));
		map.put("workHour", workHourMapper.selectByOrderId(repairId));
		map.put("material", workOrderMaterialMapper.selectByOrderId(repairId));

		result.put("data", map);
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getWashDetailByKey(String washId) {
		JSONObject result = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(washId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "洗车单不存在！");
			return result.toString();
		}

		result.put("data", getWashDetail(info));
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getInServeReapirDetailByCarNo(String carNo, String userId) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(carNo) || StringUtil.isBlank(userId)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数不能为空！");
			return result.toString();
		}
		WorkRepair info = workRepairMapper.getInServeByCarNo(carNo, userId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("repairIsNull", Constants.YES); // 维修单为空
			result.put("msg", "没有您正在服务的维修单！");
			return result.toString();
		}
		result.put("data", getRepairDetail(info)); // 详情
		result.put("repairIsNull", Constants.NO);// 维修单不为空
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getInServeWashDetailByCarNo(String carNo, String userId) {
		JSONObject result = new JSONObject();
		WorkWash info = workWashMapper.getInServeByCarNo(carNo, userId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "没有您正在服务的洗车单！");
			return result.toString();
		}

		result.put("data", getWashDetail(info)); // 详情对象
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getPickupOrderDetailByKey(String wpoId) {
		JSONObject result = new JSONObject();
		Map<String, Object> info = workPickupOrderMapper.selectDetailByPrimaryKey(wpoId);
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "接车单不存在！");
			return result.toString();
		}
		CarInfo carInfo = carInfoMapper.selectByCarNo(info.get("carNo").toString());
		log.info("获取接车单详情-查询车辆信息：【" + info.get("carNo").toString() + "】");
		if (carInfo != null) {
			WorkReservationOrder wro = iReservationService.selectUndoneByCarNo(carInfo.getCarNo());
			info.put("reservation", wro == null ? Constants.NULL : wro); // 预约单
			CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(carInfo.getCustomerId());
			info.put("headicon", cus != null ? cus.getHeadicon() : Constants.NULL); // 头像
			info.put("recommendCode", cus != null ? cus.getRecommendCode() : Constants.NULL); // 推荐人code
			log.info("获取接车单详情-查询车辆等级：【" + carInfo.getAbId() + "】");
			info.put("vipLevel", StringUtil.isBlank(carInfo.getAbId()) ? "普通"
					: accountBenefitMapper.selectByPrimaryKey(carInfo.getAbId()).getBenefitName());
			info.put("carVin", StringUtil.isBlank(carInfo.getCarVin()) ? Constants.NULL : carInfo.getCarVin());
			info.put("carTypeInfo",
					StringUtil.isBlank(carInfo.getCarTypeInfo()) ? Constants.NULL : carInfo.getCarTypeInfo()); // 车型

			// 使用的服务类和汽车类商品
			if (info.containsKey("orderDetailIds")) {
				List<Map<String, String>> serviceProduct = orderDetailMapper
						.selectByIds(info.get("orderDetailIds").toString().split(","));
				info.put("useServiceProducts", serviceProduct.size() == 0 ? Constants.NULL : serviceProduct);
				info.put("isUseProduct", serviceProduct.size() == 0 ? Constants.NO : Constants.YES);
			} else {
				info.put("useServiceProducts", Constants.NULL);
				info.put("isUseProduct", Constants.NO);
			}

			// 如果接车单未开始服务，则返回购买的商品
			if (Integer.valueOf(info.get("status").toString()) < 2) {
				List<Map<String, String>> serviceProduct = orderDetailMapper
						.selectWaitReceiveServiceOrder(carInfo.getCustomerId());
				info.put("serviceProducts", serviceProduct.size() == 0 ? Constants.NULL : serviceProduct);
				info.put("isHaveProduct", serviceProduct.size() == 0 ? Constants.NO : Constants.YES);
			}

		} else {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "车辆不存在或者车辆信息已删除！");
			return result.toString();
		}

		result.put("data", info);
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String clientFindRepairServiceOrder(Integer pageIndex, Integer pageSize, Integer payStatus, String orderNo,
			Integer status, String consultantName, String supervisorName) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = workQueryMapper.clientFindRepairServiceOrderTotal(payStatus, orderNo, status, consultantName,
				supervisorName);
		List<Map<String, Object>> list = workQueryMapper.clientFindRepairServiceOrderPage(page.getStart(),
				page.getPageSize(), payStatus, orderNo, status, consultantName, supervisorName);

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String clientFindWashServiceOrder(Integer pageIndex, Integer pageSize, Integer payStatus, String orderNo,
			Integer status, String consultantName, String supervisorName) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = workQueryMapper.clientFindWashServiceOrderTotal(payStatus, orderNo, status, consultantName,
				supervisorName);
		List<Map<String, Object>> list = workQueryMapper.clientFindWashServiceOrderPage(page.getStart(),
				page.getPageSize(), payStatus, orderNo, status, consultantName, supervisorName);

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public List<WorkOrderMaterial> findMaterialByRepairId(String wrId) {
		return workOrderMaterialMapper.selectByOrderId(wrId);
	}

	@Override
	public WorkRepair getInServeRepairByCarNo(String carNo) {
		List<WorkRepair> list = workRepairMapper.findInServeRepairByCarNo(carNo);
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 构建维修单详情对象
	 * 
	 * @param info
	 * @return
	 */
	private JSONObject getRepairDetail(WorkRepair info) {
		JSONObject data = new JSONObject();
		// 获取业务逻辑单对象
		WorkOperation wop = workOperationMapper.selectByReceiveOrder(info.getRepairSupervisor(), info.getWrId());
		if (wop != null) {
			data.put("assignDate", wop.getAllocationDate());// 派单时间
			data.put("receiveDate", wop.getReceiveDate() == null ? new Date() : wop.getReceiveDate());// 接单时间
		} else {
			data.put("assignDate", Constants.NULL);// 派单时间
			data.put("receiveDate", Constants.NULL);// 接单时间
		}
		data.put("completeDate", info.getStatus() < WorkRepair.STATUS_COMPLETE ? Constants.NULL : info.getModifyDate()); // 完成时间
		CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(info.getCustomerId());
		if (cus != null) {
			data.put("customerName", cus.getRealname());
		}
		data.put("isChildren", StringUtil.isNotBlank(info.getParentId())); // 是否子订单
		data.put("wrId", info.getWrId());
		data.put("repairNo", info.getWorkRepairNo());
		data.put("status", info.getStatus());
		data.put("carNo", info.getCarNo());
		data.put("total", info.getTotal() == null ? Constants.NULL : info.getTotal()); // 总价
		data.put("opinion", StringUtil.isBlank(info.getRemarks()) ? Constants.NULL : info.getRemarks()); // 备注：客户意见
		data.put("kilometre", StringUtil.isBlank(info.getKilometre()) ? Constants.NULL : info.getKilometre());
		data.put("bunkers", StringUtil.isBlank(info.getBunkers()) ? Constants.NULL : info.getBunkers());
		data.put("description", StringUtil.isBlank(info.getDescription()) ? Constants.NULL : info.getDescription()); // 客户描述
		data.put("inspectionItem", info.getInspectionItem());
		data.put("pic", workCarPicMapper.selectByWorkOrderId(info.getWrId())); // 车辆状态图片
		data.put("linkmanMobile",
				StringUtil.isBlank(info.getLinkmanMobile()) ? Constants.NULL : info.getLinkmanMobile());
		data.put("consultant", baseUserMapper.selectNameById(info.getConsultantBy())); // 顾问
		data.put("supervisor", baseUserMapper.selectNameById(info.getRepairSupervisor())); // 主管
		data.put("enginner", baseUserMapper.selectNameById(info.getSupervisorBy()));

		// 评价星级
		List<WorkEvaluate> evaluate = workEvaluateMapper.selectByEvaluateNo(info.getWorkRepairNo());
		if (evaluate.size() > 0) {
			data.put("evaluation", evaluate.get(0).getEvaluateConsultant()); // 评价顾问
			data.put("evaluationEngineer", evaluate.get(0).getEvaluateSupervisor()); // 评价技师
			data.put("evaluationContent", evaluate.get(0).getEvaluateInfo());
		} else {
			data.put("evaluation", Constants.INT_0);
			data.put("evaluationEngineer", Constants.INT_0); // 评价技师
			data.put("evaluationContent", Constants.NULL);
		}

		// 使用的服务类和汽车类商品
		WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
		if (StringUtil.isNotBlank(wpo.getOrderDetailIds())) {
			List<Map<String, String>> serviceProduct = orderDetailMapper
					.selectByIds(wpo.getOrderDetailIds().split(","));
			data.put("useServiceProducts", serviceProduct.size() == 0 ? Constants.NULL : serviceProduct);
			data.put("isUseProduct", serviceProduct.size() == 0 ? Constants.NO : Constants.YES);
		} else {
			data.put("useServiceProducts", Constants.NULL);
			data.put("isUseProduct", Constants.NO);
		}

		// 车辆信息
		CarInfo carInfo = carInfoMapper.selectByCarNo(info.getCarNo());
		if (carInfo != null) {
			AccountBenefit ab = accountBenefitMapper.selectByPrimaryKey(carInfo.getAbId());
			data.put("vipLevel", ab == null ? Constants.NULL : ab.getBenefitName());
			data.put("carVin", StringUtil.isBlank(carInfo.getCarVin()) ? Constants.NULL : carInfo.getCarVin());
			data.put("carTypeInfo",
					StringUtil.isBlank(carInfo.getCarTypeInfo()) ? Constants.NULL : carInfo.getCarTypeInfo()); // 车型
		} else {
			data.put("vipLevel", Constants.NULL);
			data.put("carVin", Constants.NULL);
			data.put("carTypeInfo", Constants.NULL); // 车型
		}

		// 工时费用信息
		StringBuffer workHours = new StringBuffer();
		List<WorkHour> list = workHourMapper.selectByOrderId(info.getWrId());
		for (WorkHour wh : list) {
			workHours.append(wh.getManHourName()).append(",");
		}
		String itemNames = workHours.toString();
		data.put("itemNames", itemNames.length() > 0 ? itemNames.subSequence(0, itemNames.length() - 1) : itemNames);
		return data;
	}

	/**
	 * 构建洗车单详情对象
	 * 
	 * @param info
	 * @param carInfo
	 * @return
	 */
	private JSONObject getWashDetail(WorkWash info) {
		JSONObject data = new JSONObject();
		// 返回数据
		// 获取业务逻辑单对象
		WorkOperation wop = workOperationMapper.selectByReceiveOrder(info.getGroupLeader(), info.getWwId());
		if (wop != null) {
			data.put("assignDate", wop.getAllocationDate());// 派单时间
			data.put("receiveDate", wop.getReceiveDate() == null ? new Date() : wop.getReceiveDate());// 接单时间
			data.put("completeDate",
					info.getStatus() < WorkWash.STATUS_COMPLETE ? Constants.NULL : wop.getModifyDate()); // 完成时间
		} else {
			data.put("assignDate", Constants.NULL);// 派单时间
			data.put("receiveDate", Constants.NULL);// 接单时间
			data.put("completeDate", Constants.NULL); // 完成时间
		}
		CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(info.getCustomerId());
		if (cus != null) {
			data.put("customerName", cus.getRealname());
			data.put("recommendCode",
					StringUtil.isBlank(cus.getRecommendCode()) ? Constants.NULL : cus.getRecommendCode()); // 推荐人帐号
			data.put("headicon", StringUtil.isBlank(cus.getHeadicon()) ? Constants.NULL : cus.getHeadicon()); // 头像
		} else {
			data.put("customerName", Constants.NULL);
			data.put("recommendCode", Constants.NULL); // 推荐人帐号
			data.put("headicon", Constants.NULL); // 头像
		}
		data.put("washId", info.getWwId());
		data.put("washNo", info.getWorkWashNo());
		data.put("status", info.getStatus());
		data.put("washType", info.getWashType()); // 类型
		data.put("washTypeStr", info.getWashTypeStr()); // 类型
		data.put("washCost", info.getWashCost() == null ? Constants.NULL : info.getWashCost()); // 总价
		data.put("tipTotal", info.getTipTotal() == null ? Constants.NULL : info.getTipTotal()); // 打赏
		data.put("opinion", StringUtil.isBlank(info.getDescription()) ? Constants.NULL : info.getDescription());// 备注：客户意见
		data.put("carNo", info.getCarNo());

		// 车辆信息
		CarInfo carInfo = carInfoMapper.selectByCarNo(info.getCarNo());
		if (carInfo != null) {
			AccountBenefit ab = accountBenefitMapper.selectByPrimaryKey(carInfo.getAbId());
			data.put("vipLevel", ab == null ? Constants.NULL : ab.getBenefitName());
			data.put("carVin", StringUtil.isBlank(carInfo.getCarVin()) ? Constants.NULL : carInfo.getCarVin());
			data.put("carTypeInfo",
					StringUtil.isBlank(carInfo.getCarTypeInfo()) ? Constants.NULL : carInfo.getCarTypeInfo()); // 车型
		} else {
			data.put("vipLevel", Constants.NULL);
			data.put("carVin", Constants.NULL);
			data.put("carTypeInfo", Constants.NULL); // 车型
		}

		// 评价星级
		List<WorkEvaluate> evaluate = workEvaluateMapper.selectByEvaluateNo(info.getWorkWashNo());
		if (evaluate.size() > 0) {
			data.put("evaluation", evaluate.get(0).getEvaluateConsultant()); // 取对顾问的评价
			data.put("evaluationEngineer", evaluate.get(0).getEvaluateWash()); // 评价洗车师
			data.put("evaluationContent", evaluate.get(0).getEvaluateInfo());
		} else {
			data.put("evaluation", Constants.INT_0);
			data.put("evaluationEngineer", Constants.INT_0); // 评价洗车师
			data.put("evaluationContent", Constants.NULL);
		}

		// 服务类和汽车类商品
		WorkPickupOrder wpo = workPickupOrderMapper.selectByNo(info.getWpoNo());
		if (StringUtil.isNotBlank(wpo.getOrderDetailIds())) {
			List<Map<String, String>> serviceProduct = orderDetailMapper
					.selectByIds(wpo.getOrderDetailIds().split(","));
			data.put("useServiceProducts", serviceProduct.size() == 0 ? Constants.NULL : serviceProduct);
			data.put("isUseProduct", serviceProduct.size() == 0 ? Constants.NO : Constants.YES);
		} else {
			data.put("useServiceProducts", Constants.NULL);
			data.put("isUseProduct", Constants.NO);
		}

		data.put("consultant", baseUserMapper.selectNameById(info.getConsultantBy())); // 顾问
		data.put("supervisor", baseUserMapper.selectNameById(info.getGroupLeader())); // 组长
		data.put("enginner", baseUserMapper.selectNameById(info.getSupervisorBy()));
		return data;
	}

	@Override
	public String clientRepairPay(JSONObject param) {
		JSONObject result = new JSONObject();
		WorkRepair info = workRepairMapper.selectByPrimaryKey(param.getString("wrId"));
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "维修单不存在！");
			return result.toString();
		}
		info.setPayWay(param.getInteger("payWay"));
		info.setPayTotal(param.getBigDecimal("payTotal"));
		info.setPayStatus(1);// 已支付
		// 更新维修单状态：已支付
		info.setStatus(WorkRepair.STATUS_ALREADY_PAY);
		workRepairMapper.updateByPrimaryKey(info);

		// 保存订单支付流水记录
		WorkPayInfo payInfo = new WorkPayInfo();
		payInfo.setPayWay(info.getPayWay());
		payInfo.setDescription(param.getString("remarks"));
		payInfo.setPayTotal(info.getPayTotal());
		payInfo.setPayType(WorkPayInfo.PAY_TYPE_REPAIR);
		payInfo.setWorkId(info.getWrId());
		payInfo.setPayResult(Constants.INT_1);
		workPayInfoMapper.insert(payInfo);

		// 推送消息至车间组长
		String title = String.format("维修单号：%s", info.getWorkRepairNo());
		String content = String.format("【%s】 客户已确认维修项目及材料费用，请您尽快开始维修！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()),
				info.getRepairSupervisor());

		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "支付成功");
		return result.toString();
	}

	@Override
	public String clientWashPay(JSONObject param) {
		JSONObject result = new JSONObject();
		WorkWash info = workWashMapper.selectByPrimaryKey(param.getString("wwId"));
		if (info == null) {
			result.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			result.put("msg", "洗车单不存在！");
			return result.toString();
		}
		info.setPayWay(param.getInteger("payWay"));
		info.setPayTotal(param.getBigDecimal("payTotal"));
		info.setPayStatus(1);// 已支付
		// 更新维修单状态：已支付
		info.setStatus(WorkWash.STATUS_ALREADY_PAY);
		workWashMapper.updateByPrimaryKey(info);

		// 保存订单支付流水记录
		WorkPayInfo payInfo = new WorkPayInfo();
		payInfo.setPayWay(info.getPayWay());
		payInfo.setDescription(param.getString("remarks"));
		payInfo.setPayTotal(info.getPayTotal());
		payInfo.setPayType(WorkPayInfo.PAY_TYPE_WASH);
		payInfo.setWorkId(info.getWwId());
		payInfo.setPayResult(Constants.INT_1);
		workPayInfoMapper.insert(payInfo);

		// 推送至洗车组长
		String title = String.format("洗车单号：%s", info.getWorkWashNo());
		String content = String.format("【%s】 客户已确认洗车项目及费用，请您尽快开始洗车！", info.getCarNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()),
				info.getGroupLeader());

		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "支付成功");
		return result.toString();
	}

	@Override
	public void saveCarPic(String wrId, String paths) {
		if (StringUtil.isNotBlank(wrId) && StringUtil.isNotBlank(paths)) {
			WorkCarPic info = null;
			for (String path : paths.split(",")) {
				if (path.contains("image") && path.contains(".")) {
					info = new WorkCarPic();
					info.setPicUrl(path);
					info.setWorkOrderId(wrId);
					workCarPicMapper.insert(info);
				}
			}
		}
	}

	@Override
	public String customerFindRepairOrderList(String customerId, Integer type, Integer pageIndex, Integer pageSize) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(customerId) || StringUtil.isBlank(type)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数不能为空！");
			return result.toString();
		}
		result.put("data", workQueryMapper.customerFindRepairOrderList(customerId, type,
				PageObject.getStart(pageIndex, pageSize), pageSize));
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String customerFindWashOrderList(String customerId, Integer type, Integer pageIndex, Integer pageSize) {
		JSONObject result = new JSONObject();
		if (StringUtil.isBlank(customerId) || StringUtil.isBlank(type)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "参数不能为空！");
			return result.toString();
		}
		result.put("data", workQueryMapper.customerFindWashOrderList(customerId, type,
				PageObject.getStart(pageIndex, pageSize), pageSize));
		result.put("apiStatus", ResultCode.SUCC.getValue());
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 后台：维修单删除
	 */
	@Override
	public String clientDeleteRepairOrder(String wrId) {
		JSONObject result = new JSONObject();
		if (StringUtils.isBlank(wrId)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "wrId为空");
			return result.toString();
		}
		workRepairMapper.deleteByPrimaryKey(wrId);
		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "删除成功");
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 后台：洗车单删除
	 */
	@Override
	public String clientDeleteWashOrder(String wwId) {
		JSONObject result = new JSONObject();
		if (StringUtils.isBlank(wwId)) {
			result.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			result.put("msg", "wwId为空");
			return result.toString();
		}
		workWashMapper.deleteByPrimaryKey(wwId);
		result.put("apiStatus", ResultCode.SUCC.getValue());
		result.put("msg", "删除成功");
		return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 后台：根据维修单主键查询维修单详情
	 */
	@Override
	public CommonResult clientFindRepairOrderDetailByWrId(String wrId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, String> repairOrder = workRepairMapper.selectRepairOrderDetailByPrimaryKey(wrId);
		map.put("repairOrder", repairOrder);
		List<WorkOrderMaterial> orderMaterialList = workOrderMaterialMapper.selectByOrderId(wrId);
		map.put("orderMaterialList", orderMaterialList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 后台：根据洗车单主键查询洗车单详情
	 */
	@Override
	public CommonResult clientFindWashOrderDetaileByWwId(String wwId) {
		Map<String, String> washOrder = workWashMapper.selectWashOrderDetailByPrimaryKey(wwId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", washOrder);
	}

	@Override
	public CommonResult selectWaitReceiveServiceOrderByCarNo(String carNo) {
		CarInfo car = carInfoMapper.selectByCarNo(carNo);
		if (car == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "车辆信息不存在");
		}
		List<Map<String, String>> list = orderDetailMapper.selectWaitReceiveServiceOrder(car.getCustomerId());
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", list);
	}

	@Override
	public CommonResult repairReceiveUseProducts(String wpoId, String orderDetailIds) {
		log.info("保存维修接车单的使用商品信息：wpoId{}，orderDetailIds{}", wpoId, orderDetailIds);
		WorkPickupOrder wpo = workPickupOrderMapper.selectByPrimaryKey(wpoId);
		if (wpo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "接车单信息不存在");
		}
		wpo.setOrderDetailIds(orderDetailIds);
		workPickupOrderMapper.updateByPrimaryKey(wpo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * PC：客户查询维修单列表
	 */
	@Override
	public CommonResult websiteFindRepairOrderList(String customerId, Integer type, Integer pageIndex,
			Integer pageSize) {
		if (StringUtils.isBlank(customerId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "尚未登录");
		}
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);
		int total = workQueryMapper.selectRepairOrderCount(customerId, type);
		page.setRows(total);
		List<Map<String, Object>> list = workQueryMapper.customerFindRepairOrderList(customerId, type, page.getStart(),
				page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * PC：客户查询洗车单列表
	 */
	@Override
	public CommonResult websiteFindWashOrderList(String customerId, Integer type, Integer pageIndex, Integer pageSize) {
		if (StringUtils.isBlank(customerId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "尚未登录");
		}
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);
		int total = workQueryMapper.selectWashOrderCount(customerId, type);
		page.setRows(total);
		List<Map<String, Object>> list = workQueryMapper.customerFindWashOrderList(customerId, type, page.getStart(),
				page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	@Override
	public void exportRepairServiceOrder(HttpServletRequest request, HttpServletResponse response) {
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("维修单数据");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short)20);
		//生成一个样式  
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);//样式字体水平居中
		//设置前景填充色
	    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
	    //设置前景填充样式
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		//给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("维修单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);
        cell.setCellValue("车牌号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4);  
        cell.setCellValue("服务顾问");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("维修师");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("下单时间");  
        cell.setCellStyle(style);

        Integer payStatus=request.getParameter("payStatus")==""||request.getParameter("payStatus")==null?null:Integer.parseInt(request.getParameter("payStatus"));
        String orderNo=request.getParameter("orderNo");
        Integer status=request.getParameter("status")==""||request.getParameter("status")==null?null:Integer.parseInt(request.getParameter("status"));
        String consultantName=request.getParameter("consultantName");
		String supervisorName=request.getParameter("supervisorName");
		try {
			//解决中文乱码
			consultantName=URLDecoder.decode(consultantName, "UTF-8");
			supervisorName=URLDecoder.decode(supervisorName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("参数转换异常", e);
		}
        
        List<Map<String,String>> repairList=workQueryMapper.exportRepairServiceOrder(payStatus, orderNo, status, consultantName, supervisorName);
		
        if(!repairList.isEmpty()){
        	for (short i = 0; i < repairList.size(); i++){
    			row = sheet.createRow(i + 1);
    			row.createCell(0).setCellValue(repairList.get(i).get("repairNo") == null ? "" : repairList.get(i).get("repairNo"));
    			row.createCell(1).setCellValue(repairList.get(i).get("realname") == null ? "" : repairList.get(i).get("realname"));
    			row.createCell(2).setCellValue(repairList.get(i).get("mobile") == null ? "" : repairList.get(i).get("mobile"));
    			row.createCell(3).setCellValue(repairList.get(i).get("carNo") == null ? "" : repairList.get(i).get("carNo"));
    			row.createCell(4).setCellValue(repairList.get(i).get("consultantName") == null ? "" : repairList.get(i).get("consultantName"));
    			row.createCell(5).setCellValue(repairList.get(i).get("supervisorName") == null ? "" : repairList.get(i).get("supervisorName"));
    			row.createCell(6).setCellValue(repairList.get(i).get("receiveDate") == null ? "" : repairList.get(i).get("receiveDate"));
    		}
        }
		
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName=new String(("维修单信息表"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			
			OutputStream out=response.getOutputStream();
		    wb.write(out);
		    out.close();
		    wb.close();
		    log.info("导出维修单信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出维修单信息失败", e); 
		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("导出维修单信息失败", e);
		}
	}

	@Override
	public void exportWashServiceOrder(HttpServletRequest request, HttpServletResponse response) {
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("洗车单数据");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short)20);
		//生成一个样式  
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);//样式字体水平居中
		//设置前景填充色
	    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
	    //设置前景填充样式
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		//给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("洗车单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);
        cell.setCellValue("车牌号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4);  
        cell.setCellValue("服务顾问");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("洗车师");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("洗车类型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("下单时间");  
        cell.setCellStyle(style);

        Integer payStatus=request.getParameter("payStatus")==""||request.getParameter("payStatus")==null?null:Integer.parseInt(request.getParameter("payStatus"));
        String orderNo=request.getParameter("orderNo");
        Integer status=request.getParameter("status")==""||request.getParameter("status")==null?null:Integer.parseInt(request.getParameter("status"));
        String consultantName=request.getParameter("consultantName");
		String supervisorName=request.getParameter("supervisorName");
		try {
			//解决中文乱码
			consultantName=URLDecoder.decode(consultantName, "UTF-8");
			supervisorName=URLDecoder.decode(supervisorName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("参数转换异常", e);
		}
        
        List<Map<String,String>> washList=workQueryMapper.exportWashServiceOrder(payStatus, orderNo, status, consultantName, supervisorName);
		
        if(!washList.isEmpty()){
        	for (short i = 0; i < washList.size(); i++){
    			row = sheet.createRow(i + 1);
    			row.createCell(0).setCellValue(washList.get(i).get("washNo") == null ? "" : washList.get(i).get("washNo"));
    			row.createCell(1).setCellValue(washList.get(i).get("realname") == null ? "" : washList.get(i).get("realname"));
    			row.createCell(2).setCellValue(washList.get(i).get("mobile") == null ? "" : washList.get(i).get("mobile"));
    			row.createCell(3).setCellValue(washList.get(i).get("carNo") == null ? "" : washList.get(i).get("carNo"));
    			row.createCell(4).setCellValue(washList.get(i).get("consultantName") == null ? "" : washList.get(i).get("consultantName"));
    			row.createCell(5).setCellValue(washList.get(i).get("supervisorName") == null ? "" : washList.get(i).get("supervisorName"));
    			row.createCell(6).setCellValue(washList.get(i).get("washType") == null ? "" : WorkWash.getWashType(Integer.parseInt(washList.get(i).get("washType"))));
    			row.createCell(7).setCellValue(washList.get(i).get("receiveDate") == null ? "" : washList.get(i).get("receiveDate"));
    		}
        }
		
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName=new String(("洗车单信息表"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			
			OutputStream out=response.getOutputStream();
		    wb.write(out);
		    out.close();
		    wb.close();
		    log.info("导出洗车单信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出洗单信息失败", e); 
		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("导出洗单信息失败", e);
		}
	}
}
