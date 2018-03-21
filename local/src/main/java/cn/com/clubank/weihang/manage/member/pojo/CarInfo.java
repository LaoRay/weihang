package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 车辆信息表
 * @author YangWei
 *
 */
public class CarInfo {

	/**
	 * 车辆主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String carId;

	/**
	 * 客户关联ID
	 */
    private String customerId;

    /**
     * 车牌号码
     */
    private String carNo;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     * 车辆品牌ID
     */
    private String carBrandId;

    /**
     * 车型信息
     */
    private String carTypeInfo;

    /**
     * 是否默认车辆
     */
    private Boolean isDefault = false;
    
    /**
     * 是否虚拟车辆
     */
    private Boolean isVirtual = false;

	/**
     * 删除标记
     */
    private Integer deleteMark = 0;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建日期
     */
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 创建用户主键
     */
    private String createUserId;

    /**
     * 创建用户名
     */
    private String createUserName;

    /**
     * 修改日期
     */
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    /**
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * 修改用户名
     */
    private String modifyUserName;

    /**
     * 账户
     */
    private BigDecimal account;

    /**
     * 车架号
     */
    private String carVin;

    /**
     * 接送次数
     */
    private Integer giveCount=0;

    /**
     * 救援次数
     */
    private Integer helpCount=0;

    /**
     * 集团组关联ID
     */
    private String groupId;
  
    /**
     * 充值总额
     */
    private BigDecimal accountRechargeTotal; 
  
    /**
     * 账户权益ID
     */
    private String abId;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId == null ? null : carId.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo == null ? null : carNo.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId == null ? null : carBrandId.trim();
    }

    public String getCarTypeInfo() {
        return carTypeInfo;
    }

    public void setCarTypeInfo(String carTypeInfo) {
        this.carTypeInfo = carTypeInfo == null ? null : carTypeInfo.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	
    public Integer getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(Integer deleteMark) {
        this.deleteMark = deleteMark;
    }

    public Integer getEnabledMark() {
        return enabledMark;
    }

    public void setEnabledMark(Integer enabledMark) {
        this.enabledMark = enabledMark;
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

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin == null ? null : carVin.trim();
    }

    public Integer getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(Integer giveCount) {
        this.giveCount = giveCount;
    }

    public Integer getHelpCount() {
        return helpCount;
    }

    public void setHelpCount(Integer helpCount) {
        this.helpCount = helpCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

	public BigDecimal getAccountRechargeTotal() {
		return accountRechargeTotal;
	}

	public void setAccountRechargeTotal(BigDecimal accountRechargeTotal) {
		this.accountRechargeTotal = accountRechargeTotal;
	}

	public String getAbId() {
		return abId;
	}

	public void setAbId(String abId) {
		this.abId = abId;
	}
    
}