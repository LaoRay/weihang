package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.systemSettings.dao.InvoiceInfoMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.InvoiceInfo;
import cn.com.clubank.weihang.manage.systemSettings.service.InvoiceInfoService;

/**
 * 发票信息
 * @author YangWei
 *
 */
@Service
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

	@Resource
	private InvoiceInfoMapper invoiceInfoMapper;
	
	@Override
	public CommonResult add(InvoiceInfo info) {
		if (StringUtil.isBlank(info.getIdentificationCode()) || StringUtil.isBlank(info.getBankId())) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		invoiceInfoMapper.insert(info);
		return new CommonResult(ResultCode.SUCC.getValue(), "新增成功");
	}

	@Override
	public CommonResult finishInvoice(String invId) {
		InvoiceInfo info = invoiceInfoMapper.selectByPrimaryKey(invId);
		if (info == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "信息不存在");
		}
		info.setStatus(InvoiceInfo.STATUS_YES);// 已开票
		invoiceInfoMapper.updateByPrimaryKey(info);
		return new CommonResult(ResultCode.SUCC.getValue(), "更新成功");
	}

}
