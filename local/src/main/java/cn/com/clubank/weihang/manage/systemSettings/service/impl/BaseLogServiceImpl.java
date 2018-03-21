package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseLogMapper;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseLogService;

@Service
public class BaseLogServiceImpl implements IBaseLogService {

	@Resource
	private BaseLogMapper baseLogMapper;
	
	@Override
	public String selectLogListAndPaged(String operateName, String operateTimeOne, String operateTimeTwo,
			Integer pageIndex, Integer pageSize) {
		PageObject<Map<String,Object>> page=new PageObject<>(pageIndex, pageSize);
		
		int total=baseLogMapper.selectCount(operateName, operateTimeOne, operateTimeTwo);
		List<Map<String, Object>> list=baseLogMapper.selectLogListAndPaged(operateName, operateTimeOne, operateTimeTwo, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

}
