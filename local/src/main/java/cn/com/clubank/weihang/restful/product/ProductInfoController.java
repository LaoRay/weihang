package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;

/**
 * 产品信息管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductInfoController {

	@Autowired
	private ProductInfoService productInfoService;

	/**
	 * 根据产品id查询产品信息
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productFindInfoByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findProductInfoByProductId(@RequestBody JSONObject json) {
		return productInfoService.findProductInfoByProductId(json.getString("productId"));
	}

	/**
	 * 根据产品id查询产品详细信息(包括产品信息，产品sku列表，产品基本属性信息，图片信息，收藏信息)
	 * 
	 * @param customerId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productFindDetailedInfoByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findDetailedInfoByProductId(@RequestBody JSONObject json) {
		return productInfoService.findDetailedInfoByProductId(json.getString("customerId"),
				json.getString("productId"));
	}

	/**
	 * 根据产品skuId查询产品详细信息(包括产品信息，产品sku，图片信息)
	 * 
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/productFindDetailedInfoBySkuId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findDetailedInfoBySkuId(@RequestBody JSONObject json) {
		return productInfoService.findDetailedInfoBySkuId(json.getString("customerId"), json.getString("skuId"));
	}

	/**
	 * 根据产品id查询产品详情
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productFindDetailsByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findProductDetailsByProductId(@RequestBody JSONObject json) {
		return productInfoService.findProductDetailsByProductId(json.getString("productId"));
	}

	/**
	 * 产品审核
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/productReviewByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult reviewProductByProductId(@RequestBody JSONObject json) {
		return productInfoService.reviewProductByProductId(json.getString("productId"),
				json.getInteger("reviewStatus"));
	}

	/**
	 * 根据分类id查询更多商品列表
	 * 
	 * @param categoryId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/productFindMoreProductListByCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findMoreProductListByCategoryId(@RequestBody JSONObject json) {
		return productInfoService.findMoreProductListByCategoryId(json.getString("categoryId"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}
}
