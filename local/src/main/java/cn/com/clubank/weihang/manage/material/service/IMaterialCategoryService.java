package cn.com.clubank.weihang.manage.material.service;

import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialCategory;

/**
 * 物料分类表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface IMaterialCategoryService {

	/**
	 * 分页查询：通过物料分类名模糊查询并分页
	 * @param categoryName  物料分类名
	 * @param pageIndex 页码下标
	 * @param pageSize  每页行数
	 * @return
	 */
	String selectByCategoryNamePage(String categoryName, Integer pageIndex, Integer pageSize);
	
	/**
	 * 客户端：通过物料分类ID获取分类信息
	 * @param wicId 分类ID
	 * @return
	 */
	String selectByWicId(String wicId);
	
	/**
	 * 新增或更新物料分类信息
	 * @param wmc 物料分类对象
	 * @return
	 */
	String insertOrUpdate(WorkMaterialCategory wmc);
	
	/**
	 * 获得分类ID与分类名称
	 * @return
	 */
	String selectMaterialCategoryId();
	
	/**
	 * 删除物料分类信息
	 * @param wicId 分类ID
	 * @return
	 */
	String deleteMaterialCategoryInfo(String wicId);
}
