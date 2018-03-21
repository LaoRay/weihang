package cn.com.clubank.weihang.manage.repair.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOrderMaterial;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;

/**
 * 维修业务
 * 
 * @author YangWei
 *
 */
public interface IWorkRepairService {

	/**
	 * 扫一扫接车
	 * 
	 * 修改预约单的状态和跟进客服等信息；创建接车单；返回会员、车辆及预约单信息
	 * 
	 * @param carNo
	 *            车牌号
	 * @param dutyType
	 *            职位序号（1-服务主管，2-服务顾问）
	 * @return
	 */
	public String scanReceive(String carNo, String userId, Integer dutyType);

	/**
	 * pc端查询维修单
	 * 
	 * @param payStatus
	 *            支付状态
	 * @param status
	 *            订单状态
	 * @param orderNo
	 *            订单号
	 * @return
	 */
	public String clientFindRepairServiceOrder(Integer pageIndex, Integer pageSize, Integer payStatus, String orderNo,
			Integer status);

	/**
	 * pc端查询洗车单
	 * 
	 * @param payStatus
	 *            支付状态
	 * @return
	 */
	public String clientFindWashServiceOrder(Integer pageIndex, Integer pageSize, Integer payStatus, String orderNo,
			Integer status);

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
	public String findOrderListAll(String carNo, String userId, Integer dutyType, Integer pageIndex, Integer pageSize);

	/**
	 * 查询未完成的工单列表
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
	public String findOrderListUndone(String carNo, String userId, Integer dutyType, Integer pageIndex,
			Integer pageSize);

	/**
	 * 查询已完成的工单列表
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
	public String findOrderListDone(String carNo, String userId, Integer dutyType, Integer pageIndex, Integer pageSize);

	/**
	 * 客户查询维修单
	 * 
	 * @param customerId
	 *            客户id
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	public String customerFindRepairOrderList(String customerId, Integer type, Integer pageIndex, Integer pageSize);

	/**
	 * 客户查询洗车单
	 * 
	 * @param customerId
	 *            客户id
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	public String customerFindWashOrderList(String customerId, Integer type, Integer pageIndex, Integer pageSize);

	/**
	 * 查看费用详情
	 * 
	 * @param wrId
	 * @return
	 */
	CommonResult CheckCostInfo(String wrId);

	/**
	 * 保存维修单信息
	 * 
	 * @param info
	 * @param wpoId
	 *            接车单id
	 * @return
	 */
	String saveOrderInfo(JSONObject param);

	/**
	 * 保存工时费用信息
	 * 
	 * @param info
	 * @param workHour
	 */
	void saveWorkHour(WorkRepair info, JSONArray workHour);

	/**
	 * 保存车辆状态图片
	 * 
	 * @param info
	 * @param workHour
	 */
	void saveCarPic(String wrId, String paths);

	/**
	 * 保存用料信息
	 * 
	 * @param info
	 * @param material
	 */
	void saveMaterial(WorkRepair info, JSONArray material);

	/**
	 * 根据主键删除工时费用
	 * 
	 * @param whId
	 */
	int deleteWorkHourByKey(String whId);

	/**
	 * 根据主键删除用料信息
	 * 
	 * @param womId
	 */
	int deleteMaterialByKey(String womId);

	/**
	 * 获取维修单详情（包括维修项目及维修材料列表信息）
	 * 
	 * @param repairId
	 *            维修单id
	 * @return
	 */
	String getReapirDetailByKey(String repairId);

	/**
	 * 获取洗车单详情
	 * 
	 * @param washId
	 *            洗车单id
	 * @return
	 */
	String getWashDetailByKey(String washId);

	/**
	 * 获取接车单详情
	 * 
	 * @param wpoId
	 *            接车单id
	 * @return
	 */
	String getPickupOrderDetailByKey(String wpoId);

	/**
	 * 车间组长-获取当前维修单详情
	 * 
	 * @return
	 */
	String getInServeReapirDetailByCarNo(String carNo, String userId);

	/**
	 * 洗车组长-获取当前洗车单详情
	 * 
	 * @return
	 */
	String getInServeWashDetailByCarNo(String carNo, String userId);

	/**
	 * 查询维修单的材料列表
	 * 
	 * @param wrId
	 *            维修单id
	 * @return
	 */
	List<WorkOrderMaterial> findMaterialByRepairId(String wrId);

	/**
	 * 根据车牌号获取当前未完成的维修单
	 * 
	 * @return
	 */
	WorkRepair getInServeRepairByCarNo(String carNo);

	/**
	 * 后台：财务-维修单支付
	 * 
	 * @return
	 */
	String clientRepairPay(JSONObject param);

	/**
	 * 后台：财务-洗车单支付
	 * 
	 * @return
	 */
	String clientWashPay(JSONObject param);

	/**
	 * 后台：维修单删除
	 * 
	 * @param string
	 * @return
	 */
	String clientDeleteRepairOrder(String wrId);

	/**
	 * 后台：洗车单删除
	 * 
	 * @param string
	 * @return
	 */
	String clientDeleteWashOrder(String wwId);

	/**
	 * 后台：根据维修单主键查询维修单详情
	 * 
	 * @param wrId
	 * @return
	 */
	CommonResult clientFindRepairOrderDetailByWrId(String wrId);

	/**
	 * 根据洗车单主键查询洗车单详情
	 * 
	 * @param wwId
	 * @return
	 */
	CommonResult clientFindWashOrderDetaileByWwId(String wwId);
}
