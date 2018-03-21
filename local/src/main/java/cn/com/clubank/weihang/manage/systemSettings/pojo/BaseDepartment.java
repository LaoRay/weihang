package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 机构部门表
 * @author Liangwl
 *
 */
public class BaseDepartment {
	/**
	 * 部门主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String dId;

    /**
     * 父级主键
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String dName;

    /**
     * 部门简称
     */
    private String shortName;

    /**
     * 负责人主键
     */
    private String managerId;

    /**
     * 负责人
     */
    private String manager;

    /**
     * 外线电话
     */
    private String outerPhone;

    /**
     * 内线电话
     */
    private String innerPhone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 部门传真
     */
    private String fax;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     * 成立时间
     */
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date foundedTime;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 省主键
     */
    private String provinceId;

    /**
     * 市主键
     */
    private String cityId;

    /**
     * 县/区主键
     */
    private String countyId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 公司主页
     */
    private String webaddress;

    /**
     * 删除标记
     */
    private Integer deleteMark;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
    private Date modifyDate;

    /**
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * 修改用户名
     */
    private String modifyUserName;

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId == null ? null : dId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName == null ? null : dName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId == null ? null : managerId.trim();
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    public String getOuterPhone() {
        return outerPhone;
    }

    public void setOuterPhone(String outerPhone) {
        this.outerPhone = outerPhone == null ? null : outerPhone.trim();
    }

    public String getInnerPhone() {
        return innerPhone;
    }

    public void setInnerPhone(String innerPhone) {
        this.innerPhone = innerPhone == null ? null : innerPhone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getFoundedTime() {
        return foundedTime;
    }

    public void setFoundedTime(Date foundedTime) {
        this.foundedTime = foundedTime;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope == null ? null : businessScope.trim();
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId == null ? null : provinceId.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId == null ? null : countyId.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getWebaddress() {
        return webaddress;
    }

    public void setWebaddress(String webaddress) {
        this.webaddress = webaddress == null ? null : webaddress.trim();
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
}