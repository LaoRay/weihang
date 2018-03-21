package cn.com.clubank.weihang.manage.member.service;

import java.util.List;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;

/**
 * 券兑换信息表（业务逻辑层接口）
 * 
 * @author YangWei
 *
 */
public interface ICouponListService {

	/**
	 * 通过客户关联ID查询数据
	 * 
	 * @param customerId
	 * @return
	 */
	List<CouponList> selectByCustomerId(String customerId);

	/**
	 * 使用优惠券：将优惠券状态置为已使用
	 * 
	 * @param couponCode
	 * @param orderId
	 * @return
	 */
	CommonResult useCoupon(String couponCode, String orderId);
}
