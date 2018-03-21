package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.MallActivityGoodsMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
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

	/**
	 * 查询活动列表及对应的活动商品信息
	 */
	@Override
	public CommonResult findActivityAndProduct() {
		List<MallActivity> mallActivityList = mallActivityMapper.selectValidActivityList();
		List<Object> list = new ArrayList<>();
		for (MallActivity mallActivity : mallActivityList) {
			List<Map<String, String>> mallActivityGoodList = mallActivityGoodsMapper
					.selectByActivityId(mallActivity.getId());
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
		List<Map<String, String>> mallActivityGoodList = mallActivityGoodsMapper.selectGoodListByActivityId(activityId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
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
		if (mallActivity != null) {
			if (mallActivity.getBeginTime() == null) {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动开始时间不能为空");
			}
			if (DateUtil.compareDate(mallActivity.getBeginTime(), new Date()) < 0) {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "活动开始时间不能小于当前时间");
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
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数有误");
	}

	/**
	 * 删除活动
	 */
	@Override
	public CommonResult deleteActivity(String activityId) {
		List<Map<String, Object>> list = mallActivityGoodsMapper.selectActivityGoodsByActivityId(activityId);
		if (list == null || list.size() == Constants.INT_0) {// 此活动下没有关联商品，可以删除
			mallActivityMapper.deleteByPrimaryKey(activityId);
			return CommonResult.result(ResultCode.SUCC.getValue(), "删除活动成功");
		} else {
			return CommonResult.result(ResultCode.FAIL.getValue(), "请先删除此活动下所有商品");
		}
	}

	/**
	 * 查询活动列表
	 */
	@Override
	public CommonResult findActivityList() {
		List<MallActivity> mallActivityList = mallActivityMapper.selectValidActivityList();
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询活动列表成功", mallActivityList);
	}

	@Override
	public String selectByActivityNamePaged(String title, Integer status, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = mallActivityMapper.selectSumByActivityName(title, status);
		List<Map<String, Object>> list = mallActivityMapper.selectByActivityNamePaged(title, status, page.getStart(),
				pageSize);
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
}
