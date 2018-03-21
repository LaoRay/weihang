package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品图片表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPic {

	/**
	 * 产品图片ID
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String productPicId;

	/**
	 * 属性ID
	 */
	private String productAttrId;
	
	/**
	 * 属性值ID
	 */
	private String productAttrValueId;

	/**
	 * 大图
	 */
	private String picBig;

	/**
	 * 小图
	 */
	private String picSmall;

	/**
	 * 产品ID
	 */
	private String productId;

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
	 * 创建日期
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	/**
	 * 创建用户主键
	 */
	private String modifyUserId;

	/**
	 * 创建用户名
	 */
	private String modifyUserName;
}