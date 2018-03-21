package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseLogMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseLog;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseLogService;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志管理
 * @author YangWei
 *
 */
@Slf4j
@Service
public class BaseLogServiceImpl implements IBaseLogService {

	@Resource
	private BaseLogMapper baseLogMapper;
	
	@Override
	public String selectLogListAndPaged(String operateName, String operateTimeOne, String operateTimeTwo,
			Integer pageIndex, Integer pageSize) {
		PageObject<BaseLog> page=new PageObject<BaseLog>(pageIndex, pageSize);
		
		int total=baseLogMapper.selectLogListAndPagedCount(operateName, operateTimeOne, operateTimeTwo);
		List<BaseLog> list=baseLogMapper.selectLogListAndPaged(operateName, operateTimeOne, operateTimeTwo, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void saveLog(String operateUserId, String operateName, String module, String features, String ipAddress,
			Integer flatType) {
		String content = module + "-" + features;
		BaseLog baseLog = new BaseLog(operateUserId, operateName, module, features, ipAddress, content, flatType);
		log.info("保存系统日志：{}", JSON.toJSONStringWithDateFormat(baseLog, Constants.DATE_COMMON));
		baseLogMapper.insert(baseLog);
	}

}
