package cn.com.clubank.weihang.manage.repair.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 维修单流转业务管理
 * 
 * @author YangWei
 *
 */
public interface IRepairOrderFlowService {

	/**
	 * 服务主管-派单
	 * @param wpoId		接车单id
	 * @param consultant	服务顾问
	 * @return
	 */
	public String supervisorAssign(String wpoId, String consultant);
	
	/**
	 * 服务顾问-维修派单
	 * @param repairId		维修单id
	 * @param repairSupervisor	车间组长(维修组长)
	 * @return
	 */
	public String assign(String repairId, String repairSupervisor);
	
	/**
	 * 车间组长-接车
	 * @param repairId		维修单id
	 * @param engineer		技师
	 * @return
	 */
	public String receive(String repairId, String engineer);
	
	/**
	 * 车间组长-提交维修单
	 * @param repairId	维修单id
	 * @param material	材料信息：json数组
	 * @param workHour	工时费用：json数组
	 * @return
	 */
	public String submit(String repairId, JSONArray material, JSONArray workHour);
	
	/**
	 * 服务顾问-确认项目
	 * @param repairId	维修单id
	 * @param material	材料信息：json数组（只有id、数量、费用）
	 * @param workHour	工时费用：json数组（只有id、费用）
	 * @return
	 */
	public String consultantOK(String repairId, JSONArray material, JSONArray workHour);
	
	/**
	 * 客户-确认项目-修改维修项目
	 * @param repairId	维修单id
	 * @return
	 */
	public String customerUpdate(String repairId);
	
	/**
	 * 车间组长-开始（返工）维修
	 * @param repairId	维修单id
	 * @return
	 */
	public String inService(String repairId);
	
	/**
	 * 车间组长-完成
	 * @param repairId	维修单id
	 * @return
	 */
	public String complete(String repairId);
	
	/**
	 * 客户-确认维修结果
	 * @param repairId	
	 * @param type		0-不同意，1-确定
	 * @param opinion	不同意时的意见
	 * @return
	 */
	public String result(String repairId, Integer type, String opinion);
	
	/**
	 * 客户端-库管确认价格
	 * @return
	 */
	String clientLibraryConfirm(JSONObject param);
	
	/**
	 * 客户端-库管修改用料价格-单独修改
	 * @return
	 */
	public String clientUpdateOrderMaterial(JSONObject param);
}
