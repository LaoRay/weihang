package cn.com.clubank.weihang.manage.systemSettings.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;

/**
 * 积分规则
 * 
 * @author LeiQY
 *
 */
public interface IntegralRuleService {

	/**
	 * 根据ruleCode获取积分规则
	 * 
	 * @param keyCode
	 * @return
	 */
	IntegralRule getIntegralRuleByRuleCode(String ruleCode);

	/**
	 * 根据ruleCode获取积分数
	 * 
	 * @param keyCode
	 * @return
	 */
	Integer getIntegralRuleValueByRuleCode(String ruleCode);

	/**
	 * 根据ruleCode获取积分基数
	 * 
	 * @param keyCode
	 * @return
	 */
	Integer getBaseRuleValueByRuleCode(String ruleCode);

	/**
	 * 新增积分规则
	 * 
	 * @param rule
	 * @return
	 */
	CommonResult clientAddIntegralRule(IntegralRule rule);

	/**
	 * 后台：查询积分规则列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param ruleName
	 * @return
	 */
	CommonResult clientFindIntegralRuleList(Integer pageIndex, Integer pageSize, String ruleName);

	/**
	 * 根据主键查询积分规则
	 * 
	 * @param integralRuleId
	 * @return
	 */
	CommonResult clientFindIntegralRuleByRuleId(String integralRuleId);

	/**
	 * 编辑积分规则
	 * 
	 * @param rule
	 * @return
	 */
	CommonResult clientModifyIntegralRule(IntegralRule rule);
}
