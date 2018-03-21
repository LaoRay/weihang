package cn.com.clubank.weihang.manage.pay.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;

/**
 * 退款业务处理
 * @author YangWei
 *
 */
public interface WordRefundService {

	/**
	 * 订单退款处理
	 * @return
	 */
	CommonResult refundOrder(OrderList order);
	
	/**
	 * 订单详情退款处理
	 * @return
	 */
	CommonResult refundOrderDetail(String orderDetailId);
	
}
