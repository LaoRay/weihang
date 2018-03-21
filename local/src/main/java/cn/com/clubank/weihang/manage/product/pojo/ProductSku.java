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
 * 产品SKU表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSku {

	/**
	 * SKUID
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String skuId;

	/**
	 * SKU状态 1:有效,2:无效
	 */
	private Integer skuStatus = 1;

	/**
	 * SKU名称
	 */
	private String skuName;

	/**
	 * 属性值集合
	 */
	private String attributes;

	/**
	 * 属性值id集合
	 */
	private String attributesId;

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 图片ID
	 */
	private String productPicId;

	/**
	 * 数量
	 */
	private Integer productQuantity = 0;

	/**
	 * 销售数量
	 */
	private Integer productSaleQuantity = 0;

	/**
	 * 成本价
	 */
	private BigDecimal productPrice;

	/**
	 * 普卡价
	 */
	private BigDecimal productPrice1;

	/**
	 * 金卡价
	 */
	private BigDecimal productPrice2;

	/**
	 * 钻卡价
	 */
	private BigDecimal productPrice3;

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
	 * 获得剩余数量
	 * 
	 * @return
	 */
	public Integer getSurplusQuantity() {
		return productQuantity == null ? 0 : productQuantity - productSaleQuantity;
	}
}