package cn.com.clubank.weihang.manage.product.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderList {

	/**
	 * 订单主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String orderId;

	/**
	 * 订单流水号
	 */
	private String orderNo;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	private String createUserId;

	private String createUserName;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	private String modifyUserId;

	private String modifyUserName;

	/**
	 * 支付方式 1账户 2支付宝 3微信
	 */
	private Integer payType;

	/**
	 * 订单状态 0已删除(app端不显示) 1待付款 2待收货 3待评价 4已完成 5待付定金 6已预约 7正在服务 8已失效 9已取消 10已退款 11退款中 12售后服务
	 */
	private Integer orderStatus = 1;

	/**
	 * 订单类型 1正常 2退 3退换
	 */
	private Integer orderType = 1;

	/**
	 * 订单类别 1普通订单 2预购订单
	 */
	private Integer orderCategory = 1;

	/**
	 * 客户id
	 */
	private String customerId;

	/**
	 * 订单总额
	 */
	private BigDecimal orderAmount;

	/**
	 * 订单实际支付总额
	 */
	private BigDecimal orderPayAmount;

	/**
	 * 物流状态 1等待发货2已发货3已签收
	 */
	private Integer deliveryStatus;

	/**
	 * 物流类型
	 */
	private Integer deliveryType;

	/**
	 * 配送地址
	 */
	private String deliveryAddress;

	/**
	 * 订单来源
	 */
	private Integer orderSource;

	/**
	 * 支付状态 1待支付 2已支付
	 */
	private Integer payStatus = 1;

	/**
	 * 优惠金额
	 */
	private BigDecimal discount;

	/**
	 * 券码
	 */
	private String couponCode;

	/**
	 * 发票
	 */
	private String invoiceCode;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 付款时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

	/**
	 * 发货时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date deliveryTime;

	/**
	 * 是否活动商品 1是 2否
	 */
	private Integer isActivity;
	
	/**
	 * 申请退款时间
	 */
	private Date refundApplyTime;
	
	public static String getPayType(Integer payType){
		if(payType==1){
			return "账户";
		}else if(payType==2){
			return "支付宝";
		}else if(payType==3){
			return "微信";
		}
		return null;
	}
	
	public static String getOrderStatus(Integer orderStatus){
		if(orderStatus==0){
			return "已删除";
		}else if(orderStatus==1){
			return "待付款";
		}else if(orderStatus==2){
			return "待收货";
		}else if(orderStatus==3){
			return "待评价";
		}else if(orderStatus==4){
			return "已完成";
		}else if(orderStatus==5){
			return "待付定金";
		}else if(orderStatus==6){
			return "已预约";
		}else if(orderStatus==7){
			return "正在服务";
		}else if(orderStatus==8){
			return "已失效";
		}else if(orderStatus==9){
			return "已取消";
		}else if(orderStatus==10){
			return "已退款";
		}else if(orderStatus==11){
			return "退款中";
		}else if(orderStatus==12){
			return "售后服务";
		}
		return null;
	}
}