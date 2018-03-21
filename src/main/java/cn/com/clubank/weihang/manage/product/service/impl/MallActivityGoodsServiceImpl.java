package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
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
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private ProductPicMapper productPicMapper;

	/**
	 * 编辑活动商品
	 */
	@Override
	public CommonResult modifyActivityGoods(MallActivityGoods goods) {
		if (goods == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		MallActivityGoods mag = activityGoodsMapper.selectByPrimaryKey(goods.getId());
		ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
		if (mag == null || sku == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误，没有找到sku或活动商品");
		}
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(sku.getProductId());
		if (productInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品不存在");
		}
		if (goods.getQuantity() != mag.getQuantity()) {
			// 非服务类商品编辑数量时需要校验库存
			if (productInfo.getProductType() != 1) {
				if (goods.getQuantity() - mag.getQuantity() > sku.getSurplusQuantity()) {
					return CommonResult.result(ResultCode.FAIL.getValue(), "商品SKU数量不足");
				}
			}
			int quantity = goods.getQuantity() - mag.getQuantity();
			sku.setProductSaleQuantity(sku.getProductSaleQuantity() + quantity);
			productSkuMapper.updateSelectiveByPrimaryKey(sku);
		}
		activityGoodsMapper.updateSelectiveByPrimaryKey(goods);
		return CommonResult.result(ResultCode.SUCC.getValue(), "编辑成功");
	}

	/**
	 * 删除活动商品
	 */
	@Override
	public CommonResult deleteActivityGoods(String id) {
		MallActivityGoods goods = activityGoodsMapper.selectByPrimaryKey(id);
		if (goods == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "活动商品不存在");
		}
		ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
		if (sku == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品sku不存在");
		}
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(sku.getProductId());
		if (productInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品不存在");
		}
		// 得到要删除这条活动商品的数量和已售数量
		int quantity = goods.getQuantity();// 活动商品数量
		int sellQuantity = goods.getSellQuantity();// 已售数量
		int surplus = quantity - sellQuantity;// 剩余数量
		// 更新商品SKU表中已售数量
		sku.setProductSaleQuantity(sku.getProductSaleQuantity() - surplus);
		productSkuMapper.updateSelectiveByPrimaryKey(sku);
		activityGoodsMapper.deleteByPrimaryKey(id);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除活动商品成功");
	}

	/**
	 * 批量新增活动商品
	 */
	@Override
	public CommonResult insertBatchActivityCoods(JSONArray array) {
		List<MallActivityGoods> list = JSONObject.parseArray(array.toJSONString(), MallActivityGoods.class);
		if (list == null || list.size() <= 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		for (MallActivityGoods goods : list) {
			ProductSku sku = productSkuMapper.selectByPrimaryKey(goods.getSkuId());
			if (sku == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品sku不存在");
			}
			ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(sku.getProductId());
			if (productInfo == null) {
				return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品不存在");
			}
			MallActivityGoods mallActivityGoods = activityGoodsMapper.selectBySkuId(goods.getSkuId());
			if (mallActivityGoods != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(),
						sku.getSkuName() + "商品已添加在其他活动中，不能同时添加到多个活动");
			}
			MallActivityGoods activityGoods = activityGoodsMapper.selectGoodsByActivityIdAndSkuId(goods.getActivityId(),
					goods.getSkuId());
			if (activityGoods != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), sku.getSkuName() + "商品已添加");
			}
			// 非服务类商品校验库存
			if (productInfo.getProductType() != 1) {
				if (goods.getQuantity() > sku.getSurplusQuantity()) {
					return CommonResult.result(ResultCode.FAIL.getValue(), "剩余库存小于" + goods.getQuantity());
				}
			}
			sku.setProductSaleQuantity(sku.getProductSaleQuantity() + goods.getQuantity());
			productSkuMapper.updateSelectiveByPrimaryKey(sku);
		}
		activityGoodsMapper.insertByBatch(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "添加商品成功");
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

	/**
	 * pc：根据活动id查询商品列表
	 */
	@Override
	public CommonResult selectActivityGoodsAndPagedByActivityId(String activityId, Integer pageIndex,
			Integer pageSize) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = activityGoodsMapper.selectGoodsCount(activityId);
		page.setRows(total);
		List<Map<String, String>> list = activityGoodsMapper.selectByActivityId(activityId, page.getStart(),
				page.getPageSize());
		for (Map<String, String> map : list) {
			ProductPic productPic = productPicMapper
					.selectByPrimaryKey(map.get("picId") == null ? "" : map.get("picId"));
			map.put("picBig", productPic == null ? "" : productPic.getPicBig());
		}
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}
}
