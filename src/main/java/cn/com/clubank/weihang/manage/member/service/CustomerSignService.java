package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 签到流水表（业务逻辑层接口）
 * 
 * @author LeiQY
 *
 */
public interface CustomerSignService {

	/**
	 * 获取会员签到列表
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult getSignList(String customerId);

	/**
	 * 签到
	 * 
	 * @param customerId
	 * @param signTime
	 * @return
	 */
	CommonResult sign(String customerId, String signTime);

}
