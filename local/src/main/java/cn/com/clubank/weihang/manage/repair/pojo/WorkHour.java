package cn.com.clubank.weihang.manage.repair.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 工时费用表
 * @author YangWei
 *
 */
public class WorkHour {

	/**
	 * @mbggenerated
	 * 工时费用表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String whId;
	/**
	 * @mbggenerated
	 * 维修单号
	 */
	private String workOrderId;
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
	 * 项目名
	 */
	private String manHourName;
	/**
	 * @mbggenerated
	 * 普卡价
	 */
	private BigDecimal price1;
	/**
	 * @mbggenerated
	 * 金卡价
	 */
	private BigDecimal price2;
	/**
	 * @mbggenerated
	 * 钻卡价
	 */
	private BigDecimal price3;
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
	/**
	 * @mbggenerated
	 * 修改日期
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
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

	 
	public String getWhId() {
		return whId;
	}

	 
	public void setWhId(String whId) {
		this.whId = whId == null ? null : whId.trim();
	}

	 
	public String getWorkOrderId() {
		return workOrderId;
	}

	 
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId == null ? null : workOrderId.trim();
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

	 
	public String getManHourName() {
		return manHourName;
	}

	 
	public void setManHourName(String manHourName) {
		this.manHourName = manHourName == null ? null : manHourName.trim();
	}

	 
	public BigDecimal getPrice1() {
		return price1;
	}

	 
	public void setPrice1(BigDecimal price1) {
		this.price1 = price1;
	}

	 
	public BigDecimal getPrice2() {
		return price2;
	}

	 
	public void setPrice2(BigDecimal price2) {
		this.price2 = price2;
	}

	 
	public BigDecimal getPrice3() {
		return price3;
	}

	 
	public void setPrice3(BigDecimal price3) {
		this.price3 = price3;
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
}