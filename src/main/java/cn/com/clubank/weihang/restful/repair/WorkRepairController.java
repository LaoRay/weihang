package cn.com.clubank.weihang.restful.repair;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.material.service.IMaterialStorageService;
import cn.com.clubank.weihang.manage.repair.pojo.WorkEvaluate;
import cn.com.clubank.weihang.manage.repair.service.IRepairOrderFlowService;
import cn.com.clubank.weihang.manage.repair.service.IWorkEvaluateService;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;

@Controller
public class WorkRepairController {

	@Resource
	private IWorkRepairService iWorkRepairService;

	@Resource
	private IWorkEvaluateService iWorkEvaluateService;

	@Resource
	private IMaterialStorageService iMaterialStorageService;
	
	@Resource
	private IRepairOrderFlowService iRepairOrderFlowService;

	/**
	 * 扫一扫接车
	 * 
	 * 修改预约单的状态和跟进客服等信息；创建接车单；返回会员、车辆及预约单信息
	 * 
	 * @param carNo
	 *            车牌号
	 * @param userId
	 *            用户id
	 * @param dutyType
	 *            职位序号（1-服务主管，2-服务顾问）
	 * @return
	 */
	@RequestMapping(value = "/repairScanReceive", method = RequestMethod.POST)
	@ResponseBody
	public String scanReceive(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.scanReceive(param.getString("carNo"), request.getHeader("userId"),
				request.getIntHeader("dutyType"));
	}
	
	/**
	 * 服务主管-派单
	 * @param wpoId		接车单id
	 * @param consultant	服务顾问
	 * @return
	 */
	@RequestMapping(value="/repairSupervisorAssign", method=RequestMethod.POST)
	@ResponseBody
	public String supervisorAssign(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.supervisorAssign(param.getString("wpoId"), param.getString("consultant"));
	}

