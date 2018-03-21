package cn.com.clubank.weihang.manage.material.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.material.pojo.WorkMaterialStorage;

/**
 * 物料仓储表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface IMaterialStorageService {

	/**
	 * 获取维修材料名称列表
	 */
	CommonResult getRepairMaterialList();
	
	/**
	 * 搜索维修材料
	 * @param itemName 材料名称
	 * @return
	 */
	String searchRepairMaterial(String itemName);
	
	/**
	 * 分页查询：通过物料名模糊查询并分页
	 * @param itemName  物料名
	 * @param pageIndex 页码下标
	 * @param pageSize  每页行数
	 * @return
	 */
	String selectByItemNamePage(String itemName, Integer pageIndex, Integer pageSize);
	
	/**
	 * 客户端：通过物料ID获取材料信息
	 * @param wiId 物料ID
	 * @return
	 */
	String selectByWiId(String wiId);
	
	/**
	 * 客户端：保存物料信息
	 * @param wms 物料仓储表对象
	 * @return
	 */
	String insertMaterial(WorkMaterialStorage wms);
	
	/**
	 * 删除物料
	 * @param wiId 物料id
	 * @return
	 */
	String deleteMaterial(String wiId);
}
