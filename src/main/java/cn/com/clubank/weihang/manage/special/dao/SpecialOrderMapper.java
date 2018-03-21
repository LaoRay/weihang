package cn.com.clubank.weihang.manage.special.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.special.pojo.SpecialOrder;

public interface SpecialOrderMapper {
    int deleteByPrimaryKey(String specialId);

    int insert(SpecialOrder record);

    int insertSelective(SpecialOrder record);

    SpecialOrder selectByPrimaryKey(String specialId);
    
    SpecialOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(SpecialOrder record);

    int updateByPrimaryKey(SpecialOrder record);
    
    /**
     * 客户通过状态查询到的条数
     * @param customerId
     * @param status
     * @return
     */
    int selectSpecialOrderCount(@Param("customerId") String customerId, @Param("status") Integer status);
    
    /**
     * 获得用户某状态下的特殊订单列表
     * @param customerId
     * @param status
     * @param start 起始索引
     * @param pageSize
     * @return
     */
    List<SpecialOrder> selectSpecialOrderList(@Param("customerId") String customerId,
			@Param("status") Integer status, @Param("start") Integer start, @Param("pageSize") Integer pageSize);
    
    /**
     * 后台：通过客户名称模糊查询，订单状态、下单时间查询到特殊订单的条数
     * @param orderTimeStart
     * @param orderTimeEnd
     * @param customerName
     * @param status
     * @return
     */
	int clientSelectSpecialOrderCount(@Param("orderTimeStart") String orderTimeStart,
			@Param("orderTimeEnd") String orderTimeEnd, @Param("customerName") String customerName,
			@Param("status") Integer status);
	
	/**
	 * 后台：通过客户名称模糊查询，订单状态、下单时间查询到特殊订单并分页
	 * @param orderTimeStart
	 * @param orderTimeEnd
	 * @param customerName
	 * @param status
	 * @param start 起始索引
	 * @param pageSize
	 * @return
	 */
	List<SpecialOrder> clientSelectSpecialOrderList(@Param("orderTimeStart") String orderTimeStart,
			@Param("orderTimeEnd") String orderTimeEnd, @Param("customerName") String customerName,
			@Param("status") Integer status, @Param("start") Integer start, @Param("pageSize") Integer pageSize);
	
	void handleDelayedPayOrderStatus();

	void handleUnconfirmedReceiptOrderStatus();
	
	/**
	 * 后台：按查询条件导出特殊订单
	 * @param status
	 * @param orderTimeStart
	 * @param orderTimeEnd
	 * @return
	 */
	List<SpecialOrder> exportSpecialOrder(@Param("status") Integer status, @Param("orderTimeStart") String orderTimeStart, @Param("orderTimeEnd") String orderTimeEnd);
}