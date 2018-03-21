package cn.com.clubank.weihang.client.product;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;
import cn.com.clubank.weihang.manage.product.service.ProductService;
import cn.com.clubank.weihang.manage.product.service.ProductSkuService;

/**
 * 后台：商品管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientProductController {

	@Autowired
	private ProductInfoService productInfoService;
	@Resource
	private ProductSkuService productSkuService;
	@Autowired
	private ProductService productService;

	/**
	 * 分页查询产品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductList(@RequestBody JSONObject json) {
		return productInfoService.clientFindProductList(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getString("productName"), json.getInteger("productType"), json.getString("categoryId"),
				json.getInteger("reviewStatus"));
	}

	/**
	 * 新增产品信息
	 * 
	 * @param productInfo
	 * @return
	 */
	@RequestMapping(value = "/clientProductAddInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addProductInfo(@RequestBody ProductInfo productInfo) {
		return productInfoService.addProductInfo(productInfo);
	}

	/**
	 * 根据产品id查询产品信息
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductByProductId(@RequestBody JSONObject json) {
		return productInfoService.clientFindProductByProductId(json.getString("productId"));
	}

	/**
	 * 编辑商品
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientProductModifyProductInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyProduct(@RequestBody ProductInfo productInfo) {
		return productService.modifyProduct(productInfo);
	}

	/**
	 * 商品上架下架
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientReviewProduct", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientReviewProduct(@RequestBody JSONObject json) {
		return productInfoService.clientReviewProduct(json.getString("productId"), json.getInteger("reviewStatus"));
	}

	/**
	 * 后台：通过分类ID获得产品列表并分页
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductListPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findProductListPaged(@RequestBody JSONObject param) {
		return productInfoService.selectProductListPaged(param.getString("categoryId"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 根据产品id查询产品sku列表
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/clientFindSkuListByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findProductSkuListByProductId(@RequestBody JSONObject json) {
		return productSkuService.findProductSkuListByProductId(json.getString("productId"));
	}

	/**
	 * 根据产品id查询产品信息和sku列表信息
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductInfoByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductInfoByProductId(@RequestBody JSONObject json) {
		return productInfoService.clientFindProductInfoByProductId(json.getString("productId"));
	}

	/**
	 * 新增或修改商品sku
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientModifyProductSkuInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addProductSkuInfo(@RequestBody JSONObject json) {
		return productService.addOrUpdateProductSkuInfo(json);
	}

	/**
	 * 新增sku页面-选中规格和属性值之后提交，解析选择的规格属性值保存sku
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientAddProductSkuInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientAddProductSkuInfo(@RequestBody JSONObject json) {
		return productService.addProductSkuInfo(json);
	}

	/**
	 * 新增sku页面：查询属性列表及sku列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientFindAttrAndSkuInfoList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindAttrAndSkuInfoList(@RequestBody JSONObject json) {
		return productService.clientFindAttrAndSkuInfoList(json.getString("categoryId"), json.getString("productId"));
	}

	/**
	 * 根据skuId查询sku信息
	 * 
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/clientFindSkuInfoBySkuId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindSkuInfoBySkuId(@RequestBody JSONObject json) {
		return productService.clientFindSkuInfoBySkuId(json.getString("skuId"));
	}

	/**
	 * 更新sku信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientUpdateSkuInfoBySkuId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientUpdateSkuInfoBySkuId(@RequestBody JSONObject json) {
		return productService.clientUpdateSkuInfoBySkuId(json);
	}

	/**
	 * 更新sku的价格和数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientUpdateSkuPriceQuantity", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientUpdateSkuPriceQuantity(@RequestBody JSONObject json) {
		return productService.clientUpdateSkuPriceQuantity(json);
	}
	
	/**
	 * 根据产品skuId删除sku
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productDeleteSkuBySkuId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteSkuBySkuId(@RequestBody JSONObject json) {
		return productSkuService.deleteSkuBySkuId(json.getString("skuId"));
	}
}
