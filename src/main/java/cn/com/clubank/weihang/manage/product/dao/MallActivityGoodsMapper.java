package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;

public interface MallActivityGoodsMapper {
	int deleteByPrimaryKey(String id);

	int insert(MallActivityGoods record);

	MallActivityGoods selectByPrimaryKey(String id);

	List<MallActivityGoods> selectAll();

	int updateByPrimaryKey(MallActivityGoods record);

	/**
	 * 根据活动id分页查询活动商品列表
	 */
	List<Map<String, String>> selectByActivityId(@Param("activityId") String activityId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);

	/**
	 * 查询最新的num个活动商品
	 * 
	 * @param num
	 * @return
	 */
	List<Map<String, Object>> selectNewestByNum(@Param("num") Integer num);

	int updateSelectiveByPrimaryKey(MallActivityGoods goods);

	MallActivityGoods selectBySkuId(String skuId);

	/**
	 * 批量新增
	 */
	void insertByBatch(List<MallActivityGoods> list);

	/**
	 * 通过活动ID获得活动商品
	 * 
	 * @param activityId
	 *            活动Id
	 * @return
	 */
	List<Map<String, Object>> selectActivityGoodsByActivityId(String activityId);

	/**
	 * 通过活动ID查询到的活动商品总数
	 * 
	 * @param activityId
	 *            活动ID
	 * @return
	 */
	int selectGoodsCount(String activityId);

	int deleteByActivityId(String activityId);

	MallActivityGoods selectGoodsByActivityIdAndSkuId(@Param("activityId") String activityId,
			@Param("skuId") String skuId);

	MallActivityGoods selectActivityGoodsBySkuId(String skuId);
}