package cn.com.clubank.weihang.manage.systemSettings.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.InvoiceInfo;

/**
 * 发票信息管理
 * @author YangWei
 *
 */
public interface InvoiceInfoService {

	/**
	 * 增加发票信息
	 * 
	 * @param baseParameter
	 * @return
	 */
	CommonResult add(InvoiceInfo info);
	
	/**
	 * 已开票
	 * @param invId
	 * @param status
	 * @return
	 */
	CommonResult finishInvoice(String invId);
	
}
