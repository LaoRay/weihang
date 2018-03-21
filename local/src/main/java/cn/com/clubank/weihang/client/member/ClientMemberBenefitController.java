package cn.com.clubank.weihang.client.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.service.IAccountBenefitService;

/**
 * 会员权益
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientMemberBenefitController {

	@Autowired
	private IAccountBenefitService benefitService;

	/**
	 * 获取会员权益列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindAccountBenefitList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindAccountBenefitList(@RequestBody JSONObject json) {
		return benefitService.clientFindAccountBenefitList(json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}

	/**
	 * 获取会员权益详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindAccountBenefitByAbId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindAccountBenefitByAbId(@RequestBody JSONObject json) {
		return benefitService.clientFindAccountBenefitByAbId(json.getString("abId"));
	}

	/**
	 * 新增或修改会员权益
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientModifyAccountBenefit", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientModifyAccountBenefit(@RequestBody AccountBenefit accountBenefit) {
		return benefitService.clientModifyAccountBenefit(accountBenefit);
	}

	/**
	 * 会员权益修改有效无效状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientUpdateAccountBenefitStatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientUpdateAccountBenefitStatus(@RequestBody JSONObject json) {
		return benefitService.clientUpdateAccountBenefitStatus(json.getString("abId"), json.getInteger("status"));
	}

	/**
	 * 删除会员权益
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteAccountBenefit", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteAccountBenefit(@RequestBody JSONObject json) {
		return benefitService.clientDeleteAccountBenefit(json.getString("abId"));
	}
}
