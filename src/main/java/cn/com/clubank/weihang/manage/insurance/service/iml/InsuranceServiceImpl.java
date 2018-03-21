package cn.com.clubank.weihang.manage.insurance.service.iml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DataItemCode;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.dataItem.pojo.BaseDataitemDetail;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemDetailService;
import cn.com.clubank.weihang.manage.insurance.dao.InsuranceOrderMapper;
import cn.com.clubank.weihang.manage.insurance.dao.InsurancePicMapper;
import cn.com.clubank.weihang.manage.insurance.pojo.InsuranceOrder;
import cn.com.clubank.weihang.manage.insurance.pojo.InsurancePic;
import cn.com.clubank.weihang.manage.insurance.service.InsuranceService;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 保险管理
 * 
 * @author LeiQY
 *
 */
@Slf4j
@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceOrderMapper insuranceOrderMapper;
	@Autowired
	private InsurancePicMapper insurancePicMapper;
	@Autowired
	private BaseCodeRuleService baseCodeRuleService;
	@Autowired
	private CarInfoMapper carInfoMapper;
	@Autowired
	private IDataItemDetailService iDataItemDetailService;
	@Autowired
	private IMessageService iMessageService;

	/**
	 * 添加保险信息
	 */
	@Override
	public CommonResult memberAddInsuranceOrderInfo(JSONObject json) {
		if (json == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		log.info("添加保险信息数据：{}", json.toString());
		InsuranceOrder order = json.getObject("insuranceOrder", InsuranceOrder.class);
		JSONArray array = json.getJSONArray("insurancePicList");
		if (order == null || array == null || array.size() <= 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		order.setOrderNo(baseCodeRuleService.getCode(BaseCodeRule.INSURANCE_CODE));
		insuranceOrderMapper.insert(order);
		List<InsurancePic> list = JSON.parseArray(array.toJSONString(), InsurancePic.class);
		for (InsurancePic insurancePic : list) {
			insurancePic.setInsuranceId(order.getInsuranceId());
		}
		insurancePicMapper.insertBatch(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "提交成功");
	}

	/**
	 * 查询保险订单列表
	 */
	@Override
	public CommonResult memberFindInsuranceOrderList(String customerId, Integer status, Integer pageIndex,
			Integer pageSize) {
		PageObject<InsuranceOrder> page = new PageObject<>(pageIndex, pageSize);
		int total = insuranceOrderMapper.selectInsuranceOrderCount(customerId, status);
		page.setRows(total);
		List<InsuranceOrder> list = insuranceOrderMapper.selectInsuranceOrderList(customerId, status, page.getStart(),
				page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 保险订单取消
	 */
	@Override
	public CommonResult memberCancelInsuranceOrder(String insuranceId) {
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_8);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "取消成功");
	}

	/**
	 * 保险订单确认收货
	 */
	@Override
	public CommonResult memberConfirmReceiptInsuranceOrder(String insuranceId) {
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 订单状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_6);
		// 物流状态 1待发货 2已发货 3已签收
		insuranceOrder.setDeliveryStatus(Constants.INT_3);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "确认收货成功");
	}

	/**
	 * 查询保险订单详情
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CommonResult memberFindInsuranceOrderDetail(String insuranceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		map.put("insuranceOrder", insuranceOrder);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "保单信息不存在");
		}
		// 固定保险项
		String fixedAmount = insuranceOrder.getFixedAmount();
		List<String> selected = new ArrayList<>();
		Map<String, String> fixedMap = JSON.parseObject(fixedAmount, Map.class);
		for (Map.Entry<String, String> m : fixedMap.entrySet()) {
			String key = m.getKey();
			String value = m.getValue();
			if ("true".equalsIgnoreCase(value)) {
				CommonResult result = iDataItemDetailService
						.gainNameValue(DataItemCode.FIXED_INSURANCE_ITEM.getValue());
				if (result.getApiStatus() == ResultCode.SUCC.getValue()) {
					List<BaseDataitemDetail> data = (List) result.getData();
					for (BaseDataitemDetail bd : data) {
						if (key.equalsIgnoreCase(bd.getValue())) {
							selected.add(bd.getName());
						}
					}
				}
			}
		}
		map.put("fixedInsurance", selected);
		List<InsurancePic> insurancePicList = insurancePicMapper.selectByInsuranceId(insuranceId);
		map.put("insurancePicList", insurancePicList);
		Map<String, Object> carInfo = carInfoMapper
				.selectCarDetailByCarId(insuranceOrder == null ? "" : insuranceOrder.getCarId());
		map.put("carInfo", carInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 添加保险图片
	 */
	@Override
	public CommonResult memberAddInsurancePicList(JSONArray array) {
		if (array == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		log.info("添加保险订单图片：{}", array.toString());
		List<InsurancePic> list = JSON.parseArray(array.toJSONString(), InsurancePic.class);
		if (list == null || list.size() <= 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(list.get(0).getInsuranceId());
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "保单信息不存在");
		}
		insurancePicMapper.insertBatch(list);
		// 状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_3);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "添加成功");
	}

	/**
	 * 修改保险照片
	 */
	@Override
	public CommonResult memberModifyInsurancePic(InsurancePic pic) {
		if (pic == null || StringUtils.isBlank(pic.getPicId()) || StringUtils.isBlank(pic.getPicUrl())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		InsurancePic insurancePic = insurancePicMapper.selectByPrimaryKey(pic.getPicId());
		if (insurancePic == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		insurancePic.setPicUrl(pic.getPicUrl());
		insurancePicMapper.updateByPrimaryKey(insurancePic);
		return CommonResult.result(ResultCode.SUCC.getValue(), "修改成功");
	}

	/**
	 * 查看保单相关图片
	 */
	@Override
	public CommonResult memberFindInsurancePicByType(String insuranceId, Integer type) {
		if (StringUtils.isBlank(insuranceId) || type == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "保单信息不存在");
		}
		List<InsurancePic> picList = insurancePicMapper.selectByInsuranceId(insuranceId);
		if (picList == null || picList.size() <= 0) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "图片信息不存在");
		}
		List<InsurancePic> list = new ArrayList<>();
		for (InsurancePic insurancePic : picList) {
			if (insurancePic.getType() == type) {
				list.add(insurancePic);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", list);
	}

	/**
	 * 后台 ：查询保单列表
	 */
	@Override
	public CommonResult clinetFindInsuranceOrderList(String subTimeStart, String subTimeEnd, String companyName,
			Integer status, String subName, String carNo, Integer pageIndex, Integer pageSize) {
		PageObject<InsuranceOrder> page = new PageObject<>(pageIndex, pageSize);
		int total = insuranceOrderMapper.clientSelectInsuranceOrderCount(subTimeStart, subTimeEnd, companyName, status,
				subName, carNo);
		page.setRows(total);
		List<InsuranceOrder> list = insuranceOrderMapper.clientSelectInsuranceOrderList(subTimeStart, subTimeEnd,
				companyName, status, subName, carNo, page.getStart(), page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 后台：保险订单验车
	 */
	@Override
	public CommonResult clinetInsuranceOrderCheckCar(String insuranceId) {
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_2);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * 后台：保险订单驳回
	 */
	@Override
	public CommonResult clinetRejectInsuranceOrder(String insuranceId, String description) {
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_7);
		insuranceOrder.setDescription(description);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * 后台：添加保单图片
	 */
	@Override
	public CommonResult clinetAddInsuranceOrderPic(InsurancePic pic) {
		if (pic == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		insurancePicMapper.insert(pic);
		return CommonResult.result(ResultCode.SUCC.getValue(), "添加成功");
	}

	/**
	 * 后台：编辑保单价格并确认
	 */
	@Override
	public CommonResult clinetModifyInsuranceOrderPrice(String insuranceId, BigDecimal amount1, BigDecimal amount2,
			BigDecimal amount3) {
		if (StringUtils.isBlank(insuranceId) || amount1 == null || amount2 == null || amount3 == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "保单信息不存在");
		}
		// 普通
		insuranceOrder.setAmount1(amount1);
		// 蓝金
		insuranceOrder.setAmount2(amount2);
		// 蓝钻
		insuranceOrder.setAmount3(amount3);
		// 保单状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
		insuranceOrder.setStatus(Constants.INT_4);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * 保单发货
	 */
	@Override
	public CommonResult clinetDeliveryInsuranceOrder(String insuranceId) {
		if (StringUtils.isBlank(insuranceId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		InsuranceOrder insuranceOrder = insuranceOrderMapper.selectByPrimaryKey(insuranceId);
		if (insuranceOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "保单信息不存在");
		}
		// 发货时间
		insuranceOrder.setDeliveryTime(new Date());
		// 发货状态
		insuranceOrder.setDeliveryStatus(Constants.INT_2);
		insuranceOrderMapper.updateSelectiveByPrimaryKey(insuranceOrder);

		// 消息推送
		String title = String.format("保单发货通知");
		String content = String.format("【%s】保险订单已发货，请您注意查收 。", insuranceOrder.getOrderNo());
		iMessageService.pushMessage(
				new MsgList(MsgList.MSGTYPE_INSURANCE, title, content, insuranceOrder.getInsuranceId()),
				insuranceOrder.getSubId());

		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * 定时任务：处理保单状态(待付款状态三天内未支付，订单状态修改为已取消，已发货保单十天内未确认，订单状态变更为已完成)
	 */
	@Override
	public void handleInsuranceOrderStatus() {
		// 待付款状态三天内未支付，订单状态修改为已取消
		insuranceOrderMapper.handleDelayedPayOrderStatus();
		// 已发货保单十天内未确认收货，订单状态变更为已完成
		insuranceOrderMapper.handleUnconfirmedReceiptOrderStatus();
	}
}
