package cn.com.clubank.weihang.manage.dataItem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.dataItem.dao.BaseDataitemMapper;
import cn.com.clubank.weihang.manage.dataItem.pojo.BaseDataitem;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemService;

/**
 * 数据字典分类表业务逻辑层
 * @author Liangwl
 *
 */
@Service
public class DataItemServiceImpl implements IDataItemService {

	@Resource
	private BaseDataitemMapper baseDataitemMapper;
	
	@Override
	public String selectDataItemList() {
		JSONObject json=new JSONObject();
		
		List<BaseDataitem> list=baseDataitemMapper.selectAll();
		if(!list.isEmpty()){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return json.toString();
	}


}