	/**
	 * 查询所有工单列表
	 * 
	 * 服务主管、服务顾问、车间组长、洗车组长查询自己的工单列表
	 * 
	 * @param carNo
	 *            车牌号模糊查询
	 * @param userId
	 *            用户id
	 * @param dutyType
	 *            职位（1-服务主管，2-服务顾问，3-车间组长，4-洗车组长）
	 * @return
	 */
	@RequestMapping(value = "/repairFindOrderListAll", method = RequestMethod.POST)
	@ResponseBody
	public String repairFindOrderListAll(HttpServletRequest request, @RequestBody JSONObject param) {
		//TODO 暂时未用到，后期如果使用，需要加上技师和洗车师的查询
		
		return iWorkRepairService.findOrderListAll(param.getString("carNo"), request.getHeader("userId"),
				request.getIntHeader("dutyType"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 查询未完成工单列表
	 * 
	 * 服务主管、服务顾问、车间组长、洗车组长查询自己的工单列表
	 * 
	 * @param carNo
	 *            车牌号模糊查询
	 * @param userId
	 *            用户id
	 * @param dutyType
	 *            职位（1-服务主管，2-服务顾问，3-车间组长，4-洗车组长）
	 * @return
	 */
	@RequestMapping(value = "/repairFindOrderListUndone", method = RequestMethod.POST)
	@ResponseBody
	public String repairFindOrderListUndone(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.findOrderListUndone(param.getString("carNo"), request.getHeader("userId"),
				request.getIntHeader("dutyType"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 查询已完成工单列表
	 * 
	 * 服务主管、服务顾问、车间组长、洗车组长查询自己的工单列表
	 * 
	 * @param carNo
	 *            车牌号模糊查询
	 * @param userId
	 *            用户id
	 * @param dutyType
	 *            职位（1-服务主管，2-服务顾问，3-车间组长，4-洗车组长）
	 * @return
	 */
	@RequestMapping(value = "/repairFindOrderListDone", method = RequestMethod.POST)
	@ResponseBody
	public String repairFindOrderListDone(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.findOrderListDone(param.getString("carNo"), request.getHeader("userId"),
				request.getIntHeader("dutyType"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 客户查询维修单
	 * 
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	@RequestMapping(value = "/customerFindRepairOrderList", method = RequestMethod.POST)
	@ResponseBody
	public String customerFindRepairOrderList(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.customerFindRepairOrderList(request.getHeader("customerId"), param.getInteger("type"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 客户查询洗车单
	 * 
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	@RequestMapping(value = "/customerFindWashOrderList", method = RequestMethod.POST)
	@ResponseBody
	public String customerFindWashOrderList(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.customerFindWashOrderList(request.getHeader("customerId"), param.getInteger("type"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 查看费用详情
	 * 
	 * @param wrId
	 *            维修单id
	 * @return
	 */
	@RequestMapping(value = "/repairCheckCostInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult CheckCostInfo(@RequestBody JSONObject param) {
		return iWorkRepairService.CheckCostInfo(param.getString("wrId"));
	}

	/**
	 * 保存评价信息
	 * 
	 * @param workEvaluate
	 * @return
	 */
	@RequestMapping(value = "/repairSaveEvaluateInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult saveEvaluateInfo(HttpServletRequest request, @RequestBody WorkEvaluate workEvaluate) {
		workEvaluate.setEvaluateBy(request.getHeader("customerId"));
		return iWorkEvaluateService.addEvaluateInfo(workEvaluate);
	}

	/**
	 * 分页查询评价列表
	 */
	@RequestMapping(value = "/userGainServiceAdvisorEvaluateList", method = RequestMethod.POST)
	@ResponseBody
	public String getServiceAdvisorEvaluate(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkEvaluateService.getConsultantEvaluateList(request.getIntHeader("dutyType"), request.getHeader("userId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 根据单号获取维修单详情（包括维修项目及维修材料列表信息）
	 * 
	 * @param repairId
	 *            维修单id
	 * @return
	 */
	@RequestMapping(value = "/getReapirOrderDetailById", method = RequestMethod.POST)
	@ResponseBody
	public String getReapirOrderDetailById(@RequestBody JSONObject param) {

		return iWorkRepairService.getReapirDetailByKey(param.getString("repairId"));
	}

	/**
	 * 根据单号获取洗车单详情
	 * 
	 * @param washId
	 *            洗车单id
	 * @return
	 */
	@RequestMapping(value = "/getWashOrderDetailById", method = RequestMethod.POST)
	@ResponseBody
	public String getWashOrderDetailById(@RequestBody JSONObject param) {

		return iWorkRepairService.getWashDetailByKey(param.getString("washId"));
	}

	/**
	 * 车间主管（扫一扫接车）-根据车牌号获取当前维修单详情
	 * 
	 * @param carNo
	 *            车牌号
	 * @return
	 */
	@RequestMapping(value = "/getInServeRepairOrderDetailByCarNo", method = RequestMethod.POST)
	@ResponseBody
	public String getReapirOrderDetailByCarNo(HttpServletRequest request, @RequestBody JSONObject param) {

		return iWorkRepairService.getInServeReapirDetailByCarNo(param.getString("carNo"), request.getHeader("userId"));
	}

	/**
	 * 洗车组长（扫一扫接车）-根据车牌号获取当前洗车单详情
	 * 
	 * @param caoNo
	 *            车牌号
	 * @return
	 */
	@RequestMapping(value = "/getInServeWashOrderDetailByCarNo", method = RequestMethod.POST)
	@ResponseBody
	public String getWashOrderDetailByCarNo(HttpServletRequest request, @RequestBody JSONObject param) {

		return iWorkRepairService.getInServeWashDetailByCarNo(param.getString("carNo"), request.getHeader("userId"));
	}

	/**
	 * 获取维修材料列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/repairGainRepairMaterialList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult gainRepairMaterialList() {

		return iMaterialStorageService.getRepairMaterialList();
	}

	/**
	 * 搜索维修材料
	 * 
	 * @param itemName
	 * @return
	 */
	@RequestMapping(value = "/repairSearchRepairMaterial", method = RequestMethod.POST)
	@ResponseBody
	public String searchRepairMaterial(@RequestBody JSONObject param) {

		return iMaterialStorageService.searchRepairMaterial(param.getString("itemName"));
	}

	/**
	 * 根据接车单号获取接车单详情
	 * 
	 * @param wpoId
	 *            接车单id
	 * @return
	 */
	@RequestMapping(value = "/repairGetPickupOrderDetailById", method = RequestMethod.POST)
	@ResponseBody
	public String repairGetPickupOrderDetailById(@RequestBody JSONObject param) {

		return iWorkRepairService.getPickupOrderDetailByKey(param.getString("wpoId"));
	}
	
	/**
	 * 根据车牌号查询客户未完成的服务类商品和汽车类实体商品列表
	 * @return
	 */
	@RequestMapping(value = "/selectWaitReceiveServiceOrderByCarNo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult selectWaitReceiveServiceOrderByCarNo(@RequestBody JSONObject param) {
		return iWorkRepairService.selectWaitReceiveServiceOrderByCarNo(param.getString("carNo"));
	}
	
	/**
	 * 选择购买的商品-我来接车-员工开始服务订单时选择客户购买的商品使用
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/repairReceiveUseProducts", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult repairReceiveUseProducts(@RequestBody JSONObject param) {
		return iWorkRepairService.repairReceiveUseProducts(param.getString("wpoId"), param.getString("orderDetailIds"));
	}
}
