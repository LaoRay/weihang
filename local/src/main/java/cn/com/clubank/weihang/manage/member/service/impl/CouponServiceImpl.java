package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.Date;
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
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import cn.com.clubank.weihang.manage.member.service.ICouponService;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;

@Service
public class CouponServiceImpl implements ICouponService {

	@Resource
	private CouponInventoryMapper couponInventoryMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Resource
	private ProductSkuMapper productSkuMapper;

	@Override
	public String selectCouponListByNameOrTime(String couponName, Integer couponType, String startDateOne,
			String startDateTwo, Integer pageIndex, Integer pageSize) {
		PageObject<CouponInventory> page = new PageObject<>(pageIndex, pageSize);

		int total = couponInventoryMapper.selectSumByCouponNameOrTime(couponName, couponType, startDateOne,
				startDateTwo);
		List<CouponInventory> list = couponInventoryMapper.selectCouponListPagedByNameOrTime(couponName, couponType,
				startDateOne, startDateTwo, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);

		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public CommonResult insertOrUpdateCoupon(CouponInventory record) {
		if (record.getValidTimeStart() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "优惠券有效期开始时间不能为空");
		}
		if (DateUtil.compareDate(record.getValidTimeStart(), new Date()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "优惠券有效期开始时间不能小于当前时间");
		}
		if (record.getValidTimeEnd() == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "优惠券有效期结束时间不能为空");
		}
		if (DateUtil.compareDate(record.getValidTimeEnd(), record.getValidTimeStart()) < 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "优惠券有效期结束时间不能小于开始时间");
		}
		Integer apiStatus = null;
		String msg = "";
		if (StringUtils.isBlank(record.getCrId())) {
			if (couponInventoryMapper.insert(record) > Constants.INT_0) {
				apiStatus = ResultCode.SUCC.getValue();
				msg = "新增成功";
			} else {
				apiStatus = ResultCode.FAIL.getValue();
				msg = "新增失败";
			}
		} else {
			if (couponInventoryMapper.updateSelectiveByPrimaryKey(record) > Constants.INT_0) {
				apiStatus = ResultCode.SUCC.getValue();
				msg = "编辑成功";
			} else {
				apiStatus = ResultCode.FAIL.getValue();
				msg = "编辑失败";
			}
		}
		return CommonResult.result(apiStatus, msg);
	}

	@Override
	public String deleteCoupon(String crId) {
		JSONObject json = new JSONObject();

		if (couponInventoryMapper.deleteByPrimaryKey(crId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String selectCouponDetail(String crId) {
		JSONObject json = new JSONObject();
		Map<String, String> couponDetail = couponInventoryMapper.selectCouponDetailByCrId(crId);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "查询成功");
		json.put("data", couponDetail);
		return JSON.toJSONStringWithDateFormat(json, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 后台：分页查询优惠券兑换流水列表（含条件）
	 */
	@Override
	public CommonResult clientFindCouponExchangeRecordList(Integer pageIndex, Integer pageSize, String couponName,
			String customerName, Integer couponStatus) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = couponListMapper.selectCountCouponExchangeRecord(couponName, customerName, couponStatus);
		page.setRows(total);
		List<Map<String, String>> list = couponListMapper.selectCouponExchangeRecordList(page.getStart(),
				page.getPageSize(), couponName, customerName, couponStatus);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 后台：根据优惠券兑换来记录主键查询兑换详情
	 */
	@Override
	public CommonResult clientFindCouponExchangeDetailByCouponId(String couponId) {
		Map<String, String> map = couponListMapper.selectCouponExchangeDetailByPrimaryKey(couponId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 后台：删除优惠券兑换记录
	 */
	@Override
	public CommonResult clientDeleteCouponExchangeRecord(String couponId) {
		couponListMapper.deleteByPrimaryKey(couponId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}
}
