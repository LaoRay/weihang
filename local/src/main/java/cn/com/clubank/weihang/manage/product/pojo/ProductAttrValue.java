package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性值表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttrValue {

	/**
	 * 属性值表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String vId;

	/**
	 * 属性名ID
	 */
    private String attrId;

    /**
     * 属性值名
     */
    private String valueName;

    /**
     * 类别ID
     */
    private String categoryId;

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