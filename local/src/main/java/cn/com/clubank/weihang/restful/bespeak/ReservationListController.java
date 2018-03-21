package cn.com.clubank.weihang.restful.bespeak;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuth;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;

@Controller
public class ReservationListController {

	@Resource
	private IReservationService iReservationList;

	/**
	 * 保存预约信息
	 * 
	 * @param record
	 *            预约单对象
	 * @param carNo
	 *            车牌号
	 * @return
	 */
	@RequestMapping(value = "/bespeakSaveInfo", method = RequestMethod.POST)
	@ResponseBody
	@WeihAuth
	public CommonResult addReservation(@RequestBody JSONObject param) {
		WorkReservationOrder record = JSONObject.toJavaObject(param, WorkReservationOrder.class);
		return iReservationList.save(record);
	}

	/**
	 * 查询预约列表
	 * 
	 * @param customerId
	 *            客户ID
	 * @param reservationStatus
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/bespeakViewRecord", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getReservation(@RequestBody JSONObject param) {
		return iReservationList.selectByCustomerId(param.getString("customerId"), param.getInteger("reservationStatus"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 取消预约单
	 * 
	 * @param reservationOrderNo
	 *            预约单号
	 * @param customerId
	 *            用户id
	 * @return
	 */
	@RequestMapping(value = "/bespeakCancel", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult updateReservationStatus(@RequestBody JSONObject param) {
		return iReservationList.cancel(param.getString("reservationOrderNo"), param.getString("customerId"));
	}

	/**
	 * 查看某预约单详情
	 * 
	 * @param reservationOrderNo
	 *            预约单号
	 * @return
	 */
	@RequestMapping(value = "/bespeakViewDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult getInfo(@RequestBody JSONObject param) {
		return iReservationList.selectByReservationOrderNo(param.getString("reservationOrderNo"));
	}
}
