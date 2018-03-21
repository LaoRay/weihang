package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductFavouriteMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductFavourite;
import cn.com.clubank.weihang.manage.product.service.ProductFavouriteService;

/**
 * 商品收藏管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductFavouriteServiceImpl implements ProductFavouriteService {

	@Autowired
	private ProductFavouriteMapper favouriteMapper;

	/**
	 * 收藏商品
	 */
	@Override
	public CommonResult collectionProduct(ProductFavourite productFavourite) {
		if (StringUtils.isBlank(productFavourite.getCustomerId())) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "您尚未登录");
		}
		ProductFavourite favourite = favouriteMapper.selectFavouriteByCustomerIdAndProductId(
				productFavourite.getCustomerId(), productFavourite.getProductId());
		if (favourite != null) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "您已收藏，不能重复收藏");
		}
		favouriteMapper.insert(productFavourite);
		return CommonResult.result(ResultCode.SUCC.getValue(), "商品收藏成功");
	}

	/**
	 * 查询收藏列表top
	 */
	@Override
	public CommonResult findCollectionListByCustomerId(String customerId, Integer pageIndex, Integer pageSize) {
		List<Map<String, String>> favouriteList = favouriteMapper.selectListByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询收藏列表成功", favouriteList);
	}

	/**
	 * 查询商品收藏数量
	 */
	@Override
	public CommonResult findCollectionListSizeByCustomerId(String customerId) {
		int size = favouriteMapper.selectFavouriteListSizeByCustomerId(customerId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询收藏列表成功", size);
	}

	/**
	 * 取消商品收藏
	 */
	@Override
	public CommonResult cancelCollectionProduct(String customerId, String productId) {
		favouriteMapper.deleteByCustomerIdAndProduct(customerId, productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "取消商品收藏成功");
	}
}
