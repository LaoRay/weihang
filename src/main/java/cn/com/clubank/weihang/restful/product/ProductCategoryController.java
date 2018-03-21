package cn.com.clubank.weihang.restful.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.IProductCategoryService;

@Controller
public class ProductCategoryController {

	@Resource
	private IProductCategoryService iProductCategoryService;

	/**
	 * 获取所有根分类列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productGetAllRootCategoryList", method = RequestMethod.POST)
	@ResponseBody
	public String getAllRootCategory() {

		return iProductCategoryService.selectAllRootCategory();
	}

	/**
	 * 获取所有子分类(叶子节点)列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productGetAllLeafCategoryList", method = RequestMethod.POST)
	@ResponseBody
	public String getAllLeafCategory() {
		return iProductCategoryService.selectAllLeafCategory();
	}

	/**
	 * 获取子分类列表（根据父分类ID）
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/productGetSubclassificationList", method = RequestMethod.POST)
	@ResponseBody
	public String getSubclassification(@RequestBody JSONObject param) {
		return iProductCategoryService.selectSubclassification(param.getString("parentId"));
	}

	/**
	 * 根据父分类ID获取子分类列表及其商品信息
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/productGetSubclassificationListAndProductList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getSubclassificationListAndProductList(@RequestBody JSONObject param) {

		return iProductCategoryService.selectSubclassificationListAndProductList(param.getString("parentId"));
	}
}
