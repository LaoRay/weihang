package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
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
	public CommonResult resetPassword(@RequestBody JSONObject json) {
		return iCustomerInfoService.resetPassword(json.getString("mobile"), json.getString("password"));
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
	 * 修改会员收货地址信息
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
	public CommonResult keepRegistrationId(@RequestBody JpushInfo jpushInfo) {
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

}
