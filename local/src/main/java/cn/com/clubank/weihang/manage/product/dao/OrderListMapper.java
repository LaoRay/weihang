package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderListMapper {
	int deleteByPrimaryKey(String orderId);

	int insert(OrderList record);

	OrderList selectByPrimaryKey(String orderId);

	OrderList selectByNo(@Param("orderNo") String orderNo);

	List<OrderList> selectAll();

	int updateByPrimaryKey(OrderList record);

	int updateSelectiveByPrimaryKey(OrderList record);

	List<OrderList> selectByOrderStatus(@Param("customerId") String customerId,
			@Param("orderStatus") Integer orderStatus, @Param("orderCategory") Integer orderCategory,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	int selectCountByOrderStatus(@Param("customerId") String customerId, @Param("orderStatus") Integer orderStatus);

	int updateOrderListStatus(Integer status);

	List<OrderList> selectImmediatelyInvalidOrderList();

	int updateOrderListStatusByOrderId(@Param("orderId") String orderId, @Param("status") Integer status);

	Integer selectOrderCount(@Param("orderNo") String orderNo, @Param("orderStatus") Integer orderStatus,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("orderCategory") Integer orderCategory);

	List<OrderList> selectOrderList(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("orderNo") String orderNo, @Param("orderStatus") Integer orderStatus,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("orderCategory") Integer orderCategory);

	int selectCountPreOrder(@Param("customerId") String customerId);
}