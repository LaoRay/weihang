package cn.com.clubank.weihang.restful.repair;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.repair.service.IRepairOrderFlowService;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;

/**
 * 维修单流转状态
 * 
 * @author YangWei
 *
 */
@Controller
public class RepairOrderStatusController {

	@Resource
	private IRepairOrderFlowService iRepairOrderFlowService;
	
	@Resource
	private IWorkRepairService iWorkRepairService;
	
	/**
	 * 根据主键删除工时费用
	 * @param whId	工时费用记录主键
	 * @return
	 */
	@RequestMapping(value="/repairDeleteWorkHourByKey", method=RequestMethod.POST)
	@ResponseBody
	public String deleteWorkHour(@RequestBody JSONObject param) {
		JSONObject json = new JSONObject();
		if (iWorkRepairService.deleteWorkHourByKey(param.getString("whId")) == 0) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("errorMsg", "数据不存在！");
			return json.toString();
		}
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	/**
	 * 根据主键删除用料清单
	 * @param womId	用料清单表记录主键
	 * @return
	 */
	@RequestMapping(value="/repairDeleteMaterialKey", method=RequestMethod.POST)
	@ResponseBody
	public String deleteMaterial(@RequestBody JSONObject param) {
		JSONObject json = new JSONObject();
		if (iWorkRepairService.deleteMaterialByKey(param.getString("womId")) == 0) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("errorMsg", "数据不存在！");
			return json.toString();
		}
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}
	
	/**
	 * 保存维修单信息
	 * @param wpoId	接车单id
	 * @param item	维修项目：json数组
	 * @return
	 */
	@RequestMapping(value="/repairSaveOrderInfo", method=RequestMethod.POST)
	@ResponseBody
	public String saveOrderInfo(@RequestBody JSONObject param) {
		return iWorkRepairService.saveOrderInfo(param);
	}
	
	/**
	 * 服务顾问-维修派单
	 * @param repairId		维修单id
	 * @param repairSupervisor	车间组长(维修组长)
	 * @return
	 */
	@RequestMapping(value="/repairOrderAssign", method=RequestMethod.POST)
	@ResponseBody
	public String assign(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.assign(param.getString("repairId"), param.getString("repairSupervisor"));
	}
	
	/**
	 * 车间组长-接车
	 * @param repairId		维修单id
	 * @param engineer		技师
	 * @return
	 */
	@RequestMapping(value="/repairOrderReceive", method=RequestMethod.POST)
	@ResponseBody
	public String receive(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.receive(param.getString("repairId"), param.getString("engineer"));
	}
	
	/**
	 * 车间组长-提交维修单
	 * @param repairId	维修单id
	 * @param material	材料信息：json数组
	 * @param workHour	工时费用：json数组
	 * @return
	 */
	@RequestMapping(value="/repairOrderSubmit", method=RequestMethod.POST)
	@ResponseBody
	public String submit(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.submit(param.getString("repairId"), param.getJSONArray("material"), param.getJSONArray("workHour"));
	}
	
	/**
	 * 服务顾问-确认项目
	 * @param repairId	维修单id
	 * @param material	材料信息：json数组（只有id、数量、费用）
	 * @param workHour	工时费用：json数组（只有id、费用）
	 * @return
	 */
	@RequestMapping(value="/repairOrderConsultantOK", method=RequestMethod.POST)
	@ResponseBody
	public String consultantOK(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.consultantOK(param.getString("repairId"), null, param.getJSONArray("workHour"));
	}
	
	/**
	 * 客户-确认项目-修改维修项目
	 * @param repairId	维修单id
	 * @return
	 */
	@RequestMapping(value="/repairOrderCustomerUpdate", method=RequestMethod.POST)
	@ResponseBody
	public String customerUpdate(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.customerUpdate(param.getString("repairId"));
	}
	
	/**
	 * 车间组长-开始（返工）维修
	 * @param repairId	维修单id
	 * @return
	 */
	@RequestMapping(value="/repairOrderInService", method=RequestMethod.POST)
	@ResponseBody
	public String inService(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.inService(param.getString("repairId"));
	}
	
	/**
	 * 车间组长-完成
	 * @param repairId	维修单id
	 * @return
	 */
	@RequestMapping(value="/repairOrderComplete", method=RequestMethod.POST)
	@ResponseBody
	public String complete(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.complete(param.getString("repairId"));
	}
	
	/**
	 * 客户-确认维修结果-确定
	 * @param repairId	
	 * @return
	 */
	@RequestMapping(value="/repairOrderResultAgree", method=RequestMethod.POST)
	@ResponseBody
	public String resultAgree(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.result(param.getString("repairId"), Constants.INT_1, null);
	}
	
	/**
	 * 客户-确认维修结果-不同意
	 * @param repairId	
	 * @return
	 */
	@RequestMapping(value="/repairOrderResultDisagree", method=RequestMethod.POST)
	@ResponseBody
	public String resultDisagree(@RequestBody JSONObject param) {
		return iRepairOrderFlowService.result(param.getString("repairId"), Constants.INT_0, param.getString("opinion"));
	}
}
