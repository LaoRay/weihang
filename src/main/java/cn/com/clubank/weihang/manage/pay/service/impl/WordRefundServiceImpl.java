package cn.com.clubank.weihang.manage.pay.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.weihpay.WeihAlipayUtil;
import cn.com.clubank.weihang.common.weihpay.WeihWeixinPayUtil;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.pay.dao.WorkRefundInfoMapper;
import cn.com.clubank.weihang.manage.pay.pojo.WorkRefundInfo;
import cn.com.clubank.weihang.manage.pay.service.WordRefundService;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.repair.dao.WorkPayInfoMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPayInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 退款处理
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class WordRefundServiceImpl implements WordRefundService {

	@Resource
	private WeihAlipayUtil weihAlipayUtil;
	
	@Resource
	private WeihWeixinPayUtil weihWeixinPayUtil;
	
	@Resource
	private WorkPayInfoMapper workPayInfoMapper;
	
	@Resource
	private WorkRefundInfoMapper workRefundInfoMapper;
	
	@Resource
	private CarInfoMapper carInfoMapper;
	
	@Resource
	private CarAccountRecordMapper carAccountRecordMapper;
	
	@Resource
	private OrderDetailMapper orderDetailMapper;
	
	@Resource
	private IMessageService iMessageService;
	
	@Autowired
	private OrderListMapper orderListMapper;
	
	@Override
	public CommonResult refundOrder(OrderList order) {
		log.info("订单退款.{}", order.getOrderNo());
		if (order.getOrderPayAmount().compareTo(BigDecimal.ZERO) == 0 || "0.00".equals(order.getOrderPayAmount().toString())) {
			log.info("订单退款金额为0.{}", order.getOrderNo());
			return new CommonResult(ResultCode.SUCC.getValue(), "退款成功，订单退款金额为0");
		}
		// 查询订单支付流水表
		List<WorkPayInfo> list = workPayInfoMapper.selectByOrderId(order.getOrderId());
		if (list.size() == 1) {
			// 判断订单类型（目前只处理商品订单）
			if (list.get(0).getPayType() == WorkPayInfo.PAY_TYPE_ORDER) {
				BigDecimal total = list.get(0).getPayTotal();
				if ("0.00".equals(total.toString())) {
					return new CommonResult(ResultCode.FAIL.getValue(), "【"+order.getOrderNo()+"】退款金额为【0.00】，不能进行退款");
				}
				return refundOrder(order, list.get(0).getWpiId(), list.get(0).getPayWay(), list.get(0).getTransactionId(), total, total);
			}
		} else {
			return new CommonResult(ResultCode.FAIL.getValue(), "订单未找到支付记录或查询到多个支付记录，不能正常退款");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "操作成功");
	}
	
	@Override
	public CommonResult refundOrderDetail(String orderDetailId) {
		OrderDetail detail = orderDetailMapper.selectByPrimaryKey(orderDetailId);
		if (detail == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "没有找到订单详情数据");
		}
		OrderList order = orderListMapper.selectByPrimaryKey(detail.getOrderId());
		if (order == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单不存在");
		}
		if (detail.getPrice().compareTo(BigDecimal.ZERO) == 0 || "0.00".equals(detail.getPrice().toString()) || order.getOrderPayAmount().compareTo(BigDecimal.ZERO) == 0 || "0.00".equals(order.getOrderPayAmount().toString())) {
			log.info("订单详情退款金额为0。orderNo:{}，skuId:{}", order.getOrderNo(), detail.getSkuId());
			return new CommonResult(ResultCode.SUCC.getValue(), "退款成功，订单详情退款金额为0");
		}
		// 查询订单支付流水表
		List<WorkPayInfo> list = workPayInfoMapper.selectByOrderId(detail.getOrderId());
		if (list.size() == 1) {
			// 判断订单类型（目前只处理商品订单）
			if (list.get(0).getPayType() == WorkPayInfo.PAY_TYPE_ORDER) {
				//总金额及退款金额
				BigDecimal total = list.get(0).getPayTotal();
				BigDecimal refundTotal = detail.getPrice();
				if ("0.00".equals(total.toString()) || "0.00".equals(refundTotal.toString())) {
					return new CommonResult(ResultCode.FAIL.getValue(), "【"+order.getOrderNo()+"】退款金额为【0.00】，不能进行退款");
				}
				return refundOrder(order, list.get(0).getWpiId(), list.get(0).getPayWay(), list.get(0).getTransactionId(), total, refundTotal);
			}
		} else {
			return new CommonResult(ResultCode.FAIL.getValue(), "订单未找到支付记录或查询到多个支付记录，不能正常退款");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "操作成功");
	}

	/**
	 * 处理商品订单的退款
	 * @param orderId		订单id
	 * @param wpiId			订单支付流水id
	 * @param payWay		支付方式
	 * @param transactionId	第三方流水号
	 * @param total			总金额
	 * @param refundTotal	退款金额
	 * @return
	 */
	private CommonResult refundOrder(OrderList order, String wpiId, Integer payWay, String transactionId, BigDecimal total, BigDecimal refundTotal) {
		// 判断支付类型，按照类型处理
		WorkRefundInfo refund = new WorkRefundInfo(wpiId, refundTotal, payWay, order.getOrderNo(), transactionId);
		String passbackParams = "陕西威航商品订单【" + order.getOrderNo() + "】退款";
		boolean refudnResult = false;
		if (payWay == WorkPayInfo.PAY_WAY_CUSTOMER) {
			//会员帐号
			CarInfo carInfo = carInfoMapper.selectByCarNo(transactionId); //会员支付时第三方流水号保存车牌号码
			if (carInfo == null) {
				return new CommonResult(ResultCode.FAIL.getValue(), "未找到付款车辆账户信息");
			}
			// 保存车辆账户流水表
			CarAccountRecord record = new CarAccountRecord();
			record.setBeginningAmount(carInfo.getAccount()); // 期初金额
			record.setDealAmount(refundTotal); // 产生金额
			record.setFinishAmount(record.getBeginningAmount().add(refundTotal)); // 期末金额
			record.setAccountSourceType(3); // 来源类型：3-退款
			record.setCarId(carInfo.getCarId());
			record.setTransactionId(carInfo.getCarNo()); // 第三方交易号（车牌号）
			record.setAccountType(1);
			record.setAccountSource(carInfo.getCarNo());
			record.setAccountPayType(CarAccountRecord.PAY_WAY_REFUND); //支付方式-退款
			record.setDescription(passbackParams);
			carAccountRecordMapper.insert(record);
			
			// 更新车辆信息表的账户
			carInfo.setAccount(record.getFinishAmount());
			carInfoMapper.updateByPrimaryKey(carInfo);
			
			refudnResult = true;
		} else if (payWay == WorkPayInfo.PAY_WAY_ALIPAY) {
			//支付宝
			refudnResult = weihAlipayUtil.refund(transactionId, refundTotal.toString(), passbackParams, UUID.randomUUID().toString());
		} else if (payWay == WorkPayInfo.PAY_WAY_WEIXIN) {
			//微信
			refudnResult = weihWeixinPayUtil.refund(transactionId, total.toString(), refundTotal.toString());
		}
		refund.setRefundResult(refudnResult ? 1: 0);
		refund.setDescription(passbackParams);
		workRefundInfoMapper.insert(refund);
		if (!refudnResult) {
			return new CommonResult(ResultCode.FAIL.getValue(), "退款失败");
		}
		// 退款成功推送退款消息
		String title = String.format("商品订单退款通知");
		String content = String.format("您的商品订单【%s】已退款成功，退款金额：【%s】 。", order.getOrderNo(),
				refundTotal.toString());
		iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_STATION, title, content, order.getOrderId()),
				order.getCustomerId());
		return new CommonResult(ResultCode.SUCC.getValue(), "操作成功", refundTotal.toString());
	}
}
