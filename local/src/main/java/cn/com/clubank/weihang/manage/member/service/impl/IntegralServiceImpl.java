package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
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
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductIntegralMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
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
		Integer count = integralRecordMapper.selectCountIntegralRecordByType(customerId, integralSourceType);
		// 一天限制5次
		if (count <= 5 && dealIntegral > 0) {
			CommonResult result = updateIntegral(customerId, integralSourceType, integralType, dealIntegral);
			if (result.getApiStatus() != ResultCode.SUCC.getValue()) {
				return CommonResult.result(ResultCode.SERVER_ERROR.getValue(), "服务器错误，分享失败");
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "分享并赠送积分成功",
				customerInfoMapper.selectByPrimaryKey(customerId).getRecommendCode());
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
		if (count > 5) {
			return;
		}
		updateIntegral(customerId, IntegralRecord.ADVERT, IntegralRecord.INCREASE, integral);
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
		try {
			// 根据客户id查询积分账户，作为期初积分
			CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
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
			Integer integralAccount = customerInfo.getIntegralAccount() == null ? Constants.INT_0
					: customerInfo.getIntegralAccount();
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
			customerInfoMapper.updateByPrimaryKey(customerInfo);
			return CommonResult.result(ResultCode.SUCC.getValue(), "积分修改成功");
		} catch (Exception e) {
			log.error("服务器异常", e);
			throw e;
		}
	}

	/**
	 * 获取积分记录列表
	 */
	@Override
	public CommonResult getIntegralList(String customerId, Integer pageIndex, Integer pageSize) {
		List<IntegralRecord> integralRecords = integralRecordMapper.selectByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		Integer integralCount = Constants.INT_0;
		if (integralRecords != null && integralRecords.size() != 0) {
			integralCount = integralRecords.get(0).getFinishIntegral();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", integralCount);
		map.put("dataList", integralRecords);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询积分记录成功", map);
	}

	/**
	 * 获取所有优惠券列表
	 */
	@Override
	public CommonResult getCouponList() {
		List<CouponInventory> CouponInventoryList = couponInventoryMapper.selectValidCouponList();
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", CouponInventoryList);
	}

	/**
	 * 获取用户拥有优惠券列表(根据状态分类)
	 */
	@Override
	public CommonResult getCouponListByCustomerId(String customerId, Integer couponStatus) {
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询优惠券列表成功",
				couponListMapper.selectByCustomerIdAndStatus(customerId, couponStatus));
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
				// 需要的积分数量
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
			couponInventoryMapper.updateByPrimaryKey(couponInventory);
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
		List<Map<String, String>> discountCouponList = couponListMapper.selectAvailableDiscountCouponList(customerId,
				list);
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
		List<Map<String, String>> exchangeCouponList = couponListMapper.selectAvailableExchangeCouponList(customerId,
				list);
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
		List<Map<String, String>> deductionCouponList = couponListMapper.selectAvailableServiceCouponList(customerId,
				list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", deductionCouponList);
	}
}
