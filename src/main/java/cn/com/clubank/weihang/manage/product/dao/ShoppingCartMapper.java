package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ShoppingCart;

public interface ShoppingCartMapper {
	int deleteByPrimaryKey(String cartId);

	int insert(ShoppingCart record);

	ShoppingCart selectByPrimaryKey(String cartId);

	List<ShoppingCart> selectAll();

	int updateByPrimaryKey(ShoppingCart record);

	List<Map<String, String>> selectShoppingCartListByCustomerId(@Param("customerId") String customerId,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	void deleteShoppingCartByCartIds(List<String> list);

	ShoppingCart selectShoppingCartByCustomerIdAndSkuId(@Param("customerId") String customerId,
			@Param("skuId") String skuId);

	int selectCountByCustomerId(String customerId);
}