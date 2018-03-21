package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.product.service.OrderService;

/**
 * 订单管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 生成订单
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/productAddOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addOrderList(@RequestBody JSONObject json) {
		return orderService.addOrderList(json);
	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/productCancelOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult cancelOrderList(@RequestBody JSONObject json) {
		return orderService.cancelOrderList(json.getString("orderId"));
	}

	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/productDeleteOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteOrderList(@RequestBody JSONObject json) {
		return orderService.deleteOrderList(json.getString("orderId"));
	}

	/**
	 * 根据订单状态查询订单列表
	 * 
	 * @param customerId
	 * @param orderStatus
	 * @param orderCategory
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/productFindOrderListByOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findOrderListByOrderStatus(@RequestBody JSONObject json) {
		return orderService.findOrderListByOrderStatus(json.getString("customerId"), json.getInteger("orderStatus"),
				json.getInteger("orderCategory"), json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}

	/**
	 * 根据订单id查询订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/productFindOrderDetailsByOrderId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findOrderDetailsByOrderId(@RequestBody JSONObject json) {
		return orderService.findOrderDetailsByOrderId(json.getString("orderId"));
	}

	/**
	 * 账户支付
	 * 
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/memberPayByAccount", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberPayByAccount(@RequestBody JSONObject json) {
		return orderService.memberPayByAccount(json.getString("customerId"), json.getString("orderId"));
	}

	/**
	 * 订单支付成功
	 * 
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "/productPayOrderListSucc", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult payOrderListSucc(@RequestBody OrderList orderList) {
		return orderService.payOrderListSucc(orderList);
	}

	/**
	 * 客户确认收货
	 * 
	 * @param orderDetailId
	 * @return
	 */
	@RequestMapping(value = "/memberConfirmOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberConfirmOrderList(@RequestBody JSONObject json) {
		return orderService.memberConfirmOrderList(json.getString("orderDetailId"));
	}

	/**
	 * 完成订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/productCompleteOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult completeOrderList(@RequestBody JSONObject json) {
		return orderService.completeOrderList(json.getString("orderId"));
	}

	/**
	 * 将预购订单修改为正常订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productUpdatePreOrderToOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult productUpdatePreOrderToOrder(@RequestBody JSONObject json) {
		return orderService.productUpdatePreOrderToOrder(json.getString("orderId"));
	}

	/**
	 * 申请退款
	 * 
	 * @return
	 */
	@RequestMapping(value = "/orderApplyRefund", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult orderApplyRefund(@RequestBody JSONObject json) {
		return orderService.orderApplyRefund(json.getString("orderId"));
	}

	/**
	 * 取消退款
	 * 
	 * @return
	 */
	@RequestMapping(value = "/orderCancelRefund", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult orderCancelRefund(@RequestBody JSONObject json) {
		return orderService.orderCancelRefund(json.getString("orderId"));
	}
}
