package cn.com.clubank.weihang.manage.repair.pojo;

import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 接车单
 * @author YangWei
 *
 */
public class WorkPickupOrder {
	
	/**
	 * 状态：服务主管未派单
	 */
	public static final int STATUS_NO_SEND = 0;
	
	/**
	 * 状态：未开始
	 */
	public static final int STATUS_NO_SERVE = 1;
	
	/**
	 * 状态：服务中
	 */
	public static final int STATUS_IN_SERVE = 2;
	
	/**
	 * 状态：已完成
	 */
	public static final int STATUS_COMPLETE = 3;
	
    /**
     * @mbggenerated
     * 接车单主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wpoId;

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
    * 工单类型：1、洗车单2、维修单
    */
   private Integer workType;
    
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
     * 服务主管或服务顾问
     */
    private String receiveBy;

    /**
     * @mbggenerated
     * 服务顾问
     */
    private String consultantBy;

    /**
     * @mbggenerated
     * 客户ID
     */
    private String customerId;

    /**
     * @mbggenerated
     * 到店方式
     */
    private Integer fromType;

    /**
     * @mbggenerated
     * 备注
     */
    private String description;

    /**
     * @mbggenerated
     * 创建日期
     */
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

    /**
     * @mbggenerated
     * 修改日期
     */
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
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
     * 状态    0服务主管未派单，1未开始  2服务中  3已完成
     */
    private Integer status = 1;

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
     * 使用的商品（服务类、汽车用品类）详情ids
     * 逗号间隔
     */
    private String orderDetailIds;

     
    public String getWpoId() {
        return wpoId;
    }

     
    public void setWpoId(String wpoId) {
        this.wpoId = wpoId == null ? null : wpoId.trim();
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

     
    public String getReceiveBy() {
        return receiveBy;
    }

     
    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy == null ? null : receiveBy.trim();
    }

     
    public String getConsultantBy() {
        return consultantBy;
    }

     
    public void setConsultantBy(String consultantBy) {
        this.consultantBy = consultantBy == null ? null : consultantBy.trim();
    }

     
    public String getCustomerId() {
        return customerId;
    }

     
    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

     
    public Integer getFromType() {
        return fromType;
    }

    
    public void setFromType(Integer fromType) {
        this.fromType = fromType;
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

     
    public Integer getStatus() {
        return status;
    }

     
    public void setStatus(Integer status) {
        this.status = status;
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

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public Integer getWorkType() {
		return workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}


	public String getOrderDetailIds() {
		return orderDetailIds;
	}


	public void setOrderDetailIds(String orderDetailIds) {
		this.orderDetailIds = orderDetailIds;
	}
}