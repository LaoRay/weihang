package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductAftersaleApplyMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;
import cn.com.clubank.weihang.manage.product.service.IProductAftersaleApplyService;

/**
 * 售后申请管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductAftersaleApplyServiceImpl implements IProductAftersaleApplyService {

	@Autowired
	private ProductAftersaleApplyMapper productAftersaleApplyMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;

	/**
	 * 保存售后申请信息
	 */
	@Override
	public CommonResult insertApplyInfo(ProductAftersaleApply apply) {
		productAftersaleApplyMapper.insert(apply);
		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(apply.getOrderDetailId());
		orderDetail.setIsSaleService(true);
		orderDetailMapper.updateByPrimaryKey(orderDetail);
		return CommonResult.result(ResultCode.SUCC.getValue(), "申请退货成功，工作人员会在近期联系您，请保持手机畅通，谢谢您的支持！");
	}

	/**
	 * 查询售后申请列表
	 */
	@Override
	public CommonResult findApplyInfoList(String customerId, Integer pageIndex, Integer pageSize) {
		List<Map<String, String>> applyInfoList = productAftersaleApplyMapper.selectListByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询售后申请列表成功", applyInfoList);
	}

	/**
	 * 后台：查询售后申请列表
	 */
	@Override
	public CommonResult clientFindApplyInfoList(Integer pageIndex, Integer pageSize, Integer status, String startDate,
			String endDate) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		Integer total = productAftersaleApplyMapper.selectApplyCount(status, startDate, endDate);
		page.setRows(total);
		List<Map<String, String>> infoList = productAftersaleApplyMapper.selectApplyInfoList(page.getStart(),
				page.getPageSize(), status, startDate, endDate);
		page.setDataList(infoList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询售后申请列表成功", page);
	}

	/**
	 * 后台：处理售后申请
	 */
	@Override
	public CommonResult clientHandleApplyByApplyId(String applyId, String conductBy) {
		if (StringUtils.isBlank(applyId) || StringUtils.isBlank(conductBy)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		ProductAftersaleApply apply = productAftersaleApplyMapper.selectByPrimaryKey(applyId);
		// 处理售后申请，将状态置为已处理
		apply.setStatus(Constants.INT_2);
		apply.setConductBy(conductBy);
		productAftersaleApplyMapper.updateByPrimaryKey(apply);
		return CommonResult.result(ResultCode.SUCC.getValue(), "处理成功");
	}
}
