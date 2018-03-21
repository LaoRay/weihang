package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.sms.SMSModel;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DESUtils;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.AccountBenefitMapper;
import cn.com.clubank.weihang.manage.member.dao.CarGroupMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerDeliveryAddressMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.JpushInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.pojo.IntegralRecord;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.IntegralRuleService;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员基本信息Service
 * 
 * @author LeiQY
 *
 */
@Service
@Slf4j
public class CustomerInfoServiceImpl implements ICustomerInfoService {

	@Autowired
	private CustomerInfoMapper customerInfoMapper;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private IntegralRuleService integralRuleService;
	@Autowired
	private CustomerDeliveryAddressMapper deliveryAddressMapper;
	@Autowired
	private JpushInfoMapper jpushInfoMapper;
	@Autowired
	private CarInfoMapper carInfoMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private AccountBenefitMapper benefitMapper;
	@Autowired
	private OrderListMapper orderListMapper;
	@Resource
	private CarGroupMapper carGroupMapper;

	@Autowired
	private BaseUserMapper baseUserMapper;

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("smsQueueDestination")
	private Destination queueDestination;
	@Autowired
	@Qualifier("verificationSms")
	private SMSModel verificationSms;

	@Value("${TOKEN_EXPIRE}")
	private Integer TOKEN_EXPIRE;
	@Value("${RANDOMCODE_EXPIRE}")
	private Integer RANDOMCODE_EXPIRE;

	@Override
	public CustomerInfo selectByPrimaryKey(String cId) {
		return customerInfoMapper.selectByPrimaryKey(cId);
	}

	/**
	 * 获取会员信息
	 */
	@Override
	public CommonResult getCustomerInfo(String customerId) {
		Map<String, Object> map = new HashMap<>();
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		map.put("customerInfo", customerInfo);
		CustomerDeliveryAddress defaultDeliveryAddress = deliveryAddressMapper.selectDefaultByCustomerId(customerId);
		map.put("defaultDeliveryAddress", defaultDeliveryAddress);
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取会员信息成功", map);
	}

