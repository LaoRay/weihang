package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductValue;

public interface ProductValueMapper {
	int deleteByPrimaryKey(String pvId);

	int insert(ProductValue record);

	ProductValue selectByPrimaryKey(String pvId);

	List<ProductValue> selectAll();

	int updateByPrimaryKey(ProductValue record);

	List<ProductValue> selectListByProductId(String productId);

	void insertBatch(List<ProductValue> list);

	int updateSelectiveByPrimaryKey(ProductValue ProductValue);

	List<Map<String, Object>> selectAttrListByProductId(String productId);

	List<Map<String, String>> selectAttrValueListByProductAttrId(String productAttrId);

	int deleteByProductId(String productId);
	
	/**
	 * 根据产品和属性值id获取信息
	 * @param productId
	 * @param attrValueId
	 * @return
	 */
	List<ProductValue> selectByProductAndAttrValue(@Param("productId") String productId, @Param("attrValueId") String attrValueId);
}