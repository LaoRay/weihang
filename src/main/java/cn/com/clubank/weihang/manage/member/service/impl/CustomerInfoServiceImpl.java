package cn.com.clubank.weihang.manage.member.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DESUtils;
import cn.com.clubank.weihang.common.util.MergeCellsUtil;
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
import cn.com.clubank.weihang.manage.member.dao.ThirdLoginInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.pojo.IntegralRecord;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;
import cn.com.clubank.weihang.manage.member.pojo.ThirdLoginInfo;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.pay.service.WeihPayService;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.dao.ShoppingCartMapper;
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
	private ThirdLoginInfoMapper thirdLoginInfoMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private HttpServletRequest request;
	@Resource
	private WeihPayService weihPayService;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;

	@Autowired
	@Qualifier("smsQueueDestination")
	private Destination queueDestination;
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
	public CommonResult customerLogin(String mobile, String carNo, String password, String phoneId, Integer flatType) {
		log.info("会员登录：mobile:{},carNo:{},password:{},phoneId:{},flatType:{}", mobile, carNo, password, phoneId,
				flatType);
		CustomerInfo customer = null;
		if (StringUtils.isBlank(password)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "密码不能为空");
		}
		if (StringUtils.isBlank(mobile)) {
			customer = customerInfoMapper.selectByCarNo(carNo);
		}
		if (StringUtils.isBlank(carNo)) {
			customer = customerInfoMapper.selectByMobile(mobile);
		}
		if (customer == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您尚未注册");
		}
		if (customer.getStatus() == 2) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "该账号已无效，请联系威航管理员");
		}
		if (flatType == Constants.INT_4) {
			// pc网站登录需要加密
			password = DESUtils.weihEncode(password);
		}
		if (!password.equals(customer.getPassword())) {
			return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
		}
		Map<String, String> map = saveTokenAndJpushInfo(phoneId, customer, flatType);
		return CommonResult.result(ResultCode.SUCC.getValue(), "登录成功", map);
	}

	/**
	 * 缓存token和保存极光注册信息
	 */
	private Map<String, String> saveTokenAndJpushInfo(String phoneId, CustomerInfo customer, Integer flatType) {
		String token = UUID.randomUUID().toString();
		// session中保存的内容
		JSONObject session = new JSONObject();
		session.put("operateMobile", customer.getMobile());// 操作用户手机
		session.put("operateName", customer.getRealname());// 操作用户
		session.put("flatType", flatType);
		jedisClient.set("SESSION:" + token, session.toString());
		jedisClient.expire("SESSION:" + token, TOKEN_EXPIRE); // 设置过期时间
		// 设置上一次访问时间
		customer.setPreviousVisit(customer.getLastVisit());
		// 设置最后访问时间
		customer.setLastVisit(new Date());
		// 修改访问时间
		customerInfoMapper.updateByPrimaryKey(customer);
		// 用户关联极光推送需要的唯一注册码
		if (StringUtils.isNotBlank(phoneId)) {
			JpushInfo jpushInfo = jpushInfoMapper.selectByPhoneId(phoneId);
			if (jpushInfo == null) {
				jpushInfo = new JpushInfo();
				jpushInfo.setPhoneId(phoneId);
				jpushInfo.setUserId(customer.getCId());
				jpushInfo.setFlatType(flatType);
				jpushInfoMapper.insert(jpushInfo);
				log.info("保存极光推送信息数据：{}", JSON.toJSONStringWithDateFormat(jpushInfo, Constants.DATE_COMMON));
			}
			if (StringUtils.isBlank(jpushInfo.getUserId())) {
				jpushInfo.setUserId(customer.getCId());
				jpushInfo.setModifyTime(new Date());
				jpushInfoMapper.updateByPhoneId(jpushInfo);
				log.info("更新极光推送信息数据：{}", JSON.toJSONStringWithDateFormat(jpushInfo, Constants.DATE_COMMON));
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("cId", customer.getCId());
		map.put("token", token);
		// 将登录成功的token放到request的参数中，记录日志使用
		request.setAttribute("token", token);
		return map;
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
		JpushInfo jp = jpushInfoMapper.selectByPhoneId(phoneId);
		if (jp != null) {
			jp.setUserId(null);
			jpushInfoMapper.updateByPrimaryKey(jp);
		}
		Long result = jedisClient.del("SESSION:" + token);
		log.info("会员注销，删除redis的key【{}】，删除结果：{}", "SESSION:" + token, result);
		return CommonResult.result(ResultCode.SUCC.getValue(), "注销成功");
	}

	/**
	 * 验证码发送
	 */
	@Override
	public CommonResult sendValidate(final String mobile, Integer type) {
		log.info("发送验证码：mobile：{}，type：{}", mobile, type);
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
			if (type == 0 && customerInfo != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "手机号已被注册");
			}
			// 1代表短信针对在使用的手机号，数据库存在时才发送（如初始化密码，修改密码，密码找回等）
			if (type == 1 && customerInfo == null && userInfo == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "您尚未注册");
			}
			final String code = randomCode;
			// 异步发短信业务
			jmsTemplate.send(queueDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					JSONObject json = new JSONObject();
					json.put("mobile", mobile);
					json.put("code", code);
					TextMessage textMessage = session.createTextMessage(json.toString());
					return textMessage;
				}
			});
			// 缓存验证码
			jedisClient.set("RANDOMCODE:" + mobile, randomCode);
			jedisClient.expire("RANDOMCODE:" + mobile, RANDOMCODE_EXPIRE); // 验证码过期时间
			log.info("验证码：mobile：{}，randomCode：{}", mobile, randomCode);
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
	public CommonResult register(CustomerInfo customerInfo, String randomCode, boolean pwdIsDes) {
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
			// 判断是否填写推荐码，如果填写了，去核实推荐人是否存在
			if (!StringUtils.isBlank(customerInfo.getRecommendCode())) {
				customer = customerInfoMapper.selectByRecommendCode(customerInfo.getRecommendCode());
				if (customer == null) {
					return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "推荐人不存在");
				}
				// 推荐积分
				integralService.recommendIntegral(customer.getCId());
				// 推荐人id
				customerInfo.setRecommenderId(customer.getCId());
			}
			// 默认昵称
			String nickName = "wh_" + customerInfo.getMobile().substring(0, 6) + StringUtil.randomString(2);
			customerInfo.setNickname(nickName);
			// 生成唯一推荐码
			customerInfo.setRecommendCode(uniqueRecommendCode());
			if (pwdIsDes) {
				// 密码需要加密
				customerInfo.setPassword(DESUtils.weihEncode(customerInfo.getPassword()));
			}
			// 插入用户信息
			customerInfoMapper.insert(customerInfo);
			// 添加虚拟车辆
			CarInfo car = new CarInfo();
			car.setCarNo(StringUtil.getVirtualCarNo());
			car.setCustomerId(customerInfo.getCId());
			car.setIsDefault(true);
			car.setAccount(new BigDecimal(Constants.INT_0));
			car.setIsVirtual(true);
			carInfoMapper.insert(car);
						
			// 注册积分
			Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.REGISTER_INTEGRAL);
			// 记录注册积分流水信息并修改客户积分账户
			if (value != null) {
				integralService.updateIntegral(customerInfo.getCId(), IntegralRecord.REGISTER, IntegralRecord.INCREASE,
						value);
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
		if (StringUtil.isBlank(mobile) || StringUtil.isBlank(randomCode)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
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
	public CommonResult resetPassword(String mobile, String password, boolean pwdIsDes) {
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
			if (pwdIsDes) {
				customerInfo.setPassword(DESUtils.weihEncode(password));
			} else {
				customerInfo.setPassword(password);
			}
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
		// 判断该地址是否被设置为默认地址，如果设为默认，就将该用户下其他地址取消默认
		defalutAddress(deliveryAddress);
		deliveryAddressMapper.insert(deliveryAddress);
		return CommonResult.result(ResultCode.SUCC.getValue(), "新增收货地址成功");
	}

	private void defalutAddress(CustomerDeliveryAddress deliveryAddress) {
		// 判断是否设置为默认
		boolean isDefault = deliveryAddress.getIsDefault() == null ? false : deliveryAddress.getIsDefault();
		if (isDefault) {
			// 如果设置为默认，将该用户下其他默认地址取消
			deliveryAddressMapper.updateDeliveryAddressCancelDefaultByCustomerId(deliveryAddress.getCustomerId());
		}
	}

	/**
	 * 新增或修改会员收货地址信息
	 */
	@Override
	public CommonResult modifyCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress) {
		if (deliveryAddress == null || StringUtils.isBlank(deliveryAddress.getCustomerId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 收货地址id为空，则新增
		if (StringUtils.isBlank(deliveryAddress.getDaId())) {
			return addCustomerDeliveryAddress(deliveryAddress);
		}
		// 收货地址id不为空，则为修改
		return updateCustomerDeliveryAddress(deliveryAddress);
	}

	/**
	 * 根据地址主键查询地址详情
	 */
	@Override
	public CommonResult findDeliveryAddressDetail(String daId) {
		if (StringUtils.isBlank(daId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		Map<String, String> map = deliveryAddressMapper.selectDetailByPrimaryKey(daId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 更新会员收货地址信息
	 */
	@Override
	public CommonResult updateCustomerDeliveryAddress(CustomerDeliveryAddress deliveryAddress) {
		// 判断该地址是否被设置为默认地址，如果设为默认，就将该用户下其他地址取消默认
		defalutAddress(deliveryAddress);
		deliveryAddressMapper.updateSelectiveByPrimaryKey(deliveryAddress);
		return CommonResult.result(ResultCode.SUCC.getValue(), "修改收货地址成功");
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
		log.info("保存极光注册码和手机UUID之间数据：{}", JSON.toJSONStringWithDateFormat(jpushInfo, Constants.DATE_COMMON));
		if (jpushInfo == null || StringUtils.isBlank(jpushInfo.getPhoneId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		JpushInfo jpush = jpushInfoMapper.selectByPhoneId(jpushInfo.getPhoneId());
		if (jpush == null) {
			jpushInfoMapper.insert(jpushInfo);
		} else {
			jpush.setJpushId(jpushInfo.getJpushId());
			jpushInfoMapper.updateByPhoneId(jpush);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "保存成功");
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
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "尚未注册");
		}
		if (!paymentPassword.equals(customerInfo.getPaymentPassword())) {
			return CommonResult.result(ResultCode.PASSWORD_ERROR.getValue(), "密码错误，验证失败");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "验证通过");
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
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "参数错误或者不存在客户信息！");
		}
		// 手机号码
		map.put("mobile", customerInfo.getMobile());
		// 昵称
		map.put("nickname", customerInfo.getNickname());
		// 头像
		map.put("headicon", customerInfo.getHeadicon());
		// 账户积分
		map.put("integralAccount", customerInfo.getIntegralAccount());
		// 优惠券数量
		int couponNum = couponListMapper.selectCountByCustomerId(customerId);
		map.put("counponNum", couponNum);
		// 账户金额
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		map.put("acount", carInfo == null ? Constants.INT_0 : carInfo.getAccount());
		// 账户等级
		AccountBenefit abInfo = benefitMapper.selectByPrimaryKey(carInfo == null ? Constants.NULL : carInfo.getAbId());
		map.put("level", abInfo == null ? Constants.INT_1 : abInfo.getLevel());
		// 待付款
		int noPay = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_1, Constants.INT_1);
		map.put("noPay", noPay);
		// 待收货
		int noReceipt = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_2, Constants.INT_1);
		map.put("noReceipt", noReceipt);
		// 待评价
		int noComment = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_3, Constants.INT_1);
		map.put("noComment", noComment);
		// 预购订单(已预约)
		int preOrder = orderListMapper.selectCountByOrderStatus(customerId, Constants.INT_6, Constants.INT_1);
		map.put("preOrder", preOrder);
		// 购物车数量
		int cartNum = shoppingCartMapper.selectCountByCustomerId(customerId);
		map.put("cartNum", cartNum);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询用户信息成功", map);
	}

	@Override
	public CommonResult groupLogin(String account, String password, Integer flatType) {
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
			// session中保存的内容
			JSONObject session = new JSONObject();
			session.put("operateMobile", group.getMobile());// 操作用户手机
			session.put("operateName", group.getGroupName());// 操作用户
			session.put("flatType", flatType);
			jedisClient.set("SESSION:" + token, session.toString());
			jedisClient.expire("SESSION:" + token, TOKEN_EXPIRE); // 设置过期时间

			Map<String, String> map = new HashMap<String, String>();
			map.put("groupId", group.getGroupId());
			map.put("token", token);

			// 将登录成功的token放到request的参数中，记录日志使用
			request.setAttribute("token", token);
			return CommonResult.result(ResultCode.SUCC.getValue(), "登录成功", map);
		} else {
			log.info("密码错误：{}, {}", account, password);
			return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
		}
	}

	@Override
	public String selectCustomerListAndPaged(String realname, String nickname, String carNo, Integer status,
			Integer pageIndex, Integer pageSize) {
		PageObject<CustomerInfo> page = new PageObject<>(pageIndex, pageSize);

		int total = customerInfoMapper.selectCount(carNo, realname, nickname, status);
		List<CustomerInfo> list = customerInfoMapper.selectCustomerListByNameOrStatusPaged(realname, nickname, carNo,
				status, page.getStart(), pageSize);

		List<CustomerInfo> customers = new ArrayList<>();

		for (CustomerInfo customerInfo : list) {
			CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(customerInfo.getRecommenderId());
			customerInfo.setRecommenderName(customer != null ? customer.getRealname() : "");
			customers.add(customerInfo);
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

			// 判断会员是否已经存在（通过手机号判断）
			CustomerInfo customerInfo = customerInfoMapper.selectByMobile(customer.getMobile());
			if (customerInfo != null) {
				json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
				json.put("msg", "会员已存在");
				return json.toString();
			}

			if (!StringUtils.isBlank(customer.getPassword())) {
				customer.setPassword(DESUtils.weihEncode(customer.getPassword()));// 加密登录密码
			}

			if (!StringUtils.isBlank(customer.getPaymentPassword())) {
				customer.setPaymentPassword(DESUtils.weihEncode(customer.getPaymentPassword()));// 加密支付密码
			}

			if (StringUtil.isBlank(customer.getNickname())) {
				// 默认昵称
				String nickName = "wh_" + customer.getMobile().substring(0, 6) + StringUtil.randomString(2);
				customer.setNickname(nickName);
			}
			// 生成唯一推荐码
			customer.setRecommendCode(uniqueRecommendCode());
			customerInfoMapper.insert(customer);
			// 添加虚拟车辆
			CarInfo car = new CarInfo();
			car.setCarNo(StringUtil.getVirtualCarNo());
			car.setCustomerId(customer.getCId());
			car.setIsDefault(true);
			car.setAccount(new BigDecimal(Constants.INT_0));
			car.setIsVirtual(true);
			carInfoMapper.insert(car);

			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "新增成功");
		} else {
			customerInfoMapper.updateSelectiveByPrimaryKey(customer);
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "编辑成功");
		}
		return json.toString();
	}

	/**
	 * app第三方登录
	 */
	@Override
	public CommonResult memberThirdLogin(String thirdId, String phoneId, Integer flatType) {
		if (StringUtils.isBlank(thirdId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		ThirdLoginInfo thirdLoginInfo = thirdLoginInfoMapper.selectByThirdId(thirdId);
		if (thirdLoginInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOBINDING.getValue(), "未绑定手机号");
		}
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(thirdLoginInfo.getCustomerId());
		if (customerInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "用户不存在");
		}
		Map<String, String> map = saveTokenAndJpushInfo(phoneId, customerInfo, flatType);
		return CommonResult.result(ResultCode.SUCC.getValue(), "登录成功", map);
	}

	/**
	 * 绑定第三方信息并注册会员
	 */
	@Override
	public CommonResult memberBindingThirdInfo(CustomerInfo customerInfo, ThirdLoginInfo thirdLoginInfo,
			String randomCode, Boolean isExist, String phoneId, Integer flatType) {
		log.info("会员绑定：{}", JSON.toJSONStringWithDateFormat(customerInfo, "yyyy-MM-dd HH:mm:ss"));
		CommonResult result = checkVerifyCode(customerInfo.getMobile(), randomCode);
		if (result.getApiStatus() != ResultCode.SUCC.getValue()) {
			return result;
		}
		CustomerInfo customer = null;
		// 已有账号，直接绑定
		if (isExist) {
			customer = customerInfoMapper.selectByMobile(customerInfo.getMobile());
			if (customer == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "帐号不存在");
			}
			thirdLoginInfo.setCustomerId(customer.getCId());
		}
		// 未注册，先注册再绑定
		else {
			// 未填写昵称
			if (StringUtils.isBlank(customerInfo.getNickname())) {
				String nickName = "wh_" + customerInfo.getMobile().substring(0, 6) + StringUtil.randomString(2);
				customerInfo.setNickname(nickName);
			}
			customerInfoMapper.insert(customerInfo);
			// 添加虚拟车辆
			CarInfo car = new CarInfo();
			car.setCarNo(StringUtil.getVirtualCarNo());
			car.setCustomerId(customerInfo.getCId());
			car.setIsDefault(true);
			car.setAccount(new BigDecimal(Constants.INT_0));
			car.setIsVirtual(true);
			carInfoMapper.insert(car);
			
			// 注册积分
			Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.REGISTER_INTEGRAL);
			// 记录注册积分流水信息并修改客户积分账户
			if (value != null) {
				integralService.updateIntegral(customerInfo.getCId(), IntegralRecord.REGISTER, IntegralRecord.INCREASE,
						value);
			}

			thirdLoginInfo.setCustomerId(customerInfo.getCId());
			customer = customerInfoMapper.selectByMobile(customerInfo.getMobile());
		}
		thirdLoginInfoMapper.insert(thirdLoginInfo);
		// 登录
		Map<String, String> map = saveTokenAndJpushInfo(phoneId, customer, flatType);
		return CommonResult.result(ResultCode.SUCC.getValue(), "登录成功", map);
	}

	@Override
	public CommonResult clientMemberRecharge(String customerId, BigDecimal total, Integer accountPayType, String desc) {
		return weihPayService.handleCustomerRecharge(customerId, "weihclientRecharge", total, 1, accountPayType, desc);
	}

	@Override
	public void exportMemberList(HttpServletRequest request, HttpServletResponse response) {

		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("会员数据");
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// 样式字体水平居中
		// 设置前景填充色
		style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
		// 设置前景填充样式
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// 创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("会员名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("联系电话");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("车牌号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("车架号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("车辆等级");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("车辆余额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("车辆品牌");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("车辆年限");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("发动机排量");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("发动机号");
		cell.setCellStyle(style);

		String realname = request.getParameter("realname");
		String nickname = request.getParameter("nickname");
		String carNo = request.getParameter("carNo");
		Integer status = request.getParameter("status") == "" || request.getParameter("status") == null ? null
				: Integer.parseInt(request.getParameter("status"));
		try {
			// 解决中文乱码
			realname = URLDecoder.decode(realname, "UTF-8");
			nickname = URLDecoder.decode(nickname, "UTF-8");
			carNo = URLDecoder.decode(carNo, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("参数转换异常", e);
		}

		List<Map<String, Object>> memberList = customerInfoMapper.exportMemberInfo(realname, nickname, carNo, status);

		if (!memberList.isEmpty()) {
			for (short i = 0; i < memberList.size(); i++) {
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(
						memberList.get(i).get("realname") == null ? "" : memberList.get(i).get("realname").toString());
				row.createCell(1).setCellValue(
						memberList.get(i).get("mobile") == null ? "" : memberList.get(i).get("mobile").toString());
				row.createCell(2).setCellValue(
						memberList.get(i).get("carNo") == null ? "" : memberList.get(i).get("carNo").toString());
				row.createCell(3).setCellValue(
						memberList.get(i).get("carVin") == null ? "" : memberList.get(i).get("carVin").toString());
				row.createCell(4).setCellValue(memberList.get(i).get("benefitName") == null ? ""
						: memberList.get(i).get("benefitName").toString());
				row.createCell(5).setCellValue(
						memberList.get(i).get("account") == null ? "" : memberList.get(i).get("account").toString());
				row.createCell(6).setCellValue(
						memberList.get(i).get("carBrand") == null ? "" : memberList.get(i).get("carBrand").toString());
				row.createCell(7).setCellValue(
						memberList.get(i).get("years") == null ? "" : memberList.get(i).get("years").toString());
				row.createCell(8).setCellValue(memberList.get(i).get("exhaustAmount") == null ? ""
						: memberList.get(i).get("exhaustAmount").toString());
				row.createCell(9).setCellValue(memberList.get(i).get("engineNumber") == null ? ""
						: memberList.get(i).get("engineNumber").toString());
			}

			// 处理相同的数据合并单元格
			MergeCellsUtil.addMemberInfoMergedRegion(sheet, 1, 1, sheet.getLastRowNum(), wb);
		}

		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName = new String(("会员信息表" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),
					"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			wb.close();
			log.info("导出会员信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出会员信息失败", e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("导出会员信息失败", e);
		}
	}

	@Override
	public CommonResult memberIsGroup(String customerId) {
		CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		if (customerInfo == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "参数错误或者不存在客户信息！");
		}
		// 默认车辆
		CarInfo car = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		if (car != null && StringUtil.isNotBlank(car.getGroupId())) {
			return CommonResult.result(ResultCode.SUCC.getValue(), "是企业公车", 1);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "不是企业公车", 0);
	}

	@Override
	public CommonResult memberCheckPayPassword(String customerId, String payPassword) {
		if (StringUtil.isBlank(customerId)) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "参数不能为空！");
		}
		if (StringUtil.isBlank(payPassword)) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "密码不能为空！");
		}
		CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(customerId);
		if (cus == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "用户信息不存在！");
		}
		if (StringUtil.isBlank(cus.getPaymentPassword())) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "您未设置支付密码，不能使用账号支付");
		}
		if (!DESUtils.weihEncode(payPassword).equals(cus.getPaymentPassword())) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "支付密码错误");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "验证成功");
	}
}
