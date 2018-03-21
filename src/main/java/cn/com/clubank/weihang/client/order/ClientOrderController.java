package cn.com.clubank.weihang.client.order;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuthLog;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.product.service.IProductAftersaleApplyService;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.repair.service.IRepairOrderFlowService;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;
import cn.com.clubank.weihang.manage.special.service.ISpecialOrderService;

/**
 * 后台：订单管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientOrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private IProductAftersaleApplyService aftersaleApplyService;
	@Autowired
	private IReservationService reservationService;
	@Autowired
	private IWorkRepairService iWorkRepairService;
	@Autowired
	private IRepairOrderFlowService iRepairOrderFlowService;
	@Autowired
	private ISpecialOrderService iSpecialOrderService;

	/**
	 * 商品订单分页查询(条件)
	 */
	@RequestMapping(value = "/clientFindOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindOrderList(@RequestBody JSONObject json) {
		return orderService.clientFindOrderList(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getString("orderNo"), json.getInteger("orderStatus"), json.getString("startDate"),
				json.getString("endDate"), json.getInteger("orderCategory"));
	}

	/**
	 * 根据订单id查询订单详情列表
	 */
	@RequestMapping(value = "/clientFindOrderDetailListByOrderId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindOrderDetailListByOrderId(@RequestBody JSONObject json) {
		return orderService.findOrderDetailsByOrderId(json.getString("orderId"));
	}

	/**
	 * 商品发货
	 */
	@RequestMapping(value = "/clientDeliveryGoodsByOrderId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeliveryGoodsByOrderId(@RequestBody JSONObject json) {
		return orderService.clientDeliveryGoodsByOrderId(json.getString("orderId"));
	}

	/**
	 * 分页查询售后申请列表(条件)
	 */
	@RequestMapping(value = "/clientFindApplyInfoList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindApplyInfoList(@RequestBody JSONObject json) {
		return aftersaleApplyService.clientFindApplyInfoList(json.getInteger("pageIndex"), json.getInteger("pageSize"),
				json.getInteger("status"), json.getString("startDate"), json.getString("endStart"));
	}

	/**
	 * 售后申请-处理
	 */
	@RequestMapping(value = "/clientHandleApplyByApplyId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientHandleApplyByApplyId(HttpServletRequest request, @RequestBody JSONObject json) {
		return aftersaleApplyService.clientHandleApplyByApplyId(request.getHeader("userId"), json.getString("applyId"), json.getString("conductResult"));
	}
	
	/**
	 * 售后申请-驳回
	 */
	@RequestMapping(value = "/clientRejectApplyByApplyId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientRejectApplyByApplyId(HttpServletRequest request, @RequestBody JSONObject json) {
		return aftersaleApplyService.clientRejectApplyByApplyId(request.getHeader("userId"), json.getString("applyId"), json.getString("conductResult"));
	}

	/**
	 * 查询预约服务订单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindReservationOrder", method = RequestMethod.POST)
	@ResponseBody
	public String clientFindReservationOrder(@RequestBody JSONObject param) {
		return reservationService.clientFindReservationOrder(param.getInteger("pageIndex"),
				param.getInteger("pageSize"), param.getString("carNo"), param.getInteger("orderStatus"),
				param.getString("reservationDateStart"), param.getString("reservationDateEnd"));
	}

	/**
	 * 删除预约服务订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteReservationOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteReservationOrder(@RequestBody JSONObject json) {
		return reservationService.clientDeleteReservationOrder(json.getString("roId"));
	}

	/**
	 * 查询维修单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindRepairServiceOrder", method = RequestMethod.POST)
	@ResponseBody
	public String clientFindRepairServiceOrder(@RequestBody JSONObject param) {
		return iWorkRepairService.clientFindRepairServiceOrder(param.getInteger("pageIndex"),
				param.getInteger("pageSize"), param.getInteger("payStatus"), param.getString("orderNo"),
				param.getInteger("status"), param.getString("consultantName"), param.getString("supervisorName"));
	}

	/**
	 * 根据维修单主键查询维修单详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindRepairOrderDetailByWrId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindRepairOrderDetailByWrId(@RequestBody JSONObject param) {
		return iWorkRepairService.clientFindRepairOrderDetailByWrId(param.getString("wrId"));
	}

	/**
	 * 查询洗车单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindWashServiceOrder", method = RequestMethod.POST)
	@ResponseBody
	public String clientFindWashServiceOrder(@RequestBody JSONObject param) {
		return iWorkRepairService.clientFindWashServiceOrder(param.getInteger("pageIndex"),
				param.getInteger("pageSize"), param.getInteger("payStatus"), param.getString("orderNo"),
				param.getInteger("status"), param.getString("consultantName"), param.getString("supervisorName"));
	}

	/**
	 * 根据洗车单主键查询洗车单详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindWashOrderDetaileByWwId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindWashOrderDetaileByWwId(@RequestBody JSONObject param) {
		return iWorkRepairService.clientFindWashOrderDetaileByWwId(param.getString("wwId"));
	}

	/**
	 * 查询维修单的材料列表
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientFindMaterialByRepairId", method = RequestMethod.POST)
	@ResponseBody
	public String clientFindMaterialByRepairId(@RequestBody JSONObject param) {
		return JSON.toJSONStringWithDateFormat(iWorkRepairService.findMaterialByRepairId(param.getString("wrId")),
				"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 客户端-库管确认价格
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientLibraryConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String clientLibraryConfirm(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.clientLibraryConfirm(param);
	}

	/**
	 * 客户端-库管修改用料价格-单独修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientUpdateOrderMaterial", method = RequestMethod.POST)
	@ResponseBody
	public String clientUpdateOrderMaterial(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.clientUpdateOrderMaterial(param);
	}

	/**
	 * 财务-维修单支付
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientRepairPay", method = RequestMethod.POST)
	@ResponseBody
	public String clientRepairPay(@RequestBody JSONObject param) {
		return iWorkRepairService.clientRepairPay(param);
	}

	/**
	 * 维修单删除
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteRepairOrder", method = RequestMethod.POST)
	@ResponseBody
	public String clientDeleteRepairOrder(@RequestBody JSONObject param) {
		return iWorkRepairService.clientDeleteRepairOrder(param.getString("wrId"));
	}

	/**
	 * 财务-洗车单支付
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientWashPay", method = RequestMethod.POST)
	@ResponseBody
	public String clientWashPay(@RequestBody JSONObject param) {
		return iWorkRepairService.clientWashPay(param);
	}

	/**
	 * 洗车单删除
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteWashOrder", method = RequestMethod.POST)
	@ResponseBody
	public String clientDeleteWashOrder(@RequestBody JSONObject param) {
		return iWorkRepairService.clientDeleteWashOrder(param.getString("wwId"));
	}

	/**
	 * 预购订单到货通知
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientNoticPreOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientNoticPreOrder(@RequestBody JSONObject param) {
		return orderService.clientNoticPreOrder(param.getString("orderId"));
	}
	
	/**
	 * 商品订单-确认退款
	 * 
	 * @return
	 */
	@WeihAuthLog(logModule = "订单管理", logFeatures = "确认退款")
	@RequestMapping(value = "/clientOrderConfirmRefund", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientOrderConfirmRefund(@RequestBody JSONObject json) {
		return orderService.orderConfirmRefund(json.getString("orderId"));
	}
	
	/**
	 * 后台：导出售后申请列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/clientExportAftersaleApplyInfo", method = RequestMethod.GET)
	public void clientExportAftersaleApplyInfo(HttpServletRequest request,HttpServletResponse response){
		
		aftersaleApplyService.exportAftersaleApplyInfo(request, response);
	}
	
	/**
	 * 后台：导出商品订单列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clientExportProductOrder",method=RequestMethod.GET)
	public void clientExportProductOrder(HttpServletRequest request,HttpServletResponse response){
		
		orderService.exportProductOrder(request, response);
	}
	
	/**
	 * 后台：导出预约订单列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clientExportReservationOrder",method=RequestMethod.GET)
	public void clientExportReservationOrder(HttpServletRequest request,HttpServletResponse response){
		
		reservationService.exportReservationOrder(request, response);
	}
	
	/**
	 * 后台：导出维修单列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clientExportRepairServiceOrder",method=RequestMethod.GET)
	public void clientExportRepairServiceOrder(HttpServletRequest request,HttpServletResponse response){
		
		iWorkRepairService.exportRepairServiceOrder(request, response);
	}
	
	/**
	 * 后台：导出洗车单列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clientExportWashServiceOrder",method=RequestMethod.GET)
	public void clientExportWashServiceOrder(HttpServletRequest request,HttpServletResponse response){
		
		iWorkRepairService.exportWashServiceOrder(request, response);
	}
	
	/**
	 * 后台：导出特殊订单列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/clientExportSpecialOrder",method=RequestMethod.GET)
	public void clientExportSpecialOrder(HttpServletRequest request,HttpServletResponse response){
		
		iSpecialOrderService.exportSpecialOrder(request, response);
	}
}
