package cn.com.clubank.weihang.manage.bespeak.dao;

import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 预约单表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface WorkReservationOrderMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table work_reservation_order
	 * 
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(String roId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table work_reservation_order
	 * 
	 * @mbggenerated
	 */
	int insert(WorkReservationOrder record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table work_reservation_order
	 * 
	 * @mbggenerated
	 */
	WorkReservationOrder selectByPrimaryKey(String roId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table work_reservation_order
	 * 
	 * @mbggenerated
	 */
	List<WorkReservationOrder> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table work_reservation_order
	 * 
	 * @mbggenerated
	 */
	int updateByPrimaryKey(WorkReservationOrder record);

	/**
	 * 通过客户ID查询预约单信息
	 * 
	 * @param customerId
	 * @param pageSize
	 * @param start
	 * @param reservationStatus
	 * @return
	 */
	List<WorkReservationOrder> selectByCustomerId(@Param("customerId") String customerId,
			@Param("reservationStatus") Integer reservationStatus, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);

	/**
	 * 取消预约单
	 * 
	 * @param reservationOrderNo
	 * @return
	 */
	int cancel(String reservationOrderNo);

	/**
	 * 通过预约单号获得预约单详情
	 * 
	 * @param reservationOrderNo
	 * @return
	 */
	WorkReservationOrder selectByReservationOrderNo(String reservationOrderNo);

	/**
	 * 根据车牌号查询未完成的预约单
	 * 
	 * @param carNo
	 *            车牌号
	 * @return
	 */
	List<WorkReservationOrder> selectUndoneByCarNo(String carNo);

	/**
	 * 客户端-查询预约单列表
	 * 
	 * @param carNo
	 * @param startIndex
	 * @param pageSize
	 * @param reservationDateEnd
	 * @param reservationDateStart
	 * @param orderStatus
	 * @return
	 */
	List<Map<String, Object>> clientFindReservationOrderPage(@Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize, @Param("carNo") String carNo,
			@Param("orderStatus") Integer orderStatus, @Param("reservationDateStart") String reservationDateStart,
			@Param("reservationDateEnd") String reservationDateEnd);

	int clientFindReservationOrderTotal(@Param("carNo") String carNo, @Param("orderStatus") Integer orderStatus,
			@Param("reservationDateStart") String reservationDateStart,
			@Param("reservationDateEnd") String reservationDateEnd);

	/**
	 * 根据时间设置状态为已过期
	 * 
	 * @param reservationDateHour
	 * @return
	 */
	int setTimeOut(String reservationDateHour);

	/**
	 * 导出预约单数据
	 * 
	 * @param carNo
	 *            车牌号
	 * @param orderStatus
	 *            预约状态
	 * @param reservationDateStart
	 *            预约时间1
	 * @param reservationDateEnd
	 *            预约时间2
	 * @return
	 */
	List<Map<String, Object>> exportReservationOrder(@Param("carNo") String carNo,
			@Param("orderStatus") Integer orderStatus, @Param("reservationDateStart") String reservationDateStart,
			@Param("reservationDateEnd") String reservationDateEnd);

	int selectBespeakOrderCount(@Param("customerId") String customerId,
			@Param("reservationStatus") Integer reservationStatus);
}