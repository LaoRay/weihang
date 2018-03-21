package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductBrand;

/**
 * 产品品牌管理
 * 
 * @author YangWei
 *
 */
public interface ProductBrandService {

	/**
	 * 保存、更新品牌信息
	 * 
	 * @param brand
	 * @param categoryIds
	 * @return
	 */
	CommonResult saveOrUpdate(ProductBrand brand, String categoryIds);

	/**
	 * 根据品牌ids批量删除品牌
	 * 
	 * @param brandIds
	 * @return
	 */
	CommonResult delete(String brandIds);

	/**
	 * 分页查询品牌列表
	 * 
	 * @param brandName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	String findPage(String brandName, Integer pageIndex, Integer pageSize);

	/**
	 * 查询所有品牌列表
	 * 
	 * @return
	 */
	String findAll();

	/**
	 * 根据品牌id查询品牌信息
	 * 
	 * @param brandId
	 * @return
	 */
	CommonResult clientFindProductBrandByBrandId(String brandId);

	/**
	 * 根据品牌id删除品牌
	 * 
	 * @param brandId
	 * @return
	 */
	CommonResult clientDeleteProductBrand(String brandId);

	/**
	 * 根据分类id查询品牌
	 * 
	 * @param categoryId
	 * @return
	 */
	CommonResult clientFindProductBrandByCategoryId(String categoryId);
}
