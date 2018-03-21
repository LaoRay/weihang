package cn.com.clubank.weihang.manage.material.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物料分类表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    private List<WorkMaterialCategory> categorys = new ArrayList<WorkMaterialCategory>();
}