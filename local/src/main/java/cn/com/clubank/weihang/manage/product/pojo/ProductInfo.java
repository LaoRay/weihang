package cn.com.clubank.weihang.manage.product.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

	/**
	 * 产品ID
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String productId;

	/**
	 * 产品名
	 */
	private String productName;

	/**
	 * 简介
	 */
	private String productExplain;

	/**
	 * 品牌ID
	 */
	private String brandId;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 状态 1待审核 2上架 3下架
	 */
	private Integer reviewStatus = 1;

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

	/**
	 * 产品价格
	 */
	private BigDecimal productPrice;

	/**
	 * 上架时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date productUpTime;

	/**
	 * 下架时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date productDownTime;

	/**
	 * 类别ID
	 */
	private String categoryId;

	/**
	 * 详情
	 */
	private String productDetails;
	
	/**
	 * 商品类型 1实体2服务
	 */
	private Integer productType;
}