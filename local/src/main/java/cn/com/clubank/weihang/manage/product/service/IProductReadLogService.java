package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.manage.product.pojo.ProductReadLog;

/**
 * 产品浏览记录管理
 * @author Liangwl
 *
 */
public interface IProductReadLogService {

	/**
	 * 保存浏览记录
	 * @param record
	 * @return
	 */
	String insertReadLog(ProductReadLog record);
	
	/**
	 * 查询浏览记录并分页
	 * @param customerId 客户ID
	 * @param pageIndex 页码下标
	 * @param pageSize 每页行数
	 * @return
	 */
	String selectReadLog(String customerId,Integer pageIndex,Integer pageSize);
}
