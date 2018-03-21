package cn.com.clubank.weihang.manage.material.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialStorageMapper;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;
import cn.com.clubank.weihang.manage.material.service.IMaterialStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaterialStorageServiceImpl implements IMaterialStorageService {

	@Resource
	private WorkMaterialStorageMapper workMaterialStorageMapper;
	
	@Override
	public CommonResult getRepairMaterialList() {
		try {
			List<WorkMaterialStorage> list=workMaterialStorageMapper.selectItemName();
			if(list.size()>0){
				return CommonResult.result(ResultCode.SUCC.getValue(), "成功",list);
			}else{
				return CommonResult.result(ResultCode.FAIL.getValue(), "失败");
			}
		} catch (Exception e) {
			log.error("服务器异常",e);
			return CommonResult.result(ResultCode.SERVER_ERROR.getValue(), "服务器异常");
		}		
	}

	@Override
	public String searchRepairMaterial(String itemName) {
		JSONObject json=new JSONObject();
		List<WorkMaterialStorage> list=workMaterialStorageMapper.selectByItemName(itemName == null ? "" : itemName);
		json.put("data", list);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String selectByItemNamePage(String itemName, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);
		
		int total=workMaterialStorageMapper.findPageTotal(itemName);
		List<Map<String, Object>> list=workMaterialStorageMapper.findPage(itemName, page.getStart(), page.getPageSize());
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByWiId(String wiId) {
		JSONObject json=new JSONObject();
		
		WorkMaterialStorage wms=workMaterialStorageMapper.selectByPrimaryKey(wiId);
		if(wms==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", wms);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", wms);
		}
		return json.toString();
	}

	@Override
	public String insertMaterial(WorkMaterialStorage wms) {
		JSONObject json=new JSONObject();
		
		//判断是执行新增还是更新。wiId为空执行新增，不为空执行更新
		if(StringUtils.isBlank(wms.getWiId())){
			if(workMaterialStorageMapper.insert(wms)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "添加成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "添加失败");
			}
		}else{
			if(workMaterialStorageMapper.updateByPrimaryKey(wms)>Constants.INT_0){
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
	public String deleteMaterial(String wiId) {
		JSONObject json=new JSONObject();
		
		if(workMaterialStorageMapper.deleteByPrimaryKey(wiId)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

}
