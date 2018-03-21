package cn.com.clubank.weihang.restful.login;

import javax.servlet.http.HttpServletRequest;

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
import cn.com.clubank.weihang.manage.member.pojo.ThirdLoginInfo;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import cn.com.clubank.weihang.manage.user.service.IBaseUserService;

/**
 * 登录注销Controller
 * 
 * @author LeiQY
 *
 */
@Controller
public class LoginController {

	@Autowired
	private ICustomerInfoService iCustomerInfoService;

	@Autowired
	private IBaseUserService baseUserService;

	/**
	 * 会员登录
	 * 
	 * @param customerInfo
	 * @return 登录结果信息
	 */
	@WeihAuthLog(logModule = "会员登录", logFeatures = "登录", validate = false)
	@RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerLogin(@RequestBody JSONObject json, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		return iCustomerInfoService.customerLogin(json.getString("mobile"), json.getString("carNo"),
				json.getString("password"), phoneId, request.getIntHeader("flatType"));
	}

	/**
	 * 集团组登录
	 * 
	 * @param customerInfo
	 * @return 登录结果信息
	 */
	@WeihAuthLog(logModule = "企业用户登录", logFeatures = "登录", validate = false)
	@RequestMapping(value = "/memberGroupLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberGroupLogin(@RequestBody JSONObject json, HttpServletRequest request) {
		return iCustomerInfoService.groupLogin(json.getString("account"), json.getString("password"), request.getIntHeader("flatType"));
	}

	/**
	 * 会员注销
	 * 
	 * @param token
	 * @return 注销结果信息
	 */
	@WeihAuthLog(logModule = "退出系统", logFeatures = "注销")
	@RequestMapping(value = "/memberLogout", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerLogout(HttpServletRequest request) {
		return iCustomerInfoService.customerLogout(request.getHeader("token"), request.getHeader("UUID"));
	}

	/**
	 * 员工登录
	 * 
	 * @param baseUser
	 * @param request
	 * @return
	 */
	@WeihAuthLog(logModule = "员工APP登录", logFeatures = "登录", validate = false)
	@RequestMapping(value = "/baseUserLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult baseUserLogin(@RequestBody BaseUser baseUser, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		return baseUserService.baseUserLogin(baseUser, phoneId, request.getIntHeader("flatType"));
	}

	/**
	 * 员工pc端登录
	 * 
	 * @param customerInfo
	 * @return 登录结果信息
	 */
	@WeihAuthLog(logModule = "员工PC端登录", logFeatures = "登录", validate = false)
	@RequestMapping(value = "/baseUserClientLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberClientLogin(@RequestBody JSONObject json, HttpServletRequest request) {
		return baseUserService.baseUserClientLogin(json.getString("account"), json.getString("pwd"), request.getIntHeader("flatType"));
	}

	/**
	 * app第三方登录
	 */
	@WeihAuthLog(logModule = "第三方登录", logFeatures = "登录", validate = false)
	@RequestMapping(value = "/memberThirdLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberThirdLogin(@RequestBody JSONObject json, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		return iCustomerInfoService.memberThirdLogin(json.getString("thirdId"), phoneId, request.getIntHeader("flatType"));
	}

	/**
	 * 绑定第三方信息并注册会员
	 */
	@WeihAuthLog(logModule = "第三方信息绑定", logFeatures = "注册", validate = false)
	@RequestMapping(value = "/memberBindingThirdInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberBindingThirdInfo(@RequestBody JSONObject json, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		CustomerInfo customerInfo = JSONObject.toJavaObject(json, CustomerInfo.class);
		ThirdLoginInfo thirdLoginInfo = JSONObject.toJavaObject(json, ThirdLoginInfo.class);
		return iCustomerInfoService.memberBindingThirdInfo(customerInfo, thirdLoginInfo, json.getString("randomCode"),
				json.getBoolean("isExist"), phoneId, request.getIntHeader("flatType"));
	}
}
