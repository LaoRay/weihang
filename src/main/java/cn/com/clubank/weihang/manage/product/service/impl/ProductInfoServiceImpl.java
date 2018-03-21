package cn.com.clubank.weihang.manage.product.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerDeliveryAddressMapper;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import cn.com.clubank.weihang.manage.member.pojo.CouponList;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductFavouriteMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductValueMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.ProductFavourite;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;

/**
 * 产品信息管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

	@Autowired
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private ProductPicMapper productPicMapper;
	@Autowired
	private ProductValueMapper productValueMapper;
	@Autowired
	private ProductFavouriteMapper productFavouriteMapper;
	@Autowired
	private CustomerDeliveryAddressMapper addressMapper;
	@Autowired
	private MallActivityGoodsMapper activityGoodsMapper;
	@Autowired
	private CouponInventoryMapper couponInventoryMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 增加产品信息
	 */
	@Override
	public CommonResult addProductInfo(ProductInfo productInfo) {
		productInfoMapper.insert(productInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "增加产品信息成功");
	}

	/**
	 * 根据产品id查询产品信息
	 */
	@Override
	public CommonResult findProductInfoByProductId(String productId) {
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询产品信息成功", productInfo);
	}

	/**
	 * 根据产品id查询产品详情
	 */
	@Override
	public CommonResult findProductDetailsByProductId(String productId) {
		if (StringUtils.isBlank(productId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		if (productInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "产品信息不存在");
		}
		Map<String, String> detail = new HashMap<String, String>();
		detail.put("detail", productInfo.getProductDetails());
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询产品详情成功", detail);
	}

	/**
	 * 产品审核
	 */
	@Override
	public CommonResult reviewProductByProductId(String productId, Integer reviewStatus) {
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		productInfo.setReviewStatus(reviewStatus);
		productInfoMapper.updateSelectiveByPrimaryKey(productInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "产品审核成功");
	}

	/**
	 * 根据分类id查询更多商品列表
	 */
	@Override
	public CommonResult findMoreProductListByCategoryId(String categoryId, Integer pageIndex, Integer pageSize) {
		List<Map<String, String>> productList = productInfoMapper.selectMoreProductInfoListByCategoryId(categoryId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		for (Map<String, String> map : productList) {
			String productId = map.get("productId");
			List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
			if (productPicList != null && productPicList.size() > 0) {
				map.put("picSmall", productPicList.get(0).getPicSmall());
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询商品列表成功", productList);
	}

	/**
	 * 根据产品id查询产品详细信息(包括产品信息，产品sku列表，产品基本属性信息，图片信息，收藏信息)
	 */
	@Override
	public CommonResult findDetailedInfoByProductId(String customerId, String productId) {
		if (StringUtils.isBlank(productId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "productId为空");
		}
		Map<String, Object> map = new HashMap<>();
		// 查询产品信息
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		map.put("productInfo", productInfo);
		// 查询产品sku列表
		List<ProductSku> productSkuList = productSkuMapper.selectSkuListByProductId(productId);
		map.put("productSkuList", productSkuList);
		// 查询产品图片列表
		List<Map<String, String>> picList = productPicMapper.selectPicListByProductId(productId);
		map.put("picList", picList);
		// 查询产品基本属性
		List<Map<String, Object>> productValueList = productValueMapper.selectAttrListByProductId(productId);
		for (Map<String, Object> attrMap : productValueList) {
			String productAttrId = (String) attrMap.get("productAttrId");
			List<Map<String, String>> attrValueList = productValueMapper
					.selectAttrValueListByProductAttrId(productAttrId);
			attrMap.put("attrValueList", attrValueList);
		}
		map.put("productValueList", productValueList);
		if (!StringUtils.isBlank(customerId)) {
			// 查看该商品是否已收藏
			ProductFavourite productFavourite = productFavouriteMapper
					.selectFavouriteByCustomerIdAndProductId(customerId, productId);
			if (productFavourite == null) {
				map.put("isFavourite", false);
			} else {
				map.put("isFavourite", true);
			}
			CustomerDeliveryAddress deliveryAddress = addressMapper.selectDefaultByCustomerId(customerId);
			map.put("defaultDeliveryAddress", deliveryAddress);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取产品详细信息成功", map);
	}

	/**
	 * 根据产品skuId查询产品详细信息(包括产品信息，产品sku，图片信息)
	 */
	@Override
	public CommonResult findDetailedInfoBySkuId(String customerId, String skuId, String activityId) {
		if (StringUtils.isBlank(skuId) || StringUtils.isBlank(activityId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		Map<String, Object> map = new HashMap<>();
		// 查询活动产品sku信息
		Map<String, String> skuInfo = productSkuMapper.selectProductSkuDetailBySkuIdAndActivityId(skuId, activityId);
		// 从缓存中读取活动商品库存
		String surplusQuantity = jedisClient.get(map.get("activityId") + ":" + map.get("goodsId"));
		if (StringUtils.isNotBlank(surplusQuantity)) {
			skuInfo.put("surplusQuantity", surplusQuantity);
			Integer productSaleQuantity = Integer.parseInt(skuInfo.get("productQuantity"))
					- Integer.parseInt(surplusQuantity);
			skuInfo.put("productSaleQuantity", productSaleQuantity.toString());
		}
		map.put("skuInfo", skuInfo);
		// 查询产品信息
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(skuInfo.get("productId"));
		map.put("productInfo", productInfo);
		// 查询产品图片列表
		List<Map<String, String>> picList = productPicMapper.selectPicListByProductId(skuInfo.get("productId"));
		map.put("picList", picList);
		if (!StringUtils.isBlank(customerId)) {
			CustomerDeliveryAddress deliveryAddress = addressMapper.selectDefaultByCustomerId(customerId);
			map.put("defaultDeliveryAddress", deliveryAddress);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取产品详细信息成功", map);
	}

	/**
	 * 后台：分页查询产品列表(带条件)
	 */
	@Override
	public CommonResult clientFindProductList(Integer pageIndex, Integer pageSize, String productName,
			Integer productType, String categoryId, Integer reviewStatus) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);
		int total = productInfoMapper.selectProductCount(productName, productType, categoryId, reviewStatus);
		List<Map<String, Object>> list = productInfoMapper.selectProductList(page.getStart(), page.getPageSize(),
				productName, productType, categoryId, reviewStatus);
		page.setRows(total);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "商品列表查询成功", page);
	}

	/**
	 * 后台：商品上架下架
	 */
	@Override
	public CommonResult clientReviewProduct(String productId, Integer reviewStatus) {
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		if (productInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "商品不存在");
		}
		List<ProductSku> list = productSkuMapper.selectSkuListByProductId(productId);
		String msg = "";
		// 上架
		if (reviewStatus == Constants.INT_2) {
			if (list == null || list.size() <= 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "该商品还未添加sku，不能上架");
			}
			for (ProductSku sku : list) {
				if (sku.getProductPrice() == null || sku.getProductPrice1() == null || sku.getProductPrice2() == null
						|| sku.getProductPrice3() == null || sku. getProductQuantity() == null) {
					return CommonResult.result(ResultCode.FAIL.getValue(), sku.getSkuName() + " 还未设置价格或库存，不能上架");
				}
				if (sku.getProductPrice().compareTo(new BigDecimal(0)) <= 0
						|| sku.getProductPrice1().compareTo(new BigDecimal(0)) <= 0
						|| sku.getProductPrice2().compareTo(new BigDecimal(0)) <= 0
						|| sku.getProductPrice3().compareTo(new BigDecimal(0)) <= 0) {
					return CommonResult.result(ResultCode.FAIL.getValue(), sku.getSkuName() + " 设置的价格必须大于0");
				}
			}
			productInfo.setProductDownTime(null);
			productInfo.setProductUpTime(new Date());
			msg = "上架成功";
		}
		// 下架
		else if (reviewStatus == Constants.INT_3) {
			// 下架前判断商品是否绑定优惠券
			List<CouponInventory> list1 = couponInventoryMapper.selectByProductId(productId);
			if (list1 != null && list1.size() > 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(),
						productInfo.getProductName() + " 商品已添加优惠券中，暂时不能下架");
			}
			List<CouponList> list3 = couponListMapper.selectByProductId(productId);
			if (list3 != null && list3.size() > 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(),
						productInfo.getProductName() + " 商品已添加优惠券中，暂时不能下架");
			}
			if (list != null && list.size() > 0) {
				for (ProductSku sku : list) {
					// 下架前判断sku是否在活动中
					MallActivityGoods activityGoods = activityGoodsMapper.selectBySkuId(sku.getSkuId());
					if (activityGoods != null) {
						return CommonResult.result(ResultCode.FAIL.getValue(), sku.getSkuName() + " 商品已添加到活动中，暂时不能下架");
					}
					// 下架前判断sku是否绑定优惠券
					List<CouponInventory> list2 = couponInventoryMapper.selectBySkuId(sku.getSkuId());
					if (list2 != null && list2.size() > 0) {
						return CommonResult.result(ResultCode.FAIL.getValue(), sku.getSkuName() + " 商品已添加优惠券中，暂时不能下架");
					}
					List<CouponList> list4 = couponListMapper.selectBySkuId(sku.getSkuId());
					if (list4 != null && list4.size() > 0) {
						return CommonResult.result(ResultCode.FAIL.getValue(), sku.getSkuName() + " 商品已添加优惠券中，暂时不能下架");
					}
				}
			}
			productInfo.setProductDownTime(new Date());
			productInfo.setProductUpTime(null);
			msg = "下架成功";
		}
		// 异常状态
		else {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		productInfo.setReviewStatus(reviewStatus);
		productInfoMapper.updateByPrimaryKey(productInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 后台：删除下架商品
	 */
	@Override
	public CommonResult clientDeleteProduct(String productId) {
		if (StringUtils.isBlank(productId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		productInfoMapper.deleteByPrimaryKey(productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public String selectProductListPaged(String categoryId, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = productInfoMapper.selectSumByCategoryId(categoryId);
		List<Map<String, Object>> list = productInfoMapper.selectProductListPaged(categoryId, page.getStart(),
				pageSize);
		page.setRows(total);
		page.setDataList(list);

		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getOnePicPath(String productId) {
		List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
		if (productPicList != null && productPicList.size() > 0) {
			return productPicList.get(0).getPicSmall();
		}
		return null;
	}

	@Override
	public String getBigPicPath(String productId) {
		List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
		if (productPicList != null && productPicList.size() > 0) {
			return productPicList.get(0).getPicBig();
		}
		return null;
	}

	/**
	 * 后台：根据产品id查询产品信息和sku列表信息
	 */
	@Override
	public CommonResult clientFindProductInfoByProductId(String productId) {
		Map<String, Object> map = new HashMap<>();
		// 查询产品信息
		Map<String, String> productInfo = productInfoMapper.selectProductByProductId(productId);
		map.put("productInfo", productInfo);
		// 查询产品sku列表
		List<Map<String, String>> productSkuList = productSkuMapper.selectSkuInfoByProductId(productId);
		map.put("productSkuList", productSkuList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取产品详细信息成功", map);
	}

	/**
	 * 根据产品id查询产品信息
	 */
	@Override
	public CommonResult clientFindProductByProductId(String productId) {
		Map<String, String> map = productInfoMapper.selectProductByProductId(productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 根据分类查询商品列表
	 */
	@Override
	public CommonResult websiteFindProductListByCategoryId(String categoryId, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = productInfoMapper.selectSumByCategoryId(categoryId);
		page.setRows(total);
		List<Map<String, String>> list = productInfoMapper.selectMoreProductInfoListByCategoryId(categoryId,
				page.getStart(), page.getPageSize());
		for (Map<String, String> map : list) {
			String productId = map.get("productId");
			List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
			if (productPicList != null && productPicList.size() > 0) {
				map.put("picBig", productPicList.get(0).getPicBig());
			}
		}
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}
}
