package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseRoleModuleButtonMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRoleModuleButton;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseRoleModuleButtonService;

@Service
public class BaseRoleModuleButtonServiceImpl implements IBaseRoleModuleButtonService {

	@Resource
	private BaseRoleModuleButtonMapper baseRoleModuleButtonMapper;
	
	@Override
	public String insertRoleModuleButton(BaseRoleModuleButton record) {
		JSONObject json=new JSONObject();
		
		if(baseRoleModuleButtonMapper.insert(record)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "分配成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "分配失败");
		}
		return json.toString();
	}

}
