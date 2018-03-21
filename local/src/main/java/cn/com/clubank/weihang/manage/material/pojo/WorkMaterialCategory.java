package cn.com.clubank.weihang.manage.material.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 物料分类表
 * @author Liangwl
 *
 */
public class WorkMaterialCategory {
    /**
     * @mbggenerated
     * 物料分类表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wicId;

    /**
     * @mbggenerated
     * 分类名称
     */
    private String categoryName;

    /**
     * @mbggenerated
     * 父ID
     */
    private String parentId;
    
    /**
     * 父分类名称（自定义属性）
     */
    private String parentName;

    public String getParentName() {
		return parentName;
	}


	public void setParentName(String parentName) {
		this.parentName = parentName;
	}


	/**
     * @mbggenerated
     * 编码
     */
    private String code;

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

     
    public String getWicId() {
        return wicId;
    }

     
    public void setWicId(String wicId) {
        this.wicId = wicId == null ? null : wicId.trim();
    }

     
    public String getCategoryName() {
        return categoryName;
    }

     
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

     
    public String getParentId() {
        return parentId;
    }

     
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

     
    public String getCode() {
        return code;
    }

    
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
}