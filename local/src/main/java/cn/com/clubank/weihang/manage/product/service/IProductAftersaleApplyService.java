package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;

public interface IProductAftersaleApplyService {

	/**
	 * 保存申请信息
	 * 
	 * @param apply
	 * @return
	 */
	CommonResult insertApplyInfo(ProductAftersaleApply apply);

	/**
	 * 查询售后申请列表
	 * 
	 * @param customerId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	CommonResult findApplyInfoList(String customerId, Integer pageIndex, Integer pageSize);

	/**
	 * 后台：查询售后申请列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param status
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	CommonResult clientFindApplyInfoList(Integer pageIndex, Integer pageSize, Integer status, String startDate,
			String endDate);

	/**
	 * 后台：处理售后申请
	 * 
	 * @param applyId
	 * @param conductBy
	 * @return
	 */
	CommonResult clientHandleApplyByApplyId(String applyId, String conductBy);
}
