package cn.com.clubank.weihang.restful.special;

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
 * 特殊订单管理
 * @author Liangwl
 *
 */
@Controller
public class SpecialOrderController {
	
	@Resource
	private ISpecialOrderService iSpecialOrderService;

	/**
	 * 特殊订单保存
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberSaveSpecialOrderInfo",method = RequestMethod.POST)
	@ResponseBody
	public CommonResult saveSpecialOrder(@RequestBody JSONObject json){
		
		return iSpecialOrderService.insertSpecialOrderInfo(json);
	}
	
	/**
	 * 查询特殊订单列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberFindSpecialOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findSpecialOrderList(@RequestBody JSONObject json) {
		
		return iSpecialOrderService.selectSpecialOrderList(json.getString("customerId"), json.getInteger("status"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}
	
	/**
	 * 特殊订单取消
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberCancelSpecialOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult cancelSpecialOrder(@RequestBody JSONObject json) {
		
		return iSpecialOrderService.cancelSpecialOrder(json.getString("specialId"));
	}
	
	/**
	 * 特殊订单确认收货
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberConfirmReceiptSpecialOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult confirmReceiptInsuranceOrder(@RequestBody JSONObject json) {
		
		return iSpecialOrderService.confirmReceiptSpecialOrder(json.getString("specialId"));
	}
	
	/**
	 * 查询特殊订单详情
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/memberFindSpecialOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findSpecialOrderDetail(@RequestBody JSONObject json) {
		
		return iSpecialOrderService.selectSpecialOrderDetail(json.getString("specialId"));
	}
}
