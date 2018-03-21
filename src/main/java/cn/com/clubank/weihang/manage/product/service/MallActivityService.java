package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;

/**
 * 活动商城
 * 
 * @author LeiQY
 *
 */
public interface MallActivityService {

	/**
	 * 查询活动列表及对应的活动商品信息
	 * 
	 * @return
	 */
	CommonResult findActivityAndProduct();

	/**
	 * 根据活动id查询活动商品列表
	 * 
	 * @param activityId
	 * @return
	 */
	CommonResult findActivityProductListByActivityId(String activityId, Integer pageIndex, Integer pageSize);

	/**
	 * 通过活动Id获得活动信息
	 */
	String selectInfoByKey(String id);

	/**
	 * 新增或修改活动
	 * 
	 * @param mallActivity
	 * @return
	 */
	CommonResult modifyActivity(MallActivity mallActivity);

	/**
	 * 删除活动
	 * 
	 * @param activityId
	 * @return
	 */
	CommonResult deleteActivity(String activityId);

	/**
	 * 根据活动状态查询、活动名称模糊查询并分页
	 * 
	 * @param title
	 *            活动名称
	 * @param status
	 *            活动状态
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectByActivityNamePaged(String title, Integer status, Integer pageIndex, Integer pageSize);

	/**
	 * 通过活动ID获得活动详情
	 * 
	 * @param activityId
	 *            活动ID
	 * @return
	 */
	String selectActivityInfoByActivityId(String activityId);

	/**
	 * 通过活动ID获得活动商品列表
	 * 
	 * @param activityId
	 *            活动ID
	 * @return
	 */
	String selectActivityGoodsListByActivityId(String activityId);

	/**
	 * 定时处理活动状态
	 */
	void handleActivityMall();
}
