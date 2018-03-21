package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MallActivityMapper {

	int deleteByPrimaryKey(String id);

	int insert(MallActivity record);

	MallActivity selectByPrimaryKey(String id);

	List<MallActivity> selectAll();

	int updateByPrimaryKey(MallActivity record);

	int updateSelectiveByPrimaryKey(MallActivity mallActivity);

	/**
	 * 更新活动状态
	 */
	void handleMallActivityStatus();

	/**
	 * 按照结束时间查询最先结束的一个活动-首页显示
	 * 
	 * @return
	 */
	MallActivity selectFirstByEndTime();

	/**
	 * 查询有效的活动
	 * 
	 * @return
	 */
	List<MallActivity> selectValidActivityList();

	/**
	 * 按活动名称模糊查询或状态查询到的总条数
	 * 
	 * @param title
	 *            活动名称
	 * @param status
	 *            活动状态
	 * @return
	 */
	int selectSumByActivityName(@Param("title") String title, @Param("status") Integer status);

	/**
	 * 根据活动状态查询、活动名称模糊查询并分页
	 * 
	 * @param title
	 *            活动名称
	 * @param status
	 *            活动状态
	 * @param startIndex
	 *            起始索引
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<Map<String, Object>> selectByActivityNamePaged(@Param("title") String title, @Param("status") Integer status,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 查询即将失效的活动
	 * 
	 * @return
	 */
	List<MallActivity> selectInvalidActivityList();

	/**
	 * 查询即将开始的活动
	 * 
	 * @return
	 */
	List<MallActivity> selectImminentActivity();
}