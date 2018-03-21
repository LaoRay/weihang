package cn.com.clubank.weihang.client.marketing;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CouponInventory;
import cn.com.clubank.weihang.manage.member.service.ICouponService;

/**
 * 后台：优惠券管理
 * 
 * @author Liangwl
 *
 */
@Controller
public class ClientCouponController {

	@Resource
	private ICouponService iCouponService;

	/**
	 * 后台：通过券名称模糊查询，券类型、开始时间查询获得券列表并分页
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindCouponListPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findCouponListPaged(@RequestBody JSONObject param) {

		return iCouponService.selectCouponListByNameOrTime(param.getString("couponName"),
				param.getInteger("couponType"), param.getString("startDateOne"), param.getString("startDateTwo"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 后台：通过优惠券ID获得优惠券的详情
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindCouponDetail", method = RequestMethod.POST)
	@ResponseBody
	public String findCouponDetail(@RequestBody JSONObject param) {

		return iCouponService.selectCouponDetail(param.getString("crId"));
	}

	/**
	 * 后台：新增或编辑优惠券
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientAddOrEditCoupon", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addOrEditCoupon(@RequestBody CouponInventory record) {

		return iCouponService.insertOrUpdateCoupon(record);
	}

	/**
	 * 后台：删除优惠券
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteCoupon", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCoupon(@RequestBody JSONObject param) {

		return iCouponService.deleteCoupon(param.getString("crId"));
	}

	/**
	 * 后台：分页查询优惠券兑换流水列表（含条件）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindCouponExchangeRecordList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindCouponExchangeRecordList(@RequestBody JSONObject json) {
		return iCouponService.clientFindCouponExchangeRecordList(json.getInteger("pageIndex"),
				json.getInteger("pageSize"), json.getString("couponName"), json.getString("customerName"),
				json.getInteger("couponStatus"));
	}

	/**
	 * 后台：根据优惠券兑换记录主键查询兑换详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindCouponExchangeDetailByCouponId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindCouponExchangeDetailByCouponId(@RequestBody JSONObject json) {
		return iCouponService.clientFindCouponExchangeDetailByCouponId(json.getString("couponId"));
	}

	/**
	 * 后台：删除优惠券兑换记录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteCouponExchangeRecord", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteCouponExchangeRecord(@RequestBody JSONObject param) {
		return iCouponService.clientDeleteCouponExchangeRecord(param.getString("couponId"));
	}
}
