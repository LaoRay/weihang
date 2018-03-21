package cn.com.clubank.weihang.restful.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.service.ICouponService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;

/**
 * 积分优惠券管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class IntegralCouponController {

	@Autowired
	private IntegralService integralService;
	@Autowired
	private ICouponService iCouponService;

	/**
	 * 会员分享商品链接增加积分
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberShare", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult share(@RequestBody JSONObject json) {
		return integralService.share(json.getString("customerId"));
	}

	/**
	 * 获取积分列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/memberIntegralList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getIntegralList(@RequestBody JSONObject json) {
		return integralService.getIntegralList(json.getString("customerId"), json.getInteger("pageIndex"),
				json.getInteger("pageSize"));
	}

	/**
	 * 获取所有优惠券列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/couponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getCouponList() {
		return integralService.getCouponList();
	}

	/**
	 * 获取会员拥有的优惠券列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/memberCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getCouponListByCustomerId(@RequestBody JSONObject json) {
		return integralService.getCouponListByCustomerId(json.getString("customerId"), json.getInteger("couponStatus"));
	}

	/**
	 * 积分兑换优惠券
	 * 
	 * @param customerId
	 * @param couponId
	 * @param couponNum
	 * @return
	 */
	@RequestMapping(value = "/memberExchangeCouponByIntegral", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult exchangeCouponByIntegral(@RequestBody JSONObject json) {
		return integralService.exchangeCouponByIntegral(json.getString("customerId"), json.getString("couponId"),
				json.getInteger("couponNum"), json.getInteger("level"));
	}

	/**
	 * 通过优惠券ID获得优惠券的详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindCouponDetail", method = RequestMethod.POST)
	@ResponseBody
	public String memberFindCouponDetail(@RequestBody JSONObject param) {
		return iCouponService.selectCouponDetail(param.getString("crId"));
	}

	/**
	 * 获取折扣券Discount coupon
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindAvailableDiscountCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindAvailableDiscountCouponList(@RequestBody JSONObject json) {
		return integralService.memberFindAvailableDiscountCouponList(json.getString("customerId"),
				json.getString("productIds"));
	}

	/**
	 * 获取抵扣券Deduction coupon
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindAvailableDeductionCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindAvailableDeductionCouponList(@RequestBody JSONObject json) {
		return integralService.memberFindAvailableDeductionCouponList(json.getString("customerId"));
	}

	/**
	 * 获取兑换券Exchange coupon
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindAvailableExchangeCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindAvailableExchangeCouponList(@RequestBody JSONObject json) {
		return integralService.memberFindAvailableExchangeCouponList(json.getString("customerId"),
				json.getString("skuIds"));
	}

	/**
	 * 获取服务券Service coupon
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindAvailableServiceCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindAvailableServiceCouponList(@RequestBody JSONObject json) {
		return integralService.memberFindAvailableServiceCouponList(json.getString("customerId"),
				json.getString("skuIds"));
	}
}
