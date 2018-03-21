package cn.com.clubank.weihang.manage.dataItem.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 数据字典明细表
 * @author Liangwl
 *
 */
public class BaseDataitemDetail {
    /**
     * @mbggenerated
     * 数据字典明细表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String bdId;

    /**
     * @mbggenerated
     * 分类主键
     */
    private String dataId;

    /**
     * @mbggenerated
     * 父级主键
     */
    private String parentId;

    /**
     * @mbggenerated
     * 名称
     */
    private String name;

    /**
     * @mbggenerated
     * 值
     */
    private String value;

    /**
     * @mbggenerated
     * 是否默认
     */
    private Integer isDefault;

    /**
     * @mbggenerated
     * 排序码
     */
    private Integer sort;

    /**
     * @mbggenerated
     * 删除标记
     */
    private Integer deleteMark;

    /**
     * @mbggenerated
     * 有效标志
     */
    private Integer enabledMark;

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

  
    public String getBdId() {
        return bdId;
    }

    
    public void setBdId(String bdId) {
        this.bdId = bdId == null ? null : bdId.trim();
    }

    
    public String getDataId() {
        return dataId;
    }

    
    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }

    
    public String getParentId() {
        return parentId;
    }

    
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    
    public String getValue() {
        return value;
    }

    
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    
    public Integer getIsDefault() {
        return isDefault;
    }

    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
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