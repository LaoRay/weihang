package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 优惠券表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface CouponInventoryMapper {

	int deleteByPrimaryKey(String crId);

	int insert(CouponInventory record);

	CouponInventory selectByPrimaryKey(String crId);

	Map<String, String> selectCouponDetailByCrId(String crId);

	List<CouponInventory> selectAll();

	List<CouponInventory> selectValidCouponList();

	int updateByPrimaryKey(CouponInventory record);

	int updateSelectiveByPrimaryKey(CouponInventory record);

	/**
	 * 根据类型查询未过期的优惠券
	 * 
	 * @param type
	 *            1服务券 2兑换券 3抵扣券 4优惠券
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<CouponInventory> selectValidByType(@Param("type") Integer type, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);

	/**
	 * 通过券名模糊查询或开始时间查询到的总条数
	 * 
	 * @param couponName
	 *            券名
	 * @param couponType 券类型 
	 * @param startDateOne
	 *            开始时间1
	 * @param startDateTwo
	 *            开始时间2
	 * @return
	 */
	int selectSumByCouponNameOrTime(@Param("couponName") String couponName, @Param("couponType") Integer couponType,
			@Param("startDateOne") String startDateOne, @Param("startDateTwo") String startDateTwo);

	/**
	 * 通过券名称模糊查询，开始时间查询获得券列表并分页
	 * 
	 * @param couponName
	 *            券名
	 * @param couponType 券类型           
	 * @param startDateOne
	 *            开始时间1
	 * @param startDateTwo
	 *            开始时间2
	 * @param startIndex
	 *            起始索引
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<CouponInventory> selectCouponListPagedByNameOrTime(@Param("couponName") String couponName,
			@Param("couponType") Integer couponType, @Param("startDateOne") String startDateOne,
			@Param("startDateTwo") String startDateTwo, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);
}