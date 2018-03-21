package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.dataSource.DataSource;
import cn.com.clubank.weihang.common.dataSource.DataSourceType;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CustomerDeliveryAddressMapper;
import cn.com.clubank.weihang.manage.member.pojo.CustomerDeliveryAddress;
import cn.com.clubank.weihang.manage.product.dao.ProductFavouriteMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductValueMapper;
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
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
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
	public CommonResult findDetailedInfoBySkuId(String customerId, String skuId) {
		if (StringUtils.isBlank(skuId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "skuId为空");
		}
		Map<String, Object> map = new HashMap<>();
		// 查询活动产品sku信息
		Map<String, String> skuInfo = productSkuMapper.selectProductSkuDetailBySkuId(skuId);
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
	@DataSource(DataSourceType.SLAVE)
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
		String msg = "";
		// 上架
		if (reviewStatus == Constants.INT_2) {
			productInfo.setProductUpTime(new Date());
			msg = "上架成功";
		}
		// 下架
		if (reviewStatus == Constants.INT_3) {
			productInfo.setProductDownTime(new Date());
			msg = "下架成功";
		}
		productInfo.setReviewStatus(reviewStatus);
		productInfoMapper.updateSelectiveByPrimaryKey(productInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
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
}
