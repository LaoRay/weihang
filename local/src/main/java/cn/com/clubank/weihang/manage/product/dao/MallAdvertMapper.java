package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MallAdvertMapper {
	int deleteByPrimaryKey(String id);

	int insert(MallAdvert record);

	MallAdvert selectByPrimaryKey(String id);

	List<MallAdvert> selectAll();

	int updateByPrimaryKey(MallAdvert record);

	int updateSelectiveByPrimaryKey(MallAdvert mallAdvert);

	/**
	 * 根据广告位编号查询广告列表
	 * @param positionCode
	 * @return
	 */
	List<MallAdvert> selectAdvertListByPositionCode(String positionCode);

	void handleMallAdvert();
	
	/**
	 * 根据广告名称模糊查询、广告状态、广告位置类型查询到的总条数
	 * @param adName 广告名称
	 * @param status 广告状态
	 * @param positionType 广告位置类型
	 * @return
	 */
	int selectSumByAdName(@Param("adName") String adName,@Param("status") Integer status,@Param("positionType") Integer positionType);
	
	/**
	 * 根据广告名称模糊查询，状态、类型查询并分页
	 * @param adName 广告名称
	 * @param status 广告状态
	 * @param positionType 广告位置类型
	 * @param startIndex 起始索引
	 * @param pageSize 每页行数
	 * @return
	 */
	List<Map<String,Object>> selectByAdNameOrStartOrTypePaged(@Param("adName") String adName,@Param("status") Integer status,@Param("positionType") Integer positionType,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
	
	/**
	 * 关闭广告
	 * @param id 广告ID
	 * @return
	 */
	int updateAdvertStatus(String id);
	
	/**
	 * 通过广告ID获得广告详情
	 * @param id
	 * @return
	 */
	Map<String,Object> selectAdvertDetailByKey(String id);
}