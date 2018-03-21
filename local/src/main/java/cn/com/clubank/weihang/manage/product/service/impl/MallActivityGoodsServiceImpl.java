package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.MallActivityGoodsService;

/**
 * 活动商品
 * 
 * @author LeiQY
 *
 */
@Service
public class MallActivityGoodsServiceImpl implements MallActivityGoodsService {

	@Autowired
	private MallActivityGoodsMapper activityGoodsMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;

	/**
	 * 编辑活动商品
	 */
	@Override
	public CommonResult modifyActivityGoods(MallActivityGoods goods) {
		if (goods != null) {

			MallActivityGoods mag = activityGoodsMapper.selectByPrimaryKey(goods.getId());
			ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
			if (mag == null || sku == null) {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误，没有找到sku或活动商品");
			}

			// 判断是否编辑活动商品数量
			if (goods.getQuantity() != mag.getQuantity() && (goods.getQuantity()
					- mag.getQuantity()) <= (sku.getProductQuantity() - sku.getProductSaleQuantity())) {
				// 更新商品SKU表中已售数量
				int quantity = goods.getQuantity() - mag.getQuantity();

				sku.setProductSaleQuantity(sku.getProductSaleQuantity() + quantity);
				productSkuMapper.updateSelectiveByPrimaryKey(sku);

				activityGoodsMapper.updateSelectiveByPrimaryKey(goods);
				return CommonResult.result(ResultCode.SUCC.getValue(), "编辑成功");
			} else {
				return CommonResult.result(ResultCode.FAIL.getValue(), "商品SKU数量不足");
			}
		}
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
	}

	/**
	 * 删除活动商品
	 */
	@Override
	public CommonResult deleteActivityGoods(String id) {

		// 得到要删除这条活动商品的数量和已售数量
		MallActivityGoods goods = activityGoodsMapper.selectByPrimaryKey(id);
		int quantity = goods.getQuantity();// 活动商品数量
		int sellQuantity = goods.getSellQuantity();// 已售数量

		int surplus = quantity - sellQuantity;// 剩余数量

		// 更新商品SKU表中已售数量
		ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
		sku.setProductSaleQuantity(sku.getProductSaleQuantity() - surplus);
		productSkuMapper.updateSelectiveByPrimaryKey(sku);

		activityGoodsMapper.deleteByPrimaryKey(id);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除活动商品成功");
	}

	@Override
	public CommonResult insertBatchActivityCoods(JSONArray array) {
		List<MallActivityGoods> list = JSONObject.parseArray(array.toJSONString(), MallActivityGoods.class);
		if (list != null && list.size() > 0) {
			for (MallActivityGoods goods : list) {
				ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
				if (sku == null) {
					return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品sku不存在");
				} else {
					if (goods.getQuantity() > sku.getSurplusQuantity()) {
						return CommonResult.result(ResultCode.FAIL.getValue(), "剩余库存小于" + goods.getQuantity());
					} else {
						sku.setProductSaleQuantity(sku.getProductSaleQuantity() + goods.getQuantity());
						productSkuMapper.updateSelectiveByPrimaryKey(sku);
					}
				}
			}
			activityGoodsMapper.insertByBatch(list);
			return CommonResult.result(ResultCode.SUCC.getValue(), "添加商品成功");
		}
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
	}

	@Override
	public String selectActivityGoodsInfo(String id) {
		JSONObject json = new JSONObject();

		MallActivityGoods goods = activityGoodsMapper.selectByPrimaryKey(id);
		if (goods == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", goods);
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", goods);
		}
		return json.toString();
	}
}
