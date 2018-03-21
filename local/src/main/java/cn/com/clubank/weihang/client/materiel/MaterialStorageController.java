package cn.com.clubank.weihang.client.materiel;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;
import cn.com.clubank.weihang.manage.material.service.IMaterialStorageService;

/**
 * 后台：物料管理
 * @author Liangwl
 *
 */
@Controller
public class MaterialStorageController {
	
	@Resource
	private IMaterialStorageService iMaterialStorageService;

	/**
	 * 后台：通过物料ID获取材料信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientGetMaterialInfo", method = RequestMethod.POST)
	@ResponseBody
	public String clientGetMaterialInfo(@RequestBody JSONObject param){
		
		return iMaterialStorageService.selectByWiId(param.getString("wiId"));
	}
	
	/**
	 * 后台：根据物料名查询（模糊查询）物料列表并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindMaterialListAndPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findMaterialListAndPaged(@RequestBody JSONObject param){
		
		return iMaterialStorageService.selectByItemNamePage(param.getString("itemName"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：保存物料信息
	 * @param wms
	 * @return
	 */
	@RequestMapping(value = "/clientSaveMaterialInfo", method = RequestMethod.POST)
	@ResponseBody
	public String clientSaveMaterial(@RequestBody WorkMaterialStorage wms){
		
		return iMaterialStorageService.insertMaterial(wms);
	}
	
	/**
	 * 后台：删除物料信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteMaterial", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMaterial(@RequestBody JSONObject param){
		
		return iMaterialStorageService.deleteMaterial(param.getString("wiId"));
	}
}
