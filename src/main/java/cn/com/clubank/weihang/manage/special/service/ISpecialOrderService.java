package cn.com.clubank.weihang.manage.special.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;

public interface ISpecialOrderService {

	/**
	 * 保存特殊订单
	 * @param json
	 * @return
	 */
	CommonResult insertSpecialOrderInfo(JSONObject json);
	
	/**
	 * 查询特殊订单列表
	 * @param customerId 客户ID
	 * @param status 订单状态
	 * @param pageIndex 页码下标
	 * @param pageSize 页容量
	 * @return
	 */
	CommonResult selectSpecialOrderList(String customerId, Integer status, Integer pageIndex, Integer pageSize);
	
	/**
	 * 取消特殊订单
	 * @param specialId 特殊订单id
	 * @return
	 */
	CommonResult cancelSpecialOrder(String specialId);
	
	/**
	 * 特殊订单确认收货
	 * @param specialId 特殊订单id
	 * @return
	 */
	CommonResult confirmReceiptSpecialOrder(String specialId);
	
	/**
	 * 查询特殊订单详情
	 * @param specialId 特殊订单id
	 * @return
	 */
	CommonResult selectSpecialOrderDetail(String specialId);
	
	/**
	 * 后台：查询特殊订单列表
	 * @param orderTimeStart 下单时间1
	 * @param orderTimeEnd 下单时间2
	 * @param status 订单状态
	 * @param customerName 客户名称
	 * @param pageIndex 页码下标
	 * @param pageSize 页容量
	 * @return
	 */
	CommonResult clientSelectSpecialOrderList(String orderTimeStart, String orderTimeEnd, Integer status, String customerName, Integer pageIndex, Integer pageSize);
	
	/**
	 * 后台：确认特殊订单金额
	 * @param specialId 特殊订单id
	 * @param orderAmount 订单金额
	 * @param estimateTotime 预计到货时间
	 * @return
	 */
	CommonResult cilentEditSpecialOrder(String specialId, BigDecimal orderAmount, Date estimateTotime);
	
	/**
	 * 后台：特殊订单发货
	 * @param specialId 特殊订单id
	 * @return
	 */
	CommonResult clientDeliverySpecialOrder(String specialId);
	
	/**
	 * 后台：特殊订单驳回
	 * @param specialId 特殊订单id
	 * @param turnDownDesc 驳回原因
	 * @return
	 */
	CommonResult clientRejectSpecialOrder(String specialId, String turnDownDesc);
	
	/**
	 * 定时任务：处理特殊订单状态(待付款状态三天内未支付，订单状态修改为已取消，已发货保单十天内未确认，订单状态变更为已完成)
	 */
	void handleSpecialOrderStatus();
	
	/**
	 * 后台：导出特殊订单列表
	 * @param request
	 * @param response
	 */
	void exportSpecialOrder(HttpServletRequest request,HttpServletResponse response);
}
