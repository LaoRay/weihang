package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.MallActivityPurRecord;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MallActivityPurRecordMapper {
	int deleteByPrimaryKey(String prId);

	int insert(MallActivityPurRecord record);

	MallActivityPurRecord selectByPrimaryKey(String prId);

	List<MallActivityPurRecord> selectAll();

	int updateByPrimaryKey(MallActivityPurRecord record);

	Integer selectSumQuantityByGoodsId(@Param("customerId") String customerId, @Param("goodsId") String goodsId);
}