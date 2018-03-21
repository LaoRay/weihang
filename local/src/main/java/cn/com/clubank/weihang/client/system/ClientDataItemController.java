package cn.com.clubank.weihang.client.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.dataItem.service.IDataItemDetailService;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemService;

/**
 * 后台：基础数据
 * @author Liangwl
 *
 */
@Controller
public class ClientDataItemController {

	@Resource
	private IDataItemService iDataItemService;
	
	@Resource
	private IDataItemDetailService iDataItemDetailService;
	
	/**
	 * 后台：获得数据字典分类列表
	 * @return
	 */
	@RequestMapping(value = "/clientFindDataitemList", method = RequestMethod.POST)
	@ResponseBody
	public String findDataitemList(){
		
		return iDataItemService.selectDataItemList();
	}
	
	/**
	 * 后台：获得数据字典分类下明细列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindDataitemDetailList", method = RequestMethod.POST)
	@ResponseBody
	public String findDataitemDetailList(@RequestBody JSONObject param){
		
		return iDataItemDetailService.selectDataItemDetailList(param.getString("dataId"));
	}
}
