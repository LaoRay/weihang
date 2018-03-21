package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertMapper;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertPositionMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvertPosition;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.service.MallAdvertService;
import lombok.extern.slf4j.Slf4j;

/**
 * 广告管理
 * 
 * @author LeiQY
 *
 */
@Slf4j
@Service
public class MallAdvertServiceImpl implements MallAdvertService {

	@Autowired
	private MallAdvertMapper mallAdvertMapper;
	@Resource
	private MallAdvertPositionMapper mallAdvertPositionMapper;
	@Resource
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private IntegralService integralService;

	/**
	 * 广告新增或修改
	 */
	@Override
	public CommonResult modifyAdvert(MallAdvert mallAdvert) {
		if (mallAdvert == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		if (mallAdvert.getBeginTime() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "广告开始时间不能为空");
		}
		if (DateUtil.compareDate(mallAdvert.getBeginTime(), new Date()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "广告开始时间不能小于当前时间");
		}
		if (mallAdvert.getEndTime() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "广告结束时间不能为空");
		}
		if (DateUtil.compareDate(mallAdvert.getEndTime(), mallAdvert.getBeginTime()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "广告结束时间不能小于开始时间");
		}
		// 保存所选广告位的位置类型
		MallAdvertPosition pos = mallAdvertPositionMapper.selectByCode(mallAdvert.getPositionCode());
		if (pos != null) {
			mallAdvert.setPositionType(pos.getPositionType());
		}
		String msg = "";
		if (StringUtils.isBlank(mallAdvert.getId())) {
			mallAdvertMapper.insert(mallAdvert);
			msg = "新增广告成功";
		} else {
			mallAdvertMapper.updateSelectiveByPrimaryKey(mallAdvert);
			msg = "修改广告成功";
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 广告删除
	 */
	@Override
	public CommonResult deleteAdvert(String id) {
		if (mallAdvertMapper.deleteByPrimaryKey(id) > Constants.INT_0) {
			return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
		}
		return CommonResult.result(ResultCode.FAIL.getValue(), "删除失败");
	}

	/**
	 * 根据广告位编号查询广告列表
	 */
	@Override
	public CommonResult findAdvertListByPositionCode(String positionCode) {
		List<MallAdvert> advertList = mallAdvertMapper.selectAdvertListByPositionCode(positionCode);
		log.info("根据广告位编号查询广告：{}，结果：{}", positionCode, advertList.size());
		return CommonResult.result(ResultCode.SUCC.getValue(), "广告列表查询成功", advertList);
	}

	@Override
	public String selectAdListByNameOrStatusOrType(String adName, Integer status, Integer positionType,
			Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);

		int total = mallAdvertMapper.selectSumByAdName(adName, status, positionType);
		List<Map<String, Object>> list = mallAdvertMapper.selectByAdNameOrStartOrTypePaged(adName, status, positionType,
				page.getStart(), page.getPageSize());
		page.setRows(total);
		page.setDataList(list);

		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String updateAdvertStatus(String id) {
		JSONObject json = new JSONObject();

		if (mallAdvertMapper.updateAdvertStatus(id) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "关闭成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "关闭失败");
		}
		return json.toString();
	}

	@Override
	public String selectAdvertInfoAndPositionList(String id) {
		JSONObject json = new JSONObject();

		Map<String, Object> map = new HashMap<>();
		List<MallAdvertPosition> list = mallAdvertPositionMapper.selectAll();
		MallAdvert advert = mallAdvertMapper.selectByPrimaryKey(id);

		map.put("AdvertPositionList", list);
		map.put("advertInfo", advert);

		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "查询成功");
		json.put("data", map);
		return json.toString();
	}

	@Override
	public String updateAdvertClickCount(String id, String customerId) {
		log.info("点击广告：id-{}，customerId-{}", id, customerId);
		JSONObject json = new JSONObject();
		MallAdvert advert = mallAdvertMapper.selectByPrimaryKey(id);
		if (advert == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "没有找到广告数据");
			return json.toString();
		}
		advert.setClickCount(advert.getClickCount() + 1);// 改变点击数
		mallAdvertMapper.updateSelectiveByPrimaryKey(advert);// 更新点击数

		// 点击广告增加积分
		integralService.advertIntegral(customerId);

		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "点击成功");
		return json.toString();
	}

	@Override
	public String selectAdvertDetail(String id) {
		JSONObject json = new JSONObject();

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> advertInfo = mallAdvertMapper.selectAdvertDetailByKey(id);// 广告信息

		if (!advertInfo.isEmpty()) {
			map.put("advertInfo", advertInfo);

			if (advertInfo.containsKey("objId")) {// 关联的有商品：是商品广告
				ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(advertInfo.get("objId").toString());// 商品广告关联的商品信息
				map.put("productInfo", productInfo);
			}

			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", map);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", advertInfo);
		}
		return JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public CommonResult mallAdvertFindList(Integer pageIndex, Integer pageSize) {
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", mallAdvertMapper.mallAdvertFindList(PageObject.getStart(pageIndex, pageSize), pageSize));
	}
}
