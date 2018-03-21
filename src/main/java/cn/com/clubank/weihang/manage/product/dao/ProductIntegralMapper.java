package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProductIntegralMapper {
	int deleteByPrimaryKey(String productIntegralId);

	int deleteBySkuId(String skuId);

	int insert(ProductIntegral record);

	ProductIntegral selectByPrimaryKey(String productIntegralId);

	List<ProductIntegral> selectAll();

	int updateByPrimaryKey(ProductIntegral record);

	int updateSelectiveByPrimaryKey(ProductIntegral productIntegral);

	ProductIntegral selectBySkuId(String skuId);

	int selectProductIntegralCount(@Param("status") Integer status, @Param("skuName") String skuName);

	List<Map<String, String>> selectProductIntegralList(@Param("start") Integer start,
			@Param("pageSize") Integer pageSize, @Param("status") Integer status, @Param("skuName") String skuName);
}