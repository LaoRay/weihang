package cn.com.clubank.weihang.manage.home.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.home.AppHomePageService;
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.news.dao.BaseNewsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductCategoryMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductReadLogMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;
import cn.com.clubank.weihang.manage.product.pojo.ProductCategory;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * app端首页数据
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class AppHomePageServiceImpl implements AppHomePageService {

	@Resource
	private MallActivityMapper mallActivityMapper;
	
	@Resource
	private MallActivityGoodsMapper mallActivityGoodsMapper;
	
	@Resource
	private ProductPicMapper productPicMapper;
	
	@Resource
	private ProductInfoMapper productInfoMapper;
	
	@Resource
	private ProductInfoService productInfoService;
	
	@Resource
	private ProductCategoryMapper productCategoryMapper;
	
	@Resource
	private MallAdvertMapper mallAdvertMapper;
	
	@Resource
	private ProductReadLogMapper productReadLogMapper;
	
	@Resource
	private CouponInventoryMapper couponInventoryMapper;
	
	@Resource
	private BaseNewsMapper baseNewsMapper;
	
	@Override
	public CommonResult appHomeData(String customerId, Integer type) {
		log.info("获取首页数据：{}", customerId);
		JSONObject result = new JSONObject();
		if (type == Constants.INT_1) {
			//app首页有活动数据
			result.put("activitySection", buildActivitySection()); //活动
		}
		result.put("pointMallSection", buildPointMallSection()); //积分商城
		result.put("carCareSection", buildCarCareSection()); //爱车护理
		result.put("historySection", buildHistorySection(customerId)); //猜你想要
		
//		result.put("benifitsSection", null); //福利特惠 - 二期
//		result.put("lifeMarketSection", null); //便捷生活 - 二期
//		result.put("smartLifeSection", null); //智能家居 - 二期
//		result.put("rcmtSection", buildRcmtSection()); //为你推荐 - 二期
		
		
		result.put("news", baseNewsMapper.selectBefore(null, 5)); //威航快报
		result.put("registerBanners", buildBannerArray("app_home_register")); //注册位置广告
		
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", result);
	}

	/**
	 * 构建活动商品数据
	 * @return
	 */
	private JSONObject buildActivitySection() {
		JSONObject result = new JSONObject();
		//限时抢购sku
		JSONArray lmtTimeBuys = new JSONArray();
		// 约定好限时抢购排序码是001
		MallActivity act = mallActivityMapper.selectFirstByEndTime();
		List<Map<String, String>> lmtTimeBuyMaps = mallActivityGoodsMapper.selectByActivityId(act == null ? "" : act.getId(), 0, 5);
		for (Map<String, String> map : lmtTimeBuyMaps) {
			//获取图片
			ProductPic pic = productPicMapper.selectByPrimaryKey(map.containsKey("picId") ? map.get("picId") : "");
			lmtTimeBuys.add(buildActivitySkuJson(map.containsKey("skuName") ? map.get("skuName") : "", pic == null ? "" : pic.getPicSmall(), map.containsKey("skuId") ? map.get("skuId") : "", act.getId(), map.containsKey("price") ? map.get("price") : "", map.containsKey("skuPrice") ? map.get("skuPrice") : "", map.containsKey("endTime") ? map.get("endTime") : "", map.containsKey("productType") ? Integer.valueOf(map.get("productType")) : 1));
		}
		result.put("lmtTimeBuy", lmtTimeBuys);
		
		// 前两个活动商品
		JSONArray benefits = new JSONArray();
		List<Map<String, Object>> benefitsMaps = mallActivityGoodsMapper.selectNewestByNum(2);
		for (Map<String, Object> map : benefitsMaps) {
			//获取图片
			ProductPic pic = productPicMapper.selectByPrimaryKey(map.containsKey("picId") ? map.get("picId").toString() : "");
			benefits.add(buildActivityProductJson(map.containsKey("skuName") ? map.get("skuName").toString() : "", pic == null ? "" : pic.getPicSmall(), map.containsKey("activityId") ? map.get("activityId").toString() : "", map.containsKey("skuId") ? map.get("skuId").toString() : "", map.containsKey("price") ? map.get("price").toString() : "", map.containsKey("skuPrice") ? map.get("skuPrice").toString() : "", map.containsKey("productType") ? Integer.valueOf(map.get("productType").toString()) : 1));
		}
		result.put("benefits", benefits);
		
		//前5个热销商品
		JSONArray hotSales = new JSONArray();
		List<ProductInfo> hots = productInfoMapper.selectHotProduct(2);
		for (ProductInfo prod : hots) {
			hotSales.add(buildProductJson(prod.getProductName(), productInfoService.getOnePicPath(prod.getProductId()), prod.getProductId(), prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(), prod.getProductType()));
		}
		result.put("hotSales", hotSales);
		
		//广告
		result.put("banners", buildBannerArray("app_home_activity"));
		
		return result;
	}
	
	/**
	 * 构建积分商城数据
	 * @return
	 */
	private JSONObject buildPointMallSection() {
		JSONObject result = new JSONObject();
		//折扣-优惠券
		JSONArray discount = new JSONArray();
		List<Map<String, String>> coupons = couponInventoryMapper.selectValidByType(4, 0, 2); //优惠券
		for (Map<String, String> coupon : coupons) {
			discount.add(buildCouponJson(coupon.get("couponName"), coupon.get("picSmall"), coupon.get("crId"), ""));
		}
		result.put("discount", discount);
		
		//服务商品-查询服务类商品 TODO 应该是积分商城的商品
		/*JSONArray services = new JSONArray();
		List<Map<String, Object>> servicesMaps = productInfoMapper.selectByProductType(2, 0, 2);
		for (Map<String, Object> servicesMap : servicesMaps) {
			String productId = servicesMap.containsKey("productId") ? servicesMap.get("productId").toString() : "";
			services.add(buildProductJson(servicesMap.containsKey("productName") ? servicesMap.get("productName").toString() : "", productInfoService.getOnePicPath(productId), productId, servicesMap.containsKey("productPrice") ? servicesMap.get("productPrice").toString() : "", servicesMap.containsKey("productType") ? Integer.valueOf(servicesMap.get("productType").toString()) : 1));
		}
		result.put("services", services);*/
		
		//实物商品 -新的四个商品 TODO 应该是积分商城的商品
		/*JSONArray substance = new JSONArray();
		List<Map<String, Object>> substanceMaps = productInfoMapper.selectByProductType(1, 0, 4);
		for (Map<String, Object> substanceMap : substanceMaps) {
			String productId = substanceMap.containsKey("productId") ? substanceMap.get("productId").toString() : "";
			substance.add(buildProductJson(substanceMap.containsKey("productName") ? substanceMap.get("productName").toString() : "", productInfoService.getOnePicPath(productId), productId, substanceMap.containsKey("productPrice") ? substanceMap.get("productPrice").toString() : "", substanceMap.containsKey("productType") ? Integer.valueOf(substanceMap.get("productType").toString()) : 1));
		}
		result.put("substance", substance);*/
		
		//广告
		result.put("banners", buildBannerArray("app_home_pointMall"));
		return result;
	}
	
	/**
	 * 构建爱车护理数据
	 * @return
	 */
	private JSONObject buildCarCareSection() {
		JSONObject result = new JSONObject();
		JSONArray categorys = new JSONArray();
		JSONObject category = null; //每个类别的json数据
		//查询服务类的子分类
		List<ProductCategory> categoryList = productCategoryMapper.selectByParentKey("CarService", 4);
		for (ProductCategory categoryInfo : categoryList) {
			category = new JSONObject();
			JSONArray products = new JSONArray(); //每个类别的商品列表
			List<Map<String, Object>> productList = productInfoMapper.selectProductListPaged(categoryInfo.getCategoryId(), 0, 2);
			for (Map<String, Object> map : productList) {
				String productId = map.containsKey("productId") ? map.get("productId").toString() : "";
				products.add(buildProductJson(map.containsKey("productName") ? map.get("productName").toString() : "", productInfoService.getOnePicPath(productId), productId, map.containsKey("productPrice") ? map.get("productPrice").toString() : "", map.containsKey("productType") ? Integer.valueOf(map.get("productType").toString()) : 1));
			}
			category.put("products", products);
			category.put("categoryName", categoryInfo.getCname());
			categorys.add(category);
		}
		result.put("categorys", categorys);
		//广告
		result.put("banners", buildBannerArray("app_home_carCare"));
		return result;
	}
	
	/**
	 * 构建为你推荐数据
	 * @return
	 */
	@SuppressWarnings("unused")
	private JSONObject buildRcmtSection() {
		JSONObject result = new JSONObject();
		//销量排行
		JSONArray saledRank = new JSONArray();
		List<ProductInfo> hots = productInfoMapper.selectHotProduct(2);
		for (ProductInfo prod : hots) {
			saledRank.add(buildProductJson(prod.getProductName(), productInfoService.getOnePicPath(prod.getProductId()), prod.getProductId(), prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(), prod.getProductType()));
		}
		result.put("saledRank", saledRank);
		
		//TODO 精选商品
		JSONArray choosenProducts = new JSONArray();
		List<ProductInfo> choosenProductMaps = productInfoMapper.selectByRecommendType(-1, 0, 2);
		for (ProductInfo prod : choosenProductMaps) {
			choosenProducts.add(buildProductJson(prod.getProductName(), productInfoService.getOnePicPath(prod.getProductId()), prod.getProductId(), prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(), prod.getProductType()));
		}
		result.put("choosenProducts", choosenProducts);
		
		//TODO 新品上市
		JSONArray newerSaling = new JSONArray();
		List<ProductInfo> newerSalingMaps = productInfoMapper.selectByRecommendType(-1, 0, 2);
		for (ProductInfo prod : newerSalingMaps) {
			newerSaling.add(buildProductJson(prod.getProductName(), productInfoService.getOnePicPath(prod.getProductId()), prod.getProductId(), prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(), prod.getProductType()));
		}
		result.put("newerSaling", newerSaling);
		
		//TODO 低价超值
		JSONArray superValue = new JSONArray();
		List<ProductInfo> superValueMaps = productInfoMapper.selectByRecommendType(-1, 0, 2);
		for (ProductInfo prod : superValueMaps) {
			superValue.add(buildProductJson(prod.getProductName(), productInfoService.getOnePicPath(prod.getProductId()), prod.getProductId(), prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(), prod.getProductType()));
		}
		result.put("superValue", superValue);
		
		//广告
		result.put("banners", buildBannerArray("app_home_recommend"));
		return result;
	}
	
	/**
	 * 猜你想要
	 * @return
	 */
	private JSONObject buildHistorySection(String customerId) {
		JSONObject result = new JSONObject();
		//浏览记录
		JSONArray historyData = new JSONArray();
		//查询客户的前6条浏览记录
		List<Map<String,Object>> list = productReadLogMapper.selectByCustomerId(customerId, 0, 6);
		for (Map<String,Object> map : list) {
			List<ProductPic> pics = productPicMapper.selectByProductId(map.get("productId").toString());
			map.put("picSmall", pics.size() > 0 ? pics.get(0).getPicSmall() : Constants.NULL);
			historyData.add(buildProductJson(map.containsKey("productName") ? map.get("productName").toString() : "", map.containsKey("picSmall") ? map.get("picSmall").toString() : "", map.containsKey("productId") ? map.get("productId").toString() : "", map.containsKey("productPrice") ? map.get("productPrice").toString() : "", map.containsKey("productType") ? Integer.valueOf(map.get("productType").toString()) : 1));
		}
		result.put("historyData", historyData);
		return result;
	}
	
	/**
	 * 构建商品json对象
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @return
	 */
	private JSONObject buildProductJson(String title, String imageUrl, String objId, String marketPrice, Integer productType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("objId", objId);
		result.put("marketPrice", marketPrice);
		result.put("type", "product");
		result.put("productType", productType);
		return result;
	}
	
	/**
	 * 构建优惠券json对象
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @return
	 */
	private JSONObject buildCouponJson(String title, String imageUrl, String objId, String marketPrice) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("objId", objId);
		result.put("marketPrice", marketPrice);
		result.put("type", "coupon");
		return result;
	}
	
	/**
	 * 构建sku的json对象
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @param price
	 * @return
	 */
	private JSONObject buildActivitySkuJson(String title, String imageUrl, String objId, String activityId, String marketPrice, String price, String endTime, Integer productType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("objId", objId);
		result.put("activityId", activityId);
		result.put("marketPrice", marketPrice);
		result.put("price", price);
		result.put("type", "sku");
		result.put("endTime", endTime);
		result.put("productType", productType);
		return result;
	}
	
	/**
	 * 构建活动商品的json对象
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @param price
	 * @return
	 */
	private JSONObject buildActivityProductJson(String title, String imageUrl, String activityId, String objId, String marketPrice, String price, Integer productType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("objId", objId);
		result.put("marketPrice", marketPrice);
		result.put("price", price);
		result.put("activityId", activityId);
		result.put("type", "activity");
		result.put("productType", productType);
		return result;
	}
	
	/**
	 * 构建广告json对象数组
	 * @param bannerKey
	 * @return
	 */
	private JSONArray buildBannerArray(String bannerKey) {
		JSONArray banners = new JSONArray();
		List<MallAdvert> adverts = mallAdvertMapper.selectAdvertListByPositionCode(bannerKey);
		for (MallAdvert advert : adverts) {
			JSONObject result = new JSONObject();
			result.put("id", advert.getId());
			result.put("title", advert.getAdName());
			result.put("imageUrl", advert.getImg());
			result.put("objId", StringUtil.isBlank(advert.getObjId()) ? Constants.NULL : advert.getObjId());
			result.put("link", StringUtil.isBlank(advert.getAdLink()) ? Constants.NULL : advert.getAdLink());
			result.put("advertType", advert.getAdType());
			result.put("type", "advert");
			banners.add(result);
		}
		return banners;
	}
	
}
