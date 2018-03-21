package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MallActivityGoodsMapper {
	int deleteByPrimaryKey(String id);

	int insert(MallActivityGoods record);

	MallActivityGoods selectByPrimaryKey(String id);

	List<MallActivityGoods> selectAll();

	int updateByPrimaryKey(MallActivityGoods record);

	List<Map<String, String>> selectByActivityId(String activityId);

	List<Map<String, String>> selectGoodListByActivityId(@Param("activityId") String activityId,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);
	
	/**
	 * 根据活动编号查询活动商品
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> selectBySequenceCode(@Param("sequenceCode") String sequenceCode,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	int updateSelectiveByPrimaryKey(MallActivityGoods goods);

	MallActivityGoods selectBySkuId(String skuId);
	
	/**
	 * 批量新增
	 */
	void insertByBatch(List<MallActivityGoods> list);
	
	/**
	 * 通过活动ID获得活动商品
	 * @param activityId 活动Id
	 * @return
	 */
	List<Map<String,Object>> selectActivityGoodsByActivityId(String activityId);
}