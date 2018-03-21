package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProductAftersaleApplyMapper {
	int deleteByPrimaryKey(String id);

	int insert(ProductAftersaleApply record);

	ProductAftersaleApply selectByPrimaryKey(String id);

	List<ProductAftersaleApply> selectAll();

	int updateByPrimaryKey(ProductAftersaleApply record);

	List<Map<String, String>> selectListByCustomerId(@Param("customerId") String customerId,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	Integer selectApplyCount(@Param("status") Integer status, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

	List<Map<String, String>> selectApplyInfoList(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("status") Integer status, @Param("startDate") String startDate, @Param("endDate") String endDate);
}