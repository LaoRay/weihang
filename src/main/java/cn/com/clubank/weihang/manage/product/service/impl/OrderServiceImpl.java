package cn.com.clubank.weihang.manage.product.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.MergeCellsUtil;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.pay.service.WordRefundService;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityPurRecordMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityPurRecord;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 订单管理
 * 
 * @author LeiQY
 *
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderListMapper orderListMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private CarInfoMapper carInfoMapper;
	@Autowired
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private CarAccountRecordMapper carAccountRecordMapper;
	@Autowired
	private MallActivityGoodsMapper activityGoodsMapper;
	@Autowired
	private BaseCodeRuleService baseCodeRuleService;
	@Autowired
	private MallActivityMapper activityMapper;
	@Autowired
	private WordRefundService wordRefundService;
	@Autowired
	private MallActivityPurRecordMapper activityPurRecordMapper;
	@Autowired
	private CustomerInfoMapper customerInfoMapper;
	@Resource
	private IMessageService iMessageService;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 生成订单
	 */
	@Override
	public CommonResult addOrderList(JSONObject json) {
		JSONArray jsonArray = json.getJSONArray("orderDetails");
		if (jsonArray == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		List<OrderDetail> orderDetailList = JSONObject.parseArray(jsonArray.toJSONString(), OrderDetail.class);
		OrderList orderList = json.getObject("orderList", OrderList.class);
		if (orderList == null || orderDetailList == null || orderDetailList.size() <= 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 生成订单号
		orderList.setOrderNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_SP));
		// 预购订单，不处理库存
		if (orderList.getOrderCategory() == 2) {
			for (OrderDetail orderDetail : orderDetailList) {
				if (StringUtils.isBlank(orderDetail.getProductId()) || StringUtils.isBlank(orderDetail.getSkuId())) {
					return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
				}
			}
			orderList.setOrderStatus(Constants.INT_6); //已预约
		}
		// 普通订单，需要处理库存
		if (orderList.getOrderCategory() == 1) {
			CommonResult result = addCommonOrder(orderList, orderDetailList);
			if (result.getApiStatus() != ResultCode.SUCC.getValue()) {
				return result;
			}
		}
		// 服务端对订单金额进行了计算
		OrderList orderListServer = calculateAmount(orderList, orderDetailList);
		// 客户端计算的订单金额与服务端计算的订单金额进行比较
		if(orderList.getOrderAmount().compareTo(orderListServer.getOrderAmount()) != 0
				|| orderList.getOrderPayAmount().compareTo(orderListServer.getOrderPayAmount()) != 0){
			return CommonResult.result(ResultCode.FAIL.getValue(), "订单金额有误");
		}
		//如果订单金额为零，则状态设置为待收货
		if (orderList.getOrderCategory() == 1 && (orderList.getOrderPayAmount().compareTo(BigDecimal.ZERO) == 0 || "0.00".equals(orderList.getOrderPayAmount().toString()))) {
			orderList.setOrderStatus(Constants.INT_2);
		}
		// 生成订单
		orderListMapper.insert(orderList);
		for (OrderDetail orderDetail : orderDetailList) {
			orderDetail.setOrderId(orderList.getOrderId());
		}
		// 批量生成订单详情
		orderDetailMapper.insertBatch(orderDetailList);
		Map<String, String> map = new HashMap<>();
		map.put("orderId", orderList.getOrderId());
		return CommonResult.result(ResultCode.SUCC.getValue(), "下单成功", map);
	}

	/**
	 * 计算订单金额
	 */
	public OrderList calculateAmount(OrderList orderList, List<OrderDetail> orderDetailList) {
		// 订单总额
		BigDecimal orderAmount = new BigDecimal(0);
		// 付款金额
		BigDecimal orderPayAmount = new BigDecimal(0);
		for (OrderDetail orderDetail : orderDetailList) {
			int quantity = orderDetail.getQuantity();//SKU数量
			BigDecimal totalPrice = orderDetail.getPrice().multiply(new BigDecimal(quantity));//SKU总价
			orderAmount = orderAmount.add(totalPrice);
		}
		orderList.setOrderAmount(orderAmount);
		orderPayAmount = orderAmount;
		if (StringUtils.isBlank(orderList.getCouponCode())) {
			orderList.setOrderPayAmount(orderPayAmount);
			return orderList;
		}
		Map<String, String> coupon = couponListMapper.selectCouponInfoByCouponCode(orderList.getCouponCode());
		Integer couponType = Integer.parseInt(coupon.get("couponType") == null ? "0" : coupon.get("couponType"));
		// 服务券或兑换券
		if (couponType == 1 || couponType == 2) {
			for (OrderDetail orderDetail : orderDetailList) {
				if (orderDetail.getSkuId().equals(coupon.get("skuId"))) {
					orderPayAmount = orderPayAmount.subtract(orderDetail.getPrice());
				}
			}
		}
		// 抵扣券
		if (couponType == 3) {
			if (orderPayAmount.compareTo(new BigDecimal(coupon.get("faceValue"))) > 0) {
				orderPayAmount = orderPayAmount.subtract(new BigDecimal(coupon.get("faceValue")));
			}
		}
		// 折扣券
		if (couponType == 4) {
			for (OrderDetail orderDetail : orderDetailList) {
				if (orderDetail.getProductId().equals(coupon.get("productId"))) {
					orderPayAmount = orderPayAmount.subtract(orderDetail.getPrice()
							.subtract(orderDetail.getPrice().multiply(new BigDecimal(coupon.get("discount")))));
				}
			}
		}
		orderList.setOrderPayAmount(orderPayAmount);
		orderList.setDiscount(orderAmount.subtract(orderPayAmount));
		log.info("服务端校验金额，优惠金额：{}，应付金额：{}", orderList.getDiscount(), orderPayAmount);
		CouponList couponList = couponListMapper.selectByCouponCode(orderList.getCouponCode());
		// 将优惠券设置为已使用
		couponList.setCouponStatus(2);
		couponListMapper.updateByPrimaryKey(couponList);
		return orderList;
	}

	/**
	 * 普通订单下单业务处理
	 */
	private CommonResult addCommonOrder(OrderList orderList, List<OrderDetail> orderDetailList) {
		// 活动订单
		if (orderList.getIsActivity() == 1) {
			return addActivityOrder(orderList, orderDetailList);
		}
		// 非活动订单
		if (orderList.getIsActivity() == 2) {
			return addInactivityOrder(orderDetailList);
		}
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
	}

	/**
	 * 活动商品订单业务处理
	 */
	private CommonResult addActivityOrder(OrderList orderList, List<OrderDetail> orderDetailList) {
		// 查询用户等级
		Map<String, String> map = customerInfoMapper.selectCustomerLevel(orderList.getCustomerId());
		Integer customerLevel = null;
		if (map == null) {
			customerLevel = 1;//普通会员
		} else {
			customerLevel = Integer.parseInt(map.get("level"));
		}
		for (OrderDetail orderDetail : orderDetailList) {
			// 商品skuId
			String skuId = orderDetail.getSkuId();
			// 活动商品id
			String goodsId = orderDetail.getGoodsId();
			if (StringUtil.isBlank(skuId) || StringUtil.isBlank(goodsId)) {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
			}
			ProductSku productSku = productSkuMapper.selectByPrimaryKey(skuId);
			if (productSku == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品sku不存在");
			}
			MallActivityGoods goods = activityGoodsMapper.selectActivityGoodsBySkuId(skuId);
			if (goods == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "活动商品不存在");
			}
			MallActivity activity = activityMapper.selectByPrimaryKey(goods.getActivityId());
			if (activity == null || activity.getStatus() > 3) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "活动已结束");
			}
			if (activity.getStatus() < 3) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "活动未开始");
			}
			// 普卡会员限制购买个数
			Integer limit1 = goods.getLimit1() == null ? goods.getQuantity() : goods.getLimit1();
			// 金卡会员限制购买个数
			Integer limit2 = goods.getLimit2() == null ? goods.getQuantity() : goods.getLimit2();
			// 钻卡会员限制购买个数
			Integer limit3 = goods.getLimit3() == null ? goods.getQuantity() : goods.getLimit3();

			// 查询活动商品购买记录
			Integer quantity = activityPurRecordMapper.selectSumQuantityByGoodsId(orderList.getCustomerId(), goodsId);
			if (quantity == null) {
				quantity = 0;
			}
			// 普通会员
			if (customerLevel == 1) {
				if (quantity + orderDetail.getQuantity() > limit1) {
					return CommonResult.result(ResultCode.FAIL.getValue(),
							"本次活动 " + productSku.getSkuName() + " 普卡会员限购" + limit1 + "件");
				}
			}
			// 金卡会员
			else if (customerLevel == 2 || customerLevel == 4) {
				if (quantity + orderDetail.getQuantity() > limit2) {
					return CommonResult.result(ResultCode.FAIL.getValue(),
							"本次活动 " + productSku.getSkuName() + " 金卡会员限购" + limit2 + "件");
				}
			}
			// 钻卡会员
			else if (customerLevel == 3 || customerLevel == 5) {
				if (quantity + orderDetail.getQuantity() > limit3) {
					return CommonResult.result(ResultCode.FAIL.getValue(),
							"本次活动 " + productSku.getSkuName() + " 钻卡会员限购" + limit3 + "件");
				}
			}

			String key = goods.getActivityId() + ":" + goods.getId();
			Boolean exists = jedisClient.exists(key);
			if (exists) {
				jedisClient.watch(key);
				String surplus = jedisClient.get(key);
				Integer surplusQuantity = Integer.parseInt(StringUtils.isBlank(surplus) ? "0" : surplus);
				if (surplusQuantity - orderDetail.getQuantity() >= 0) {
					Jedis jedis = null;
					try {
						jedis = jedisClient.getJedis();
						Transaction tx = jedis.multi(); // 开启事务
						tx.decrBy(key, orderDetail.getQuantity()); // 减库存
						List<Object> list = tx.exec();// 提交事务，如果此时key被改动了，则返回null
						if (list == null) {
							// 失败
							return CommonResult.result(ResultCode.FAIL.getValue(), "目前抢购人数较多，请稍后重试");
						}
					} catch (Exception e) {
						log.error("活动商品订单下单异常", e);
					} finally {
						jedis.close();
					}
				} else {
					// 库存不足
					return CommonResult.result(ResultCode.FAIL.getValue(), productSku.getSkuName() + "商品火爆，库存不足");
				}
			} else {
				if (goods.getQuantity() - goods.getSellQuantity() < orderDetail.getQuantity()) {
					return CommonResult.result(ResultCode.FAIL.getValue(), productSku.getSkuName() + "商品火爆，库存不足");
				} else {
					goods.setSellQuantity(goods.getSellQuantity() + orderDetail.getQuantity());
					activityGoodsMapper.updateSelectiveByPrimaryKey(goods);
				}
			}

			// 生成活动商品购买记录
			MallActivityPurRecord purRecord = new MallActivityPurRecord();
			purRecord.setActivityGoodsId(goodsId);
			purRecord.setCustomerId(orderList.getCustomerId());
			purRecord.setQuantity(orderDetail.getQuantity());
			purRecord.setActivityId(goods.getActivityId());
			purRecord.setOrderNo(orderList.getOrderNo());
			activityPurRecordMapper.insert(purRecord);

			orderDetail.setPrice(goods.getPrice());
		}
		return CommonResult.result(ResultCode.SUCC.getValue());
	}

	/**
	 * 非活动商品订单业务处理
	 */
	private CommonResult addInactivityOrder(List<OrderDetail> orderDetailList) {
		for (OrderDetail orderDetail : orderDetailList) {
			String skuId = orderDetail.getSkuId();
			if (StringUtil.isBlank(skuId)) {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
			}
			ProductSku productSku = productSkuMapper.selectByPrimaryKey(skuId);
			if (productSku == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品sku不存在");
			}
			ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productSku.getProductId());
			if (productInfo == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品不存在");
			}
			// 非服务类商品校验库存
			if (productInfo.getProductType() != 1) {
				if (productSku.getProductQuantity() - productSku.getProductSaleQuantity() < orderDetail.getQuantity()) {
					return CommonResult.result(ResultCode.FAIL.getValue(), productSku.getSkuName() + " 商品太火爆了，库存不足啦");
				}
			}
			productSku.setProductSaleQuantity(productSku.getProductSaleQuantity() + orderDetail.getQuantity());
			productSkuMapper.updateSelectiveByPrimaryKey(productSku);
		}
		return CommonResult.result(ResultCode.SUCC.getValue());
	}

	/**
	 * 取消订单
	 */
	@Override
	public CommonResult cancelOrderList(String orderId) {
		if (StringUtils.isBlank(orderId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		if (orderList == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		// 普通订单处理库存，预购订单不需要处理
		if (orderList.getOrderCategory() == 1) {
			List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
			for (OrderDetail orderDetail : orderDetailList) {
				ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
				ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(orderDetail.getProductId());
				if (sku != null && productInfo != null) {
					sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
					productSkuMapper.updateSelectiveByPrimaryKey(sku);
				}
			}
		}
		// 将订单状态置为已取消
		orderListMapper.updateOrderListStatusByOrderId(orderId, Constants.INT_9);
		//如果使用优惠券，则设置为可使用
		handleCoupon(orderList.getCouponCode());
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单已取消");
	}

	/**
	 * 订单删除
	 */
	@Override
	public CommonResult deleteOrderList(String orderId) {
		if (StringUtils.isBlank(orderId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		if (orderList == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		// 该订单未支付且为非预购订单
		if (orderList.getPayStatus() == 1 && orderList.getOrderCategory() == 1) {
			List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
			for (OrderDetail orderDetail : orderDetailList) {
				ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
				ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(orderDetail.getProductId());
				if (sku != null && productInfo != null) {
					sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
					productSkuMapper.updateSelectiveByPrimaryKey(sku);
				}
			}
		}
		// 将订单状态置为已删除
		orderListMapper.updateOrderListStatusByOrderId(orderId, Constants.INT_0);
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单已删除");
	}

	/**
	 * 根据订单状态查询订单列表
	 */
	@Override
	public CommonResult findOrderListByOrderStatus(String customerId, Integer orderStatus, Integer orderCategory,
			Integer pageIndex, Integer pageSize) {
		List<OrderList> orderLists = orderListMapper.selectByOrderStatus(customerId, orderStatus, orderCategory,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		List<Map<String, Object>> list = new ArrayList<>();
		for (OrderList orderList : orderLists) {
			List<Map<String, String>> orderDetailList = orderDetailMapper
					.selectOrderDetailsByOrderId(orderList.getOrderId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderList", orderList);
			map.put("orderDetailList", orderDetailList);
			// 如果是预购订单，查询订单内商品库存是否足够
			if (orderList.getOrderCategory() == 2) {
				boolean isEnough = true;
				for (Map<String, String> detailMap : orderDetailList) {
					ProductSku sku = productSkuMapper.selectByPrimaryKey(detailMap.get("skuId"));
					if (sku.getSurplusQuantity() < Integer.parseInt(detailMap.get("quantity"))) {
						isEnough = false;
						break;
					}
				}
				map.put("isEnough", isEnough);
			}
			list.add(map);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询订单列表成功", list);
	}

	/**
	 * 根据订单id查询订单详情
	 */
	@Override
	public CommonResult findOrderDetailsByOrderId(String orderId) {
		Map<String, Object> map = new HashMap<>();
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		map.put("orderList", orderList);
		List<Map<String, String>> orderDetailList = orderDetailMapper.selectOrderDetailsByOrderId(orderId);
		map.put("orderDetailList", orderDetailList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单详情查询成功", map);
	}

	/**
	 * 账户支付
	 */
	@Override
	public CommonResult memberPayByAccount(String customerId, String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		if (orderList == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单无效");
		}
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		if (carInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "账户异常");
		}
		if (carInfo.getAccount().compareTo(orderList.getOrderPayAmount()) < 0) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "账户余额不足，请及时充值");
		}
		// 增加账户金额流水
		CarAccountRecord accountRecord = new CarAccountRecord();
		// 消费对应的订单id
		accountRecord.setAccountSource(orderId);
		// 金额来源类型 1充值 2消费
		accountRecord.setAccountSourceType(Constants.INT_2);
		// 金额变化类型 1增加 2减少
		accountRecord.setAccountType(Constants.INT_2);
		// 期初
		accountRecord.setBeginningAmount(carInfo.getAccount());
		// 期末
		accountRecord.setFinishAmount(carInfo.getAccount().subtract(orderList.getOrderPayAmount()));
		// 交易金额
		accountRecord.setDealAmount(orderList.getOrderPayAmount());
		// carId
		accountRecord.setCarId(carInfo.getCarId());
		carAccountRecordMapper.insert(accountRecord);
		// 更新账户余额
		carInfo.setAccount(carInfo.getAccount().subtract(orderList.getOrderPayAmount()));
		carInfoMapper.updateByPrimaryKey(carInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "支付成功");
	}

	/**
	 * 订单支付成功
	 */
	@Override
	public CommonResult payOrderListSucc(OrderList orderList) {
		// 查看是否使用优惠券，将使用过的优惠券状态置为已使用
		if (!StringUtils.isBlank(orderList.getCouponCode())) {
			String[] couponCodes = orderList.getCouponCode().split(",");
			for (int i = 0; i < couponCodes.length; i++) {
				CouponList couponList = couponListMapper.selectByCouponCode(couponCodes[i]);
				// 使用时间
				couponList.setUserTime(new Date());
				// 优惠券状态 1未使用 2已使用 3已过期
				couponList.setCouponStatus(Constants.INT_2);
				// 设置订单号
				couponList.setOrderNo(orderList.getOrderNo());
				couponListMapper.updateByPrimaryKey(couponList);
			}
		}
		// 支付状态 1待支付 2已支付
		orderList.setPayStatus(Constants.INT_2);
		// 订单状态置为 1待付款2待收货3待评价4已完成
		orderList.setOrderStatus(Constants.INT_2);
		orderListMapper.updateSelectiveByPrimaryKey(orderList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单支付成功");
	}

	/**
	 * 客户确认收货
	 */
	@Override
	public CommonResult memberConfirmOrderList(String orderDetailId) {
		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(orderDetailId);
		if (orderDetail == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		orderDetail.setStatus(2);
		orderDetailMapper.updateByPrimaryKey(orderDetail);
		List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderDetail.getOrderId());
		boolean flag = true;
		for (OrderDetail detail : orderDetailList) {
			if (detail.getStatus() == 1) {
				flag = false;
				break;
			}
		}
		if (flag == true) {
			OrderList orderList = orderListMapper.selectByPrimaryKey(orderDetail.getOrderId());
			// 确认收货订单状态置为待评价
			orderList.setOrderStatus(3);
			// 物流状态置为已签收
			orderList.setDeliveryStatus(3);
			orderListMapper.updateSelectiveByPrimaryKey(orderList);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "确认收货成功");
	}

	/**
	 * 完成订单
	 */
	@Override
	public CommonResult completeOrderList(String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		orderList.setOrderStatus(Constants.INT_4);
		orderListMapper.updateByPrimaryKey(orderList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单已完成");
	}

	/**
	 * 定时任务：订单处理
	 */
	@Override
	public void updateOrderListStatus() {
		// 未支付订单超过3天未支付将失效
		List<OrderList> orderList = orderListMapper.selectImmediatelyInvalidOrderList();
		if (orderList != null && orderList.size() > 0) {
			for (OrderList order : orderList) {
				List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(order.getOrderId());
				for (OrderDetail orderDetail : orderDetailList) {
					ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
					ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(orderDetail.getProductId());
					if (sku != null && productInfo != null) {
						sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
						productSkuMapper.updateSelectiveByPrimaryKey(sku);
					}
				}
				//TODO （暂时不处理定时失效的）如果使用优惠券，则设置为可使用
//				handleCoupon(order.getCouponCode());
			}
			orderListMapper.updateOrderListStatus(Constants.INT_8);
		}
		// 已发货订单，超过10天未确认收货系统自动收货
		List<OrderList> list = orderListMapper.selectUnconfirmedReceiptOrder();
		if (orderList != null && orderList.size() > 0) {
			for (OrderList order : list) {
				List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(order.getOrderId());
				if (orderDetailList != null && orderDetailList.size() > 0) {
					for (OrderDetail orderDetail : orderDetailList) {
						orderDetail.setStatus(Constants.INT_2);
						orderDetailMapper.updateByPrimaryKey(orderDetail);
					}
				}
				// 发货状态：已签收
				order.setDeliveryStatus(Constants.INT_3);
				// 订单状态：待评价
				order.setOrderStatus(Constants.INT_3);
				orderListMapper.updateSelectiveByPrimaryKey(order);
			}
		}
	}

	/**
	 * 后台：商品订单查询
	 */
	@Override
	public CommonResult clientFindOrderList(Integer pageIndex, Integer pageSize, String orderNo, Integer orderStatus,
			String startDate, String endDate, Integer orderCategory) {
		PageObject<OrderList> page = new PageObject<>(pageIndex, pageSize);
		Integer total = orderListMapper.selectOrderCount(orderNo, orderStatus, startDate, endDate, orderCategory);
		page.setRows(total);
		List<OrderList> orderList = orderListMapper.selectOrderList(page.getStart(), page.getPageSize(), orderNo,
				orderStatus, startDate, endDate, orderCategory);
		page.setDataList(orderList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询订单列表成功", page);
	}

	/**
	 * 后台：商品发货
	 */
	@Override
	public CommonResult clientDeliveryGoodsByOrderId(String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		if (orderList == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单信息不存在");
		}
		// 将物流状态置为已发货
		orderList.setDeliveryStatus(2);
		orderList.setDeliveryTime(new Date());
		orderListMapper.updateSelectiveByPrimaryKey(orderList);

		// 消息推送
		String title = String.format("商品发货通知");
		String content = String.format("【%s】订单商品已发货，请您注意查收 。", orderList.getOrderNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_PRODUCT, title, content, orderList.getOrderId()),
				orderList.getCustomerId());

		return CommonResult.result(ResultCode.SUCC.getValue(), "发货成功");
	}

	/**
	 * 后台：预购订单到货通知
	 */
	@Override
	public CommonResult clientNoticPreOrder(String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		// 预购订单已通知状态
		orderList.setOrderStatus(-1);
		orderListMapper.updateSelectiveByPrimaryKey(orderList);

		// 消息推送
		String title = String.format("商品到货通知");
		String content = String.format("【%s】预购订单商品已到货，请您及时下单购买 。", orderList.getOrderNo());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_PRODUCT, title, content, orderList.getOrderId()),
				orderList.getCustomerId());

		return CommonResult.result(ResultCode.SUCC.getValue(), "通知成功");
	}

	/**
	 * 将预购订单修改为正常订单
	 */
	@Override
	public CommonResult productUpdatePreOrderToOrder(String orderId) {
		OrderList order = orderListMapper.selectByPrimaryKey(orderId);
		if (order == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		// 待付款
		order.setOrderStatus(1);
		// 正常订单
		order.setOrderCategory(1);
		// 下单时间
		order.setCreateDate(new Date());
		List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
		for (OrderDetail orderDetail : orderDetailList) {
			ProductSku productSku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
			if (productSku.getSurplusQuantity() < orderDetail.getQuantity()) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "您来晚了，卖光了");
			} else {
				productSku.setProductSaleQuantity(productSku.getProductSaleQuantity() + orderDetail.getQuantity());
				productSkuMapper.updateSelectiveByPrimaryKey(productSku);
			}
		}
		orderListMapper.updateSelectiveByPrimaryKey(order);
		return CommonResult.result(ResultCode.SUCC.getValue(), "下单成功");
	}

	/**
	 * pc:根据订单状态查询订单列表
	 */
	@Override
	public CommonResult websiteFindOrderListByOrderStatus(String customerId, Integer orderStatus, Integer orderCategory,
			Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);
		int total = orderListMapper.selectCountByOrderStatus(customerId, orderStatus, orderCategory);
		page.setRows(total);
		List<OrderList> orderLists = orderListMapper.selectByOrderStatus(customerId, orderStatus, orderCategory,
				page.getStart(), page.getPageSize());
		List<Map<String, Object>> list = new ArrayList<>();
		for (OrderList orderList : orderLists) {
			List<Map<String, String>> orderDetailList = orderDetailMapper
					.selectOrderDetailsByOrderId(orderList.getOrderId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderList", orderList);
			map.put("orderDetailList", orderDetailList);
			// 如果是预购订单，查询订单内商品库存是否足够
			if (orderList.getOrderCategory() == 2) {
				boolean isEnough = true;
				for (Map<String, String> detailMap : orderDetailList) {
					ProductSku sku = productSkuMapper.selectByPrimaryKey(detailMap.get("skuId"));
					if (sku.getSurplusQuantity() < Integer.parseInt(detailMap.get("quantity"))) {
						isEnough = false;
						break;
					}
				}
				map.put("isEnough", isEnough);
			}
			list.add(map);
		}
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询订单列表成功", page);
	}

	@Override
	public CommonResult orderApplyRefund(String orderId) {
		OrderList order = orderListMapper.selectByPrimaryKey(orderId);
		if (order == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		if (order.getOrderStatus() > Constants.INT_2) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "订单已收货，不能申请退款");
		}
		if (order.getDeliveryStatus() > Constants.INT_1) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "订单已发货，不能申请退款");
		}
		order.setOrderStatus(11); // 修改状态为退款中
		order.setRefundApplyTime(new Date());
		orderListMapper.updateByPrimaryKey(order);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public CommonResult orderCancelRefund(String orderId) {
		OrderList order = orderListMapper.selectByPrimaryKey(orderId);
		if (order == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		if (order.getOrderStatus() != Constants.INT_11) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "订单状态不在退款中或已处理，不能取消退款");
		}
		order.setOrderStatus(2); // 修改状态为待收货
		order.setRefundApplyTime(null);
		orderListMapper.updateByPrimaryKey(order);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public CommonResult orderConfirmRefund(String orderId) {
		OrderList order = orderListMapper.selectByPrimaryKey(orderId);
		if (order == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		if (order.getOrderStatus() != Constants.INT_11) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "订单状态不在退款中，不能确认退款");
		}
		// 进行退款操作
		CommonResult refundResult = wordRefundService.refundOrder(order);
		if (refundResult.getApiStatus() == ResultCode.SUCC.getValue()) {
			order.setOrderStatus(10); // 修改状态为已退款
			orderListMapper.updateByPrimaryKey(order);
			//如果使用优惠券，则设置为可使用 TODO（确认退款之后优惠券是否可使用）
			handleCoupon(order.getCouponCode());
			return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
		} else {
			return refundResult;
		}
	}

	@Override
	public void timedOrderConfirmRefund() {
		log.info("定时任务：处理商品订单的退款（申请退款24小时候之后自动原路退款）");
		List<OrderList> list = orderListMapper.selectRefund();
		CommonResult refundResult = null;
		for (OrderList order : list) {
			refundResult = wordRefundService.refundOrder(order);
			if (refundResult.getApiStatus() == ResultCode.SUCC.getValue()) {
				order.setOrderStatus(10); // 修改状态为已退款
				orderListMapper.updateByPrimaryKey(order);
				//如果使用优惠券，则设置为可使用 TODO（确认退款之后优惠券是否可使用）
				handleCoupon(order.getCouponCode());
				log.info("定时任务：处理商品订单的退款成功：{}, {}", order.getOrderNo(), refundResult.getMsg());
			} else {
				log.info("定时任务：处理商品订单的退款失败：{}, {}", order.getOrderNo(), refundResult.getMsg());
			}
		}
		log.info("定时任务：处理商品订单的退款，处理数量：{}", list.size());
	}
public static void main(String[] args) {
	System.out.println(JSON.parseObject(""));
}
	@Override
	public void exportProductOrder(HttpServletRequest request, HttpServletResponse response) {
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("商品订单数据");
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
		cell.setCellValue("商品订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("收货人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("联系电话");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("订单总额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("支付总额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("支付方式");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("订单状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("商品名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("商品SKU名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("商品SKU数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("商品SKU价格");
		cell.setCellStyle(style);

		String orderNo = request.getParameter("orderNo");
		Integer orderStatus = request.getParameter("orderStatus") == "" || request.getParameter("orderStatus") == null
				? null : Integer.parseInt(request.getParameter("orderStatus"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer orderCategory = request.getParameter("orderCategory") == ""
				|| request.getParameter("orderCategory") == null ? null
						: Integer.parseInt(request.getParameter("orderCategory"));

		List<Map<String, Object>> productOrderList = orderListMapper.exportProductOrder(orderNo, orderStatus, startDate,
				endDate, orderCategory);

		if (!productOrderList.isEmpty()) {
			for (short i = 0; i < productOrderList.size(); i++) {
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(productOrderList.get(i).get("orderNo") == null ? ""
						: productOrderList.get(i).get("orderNo").toString());
				
				String deliveryAddress = "";
				String name = "";
				String mobile = "";
				if (productOrderList.get(i).get("deliveryAddress") != null) {
					deliveryAddress = productOrderList.get(i).get("deliveryAddress").toString();
					if (StringUtils.isNotBlank(deliveryAddress)) {
						name = JSON.parseObject(deliveryAddress).getString("name");
						mobile = JSON.parseObject(deliveryAddress).getString("mobile");
					}
				}
				row.createCell(1).setCellValue(name == null ? "" : name);
				row.createCell(2).setCellValue(mobile == null ? "" : mobile);
				
				row.createCell(3).setCellValue(productOrderList.get(i).get("Amount") == null ? ""
						: productOrderList.get(i).get("Amount").toString());
				row.createCell(4).setCellValue(productOrderList.get(i).get("payAmount") == null ? ""
						: productOrderList.get(i).get("payAmount").toString());
				row.createCell(5).setCellValue(productOrderList.get(i).get("payType") == null ? ""
						: OrderList.getPayType((Integer) productOrderList.get(i).get("payType")));
				row.createCell(6).setCellValue(productOrderList.get(i).get("orderStatus") == null ? ""
						: OrderList.getOrderStatus((Integer) productOrderList.get(i).get("orderStatus")));
				row.createCell(7).setCellValue(productOrderList.get(i).get("productName") == null ? ""
						: productOrderList.get(i).get("productName").toString());
				row.createCell(8).setCellValue(productOrderList.get(i).get("skuName") == null ? ""
						: productOrderList.get(i).get("skuName").toString());
				row.createCell(9).setCellValue(productOrderList.get(i).get("quantity") == null ? ""
						: productOrderList.get(i).get("quantity").toString());
				row.createCell(10).setCellValue(productOrderList.get(i).get("price") == null ? ""
						: productOrderList.get(i).get("price").toString());
			}

			// 处理相同的数据合并单元格
			MergeCellsUtil.addProductOrderMergedRegion(sheet, 0, 1, sheet.getLastRowNum(), wb);
		}

		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName = new String(("商品订单信息表" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),
					"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			wb.close();
			log.info("导出商品订单信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出商品订单信息失败", e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("导出商品订单信息失败", e);
		}
	}
	
	/**
	 * 订单取消、确认退款之后处理订单的优惠券
	 * @param couponCode
	 */
	private void handleCoupon(String couponCode) {
		//如果使用优惠券，则设置为可使用
		if (StringUtil.isNotBlank(couponCode)) {
			for (String code : couponCode.split(",")) {
				CouponList coupon = couponListMapper.selectByCouponCode(code);
				if (coupon != null) {
					if (new Date().getTime() > coupon.getValidTimeEnd().getTime()) {
						//已过期
						coupon.setCouponStatus(3);
					} else {
						//未使用
						coupon.setCouponStatus(1);
					}
					couponListMapper.updateByPrimaryKey(coupon);
				}
			}
		}
	}
	
}
