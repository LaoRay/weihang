package cn.com.clubank.weihang.client.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseParameters;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseParameterService;

/**
 * 后台：参数设置
 * @author Liangwl
 *
 */
@Controller
public class ClientParameterController {
	
	@Resource
	private BaseParameterService baseParameterService;

	/**
	 * 后台：通过参数名称（模糊），参数编码（模糊）分页查询列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindParameterListAndPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findParameterListAndPaged(@RequestBody JSONObject param){
		
		return baseParameterService.selectParameterListAndPaged(param.getString("name"), param.getString("keyCode"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：新增或编辑系统参数
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientAddOrEditParameter", method = RequestMethod.POST)
	@ResponseBody
	public String addOrEditParameter(@RequestBody BaseParameters record){
		
		return baseParameterService.insertOrUpdateParameter(record);
	}
	
	/**
	 * 后台：删除参数
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteParameter", method = RequestMethod.POST)
	@ResponseBody
	public String deleteParameter(@RequestBody JSONObject param){
		
		return baseParameterService.deleteParameter(param.getString("pId"));
	}
}
