package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ShoppingCartMapper;
import cn.com.clubank.weihang.manage.product.pojo.ShoppingCart;
import cn.com.clubank.weihang.manage.product.service.ShoppingCartService;

/**
 * 购物车管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ShoppingCartMapper shoppingCartMapper;

	/**
	 * 商品添加购物车
	 */
	@Override
	public CommonResult addShoppingCart(ShoppingCart shoppingCart) {
		if (StringUtils.isBlank(shoppingCart.getCustomerId())) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "您尚未登录");
		}
		ShoppingCart cart = shoppingCartMapper.selectShoppingCartByCustomerIdAndSkuId(shoppingCart.getCustomerId(),
				shoppingCart.getSkuId());
		if (cart != null) {
			cart.setQuantity(cart.getQuantity() + shoppingCart.getQuantity());
			shoppingCartMapper.updateByPrimaryKey(cart);
		} else {
			shoppingCartMapper.insert(shoppingCart);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "添加购物车成功");
	}

	/**
	 * 编辑购物车商品数量
	 */
	@Override
	public CommonResult modifyShoppingCart(String cartId, Integer quantity) {
		if (StringUtils.isBlank(cartId) || quantity == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		ShoppingCart cart = shoppingCartMapper.selectByPrimaryKey(cartId);
		if (cart == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "信息不存在");
		}
		cart.setQuantity(quantity);
		shoppingCartMapper.updateByPrimaryKey(cart);
		return CommonResult.result(ResultCode.SUCC.getValue(), "更新成功");
	}

	/**
	 * 查询购物车列表
	 */
	@Override
	public CommonResult findShoppingCartListByCustomerId(String customerId) {
		List<Map<String, String>> shoppingCartList = shoppingCartMapper.selectShoppingCartListByCustomerId(customerId,
				-1, 10);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询购物车列表成功", shoppingCartList);
	}

	/**
	 * 删除购物车中商品
	 */
	@Override
	public CommonResult deleteShoppingCartByCartIds(JSONArray array) {
		List<String> cartIds = JSONObject.parseArray(array.toJSONString(), String.class);
		shoppingCartMapper.deleteShoppingCartByCartIds(cartIds);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	/**
	 * pc：查询购物车列表
	 */
	@Override
	public CommonResult websiteFindShoppingCartListByCustomerId(String customerId, Integer pageIndex,
			Integer pageSize) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = shoppingCartMapper.selectCountByCustomerId(customerId);
		page.setRows(total);
		List<Map<String, String>> shoppingCartList = shoppingCartMapper.selectShoppingCartListByCustomerId(customerId,
				page.getStart(), page.getPageSize());
		page.setDataList(shoppingCartList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}
}
