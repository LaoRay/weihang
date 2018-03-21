package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.InvoiceBasisMapper;
import cn.com.clubank.weihang.manage.member.pojo.InvoiceBasis;
import cn.com.clubank.weihang.manage.member.service.InvoiceBasisService;

@Service
public class InvoiceBasisServiceImpl implements InvoiceBasisService {
	
	@Resource
	private InvoiceBasisMapper invoiceBasisMapper;

	@Override
	public String selectInvoiceBasisList(String customerId) {
		JSONObject json=new JSONObject();
		
		List<InvoiceBasis> list=invoiceBasisMapper.selectBycustomerId(customerId);
		if(!list.isEmpty()){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return json.toString();
	}

	@Override
	public String insertOrUpdateInvoiceBasis(InvoiceBasis record) {
		JSONObject json=new JSONObject();
	
		if(StringUtils.isBlank(record.getInvoiceId())){
			
			if(!StringUtils.isBlank(record.getIdentificationCode())){//纳税人识别号不为空
				//验证纳税人识别号的唯一性
				int count=invoiceBasisMapper.selectCountByIdentificationCode(record.getIdentificationCode());
				if(count>Constants.INT_0){
					json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
					json.put("msg", "此纳税人识别号已存在");
					return json.toString();
				}
			}
			
			if(!StringUtils.isBlank(record.getBankId())){//银行账户不为空
				//验证银行账户的唯一性
				int count=invoiceBasisMapper.selectCountByBankId(record.getBankId());
				if(count>Constants.INT_0){
					json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
					json.put("msg", "此银行账户已存在");
					return json.toString();
				}
			}
			
			if(invoiceBasisMapper.insert(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		}else{
			InvoiceBasis invoiceBasis=invoiceBasisMapper.selectByPrimaryKey(record.getInvoiceId());
			//判断用户是否修改了纳税人识别号
			if(!record.getIdentificationCode().equals(invoiceBasis.getIdentificationCode())){
				//验证纳税人识别号的唯一性
				int count=invoiceBasisMapper.selectCountByIdentificationCode(record.getIdentificationCode());
				if(count>Constants.INT_0){
					json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
					json.put("msg", "此纳税人识别号已存在");
					return json.toString();
				}
			}
			
			//判断用户是否修改了银行账户
			if(!record.getBankId().equals(invoiceBasis.getBankId())){
				//验证银行账户的唯一性
				int count=invoiceBasisMapper.selectCountByBankId(record.getBankId());
				if(count>Constants.INT_0){
					json.put("apiStatus", ResultCode.DATA_EXIST.getValue());
					json.put("msg", "此银行账户已存在");
					return json.toString();
				}
			}
			
			if(invoiceBasisMapper.updateByPrimaryKeySelective(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "编辑成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "编辑失败");
			}
		}
		return json.toString();
	}

	@Override
	public String deleteInvoiceBasis(String invoiceId) {
		JSONObject json=new JSONObject();
		
		if(invoiceBasisMapper.deleteByPrimaryKey(invoiceId)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String updateDefaultInvoiceBasis(InvoiceBasis record) {
		JSONObject json=new JSONObject();
		
		if(invoiceBasisMapper.updateIsDefault(record)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "设置默认成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "设置默认失败");
		}
		return json.toString();
	}

	@Override
	public String softDeleteInvoiceBasis(String invoiceId) {
		JSONObject json=new JSONObject();
		
		if(invoiceBasisMapper.softDeleteByPrimaryKey(invoiceId)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String selectInvoiceBasisInfo(String invoiceId) {
		JSONObject json=new JSONObject();
		
		InvoiceBasis invoiceBasis=invoiceBasisMapper.selectByPrimaryKey(invoiceId);
		if(invoiceBasis!=null){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", invoiceBasis);
		}else{
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", invoiceBasis);
		}
		return json.toString();
	}

}
