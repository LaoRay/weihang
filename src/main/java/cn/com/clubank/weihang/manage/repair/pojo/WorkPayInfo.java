package cn.com.clubank.weihang.manage.repair.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 订单支付流水表
 * @author Liangwl
 *
 */
public class WorkPayInfo {
	
	/**
	 * 支付类型-洗车
	 */
	public static final int PAY_TYPE_WASH = 1;
	
	/**
	 * 支付类型-维修
	 */
	public static final int PAY_TYPE_REPAIR = 2;
	
	/**
	 * 支付类型-打赏
	 */
	public static final int PAY_TYPE_TIP = 3;
	
	/**
	 * 支付类型-商品订单
	 */
	public static final int PAY_TYPE_ORDER = 4;
	
	/**
	 * 支付类型-保险订单
	 */
	public static final int PAY_TYPE_INSURANCE = 5;
	
	/**
	 * 支付类型-特殊订单
	 */
	public static final int PAY_TYPE_SPECIAL= 6;
	
	
	/**
	 * 支付方式-会员帐号
	 */
	public static final int PAY_WAY_CUSTOMER = 1;
	
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
     * @mbggenerated
     * 订单支付流水表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wpiId;

    /**
     * @mbggenerated
     * 支付类型：1洗车 2维修 3打赏 4商品订单 5会员充值
     */
    private Integer payType;

    /**
     * @mbggenerated
     * 支付日期
     */
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date payDate;

    /**
     * @mbggenerated
     * 支付金额
     */
    private BigDecimal payTotal;

    /**
     * @mbggenerated
     * 支付方式：1-会员帐号、2-支付宝、3-微信、4-现金、5-刷卡
     */
    private Integer payWay = 0;

    /**
     * @mbggenerated
     * 单号：通过类型判断如果是洗车则是洗车单号如果是维修那就是维修单号
     */
    private String workId;

    /**
     * @mbggenerated
     * 第三方流水号
     */
    private String transactionId;

    /**
     * @mbggenerated
     * 支付结果：1-成功，2-失败
     */
    private Integer payResult;

    /**
     * @mbggenerated
     * 备注
     */
    private String description;

    /**
     * @mbggenerated
     * 创建日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    /**
     * @mbggenerated
     * 创建用户主键
     */
    private String createUserId;

    /**
     * @mbggenerated
     * 创建用户名
     */
    private String createUserName;

     
    public String getWpiId() {
        return wpiId;
    }

     
    public void setWpiId(String wpiId) {
        this.wpiId = wpiId == null ? null : wpiId.trim();
    }

     
    public Integer getPayType() {
        return payType;
    }

     
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

     
    public Date getPayDate() {
        return payDate;
    }

     
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

     
    public BigDecimal getPayTotal() {
        return payTotal;
    }

     
    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

     
    public Integer getPayWay() {
        return payWay;
    }

     
    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

     
    public String getWorkId() {
        return workId;
    }

     
    public void setWorkId(String workId) {
        this.workId = workId == null ? null : workId.trim();
    }

     
    public String getTransactionId() {
        return transactionId;
    }

     
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

     
    public Integer getPayResult() {
        return payResult;
    }

     
    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }

    
    public String getDescription() {
        return description;
    }

     
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

     
    public Date getCreateDate() {
        return createDate;
    }

     
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

     
    public String getCreateUserId() {
        return createUserId;
    }

     
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

     
    public String getCreateUserName() {
        return createUserName;
    }

     
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }
}