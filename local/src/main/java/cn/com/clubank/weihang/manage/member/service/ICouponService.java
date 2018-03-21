package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;

/**
 * 优惠券表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface ICouponService {

	/**
	 * 通过券名称模糊查询，券类型、开始时间查询获得券列表并分页
	 * 
	 * @param couponName
	 *            券名
	 * @param couponType
	 *            券类型
	 * @param startDateOne
	 *            开始时间1
	 * @param startDateTwo
	 *            开始时间2
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectCouponListByNameOrTime(String couponName, Integer couponType, String startDateOne, String startDateTwo,
			Integer pageIndex, Integer pageSize);

	/**
	 * 新增或编辑优惠券
	 * 
	 * @param record
	 *            优惠券表对象
	 * @return
	 */
	CommonResult insertOrUpdateCoupon(CouponInventory record);

	/**
	 * 删除优惠券
	 * 
	 * @param crId
	 *            优惠券ID
	 * @return
	 */
	String deleteCoupon(String crId);

	/**
	 * 获得优惠券详情
	 * 
	 * @param crId
	 *            优惠券ID
	 * @return
	 */
	String selectCouponDetail(String crId);

	/**
	 * 后台：分页查询优惠券兑换流水列表（含条件）
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param couponName
	 * @param customerName
	 * @param couponStatus
	 * @return
	 */
	CommonResult clientFindCouponExchangeRecordList(Integer pageIndex, Integer pageSize, String couponName,
			String customerName, Integer couponStatus);

	/**
	 * 后台：根据优惠券兑换来记录主键查询兑换详情
	 * 
	 * @param couponId
	 * @return
	 */
	CommonResult clientFindCouponExchangeDetailByCouponId(String couponId);

	/**
	 * 后台：删除优惠券兑换记录
	 * 
	 * @param couponId
	 * @return
	 */
	CommonResult clientDeleteCouponExchangeRecord(String couponId);
}
