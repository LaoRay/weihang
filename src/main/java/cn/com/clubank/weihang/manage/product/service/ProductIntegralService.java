package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;

/**
 * 商品积分管理
 * 
 * @author LeiQY
 *
 */
public interface ProductIntegralService {

	/**
	 * 添加或修改商品积分
	 * 
	 * @param productIntegral
	 * @return
	 */
	CommonResult clientModifyProductIntegral(ProductIntegral productIntegral);

	/**
	 * 查询商品积分列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param status
	 * @param skuName
	 * @return
	 */
	CommonResult clientFindProductIntegralList(Integer pageIndex, Integer pageSize, Integer status, String skuName);

	/**
	 * 删除
	 * 
	 * @param productIntegralId
	 * @return
	 */
	CommonResult clientDeleteProductIntegral(String productIntegralId);

}
