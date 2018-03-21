package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品基本属性表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductValue {

	/**
	 * 产品基本属性表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String pvId;

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 属性名ID
	 */
	private String productAttrId;
	
	/**
	 * 属性名
	 */
	private String productAttr;

	/**
	 * 属性值ID
	 */
	private String productAttrValueId;
	
	/**
	 * 属性值
	 */
	private String productAttrValue;

	/**
	 * SKUID
	 */
	private String skuId;

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