package cn.com.clubank.weihang.manage.repair.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 维修单
 * @author Liangwl
 *
 */
public class WorkRepair {
	
	/**
	 * 暂存
	 */
	public static final int STATUS_SAVE = 0;
	
	/**
	 * 待接车
	 */
	public static final int STATUS_WAIT_RECEIVE = 1;
	
	/**
	 * 已接车
	 */
	public static final int STATUS_ALREADY_RECEIVE = 2;
	
	/**
	 * 库房待确认
	 */
	public static final int STATUS_STORE_WAIT_CONFIRM = 3;
	
	/**
	 * 服务顾问待确认
	 */
	public static final int STATUS_CONSULTANT_WAIT_CONFIRM = 4;
	
	/**
	 * 待支付（客户确认订单支付）
	 */
	public static final int STATUS_WAIT_PAY = 5;
	
	/**
	 * 修改项目（车间主管）
	 */
	public static final int STATUS_UPDATE = 6;
	
	/**
	 * 已支付
	 */
	public static final int STATUS_ALREADY_PAY = 7;
	
	/**
	 * 维修中
	 */
	public static final int STATUS_IN_REPAIR = 8;
	
	/**
	 * 客户确认
	 */
	public static final int STATUS_CUSTOMER_CONFIRM = 9;
	
	/**
	 * 完成
	 */
	public static final int STATUS_COMPLETE = 10;
	
	/**
	 * 不同意
	 */
	public static final int STATUS_DISAGREE = 11;
	
    /**
     * @mbggenerated
     * 维修单主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wrId;

    /**
     * @mbggenerated
     * 维修单号
     */
    private String workRepairNo;

    /**
     * @mbggenerated
     * 父订单ID
     */
    private String parentId;

    /**
     * @mbggenerated
     * 预约单号
     */
    private String reservationOrderNo;

    /**
     * @mbggenerated
     * 接车单号
     */
    private String wpoNo;

    /**
     * @mbggenerated
     * 接车日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date receiveDate;

    /**
     * @mbggenerated
     * 客户ID
     */
    private String customerId;

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
     * 服务顾问
     */
    private String consultantBy;

    /**
     * @mbggenerated
     * 支付金额
     */
    private BigDecimal payTotal;

    /**
     * @mbggenerated
     * 支付状态 0未支付 1已支付
     */
    private Integer payStatus = 0;

    /**
     * @mbggenerated
     * 支付方式：1-会员帐号、2-支付宝、3-微信、4-现金、5-刷卡
     */
    private Integer payWay;

    /**
     * @mbggenerated
     * 车间组长
     */
    private String repairSupervisor;

    /**
     * @mbggenerated
     * 技师
     */
    private String supervisorBy;

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
     * 公里数
     */
    private Integer kilometre;

    /**
     * @mbggenerated
     * 剩余油量
     */
    private Integer bunkers;

    /**
     * @mbggenerated
     * 到店方式
     */
    private Integer fromType;

    /**
     * @mbggenerated
     * 检查项目
     */
    private String inspectionItem;

    /**
     * @mbggenerated
     * 券码
     */
    private String couponCode;

    /**
     * @mbggenerated
     * 优惠金额
     */
    private BigDecimal discountsTotal;

    /**
     * @mbggenerated
     * 用料总价
     */
    private BigDecimal billMaterialTotal;

    /**
     * @mbggenerated
     * 工时总价
     */
    private BigDecimal manHaurTotal;

    /**
     * @mbggenerated
     * 总价
     */
    private BigDecimal total;

    /**
     * @mbggenerated
     * 状态：0暂存 1待接车 2已接车 3库房待确认 4服务顾问待确认 5待支付（客户确认订单支付） 6修改项目（车间主管） 7已支付 8维修中 9客户确认 10完成 11不同意
     */
    private Integer status = 0;

    /**
     * @mbggenerated
     * 备注：客户意见
     */
    private String remarks;

    /**
     * @mbggenerated
     * 修改日期
     */
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    /**
     * @mbggenerated
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * @mbggenerated
     * 修改用户名
     */
    private String modifyUserName;

