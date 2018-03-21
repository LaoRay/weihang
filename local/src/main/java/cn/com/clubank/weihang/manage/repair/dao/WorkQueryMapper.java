package cn.com.clubank.weihang.manage.repair.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 自定义的查询统计方法
 * 
 * @author YangWei
 *
 */
public interface WorkQueryMapper {

	/**
	 * 客户查询维修单
	 * 
	 * @param customerId
	 *            客户id
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	List<Map<String, Object>> customerFindRepairOrderList(@Param("customerId") String customerId,
			@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 客户查询洗车单
	 * 
	 * @param customerId
	 *            客户id
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	List<Map<String, Object>> customerFindWashOrderList(@Param("customerId") String customerId,
			@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务主管-查询所有接车单
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> serveSupervisorQueryAllOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务主管-查询未完成的接车单
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> serveSupervisorQueryUndoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务主管-查询已完成的接车单
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> serveSupervisorQueryDoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务顾问-查询所有
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> consultantQueryAllOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务顾问-查询未完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> consultantQueryUndoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 服务顾问-查询已完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> consultantQueryDoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 车间主管-查询所有
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> shopSupervisorQueryAllOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 车间主管-查询未完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> shopSupervisorQueryUndoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 车间主管-查询已完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> shopSupervisorQueryDoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 洗车组长-查询所有
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> washLeaderQueryAllOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 洗车组长-查询未完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> washLeaderQueryUndoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 洗车组长-查询已完成
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> washLeaderQueryDoneOrder(String userId, String carNo,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 查询维修单-分页
	 * 
	 * @param supervisorBy
	 * @param consultantBy
	 * @param status
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	List<Map<String, Object>> clientFindRepairServiceOrderPage(@Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize, @Param("payStatus") Integer payStatus,
			@Param("orderNo") String orderNo, @Param("status") Integer status);

	/**
	 * 查询维修单-所有
	 */
	int clientFindRepairServiceOrderTotal(@Param("payStatus") Integer payStatus, @Param("orderNo") String orderNo,
			@Param("status") Integer status);

	/**
	 * 查询洗车单-分页
	 */
	List<Map<String, Object>> clientFindWashServiceOrderPage(@Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize, @Param("payStatus") Integer payStatus,
			@Param("orderNo") String orderNo, @Param("status") Integer status);

	/**
	 * 查询洗车单-所有
	 * 
	 * @param userId
	 * @param carNo
	 * @return
	 */
	int clientFindWashServiceOrderTotal(@Param("payStatus") Integer payStatus, @Param("orderNo") String orderNo,
			@Param("status") Integer status);

}