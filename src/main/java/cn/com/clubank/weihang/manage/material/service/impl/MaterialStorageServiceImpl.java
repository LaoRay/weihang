package cn.com.clubank.weihang.manage.material.service.impl;

import java.util.HashMap;
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
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialCategoryMapper;
import cn.com.clubank.weihang.manage.material.dao.WorkMaterialStorageMapper;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialCategory;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;
import cn.com.clubank.weihang.manage.material.service.IMaterialStorageService;
import cn.com.clubank.weihang.manage.repair.dao.WorkOrderMaterialMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkRepairMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOrderMaterial;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;

@Service
public class MaterialStorageServiceImpl implements IMaterialStorageService {
	
	@Resource
	private WorkMaterialCategoryMapper workMaterialCategoryMapper;

	@Resource
	private WorkMaterialStorageMapper workMaterialStorageMapper;

	@Resource
	private WorkOrderMaterialMapper workOrderMaterialMapper;

	@Resource
	private WorkRepairMapper workRepairMapper;

	@Override
	public CommonResult getRepairMaterialList() {
		List<WorkMaterialStorage> list = workMaterialStorageMapper.selectItemName();
		return CommonResult.result(ResultCode.SUCC.getValue(), "成功", list);
	}

	@Override
	public String searchRepairMaterial(String itemName) {
		JSONObject json = new JSONObject();
		List<WorkMaterialStorage> list = workMaterialStorageMapper.selectByItemName(itemName == null ? "" : itemName);
		json.put("data", list);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	@Override
	public String selectByItemNamePage(String itemName, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);

		int total = workMaterialStorageMapper.findPageTotal(itemName);
		List<Map<String, Object>> list = workMaterialStorageMapper.findPage(itemName, page.getStart(),
				page.getPageSize());
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByWiId(String wiId) {
		JSONObject json = new JSONObject();

		Map<String,Object> map=new HashMap<>();
		
		WorkMaterialStorage wms = workMaterialStorageMapper.selectByPrimaryKey(wiId);
		
		if (wms == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", wms);
		} else {
			map.put("MaterialInfo", wms);
			if(!StringUtils.isBlank(wms.getCategoryId())){
				WorkMaterialCategory materialCategory=workMaterialCategoryMapper.selectByPrimaryKey(wms.getCategoryId());
				if(materialCategory!=null){
					map.put("categoryName", materialCategory.getCategoryName() == null ? null : materialCategory.getCategoryName());
				}
			}
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", map);
		}
		return json.toString();
	}

	@Override
	public String insertMaterial(WorkMaterialStorage wms) {
		JSONObject json = new JSONObject();

		// 判断是执行新增还是更新。wiId为空执行新增，不为空执行更新
		if (StringUtils.isBlank(wms.getWiId())) {
			if (workMaterialStorageMapper.insert(wms) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "添加成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "添加失败");
			}
		} else {
			if (workMaterialStorageMapper.updateByPrimaryKey(wms) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		}
		return json.toString();
	}

	@Override
	public String deleteMaterial(String wiId) {
		JSONObject json = new JSONObject();

		WorkMaterialStorage material = workMaterialStorageMapper.selectByPrimaryKey(wiId);
		if (!StringUtils.isBlank(material.getCode())) {// 物料编码不为空
			List<WorkOrderMaterial> list = workOrderMaterialMapper.selectWrIdListByCode(material.getCode());

			for (WorkOrderMaterial workOrderMaterial : list) {
				if (!StringUtils.isBlank(workOrderMaterial.getWorkOrderId())) {// 维修单ID不为空
					WorkRepair repair = workRepairMapper.selectByPrimaryKey(workOrderMaterial.getWorkOrderId());// 获得维修单对象
					// 判断维修单是否结束
					if (repair.getStatus() < Constants.INT_10) {
						json.put("apiStatus", ResultCode.FAIL.getValue());
						json.put("msg", "有未完成的维修单在使用此物料，不能删除");
						return json.toString();
					}
				}
			}
		}

		if (workMaterialStorageMapper.deleteByPrimaryKey(wiId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

}
