package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.ProductBrandService;

/**
 * 产品品牌
 * 
 * @author YangWei
 *
 */
@Controller
public class ProductBrandController {

	@Autowired
	private ProductBrandService productBrandService;

	/**
	 * 根据品牌ids批量删除品牌
	 * 
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/productBrandDelete", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult productBrandDelete(@RequestBody JSONObject json) {
		return productBrandService.delete(json.getString("brandIds"));
	}

	/**
	 * 查询所有品牌
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productBrandFindAll", method = RequestMethod.POST)
	@ResponseBody
	public String productBrandFindAll() {
		return productBrandService.findAll();
	}
}
