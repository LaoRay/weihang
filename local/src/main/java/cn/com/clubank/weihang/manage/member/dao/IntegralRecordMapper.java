package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.IntegralRecord;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 积分流水表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface IntegralRecordMapper {
	int deleteByPrimaryKey(String recordId);

	int insert(IntegralRecord record);

	IntegralRecord selectByPrimaryKey(String recordId);

	List<IntegralRecord> selectAll();

	int updateByPrimaryKey(IntegralRecord record);

	List<IntegralRecord> selectByCustomerId(@Param("customerId") String customerId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);

	Integer selectCountIntegralRecordByType(@Param("customerId") String customerId, @Param("type") Integer type);
}