package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseAreaMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseParametersMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseArea;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseParameters;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseParameterService;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础参数管理 系统参数表业务逻辑层
 * 
 * @author LeiQY
 *
 */
@Service
@Slf4j
public class BaseParameterServiceImpl implements BaseParameterService {

	@Autowired
	private BaseParametersMapper parameterMapper;

	@Autowired
	private BaseAreaMapper areaMapper;

	/**
	 * 根据keyCode获取参数
	 */
	@Override
	public BaseParameters getParameterByKeyCode(String keyCode) {
		try {
			if (StringUtils.isBlank(keyCode)) {
				return new BaseParameters();
			}
			return parameterMapper.selectByKeyCode(keyCode);
		} catch (Exception e) {
			log.error("服务器内部异常，获取参数失败", e);
			return new BaseParameters();
		}
	}

	/**
	 * 获取地区列表
	 */
	@Override
	public CommonResult findAreaList(String parentId) {
		if (StringUtils.isBlank(parentId)) {
			parentId = Constants.INT_0.toString();
		}
		List<BaseArea> list = areaMapper.selectAreaListByParentId(parentId);
		if (list == null || list.size() == 0) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "获取地区列表失败");
		}
		List<Map<String, String>> areaList = new ArrayList<>();
		for (BaseArea baseArea : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("aId", baseArea.getAId());
			map.put("parentId", baseArea.getParentId());
			map.put("name", baseArea.getName());
			areaList.add(map);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取地区列表成功", areaList);
	}

	@Override
	public String selectParameterListAndPaged(String name, String keyCode, Integer pageIndex, Integer pageSize) {
		PageObject<BaseParameters> page=new PageObject<>(pageIndex, pageSize);
		
		int total=parameterMapper.selectCountByNameAndCode(name, keyCode);
		List<BaseParameters> list=parameterMapper.selectParameterListAndPaged(name, keyCode, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String insertOrUpdateParameter(BaseParameters record) {
		JSONObject json=new JSONObject();
		
		if(StringUtils.isBlank(record.getPId())){//参数主键为空，执行新增
			if(parameterMapper.insert(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		}else{
			if(parameterMapper.updateByPrimaryKeySelective(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "编辑成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "编辑失败");
			}
		}
		return json.toString();
	}

	@Override
	public String deleteParameter(String pId) {
		JSONObject json=new JSONObject();
		
		if(parameterMapper.deleteByPrimaryKey(pId)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}
}
