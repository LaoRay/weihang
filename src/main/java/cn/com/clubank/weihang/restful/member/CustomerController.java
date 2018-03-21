package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;

@Controller
public class CustomerController {

	@Resource
	private ICustomerInfoService iCustomerInfoService;

	@Resource
	private ICarInfoService iCarInfoService;

	/**
	 * 根据会员编号，获取会员基本信息
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/memberGetInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getCustomerInfo(@RequestBody JSONObject json) {
		return iCustomerInfoService.getCustomerInfo(json.getString("customerId"));
	}

	/**
	 * 获取会员等级
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberGetLevel", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getCustomerLevel(@RequestBody JSONObject json) {
		return iCustomerInfoService.getCustomerLevel(json.getString("customerId"));
	}

	/**
	 * 输入车牌字符，获取车牌登记日期及会员信息
	 * 
	 * @param carNo
	 * @return
	 */
	@RequestMapping(value = "/memberGetCarNoRecordDateAndInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getCarNoRecordDateAndInfo(@RequestBody JSONObject json) {
		return iCarInfoService.selectByCarNo(json.getString("carNo"));
	}

	/**
	 * 验证码校验
	 * 
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/memberCheckVerifyCode", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult checkVerifyCode(@RequestBody JSONObject json) {
		return iCustomerInfoService.checkVerifyCode(json.getString("mobile"), json.getString("randomCode"));
	}

	/**
	 * 重置密码
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/memberResetPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult resetPassword(HttpServletRequest request, @RequestBody JSONObject json) {
		if (request.getIntHeader("flatType") == Constants.INT_4) {
			//pc网站充值密码需要加密
			return iCustomerInfoService.resetPassword(json.getString("mobile"), json.getString("password"), true);
		}
		return iCustomerInfoService.resetPassword(json.getString("mobile"), json.getString("password"), false);
	}

	/**
	 * 修改登录密码
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberModifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyPassword(@RequestBody JSONObject json) {
		return iCustomerInfoService.modifyPassword(json.getString("customerId"), json.getString("password"));
	}

	/**
	 * 会员信息补全
	 * 
	 * @param customerInfo
	 * @return
	 */
	@RequestMapping(value = "/memberCompleteInformation", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult completeInformation(@RequestBody CustomerInfo customerInfo) {
		return iCustomerInfoService.completeInformation(customerInfo);
	}

	/**
	 * 获取会员收货地址列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberDeliveryAddressList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getDeliveryAddressList(@RequestBody JSONObject json) {
		return iCustomerInfoService.getDeliveryAddressList(json.getString("customerId"));
	}

	/**
	 * 根据地址主键查询地址详情
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberFindDeliveryAddressDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findDeliveryAddressDetail(@RequestBody JSONObject json) {
		return iCustomerInfoService.findDeliveryAddressDetail(json.getString("daId"));
	}

	/**
	 * 新增或修改会员收货地址信息
	 * 
	 * @param deliveryAddress
	 * @return
	 */
	@RequestMapping(value = "/modifyCustomerDeliveryAddress", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyCustomerDeliveryAddress(@RequestBody CustomerDeliveryAddress deliveryAddress) {
		return iCustomerInfoService.modifyCustomerDeliveryAddress(deliveryAddress);
	}

	/**
	 * 删除收货地址
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/deleteCustomerDeliveryAddress", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteCustomerDeliveryAddress(@RequestBody JSONObject json) {
		return iCustomerInfoService.deleteCustomerDeliveryAddress(json.getString("daId"));
	}

	/**
	 * 设置或修改支付密码
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberSetPaymentPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult setPaymentPassword(@RequestBody JSONObject json) {
		return iCustomerInfoService.setPaymentPassword(json.getString("customerId"), json.getString("paymentPassword"));
	}

	/**
	 * 验证支付密码
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberVerifyingPaymentPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult verifyingPaymentPassword(@RequestBody JSONObject json) {
		return iCustomerInfoService.verifyingPaymentPassword(json.getString("customerId"),
				json.getString("paymentPassword"));
	}

	/**
	 * 验证手机
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberChangeMobile", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult changeMobile(@RequestBody JSONObject json) {
		return iCustomerInfoService.changeMobile(json.getString("customerId"), json.getString("mobile"));
	}

	/**
	 * 保存app传递的jpushInfo
	 * 
	 * @param jpushInfo
	 * @return
	 */
	@RequestMapping(value = "/keepRegistrationId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult keepRegistrationId(HttpServletRequest request, @RequestBody JpushInfo jpushInfo) {
		String phoneId = request.getHeader("UUID");
		if (StringUtil.isNotBlank(phoneId)) {
			jpushInfo.setPhoneId(phoneId);
		}
		return iCustomerInfoService.insertRegistrationId(jpushInfo);
	}

	/**
	 * 用户个人中心信息
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberCenterOverviewData", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getMemberCenterOverviewData(@RequestBody JSONObject json) {
		return iCustomerInfoService.getMemberCenterOverviewData(json.getString("customerId"));
	}
	
	/**
	 * 判断是否集团组公车
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberCarIsGroup", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberIsGroup(HttpServletRequest request) {
		return iCustomerInfoService.memberIsGroup(request.getHeader("customerId"));
	}
	
	/**
	 * 判断支付密码
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberCheckPayPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberCheckPayPassword(HttpServletRequest request, @RequestBody JSONObject json) {
		return iCustomerInfoService.memberCheckPayPassword(request.getHeader("customerId"), json.getString("payPassword"));
	}

}
