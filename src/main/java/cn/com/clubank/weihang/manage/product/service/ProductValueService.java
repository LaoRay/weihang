package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductValue;

/**
 * 产品属性管理
 * 
 * @author LeiQY
 *
 */
public interface ProductValueService {

	/**
	 * 增加产品基本属性
	 * 
	 * @param productValue
	 * @return
	 */
	CommonResult addProductValue(ProductValue productValue);
	
	/**
	 * 增加产品基本属性-根据属性值id添加
	 * 
	 * @param attrValueId
	 * @return
	 */
	void addProductValue(String productId, String attrValueId);

	/**
	 * 根据产品id查询产品基本属性列表
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult findProductValueListByProductId(String productId);
}
