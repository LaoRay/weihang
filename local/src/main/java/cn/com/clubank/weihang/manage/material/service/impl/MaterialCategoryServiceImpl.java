package cn.com.clubank.weihang.manage.material.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialCategoryMapper;
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialStorageMapper;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialCategory;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;
import cn.com.clubank.weihang.manage.material.service.IMaterialCategoryService;

@Service
public class MaterialCategoryServiceImpl implements IMaterialCategoryService {

	@Resource
	private WorkMaterialCategoryMapper workMaterialCategoryMapper;
	
	@Resource
	private WorkMaterialStorageMapper workMaterialStorageMapper;
	
	@Override
	public String selectByCategoryNamePage(String categoryName, Integer pageIndex, Integer pageSize) {
		PageObject<WorkMaterialCategory> page=new PageObject<WorkMaterialCategory>(pageIndex, pageSize);
		
		int total=workMaterialCategoryMapper.findPageTotal(categoryName);
		List<WorkMaterialCategory> list=workMaterialCategoryMapper.findPage(categoryName, page.getStart(), page.getPageSize());
		
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByWicId(String wicId) {
		JSONObject json=new JSONObject();
		
		WorkMaterialCategory wmc=workMaterialCategoryMapper.selectByPrimaryKey(wicId);
		if(wmc==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", wmc);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", wmc);
		}
		return json.toString();
	}

	@Override
	public String insertOrUpdate(WorkMaterialCategory wmc) {
		JSONObject json =new JSONObject();
		
		//判断是执行新增还是更新。wicId为空执行新增，不为空执行更新
		if(StringUtils.isBlank(wmc.getWicId())){
			if(workMaterialCategoryMapper.insert(wmc)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		}else{
			if(workMaterialCategoryMapper.updateByPrimaryKey(wmc)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		}
		return json.toString();
	}

	@Override
	public String selectMaterialCategoryId() {
		JSONObject json =new JSONObject();
		
		List<WorkMaterialCategory> list=workMaterialCategoryMapper.selectWicId();
		if(list==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data",list);
		}
		return json.toString();
	}

	@Override
	public String deleteMaterialCategoryInfo(String wicId) {
		JSONObject json =new JSONObject();
		
		//判断此分类下是否有物料，有的话不能删除。
		List<WorkMaterialStorage> list=workMaterialStorageMapper.selectMaterialListByCategoryId(wicId);
		if(list==null||list.size()==Constants.INT_0){
			if(workMaterialCategoryMapper.deleteByPrimaryKey(wicId)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "删除成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "删除失败");
			}
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "此分类下已有物料，不能删除");
		}
		return json.toString();
	}

}
