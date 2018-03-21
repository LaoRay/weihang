package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductFavourite;

/**
 * 商品收藏管理
 * 
 * @author LeiQY
 *
 */
public interface ProductFavouriteService {

	/**
	 * 收藏商品
	 * 
	 * @param productFavourite
	 * @return
	 */
	CommonResult collectionProduct(ProductFavourite productFavourite);

	/**
	 * 查询收藏列表top
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult findCollectionListByCustomerId(String customerId, Integer pageIndex, Integer pageSize);

	/**
	 * 查询收藏商品数量
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult findCollectionListSizeByCustomerId(String customerId);

	/**
	 * 取消收藏商品
	 * 
	 * @param customerId
	 * @param productId
	 * @return
	 */
	CommonResult cancelCollectionProduct(String customerId, String productId);
}
