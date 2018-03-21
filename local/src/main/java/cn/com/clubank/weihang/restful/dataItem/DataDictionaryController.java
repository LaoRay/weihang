package cn.com.clubank.weihang.restful.dataItem;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.DataItemCode;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemDetailService;

@Controller
public class DataDictionaryController {

	@Resource
	private IDataItemDetailService iDataItemDetailService;
	/**
	 * 获取预约类型
	 * @return
	 */
	@RequestMapping(value="/bespeakGetType",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getBespeakType() {		

		return iDataItemDetailService.gainNameValue(DataItemCode.BESPEAK_TYPE.getValue());
	}
	
	/**
	 * 获取检查项目列表
	 * @return
	 */
	@RequestMapping(value="/repairgainObspList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult gainObspList(){
		
		return iDataItemDetailService.gainNameValue(DataItemCode.INSPECTION_ITEM.getValue());
	}
	
	/**
	 * 获取洗车项目列表
	 * @return
	 */
	@RequestMapping(value="/repairgainWashCarList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult gainWashCarList(){
		
		return  iDataItemDetailService.gainNameValue(DataItemCode.WORK_WASH_TYPE.getValue());
	}
}
