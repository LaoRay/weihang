package cn.com.clubank.weihang.manage.member.service.impl;

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
import cn.com.clubank.weihang.manage.member.dao.CouponInventoryMapper;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import cn.com.clubank.weihang.manage.member.service.ICouponService;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;

@Service
public class CouponServiceImpl implements ICouponService {

	@Resource
	private CouponInventoryMapper couponInventoryMapper;
	@Autowired
	private CouponListMapper couponListMapper;
	@Resource
	private ProductSkuMapper productSkuMapper;
	@Resource
	private ProductInfoMapper poInfoMapper;

	@Override
	public String selectCouponListByNameOrTime(String couponName, Integer couponType, String startDateOne,
			String startDateTwo, Integer pageIndex, Integer pageSize) {
		PageObject<CouponInventory> page = new PageObject<>(pageIndex, pageSize);

		int total = couponInventoryMapper.selectSumByCouponNameOrTime(couponName, couponType, startDateOne,
				startDateTwo);
		List<CouponInventory> list = couponInventoryMapper.selectCouponListPagedByNameOrTime(couponName, couponType,
				startDateOne, startDateTwo, page.getStart(), page.getPageSize());
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

	/**
	 * 审核优惠券
	 */
	@Override
	public CommonResult clientReviewCoupon(String crId, Integer enabledMark) {
		if (StringUtils.isBlank(crId) || enabledMark == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		CouponInventory couponInventory = couponInventoryMapper.selectByPrimaryKey(crId);
		if (couponInventory == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "优惠券不存在");
		}
		String msg = "";
		// 上架
		if (enabledMark == Constants.INT_2) {
			if (DateUtil.compareDate(couponInventory.getValidTimeEnd(), new Date()) <= 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "已超过有效期结束时间，不能上架");
			}
			// 服务券和兑换券查看是否已设置商品sku
			if (couponInventory.getCouponType() == Constants.INT_1
					|| couponInventory.getCouponType() == Constants.INT_2) {
				if (StringUtils.isBlank(couponInventory.getSkuId())) {
					return CommonResult.result(ResultCode.FAIL.getValue(), "优惠券还未设置商品信息，不能上架");
				}
			}
			// 折扣券查看是否已设置商品
			if (couponInventory.getCouponType() == Constants.INT_4) {
				if (StringUtils.isBlank(couponInventory.getProductId())) {
					return CommonResult.result(ResultCode.FAIL.getValue(), "优惠券还未设置商品信息，不能上架");
				}
			}
			msg = "上架成功";
		}
		// 下架
		else if (enabledMark == Constants.INT_3) {
			msg = "下架成功";
		}
		// 异常状态
		else {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		couponInventory.setEnabledMark(enabledMark);
		couponInventoryMapper.updateSelectiveByPrimaryKey(couponInventory);
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
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
	public CommonResult selectCouponDetail(String crId) {

		if (StringUtils.isBlank(crId)) {
			return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "参数不能为空");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		CouponInventory coupon = couponInventoryMapper.selectByPrimaryKey(crId);// 获得优惠券对象
		if (coupon == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "参数错误");
		}
		map.put("coupon", coupon);
		if (coupon.getCouponType() == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "此券无类型", map);
		}
		// 判断为哪种券（(1服务券和2兑换券是SKU商品，4折扣券是商品)
		if (coupon.getCouponType() == 1 || coupon.getCouponType() == 2 && !StringUtils.isBlank(coupon.getSkuId())) {
			ProductSku productSku = productSkuMapper.selectByPrimaryKey(coupon.getSkuId());
			if (productSku != null) {
				map.put("productSku", productSku);
				return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
			} else {
				return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "关联的SKU商品不存在", map);
			}
		}
		if (coupon.getCouponType() == 4 && !StringUtils.isBlank(coupon.getProductId())) {
			ProductInfo productInfo = poInfoMapper.selectByPrimaryKey(coupon.getProductId());
			if (productInfo != null) {
				map.put("productInfo", productInfo);
				return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
			} else {
				return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "关联的商品不存在", map);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
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

	@Override
	public CommonResult updateCouponGoods(String crId, String productId, String skuId) {
		if (StringUtils.isBlank(crId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "crId不能为空");
		}

		CouponInventory coupon = couponInventoryMapper.selectByPrimaryKey(crId);
		if (coupon == null) {
			return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "查询为空");
		}

		if (coupon.getCouponType() != null) {
			if (coupon.getCouponType() == Constants.INT_4) {// 4折扣券
				coupon.setProductId(productId);
			}

			if (coupon.getCouponType() == Constants.INT_1 || coupon.getCouponType() == Constants.INT_2) {// 1服务券
																											// 2兑换券
				coupon.setProductId(productId);
				coupon.setSkuId(skuId);
			}
		}

		Integer apiStatus = null;
		String msg = "";
		if (couponInventoryMapper.updateSelectiveByPrimaryKey(coupon) > Constants.INT_0) {
			apiStatus = ResultCode.SUCC.getValue();
			msg = "设置成功";
		} else {
			apiStatus = ResultCode.FAIL.getValue();
			msg = "设置失败";
		}
		return CommonResult.result(apiStatus, msg);
	}

	@Override
	public CommonResult selectCouponGoods(String crId) {

		if (StringUtils.isBlank(crId)) {
			return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "参数不能为空");
		}

		CouponInventory coupon = couponInventoryMapper.selectByPrimaryKey(crId);// 获得优惠券对象

		if (coupon.getCouponType() == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "此券无类型");
		}
		// 判断为哪种券（(1服务券和2兑换券是SKU商品，4折扣券是商品)
		if (coupon.getCouponType() == 1 || coupon.getCouponType() == 2 && !StringUtils.isBlank(coupon.getSkuId())) {
			ProductSku productSku = productSkuMapper.selectByPrimaryKey(coupon.getSkuId());
			if (productSku != null) {
				return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", productSku);
			} else {
				return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "查询为空", productSku);
			}
		}
		if (coupon.getCouponType() == 4 && !StringUtils.isBlank(coupon.getProductId())) {
			ProductInfo productInfo = poInfoMapper.selectByPrimaryKey(coupon.getProductId());
			if (productInfo != null) {
				return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", productInfo);
			} else {
				return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "查询为空", productInfo);
			}
		}
		return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "此券不关联商品");
	}

	/**
	 * pc：获取优惠券详情
	 */
	@Override
	public CommonResult websiteFindCouponDetail(String crId) {
		Map<String, String> couponDetail = couponInventoryMapper.selectCouponDetailByCrId(crId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", couponDetail);
	}
}