    /**
     * @mbggenerated
     * 客户描述
     */
    private String description;

     
    public String getWrId() {
        return wrId;
    }

     
    public void setWrId(String wrId) {
        this.wrId = wrId == null ? null : wrId.trim();
    }

    
    public String getWorkRepairNo() {
        return workRepairNo;
    }

     
    public void setWorkRepairNo(String workRepairNo) {
        this.workRepairNo = workRepairNo == null ? null : workRepairNo.trim();
    }

     
    public String getParentId() {
        return parentId;
    }

     
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

     
    public String getReservationOrderNo() {
        return reservationOrderNo;
    }

     
    public void setReservationOrderNo(String reservationOrderNo) {
        this.reservationOrderNo = reservationOrderNo == null ? null : reservationOrderNo.trim();
    }

     
    public String getWpoNo() {
        return wpoNo;
    }

     
    public void setWpoNo(String wpoNo) {
        this.wpoNo = wpoNo == null ? null : wpoNo.trim();
    }

     
    public Date getReceiveDate() {
        return receiveDate;
    }

     
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    
    public String getCustomerId() {
        return customerId;
    }

     
    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

     
    public String getCarId() {
        return carId;
    }

     
    public void setCarId(String carId) {
        this.carId = carId == null ? null : carId.trim();
    }

     
    public String getConsultantBy() {
        return consultantBy;
    }

     
    public void setConsultantBy(String consultantBy) {
        this.consultantBy = consultantBy == null ? null : consultantBy.trim();
    }

     
    public BigDecimal getPayTotal() {
        return payTotal;
    }

     
    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

     
    public Integer getPayStatus() {
        return payStatus;
    }

     
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

     
    public Integer getPayWay() {
        return payWay;
    }

     
    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

     
    public String getRepairSupervisor() {
        return repairSupervisor;
    }

     
    public void setRepairSupervisor(String repairSupervisor) {
        this.repairSupervisor = repairSupervisor == null ? null : repairSupervisor.trim();
    }

     
    public String getSupervisorBy() {
        return supervisorBy;
    }

     
    public void setSupervisorBy(String supervisorBy) {
        this.supervisorBy = supervisorBy == null ? null : supervisorBy.trim();
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

    
    public Integer getKilometre() {
        return kilometre;
    }

     
    public void setKilometre(Integer kilometre) {
        this.kilometre = kilometre;
    }

     
    public Integer getBunkers() {
        return bunkers;
    }

     
    public void setBunkers(Integer bunkers) {
        this.bunkers = bunkers;
    }

     
    public Integer getFromType() {
        return fromType;
    }

     
    public void setFromType(Integer fromType) {
        this.fromType = fromType;
    }

     
    public String getInspectionItem() {
        return inspectionItem;
    }

     
    public void setInspectionItem(String inspectionItem) {
        this.inspectionItem = inspectionItem == null ? null : inspectionItem.trim();
    }

     
    public String getCouponCode() {
        return couponCode;
    }

     
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

     
    public BigDecimal getDiscountsTotal() {
        return discountsTotal;
    }

     
    public void setDiscountsTotal(BigDecimal discountsTotal) {
        this.discountsTotal = discountsTotal;
    }

     
    public BigDecimal getBillMaterialTotal() {
        return billMaterialTotal;
    }

     
    public void setBillMaterialTotal(BigDecimal billMaterialTotal) {
        this.billMaterialTotal = billMaterialTotal;
    }

    
    public BigDecimal getManHaurTotal() {
        return manHaurTotal;
    }

     
    public void setManHaurTotal(BigDecimal manHaurTotal) {
        this.manHaurTotal = manHaurTotal;
    }

     
    public BigDecimal getTotal() {
        return total;
    }

     
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

     
    public Integer getStatus() {
        return status;
    }

    
    public void setStatus(Integer status) {
        this.status = status;
    }

     
    public String getRemarks() {
        return remarks;
    }

     
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

     
    public Date getModifyDate() {
        return modifyDate;
    }

     
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    
    public String getModifyUserId() {
        return modifyUserId;
    }

     
    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId == null ? null : modifyUserId.trim();
    }

     
    public String getModifyUserName() {
        return modifyUserName;
    }

     
    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName == null ? null : modifyUserName.trim();
    }

     
    public String getDescription() {
        return description;
    }

     
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
}