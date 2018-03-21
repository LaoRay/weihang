package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;

/**
 * 广告管理
 * 
 * @author LeiQY
 *
 */
public interface MallAdvertService {

	/**
	 * 广告新增或修改
	 * 
	 * @param mallAdvert
	 * @return
	 */
	CommonResult modifyAdvert(MallAdvert mallAdvert);

	/**
	 * 广告删除
	 * 
	 * @param id
	 *            广告ID
	 * @return
	 */
	CommonResult deleteAdvert(String id);

	/**
	 * 根据广告位编号查询广告列表
	 * 
	 * @param positionCode
	 * @return
	 */
	CommonResult findAdvertListByPositionCode(String positionCode);

	/**
	 * 根据广告名称模糊查询，广告状态、广告位置类型查询并分页
	 * 
	 * @param adName
	 *            广告名称
	 * @param status
	 *            广告状态
	 * @param positionType
	 *            广告位置类型
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectAdListByNameOrStatusOrType(String adName, Integer status, Integer positionType, Integer pageIndex,
			Integer pageSize);

	/**
	 * 关闭广告
	 * 
	 * @param id
	 *            广告ID
	 * @return
	 */
	String updateAdvertStatus(String id);

	/**
	 * 通过广告ID获得广告信息和广告位列表
	 * 
	 * @param id
	 *            广告ID
	 * @return
	 */
	String selectAdvertInfoAndPositionList(String id);

	/**
	 * 通过广告ID获得广告详情
	 * 
	 * @param id
	 *            广告ID
	 * @return
	 */
	String selectAdvertDetail(String id);

	/**
	 * 点击广告
	 * 
	 * @param id
	 *            广告ID
	 * @param customerId
	 *            用户id
	 * @return
	 */
	String updateAdvertClickCount(String id, String customerId);
}
