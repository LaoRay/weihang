package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.IntegralRuleMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.IntegralRuleService;

/**
 * 积分规则
 * 
 * @author LeiQY
 *
 */
@Service
public class IntegralRuleServiceImpl implements IntegralRuleService {

	@Autowired
	private IntegralRuleMapper integralRuleMapper;

	/**
	 * 根据ruleCode获取积分规则
	 */
	@Override
	public IntegralRule getIntegralRuleByRuleCode(String ruleCode) {
		IntegralRule integralRule = integralRuleMapper.selectByRuleCode(ruleCode);
		if (integralRule == null) {
			integralRule = new IntegralRule();
		}
		return integralRule;
	}

	/**
	 * 根据ruleCode获取积分数
	 */
	@Override
	public Integer getIntegralRuleValueByRuleCode(String ruleCode) {
		IntegralRule integralRule = integralRuleMapper.selectByRuleCode(ruleCode);
		if (integralRule != null && integralRule.getRuleStatus() == IntegralRule.STATUS_YES) {
			return integralRule.getRuleValue();
		}
		return null;
	}

	/**
	 * 根据ruleCode获取积分基数
	 */
	@Override
	public Integer getBaseRuleValueByRuleCode(String ruleCode) {
		IntegralRule integralRule = integralRuleMapper.selectByRuleCode(ruleCode);
		if (integralRule != null && integralRule.getRuleStatus() == IntegralRule.STATUS_YES) {
			return integralRule.getBaseValue();
		}
		return null;
	}

	/**
	 * 新增积分规则
	 */
	@Override
	public CommonResult clientAddIntegralRule(IntegralRule rule) {
		integralRuleMapper.insert(rule);
		return CommonResult.result(ResultCode.SUCC.getValue(), "新增成功");
	}

	/**
	 * 后台：查询积分规则列表
	 */
	@Override
	public CommonResult clientFindIntegralRuleList(Integer pageIndex, Integer pageSize, String ruleName) {
		PageObject<IntegralRule> page = new PageObject<>(pageIndex, pageSize);
		int total = integralRuleMapper.selectIntegralRuleCount(ruleName);
		page.setRows(total);
		List<IntegralRule> list = integralRuleMapper.selectIntegralRuleList(page.getStart(), page.getPageSize(),
				ruleName);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 根据主键查询积分规则
	 */
	@Override
	public CommonResult clientFindIntegralRuleByRuleId(String integralRuleId) {
		IntegralRule integralRule = integralRuleMapper.selectByPrimaryKey(integralRuleId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", integralRule);
	}

	/**
	 * 后台：编辑积分规则
	 */
	@Override
	public CommonResult clientModifyIntegralRule(IntegralRule rule) {
		if (rule == null || StringUtils.isBlank(rule.getIntegralRuleId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		integralRuleMapper.updateSelectiveByPrimaryKey(rule);
		return CommonResult.result(ResultCode.SUCC.getValue(), "修改成功");
	}
}
