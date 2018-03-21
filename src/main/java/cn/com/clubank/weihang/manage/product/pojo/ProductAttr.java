package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttr {

	/**
	 * 属性ID
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String attrId;

	/**
	 * 属性名
	 */
    private String attrName;

    /**
     * 类别ID
     */
    private String categoryId;

    /**
     * 属性状态
     */
    private Integer status;

    /**
     * 是否搜索字段
     */
    private Integer isSearch = 2;

    /**
     * 是否销售属性
     */
    private Integer isSellAttr = 2;

    /**
     * 是否关键属性
     */
    private Integer isMainAttr = 2;

    /**
     * 是否一般属性
     */
    private Integer isCommonAttr = 2;
    
    /**
     * 是否颜色属性
     */
    private Integer isColor = 2;

    /**
     * 是否多选
     */
    private Integer isChx = 2;
    
    /**
     * 是否枚举
     */
    private Integer isEnum = 2;

    /**
     * 是否必选
     */
    private Integer isRequired = 2;

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
}