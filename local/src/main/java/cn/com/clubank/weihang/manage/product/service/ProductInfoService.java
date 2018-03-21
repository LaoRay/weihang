package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;

/**
 * 产品信息管理
 * 
 * @author LeiQY
 *
 */
public interface ProductInfoService {

	/**
	 * 增加产品信息
	 * 
	 * @param productInfo
	 * @return
	 */
	CommonResult addProductInfo(ProductInfo productInfo);

	/**
	 * 根据产品id查询产品信息
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult findProductInfoByProductId(String productId);

	/**
	 * 根据产品id查询产品详情
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult findProductDetailsByProductId(String productId);

	/**
	 * 产品审核
	 * 
	 * @param productId
	 * @param reviewStatus
	 * @return
	 */
	CommonResult reviewProductByProductId(String productId, Integer reviewStatus);

	/**
	 * 根据分类id查询更多商品列表
	 * 
	 * @param categoryId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult findMoreProductListByCategoryId(String categoryId, Integer pageIndex, Integer pageSize);

	/**
	 * 根据产品id查询产品详细信息(包括产品信息，产品sku列表，产品基本属性信息，图片信息，收藏信息)
	 * 
	 * @param customerId
	 * @param productId
	 * @return
	 */
	CommonResult findDetailedInfoByProductId(String customerId, String productId);

	/**
	 * 根据产品skuId查询产品详细信息(包括产品信息，产品sku，图片信息)
	 * 
	 * @param customerId
	 * @param skuId
	 * @return
	 */
	CommonResult findDetailedInfoBySkuId(String customerId, String skuId);

	/**
	 * 后台：分页查询产品列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param productName
	 * @param productType
	 * @param categoryId
	 * @param productStatus
	 * @return
	 */
	CommonResult clientFindProductList(Integer pageIndex, Integer pageSize, String productName, Integer productType,
			String categoryId, Integer reviewStatus);

	/**
	 * 后台：商品上架下架
	 * 
	 * @param productId
	 * @param productStatus
	 * @return
	 */
	CommonResult clientReviewProduct(String productId, Integer reviewStatus);

	/**
	 * 通过分类ID获得产品列表并分页
	 * 
	 * @param categoryId
	 *            分类ID
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectProductListPaged(String categoryId, Integer pageIndex, Integer pageSize);

	/**
	 * 获取商品第一张图片（默认图片）
	 * 
	 * @param productId
	 * @return
	 */
	String getOnePicPath(String productId);

	/**
	 * 根据产品id查询产品信息和sku列表信息
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult clientFindProductInfoByProductId(String productId);

	/**
	 * 根据产品id查询产品信息
	 * 
	 * @param string
	 * @return
	 */
	CommonResult clientFindProductByProductId(String productId);
}
