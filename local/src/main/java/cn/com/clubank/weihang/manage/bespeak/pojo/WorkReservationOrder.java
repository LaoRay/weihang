package cn.com.clubank.weihang.manage.bespeak.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import cn.com.clubank.weihang.common.util.Constants;

/**
 * 预约单表
 * @author Liangwl
 *
 */
public class WorkReservationOrder {
	
	/**
	 * 未确认
	 */
	public static final int STATUS_CONFIRM_NO = 1;
	
	/**
	 * 已确认
	 */
	public static final int STATUS_CONFIRM_YES = 2;
	
	/**
	 * 已取消
	 */
	public static final int STATUS_CANCEL = 3;
	
	/**
	 * 已完成
	 */
	public static final int STATUS_COMPLETE = 4;
	
	/**
	 * 已过期
	 */
	public static final int STATUS_CONFIRM_TIMEOUT = 5;
	
	/**
	 * 汽车清洗
	 */
	public static final int TYPE_WASH = 1;
	
	/**
	 * 汽车维修
	 */
	public static final int TYPE_REPAIR = 2;
	
	/**
	 * 汽车保养
	 */
	public static final int TYPE_MAINT = 3;
	
	/**
	 * 美容装潢
	 */
	public static final int TYPE_BEAUTY = 4;

	/**
	 * @mbggenerated
	 * 预约单主键
	 */
	
	@WeihColumn(WeihColumnCode.UUID)
	private String roId;
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
	 * 预约点
	 */
	private String reservationShop;
	/**
	 * @mbggenerated
	 * 预约类型 1、汽车清洗2、汽车维修3、汽车保养4、美容装潢
	 */
	private Integer reservationType;
	/**
	 * @mbggenerated
	 * 预约时间
	 */
	private String reservationDate;
	/**
	 * @mbggenerated
	 * 到店方式
	 */
	private Integer fromType;
	/**
	 * @mbggenerated
	 * 预约状态 1、未确认 2、已确认 3、已取消 4、已完成 5、已过期
	 */
	private Integer reservationStatus = 2;
	/**
	 * @mbggenerated
	 * 跟进客服
	 */
	private String followServiceBy;
	/**
	 * @mbggenerated
	 * 有效标志
	 */
	private Integer enabledMark;
	/**
	 * @mbggenerated
	 * 联系人
	 */
	private String contacts;
	/**
	 * @mbggenerated
	 * 联系人手机
	 */
	private String contactsMobile;
	/**
	 * @mbggenerated
	 * 备注
	 */
	private String description;
	/**
	 * @mbggenerated
	 * 创建用户
	 */
	private String createUserName;
	/**
	 * @mbggenerated
	 * 创建用户主键
	 */
	private String createUserId;
	/**
	 * @mbggenerated
	 * 创建日期
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;
	/**
	 * @mbggenerated
	 * 修改用户
	 */
	private String modifyUserName;
	/**
	 * @mbggenerated
	 * 修改用户主键
	 */
	private String modifyUserId;
	/**
	 * @mbggenerated
	 * 修改日期
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	public String getRoId() {
		return roId;
	}


	public void setRoId(String roId) {
		this.roId = roId == null ? null : roId.trim();
	}


	public String getReservationOrderNo() {
		return reservationOrderNo;
	}

	
	public void setReservationOrderNo(String reservationOrderNo) {
		this.reservationOrderNo = reservationOrderNo == null ? null : reservationOrderNo.trim();
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

	
	public String getReservationShop() {
		return reservationShop;
	}

	
	public void setReservationShop(String reservationShop) {
		this.reservationShop = reservationShop == null ? null : reservationShop.trim();
	}

	
	public Integer getReservationType() {
		return reservationType;
	}

	
	public void setReservationType(Integer reservationType) {
		this.reservationType = reservationType;
	}

	
	public String getReservationDate() {
		return reservationDate;
	}

	
	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate == null ? null : reservationDate.trim();
	}

	
	public Integer getFromType() {
		return fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}

	
	public Integer getReservationStatus() {
		return reservationStatus;
	}

	
	public void setReservationStatus(Integer reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	
	public String getFollowServiceBy() {
		return followServiceBy;
	}

	
	public void setFollowServiceBy(String followServiceBy) {
		this.followServiceBy = followServiceBy == null ? null : followServiceBy.trim();
	}

	
	public Integer getEnabledMark() {
		return enabledMark;
	}

	
	public void setEnabledMark(Integer enabledMark) {
		this.enabledMark = enabledMark;
	}

	
	public String getContacts() {
		return contacts;
	}

	
	public void setContacts(String contacts) {
		this.contacts = contacts == null ? null : contacts.trim();
	}

	
	public String getContactsMobile() {
		return contactsMobile;
	}

	
	public void setContactsMobile(String contactsMobile) {
		this.contactsMobile = contactsMobile == null ? null : contactsMobile.trim();
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	
	public String getCreateUserName() {
		return createUserName;
	}

	
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName == null ? null : createUserName.trim();
	}

	
	public String getCreateUserId() {
		return createUserId;
	}

	
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public String getModifyUserName() {
		return modifyUserName;
	}

	
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName == null ? null : modifyUserName.trim();
	}

	
	public String getModifyUserId() {
		return modifyUserId;
	}

	
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId == null ? null : modifyUserId.trim();
	}

	
	public Date getModifyDate() {
		return modifyDate;
	}

	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	public String getReservationStatusStr() {
		if (reservationStatus == STATUS_CANCEL) {
			return "已取消";
		} else if (reservationStatus == STATUS_COMPLETE) {
			return "已完成";
		} else if (reservationStatus == STATUS_CONFIRM_NO) {
			return "未确认";
		} else if (reservationStatus == STATUS_CONFIRM_YES) {
			return "已确认";
		} else if (reservationStatus == STATUS_CONFIRM_TIMEOUT) {
			return "已过期";
		}
		return "未知状态";
	}
	
	public String getReservationTypeStr() {
		if (reservationType == TYPE_WASH) {
			return "汽车清洗";
		} else if (reservationType == TYPE_REPAIR) {
			return "汽车维修";
		} else if (reservationType == TYPE_MAINT) {
			return "汽车保养";
		} else if (reservationType == TYPE_BEAUTY) {
			return "美容装潢";
		}
		return Constants.NULL;
	}
}