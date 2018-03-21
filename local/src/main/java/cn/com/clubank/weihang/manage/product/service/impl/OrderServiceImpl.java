package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;

/**
 * 订单管理
 * 
 * @author LeiQY
 *
 */
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
	private IMessageService imessageService;

	/**
	 * 生成订单
	 */
	@Override
	public CommonResult addOrderList(JSONObject json) {
		OrderList orderList = json.getObject("orderList", OrderList.class);
		if (orderList == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		orderList.setOrderNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_SP));
		orderListMapper.insert(orderList);
		JSONArray jsonArray = json.getJSONArray("orderDetails");
		List<OrderDetail> orderDetailList = JSONObject.parseArray(jsonArray.toJSONString(), OrderDetail.class);
		// 预购订单，不处理库存
		if (orderList.getOrderCategory() == 2) {
			for (OrderDetail orderDetail : orderDetailList) {
				orderDetail.setOrderId(orderList.getOrderId());
			}
		}
		// 正常订单，需要处理库存
		if (orderList.getOrderCategory() == 1) {
			for (OrderDetail orderDetail : orderDetailList) {
				orderDetail.setOrderId(orderList.getOrderId());
				String skuId = orderDetail.getSkuId();
				if (StringUtil.isBlank(skuId)) {
					orderListMapper.deleteByPrimaryKey(orderList.getOrderId());
					return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
				}
				ProductSku productSku = productSkuMapper.selectByPrimaryKey(skuId);
				if (orderList.getIsActivity() == 1) {
					MallActivityGoods goods = activityGoodsMapper.selectBySkuId(skuId);
					if (goods != null) {
						MallActivity activity = activityMapper.selectByPrimaryKey(goods.getActivityId());
						if (activity != null && activity.getStatus() >= 4) {
							orderListMapper.deleteByPrimaryKey(orderList.getOrderId());
							return CommonResult.result(ResultCode.FAIL.getValue(), "活动已结束，您可以看看其他活动");
						} else {
							if (goods.getQuantity() - goods.getSellQuantity() >= orderDetail.getQuantity()) {
								goods.setSellQuantity(goods.getSellQuantity() + orderDetail.getQuantity());
								activityGoodsMapper.updateSelectiveByPrimaryKey(goods);
								orderDetail.setPrice(goods.getPrice());
							} else {
								orderListMapper.deleteByPrimaryKey(orderList.getOrderId());
								return CommonResult.result(ResultCode.FAIL.getValue(),
										productSku.getSkuName() + " 本次活动该商品已被抢光，您可以看看其他商品");
							}
						}
					}
				} else {
					if (productSku.getProductQuantity() - productSku.getProductSaleQuantity() < orderDetail
							.getQuantity()) {
						orderListMapper.deleteByPrimaryKey(orderList.getOrderId());
						return CommonResult.result(ResultCode.FAIL.getValue(),
								productSku.getSkuName() + " 商品太火爆了，库存不足啦");
					}
					productSku.setProductSaleQuantity(productSku.getProductSaleQuantity() + orderDetail.getQuantity());
					productSkuMapper.updateSelectiveByPrimaryKey(productSku);
				}
			}
		}
		orderDetailMapper.insertBatch(orderDetailList);
		Map<String, String> map = new HashMap<>();
		map.put("orderId", orderList.getOrderId());
		return CommonResult.result(ResultCode.SUCC.getValue(), "下单成功", map);
	}

	/**
	 * 取消订单
	 */
	@Override
	public CommonResult cancelOrderList(String orderId) {
		List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
		for (OrderDetail orderDetail : orderDetailList) {
			ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
			sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
			productSkuMapper.updateSelectiveByPrimaryKey(sku);
		}
		// 将订单状态置为已取消
		orderListMapper.updateOrderListStatusByOrderId(orderId, Constants.INT_9);
		return CommonResult.result(ResultCode.SUCC.getValue(), "订单已取消");
	}

	/**
	 * 订单删除
	 */
	@Override
	public CommonResult deleteOrderList(String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		if (orderList != null && orderList.getPayStatus() == Constants.INT_1) {
			// 该订单未支付
			List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(orderId);
			for (OrderDetail orderDetail : orderDetailList) {
				ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
				sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
				productSkuMapper.updateSelectiveByPrimaryKey(sku);
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
			if (orderList.getOrderCategory() == 2 && orderList.getOrderStatus() == 6) {
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
	public CommonResult memberConfirmOrderList(String orderId) {
		OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
		// 确认收货订单状态置为待评价
		orderList.setOrderStatus(3);
		// 物流状态置为已签收
		orderList.setDeliveryStatus(3);
		orderListMapper.updateByPrimaryKey(orderList);
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
	 * 定时任务，未支付订单超时未支付将失效
	 */
	@Override
	public void updateOrderListStatus() {
		List<OrderList> orderList = orderListMapper.selectImmediatelyInvalidOrderList();
		if (orderList != null && orderList.size() > 0) {
			for (OrderList order : orderList) {
				List<OrderDetail> orderDetailList = orderDetailMapper.selectListByOrderId(order.getOrderId());
				for (OrderDetail orderDetail : orderDetailList) {
					ProductSku sku = productSkuMapper.selectByPrimaryKey(orderDetail.getSkuId());
					if (sku != null) {
						sku.setProductSaleQuantity(sku.getProductSaleQuantity() - orderDetail.getQuantity());
						productSkuMapper.updateSelectiveByPrimaryKey(sku);
					}
				}
			}
			orderListMapper.updateOrderListStatus(Constants.INT_8);
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
		// 将物流状态置为已发货
		orderList.setDeliveryStatus(2);
		orderList.setDeliveryTime(new Date());
		orderListMapper.updateSelectiveByPrimaryKey(orderList);
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
		MsgList msg = new MsgList();
		msg.setTitle("商品到货通知");
		msg.setMsgType(MsgList.MSGTYPE_PRODUCT);
		msg.setContent("【" + orderList.getOrderNo() + "】预购订单商品已到货，请您及时下单购买");
		msg.setObjId(orderList.getOrderNo());
		imessageService.pushMessage(msg, orderList.getCustomerId());
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
}
