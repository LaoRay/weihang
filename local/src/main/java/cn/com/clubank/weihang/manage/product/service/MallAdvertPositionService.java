package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.manage.product.pojo.MallAdvertPosition;

/**
 * 广告位表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface MallAdvertPositionService {

	/**
	 * 根据广告位位置类型查询、广告位名称模糊查询并分页
	 * @param positionName 广告位名称
	 * @param positionType 广告位位置类型
	 * @param pageIndex 页码下标
	 * @param pageSize  每页行数
	 * @return
	 */
	String selectByAdPositionIdNamePaged(String positionName,Integer positionType,Integer pageIndex,Integer pageSize);
	
	/**
	 * 通过广告位ID获得广告位信息
	 * @param id 广告位ID
	 * @return
	 */
	String selectAdvertPositionInfo(String id);
	
	/**
	 * 新增或编辑广告位
	 * @param record 广告位表对象
	 * @return
	 */
	String insertOrUpdateAdvertPosition(MallAdvertPosition record);
	
	/**
	 * 删除广告位
	 * @param id 广告位主键
	 * @return
	 */
	String deleteAdvertPosition(String id);
	
	/**
	 * 获得所有广告位信息
	 */
	String selectAllAdvertPositionInfo();
}
