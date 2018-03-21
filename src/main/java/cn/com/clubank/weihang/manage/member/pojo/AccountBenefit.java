package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 账户权益表（账户等级说明表）
 * 
 * @author YangWei
 *
 */
public class AccountBenefit {
	/**
	 * @mbggenerated 账户权益表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String abId;

	/**
	 * @mbggenerated 权益名称
	 */
	private String benefitName;

    /**
     * 等级 1普通 2白金 3白钻 4蓝金 5蓝钻
     */
    private Integer level;
    /**
     * @mbggenerated
     * 图标
     */
    private String icon;

	/**
	 * @mbggenerated 权益升级界限
	 */
	private BigDecimal upgradeTotal;

	/**
	 * @mbggenerated 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * @mbggenerated 有效标志 1有效 2无效
	 */
	private Integer enabledMark = 1;

	/**
	 * @mbggenerated 创建日期
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/**
	 * @mbggenerated 创建用户主键
	 */
	private String createUserId;

	/**
	 * @mbggenerated 创建用户名
	 */
	private String createUserName;

	/**
	 * @mbggenerated 修改日期
	 */
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	/**
	 * @mbggenerated 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * @mbggenerated 修改用户名
	 */
	private String modifyUserName;

	/**
	 * @mbggenerated 权益说明
	 */
	private String description;

	public String getAbId() {
		return abId;
	}

	public void setAbId(String abId) {
		this.abId = abId == null ? null : abId.trim();
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName == null ? null : benefitName.trim();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon == null ? null : icon.trim();
	}

	public BigDecimal getUpgradeTotal() {
		return upgradeTotal;
	}

	public void setUpgradeTotal(BigDecimal upgradeTotal) {
		this.upgradeTotal = upgradeTotal;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}