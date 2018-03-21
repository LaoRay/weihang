package cn.com.clubank.weihang.manage.repair.service;

import java.math.BigDecimal;

/**
 * 洗车单流转业务管理
 * 
 * @author YangWei
 *
 */
public interface IWashOrderFlowService {

	/**
	 * 服务顾问-洗车派单
	 * @param wpoId		接车单id
	 * @param washLeader	洗车组长
	 * @param orderDetailIds	使用所购买的商品
	 * @return
	 */
	public String washAssign(String wpoId, String washLeader, String orderDetailIds);
	
	/**
	 * 洗车组长-接车
	 * @param washId	洗车单id
	 * @param engineer	洗车师
	 * @return
	 */
	public String receive(String washId, String engineer);
	
	/**
	 * 洗车组长-提交洗车单
	 * @param washId	
	 * @param washType	洗车类型（洗车项目）
	 * @param washCost	洗车费用
	 * @return
	 */
	public String submit(String washId, Integer washType, BigDecimal washCost);
	
	/**
	 * 客户-确认项目-修改洗车项目
	 * @param washId	
	 * @return
	 */
	public String customerUpdate(String washId);
	
	/**
	 * 洗车组长-开始洗车
	 * @param washId	
	 * @return
	 */
	public String start(String washId);
	
	/**
	 * 洗车组长-完成洗车
	 * @param washId	
	 * @return
	 */
	public String complete(String washId);
	
	/**
	 * 洗车组长-返工清洗
	 * @param washId	
	 * @return
	 */
	public String rework(String washId);
	
	/**
	 * 客户-确认洗车结果
	 * @param washId	
	 * @param type		0-不同意，1-确定
	 * @param opinion	不同意时的意见
	 * @return
	 */
	public String result(String washId, Integer type, String opinion);
	
}
