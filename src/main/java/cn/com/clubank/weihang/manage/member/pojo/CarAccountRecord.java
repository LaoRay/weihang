package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆账户流水表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarAccountRecord {

	/**
	 * 支付方式－转账
	 */
	public static final int PAY_WAY_TRANSFER = 1;
	
	/**
	 * 支付方式-支付宝
	 */
	public static final int PAY_WAY_ALIPAY = 2;
	
	/**
	 * 支付方式-微信
	 */
	public static final int PAY_WAY_WEIXIN = 3;
	
	/**
	 * 支付方式-现金
	 */
	public static final int PAY_WAY_CASH = 4;
	
	/**
	 * 支付方式-刷卡
	 */
	public static final int PAY_WAY_CREDIT = 5;
	
	/**
	 * 支付方式-退款
	 */
	public static final int PAY_WAY_REFUND = 6;
	
	/**
	 * 车辆账户流水主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String recordId;
	
	/**
     * 第三方流水号
     */
    private String transactionId;
    
    /**
     * 账户的支付类型：１－转账，２－支付宝，３－微信，４－现金，５－刷卡  6-退款
     */
    private Integer accountPayType = 0;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 期初金额
	 */
	private BigDecimal beginningAmount;

	/**
	 * 期末金额
	 */
	private BigDecimal finishAmount;

	/**
	 * 交易金额
	 */
	private BigDecimal dealAmount;

	/**
	 * 金额来源类型 1充值 2消费 3退款
	 */
	private Integer accountSourceType;

	/**
	 * 消费对应得订单id
	 */
	private String accountSource;

	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private String createUserId;

	/**
	 * 车辆id
	 */
	private String carId;

	/**
	 * 账户类型(金额变化类型)1增加 2减少
	 */
	private Integer accountType;

	/**
	 * 是否集团扣费
	 */
	private Boolean isGroup = false;

	/**
	 * 使用的车辆id
	 */
	private String carIdUser;
	
	/**
	 * 自定义属性：车牌号
	 */
	private String carNo;
	
	/**
	 * 自定义属性：客户姓名
	 */
	private String customerName;
	
	public String getAccountPayTypeStr() {
		if (accountPayType == PAY_WAY_TRANSFER) {
			return "转账";
		} else if (accountPayType == PAY_WAY_ALIPAY) {
			return "支付宝充值";
		} else if (accountPayType == PAY_WAY_CASH) {
			return "现金充值";
		} else if (accountPayType == PAY_WAY_CREDIT) {
			return "刷卡充值";
		} else if (accountPayType == PAY_WAY_REFUND) {
			return "退款";
		} else if (accountPayType == PAY_WAY_WEIXIN) {
			return "微信充值";
		}
		return "";
	}
}