	/**
	 * 获取会员等级
	 */
	@Override
	public CommonResult getCustomerLevel(String customerId) {
		Map<String, String> map = customerInfoMapper.selectCustomerLevel(customerId);
		if (map == null) {
			map = new HashMap<>();
			map.put("customerId", customerId);
			map.put("level", "1");
			map.put("benefitName", "普通会员");
			map.put("abId", "");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取会员等级成功", map);
	}

	/**
	 * 会员登录
	 */
	@Override
	public CommonResult customerLogin(CustomerInfo customerInfo, String phoneId) {
		log.info("会员登录：{}", JSON.toJSONStringWithDateFormat(customerInfo, "yyyy-MM-dd HH:mm:ss"));
		try {
			CustomerInfo customer = customerInfoMapper.selectByMobile(customerInfo.getMobile());
			if (customer == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您尚未注册");
			}
			if (customer.getPassword().equals(customerInfo.getPassword())) {
				String token = UUID.randomUUID().toString();
				jedisClient.set("SESSION:" + token, JSONObject.toJSONString(customer));
				jedisClient.expire("SESSION:" + token, TOKEN_EXPIRE); // 设置过期时间
				// 设置上一次访问时间
				customer.setPreviousVisit(customer.getLastVisit());
				// 设置最后访问时间
				customer.setLastVisit(new Date());
				// 修改访问时间
				customerInfoMapper.updateByPrimaryKey(customer);
				// 用户关联极光推送需要的唯一注册码
				JpushInfo jpushInfo = jpushInfoMapper.selectByPhoneId(phoneId);
				if (jpushInfo == null) {
					jpushInfo = new JpushInfo();
					jpushInfo.setPhoneId(phoneId);
					jpushInfo.setUserId(customer.getCId());
					jpushInfoMapper.insert(jpushInfo);
				}
				if (StringUtils.isBlank(jpushInfo.getUserId())) {
					jpushInfo.setUserId(customer.getCId());
					jpushInfoMapper.updateByPhoneId(jpushInfo);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("cId", customer.getCId());
				map.put("token", token);
				return CommonResult.result(ResultCode.SUCC.getValue(), "登陆成功", map);
			} else {
				return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
			}
		} catch (Exception e) {
			log.error("服务器异常，登陆失败", e);
			throw e;
		}
	}

	/**
	 * 会员注销
	 */
	@Override
	public CommonResult customerLogout(String token, String phoneId) {
		log.info("会员注销：token：{}，phoneId：{}", token, phoneId);
		if (StringUtils.isBlank(token)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "注销失败，token为空");
		}
		if (StringUtil.isNotBlank(phoneId)) {
			jpushInfoMapper.deleteByPhoneId(phoneId);
		}
		jedisClient.del("SESSION:" + token);
		return CommonResult.result(ResultCode.SUCC.getValue(), "注销成功");
	}

	/**
	 * 验证码发送
	 */
	@Override
	public CommonResult sendValidate(final String mobile, Integer type) {
		if (StringUtil.isBlank(mobile) || type == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "mobile或type为空");
		}
		try {
			String randomCode = "";
			// 生成验证码
			for (int i = 0; i < 6; i++) {
				randomCode = randomCode + (int) (Math.random() * Constants.INT_9);
			}
			CustomerInfo customerInfo = customerInfoMapper.selectByMobile(mobile);
			BaseUser userInfo = baseUserMapper.selectByMobile(mobile);
			// 0代表该短信针对未使用过的手机号，数据库不存在时才发送（如注册，修改验证手机等）
			if (type == 0 && (customerInfo != null || userInfo != null)) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "手机号已被注册");
			}
			// 1代表短信针对在使用的手机号，数据库存在时才发送（如初始化密码，修改密码，密码找回等）
			if (type == 1 && customerInfo == null && userInfo == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您尚未注册");
			}
			final String[] msText = { randomCode };
			// 异步发短信业务
			jmsTemplate.send(queueDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					JSONObject json = new JSONObject();
					json.put("mobile", mobile);
					json.put("msText", msText);
					json.put("smsModel", verificationSms);
					return session.createTextMessage(json.toString());
				}
			});
			// 缓存验证码
			jedisClient.set("RANDOMCODE:" + mobile, randomCode);
			jedisClient.expire("RANDOMCODE:" + mobile, RANDOMCODE_EXPIRE); // 验证码过期时间
			return CommonResult.result(ResultCode.SUCC.getValue(), "短信发送成功");
		} catch (Exception e) {
			log.error("短信发送异常", e);
			throw e;
		}
	}

	/**
	 * 会员注册
	 */
	@Override
	public CommonResult register(CustomerInfo customerInfo, String randomCode) {
		log.info("会员注册：{}", JSON.toJSONStringWithDateFormat(customerInfo, "yyyy-MM-dd HH:mm:ss"));
		try {
			// 校验验证码
			CommonResult result = checkVerifyCode(customerInfo.getMobile(), randomCode);
			if (result.getApiStatus() != ResultCode.SUCC.getValue()) {
				return result;
			}
			CustomerInfo customer = customerInfoMapper.selectByMobile(customerInfo.getMobile());
			if (customer != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "手机号已被注册");
			}
			Integer integral = 0;
			// 判断是否填写推荐码，如果填写了，去核实推荐人是否存在
			if (!StringUtils.isBlank(customerInfo.getRecommendCode())) {
				customer = customerInfoMapper.selectByRecommendCode(customerInfo.getRecommendCode());
				if (customer == null) {
					return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "推荐人不存在");
				}
				// 如果存在，赠送积分
				Integer value = integralRuleService
						.getIntegralRuleValueByRuleCode(IntegralRule.RECOMMENDRIGISTER_INTEGRAL);
				if (value != null) {
					integral = value;
				}
				// 推荐人id
				customerInfo.setRecommenderId(customer.getCId());
			}
			// 默认昵称
			String nickName = "wh_" + customerInfo.getMobile().substring(0, 6) + StringUtil.randomString(2);
			customerInfo.setNickname(nickName);
			// 增加注册积分
			Integer val = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.REGISTER_INTEGRAL);
			customerInfo.setIntegralAccount(val == null ? 0 : val);
			// 生成唯一推荐码
			customerInfo.setRecommendCode(uniqueRecommendCode());
			// 插入用户信息
			customerInfoMapper.insert(customerInfo);
			// 记录推荐积分流水信息
			if (integral != 0) {
				integralService.updateIntegral(customerInfo.getCId(), IntegralRecord.RECOMMEND, IntegralRecord.INCREASE,
						integral);
			}
			// 记录注册积分流水信息
			if (val != null) {
				integralService.updateIntegral(customerInfo.getCId(), IntegralRecord.REGISTER, IntegralRecord.INCREASE,
						val);
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), "注册成功", customerInfo.getCId());
		} catch (Exception e) {
			log.error("服务器异常，注册失败", e);
			throw e;
		}
	}

	/**
	 * 生成唯一推荐码
	 */
	private String uniqueRecommendCode() {
		// 生成推荐码
		String recommendCode = "wh_" + StringUtil.randomString(8);
		if (customerInfoMapper.selectByRecommendCode(recommendCode) != null) {
			recommendCode = uniqueRecommendCode();
		}
		return recommendCode;
	}

	/**
	 * 校验验证码
	 */
	@Override
	public CommonResult checkVerifyCode(String mobile, String randomCode) {
		try {
			String code = jedisClient.get("RANDOMCODE:" + mobile);
			// 未获取到验证码，说明验证码已过期
			if (code == null) {
				return CommonResult.result(ResultCode.CODE_DUE.getValue(), "验证码已过期");
			}
			// 验证码不匹配
			if (!randomCode.equals(code)) {
				return CommonResult.result(ResultCode.CODE_ERROR.getValue(), "验证码不正确");
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), "校验通过");
		} catch (Exception e) {
			log.error("服务器内部错误，校验失败", e);
			throw e;
		}
	}

	/**
	 * 密码重置
	 */
	@Override
	public CommonResult resetPassword(String mobile, String password) {
		if (StringUtil.isBlank(mobile) || StringUtil.isBlank(password)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "mobile或password空");
		}
		try {
			CustomerInfo customerInfo = customerInfoMapper.selectByMobile(mobile);
			if (customerInfo == null) {
				log.info("参数错误：mobile【" + mobile + "】，password【" + password + "】！");
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "手机号码不存在");
			}
			// 用户新输入的密码覆盖原有密码
			customerInfo.setPassword(password);
			// 更新用户信息，重置密码
			customerInfoMapper.updateByPrimaryKey(customerInfo);
			return CommonResult.result(ResultCode.SUCC.getValue(), "密码重置成功");
		} catch (Exception e) {
			log.error("服务器内部错误，密码重置失败", e);
			throw e;
		}
	}

	/**
	 * 修改登录密码
	 */
	@Override
	public CommonResult modifyPassword(String customerId, String password) {
		CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customer == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您尚未注册");
		}
		customer.setPassword(password);
		customerInfoMapper.updateSelectiveByPrimaryKey(customer);
		return CommonResult.result(ResultCode.SUCC.getValue(), "修改密码成功");
	}

	/**
	 * 补全会员信息
	 */
	@Override
	public CommonResult completeInformation(CustomerInfo customerInfo) {
		try {
			if (StringUtils.isBlank(customerInfo.getCId())
					|| customerInfoMapper.selectByPrimaryKey(customerInfo.getCId()) == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您还未注册");
			}
			customerInfoMapper.updateSelectiveByPrimaryKey(customerInfo);
			return CommonResult.result(ResultCode.SUCC.getValue(), "补全信息成功");
		} catch (Exception e) {
			log.error("服务器内部错误，信息补全失败", e);
			throw e;
		}
	}

	/**
	 * 获取会员收货地址
	 */
	@Override
	public CommonResult getDeliveryAddressList(String customerId) {
		List<CustomerDeliveryAddress> customerDeliveryAddressList = deliveryAddressMapper
				.selectByCustomerId(customerId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取收货地址列表成功", customerDeliveryAddressList);
	}

	/**
	 * 新增会员收货地址
	 */
	@Override
	public CommonResult addCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress) {
		try {
			// 判断该地址是否被设置为默认地址，如果设为默认，就将该用户下其他地址取消默认
			defalutAddress(deliveryAddress);
			deliveryAddressMapper.insert(deliveryAddress);
			return CommonResult.result(ResultCode.SUCC.getValue(), "新增收货地址成功");
		} catch (Exception e) {
			log.error("服务器内部错误，新增收货地址失败", e);
			throw e;
		}
	}

	private void defalutAddress(CustomerDeliveryAddress deliveryAddress) {
		// 判断是否设置为默认
		boolean isDefault = deliveryAddress.getIsDefault();
		if (isDefault) {
			// 如果设置为默认，将该用户下其他默认地址取消
			deliveryAddressMapper.updateDeliveryAddressCancelDefaultByCustomerId(deliveryAddress.getCustomerId());
		}
	}

	/**
	 * 修改会员收货地址信息
	 */
	@Override
	public CommonResult modifyCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress) {
		// 收货地址id为空，则新增
		if (StringUtils.isBlank(deliveryAddress.getDaId())) {
			return addCustomerDeliveryAddress(deliveryAddress);
		}
		// 收货地址id不为空，则为修改
		return updateCustomerDeliveryAddress(deliveryAddress);
	}

	/**
	 * 更新会员收货地址信息
	 */
	@Override
	public CommonResult updateCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress) {
		try {
			// 判断该地址是否被设置为默认地址，如果设为默认，就将该用户下其他地址取消默认
			defalutAddress(deliveryAddress);
			deliveryAddressMapper.updateSelectiveByPrimaryKey(deliveryAddress);
			return CommonResult.result(ResultCode.SUCC.getValue(), "修改收货地址成功");
		} catch (Exception e) {
			log.error("服务器内部错误，修改收货地址失败", e);
			throw e;
		}
	}

	/**
	 * 删除收货地址
	 */
	@Override
	public CommonResult deleteCustomerDeliveryAddress(String daId) {
		deliveryAddressMapper.deleteByPrimaryKey(daId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除收货地址成功");
	}

	/**
	 * 保存app传递的jpushInfo
	 */
	@Override
	public CommonResult insertRegistrationId(JpushInfo jpushInfo) {
		try {
			JpushInfo jpush = jpushInfoMapper.selectByPhoneId(jpushInfo.getPhoneId());
			if (jpush == null) {
				jpushInfoMapper.insert(jpushInfo);
			} else {
				jpush.setJpushId(jpushInfo.getJpushId());
				jpushInfoMapper.updateByPhoneId(jpush);
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), "保存成功");
		} catch (Exception e) {
			log.error("服务器内部错误，保存失败", e);
			throw e;
		}
	}

	/**
	 * 设置或修改支付密码
	 */
	@Override
	public CommonResult setPaymentPassword(String customerId, String paymentPassword) {
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		customerInfo.setPaymentPassword(paymentPassword);
		customerInfoMapper.updateSelectiveByPrimaryKey(customerInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "支付密码更新成功");
	}

	/**
	 * 验证支付密码
	 */
	@Override
	public CommonResult verifyingPaymentPassword(String customerId, String paymentPassword) {
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(paymentPassword)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "会员id或支付密码为空");
		}
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "尚未注册");
		}
		if (paymentPassword.equals(customerInfo.getPaymentPassword())) {
			return CommonResult.result(ResultCode.SUCC.getValue(), "验证通过");
		} else {
			return CommonResult.result(ResultCode.PASSWORD_ERROR.getValue(), "密码错误，验证失败");
		}
	}

	/**
	 * 验证手机
	 */
	@Override
	public CommonResult changeMobile(String customerId, String mobile) {
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		customerInfo.setMobile(mobile);
		customerInfoMapper.updateSelectiveByPrimaryKey(customerInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "手机号更换成功");
	}

	/**
	 * 用户个人中心信息
	 */
	@Override
	public CommonResult getMemberCenterOverviewData(String customerId) {
		Map<String, Object> map = new HashMap<>();
		// 账户积分
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "参数错误或者不存在客户信息！");
		}
		map.put("integralAccount", customerInfo.getIntegralAccount());
		// 优惠券数量
		int couponNum = couponListMapper.selectCountByCustomerId(customerId);
		map.put("counponNum", couponNum);
		// 账户金额
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		map.put("acount", carInfo == null ? Constants.NULL : carInfo.getAccount());
		// 账户等级
		AccountBenefit abInfo = benefitMapper.selectByPrimaryKey(carInfo == null ? Constants.NULL : carInfo.getAbId());
		map.put("level", abInfo == null ? Constants.INT_1 : abInfo.getLevel());
		// 待付款
		int noPay = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_1);
		map.put("noPay", noPay);
		// 待收货
		int noReceipt = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_2);
		map.put("noReceipt", noReceipt);
		// 待评价
		int noComment = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_3);
		map.put("noComment", noComment);
		// 预购订单
		int preOrder = orderListMapper.selectCountPreOrder(customerId);
		map.put("preOrder", preOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询用户信息成功", map);
	}

	@Override
	public CommonResult groupLogin(String account, String password) {
		log.info("集团组登录：{}", account);
		if (StringUtil.isBlank(account) || StringUtil.isBlank(password)) {
			log.info("参数不能为空：{}, {}", account, password);
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}

		CarGroup group = carGroupMapper.selectByAccount(account);
		if (group == null) {
			log.info("帐号不存在：{}", account);
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "帐号不存在");
		}
		if (group.getPassword().equals(password)) {
			String token = UUID.randomUUID().toString();
			jedisClient.set("SESSION:" + token, JSONObject.toJSONString(group));
			jedisClient.expire("SESSION:" + token, TOKEN_EXPIRE); // 设置过期时间

			Map<String, String> map = new HashMap<String, String>();
			map.put("groupId", group.getGroupId());
			map.put("token", token);
			return CommonResult.result(ResultCode.SUCC.getValue(), "登陆成功", map);
		} else {
			log.info("密码错误：{}, {}", account, password);
			return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
		}
	}

	@Override
	public String selectCustomerListAndPaged(String realname, String nickname, Integer status, Integer pageIndex,
			Integer pageSize) {
		PageObject<CustomerInfo> page = new PageObject<>(pageIndex, pageSize);

		int total = customerInfoMapper.selectCount(realname, nickname, status);
		List<CustomerInfo> list = customerInfoMapper.selectCustomerListByNameOrStatusPaged(realname, nickname, status,
				page.getStart(), pageSize);

		List<CustomerInfo> customers = new ArrayList<>();

		for (CustomerInfo customerInfo : list) {

			if (!StringUtils.isBlank(customerInfo.getRecommenderId())) {
				CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(customerInfo.getRecommenderId());

				customerInfo.setRecommenderName(customer.getRealname());
				customers.add(customerInfo);
			} else {
				customers.add(customerInfo);
			}

		}

		page.setRows(total);
		page.setDataList(customers);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectCustomerDetail(String cId) {
		JSONObject json = new JSONObject();

		CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(cId);
		if (customer == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", customer);
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", customer);
		}
		return json.toString();
	}

	@Override
	public String insertOrUpdateCustomer(CustomerInfo customer) {
		JSONObject json = new JSONObject();

		if (StringUtils.isBlank(customer.getCId())) {

			if (!StringUtils.isBlank(customer.getPassword())) {
				customer.setPassword(DESUtils.weihEncode(customer.getPassword()));// 加密登录密码
			}

			if (!StringUtils.isBlank(customer.getPaymentPassword())) {
				customer.setPaymentPassword(DESUtils.weihEncode(customer.getPaymentPassword()));// 加密支付密码
			}

			customerInfoMapper.insert(customer);
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "新增成功");
		} else {
			customerInfoMapper.updateSelectiveByPrimaryKey(customer);
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "编辑成功");
		}
		return json.toString();
	}
}
