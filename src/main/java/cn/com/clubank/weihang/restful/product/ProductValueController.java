package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductValue;
import cn.com.clubank.weihang.manage.product.service.ProductValueService;

/**
 * 产品基本属性管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductValueController {

	@Autowired
	private ProductValueService productValueService;

	/**
	 * 增加产品基本属性
	 * 
	 * @param productValue
	 * @return
	 */
	@RequestMapping(value = "/productAddValue", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addProductValue(@RequestBody ProductValue productValue) {
		return productValueService.addProductValue(productValue);
	}

	/**
	 * 根据产品id查询产品基本属性列表
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productFindValueListByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findProductValueListByProductId(@RequestBody JSONObject json) {
		return productValueService.findProductValueListByProductId(json.getString("productId"));
	}
}
