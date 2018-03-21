package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductCategory;

/**
 * 产品类別表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface IProductCategoryService {

	/**
	 * 保存分类信息
	 * 
	 * @param record
	 *            产品类别表对象
	 * @return
	 */
	public String insertSortInfo(ProductCategory record);

	/**
	 * 删除分类信息
	 * 
	 * @param categoryId
	 *            类别ID
	 * @return
	 */
	public String deleteSortInfo(String categoryId);

	/**
	 * 获取所有根分类列表
	 * 
	 * @return
	 */
	public String selectAllRootCategory();

	/**
	 * 获取所有子分类(叶子节点)列表
	 * 
	 * @return
	 */
	public String selectAllLeafCategory();

	/**
	 * @param parentId
	 *            父ID
	 * @return
	 */
	public String selectSubclassification(String parentId);

	/**
	 * 分页查询：通过分类名称(分类名)模糊查询
	 * 
	 * @param cname
	 *            分类名
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String findPage(String cname, Integer pageIndex, Integer pageSize);

	/**
	 * 根据父分类ID获取子分类列表及其商品信息
	 * 
	 * @param parentId
	 * @return
	 */
	CommonResult selectSubclassificationListAndProductList(String parentId);

	/**
	 * 后台：获取分类树结构数据
	 * 
	 * @return
	 */
	String clientProductCategoryTreeData();
	
	/**
	 * 后台：获取分类树结构数据
	 * 
	 * @param type 0-所有，1-服务类，2-实体类
	 * @return
	 */
	String clientProductCategoryTreeDataByType(Integer type);

	/**
	 * 后台：编辑分类信息
	 * 
	 * @param category
	 * @return
	 */
	String modifySortInfo(ProductCategory category);

	/**
	 * 后台：根据分类id查询分类信息
	 * 
	 * @param categoryId
	 * @return
	 */
	String findSortInfoByCategoryId(String categoryId);
}
