package cn.com.clubank.weihang.pay;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import cn.com.clubank.weihang.TestBase;
import cn.com.clubank.weihang.common.weihpay.WeihAlipayUtil;
import cn.com.clubank.weihang.common.weihpay.WeihWeixinPayUtil;

/**
 * 支付的单元测试
 * 
 * @author YangWei
 *
 */
public class PayTest extends TestBase {

	@Resource
	private WeihAlipayUtil weihAlipayUtil;
	
	@Resource
	private WeihWeixinPayUtil weihWeixinPayUtil;
	
	/**
	 * 阿里退款
	 */
	@Test
	public void aliRefund() {
		String outTradeNo = "2017111621001104600205318690", totalAmount = "0.01", passbackParams = "测试退款";
		System.out.println(weihAlipayUtil.refund(outTradeNo, totalAmount, passbackParams, UUID.randomUUID().toString()));
		
		System.out.println(weihAlipayUtil.query(outTradeNo));
	}
	
	/**
	 * 微信签名
	 */
	@Test
	public void weixinSign() {
		String out_trade_no = "SP17322133757032", total = "0.01";
		System.out.println(weihWeixinPayUtil.sign(out_trade_no, total, "APP", "支付签名测试", "", "117.36.74.42"));
	}
	
	/**
	 * 微信退款
	 */
	@Test
	public void weixinRefund() {
		System.out.println(weihWeixinPayUtil.refund("4200000046201711205904412408", "0.01", "0.01"));
	}
	
}
