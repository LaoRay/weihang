package cn.com.clubank.weihang.manage.dataItem.service;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 数据字典明细表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface IDataItemDetailService {
	
	/**
	 * 通过key(code)获得名称和值
	 * @param code
	 * @return
	 */
	CommonResult gainNameValue(String code);
	
	/**
	 * 获得数据字典分类下明细列表
	 * @param dataId 数据字典分类ID
	 * @return
	 */
	String selectDataItemDetailList(String dataId);
}
