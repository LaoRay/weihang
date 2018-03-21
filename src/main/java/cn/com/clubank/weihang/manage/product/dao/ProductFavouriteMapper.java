package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.ProductFavourite;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProductFavouriteMapper {
	int deleteByPrimaryKey(String fId);

	int insert(ProductFavourite record);

	ProductFavourite selectByPrimaryKey(String fId);

	List<ProductFavourite> selectAll();

	int updateByPrimaryKey(ProductFavourite record);

	List<Map<String, String>> selectListByCustomerId(@Param("customerId") String customerId,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	int selectFavouriteListSizeByCustomerId(String customerId);

	ProductFavourite selectFavouriteByCustomerIdAndProductId(@Param("customerId") String customerId,
			@Param("productId") String productId);

	int deleteByCustomerIdAndProduct(@Param("customerId") String customerId, @Param("productId") String productId);
}