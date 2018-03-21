package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.MallAdvertPosition;

public interface MallAdvertPositionMapper {
    int deleteByPrimaryKey(String id);

    int insert(MallAdvertPosition record);

    MallAdvertPosition selectByPrimaryKey(String id);

    List<MallAdvertPosition> selectAll();

    int updateByPrimaryKey(MallAdvertPosition record);
    
    int updateSelectiveByPrimaryKey(MallAdvertPosition record);
    
	/**
	 * 根据广告位名称模糊查询、广告位位置类型查询到的总条数
	 * @param positionName 广告位名称
	 * @param positionType 广告位位置类型
	 * @return
	 */
	int selectSumByPositionName(@Param("positionName")String positionName,@Param("positionType")Integer positionType);
	
	/**
	 * 根据广告位位置类型查询、广告位名称模糊查询并分页
	 * @param positionName 广告位名称
	 * @param positionType 广告位位置类型
	 * @param startIndex 起始索引
	 * @param pageSize 每页行数
	 * @return
	 */
	List<MallAdvertPosition> selectByAdPositionIdNamePaged(@Param("positionName")String positionName,@Param("positionType")Integer positionType,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}