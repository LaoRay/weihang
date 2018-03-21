package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.manage.systemSettings.dao.BaseCodeRuleMapper;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 编号规则项目
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class BaseCodeRuleServiceImpl implements BaseCodeRuleService {

	@Resource
	private BaseCodeRuleMapper baseCodeRuleMapper;
	
	@Override
	public synchronized String getCode(String itemCode) {
		//TODO 统一生成
		String result = itemCode + new SimpleDateFormat("yyDDDHHmmssSSS").format(new Date());
		log.info("生成单号：{}", result);
		return result;
	}

}
