package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseButtonMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseButton;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseButtonService;

@Service
public class BaseButtonServiceImpl implements IBaseButtonService {

	@Resource
	private BaseButtonMapper baseButtonMapper;
	
	@Override
	public String selectModuleButton(String moduleId) {
		JSONObject json=new JSONObject();
		
		List<BaseButton> list=baseButtonMapper.selectModuleButtonByModuleId(moduleId);
		if(list==null||list.size()==0){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		}
		return json.toString();
	}

}
