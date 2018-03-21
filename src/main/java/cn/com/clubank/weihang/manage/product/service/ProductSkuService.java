package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;

/**
 * 产品sku管理
 * 
 * @author LeiQY
 *
 */
public interface ProductSkuService {

	/**
	 * 增加产品sku
	 * 
	 * @param productSku
	 * @return
	 */
	CommonResult addProductSku(ProductSku productSku);

	/**
	 * 根据skuId查询产品sku
	 * 
	 * @param skuId
	 * @return
	 */
	CommonResult FindProductSkuBySkuId(String skuId);

	/**
	 * 根据产品id查询产品sku列表
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult findProductSkuListByProductId(String productId);

	/**
	 * 根据skuId删除sku
	 * 
	 * @param skuId
	 * @return
	 */
	CommonResult deleteSkuBySkuId(String skuId);
}
