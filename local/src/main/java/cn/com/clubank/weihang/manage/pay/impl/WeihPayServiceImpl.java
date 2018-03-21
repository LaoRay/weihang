package cn.com.clubank.weihang.manage.pay.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.common.util.XMLUtil;
import cn.com.clubank.weihang.common.weihpay.WeihAlipayUtil;
import cn.com.clubank.weihang.manage.member.dao.CarGroupMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.IAccountBenefitService;
import cn.com.clubank.weihang.manage.member.service.ICarAccountRecordService;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.member.service.ICouponListService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.pay.WeihPayService;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.repair.dao.WorkPayInfoMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkRepairMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkWashMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPayInfo;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;
import cn.com.clubank.weihang.manage.repair.pojo.WorkWash;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付处理
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class WeihPayServiceImpl implements WeihPayService {

	@Resource
	private WeihAlipayUtil weihAlipayUtil;
	@Resource
	private WorkRepairMapper workRepairMapper;
	@Resource
	private WorkWashMapper workWashMapper;
	@Resource
	private OrderListMapper orderListMapper;
	@Resource
	private CarInfoMapper carInfoMapper;
	@Resource
	private IMessageService iMessageService;
	@Resource
	private WorkPayInfoMapper workPayInfoMapper;
	@Resource
	private ICarInfoService iCarInfoService;
	@Resource
	private CarGroupMapper carGroupMapper;
	@Resource
	private ICarAccountRecordService iCarAccountRecordService;
	@Resource
	private IAccountBenefitService iAccountBenefitService;
	@Autowired
	private ICouponListService couponListService;
	@Autowired
	private IntegralService integralService;

	@Override
	public CommonResult alipaySignature(String objNo, String total, String customerId, Integer type) {
		log.info("开始签名：objNo【" + objNo + "】，customerId【" + customerId + "】，type【" + type + "】");
		String outTradeNo = "";
		String subject = "";
		String subjectRoot = "陕西威航-";
		JSONObject params = new JSONObject(); // 公共参数
		if (type == Constants.INT_1) {
			// 维修单
			WorkRepair info = workRepairMapper.selectByNo(objNo);
			if (info != null) {
				outTradeNo = info.getWorkRepairNo();
				subject = "维修单【" + outTradeNo + "】支付";
				params.put("objId", info.getWrId());
			}
		} else if (type == Constants.INT_2) {
			// 洗车单
			WorkWash info = workWashMapper.selectByNo(objNo);
			if (info != null) {
				outTradeNo = info.getWorkWashNo();
				subject = "洗车单【" + outTradeNo + "】支付";
				params.put("objId", info.getWwId());
			}
		} else if (type == Constants.INT_3) {
			// 商品
			OrderList info = orderListMapper.selectByNo(objNo);
			if (info != null) {
				outTradeNo = info.getOrderNo();
				subject = "商品订单【" + outTradeNo + "】支付";
				params.put("objId", info.getOrderId());
			}
		} else if (type == Constants.INT_4) {
			// 会员充值
			CarInfo info = carInfoMapper.selectDefaultCarByCustomerId(customerId);
			log.info("会员充值：customerId【" + customerId + "】，type【" + type + "】");
			if (info != null) {
				outTradeNo = "MEMBER" + new SimpleDateFormat("yyyyDDDssSSS").format(new Date());
				subject = "会员【" + info.getCarNo() + "】充值";
				params.put("objId", customerId);
			}
		} else if (type == Constants.INT_5) {
			// 洗车打赏
			WorkWash info = workWashMapper.selectByNo(objNo);
			if (info != null) {
				outTradeNo = info.getWorkWashNo();
				subject = "洗车单【" + outTradeNo + "】打赏";
				params.put("objId", info.getWwId());
			}
		} else if (type == Constants.INT_6) {
			// 集团组充值
			CarGroup group = carGroupMapper.selectByPrimaryKey(objNo);
			if (group != null) {
				outTradeNo = "GROUP" + new SimpleDateFormat("yyyyDDDssSSS").format(new Date());
				subject = "集团组帐号【" + group.getGroupName() + "】充值";
				params.put("objId", group.getGroupId());
			}
		}
		if (StringUtil.isBlank(outTradeNo)) {
			log.info("参数错误:objNo【" + objNo + "】，customerId【" + customerId + "】，type【" + type + "】");
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，没有找到对象！");
		}

		params.put("type", type);
		String orderStr = weihAlipayUtil.sdkExecute(outTradeNo, subjectRoot + subject, total, params.toString());
		log.info("签名成功：orderStr【" + orderStr + "】，total【" + total + "】");

		if (StringUtil.isBlank(orderStr)) {
			log.info("签名失败:objNo【" + objNo + "】，customerId【" + customerId + "】，type【" + type + "】");
			return new CommonResult(ResultCode.FAIL.getValue(), "签名失败");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "签名成功", orderStr);
	}

	@Override
	public void alipayNotify(HttpServletRequest request) {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		// 类型：1维修单、2洗车单、3商品、4会员充值、5洗车单打赏、6集团组充值
		int type = 0;
		String objId = "";
		try {
			JSONObject json = JSON.parseObject(URLDecoder.decode(params.get("passback_params"), "utf-8"));
			type = json.getIntValue("type"); // 类型：1维修单、2洗车单、3商品、4会员充值、5洗车单打赏、6集团组充值
			objId = json.getString("objId");
		} catch (Exception e) {
			log.info("解析公共参数出错：{}", params.get("passback_params"));
			e.printStackTrace();
		}

		boolean flag = weihAlipayUtil.rsaCheckV1(params);
		if (flag && type > 0) {
			// 付款金额
			BigDecimal total = new BigDecimal(params.get("total_amount"));
			// 商户订单号
			String out_trade_no = params.get("out_trade_no");
			// 支付宝交易号
			String tradeNo = params.get("trade_no");
			// 支付状态
			String tradeStatus = params.get("trade_status");

			log.info("支付宝回调结果：type【{}】，tradeStatus【{}】，out_trade_no【{}】，tradeNo【{}】，total【{}】，objId【{}】", type,
					tradeStatus, out_trade_no, tradeNo, total, objId);
			CommonResult result = handleJudge(out_trade_no, tradeNo, total, "TRADE_SUCCESS".equals(tradeStatus) ? 1 : 0,
					type, objId, WorkPayInfo.PAY_WAY_ALIPAY);
			log.info("支付宝回调处理结果：{}", JSON.toJSONString(result));
		}
	}

	@Override
	public void weixinNotify(HttpServletRequest request) {
		try {
			InputStream in = request.getInputStream();
			String s = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			StringBuffer sb = new StringBuffer();
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			in.close();
			Map<String, String> params = XMLUtil.doXMLParse(sb.toString());

			// 商户订单号
			String out_trade_no = (String) params.get("out_trade_no");
			// 第三方流水号
			String tradeNo = (String) params.get("transaction_id");
			// 付款金额（微信金额要除以100）
			BigDecimal notifyTotal = new BigDecimal(params.get("total_fee"));
			BigDecimal base = new BigDecimal(100);
			BigDecimal total = notifyTotal.divide(base, 2, RoundingMode.HALF_UP);
			// 支付结果
			String tradeStatus = (String) params.get("result_code");

			// 类型：1维修单、2洗车单、3商品、4会员充值、5洗车单打赏、6集团组充值
			int type = 0;
			String objId = "";
			try {
				JSONObject json = JSON.parseObject(params.get("attach"));
				type = json.getIntValue("type"); // 类型：1维修单、2洗车单、3商品、4会员充值、5洗车单打赏
				objId = json.getString("objId");
			} catch (Exception e) {
				log.info("解析公共参数出错：{}", params.get("attach"));
				type = Integer.valueOf(params.get("attach"));
				e.printStackTrace();
			}

			log.info("微信回调结果：type【{}】，tradeStatus【{}】，out_trade_no【{}】，tradeNo【{}】，total【{}】，objId【{}】", type,
					tradeStatus, out_trade_no, tradeNo, total, objId);
			CommonResult result = handleJudge(out_trade_no, tradeNo, total, "SUCCESS".equals(tradeStatus) ? 1 : 0, type,
					objId, WorkPayInfo.PAY_WAY_WEIXIN);
			log.info("微信回调处理结果：{}", JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断处理业务
	 */
	private CommonResult handleJudge(String out_trade_no, String tradeNo, BigDecimal total, int tradeStatus, int type,
			String objId, int payWay) {
		if (type == Constants.INT_1) {
			// 维修单
			return handleWorkRepair(out_trade_no, tradeNo, total, tradeStatus, payWay);
		} else if (type == Constants.INT_2) {
			// 洗车单
			return handleWorkWash(out_trade_no, tradeNo, total, tradeStatus, payWay);
		} else if (type == Constants.INT_3) {
			// 商品
			return handleOrderList(out_trade_no, tradeNo, total, tradeStatus, payWay);
		} else if (type == Constants.INT_4) {
			// 会员充值时objId是customerId
			return handleCustomerRecharge(objId, tradeNo, total, tradeStatus, payWay);
		} else if (type == Constants.INT_5) {
			// 洗车打赏
			return handleWorkWashTip(out_trade_no, tradeNo, total, tradeStatus, payWay);
		} else if (type == Constants.INT_6) {
			// 集团组帐号充值
			return handleGroupRecharge(objId, tradeNo, total, tradeStatus, payWay);
		}
		return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "未找到处理业务类型");
	}

	@Override
	public CommonResult handleWorkRepair(String repairNo, String tradeNo, BigDecimal total, int tradeStatus,
			int payWay) {
		WorkRepair info = workRepairMapper.selectByNo(repairNo);
		if (info == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到维修单");
		}
		WorkPayInfo record = null; // 支付记录
		if (info != null) {
			record = new WorkPayInfo();
			record.setPayType(WorkPayInfo.PAY_TYPE_REPAIR);
			record.setPayTotal(total);
			record.setPayWay(payWay);
			record.setWorkId(info.getWrId());
			record.setTransactionId(tradeNo); // 第三方交易号
			record.setPayResult(tradeStatus);
			// 保存支付信息
			workPayInfoMapper.insert(record);

			if (record.getPayResult() == 1) {
				info.setPayWay(record.getPayWay());// 支付方式
				info.setPayTotal(record.getPayTotal());// 支付金额
				info.setPayStatus(1);// 已支付
				// 更新维修单状态：已支付
				info.setStatus(WorkRepair.STATUS_ALREADY_PAY);
				workRepairMapper.updateByPrimaryKey(info);

				// 推送消息至车间组长
				String title = String.format("维修单号：%s", info.getWorkRepairNo());
				String content = String.format("【%s】 客户已确认维修项目及材料费用，请您尽快开始维修！", info.getCarNo());
				iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_REPAIR, title, content, info.getWrId()),
						info.getRepairSupervisor());
			}
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult handleWorkWash(String washNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay) {
		WorkWash info = workWashMapper.selectByNo(washNo);
		if (info == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到洗车单");
		}
		WorkPayInfo record = null; // 支付记录
		if (info != null) {
			record = new WorkPayInfo();
			record.setPayType(WorkPayInfo.PAY_TYPE_WASH);
			record.setPayTotal(total);
			record.setPayWay(payWay);
			record.setWorkId(info.getWwId());
			record.setTransactionId(tradeNo); // 第三方交易号
			record.setPayResult(tradeStatus);
			// 保存支付信息
			workPayInfoMapper.insert(record);

			if (record.getPayResult() == 1) {
				info.setPayWay(record.getPayWay());// 支付方式
				info.setPayTotal(record.getPayTotal());// 支付金额
				info.setPayStatus(1); // 已支付
				// 更新洗车单状态：已支付
				info.setStatus(WorkWash.STATUS_ALREADY_PAY);
				workWashMapper.updateByPrimaryKey(info);
				// 推送至洗车组长
				String title = String.format("洗车单号：%s", info.getWorkWashNo());
				String content = String.format("【%s】 客户已确认洗车项目及费用，请您尽快开始洗车！", info.getCarNo());
				iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_WASH, title, content, info.getWwId()),
						info.getGroupLeader());
			}
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult handleWorkWashTip(String washNo, String tradeNo, BigDecimal total, int tradeStatus,
			int payWay) {
		WorkWash info = workWashMapper.selectByNo(washNo);
		if (info == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到洗车单");
		}
		WorkPayInfo record = null; // 支付记录
		if (info != null) {
			record = new WorkPayInfo();
			record.setPayType(WorkPayInfo.PAY_TYPE_TIP);
			record.setPayTotal(total);
			record.setPayWay(payWay);
			record.setWorkId(info.getWwId());
			record.setTransactionId(tradeNo); // 第三方交易号
			record.setPayResult(tradeStatus);
			// 保存支付信息
			workPayInfoMapper.insert(record);

			if (record.getPayResult() == 1) {
				info.setTipTotal(record.getPayTotal());// 打赏金额
				workWashMapper.updateByPrimaryKey(info);
			}
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult handleCustomerRecharge(String customerId, String tradeNo, BigDecimal total, int tradeStatus,
			int payWay) {
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		if (carInfo == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到客户车辆信息");
		}
		if (carInfo != null && tradeStatus == 1) {
			// 保存车辆账户流水表
			CarAccountRecord record = new CarAccountRecord();
			record.setBeginningAmount(carInfo.getAccount()); // 期初金额
			record.setDealAmount(total); // 产生金额
			record.setFinishAmount(record.getBeginningAmount().add(total)); // 期末金额
			record.setAccountSourceType(1); // 来源类型：1-充值
			record.setCarId(carInfo.getCarId());
			record.setAccountType(1);
			iCarAccountRecordService.insert(record);

			// 更新车辆信息表的账户
			carInfo.setAccount(record.getFinishAmount());
			// 充值总额
			carInfo.setAccountRechargeTotal(
					carInfo.getAccountRechargeTotal() == null ? total : carInfo.getAccountRechargeTotal().add(total));
			// 等级（根据充值金额判断）
			AccountBenefit ab = iAccountBenefitService.getByAccount(carInfo.getAccountRechargeTotal());
			carInfo.setAbId(ab == null ? null : ab.getAbId());

			iCarInfoService.updateByPrimaryKey(carInfo);

			// 充值赠送积分
			integralService.rechargeIntegral(customerId, total.divide(new BigDecimal(100)));

		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult handleOrderList(String orderNo, String tradeNo, BigDecimal total, int tradeStatus, int payWay) {
		OrderList info = orderListMapper.selectByNo(orderNo);
		if (info == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到商品订单");
		}
		WorkPayInfo record = null; // 支付记录
		if (info != null) {
			info.setPayType(payWay); // 支付方式
			info.setPayTime(new Date()); // 支付时间
			info.setPayStatus(2); // 支付状态
			info.setOrderStatus(2); // 待收货
			info.setDeliveryStatus(1); // 等待发货
			info.setOrderPayAmount(total); // 实付金额
			orderListMapper.updateByPrimaryKey(info);

			// 将订单中使用的优惠券状态置为已使用
			if (!StringUtils.isBlank(info.getCouponCode())) {
				for (String couponCode : info.getCouponCode().split(",")) {
					couponListService.useCoupon(couponCode, info.getOrderNo());
				}
			}

			// 消费带积分的商品增加消费积分
			integralService.productIntegral(info.getCustomerId(), info.getOrderId());

			record = new WorkPayInfo();
			record.setPayType(WorkPayInfo.PAY_TYPE_ORDER);
			record.setPayTotal(total);
			record.setPayWay(payWay);
			record.setWorkId(info.getOrderId());
			record.setTransactionId(tradeNo); // 第三方交易号
			record.setPayResult(tradeStatus);
			// 保存支付信息
			workPayInfoMapper.insert(record);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult handleGroupRecharge(String groupId, String tradeNo, BigDecimal total, int tradeStatus,
			int payWay) {
		CarGroup group = carGroupMapper.selectByPrimaryKey(groupId);
		CarInfo carInfo = carInfoMapper.selectByPrimaryKey(group == null ? "" : group.getGroupCar());
		if (carInfo == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误，未找到集团组车辆帐号信息");
		}
		if (carInfo != null && tradeStatus == 1) {
			// 保存车辆账户流水表
			CarAccountRecord record = new CarAccountRecord();
			record.setBeginningAmount(carInfo.getAccount()); // 期初金额
			record.setDealAmount(total); // 产生金额
			record.setFinishAmount(record.getBeginningAmount().add(total)); // 期末金额
			record.setAccountSourceType(1); // 来源类型：1-充值
			record.setCarId(carInfo.getCarId());
			record.setAccountType(1); // 增加
			iCarAccountRecordService.insert(record);

			// 更新车辆信息表的账户
			carInfo.setAccount(record.getFinishAmount());
			// 充值总额
			carInfo.setAccountRechargeTotal(
					carInfo.getAccountRechargeTotal() == null ? total : carInfo.getAccountRechargeTotal().add(total));
			// 等级（根据充值金额判断）
			AccountBenefit ab = iAccountBenefitService.getByAccount(carInfo.getAccountRechargeTotal());
			carInfo.setAbId(ab == null ? null : ab.getAbId());

			iCarInfoService.updateByPrimaryKey(carInfo);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult memberAccountPay(String objNo, BigDecimal total, String customerId, Integer type,
			Integer isGroup) {
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		// 帐号是否存在
		if (carInfo == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "未找到车辆数据");
		}
		CarAccountRecord record = new CarAccountRecord(); // 支付流水记录
		// 判断是否集团付费，如果是，使用集团账户付款
		if (isGroup == Constants.INT_1) {
			// 集团付费
			CarGroup group = carGroupMapper.selectByPrimaryKey(carInfo.getGroupId());
			CarInfo groupCar = carInfoMapper.selectByPrimaryKey(group == null ? "" : group.getGroupCar());
			if (groupCar == null) {
				log.info("集团组帐号不存在：{}", carInfo.getCarNo());
				return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "集团组帐号不存在");
			}
			// 账户金额数额
			if (groupCar.getAccount() == null || groupCar.getAccount().compareTo(total) == -1) {
				return new CommonResult(ResultCode.FAIL.getValue(), "集团组账户余额不足");
			}
			// 支付流水记录-所用车辆是集团组的虚拟车辆
			record.setBeginningAmount(groupCar.getAccount()); // 期初金额
			record.setCarIdUser(groupCar.getCarId()); // 所用车辆
			// 更新集团组帐号金额
			groupCar.setAccount(groupCar.getAccount().subtract(total));
			carInfoMapper.updateByPrimaryKey(groupCar);
		} else {
			// 账户金额数额
			if (carInfo.getAccount() == null || carInfo.getAccount().compareTo(total) == -1) {
				return new CommonResult(ResultCode.FAIL.getValue(), "账户余额不足，请先充值");
			}
			// 支付流水记录-所用车辆是当前车辆
			record.setBeginningAmount(carInfo.getAccount()); // 期初金额
			record.setCarIdUser(carInfo.getCarId()); // 所用车辆
			// 更新账户金额
			carInfo.setAccount(carInfo.getAccount().subtract(total));
			carInfoMapper.updateByPrimaryKey(carInfo);
		}

		log.info("会员帐号支付：type【{}】，total【{}】，objNo【{}】", type, total, objNo);
		CommonResult result = handleJudge(objNo, carInfo.getCarNo(), total, 1, type, carInfo.getCarId(),
				WorkPayInfo.PAY_WAY_CUSTOMER);
		log.info("会员帐号支付结果：{}", JSON.toJSONString(result));
		if (result.getApiStatus() == ResultCode.SUCC.getValue()) {
			// 保存支付流水记录
			record.setDealAmount(total); // 产生金额
			record.setFinishAmount(record.getBeginningAmount().subtract(total)); // 期末金额
			record.setAccountSourceType(2); // 来源类型：2-消费
			record.setAccountSource(objNo); // 消费对应的订单no
			record.setCarId(carInfo.getCarId());
			record.setAccountType(2); // 减少
			iCarAccountRecordService.insert(record);
		}
		return result;
	}

}
