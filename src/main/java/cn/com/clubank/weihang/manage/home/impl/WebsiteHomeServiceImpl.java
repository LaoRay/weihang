package cn.com.clubank.weihang.manage.home.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.home.WebsiteHomeService;
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.news.dao.BaseNewsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductReadLogMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * pc端首页
 * 
 * @author LeiQY
 *
 */
@Slf4j
@Service
public class WebsiteHomeServiceImpl implements WebsiteHomeService {

	@Autowired
	private MallActivityMapper mallActivityMapper;
	@Autowired
	private MallActivityGoodsMapper mallActivityGoodsMapper;
	@Autowired
	private ProductPicMapper productPicMapper;
	@Autowired
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private MallAdvertMapper mallAdvertMapper;
	@Autowired
	private ProductReadLogMapper productReadLogMapper;
	@Autowired
	private CouponInventoryMapper couponInventoryMapper;
	@Autowired
	private BaseNewsMapper baseNewsMapper;

	/**
	 * 构建pc首页展示的所有数据
	 */
	@Override
	public CommonResult websiteHomeData(String customerId) {
		log.info("获取pc首页数据：{}", customerId);
		JSONObject result = new JSONObject();

		result.put("topAdvertSection", buildBannerArray("pc_home_top")); // 首页头广告
		result.put("hotSaleSection", buildHotSaleSection(customerId)); // 热销商品--首页
		result.put("news", baseNewsMapper.selectBefore(1, 3)); // 威航快报

		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", result);
	}

	/**
	 * 构建pc商城首页展示的所有数据
	 */
	@Override
	public CommonResult websiteMallHomeData(String customerId) {
		log.info("获取pc商城首页数据：{}", customerId);
		JSONObject result = new JSONObject();

		result.put("topAdvertSection", buildBannerArray("pc_mall_home_top")); // 商城首页头广告
		result.put("news", baseNewsMapper.selectBefore(2, 4)); // 威航公告
		result.put("activitySection", buildActivitySection(customerId)); // 活动
		result.put("hotSaleSection", buildMallHotSaleSection(customerId)); // 热销商品--商城首页
		result.put("pointMallSection", buildPointMallSection()); // 积分商城
		result.put("historySection", buildHistorySection(customerId)); // 猜你想要

		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", result);
	}

	/**
	 * 构建活动商品数据
	 * 
	 * @return
	 */
	private JSONObject buildActivitySection(String customerId) {
		JSONObject result = new JSONObject();
		// 限时抢购sku
		JSONArray lmtTimeBuys = new JSONArray();
		// 约定好限时抢购排序码是001
		MallActivity act = mallActivityMapper.selectFirstByEndTime();
		JSONObject activityInfo = new JSONObject();
		if (act != null) {
			activityInfo.put("activityId", act.getId());
			activityInfo.put("title", act.getTitle());
			activityInfo.put("startTime", act.getBeginTime());
			activityInfo.put("endTime", act.getEndTime());
			activityInfo.put("status", act.getStatus());
			activityInfo.put("grade", act.getGrade());
			activityInfo.put("surplusTime", DateUtil.decrease(act.getBeginTime(), new Date(), act.getEndTime()));
		}

		result.put("activityInfo", activityInfo);
		List<Map<String, String>> lmtTimeBuyMaps = mallActivityGoodsMapper
				.selectByActivityId(act == null ? "" : act.getId(), 0, 4);
		if (lmtTimeBuyMaps != null && lmtTimeBuyMaps.size() > 0) {
			for (Map<String, String> map : lmtTimeBuyMaps) {
				// 获取图片
				ProductPic pic = productPicMapper.selectByPrimaryKey(map.get("picId") == null ? "" : map.get("picId"));
				lmtTimeBuys.add(buildActivityProductJson(map.get("skuName") == null ? "" : map.get("skuName"),
						pic == null ? "" : pic.getPicBig(), map.get("activityId") == null ? "" : map.get("activityId"),
						map.get("productId") == null ? "" : map.get("productId"),
						map.get("skuId") == null ? "" : map.get("skuId"),
						map.get("price") == null ? "" : map.get("price"),
						map.get("skuPrice") == null ? "" : map.get("skuPrice"),
						map.get("attributes") == null ? "" : map.get("attributes"),
						map.get("productType") == null ? 1 : Integer.valueOf(map.get("productType"))));
			}
		}
		result.put("lmtTimeBuy", lmtTimeBuys);

		// 前9个活动商品
		JSONArray benefits = new JSONArray();
		List<Map<String, Object>> benefitsMaps = mallActivityGoodsMapper.selectNewestByNum(9);
		if (benefitsMaps != null && benefitsMaps.size() > 0) {
			for (Map<String, Object> map : benefitsMaps) {
				// 获取图片
				ProductPic pic = productPicMapper
						.selectByPrimaryKey(map.get("picId") == null ? "" : map.get("picId").toString());
				benefits.add(
						buildActivityProductJson(map.get("skuName") == null ? "" : map.get("skuName").toString(),
								pic == null ? "" : pic.getPicBig(),
								map.get("activityId") == null ? "" : map.get("activityId").toString(),
								map.get("productId") == null ? "" : map.get("productId").toString(),
								map.get("skuId") == null ? "" : map.get("skuId").toString(),
								map.get("price") == null ? "" : map.get("price").toString(), map.get("skuPrice") == null
										? "" : map.get("skuPrice").toString(),
						map.get("attributes") == null ? "" : map.get("attributes").toString(),
						map.get("productType") == null ? 1 : Integer.valueOf(map.get("productType").toString())));
			}
		}
		result.put("benefits", benefits);
		return result;
	}

