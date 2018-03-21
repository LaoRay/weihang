package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface IntegralRuleMapper {
	int deleteByPrimaryKey(String integralRuleId);

	int insert(IntegralRule record);

	IntegralRule selectByPrimaryKey(String integralRuleId);

	List<IntegralRule> selectAll();

	int updateByPrimaryKey(IntegralRule record);

	IntegralRule selectByRuleCode(String ruleCode);

	int selectIntegralRuleCount(@Param("ruleName") String ruleName);

	List<IntegralRule> selectIntegralRuleList(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("ruleName") String ruleName);

	int updateSelectiveByPrimaryKey(IntegralRule rule);
}