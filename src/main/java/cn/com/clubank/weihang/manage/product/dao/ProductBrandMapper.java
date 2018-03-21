package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.ProductBrand;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProductBrandMapper {
	int deleteByPrimaryKey(String brandId);

	int insert(ProductBrand record);

	ProductBrand selectByPrimaryKey(String brandId);

	List<ProductBrand> selectAll();

	int updateByPrimaryKey(ProductBrand record);

	List<ProductBrand> findPage(@Param("brandName") String brandName, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);

	int findPageTotal(@Param("brandName") String brandName);

	List<Map<String, String>> selectByCategoryId(String categoryId);
}