	/**
	 * 构建pc首页热销商品数据
	 * 
	 * @return
	 */
	private JSONObject buildHotSaleSection(String customerId) {
		JSONObject result = new JSONObject();

		// 前4个热销商品
		JSONArray hotSales = new JSONArray();
		List<ProductInfo> hots = productInfoMapper.selectHotProduct(4);
		if (hots != null && hots.size() > 0) {
			for (ProductInfo prod : hots) {
				hotSales.add(buildProductJson(prod.getProductName(),
						productInfoService.getBigPicPath(prod.getProductId()), prod.getProductId(),
						prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(),
						prod.getProductType()));
			}
		}
		result.put("hotSales", hotSales);
		return result;
	}

	/**
	 * 构建pc商城首页热销商品数据
	 * 
	 * @return
	 */
	private JSONObject buildMallHotSaleSection(String customerId) {
		JSONObject result = new JSONObject();

		// 前8个热销商品
		JSONArray hotSales = new JSONArray();
		List<ProductInfo> hots = productInfoMapper.selectHotProduct(8);
		if (hots != null && hots.size() > 0) {
			for (ProductInfo prod : hots) {
				hotSales.add(buildProductJson(prod.getProductName(),
						productInfoService.getBigPicPath(prod.getProductId()), prod.getProductId(),
						prod.getProductPrice() == null ? "" : prod.getProductPrice().toString(),
						prod.getProductType()));
			}
		}
		result.put("hotSales", hotSales);

		// 热销广告
		result.put("banners", buildBannerArray("pc_mall_home_hotSale"));
		return result;
	}

	/**
	 * 构建积分商城数据
	 * 
	 * @return
	 */
	private JSONObject buildPointMallSection() {
		JSONObject result = new JSONObject();
		// 优惠券
		JSONArray couponList = new JSONArray();
		List<Map<String, String>> coupons = couponInventoryMapper.selectValidByType(null, 0, 5); // 优惠券
		if (coupons != null && coupons.size() > 0) {
			for (Map<String, String> coupon : coupons) {
				couponList.add(buildCouponJson(coupon.get("couponName"), coupon.get("picBig"), coupon.get("crId"),
						coupon.get("changeIntegral"), coupon.get("couponType")));
			}
		}
		result.put("couponList", couponList);
		return result;
	}

	/**
	 * 猜你想要
	 * 
	 * @return
	 */
	private JSONObject buildHistorySection(String customerId) {
		JSONObject result = new JSONObject();
		// 浏览记录
		JSONArray historyData = new JSONArray();
		// 查询客户的前10条浏览记录
		List<Map<String, Object>> list = productReadLogMapper.selectByCustomerId(customerId, 0, 10);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				List<ProductPic> pics = productPicMapper.selectByProductId(map.get("productId").toString());
				map.put("picBig", pics.size() > 0 ? pics.get(0).getPicBig() : Constants.NULL);
				historyData.add(buildProductJson(
						map.get("productName") == null ? "" : map.get("productName").toString(),
						map.get("picBig") == null ? "" : map.get("picBig").toString(),
						map.get("productId") == null ? "" : map.get("productId").toString(),
						map.get("productPrice") == null ? "" : map.get("productPrice").toString(),
						map.get("productType") == null ? 1 : Integer.valueOf(map.get("productType").toString())));
			}
		}
		result.put("historyData", historyData);
		return result;
	}

	/**
	 * 构建商品json对象
	 * 
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @return
	 */
	private JSONObject buildProductJson(String title, String imageUrl, String productId, String marketPrice,
			Integer productType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("productId", productId);
		result.put("marketPrice", marketPrice);
		result.put("productType", productType);
		result.put("type", "product");
		return result;
	}

	/**
	 * 构建优惠券json对象
	 * 
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @param couponType
	 *            1服务券2兑换券3抵扣券4折扣券
	 * @return
	 */
	private JSONObject buildCouponJson(String title, String imageUrl, String objId, String marketPrice,
			String couponType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("objId", objId);
		result.put("marketPrice", marketPrice);
		result.put("couponType", couponType);
		result.put("type", "coupon");
		return result;
	}

	/**
	 * 构建活动商品的json对象
	 * 
	 * @param title
	 * @param imageUrl
	 * @param objId
	 * @param marketPrice
	 * @param price
	 * @return
	 */
	private JSONObject buildActivityProductJson(String title, String imageUrl, String activityId, String productId,
			String skuId, String marketPrice, String price, String attributes, Integer productType) {
		JSONObject result = new JSONObject();
		result.put("title", title);
		result.put("imageUrl", imageUrl);
		result.put("activityId", activityId);
		result.put("productId", productId);
		result.put("skuId", skuId);
		result.put("marketPrice", marketPrice);
		result.put("price", price);
		result.put("attributes", attributes);
		result.put("productType", productType);
		result.put("type", "activity");
		return result;
	}

	/**
	 * 构建广告json对象数组
	 * 
	 * @param bannerKey
	 * @return
	 */
	private JSONArray buildBannerArray(String bannerKey) {
		JSONArray banners = new JSONArray();
		List<MallAdvert> adverts = mallAdvertMapper.selectAdvertListByPositionCode(bannerKey);
		if(adverts != null && adverts.size() > 0){
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
		}
		return banners;
	}
}
