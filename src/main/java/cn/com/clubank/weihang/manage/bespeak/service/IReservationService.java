package cn.com.clubank.weihang.manage.bespeak.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;

/**
 * 预约单表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface IReservationService {

	/**
	 * 预约信息保存
	 * 
	 * @param carNo
	 * @param record
	 * @return
	 */
	CommonResult save(WorkReservationOrder record);

	/**
	 * 查看预约记录
	 * 
	 * @param customerId
	 * @param reservationStatus
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult selectByCustomerId(String customerId, Integer reservationStatus, Integer pageIndex, Integer pageSize);

	/**
	 * 取消预约单
	 * 
	 * @param reservationOrderNo
	 * @param customerId
	 * @return
	 */
	CommonResult cancel(String reservationOrderNo, String customerId);

	/**
	 * 查看某预约单详情
	 * 
	 * @param reservationOrderNo
	 *            预约单号
	 * @return
	 */
	CommonResult selectByReservationOrderNo(String reservationOrderNo);

	int updateByPrimaryKey(WorkReservationOrder record);

	/**
	 * 根据车牌号查询未完成的预约单
	 * 
	 * @param carNo
	 *            车牌号
	 * @return
	 */
	WorkReservationOrder selectUndoneByCarNo(String carNo);

	/**
	 * 后台：查询预约单列表
	 * 
	 * @return
	 */
	String clientFindReservationOrder(Integer pageIndex, Integer pageSize, String carNo, Integer orderStatus,
			String reservationDateStart, String reservationDateEnd);

	/**
	 * 后台：删除预约单
	 * 
	 * @param roId
	 * @return
	 */
	CommonResult clientDeleteReservationOrder(String roId);

	/**
	 * 处理过期的预约单
	 */
	void handleTimeOut();

	/**
	 * pc：查询预约单列表
	 * 
	 * @param customerId
	 * @param reservationStatus
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult websiteBespeakViewRecord(String customerId, Integer reservationStatus, Integer pageIndex,
			Integer pageSize);
	
	/**
	 * 导出预约订单列表
	 * @param request
	 * @param response
	 */
	void exportReservationOrder(HttpServletRequest request, HttpServletResponse response);
}
