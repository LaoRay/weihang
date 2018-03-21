package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.ProductSkuService;

/**
 * 产品sku管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductSkuController {
	
	@Autowired
	private ProductSkuService productSkuService;
	
	/**
	 * 增加产品sku
	 * 
	 * @param productSku
	 * @return
	 */
	@RequestMapping(value = "/productAddSku", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addProductSku(@RequestBody ProductSku productSku) {
		return productSkuService.addProductSku(productSku);
	}
	
	/**
	 * 根据skuId查询产品sku
	 * 
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/productFindSkuBySkuId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult FindProductSkuBySkuId(@RequestBody JSONObject json) {
		return productSkuService.FindProductSkuBySkuId(json.getString("skuId"));
	}
	
}
