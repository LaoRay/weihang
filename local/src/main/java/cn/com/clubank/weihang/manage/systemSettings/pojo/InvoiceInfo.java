package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 发票信息表
 * 
 * @author YangWei
 *
 */
public class InvoiceInfo {
	
	/**
	 * 未开票
	 */
	public static final int STATUS_NO = 0;
	
	/**
	 * 已开票
	 */
	public static final int STATUS_YES = 1;

	/**
	 * 发票主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String invoiceId;

	/**
	 * 单位名称
	 */
	private String companyAme;

	/**
	 * 纳税人识别号
	 */
	private String identificationCode;

	/**
	 * 注册地址
	 */
	private String registerAddress;

	/**
	 * 注册电话
	 */
	private String registerTelephone;

	/**
	 * 开户银行
	 */
	private String openingTank;
	
	/**
	 * 状态  0-未开票，1-已开票
	 */
	private Integer status = 0;

	/**
	 * 银行账户
	 */
	private String bankId;

	/**
	 * 账单号
	 */
	private String orderNo;

	/**
	 * 开票金额
	 */
	private BigDecimal openingMoney;

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

	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	private String createUserId;

	private String createUserName;

	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	private String modifyUserId;

	private String modifyUserName;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId == null ? null : invoiceId.trim();
	}

	public String getCompanyAme() {
		return companyAme;
	}

	public void setCompanyAme(String companyAme) {
		this.companyAme = companyAme == null ? null : companyAme.trim();
	}

	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode == null ? null : identificationCode.trim();
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress == null ? null : registerAddress.trim();
	}

	public String getRegisterTelephone() {
		return registerTelephone;
	}

	public void setRegisterTelephone(String registerTelephone) {
		this.registerTelephone = registerTelephone == null ? null : registerTelephone.trim();
	}

	public String getOpeningTank() {
		return openingTank;
	}

	public void setOpeningTank(String openingTank) {
		this.openingTank = openingTank == null ? null : openingTank.trim();
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId == null ? null : bankId.trim();
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public BigDecimal getOpeningMoney() {
		return openingMoney;
	}

	public void setOpeningMoney(BigDecimal openingMoney) {
		this.openingMoney = openingMoney;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}