package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.ProductService;

/**
 * 产品管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 首页搜索--按照产品名称模糊查询产品列表--app
	 * 
	 * @param productName
	 * @return
	 */
	@RequestMapping(value = "/productFindListByProductName", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult productFindListByProductName(@RequestBody JSONObject json) {
		return productService.productFindListByProductName(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getString("productName"));
	}

	/**
	 * 按照类别key查询产品列表
	 * 
	 * @param categoryKey
	 * @return
	 */
	@RequestMapping(value = "/productFindListByCategoryKey", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult productFindListByCategoryKey(@RequestBody JSONObject json) {
		return productService.productFindListByCategoryKey(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getString("categoryKey"));
	}
}
