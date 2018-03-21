package cn.com.clubank.weihang.restful.repair;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.repair.service.IWashOrderFlowService;

/**
 * 洗车单流转状态
 * 
 * @author YangWei
 *
 */
@Controller
public class WashOrderStatusController {

	@Resource
	private IWashOrderFlowService iWashOrderFlowService;
	
	/**
	 * 服务顾问-洗车派单
	 * @param wpoId		接车单id
	 * @param washLeader	洗车组长
	 * @return
	 */
	@RequestMapping(value="/repairConsultantWashAssign", method=RequestMethod.POST)
	@ResponseBody
	public String consultantWashAssign(@RequestBody JSONObject param) {
		return iWashOrderFlowService.washAssign(param.getString("wpoId"), param.getString("washLeader"));
	}
	
	/**
	 * 洗车组长-接车
	 * @param washId	洗车单id
	 * @param engineer	洗车师
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderReceive", method=RequestMethod.POST)
	@ResponseBody
	public String receive(@RequestBody JSONObject param) {
		return iWashOrderFlowService.receive(param.getString("washId"), param.getString("engineer"));
	}
	
	/**
	 * 洗车组长-提交洗车单
	 * @param washId	
	 * @param washType	洗车类型（洗车项目）
	 * @param washCost	洗车费用
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderSubmit", method=RequestMethod.POST)
	@ResponseBody
	public String submit(@RequestBody JSONObject param) {
		if (!param.containsKey("washCost")) {
			JSONObject json = new JSONObject();
			json.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			json.put("msg", "参数错误！");
			return json.toString();
		}
		return iWashOrderFlowService.submit(param.getString("washId"), param.getInteger("washType"), new BigDecimal(param.getString("washCost")));
	}
	
	/**
	 * 客户-确认项目-修改洗车项目
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderCustomerUpdate", method=RequestMethod.POST)
	@ResponseBody
	public String customerUpdate(@RequestBody JSONObject param) {
		return iWashOrderFlowService.customerUpdate(param.getString("washId"));
	}
	
	/**
	 * 洗车组长-开始洗车
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderStart", method=RequestMethod.POST)
	@ResponseBody
	public String start(@RequestBody JSONObject param) {
		return iWashOrderFlowService.start(param.getString("washId"));
	}
	
	/**
	 * 洗车组长-完成洗车
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderComplete", method=RequestMethod.POST)
	@ResponseBody
	public String complete(@RequestBody JSONObject param) {
		return iWashOrderFlowService.complete(param.getString("washId"));
	}
	
	/**
	 * 洗车组长-返工清洗
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderRework", method=RequestMethod.POST)
	@ResponseBody
	public String rework(@RequestBody JSONObject param) {
		return iWashOrderFlowService.rework(param.getString("washId"));
	}
	
	/**
	 * 客户-确认洗车结果-确定
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderResultAgree", method=RequestMethod.POST)
	@ResponseBody
	public String resultAgree(@RequestBody JSONObject param) {
		return iWashOrderFlowService.result(param.getString("washId"), Constants.INT_1, null);
	}
	
	/**
	 * 客户-确认洗车结果-不同意
	 * @param washId	
	 * @return
	 */
	@RequestMapping(value="/repairWashOrderResultDisagree", method=RequestMethod.POST)
	@ResponseBody
	public String resultDisagree(@RequestBody JSONObject param) {
		return iWashOrderFlowService.result(param.getString("washId"), Constants.INT_0, param.getString("opinion"));
	}
	
}
