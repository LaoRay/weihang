package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import java.util.List;
import java.util.Map;

public interface OrderDetailMapper {
	int deleteByPrimaryKey(String odiId);

	int insert(OrderDetail record);

	OrderDetail selectByPrimaryKey(String odiId);

	List<OrderDetail> selectAll();

	int updateByPrimaryKey(OrderDetail record);

	void insertBatch(List<OrderDetail> list);

	List<OrderDetail> selectListByOrderId(String orderId);

	List<Map<String, String>> selectOrderDetailsByOrderId(String orderId);

	int deleteByOrderId(String orderId);

	List<Map<String, String>> selectNotCommentOrder();
}