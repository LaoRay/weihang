package cn.com.clubank.weihang.manage.repair.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.repair.pojo.WorkEvaluate;

/**
 * 评价表（业务逻辑层接口）
 * @author Liangwl
 *
 */
public interface IWorkEvaluateService {

	/**
	 * 保存评价信息
	 * @param workEvaluate
	 * @return
	 */
	CommonResult addEvaluateInfo(WorkEvaluate workEvaluate);
	
	/**
	 * 获得评价列表
	 * @param dutyType 职位类型
	 * @param userId 用户ID
	 * @param pageIndex 页码下标
	 * @param pageSize 每页行数
	 * @return
	 */
	String getConsultantEvaluateList(Integer dutyType, String userId, Integer pageIndex, Integer pageSize);
}
