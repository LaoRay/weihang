package cn.com.clubank.weihang.client.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.IntegralRuleService;

/**
 * 后台：积分规则管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientIntegralRuleController {

	@Autowired
	private IntegralRuleService integralRuleService;

	/**
	 * 新增积分规则
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientAddIntegralRule", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientAddIntegralRule(@RequestBody IntegralRule rule) {
		return integralRuleService.clientAddIntegralRule(rule);
	}

	/**
	 * 查询积分规则列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientFindIntegralRuleList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindIntegralRuleList(@RequestBody JSONObject json) {
		return integralRuleService.clientFindIntegralRuleList(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getString("ruleName"));
	}

	/**
	 * 根据主键查询积分规则
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientFindIntegralRuleByRuleId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindIntegralRuleByRuleId(@RequestBody JSONObject json) {
		return integralRuleService.clientFindIntegralRuleByRuleId(json.getString("integralRuleId"));
	}

	/**
	 * 编辑积分规则
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientModifyIntegralRule", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientModifyIntegralRule(@RequestBody IntegralRule rule) {
		return integralRuleService.clientModifyIntegralRule(rule);
	}
}
