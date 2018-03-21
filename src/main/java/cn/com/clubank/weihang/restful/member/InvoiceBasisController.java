package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.member.pojo.InvoiceBasis;
import cn.com.clubank.weihang.manage.member.service.InvoiceBasisService;

@Controller
public class InvoiceBasisController {

	@Resource
	private InvoiceBasisService invoiceBasisService;
	
	/**
	 * 获得客户增票资质列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/memberFindInvoiceBasisList",method=RequestMethod.POST)
	@ResponseBody
	public String findInvoiceBasisList(@RequestBody JSONObject param){
		
		return invoiceBasisService.selectInvoiceBasisList(param.getString("customerId"));
	}
	
	/**
	 * 获得增票资质信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/memberFindInvoiceBasisInfo",method=RequestMethod.POST)
	@ResponseBody
	public String findInvoiceBasisInfo(@RequestBody JSONObject param){
		
		return invoiceBasisService.selectInvoiceBasisInfo(param.getString("invoiceId"));
	}
	
	/**
	 * 新增或编辑增票资质
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/memberAddOrEditInvoiceBasis",method=RequestMethod.POST)
	@ResponseBody
	public String addOrEditInvoiceBasis(@RequestBody InvoiceBasis record){
		
		return invoiceBasisService.insertOrUpdateInvoiceBasis(record);
	}
	
	/**
	 * 删除增票资质(硬删除)
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/memberHardDeleteInvoiceBasis",method=RequestMethod.POST)
	@ResponseBody
	public String hardDeleteInvoiceBasis(@RequestBody JSONObject param){
		
		return invoiceBasisService.deleteInvoiceBasis(param.getString("invoiceId"));
	}
	
	/**
	 * 删除增票资质(软删除)
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/memberSoftDeleteInvoiceBasis",method=RequestMethod.POST)
	@ResponseBody
	public String softDeleteInvoiceBasis(@RequestBody JSONObject param){
		
		return invoiceBasisService.softDeleteInvoiceBasis(param.getString("invoiceId"));
	}
	
	/**
	 * 设置默认增票资质
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/memberSetDefaultInvoiceBasis",method=RequestMethod.POST)
	@ResponseBody
	public String setDefaultInvoiceBasis(@RequestBody InvoiceBasis record){
		
		return invoiceBasisService.updateDefaultInvoiceBasis(record);
	}
}
