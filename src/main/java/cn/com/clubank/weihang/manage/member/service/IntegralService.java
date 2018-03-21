package cn.com.clubank.weihang.manage.member.service;

import java.math.BigDecimal;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 积分管理（业务逻辑层接口）
 * 
 * @author LeiQY
 *
 */
public interface IntegralService {

	/**
	 * 会员分享增加积分
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult share(String customerId);

	/**
	 * 会员充值赠送积分
	 * 
	 * @param customerId
	 * @param total
	 */
	void rechargeIntegral(String customerId, BigDecimal total);

	/**
	 * 消费带积分的商品为客户增加积分
	 * 
	 * @param customerId
	 * @param orderId
	 */
	void productIntegral(String customerId, String orderId);

	/**
	 * 签到为客户增加积分
	 * 
	 * @param customerId
	 * @param integral
	 */
	void signIntegral(String customerId, Integer integral);

	/**
	 * 点击广告为客户增加积分
	 * 
	 * @param customerId
	 */
	void advertIntegral(String customerId);

	/**
	 * 推荐成功注册后为客户增加积分
	 * 
	 * @param customerId
	 */
	void recommendIntegral(String customerId);

	/**
	 * 积分变化管理
	 * 
	 * @param customerId
	 *            客户id
	 * @param integralSourceType
	 *            积分来源类型 1.签到 2.分享 3.消费
	 * @param integralType
	 *            积分变化类型 1.增加 2.减少
	 * @param dealIntegral
	 *            变化的积分数量
	 * @return
	 */
	CommonResult updateIntegral(String customerId, Integer integralSourceType, Integer integralType,
			Integer dealIntegral);

	/**
	 * 获取积分列表
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult getIntegralList(String customerId, Integer pageIndex, Integer pageSize);

	/**
	 * 获取积分页面信息
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult getIntegralInfo(String customerId);

	/**
	 * 获取优惠券列表
	 * 
	 * @param couponType
	 * @return
	 */
	CommonResult findCouponListByCouponType(Integer couponType);

	/**
	 * 获取用户拥有的优惠券列表
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult getCouponListByCustomerId(String customerId, Integer couponStatus);

	/**
	 * 积分兑换优惠券
	 * 
	 * @param customerId
	 * @param couponId
	 * @param couponNum
	 * @param level
	 * @return
	 */
	CommonResult exchangeCouponByIntegral(String customerId, String couponId, Integer couponNum, Integer level);

	/**
	 * 获取折扣券Discount coupon
	 * 
	 * @param customerId
	 * @param productIds
	 * @return
	 */
	CommonResult memberFindAvailableDiscountCouponList(String customerId, String productIds);

	/**
	 * 获取抵扣券Deduction coupon
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult memberFindAvailableDeductionCouponList(String customerId);

	/**
	 * 获取兑换券Exchange coupon
	 * 
	 * @param customerId
	 * @param skuIds
	 * @return
	 */
	CommonResult memberFindAvailableExchangeCouponList(String customerId, String skuIds);

	/**
	 * 获取服务券Service coupon
	 * 
	 * @param customerId
	 * @param skuIds
	 * @return
	 */
	CommonResult memberFindAvailableServiceCouponList(String customerId, String skuIds);

	/**
	 * PC：获取四种优惠券
	 * 
	 * @return
	 */
	CommonResult websiteFindCouponList();

	/**
	 * PC：获取更多优惠券
	 * 
	 * @param couponType
	 * @return
	 */
	CommonResult websiteFindCouponList(Integer couponType, Integer pageIndex, Integer pageSize);
}
