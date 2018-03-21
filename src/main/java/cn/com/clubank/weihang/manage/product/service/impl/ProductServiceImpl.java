package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrValueMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductValueMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.pojo.ProductValue;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;
import cn.com.clubank.weihang.manage.product.service.ProductService;
import cn.com.clubank.weihang.manage.product.service.ProductValueService;
import lombok.extern.slf4j.Slf4j;

/**
 * 产品管理
 * 
 * @author LeiQY
 *
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private ProductValueMapper productValueMapper;
	@Autowired
	private ProductPicMapper productPicMapper;
	@Autowired
	private ProductAttrMapper productAttrMapper;
	@Autowired
	private ProductAttrValueMapper productAttrValueMapper;
	@Autowired
	private ProductValueService productValueService;
	@Autowired
	private ProductInfoService productInfoService;

	/**
	 * 新增商品
	 */
	@Override
	public CommonResult addNewProduct(JSONObject json) {
		try {
			// 添加产品信息
			ProductInfo productInfo = json.getObject("productInfo", ProductInfo.class);
			productInfoMapper.insert(productInfo);
			// 添加产品sku
			JSONArray skuArray = json.getJSONArray("productSkuList");
			List<ProductSku> skuList = JSONObject.parseArray(skuArray.toJSONString(), ProductSku.class);
			for (ProductSku productSku : skuList) {
				productSku.setProductId(productInfo.getProductId());
			}
			productSkuMapper.insertBatch(skuList);
			// 添加产品属性
			JSONArray valueArray = json.getJSONArray("productValue");
			List<ProductValue> valueList = JSONObject.parseArray(valueArray.toJSONString(), ProductValue.class);
			for (ProductValue productValue : valueList) {
				productValue.setProductId(productInfo.getProductId());
			}
			productValueMapper.insertBatch(valueList);
			// 添加图片信息
			JSONArray picArray = json.getJSONArray("productPic");
			List<ProductPic> picList = JSONObject.parseArray(picArray.toJSONString(), ProductPic.class);
			for (ProductPic productPic : picList) {
				productPic.setProductId(productInfo.getProductId());
			}
			productPicMapper.insertBatch(picList);
			return CommonResult.result(ResultCode.SUCC.getValue(), "新增商品成功");
		} catch (Exception e) {
			log.error("服务器内部错误，商品添加失败");
			throw e;
		}
	}

	@Override
	public CommonResult addOrUpdateProductSkuInfo(JSONObject json) {
		// 新增产品基本属性
		JSONArray valueArray = json.getJSONArray("productValue");
		List<ProductValue> valueList = JSONObject.parseArray(valueArray.toJSONString(), ProductValue.class);
		if (valueList != null && valueList.size() > 0 && !StringUtils.isBlank(valueList.get(0).getProductId())) {
			productValueMapper.deleteByProductId(valueList.get(0).getProductId());
			productValueMapper.insertBatch(valueList);
		} else {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		// 新增或修改sku
		JSONArray skuInfoArray = json.getJSONArray("productSkuInfo");
		List<JSONObject> objectList = JSONObject.parseArray(skuInfoArray.toJSONString(), JSONObject.class);
		if (objectList != null && objectList.size() > 0) {
			for (JSONObject jsonObject : objectList) {
				ProductPic productPic = JSONObject.toJavaObject(jsonObject, ProductPic.class);
				ProductSku productSku = JSONObject.toJavaObject(jsonObject, ProductSku.class);
				if (productPic != null && productSku != null) {
					ProductPic pic = productPicMapper.selectBySmallAndBig(productPic.getPicSmall(),
							productPic.getPicBig());
					if (pic == null) {
						productPicMapper.insert(productPic);
					} else {
						productSku.setProductPicId(pic.getProductPicId());
					}
					if (StringUtils.isBlank(productSku.getSkuId())) {
						productSkuMapper.insert(productSku);
					} else {
						productSkuMapper.updateSelectiveByPrimaryKey(productSku);
					}
					return CommonResult.result(ResultCode.SUCC.getValue(), "成功");
				}
			}
		}
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
	}

	@Override
	public CommonResult modifyProduct(ProductInfo productInfo) {
		String msg = "";
		if (productInfo != null && !StringUtils.isBlank(productInfo.getProductId())) {
			productInfoMapper.updateSelectiveByPrimaryKey(productInfo);
			msg = "更新成功";
		} else {
			msg = "参数错误";
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 按照产品名称模糊查询产品列表--app
	 */
	@Override
	public CommonResult productFindListByProductName(Integer pageIndex, Integer pageSize, String productName) {
		List<Map<String, String>> list = productInfoMapper.selectListByProductName(
				PageObject.getStart(pageIndex, pageSize), pageSize == null ? 10 : pageSize, productName);
		if (list == null || list.size() <= 0) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "对不起，查不到您要找的商品");
		}
		for (Map<String, String> map : list) {
			String picSmall = productInfoService.getOnePicPath(map.get("productId"));
			map.put("picSmall", picSmall);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询商品信息成功", list);
	}

	/**
	 * 按照产品名称模糊查询产品列表--pc
	 */
	@Override
	public CommonResult websiteFindListByProductName(Integer pageIndex, Integer pageSize, String productName) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = productInfoMapper.selectProductCount(productName, null, null, null);
		page.setRows(total);
		List<Map<String, String>> list = productInfoMapper.selectListByProductName(page.getStart(), page.getPageSize(),
				productName);
		if (list == null || list.size() <= 0) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "对不起，查不到您要找的商品");
		}
		for (Map<String, String> map : list) {
			String picBig = productInfoService.getBigPicPath(map.get("productId"));
			map.put("picBig", picBig);
		}
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询商品信息成功", page);
	}

	/**
	 * 查询属性列表及sku列表
	 */
	@Override
	public CommonResult clientFindAttrAndSkuInfoList(String categoryId, String productId) {
		List<ProductAttr> attrList = productAttrMapper.selectListByCategoryId(categoryId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		if (attrList != null && attrList.size() > 0) {
			for (ProductAttr productAttr : attrList) {
				Map<String, Object> attrMap = new HashMap<String, Object>();
				attrMap.put("productAttr", productAttr);
				List<ProductAttrValue> attrValueList = productAttrValueMapper
						.selectListByAttrId(productAttr.getAttrId());
				attrMap.put("productAttrValueList", attrValueList);
				/*
				 * List<Map<String, String>> productValueList =
				 * productValueMapper.selectAttrValueListByProductAttrId(
				 * productAttr.getAttrId()); attrMap.put("productValueList",
				 * productValueList);
				 */
				list.add(attrMap);
			}
		}
		map.put("attrList", list);
		List<Map<String, String>> skuInfoList = productSkuMapper.selectSkuInfoByProductId(productId);
		map.put("skuInfoList", skuInfoList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 根据skuId查询sku信息
	 */
	@Override
	public CommonResult clientFindSkuInfoBySkuId(String skuId) {
		Map<String, String> map = productSkuMapper.selectSkuInfoByPrimaryKey(skuId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 更新sku信息
	 */
	@Override
	public CommonResult clientUpdateSkuInfoBySkuId(JSONObject json) {
		ProductSku productSku = JSONObject.toJavaObject(json, ProductSku.class);
		if (productSku == null || StringUtils.isBlank(productSku.getSkuId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数有误");
		}
		productSkuMapper.updateSelectiveByPrimaryKey(productSku);
		ProductPic productPic = JSONObject.toJavaObject(json, ProductPic.class);
		if (productPic == null || StringUtils.isBlank(productPic.getProductPicId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数有误");
		}
		productPicMapper.updateSelectiveByPrimaryKey(productPic);
		return CommonResult.result(ResultCode.SUCC.getValue(), "更新成功");
	}

	@Override
	public CommonResult clientUpdateSkuPriceQuantity(JSONObject json) {
		ProductSku productSku = productSkuMapper.selectByPrimaryKey(json.getString("skuId"));
		if (productSku == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数有误");
		}
		productSku.setProductPrice(json.getBigDecimal("productPrice1"));
		productSku.setProductPrice1(json.getBigDecimal("productPrice1"));
		productSku.setProductPrice2(json.getBigDecimal("productPrice2"));
		productSku.setProductPrice3(json.getBigDecimal("productPrice3"));
		productSku.setProductQuantity(json.getInteger("productQuantity"));
		productSkuMapper.updateSelectiveByPrimaryKey(productSku);
		return CommonResult.result(ResultCode.SUCC.getValue(), "更新成功");
	}

	@Override
	public CommonResult addProductSkuInfo(JSONObject json) {
		log.info("新增商品sku信息：{}", json.toString());
		ProductInfo product = productInfoMapper.selectByPrimaryKey(json.getString("productId"));
		if (product == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "产品数据不存在");
		}
		// 构建数组list，进行遍历
		List<String[]> list = new ArrayList<String[]>();
		String[] vidsStart = null; // 定义第一个数组，遍历使用

		JSONArray values = json.getJSONArray("values");
		for (int i = 0; i < values.size(); i++) {
			JSONArray value = values.getJSONArray(i);
			// 一个规格一个数组，数组里面是属性值的id
			String[] vids = new String[value.size()];
			for (int j = 0; j < value.size(); j++) {
				JSONObject obj = value.getJSONObject(j);
				vids[j] = obj.getString("vId");
			}
			if (i == 0) {
				vidsStart = vids;
			}
			list.add(vids);
		}

		// 遍历结果，一个list算是一个sku，里面是属性值ids，逗号间隔
		List<String> result = new ArrayList<String>();
		handle(list, vidsStart, "", result);

		// 保存图片
		ProductPic pic = new ProductPic();
		if (StringUtil.isNotBlank(json.getString("bigImage")) || StringUtil.isNotBlank(json.getString("smallImage"))) {
			pic.setPicBig(json.getString("bigImage"));
			pic.setPicSmall(json.getString("smallImage"));
			pic.setProductId(product.getProductId());
			productPicMapper.insert(pic);
		}

		// 保存sku
		ProductSku sku = null;
		for (String attrValueIds : result) {
			// 根据属性ids判断是否存在sku，如果不存在则新增
			if (!checkSkuIsExist(product.getProductId(), attrValueIds)) {
				sku = new ProductSku();
				sku.setAttributesId(attrValueIds);
				sku.setProductId(product.getProductId());
				sku.setProductPicId(pic.getProductPicId());
				String attributes = ""; // 属性值集合
				String skuName = "";
				for (String attrValueId : attrValueIds.split(",")) {
					ProductAttrValue attrValue = productAttrValueMapper.selectByPrimaryKey(attrValueId);
					ProductAttr attr = productAttrMapper
							.selectByPrimaryKey(attrValue == null ? "" : attrValue.getAttrId());
					// 构建属性名称集合和sku名称
					attributes += attr.getAttrName() + ":" + attrValue.getValueName() + ",";
					skuName += attrValue.getValueName() + " ";
					// 保存产品的属性
					productValueService.addProductValue(product.getProductId(), attrValueId);
				}
				sku.setAttributes(attributes.length() > 0 ? attributes.substring(0, attributes.length() - 1) : "");
				sku.setSkuName(product.getProductName() + " "
						+ (skuName.length() > 0 ? skuName.substring(0, skuName.length() - 1) : ""));
				productSkuMapper.insert(sku);
			}
		}

		return new CommonResult(ResultCode.SUCC.getValue(), "保存成功", result);
	}

	/**
	 * 根据属性ids判断是否存在sku
	 * 
	 * @param productId
	 * @param attrValueIds
	 * @return
	 */
	private boolean checkSkuIsExist(String productId, String attrValueIds) {
		// 根据商品id和属性ids数组查询sku：查询属性值包含attrValueIds的sku
		List<ProductSku> list = productSkuMapper.selectByProductIdAndAttrValueIds(productId, attrValueIds.split(","));
		for (ProductSku sku : list) {
			if (sku.getAttributesId().split(",").length == attrValueIds.split(",").length) {
				// 如果有查询到sku，并且有sku的属性值和传递过来的属性值一样，则判断为存在
				return true;
			}
		}
		// 如果没有查询到数据则不存在
		return false;
	}

	/**
	 * 递归处理-处理属性值的排列组合
	 * 
	 * @param resource
	 * @param start
	 * @param str
	 * @param result
	 */
	private void handle(List<String[]> resource, String[] start, String str, List<String> result) {
		for (int i = 0; i < resource.size(); i++) {
			// 取得当前的数组
			if (i == resource.indexOf(start)) {
				// 迭代数组
				for (String st : start) {
					st = str + st + ",";
					if (i < resource.size() - 1) {
						handle(resource, resource.get(i + 1), st, result);
					} else if (i == resource.size() - 1) {
						// 去掉最后一位逗号
						if (st.length() > 0 && st.endsWith(",")) {
							result.add(st.substring(0, st.length() - 1));
						} else {
							result.add(st);
						}
					}
				}
			}
		}
	}

	/**
	 * 按照类别key查询产品列表
	 */
	@Override
	public CommonResult productFindListByCategoryKey(Integer pageIndex, Integer pageSize, String categoryKey) {
		List<Map<String, String>> list = productInfoMapper.selectByCategoryKey(PageObject.getStart(pageIndex, pageSize),
				pageSize, categoryKey);
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				String picSmall = productInfoService.getOnePicPath(map.get("productId"));
				map.put("picSmall", picSmall);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询商品信息成功", list);
	}
}
