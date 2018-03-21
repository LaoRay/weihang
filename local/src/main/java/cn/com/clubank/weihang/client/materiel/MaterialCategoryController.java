package cn.com.clubank.weihang.client.materiel;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialCategory;
import cn.com.clubank.weihang.manage.material.service.IMaterialCategoryService;

/**
 * 后台：物料分类管理
 * @author Liangwl
 *
 */
@Controller
public class MaterialCategoryController {

	@Resource
	private IMaterialCategoryService iMaterialCategoryService;
	
	/**
	 * 后台：根据物料分类名称查询（模糊查询）物料分类表并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientMaterialCategoryFindPage", method = RequestMethod.POST)
	@ResponseBody
	public String productCategoryFindPage(@RequestBody JSONObject param){
		
		return iMaterialCategoryService.selectByCategoryNamePage(param.getString("categoryName"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：通过物料分类ID获取分类信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientGetMaterialCategoryInfo", method = RequestMethod.POST)
	@ResponseBody
	public String clientGetMaterialInfo(@RequestBody JSONObject param){
		
		return iMaterialCategoryService.selectByWicId(param.getString("wicId"));
	}
	
	/**
	 * 后台：保存物料分类信息
	 * @param wmc
	 * @return
	 */
	@RequestMapping(value = "/clientSaveMaterialCategoryInfo", method = RequestMethod.POST)
	@ResponseBody
	public String clientAddOrEdit(@RequestBody WorkMaterialCategory wmc){
		
		return iMaterialCategoryService.insertOrUpdate(wmc);
	}
	
	/**
	 * 后台：获得分类ID与分类名称
	 * @return
	 */
	@RequestMapping(value = "/clientGetMaterialCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public String clientGetMaterialCategoryId(){
		
		return iMaterialCategoryService.selectMaterialCategoryId();
	}
	
	/**
	 * 后台：删除物料分类信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteMaterialCategoryInfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMaterialCategoryInfo(@RequestBody JSONObject param){
		
		return iMaterialCategoryService.deleteMaterialCategoryInfo(param.getString("wicId"));
	}
}
