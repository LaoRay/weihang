package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import java.util.List;
import java.util.Map;

/**
 * 收货地址表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface CustomerDeliveryAddressMapper {
	int deleteByPrimaryKey(String daId);

	int insert(CustomerDeliveryAddress record);

	CustomerDeliveryAddress selectByPrimaryKey(String daId);

	List<CustomerDeliveryAddress> selectAll();

	int updateByPrimaryKey(CustomerDeliveryAddress record);

	List<CustomerDeliveryAddress> selectByCustomerId(String customerId);

	int updateSelectiveByPrimaryKey(CustomerDeliveryAddress deliveryAddress);

	int updateDeliveryAddressCancelDefaultByCustomerId(String customerId);

	CustomerDeliveryAddress selectDefaultByCustomerId(String customerId);

	Map<String, String> selectDetailByPrimaryKey(String daId);
}