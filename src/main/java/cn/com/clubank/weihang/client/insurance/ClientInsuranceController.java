package cn.com.clubank.weihang.client.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.insurance.pojo.InsurancePic;
import cn.com.clubank.weihang.manage.insurance.service.InsuranceService;

/**
 * 后台：保险管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientInsuranceController {

	@Autowired
	private InsuranceService insuranceService;

	/**
	 * 查询保险订单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetFindInsuranceOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetFindInsuranceOrderList(@RequestBody JSONObject json) {
		return insuranceService.clinetFindInsuranceOrderList(json.getString("subTimeStart"),
				json.getString("subTimeEnd"), json.getString("companyName"), json.getInteger("status"),
				json.getString("subName"), json.getString("carNo"), json.getInteger("pageIndex"),
				json.getInteger("pageSize"));
	}

	/**
	 * 保险订单验车
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetInsuranceOrderCheckCar", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetInsuranceOrderCheckCar(@RequestBody JSONObject json) {
		return insuranceService.clinetInsuranceOrderCheckCar(json.getString("insuranceId"));
	}

	/**
	 * 保险订单驳回
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetRejectInsuranceOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetRejectInsuranceOrder(@RequestBody JSONObject json) {
		return insuranceService.clinetRejectInsuranceOrder(json.getString("insuranceId"),
				json.getString("description"));
	}

	/**
	 * 添加保单图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetAddInsuranceOrderPic", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetAddInsuranceOrderPic(@RequestBody InsurancePic pic) {
		return insuranceService.clinetAddInsuranceOrderPic(pic);
	}

	/**
	 * 编辑保单价格并确认
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetModifyInsuranceOrderPrice", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetModifyInsuranceOrderPrice(@RequestBody JSONObject param) {
		return insuranceService.clinetModifyInsuranceOrderPrice(param.getString("insuranceId"),
				param.getBigDecimal("amount1"), param.getBigDecimal("amount2"), param.getBigDecimal("amount3"));
	}
	
	/**
	 * 保单发货
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clinetDeliveryInsuranceOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clinetDeliveryInsuranceOrder(@RequestBody JSONObject param) {
		return insuranceService.clinetDeliveryInsuranceOrder(param.getString("insuranceId"));
	}
}
