package cn.com.clubank.weihang.manage.product.service;

import com.alibaba.fastjson.JSONArray;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ShoppingCart;

/**
 * 购物车管理
 * 
 * @author LeiQY
 *
 */
public interface ShoppingCartService {

	/**
	 * 商品添加购物车
	 * 
	 * @param shoppingCart
	 * @return
	 */
	CommonResult addShoppingCart(ShoppingCart shoppingCart);

	/**
	 * 查询购物车列表
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult findShoppingCartListByCustomerId(String customerId);

	/**
	 * 删除购物车中商品
	 * 
	 * @param cartIds
	 * @return
	 */
	CommonResult deleteShoppingCartByCartIds(JSONArray cartIds);

}
