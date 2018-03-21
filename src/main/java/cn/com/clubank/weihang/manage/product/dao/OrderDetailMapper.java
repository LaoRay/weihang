package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;

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
	
	/**
	 * 查询客户未完成的服务类商品和汽车类实体商品列表
	 * 
	 * @param customerId
	 * @return
	 */
	List<Map<String, String>> selectWaitReceiveServiceOrder(String customerId);
	
	/**
	 * 根据ids查询详情列表
	 * 
	 * @param odiIds
	 * @return
	 */
	List<Map<String, String>> selectByIds(@Param("odiIds") String[] odiIds);
}