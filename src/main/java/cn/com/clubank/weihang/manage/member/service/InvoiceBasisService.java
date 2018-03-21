package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.manage.member.pojo.InvoiceBasis;

public interface InvoiceBasisService {

	/**
	 * 通过客户ID获得客户的增票资质列表
	 * @param customerId 客户ID
	 * @return
	 */
	String selectInvoiceBasisList(String customerId);
	
	/**
	 * 获得增票资质信息
	 * @param invoiceId 增票资质主键
	 * @return
	 */
	String selectInvoiceBasisInfo(String invoiceId);
	
	/**
	 * 新增或编辑增票资质
	 * @param record
	 * @return
	 */
	String insertOrUpdateInvoiceBasis(InvoiceBasis record);
	
	/**
	 * 删除增票资质(硬删除)
	 * @param invoiceId 增票资质主键
	 * @return
	 */
	String deleteInvoiceBasis(String invoiceId);
	
	/**
	 * 删除增票资质(软删除)
	 * @param invoiceId 增票资质主键
	 * @return
	 */
	String softDeleteInvoiceBasis(String invoiceId);
	
	/**
	 * 设置默认增票资质
	 * @param record
	 * @return
	 */
	String updateDefaultInvoiceBasis(InvoiceBasis record);
}
