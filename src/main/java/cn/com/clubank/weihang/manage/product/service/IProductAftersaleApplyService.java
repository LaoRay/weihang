package cn.com.clubank.weihang.manage.product.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	 * @param conductBy		处理人
	 * @param applyId		售后申请id
	 * @param conductResult	处理结果
	 * @return
	 */
	CommonResult clientHandleApplyByApplyId(String conductBy, String applyId, String conductResult);
	
	/**
	 * 后台：驳回售后申请
	 * @param conductBy		处理人
	 * @param applyId		售后申请id
	 * @param conductResult	处理结果
	 * @return
	 */
	CommonResult clientRejectApplyByApplyId(String conductBy, String applyId, String conductResult);
	
	/**
	 * 导出售后申请列表
	 * @param request
	 * @param response
	 */
	void exportAftersaleApplyInfo(HttpServletRequest request, HttpServletResponse response);
}
