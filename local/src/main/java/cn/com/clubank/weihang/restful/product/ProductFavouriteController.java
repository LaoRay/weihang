package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductFavourite;
import cn.com.clubank.weihang.manage.product.service.ProductFavouriteService;

/**
 * 商品收藏管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductFavouriteController {

	@Autowired
	private ProductFavouriteService productFavouriteService;

	/**
	 * 收藏商品
	 * 
	 * @param productFavourite
	 * @return
	 */
	@RequestMapping(value = "/productCollectionProduct", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult collectionProduct(@RequestBody ProductFavourite productFavourite) {
		return productFavouriteService.collectionProduct(productFavourite);
	}

	/**
	 * 获取收藏列表
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/productFindCollectionListByCustomerId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findCollectionListByCustomerId(@RequestBody JSONObject json) {
		return productFavouriteService.findCollectionListByCustomerId(json.getString("customerId"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}

	/**
	 * 获取收藏商品数量
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/productFindCollectionListSizeByCustomerId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findCollectionListSizeByCustomerId(@RequestBody JSONObject json) {
		return productFavouriteService.findCollectionListSizeByCustomerId(json.getString("customerId"));
	}

	/**
	 * 取消收藏商品
	 * 
	 * @param customerId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productCancelCollection", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult cancelCollectionProduct(@RequestBody JSONObject json) {
		return productFavouriteService.cancelCollectionProduct(json.getString("customerId"),
				json.getString("productId"));
	}
}
