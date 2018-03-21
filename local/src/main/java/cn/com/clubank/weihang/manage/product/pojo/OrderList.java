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
	 * 订单状态 0已删除(app端不显示)1待付款2待收货3待评价4已完成5待付定金6已预约7正在服务8已失效9已取消10已退款
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
}