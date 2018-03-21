package cn.com.clubank.weihang.manage.repair.pojo;

import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 业务逻辑表（订单业务流转记录）
 * 
 * @author YangWei
 *
 */
public class WorkOperation {
	
	/**
	 * 类型：洗车单
	 */
	public static final int WORK_TYPE_WASH = 1;
	
	/**
	 * 类型：维修单
	 */
	public static final int WORK_TYPE_REPAIR = 2;
	
	/**
	 * 类型：接车单
	 */
	public static final int WORK_TYPE_RECEIVE = 3;
	
	/**
	 * 状态：待接车
	 */
	public static final int STATUS_WAIT_RECEIVE = 1;
	
	/**
	 * 状态：待服务
	 */
	public static final int STATUS_WAIT_SERVE = 2;
	
	/**
	 * 状态：服务中
	 */
	public static final int STATUS_IN_SERVE = 3;
	
	/**
	 * 状态：已完成
	 */
	public static final int STATUS_COMPLETE = 4;
	
    /**
     * @mbggenerated
     * 业务逻辑表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String woId;

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
     * 接车单ID
     */
    private String wpoId;

    /**
     * @mbggenerated
     * 接车单号
     */
    private String wpoNo;

    /**
     * @mbggenerated
     * 派单人
     */
    private String allocationBy;

    /**
     * @mbggenerated
     * 派单人名
     */
    private String allocationName;

    /**
     * @mbggenerated
     * 接单人名
     */
    private String receiveName;

    /**
     * @mbggenerated
     * 接单人
     */
    private String receiveBy;

    /**
     * @mbggenerated
     * 派单时间
     */
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date allocationDate; //派单时间-创建时间
    
   /**
    * @mbggenerated
    * 接单时间
    */
   private Date receiveDate; 

    /**
     * @mbggenerated
     * 状态 : 1待接车  2待服务  3服务中  4已完成
     */
    private Integer status = 1;

    /**
     * @mbggenerated
     * 工单属性：1、正常单  2、返工单
     */
    private Integer workOrderType = 1;

    /**
     * @mbggenerated
     * 工单ID 根据工单类型保存：维修单id；洗车单id
     */
    private String workOrderId;

    /**
     * @mbggenerated
     * 工单类型：1、洗车单 2、维修单 3、接车单
     */
    private Integer workType;

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

     
    public String getWoId() {
        return woId;
    }

    
    public void setWoId(String woId) {
        this.woId = woId == null ? null : woId.trim();
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

     
    public String getWpoId() {
        return wpoId;
    }

     
    public void setWpoId(String wpoId) {
        this.wpoId = wpoId == null ? null : wpoId.trim();
    }

     
    public String getWpoNo() {
        return wpoNo;
    }

     
    public void setWpoNo(String wpoNo) {
        this.wpoNo = wpoNo == null ? null : wpoNo.trim();
    }

    
    public String getAllocationBy() {
        return allocationBy;
    }

     
    public void setAllocationBy(String allocationBy) {
        this.allocationBy = allocationBy == null ? null : allocationBy.trim();
    }

     
    public String getAllocationName() {
        return allocationName;
    }

    
    public void setAllocationName(String allocationName) {
        this.allocationName = allocationName == null ? null : allocationName.trim();
    }

     
    public String getReceiveName() {
        return receiveName;
    }

    
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName == null ? null : receiveName.trim();
    }

     
    public String getReceiveBy() {
        return receiveBy;
    }

     
    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy == null ? null : receiveBy.trim();
    }

     
    public Date getAllocationDate() {
        return allocationDate;
    }

     
    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

     
    public Integer getStatus() {
        return status;
    }

     
    public void setStatus(Integer status) {
        this.status = status;
    }

     
    public Integer getWorkOrderType() {
        return workOrderType;
    }

     
    public void setWorkOrderType(Integer workOrderType) {
        this.workOrderType = workOrderType;
    }

     
    public String getWorkOrderId() {
        return workOrderId;
    }

     
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId == null ? null : workOrderId.trim();
    }

     
    public Integer getWorkType() {
        return workType;
    }

     
    public void setWorkType(Integer workType) {
        this.workType = workType;
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

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
}