package cn.com.clubank.weihang.manage.repair.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import cn.com.clubank.weihang.common.util.Constants;

/**
 * 洗车单
 * @author Liangwl
 *
 */
public class WorkWash {
	
	/**
	 * 待接车
	 */
	public static final int STATUS_WAIT_RECEIVE = 1;
	
	/**
	 * 已接车
	 */
	public static final int STATUS_ALREADY_RECEIVE = 2;
	
	/**
	 * 待支付
	 */
	public static final int STATUS_WAIT_PAY = 3;
	
	/**
	 * 已支付
	 */
	public static final int STATUS_ALREADY_PAY = 4;
	
	/**
	 * 洗车中
	 */
	public static final int STATUS_IN_WASH = 5;
	
	/**
	 * 客户确认
	 */
	public static final int STATUS_CUSTOMER_CONFIRM = 6;
	
	/**
	 * 完成
	 */
	public static final int STATUS_COMPLETE = 7;
	
	/**
	 * 不同意
	 */
	public static final int STATUS_DISAGREE = 8;
	
    /**
     * @mbggenerated
     * 洗车单主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wwId;

    /**
     * @mbggenerated
     * 洗车单号
     */
    private String workWashNo;

    /**
     * @mbggenerated
     * 接车单号
     */
    private String wpoNo;

    /**
     * @mbggenerated
     * 预约单号
     */
    private String reservationOrderNo;
    
    /**
    * @mbggenerated
    * 客户ID
    */
   private String customerId;

    /**
     * @mbggenerated
     * 接车日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date receiveDate;

    /**
     * @mbggenerated
     * 服务顾问
     */
    private String consultantBy;

    /**
     * @mbggenerated
     * 车间组长
     */
    private String groupLeader;

    /**
     * @mbggenerated
     * 洗车师
     */
    private String supervisorBy;

    /**
     * @mbggenerated
     * 洗车类型 1、普洗 2、精洗 3、VIP洗车
     */
    private Integer washType = 1;
    
    /**
    *@mbggenerated
    * 洗车费用
    */
   private BigDecimal washCost;

    /**
     * @mbggenerated
     * 车辆ID
     */
    private String carId;
    
    /**
    * @mbggenerated
    * 车牌号
    */
   private String carNo;

    /**
     * @mbggenerated
     * 状态 : 1待接车  2已接车  3待支付  4已支付  5洗车中  6客户确认  7已完成  8不同意
     */
    private Integer status;

    /**
     * @mbggenerated
     * 券码
     */
    private String couponCode;

    /**
     * @mbggenerated
     * 支付金额
     */
    private BigDecimal payTotal;

    /**
     * @mbggenerated
     * 支付方式：1-会员帐号、2-支付宝、3-微信、4-现金、5-刷卡
     */
    private Integer payWay;

    /**
     * @mbggenerated
     * 支付状态：0待支付  1已支付
     */
    private Integer payStatus = 0;

    /**
     * @mbggenerated
     * 打赏
     */
    private BigDecimal tipTotal;

    /**
     * @mbggenerated
     * 到店方式
     */
    private Integer fromType;

    /**
     * @mbggenerated
     * 联系人
     */
    private String linkman;

    /**
     * @mbggenerated
     * 联系人手机
     */
    private String linkmanMobile;

    /**
     * @mbggenerated
     * 备注：客户意见
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

     
    public String getWwId() {
        return wwId;
    }

     
    public void setWwId(String wwId) {
        this.wwId = wwId == null ? null : wwId.trim();
    }

     
    public String getWorkWashNo() {
        return workWashNo;
    }

    
    public void setWorkWashNo(String workWashNo) {
        this.workWashNo = workWashNo == null ? null : workWashNo.trim();
    }

     
    public String getWpoNo() {
        return wpoNo;
    }

     
    public void setWpoNo(String wpoNo) {
        this.wpoNo = wpoNo == null ? null : wpoNo.trim();
    }

     
    public String getReservationOrderNo() {
        return reservationOrderNo;
    }

     
    public void setReservationOrderNo(String reservationOrderNo) {
        this.reservationOrderNo = reservationOrderNo == null ? null : reservationOrderNo.trim();
    }

     
    public Date getReceiveDate() {
        return receiveDate;
    }

     
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

     
    public String getConsultantBy() {
        return consultantBy;
    }

     
    public void setConsultantBy(String consultantBy) {
        this.consultantBy = consultantBy == null ? null : consultantBy.trim();
    }

     
    public String getGroupLeader() {
        return groupLeader;
    }

     
    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader == null ? null : groupLeader.trim();
    }

     
    public String getSupervisorBy() {
        return supervisorBy;
    }

     
    public void setSupervisorBy(String supervisorBy) {
        this.supervisorBy = supervisorBy == null ? null : supervisorBy.trim();
    }

     
    public Integer getWashType() {
        return washType;
    }

     
    public void setWashType(Integer washType) {
        this.washType = washType;
    }

     
    public String getCarId() {
        return carId;
    }

     
    public void setCarId(String carId) {
        this.carId = carId == null ? null : carId.trim();
    }

     
    public Integer getStatus() {
        return status;
    }

     
    public void setStatus(Integer status) {
        this.status = status;
    }

     
    public String getCouponCode() {
        return couponCode;
    }

     
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
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

     
    public Integer getPayStatus() {
        return payStatus;
    }

     
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

     
    public BigDecimal getTipTotal() {
        return tipTotal;
    }

     
    public void setTipTotal(BigDecimal tipTotal) {
        this.tipTotal = tipTotal;
    }

     
    public Integer getFromType() {
        return fromType;
    }

    
    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

     
    public String getLinkman() {
        return linkman;
    }

     
    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

     
    public String getLinkmanMobile() {
        return linkmanMobile;
    }

     
    public void setLinkmanMobile(String linkmanMobile) {
        this.linkmanMobile = linkmanMobile == null ? null : linkmanMobile.trim();
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

	public BigDecimal getWashCost() {
		return washCost;
	}

	public void setWashCost(BigDecimal washCost) {
		this.washCost = washCost;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getWashTypeStr() {
		if (washType == 1) {
			return "普洗";
		} else if (washType == 2) {
			return "精洗";
		} else if (washType == 3) {
			return "VIP洗车";
		}
        return Constants.NULL;
    }

}