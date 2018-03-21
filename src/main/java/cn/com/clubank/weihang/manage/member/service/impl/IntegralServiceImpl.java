package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.BaseParameterCode;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.IntegralRecordMapper;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.pojo.IntegralRecord;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductIntegralMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseParameterService;
import cn.com.clubank.weihang.manage.systemSettings.service.IntegralRuleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分管理Service
 * 
 * @author LeiQY
 *
 */
@Service
@Slf4j
public class IntegralServiceImpl implements IntegralService {

	@Autowired
	private CustomerInfoMapper customerInfoMapper;
	@Autowired
	private IntegralRecordMapper integralRecordMapper;
	@Autowired
	private CouponInventoryMapper couponInventoryMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private IntegralRuleService integralRuleService;
	@Autowired
	private BaseCodeRuleService baseCodeRuleService;
	@Autowired
	private ProductIntegralMapper productIntegralMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private BaseParameterService baseParameterService;
	@Resource
	private IMessageService iMessageService;

	/**
	 * 日常任务可获得总积分数
	 */
	private Integer getTotalIntegral() {
		String value = baseParameterService.getValueByKeyCode(BaseParameterCode.DAILY_TASK_TOTAL_INTEGRAL.getValue());
		if (value != null) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	/**
	 * 会员分享增加积分
	 */
	@Override
	public CommonResult share(String customerId) {
		// 积分来源方式 0兑换 1签到 2分享 3消费 4充值 5推荐 6注册 7广告
		Integer integralSourceType = IntegralRecord.SHARE;
		// 积分变化类型 1增加 2减少
		Integer integralType = IntegralRecord.INCREASE;
		// 分享积分数量
		Integer dealIntegral = Constants.INT_0;
		Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.SHARE_INTEGRAL);
		if (value != null) {
			dealIntegral = value;
		}
		if (dealIntegral > 0) {
			Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, integralSourceType);
			// 一天限制5次
			if (count < IntegralRule.DailyTask.SHARE_TIME.getValue()) {
				updateIntegral(customerId, integralSourceType, integralType, dealIntegral);
			}
			// 完成分享任务赠送额外积分
			count = integralRecordMapper.selectCountIntegralRecordByType(customerId, integralSourceType);
			// 判断是否已添加日常任务积分
			int daily = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.SHARE_DAILY);
			if (count == IntegralRule.DailyTask.SHARE_TIME.getValue() && daily == 0) {
				updateIntegral(customerId, IntegralRecord.SHARE_DAILY, integralType, getTotalIntegral() / 5);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "分享并赠送积分成功", customerInfoMapper.selectByPrimaryKey(customerId).getRecommendCode());
	}

	/**
	 * 会员充值赠送积分
	 */
	@Override
	public void rechargeIntegral(String customerId, BigDecimal total) {
		if (StringUtils.isBlank(customerId) || total == null) {
			return;
		}
		Integer integral = 0;
		Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.RECHARGE_INTEGRAL);
		Integer baseValue = integralRuleService.getBaseRuleValueByRuleCode(IntegralRule.RECHARGE_INTEGRAL);
		if (value != null && baseValue != null) {
			BigDecimal divide = total.divide(new BigDecimal(baseValue)).multiply(new BigDecimal(value));
			integral = divide.intValue();
		}
		if (integral == 0) {
			return;
		}
		updateIntegral(customerId, IntegralRecord.RECHARGE, IntegralRecord.INCREASE, integral);
	}

	/**
	 * 消费带积分的商品为客户增加积分
	 */
	@Override
	public void productIntegral(String customerId, String orderId) {
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(orderId)) {
			return;
		}
		List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
		if (orderDetailList == null || orderDetailList.size() <= 0) {
			return;
		}
		Integer integral = 0;
		for (OrderDetail orderDetail : orderDetailList) {
			ProductIntegral productIntegral = productIntegralMapper.selectBySkuId(orderDetail.getSkuId());
			if (productIntegral != null && productIntegral.getStatus() == 1) {
				// 增加积分
				integral += productIntegral.getIntegralValue();
			}
		}
		if (integral == 0) {
			return;
		}
		updateIntegral(customerId, IntegralRecord.CONSUME, IntegralRecord.INCREASE, integral);
		Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.CONSUME);
		// 判断是否已添加日常任务积分
		int daily = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.BUY_DAILY);
		// 完成购买任务赠送额外积分
		if (count == IntegralRule.DailyTask.CONSUME_TIME.getValue() && daily == 0) {
			updateIntegral(customerId, IntegralRecord.BUY_DAILY, IntegralRecord.INCREASE, getTotalIntegral() / 5);
		}
	}

	/**
	 * 推荐成功注册后为客户增加积分
	 */
	@Override
	public void recommendIntegral(String customerId) {
		if (StringUtils.isBlank(customerId)) {
			return;
		}
		Integer integral = 0;
		Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.RECOMMENDRIGISTER_INTEGRAL);
		if (value != null) {
			integral = value;
		}
		if (integral == 0) {
			return;
		}
		Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.RECOMMEND);
		// 一天限制5次
		if (count < IntegralRule.DailyTask.RECOMMEND_TIME.getValue()) {
			updateIntegral(customerId, IntegralRecord.RECOMMEND, IntegralRecord.INCREASE, integral);
		}
		// 完成推荐任务赠送额外积分
		count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.RECOMMEND);
		// 判断是否已添加日常任务积分
		int daily = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.RECOMMEND_DAILY);
		if (count == IntegralRule.DailyTask.RECOMMEND_TIME.getValue() && daily == 0) {
			updateIntegral(customerId, IntegralRecord.RECOMMEND_DAILY, IntegralRecord.INCREASE, getTotalIntegral() / 5);
		}
	}

	/**
	 * 签到赠送积分
	 */
	@Override
	public void signIntegral(String customerId, Integer integral) {
		updateIntegral(customerId, IntegralRecord.SIGN, IntegralRecord.INCREASE, integral);
		Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.SIGN);
		// 判断是否已添加日常任务积分
		int daily = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.SIGN_DAILY);
		// 完成签到任务赠送额外积分
		if (count == IntegralRule.DailyTask.SIGN_TIME.getValue() && daily == 0) {
			updateIntegral(customerId, IntegralRecord.SIGN_DAILY, IntegralRecord.INCREASE, getTotalIntegral() / 5);
		}
	}

	/**
	 * 点击广告为客户增加积分
	 */
	@Override
	public void advertIntegral(String customerId) {
		if (StringUtils.isBlank(customerId)) {
			return;
		}
		Integer integral = 0;
		Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.ADVERT_INTEGRAL);
		if (value != null) {
			integral = value;
		}
		if (integral == 0) {
			return;
		}
		Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.ADVERT);
		// 一天限制5次
		if (count < IntegralRule.DailyTask.ADVERT_TIME.getValue()) {
			updateIntegral(customerId, IntegralRecord.ADVERT, IntegralRecord.INCREASE, integral);
		}
		// 完成点击广告任务赠送额外积分
		count = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.ADVERT);
		// 判断是否已添加日常任务积分
		int daily = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.ADVERT_DAILY);
		if (count == IntegralRule.DailyTask.ADVERT_TIME.getValue() && daily == 0) {
			updateIntegral(customerId, IntegralRecord.ADVERT_DAILY, IntegralRecord.INCREASE, getTotalIntegral() / 5);
		}
	}

	/**
	 * 新增积分流水信息并修改客户积分账户
	 * 
	 * @param customerId
	 *            客户id
	 * @param integralSourceType
	 *            积分来源类型
	 * @param integralType
	 *            积分变化类型
	 * @param dealIntegral
	 *            积分数量
	 * @return
	 */
	@Override
	public CommonResult updateIntegral(String customerId, Integer integralSourceType, Integer integralType,
			Integer dealIntegral) {
		log.info("新增积分流水信息并修改客户积分账户：customerId-{}, integralSourceType-{}，integralType-{}, dealIntegral-{}", customerId,
				integralSourceType, integralType, dealIntegral);
		// 根据客户id查询积分账户，作为期初积分
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "没有找到客户信息");
		}
		// 创建积分流水对象
		IntegralRecord integralRecord = new IntegralRecord();
		// 客户id
		integralRecord.setCustomerId(customerId);
		// 积分数量
		integralRecord.setDealIntegral(dealIntegral);
		// 积分来源方式 0兑换 1签到 2分享 3消费 4充值 5推荐 6注册 7广告
		integralRecord.setIntegralSourceType(integralSourceType);
		// 积分变化类型 1增加 2减少
		integralRecord.setIntegralType(integralType);
		Integer integralAccount = customerInfo.getIntegralAccount() == null ? 0 : customerInfo.getIntegralAccount();
		// 期初积分
		integralRecord.setBeginningIntegral(integralAccount);
		if (integralType == IntegralRecord.DECREASE) {
			dealIntegral *= (-1);
		}
		// 期末积分
		integralRecord.setFinishIntegral(integralAccount + dealIntegral);
		// 将积分流水记录新增到积分流水表
		integralRecordMapper.insert(integralRecord);
		// 修改客户信息表中积分账户信息
		customerInfo.setIntegralAccount(integralAccount + dealIntegral);
		customerInfoMapper.updateSelectiveByPrimaryKey(customerInfo);
		
		// 推送积分变动消息
		if (integralSourceType >= 10 && integralSourceType <= 14) {
			//只推送日常任务积分变动
			String title = String.format("赠送日常任务积分通知");
			String content = String.format("您已完成【%s】，赠送积分：【%s】，最终积分：【%s】 。", integralRecord.getIntegralSourceName(), 
					dealIntegral, integralRecord.getFinishIntegral());
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_STATION, title, content, customerId), customerId);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "积分修改成功");
	}

	/**
	 * 获取积分记录列表
	 */
	@Override
	public CommonResult getIntegralList(String customerId, Integer pageIndex, Integer pageSize) {
		List<IntegralRecord> integralRecords = integralRecordMapper.selectByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "用户不存在");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", customerInfo.getIntegralAccount());
		map.put("dataList", integralRecords);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询积分记录成功", map);
	}

	/**
	 * 获取积分页面信息
	 */
	@Override
	public CommonResult getIntegralInfo(String customerId) {
		CustomerInfo info = customerInfoMapper.selectByPrimaryKey(customerId);
		if (info == null) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "客户信息不存在");
		}
		Map<String, Integer> map = new HashMap<>();
		Integer sign = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.SIGN);
		map.put("sign", sign > IntegralRule.DailyTask.SIGN_TIME.getValue() 
				? IntegralRule.DailyTask.SIGN_TIME.getValue() : sign);
		Integer share = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.SHARE);
		map.put("share", share > IntegralRule.DailyTask.SHARE_TIME.getValue()
				? IntegralRule.DailyTask.SHARE_TIME.getValue() : share);
		Integer advert = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.ADVERT);
		map.put("advert", advert > IntegralRule.DailyTask.ADVERT_TIME.getValue()
				? IntegralRule.DailyTask.ADVERT_TIME.getValue() : advert);
		Integer consume = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.CONSUME);
		map.put("consume", consume > IntegralRule.DailyTask.CONSUME_TIME.getValue()
				? IntegralRule.DailyTask.CONSUME_TIME.getValue() : consume);
		Integer recommend = integralRecordMapper.selectCountIntegralRecordByType(customerId, IntegralRecord.RECOMMEND);
		map.put("recommend", recommend > IntegralRule.DailyTask.RECOMMEND_TIME.getValue()
				? IntegralRule.DailyTask.RECOMMEND_TIME.getValue() : recommend);
		// 日常任务可获得的总积分数
		Integer dailyTaskToal = getTotalIntegral();
		map.put("dailyTaskToal", dailyTaskToal);
		// 今日获得日常任务积分
		Integer todayDailyTaskToal = integralRecordMapper.selectTodayDailyTaskIntegralByCustomerId(customerId);
		if (todayDailyTaskToal == null) {
			todayDailyTaskToal = 0;
		}
		map.put("todayDailyTaskToal", todayDailyTaskToal);
		if (dailyTaskToal > 0) {
			NumberFormat numberFormat = NumberFormat.getInstance();  
	        // 设置精确到小数点后2位  
	        numberFormat.setMaximumFractionDigits(2);
			map.put("todayDailyTaskPercent", Integer.valueOf(numberFormat.format((float) todayDailyTaskToal / (float) dailyTaskToal * 100)));
		} else {
			map.put("todayDailyTaskPercent", 0);
		}
		// 总积分
		map.put("total", info.getIntegralAccount());
		// 今日已获积分
		Integer today = integralRecordMapper.selectTodayIntegralByCustomerId(customerId);
		map.put("today", today == null ? 0 : today);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 获取优惠券列表
	 */
	@Override
	public CommonResult findCouponListByCouponType(Integer couponType) {
		List<Map<String, String>> couponInventoryList = couponInventoryMapper
				.selectValidCouponListByCouponType(couponType, null);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", couponInventoryList);
	}

	/**
	 * 获取用户拥有优惠券列表(根据状态分类)
	 */
	@Override
	public CommonResult getCouponListByCustomerId(String customerId, Integer couponStatus) {
		List<Map<String, String>> list = couponListMapper.selectByCustomerIdAndStatus(customerId, couponStatus);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询优惠券列表成功", list);
	}

	/**
	 * 积分兑换优惠券
	 */
	@Override
	public CommonResult exchangeCouponByIntegral(String customerId, String couponId, Integer couponNum, Integer level) {
		try {
			// 根据优惠券id查询剩余优惠券
			CouponInventory couponInventory = couponInventoryMapper.selectByPrimaryKey(couponId);
			if (couponInventory == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "优惠券不存在");
			}
			// 查看有效期
			if (DateUtil.compareDate(couponInventory.getValidTimeEnd(), new Date()) <= 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "优惠券已过期失效");
			}
			// 剩余券数量
			Integer remainingQuantity = couponInventory.getCouponQuantity() - couponInventory.getCouponQuantityGet();
			if (remainingQuantity < couponNum) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "优惠券剩余数量不足");
			}
			// 兑换等级
			Integer changeGrade = couponInventory.getChangeGrade();
			if (level < changeGrade) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "等级不够");
			}
			// 兑换券所需积分
			Integer integralNum = couponNum * couponInventory.getChangeIntegral();
			// 查看用户积分数量
			CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(customerId);
			if (customer.getIntegralAccount() < integralNum) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "积分不足");
			}
			// 开始兑换
			// 增加积分流水信息并修改用户积分账户
			// 积分来源类型（0兑换）
			updateIntegral(customerId, IntegralRecord.EXCHANGE, IntegralRecord.DECREASE, integralNum);
			// 批量新增兑换的优惠券
			List<CouponList> list = new ArrayList<>();
			for (int i = 0; i < couponNum; i++) {
				// 用户新增优惠券数据
				CouponList coupon = new CouponList();
				// 券名
				coupon.setCouponName(couponInventory.getCouponName());
				// 券id
				coupon.setCouponTypeId(couponInventory.getCrId());
				// 换购级别
				coupon.setChangeGrade(couponInventory.getChangeGrade());
				// 券说明
				coupon.setCouponExplain(couponInventory.getCouponExplain());
				// 有效时间开始
				coupon.setValidTimeStart(couponInventory.getValidTimeStart());
				// 有效时间结束
				coupon.setValidTimeEnd(couponInventory.getValidTimeEnd());
				// 兑换时间
				coupon.setChangeTime(new Date());
				// 面值
				coupon.setFaceValue(couponInventory.getFaceValue());
				// 兑换积分
				coupon.setChangeIntegral(couponInventory.getChangeIntegral());
				// 券状态 1未使用2已使用3已过期(默认未使用)
				coupon.setCouponStatus(Constants.INT_1);
				// 有效标记
				coupon.setEnabledMark(couponInventory.getEnabledMark());
				// 删除标记
				coupon.setDeleteMark(couponInventory.getDeleteMark());
				// 用户id
				coupon.setCustomerId(customerId);
				// 优惠券码
				coupon.setCouponCode(baseCodeRuleService.getCode(BaseCodeRule.COUPON_CODE));
				list.add(coupon);
			}
			couponListMapper.insertBatch(list);
			// 修改优惠券表中已领取数量
			couponInventory.setCouponQuantityGet(couponInventory.getCouponQuantityGet() + couponNum);
			couponInventoryMapper.updateSelectiveByPrimaryKey(couponInventory);
			return CommonResult.result(ResultCode.SUCC.getValue(), "优惠券兑换成功");
		} catch (Exception e) {
			log.error("服务器内部错误，兑换失败", e);
			throw e;
		}
	}

	/**
	 * 获取折扣券Discount coupon
	 */
	@Override
	public CommonResult memberFindAvailableDiscountCouponList(String customerId, String productIds) {
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(productIds)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		List<String> list = new ArrayList<>();
		for (String productId : productIds.split(",")) {
			list.add(productId);
		}
		List<Map<String, String>> discountCouponList = couponListMapper.selectAvailableDiscountCouponList(customerId, list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", discountCouponList);
	}

	/**
	 * 获取抵扣券Deduction coupon
	 */
	@Override
	public CommonResult memberFindAvailableDeductionCouponList(String customerId) {
		if (StringUtils.isBlank(customerId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		List<Map<String, String>> deductionCouponList = couponListMapper.selectAvailableDeductionCouponList(customerId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", deductionCouponList);
	}

	/**
	 * 获取兑换券Exchange coupon
	 */
	@Override
	public CommonResult memberFindAvailableExchangeCouponList(String customerId, String skuIds) {
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(skuIds)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		List<String> list = new ArrayList<>();
		for (String skuId : skuIds.split(",")) {
			list.add(skuId);
		}
		List<Map<String, String>> exchangeCouponList = couponListMapper.selectAvailableExchangeCouponList(customerId, list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", exchangeCouponList);
	}

	/**
	 * 获取服务券Service coupon
	 */
	@Override
	public CommonResult memberFindAvailableServiceCouponList(String customerId, String skuIds) {
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(skuIds)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		List<String> list = new ArrayList<>();
		for (String skuId : skuIds.split(",")) {
			list.add(skuId);
		}
		List<Map<String, String>> serviceCouponList = couponListMapper.selectAvailableServiceCouponList(customerId, list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", serviceCouponList);
	}

	/**
	 * pc:获取四种优惠券
	 */
	@Override
	public CommonResult websiteFindCouponList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serviceCoupon", couponInventoryMapper.selectValidCouponListByCouponType(1, 4));
		map.put("exchangeCoupon", couponInventoryMapper.selectValidCouponListByCouponType(2, 4));
		map.put("deductionCoupon", couponInventoryMapper.selectValidCouponListByCouponType(3, 4));
		map.put("discountCoupon", couponInventoryMapper.selectValidCouponListByCouponType(4, 4));
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * PC：获取更多优惠券
	 */
	@Override
	public CommonResult websiteFindCouponList(Integer couponType, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = couponInventoryMapper.selectCouponCountByType(couponType);
		page.setRows(total);
		List<Map<String, String>> list = couponInventoryMapper.selectValidByType(couponType, page.getStart(),
				page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}
}
