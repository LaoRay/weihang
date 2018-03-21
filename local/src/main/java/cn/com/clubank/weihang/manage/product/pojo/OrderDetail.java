package cn.com.clubank.weihang.manage.product.pojo;

import java.math.BigDecimal;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String odiId;

	/**
	 * 订单id
	 */
	private String orderId;

	/**
	 * sku id
	 */
	private String skuId;

	/**
	 * 数量
	 */
	private Integer quantity;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 是否评论
	 */
	private Boolean isComment = false;

	/**
	 * 是否已申请售后服务
	 */
	private Boolean isSaleService = false;

	/**
	 * 商品id
	 */
	private String productId;
}