package cn.com.clubank.weihang.client.special;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.special.service.ISpecialOrderService;

/**
 * 后台：特殊订单管理
 * @author Liangwl
 *
 */
@Controller
public class ClientSpecialOrderController {
	
	@Resource
	private ISpecialOrderService iSpecialOrderService;

	/**
	 * 后台：查询特殊订单列表并分页
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clinetFindSpecialOrderListAndPaged", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindSpecialOrderList(@RequestBody JSONObject json){
		
		return iSpecialOrderService.clientSelectSpecialOrderList(json.getString("orderTimeStart"), 
				json.getString("orderTimeEnd"), json.getInteger("status"), json.getString("customerName"), 
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}
	
	/**
	 * 后台：查询特殊订单详情
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clinetFindSpecialOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindSpecialOrderDetail(@RequestBody JSONObject json){
		
		return iSpecialOrderService.selectSpecialOrderDetail(json.getString("specialId"));
	}
	
	/**
	 * 后台：确认特殊订单金额
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientAmountRecognizedSpecialOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientAmountRecognizedSpecialOrder(@RequestBody JSONObject json){
		
		return iSpecialOrderService.cilentEditSpecialOrder(json.getString("specialId"), json.getBigDecimal("orderAmount"), json.getDate("estimateTotime"));
	}
	
	/**
	 * 后台：特殊订单发货
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientDeliverySpecialOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeliverySpecialOrder(@RequestBody JSONObject json){
		
		return iSpecialOrderService.clientDeliverySpecialOrder(json.getString("specialId"));
	}
	
	/**
	 * 后台：特殊订单驳回
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientRejectSpecialOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientRejectSpecialOrder(@RequestBody JSONObject json){
		
		return iSpecialOrderService.clientRejectSpecialOrder(json.getString("specialId"), json.getString("turnDownDesc"));
	}
}
