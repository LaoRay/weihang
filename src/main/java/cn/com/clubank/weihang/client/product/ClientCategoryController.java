package cn.com.clubank.weihang.client.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.product.pojo.ProductCategory;
import cn.com.clubank.weihang.manage.product.service.IProductCategoryService;

/**
 * 后台：分类管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientCategoryController {

	@Autowired
	private IProductCategoryService productCategoryService;

	/**
	 * 分页查询产品类別列表（条件）
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientProductCategoryFindPage", method = RequestMethod.POST)
	@ResponseBody
	public String productCategoryFindPage(@RequestBody JSONObject param) {
		return productCategoryService.findPage(param.getString("cname"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 新增分类
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientSaveSortInfo", method = RequestMethod.POST)
	@ResponseBody
	public String saveSortInfo(@RequestBody ProductCategory record) {
		return productCategoryService.insertSortInfo(record);
	}

	/**
	 * 删除分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteSortInfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteSortInfo(@RequestBody JSONObject param) {
		return productCategoryService.deleteSortInfo(param.getString("categoryId"));
	}

	/**
	 * 根据categoryId查询分类信息
	 * 
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/clientFindSortInfoByCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public String findSortInfoByCategoryId(@RequestBody JSONObject param) {
		return productCategoryService.findSortInfoByCategoryId(param.getString("categoryId"));
	}

	/**
	 * 编辑分类
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientProductModifySortInfo", method = RequestMethod.POST)
	@ResponseBody
	public String modifySortInfo(@RequestBody ProductCategory category) {
		return productCategoryService.modifySortInfo(category);
	}

	/**
	 * 获取分类树结构数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientProductCategoryTreeData", method = RequestMethod.POST)
	@ResponseBody
	public String clientProductCategoryTreeData() {
		return productCategoryService.clientProductCategoryTreeData();
	}
	
	/**
	 * 获取分类树结构数据
	 * 
	 * @param type type 0-所有，1-服务类，2-实体类
	 * @return
	 */
	@RequestMapping(value = "/clientProductCategoryTreeDataByType", method = RequestMethod.POST)
	@ResponseBody
	public String clientProductCategoryTreeData(@RequestBody JSONObject param) {
		return productCategoryService.clientProductCategoryTreeDataByType(param.getInteger("type"));
	}
}
