package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductSku;

public interface ProductSkuMapper {
	
	int deleteByPrimaryKey(String skuId);

	int insert(ProductSku record);

	ProductSku selectByPrimaryKey(String skuId);

	List<ProductSku> selectAll();

	int updateByPrimaryKey(ProductSku record);

	List<ProductSku> selectSkuListByProductId(String productId);

	int updateSelectiveByPrimaryKey(ProductSku productSku);

	void insertBatch(List<ProductSku> list);

	Map<String, String> selectProductSkuDetailBySkuId(String skuId);

	List<Map<String, String>> selectSkuInfoByProductId(String productId);

	Map<String, String> selectSkuInfoByPrimaryKey(String skuId);
	
	/**
	 * 根据商品id和属性ids数组查询sku：查询属性值包含attrValueIds的sku
	 * @param productId
	 * @param attrValueIds
	 * @return
	 */
	List<ProductSku> selectByProductIdAndAttrValueIds(@Param("productId") String productId, @Param("attrValueIds") String[] attrValueIds);
}