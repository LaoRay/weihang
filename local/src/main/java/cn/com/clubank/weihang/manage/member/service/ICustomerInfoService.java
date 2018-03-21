package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;

/**
 * 客户信息表（业务逻辑层接口）
 * 
 * @author LeiQY
 *
 */
public interface ICustomerInfoService {

	/**
	 * 通过主键ID查询数据
	 * 
	 * @param cId
	 * @return
	 */
	CustomerInfo selectByPrimaryKey(String cId);

	/**
	 * 获取会员信息
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult getCustomerInfo(String customerId);

	/**
	 * 会员登录
	 * 
	 * @param customerInfo
	 * @param phoneId
	 * @return 会员登录结果信息
	 */
	CommonResult customerLogin(CustomerInfo customerInfo, String phoneId);

	/**
	 * 集团组登录
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	CommonResult groupLogin(String account, String password);

	/**
	 * 会员注销登录
	 * 
	 * @param token
	 * @return
	 */
	CommonResult customerLogout(String token, String phoneId);

	/**
	 * 验证码发送
	 * 
	 * @param mobile
	 * @param type
	 * @return
	 * @throws Exception
	 */
	CommonResult sendValidate(String mobile, Integer type);

	/**
	 * 会员注册
	 * 
	 * @param customerInfo
	 * @param randomCode
	 * @return
	 */
	CommonResult register(CustomerInfo customerInfo, String randomCode);

	/**
	 * 校验验证码
	 * 
	 * @param randomCode
	 * @return
	 */
	CommonResult checkVerifyCode(String mobile, String randomCode);

	/**
	 * 密码重置
	 * 
	 * @param customerId
	 * @param password
	 * @return
	 */
	CommonResult resetPassword(String customerId, String password);

	/**
	 * 补全会员信息
	 * 
	 * @param customerInfo
	 * @return
	 */
	CommonResult completeInformation(CustomerInfo customerInfo);

	/**
	 * 获取会员收货地址
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult getDeliveryAddressList(String customerId);

	/**
	 * 新增会员收货地址
	 * 
	 * @param deliveryAddress
	 * @return
	 */
	CommonResult addCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress);

	/**
	 * 更新会员收货地址信息
	 */
	CommonResult updateCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress);

	/**
	 * 修改会员收货地址信息
	 * 
	 * @param deliveryAddress
	 * @return
	 */
	CommonResult modifyCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress);

	/**
	 * 删除收货地址
	 * 
	 * @param daId
	 * @return
	 */
	CommonResult deleteCustomerDeliveryAddress(String daId);

	/**
	 * 修改密码
	 * 
	 * @param customerId
	 * @param password
	 * @return
	 */
	CommonResult modifyPassword(String customerId, String password);

	/**
	 * 保存app传递的jpushInfo
	 * 
	 * @param jpushInfo
	 * @return
	 */
	CommonResult insertRegistrationId(JpushInfo jpushInfo);

	/**
	 * 设置支付密码
	 * 
	 * @param customerId
	 * @param paymentPassword
	 * @return
	 */
	CommonResult setPaymentPassword(String customerId, String paymentPassword);

	/**
	 * 验证支付密码
	 * 
	 * @param customerId
	 * @param paymentPassword
	 * @return
	 */
	CommonResult verifyingPaymentPassword(String customerId, String paymentPassword);

	/**
	 * 验证手机
	 * 
	 * @param customerId
	 * @param mobile
	 * @return
	 */
	CommonResult changeMobile(String customerId, String mobile);

	/**
	 * 用户个人中心信息
	 * 
	 * @param string
	 * @return
	 */
	CommonResult getMemberCenterOverviewData(String customerId);

	/**
	 * 获取会员等级
	 * 
	 * @param string
	 * @return
	 */
	CommonResult getCustomerLevel(String customerId);

	/**
	 * 按会员姓名、昵称模糊查询，会员状态查询并分页
	 * 
	 * @param realname
	 *            会员姓名（真实姓名）
	 * @param nickname
	 *            会员昵称
	 * @param status
	 *            会员状态
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectCustomerListAndPaged(String realname, String nickname, Integer status, Integer pageIndex,
			Integer pageSize);

	/**
	 * 获得会员详情
	 * 
	 * @param cId
	 *            会员ID
	 * @return
	 */
	String selectCustomerDetail(String cId);

	/**
	 * 新增或编辑会员信息
	 * 
	 * @param customer
	 * @return
	 */
	String insertOrUpdateCustomer(CustomerInfo customer);
}
