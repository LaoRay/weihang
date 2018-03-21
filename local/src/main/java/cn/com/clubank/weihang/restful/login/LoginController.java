package cn.com.clubank.weihang.restful.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
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
	@RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerLogin(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		return iCustomerInfoService.customerLogin(customerInfo, phoneId);
	}
	
	/**
	 * 集团组登录
	 * 
	 * @param customerInfo
	 * @return 登录结果信息
	 */
	@RequestMapping(value = "/memberGroupLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberGroupLogin(@RequestBody JSONObject json) {
		return iCustomerInfoService.groupLogin(json.getString("account"), json.getString("password"));
	}

	/**
	 * 会员注销
	 * 
	 * @param token
	 * @return 注销结果信息
	 */
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
	@RequestMapping(value = "/baseUserLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult baseUserLogin(@RequestBody BaseUser baseUser, HttpServletRequest request) {
		String phoneId = request.getHeader("UUID");
		return baseUserService.baseUserLogin(baseUser, phoneId);
	}
	
	/**
	 * 员工pc端登录
	 * 
	 * @param customerInfo
	 * @return 登录结果信息
	 */
	@RequestMapping(value = "/baseUserClientLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberClientLogin(@RequestBody JSONObject json) {
		return baseUserService.baseUserClientLogin(json.getString("account"), json.getString("pwd"));
	}
}
