package cn.com.clubank.weihang.manage.pay;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 支付处理
 * @author YangWei
 *
 */
public interface WeihPayService {
	
	/**
	 * 支付宝-app支付签名
	 * @param objId		订单id（维修单、洗车单、商品订单）
	 * @param total		支付总金额
	 * @param type		类型：1维修单、2洗车单、3商品、4会员充值、5洗车打赏、6集团组充值
	 * @param customerId
	 * @return
	 */
	CommonResult alipaySignature(String objId, String total, String customerId, Integer type);
	
	/**
	 * 支付宝支付回调处理
	 * @param params
	 * @return
	 */
	void alipayNotify(HttpServletRequest request);
	
	/**
	 * 微信支付回调处理
	 * @param params
	 * @return
	 */
	void weixinNotify(HttpServletRequest request);
	
	/**
	 * 处理维修单支付
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleWorkRepair(String repairNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 处理洗车单支付
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleWorkWash(String washNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 处理洗车单打赏
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleWorkWashTip(String washNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 处理会员充值
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleCustomerRecharge(String carId, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 处理集团组充值
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleGroupRecharge(String groupId, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 处理商品订单
	 * 
	 * @param carId
	 * @param tradeNo 第三方流水号
	 * @param total
	 * @param tradeStatus
	 */
	CommonResult handleOrderList(String oederNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay);
	
	/**
	 * 会员帐号支付
	 * @param objNo			单号（维修单、洗车单、商品订单）
	 * @param total			金额
	 * @param customerId	会员id
	 * @param type			类型：1维修单、2洗车单、3商品、5洗车打赏
	 * @param isGroup		是否集团付费
	 * @return
	 */
	CommonResult memberAccountPay(String objNo, BigDecimal total, String customerId, Integer type, Integer isGroup);
	
}
