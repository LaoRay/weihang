package cn.com.clubank.weihang.client.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductBrand;
import cn.com.clubank.weihang.manage.product.service.ProductBrandService;

/**
 * 后台：商品品牌管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientProductBrandController {

	@Autowired
	private ProductBrandService productBrandService;

	/**
	 * 根据品牌名称查询品牌并分页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientProductBrandFindPage", method = RequestMethod.POST)
	@ResponseBody
	public String productBrandFindPage(@RequestBody JSONObject param) {
		return productBrandService.findPage(param.getString("brandName"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 保存、更新品牌信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientProductBrandSaveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult productBrandSaveOrUpdate(@RequestBody JSONObject json) {
		ProductBrand brand = JSONObject.toJavaObject(json, ProductBrand.class);
		return productBrandService.saveOrUpdate(brand, json.getString("categoryIds"));
	}

	/**
	 * 根据品牌id查询品牌信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductBrandByBrandId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductBrandByBrandId(@RequestBody JSONObject json) {
		return productBrandService.clientFindProductBrandByBrandId(json.getString("brandId"));
	}

	/**
	 * 根据品牌id删除品牌
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteProductBrand", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteProductBrand(@RequestBody JSONObject json) {
		return productBrandService.clientDeleteProductBrand(json.getString("brandId"));
	}

	/**
	 * 根据类别id查询品牌
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductBrandByCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductBrandByCategoryId(@RequestBody JSONObject json) {
		return productBrandService.clientFindProductBrandByCategoryId(json.getString("categoryId"));
	}
}
