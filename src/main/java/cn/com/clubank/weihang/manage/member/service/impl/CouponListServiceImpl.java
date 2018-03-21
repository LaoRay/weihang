package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;
import cn.com.clubank.weihang.manage.member.service.ICouponListService;

/**
 * 券兑换信息
 * 
 * @author YangWei
 *
 */
@Service
public class CouponListServiceImpl implements ICouponListService {

	@Resource
	private CouponListMapper couponListMapper;

	@Override
	public List<CouponList> selectByCustomerId(String customerId) {

		return couponListMapper.selectByCustomerId(customerId);
	}

	/**
	 * 使用优惠券：将优惠券状态置为已使用
	 */
	@Override
	public CommonResult useCoupon(String couponCode, String orderNo) {
		CouponList couponList = couponListMapper.selectByCouponCode(couponCode);
		// 使用时间
		couponList.setUserTime(new Date());
		// 优惠券状态 1未使用 2已使用 3已过期
		couponList.setCouponStatus(Constants.INT_2);
		// 设置订单号
		couponList.setOrderNo(orderNo);
		couponListMapper.updateByPrimaryKey(couponList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "成功");
	}
}
