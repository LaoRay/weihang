package cn.com.clubank.weihang.restful.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuthLog;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;

/**
 * 会员注册信息管理Controller
 * 
 * @author LeiQY
 *
 */
@Controller
public class RegisterController {

	@Autowired
	private ICustomerInfoService iCustomerInfoService;

	/**
	 * 发送验证码通用接口
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendValidate", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult sendValidate(@RequestBody JSONObject json) {
		// 手机号
		String mobile = json.getString("mobile");
		// 验证码类型
		// 0 代表该短信针对未使用过的手机号，数据库不存在时才发送（如注册，修改验证手机等）
		// 1 代表短信针对在使用的手机号，数据库存在时才发送（如初始化密码，修改密码，密码找回等）
		Integer type = json.getInteger("type");
		return iCustomerInfoService.sendValidate(mobile, type);
	}

	/**
	 * 客户端-会员注册
	 * 
	 * @param customerInfo
	 * @param randomCode
	 * @return
	 */
	@WeihAuthLog(logModule = "会员注册", logFeatures = "注册", validate = false)
	@RequestMapping(value = "/memberRegister", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult register(@RequestBody JSONObject json) {
		CustomerInfo customerInfo = JSONObject.toJavaObject(json, CustomerInfo.class);
		return iCustomerInfoService.register(customerInfo, json.getString("randomCode"), false);
	}
}
