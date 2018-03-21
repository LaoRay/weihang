package cn.com.clubank.weihang.manage.dataItem.service;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 数据字典分类表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface IDataItemService {
	
	/**
	 * 获得数据字典分类列表
	 * @return
	 */
	CommonResult selectDataItemList(Integer pageIndex, Integer pageSize);
}
