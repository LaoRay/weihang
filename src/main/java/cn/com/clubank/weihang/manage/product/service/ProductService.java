package cn.com.clubank.weihang.manage.product.service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;

/**
 * 产品管理
 * 
 * @author LeiQY
 *
 */
public interface ProductService {

	/**
	 * 新增商品
	 * 
	 * @param json
	 * @return
	 */
	CommonResult addNewProduct(JSONObject json);

	/**
	 * 编辑商品
	 * 
	 * @param productInfo
	 * @return
	 */
	CommonResult modifyProduct(ProductInfo productInfo);

	/**
	 * 按照产品名称模糊查询产品列表--app
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @param productName
	 * @return
	 */
	CommonResult productFindListByProductName(Integer pageIndex, Integer pageSize, String productName);

	/**
	 * 按照产品名称模糊查询产品列表--pc
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @param productName
	 * @return
	 */
	CommonResult websiteFindListByProductName(Integer pageIndex, Integer pageSize, String productName);

	/**
	 * 后台：批量新增sku
	 * 
	 * @param json
	 * @return
	 */
	CommonResult addOrUpdateProductSkuInfo(JSONObject json);

	/**
	 * 后台：批量新增sku
	 * 
	 * @param json
	 * @return
	 */
	CommonResult addProductSkuInfo(JSONObject json);

	/**
	 * 查询属性列表及sku列表
	 * 
	 * @param categoryId
	 * @param productId
	 * @return
	 */
	CommonResult clientFindAttrAndSkuInfoList(String categoryId, String productId);

	/**
	 * 根据skuId查询sku信息
	 * 
	 * @param skuId
	 * @return
	 */
	CommonResult clientFindSkuInfoBySkuId(String skuId);

	/**
	 * 更新sku信息
	 * 
	 * @param json
	 * @return
	 */
	CommonResult clientUpdateSkuInfoBySkuId(JSONObject json);

	/**
	 * 更新sku价格和数量
	 * 
	 * @param json
	 * @return
	 */
	CommonResult clientUpdateSkuPriceQuantity(JSONObject json);

	/**
	 * 按照类别key查询产品列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param categoryKey
	 * @return
	 */
	CommonResult productFindListByCategoryKey(Integer pageIndex, Integer pageSize, String categoryKey);
}
