package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import java.util.List;
import java.util.Map;

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

	int selectCountByOrderStatus(@Param("customerId") String customerId, @Param("orderStatus") Integer orderStatus,
			@Param("orderCategory") Integer orderCategory);

	/**
	 * 超过三天未支付的订单设置为已失效
	 * @param status
	 * @return
	 */
	int updateOrderListStatus(Integer status);

	/**
	 * 查询待付款的普通订单超过72小时未付款的订单
	 * 
	 * @return
	 */
	List<OrderList> selectImmediatelyInvalidOrderList();

	int updateOrderListStatusByOrderId(@Param("orderId") String orderId, @Param("status") Integer status);

	Integer selectOrderCount(@Param("orderNo") String orderNo, @Param("orderStatus") Integer orderStatus,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("orderCategory") Integer orderCategory);

	List<OrderList> selectOrderList(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("orderNo") String orderNo, @Param("orderStatus") Integer orderStatus,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("orderCategory") Integer orderCategory);

	/**
	 * 导出商品订单
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param orderStatus
	 *            订单状态
	 * @param startDate
	 *            下单时间1
	 * @param endDate
	 *            下单时间2
	 * @param orderCategory
	 *            订单类型
	 * @return
	 */
	List<Map<String, Object>> exportProductOrder(@Param("orderNo") String orderNo,
			@Param("orderStatus") Integer orderStatus, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("orderCategory") Integer orderCategory);

	/**
	 * 查询已发货且超过十天未确认收货订单
	 * 
	 * @return
	 */
	List<OrderList> selectUnconfirmedReceiptOrder();

	/**
	 * 查询状态为退款中，超过24小时的订单
	 */
	List<OrderList> selectRefund();
}