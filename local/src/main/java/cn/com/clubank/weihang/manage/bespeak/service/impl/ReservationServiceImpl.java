package cn.com.clubank.weihang.manage.bespeak.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.bespeak.dao.WorkReservationOrderMapper;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 预约单表业务逻辑层
 * 
 * @author Liangwl
 *
 */
@Slf4j
@Service
public class ReservationServiceImpl implements IReservationService {

	@Resource
	private WorkReservationOrderMapper workReservationOrderMapper;
	
	@Resource
	private WorkPickupOrderMapper workPickupOrderMapper;

	@Resource
	private CarInfoMapper carInfoMapper;

	@Resource
	private IMessageService imessageService;

	@Autowired
	private BaseCodeRuleService baseCodeRuleService;

	/**
	 * 预约信息保存
	 */
	@Override
	public CommonResult save(WorkReservationOrder record) {
		if (record.getReservationType() == null || record.getReservationType() > 4 || record.getReservationType() < 1) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数【reservationType】错误！");
		}
		// 通过车牌号查询到车辆信息
		CarInfo car = carInfoMapper.selectByCarNo(record.getCarNo());
		if (car == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "车辆不存在！");
		}
		List<WorkReservationOrder> wros = workReservationOrderMapper.selectUndoneByCarNo(record.getCarNo());
		if (wros.size() > 0) {
			return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "此车辆存在未完成的预约单！");
		}
		List<WorkPickupOrder> wpos = workPickupOrderMapper.findUndoneByCarNo(record.getCarNo());
		if (wpos.size() > 0) {
			return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "此车辆存在未完成的工单！");
		}
		// 将此车牌号对应的车辆ID赋值给预约单的车辆ID
		record.setCarId(car.getCarId());
		// 将此车牌号对应的车辆的客户ID赋值给预约单的客户ID
		record.setCustomerId(car.getCustomerId());
		record.setReservationStatus(WorkReservationOrder.STATUS_CONFIRM_YES); // 默认设置成已确认
		record.setReservationOrderNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_YY)); // 预约单号

		workReservationOrderMapper.insert(record);
		return CommonResult.result(ResultCode.SUCC.getValue(), "保存成功");

	}

	/**
	 * 通过会员编号获得会员预约记录
	 */
	@Override
	public CommonResult selectByCustomerId(String customerId, Integer reservationStatus, Integer pageIndex,
			Integer pageSize) {
		List<WorkReservationOrder> list = workReservationOrderMapper.selectByCustomerId(customerId, reservationStatus,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		if (list == null || list.size() == 0) {
			return CommonResult.result(ResultCode.DB_QUERY_EMPTY.getValue(), "此用户无预约信息");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "成功", list);
	}

	/**
	 * 通过预约单号修改预约状态
	 */
	@Override
	public CommonResult cancel(String reservationOrderNo, String customerId) {
		// 查看预约单状态，如果为1（已确认）那么取消之后需要推送消息
		WorkReservationOrder workReservationOrder = workReservationOrderMapper.selectByReservationOrderNo(reservationOrderNo);
		if (workReservationOrder.getReservationStatus() == 3 || workReservationOrder.getReservationStatus() == 4) {
			return CommonResult.result(ResultCode.FAIL.getValue(), "此预约单已完成或已取消");
		}
		workReservationOrderMapper.cancel(reservationOrderNo);
		
		MsgList msg = new MsgList();
		msg.setTitle("取消预约");
		msg.setMsgType(MsgList.MSGTYPE_STATION);
		msg.setContent("【"+reservationOrderNo+"】预约单已取消");
		msg.setObjId(reservationOrderNo);
		imessageService.pushMessage(msg, customerId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "取消成功");
	}

	/**
	 * 通过预约单号查看预约单详情
	 */
	@Override
	public CommonResult selectByReservationOrderNo(String reservationOrderNo) {
		WorkReservationOrder workReservationOrder = workReservationOrderMapper
				.selectByReservationOrderNo(reservationOrderNo);
		if (workReservationOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "数据不存在");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "成功", workReservationOrder);
	}

	@Override
	public WorkReservationOrder selectUndoneByCarNo(String carNo) {
		List<WorkReservationOrder> list = workReservationOrderMapper.selectUndoneByCarNo(carNo);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public int updateByPrimaryKey(WorkReservationOrder record) {
		return workReservationOrderMapper.updateByPrimaryKey(record);
	}

	/**
	 * 后台：查询预约单列表
	 */
	@Override
	public String clientFindReservationOrder(Integer pageIndex, Integer pageSize, String carNo, Integer orderStatus,
			String reservationDateStart, String reservationDateEnd) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);
		int total = workReservationOrderMapper.clientFindReservationOrderTotal(carNo, orderStatus, reservationDateStart,
				reservationDateEnd);
		List<Map<String, Object>> list = workReservationOrderMapper.clientFindReservationOrderPage(page.getStart(),
				page.getPageSize(), carNo, orderStatus, reservationDateStart, reservationDateEnd);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 后台：删除预约单
	 */
	@Override
	public CommonResult clientDeleteReservationOrder(String roId) {
		if (StringUtils.isBlank(roId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "roId为空");
		}
		workReservationOrderMapper.deleteByPrimaryKey(roId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public void handleTimeOut() {
		//超时3小时设置为过期
		int hour = -3;
		
		String hourMinute = DateUtil.addNowTimeHour(hour) + ":00";
		int num = workReservationOrderMapper.setTimeOut(hourMinute);
		log.info("定时将过期的预约单状态设置为已过期，设置数量：{}", num);
	}
}
