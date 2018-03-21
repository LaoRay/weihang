package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.CustomerSign;
import java.util.List;

/**
 * 签到流水表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface CustomerSignMapper {
	int deleteByPrimaryKey(String signId);

	int insert(CustomerSign record);

	CustomerSign selectByPrimaryKey(String signId);

	List<CustomerSign> selectAll();

	int updateByPrimaryKey(CustomerSign record);
	
	List<CustomerSign> selectByCustomerId(String customerId);
}