package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CustomerSignMapper;
import cn.com.clubank.weihang.manage.member.pojo.CustomerSign;
import cn.com.clubank.weihang.manage.member.service.CustomerSignService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.IntegralRule;
import cn.com.clubank.weihang.manage.systemSettings.service.IntegralRuleService;

/**
 * 会员签到Service
 * 
 * @author LeiQY
 *
 */
@Service
public class CustomerSignServiceImpl implements CustomerSignService {

	@Autowired
	private CustomerSignMapper signMapper;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private IntegralRuleService integralRuleService;

	/**
	 * 获取会员签到列表
	 */
	@Override
	public CommonResult getSignList(String customerId) {
		if (StringUtils.isBlank(customerId)) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "用户不存在");
		}
		List<CustomerSign> signlist = signMapper.selectByCustomerId(customerId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "获取签到列表成功", signlist);
	}

	/**
	 * 签到
	 */
	@Override
	public CommonResult sign(String customerId, String signTime) {
		if (!DateUtil.isToday(signTime)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "签到日期与服务器日期不符");
		}
		// 获取签到记录列表
		List<CustomerSign> signlist = signMapper.selectByCustomerId(customerId);
		// 连续签到次数
		Integer sustain_day = Constants.INT_1;
		// 签到赠送积分
		Integer dealIntegral = Constants.INT_0;
		Integer value = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.SIGN_INTEGRAL);
		if (value != null) {
			dealIntegral = value;
		}
		// 如果有签到记录
		if (signlist != null && signlist.size() > 0) {
			// 获取最后一次签到记录
			CustomerSign lastSign = signlist.get(signlist.size() - Constants.INT_1);
			// 获取最后一次签到时间
			Date lastSignDate = lastSign.getSignTime();

			if (DateUtil.isToday(lastSignDate)) {
				// 如果为true，已签到
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "今天已签到");
			}
			// 判断连续登录
			// 最后一次签到如果是昨天，说明是连续签到，连续签到次数+1
			if (DateUtil.isYesterday(lastSignDate)) {
				sustain_day = lastSign.getSustainDay() + Constants.INT_1;
			}
			// 如果连续签到7天，赠送积分
			if (sustain_day % Constants.INT_7 == 0) {
				Integer val = integralRuleService.getIntegralRuleValueByRuleCode(IntegralRule.SUSTAINSIGN_INTEGRAL);
				if (val != null) {
					dealIntegral = val;
				}
			}
		}
		// 签到
		CustomerSign sign = new CustomerSign();
		sign.setCustomerId(customerId);
		// 设置连续签到次数
		sign.setSustainDay(sustain_day);
		// 签到赠送积分
		sign.setBestowalIntegral(dealIntegral);
		signMapper.insert(sign);
		// 签到为客户增加积分
		integralService.signIntegral(customerId, dealIntegral);
		return CommonResult.result(ResultCode.SUCC.getValue(), "签到成功");
	}
}
