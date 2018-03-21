package cn.com.clubank.weihang.restful.systemSettings;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.manage.home.AppHomePageService;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseParameterService;

/**
 * 系统基础参数
 * 
 * @author LeiQY
 *
 */
@Controller
public class BaseParameterController {

	@Autowired
	private BaseParameterService parameterService;

	@Autowired
	private AppHomePageService appHomePageService;

	/**
	 * 获取所有省份
	 * 
	 * @return
	 */
	@RequestMapping(value = "/systemFindAreaList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAreaList(@RequestBody JSONObject json) {
		return parameterService.findAreaList(json.getString("parentId"));
	}

	/**
	 * 构建app首页展示的所有数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/appHomeData", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult appHomeData(HttpServletRequest request) {
		return appHomePageService.appHomeData(request.getHeader("customerId"), Constants.INT_1);
	}
	
	/**
	 * 构建app汽车维修主页展示的所有数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/repairHomeData", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult repairHomeData(HttpServletRequest request) {
		return appHomePageService.appHomeData(request.getHeader("customerId"), Constants.INT_2);
	}

}
