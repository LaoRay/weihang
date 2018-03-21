package cn.com.clubank.weihang.manage.systemSettings.service;

/**
 * 编号规则项目
 * 
 * @author YangWei
 *
 */
public interface BaseCodeRuleService {

	/**
	 * 获取单号
	 * @param itemCode
	 * @return
	 */
	String getCode(String itemCode);
	
}
