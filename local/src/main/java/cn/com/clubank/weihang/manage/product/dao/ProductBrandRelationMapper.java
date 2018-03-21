package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.ProductBrandRelation;
import java.util.List;

public interface ProductBrandRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductBrandRelation record);

    ProductBrandRelation selectByPrimaryKey(String id);

    List<ProductBrandRelation> selectAll();

    int updateByPrimaryKey(ProductBrandRelation record);

	int deleteByBrandId(String brandId);

	List<ProductBrandRelation> selectByBrandId(String brandId);
}