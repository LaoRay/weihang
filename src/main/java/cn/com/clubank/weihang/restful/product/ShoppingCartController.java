package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ShoppingCart;
import cn.com.clubank.weihang.manage.product.service.ShoppingCartService;

/**
 * 购物车管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	/**
	 * 商品添加购物车
	 * 
	 * @param shoppingCart
	 * @return
	 */
	@RequestMapping(value = "/productAddShoppingCart", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
		return shoppingCartService.addShoppingCart(shoppingCart);
	}

	/**
	 * 编辑购物车商品数量
	 * 
	 * @param shoppingCart
	 * @return
	 */
	@RequestMapping(value = "/productModifyShoppingCart", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyShoppingCart(@RequestBody JSONObject json) {
		return shoppingCartService.modifyShoppingCart(json.getString("cartId"), json.getInteger("quantity"));
	}

	/**
	 * 查询购物车列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/productFindShoppingCartListByCustomerId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findShoppingCartListByCustomerId(@RequestBody JSONObject json) {
		return shoppingCartService.findShoppingCartListByCustomerId(json.getString("customerId"));
	}

	/**
	 * 删除购物车中商品
	 * 
	 * @param cartIds
	 * @return
	 */
	@RequestMapping(value = "/productDeleteShoppingCartByCartIds", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteShoppingCartByCartIds(@RequestBody JSONObject json) {
		return shoppingCartService.deleteShoppingCartByCartIds(json.getJSONArray("cartIds"));
	}
}
