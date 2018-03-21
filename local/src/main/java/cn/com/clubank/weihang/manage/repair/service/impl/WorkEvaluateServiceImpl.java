package cn.com.clubank.weihang.manage.repair.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkEvaluateMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkRepairMapper;
import cn.com.clubank.weihang.manage.repair.dao.WorkWashMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkEvaluate;
import cn.com.clubank.weihang.manage.repair.pojo.WorkOperation;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.repair.pojo.WorkRepair;
import cn.com.clubank.weihang.manage.repair.pojo.WorkWash;
import cn.com.clubank.weihang.manage.repair.service.IWorkEvaluateService;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 评价表业务逻辑层
 * 
 * @author Liangwl
 *
 */
@Slf4j
@Service
public class WorkEvaluateServiceImpl implements IWorkEvaluateService {

	@Resource
	private WorkEvaluateMapper workEvaluateMapper;
	
	@Resource
	private WorkRepairMapper workRepairMapper;
	
	@Resource
	private WorkWashMapper workWashMapper;
	
	@Resource
	private WorkPickupOrderMapper workPickupOrderMapper;
	
	@Resource
	private CustomerInfoMapper customerInfoMapper;
	
	/**
	 * 保存评价信息
	 */
	@Override
	public CommonResult addEvaluateInfo(WorkEvaluate info) {
		log.info("保存评价信息：{}", JSON.toJSONStringWithDateFormat(info, Constants.DATE_COMMON));
		WorkPickupOrder wpo = null;
		if (info.getEvaluateType() == WorkOperation.WORK_TYPE_REPAIR) {
			WorkRepair wr = workRepairMapper.selectByNo(info.getEvaluateNo());
			if (wr != null) {
				info.setConsultantId(wr.getConsultantBy());//服务顾问
				info.setSupervisorId(wr.getSupervisorBy()); //技师
				wpo = workPickupOrderMapper.selectByNo(wr.getWpoNo());
				info.setWpoId(wpo == null ? null : wpo.getWpoId()); //接车单
			} else {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误，维修单不存在");
			}
		} else if (info.getEvaluateType() == WorkOperation.WORK_TYPE_WASH) {
			WorkWash ww = workWashMapper.selectByNo(info.getEvaluateNo());
			if (ww != null) {
				info.setConsultantId(ww.getConsultantBy());//服务顾问
				info.setWasherId(ww.getSupervisorBy());//洗车师
				wpo = workPickupOrderMapper.selectByNo(ww.getWpoNo());
				info.setWpoId(wpo == null ? null : wpo.getWpoId());//接车单
			} else {
				return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误，洗车单不存在");
			}
		} else {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误，没有找到评价类型");
		}
		if (StringUtil.isBlank(info.getEvaluateInfo())) {
			info.setEvaluateInfo("此用户没有填写评价信息。");
		}
		workEvaluateMapper.insert(info);
		return CommonResult.result(ResultCode.SUCC.getValue(), "保存评价信息成功");
	}

	@Override
	public String getConsultantEvaluateList(Integer dutyType, String userId, Integer pageIndex, Integer pageSize) {
		JSONObject json = new JSONObject();
		log.info("查询评价信息：{}，dutyType：{}", userId, dutyType);
		List<WorkEvaluate> list = null;
		int WorkEvaluateTotal = 0; //评论总数
		
		if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			//服务顾问
			list = workEvaluateMapper.selectByConsultantId(userId, PageObject.getStart(pageIndex, pageSize), pageSize);
		} else if (dutyType == BaseUser.DUTY_SERVICE_SUPERVISOR) {
			//服务主管
			list = workEvaluateMapper.selectByServiceSupervisorId(userId, PageObject.getStart(pageIndex, pageSize), pageSize);
			WorkEvaluateTotal = workEvaluateMapper.selectByServiceSupervisorIdTotal(userId);
		} else if (dutyType == BaseUser.DUTY_WORKSHOP_LEADER) {
			//车间组长
			list = workEvaluateMapper.selectByShopSupervisorId(userId, PageObject.getStart(pageIndex, pageSize), pageSize);
			WorkEvaluateTotal = workEvaluateMapper.selectByShopSupervisorIdTotal(userId);
		} else if (dutyType == BaseUser.DUTY_WASH_CAR_LEADER) {
			//洗车组长
			list = workEvaluateMapper.selectByWashLeaderId(userId, PageObject.getStart(pageIndex, pageSize), pageSize);
			WorkEvaluateTotal = workEvaluateMapper.selectByWashLeaderIdTotal(userId);
		}
		
		JSONArray evaluateInfos = new JSONArray();
		JSONObject evaluateInfo = new JSONObject();
		for (WorkEvaluate info : list) {
			evaluateInfo = JSON.parseObject(JSON.toJSONStringWithDateFormat(info, Constants.DATE_COMMON));
			Map<String, Object> map = customerInfoMapper.selectCustomerIntegralGradeInfo(info.getEvaluateBy());
			if (map != null) {
				evaluateInfo.put("headIcon", map.containsKey("headIcon") ? map.get("headIcon").toString() : Constants.NULL);
				evaluateInfo.put("carNo", map.containsKey("carNo") ? map.get("carNo").toString() : Constants.NULL);
				evaluateInfo.put("level", map.containsKey("level") ? map.get("level").toString() : Constants.NULL);
			} else {
				evaluateInfo.put("headIcon", Constants.NULL);
				evaluateInfo.put("carNo", Constants.NULL);
				evaluateInfo.put("level", Constants.NULL);
			}
			evaluateInfos.add(evaluateInfo);
		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("evaluateInfo", evaluateInfos);
		
		if (dutyType == BaseUser.DUTY_SERVICE_CONSULTANT) {
			//如果是服务顾问，则查询好评率
			// 获得评价统计信息
			Map<String, String> map = workEvaluateMapper.selectCountConsultantEvaluate(userId);
			int total = Integer.parseInt(map.get("total"));// 评价总数
			int good = Integer.parseInt(map.get("good"));// 好评数
			// 好评度
			String goodRate = (total == 0 ? "0" : String.format("%.0f", (double) good * 100 / total) + "%");
			
			dataMap.put("total", total);
			dataMap.put("goodRate", goodRate);
		} else {
			dataMap.put("total", WorkEvaluateTotal);
			dataMap.put("goodRate", Constants.NULL);
		}

		
		log.info("查询评价信息成功：{}", dataMap.size());
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "成功");
		json.put("data", dataMap);
		return JSON.toJSONStringWithDateFormat(json, Constants.DATE_COMMON);
	}
}
