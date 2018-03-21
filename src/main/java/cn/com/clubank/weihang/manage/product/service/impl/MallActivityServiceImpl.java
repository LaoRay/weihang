package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.MallActivityService;

/**
 * 活动商城
 * 
 * @author LeiQY
 *
 */
@Service
public class MallActivityServiceImpl implements MallActivityService {

	@Autowired
	private MallActivityMapper mallActivityMapper;
	@Autowired
	private MallActivityGoodsMapper mallActivityGoodsMapper;
	@Autowired
	private ProductPicMapper productPicMapper;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 查询活动列表及对应的活动商品信息
	 */
	@Override
	public CommonResult findActivityAndProduct() {
		List<MallActivity> mallActivityList = mallActivityMapper.selectValidActivityList();
		List<Object> list = new ArrayList<>();
		for (MallActivity mallActivity : mallActivityList) {
			List<Map<String, String>> mallActivityGoodList = mallActivityGoodsMapper
					.selectByActivityId(mallActivity.getId(), 0, 10);
			if (mallActivityGoodList == null || mallActivityGoodList.size() <= 0) {
				continue;
			}
			for (Map<String, String> map : mallActivityGoodList) {
				String productId = map.get("productId");
				// 根据商品id查询图片
				List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
				if (productPicList != null && productPicList.size() > 0) {
					map.put("picSmall", productPicList.get(0).getPicSmall());
					map.put("picBig", productPicList.get(0).getPicBig());
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mallActivity", mallActivity);
			map.put("mallActivityGoodList", mallActivityGoodList);
			list.add(map);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询活动及活动商品列表成功", list);
	}

	/**
	 * 根据活动id查询活动商品列表
	 */
	@Override
	public CommonResult findActivityProductListByActivityId(String activityId, Integer pageIndex, Integer pageSize) {
		List<Map<String, String>> mallActivityGoodList = mallActivityGoodsMapper.selectByActivityId(activityId,
				PageObject.getStart(pageIndex, pageSize), pageSize == null ? 10 : pageSize);
		for (Map<String, String> map : mallActivityGoodList) {
			String productId = map.get("productId");
			// 根据商品id查询图片
			List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
			if (productPicList != null && productPicList.size() > 0) {
				map.put("picSmall", productPicList.get(0).getPicSmall());
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询活动商品列表成功", mallActivityGoodList);
	}

	/**
	 * 新增或修改活动
	 */
	@Override
	public CommonResult modifyActivity(MallActivity mallActivity) {
		if (mallActivity == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数有误");
		}
		if (mallActivity.getBeginTime() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动开始时间不能为空");
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 1);
		if (DateUtil.compareDate(mallActivity.getBeginTime(), cal.getTime()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动开始时间必须晚于当前一小时后");
		}
		if (mallActivity.getEndTime() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动结束时间不能为空");
		}
		if (DateUtil.compareDate(mallActivity.getEndTime(), mallActivity.getBeginTime()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动结束时间不能小于开始时间");
		}
		String msg = "";
		if (StringUtils.isBlank(mallActivity.getId())) {
			mallActivityMapper.insert(mallActivity);
			msg = "新增活动成功";
		} else {
			mallActivityMapper.updateSelectiveByPrimaryKey(mallActivity);
			msg = "修改活动成功";
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 删除活动
	 */
	@Override
	public CommonResult deleteActivity(String activityId) {
		if (StringUtils.isBlank(activityId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		MallActivity activity = mallActivityMapper.selectByPrimaryKey(activityId);
		if (activity == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "活动不存在");
		}
		// 未开始
		if (activity.getStatus() == 1) {
			List<Map<String, Object>> goodsList = mallActivityGoodsMapper.selectActivityGoodsByActivityId(activityId);
			if (goodsList != null && goodsList.size() > 0) {
				for (Map<String, Object> map : goodsList) {
					ProductSku sku = productSkuMapper
							.selectByPrimaryKey(map.containsKey("skuId") ? map.get("skuId").toString() : "");
					if (sku != null) {
						sku.setProductSaleQuantity(sku.getProductSaleQuantity()
								- (map.containsKey("quantity") ? Integer.valueOf(map.get("quantity").toString()) : 0));
						productSkuMapper.updateSelectiveByPrimaryKey(sku);
					}
				}
			}
		}
		mallActivityMapper.deleteByPrimaryKey(activityId);
		mallActivityGoodsMapper.deleteByActivityId(activityId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public String selectByActivityNamePaged(String title, Integer status, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = mallActivityMapper.selectSumByActivityName(title, status);
		List<Map<String, Object>> list = mallActivityMapper.selectByActivityNamePaged(title, status, page.getStart(),
				page.getPageSize());
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectInfoByKey(String id) {
		JSONObject json = new JSONObject();

		MallActivity activity = mallActivityMapper.selectByPrimaryKey(id);
		if (activity == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", activity);
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", activity);
		}
		return json.toString();
	}

	@Override
	public String selectActivityInfoByActivityId(String activityId) {
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<>();

		// 获得活动
		MallActivity activity = mallActivityMapper.selectByPrimaryKey(activityId);
		if (activity == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
		} else {
			map.put("activity", activity);
			// 获得活动商品
			List<Map<String, Object>> list = mallActivityGoodsMapper.selectActivityGoodsByActivityId(activityId);
			if (list == null || list.size() == Constants.INT_0) {
				json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
				json.put("msg", "查询活动商品为空");
			} else {
				for (Map<String, Object> info : list) {
					// 从缓存中读取活动商品库存
					String surplusQuantity = jedisClient.get(info.get("activityId") + ":" + info.get("id"));
					if (StringUtils.isNotBlank(surplusQuantity)) {
						info.put("surplus", surplusQuantity);
						Integer sellQuantity = Integer.parseInt(info.get("quantity").toString())
								- Integer.parseInt(surplusQuantity);
						info.put("sellQuantity", sellQuantity);
					}
				}
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "查询成功");
				map.put("activityGoods", list);
			}
			json.put("data", map);
		}
		return json.toString();
	}

	@Override
	public String selectActivityGoodsListByActivityId(String activityId) {
		JSONObject json = new JSONObject();

		List<Map<String, Object>> list = mallActivityGoodsMapper.selectActivityGoodsByActivityId(activityId);
		if (list == null || list.size() == Constants.INT_0) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询活动商品为空");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("activityGoods", list);
		}
		return json.toString();
	}

	/**
	 * 定时任务：管理活动状态及活动商品数量
	 */
	@Override
	public void handleActivityMall() {
		List<MallActivity> list = mallActivityMapper.selectInvalidActivityList();
		if (list != null && list.size() > 0) {
			for (MallActivity activity : list) {
				List<Map<String, String>> goodsList = mallActivityGoodsMapper.selectByActivityId(activity.getId(), -1,
						10);
				if (goodsList != null && goodsList.size() > 0) {
					for (Map<String, String> map : goodsList) {
						// 从redis中同步该活动商品的已售数量，更新至数据库
						String surplusQuantity = jedisClient.get(map.get("activityId") + ":" + map.get("goodsId"));
						if (StringUtils.isNotBlank(surplusQuantity)) {
							MallActivityGoods goods = mallActivityGoodsMapper.selectByPrimaryKey(map.get("goodsId"));
							if (goods != null) {
								goods.setSellQuantity(goods.getQuantity() - Integer.parseInt(surplusQuantity));
								mallActivityGoodsMapper.updateSelectiveByPrimaryKey(goods);
								map.put("sellQuantity", goods.getQuantity() - Integer.parseInt(surplusQuantity) + "");
							}
						}
						// 处理剩余活动商品
						if (Integer.parseInt(map.get("status")) == 1) {
							Integer surplus = Integer.parseInt(map.get("quantity"))
									- Integer.parseInt(map.get("sellQuantity"));
							if (surplus > 0) {
								ProductSku sku = productSkuMapper.selectByPrimaryKey(map.get("skuId"));
								sku.setProductSaleQuantity(sku.getProductSaleQuantity() - surplus);
								productSkuMapper.updateSelectiveByPrimaryKey(sku);
							}
						}
					}
				}
			}
		}
		mallActivityMapper.handleMallActivityStatus();
		// 查询即将开始的活动
		List<MallActivity> imminentActivity = mallActivityMapper.selectImminentActivity();
		if (imminentActivity != null && imminentActivity.size() > 0) {
			for (MallActivity activity : imminentActivity) {
				List<Map<String, String>> goodsList = mallActivityGoodsMapper.selectByActivityId(activity.getId(), -1,
						10);
				long expire = activity.getEndTime().getTime() - new Date().getTime();
				if (goodsList != null && goodsList.size() > 0) {
					for (Map<String, String> map : goodsList) {
						jedisClient.set(activity.getId() + ":" + map.get("goodsId"), map.get("surplusQuantity"));
						jedisClient.expire(activity.getId() + ":" + map.get("goodsId"), (int) expire / 1000 + 600);
					}
				}
			}
		}
	}
}
