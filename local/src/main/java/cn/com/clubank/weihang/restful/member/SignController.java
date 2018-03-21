package cn.com.clubank.weihang.restful.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.service.CustomerSignService;

/**
 * 签到Controller
 * 
 * @author LeiQY
 *
 */
@Controller
public class SignController {

	@Autowired
	private CustomerSignService signService;

	/**
	 * 获取会员签到列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/memberSignList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getSignList(@RequestBody JSONObject json) {
		return signService.getSignList(json.getString("customerId"));
	}

	/**
	 * 会员签到
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberSign", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult sign(@RequestBody JSONObject json) {
		return signService.sign(json.getString("customerId"), json.getString("signTime"));
	}
}
