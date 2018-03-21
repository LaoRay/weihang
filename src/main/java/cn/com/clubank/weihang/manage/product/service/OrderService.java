package cn.com.clubank.weihang.manage.product.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;

/**
 * 订单管理
 * 
 * @author LeiQY
 *
 */
public interface OrderService {

	/**
	 * 生成订单
	 * 
	 * @param json
	 * @return
	 */
	CommonResult addOrderList(JSONObject json);

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult cancelOrderList(String orderId);

	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult deleteOrderList(String orderId);

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
	CommonResult findOrderListByOrderStatus(String customerId, Integer orderStatus, Integer orderCategory,
			Integer pageIndex, Integer pageSize);

	/**
	 * 根据订单id查询订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult findOrderDetailsByOrderId(String orderId);

	/**
	 * 账户支付
	 * 
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	CommonResult memberPayByAccount(String customerId, String orderId);

	/**
	 * 订单支付成功
	 * 
	 * @param orderList
	 * @return
	 */
	CommonResult payOrderListSucc(OrderList orderList);

	/**
	 * 客户确认收货
	 * 
	 * @param orderDetailId
	 * @return
	 */
	CommonResult memberConfirmOrderList(String orderDetailId);

	/**
	 * 完成订单
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult completeOrderList(String orderId);

	/**
	 * 定时任务：订单处理，三天内未支付订单置为已失效，已发货订单十天内未确认收货系统自动确认收货
	 */
	void updateOrderListStatus();

	/**
	 * 后台：商品订单查询
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param orderNo
	 * @param orderStatus
	 * @param startDate
	 * @param endDate
	 * @param orderCategory
	 * @return
	 */
	CommonResult clientFindOrderList(Integer pageIndex, Integer pageSize, String orderNo, Integer orderStatus,
			String startDate, String endDate, Integer orderCategory);

	/**
	 * 后台：发货
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult clientDeliveryGoodsByOrderId(String orderId);

	/**
	 * 后台：预购订单到货通知
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult clientNoticPreOrder(String orderId);

	/**
	 * 将预购订单修改为正常订单
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult productUpdatePreOrderToOrder(String orderId);

	/**
	 * pc：根据订单状态查询订单列表
	 * 
	 * @param customerId
	 * @param orderStatus
	 * @param orderCategory
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult websiteFindOrderListByOrderStatus(String customerId, Integer orderStatus, Integer orderCategory,
			Integer pageIndex, Integer pageSize);
	
	/**
	 * 申请退款
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult orderApplyRefund(String orderId);
	
	/**
	 * 取消退款
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult orderCancelRefund(String orderId);
	
	/**
	 * 客户端确认退款
	 * 
	 * @param orderId
	 * @return
	 */
	CommonResult orderConfirmRefund(String orderId);
	
	/**
	 * 定时任务：处理商品订单的退款（申请退款24小时候之后自动原路退款）
	 */
	void timedOrderConfirmRefund();
	
	/**
	 * 导出商品订单列表
	 * @param request
	 * @param response
	 */
	void exportProductOrder(HttpServletRequest request, HttpServletResponse response);
}
