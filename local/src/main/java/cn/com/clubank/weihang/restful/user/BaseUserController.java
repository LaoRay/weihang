package cn.com.clubank.weihang.restful.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import cn.com.clubank.weihang.manage.user.service.IBaseUserService;

@Controller
public class BaseUserController {

	@Resource
	private IBaseUserService iBaseUserService;
	
	/**
	 * 获取技师列表
	 * @return
	 */
	@RequestMapping(value="/userGainTechnicianList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getArtificer(){
		return iBaseUserService.gainNameIcon(BaseUser.DUTY_TECHNICIAN);
	}
	
	/**
	 * 获取服务顾问列表
	 * @return
	 */
	@RequestMapping(value="/userGainServiceAdvisorList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getServiceAdvisor(){
		return iBaseUserService.gainNameIcon(BaseUser.DUTY_SERVICE_CONSULTANT);
	} 
	
	/**
	 * 获取洗车组长列表
	 * @return
	 */
	@RequestMapping(value="/userGainWashCarHeadmanList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getWashCarHeadman(){
		return iBaseUserService.gainNameIcon(BaseUser.DUTY_WASH_CAR_LEADER);
	}
	
	/**
	 * 获取车间组长列表
	 * @return
	 */
	@RequestMapping(value="/userGainWorkshopLeaderList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getWorkshopLeader(){
		return iBaseUserService.gainNameIcon(BaseUser.DUTY_WORKSHOP_LEADER);
	}
	
	/**
	 * 获取洗车师列表
	 * @return
	 */
	@RequestMapping(value="/userGainWashCarDivisionList",method=RequestMethod.POST)
	@ResponseBody
	public CommonResult getWashCarDivision(){
		return iBaseUserService.gainNameIcon(BaseUser.DUTY_WASH_CAR_DIVISION);
	}
	
	/**
	 * 重置密码
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/userResetPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult userResetPassword(@RequestBody JSONObject json) {
		return iBaseUserService.userResetPassword(json.getString("mobile"), json.getString("password"));
	}
	
	/**
	 * 获取员工个人信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/userGainBaseUserInfo",method=RequestMethod.POST)
	@ResponseBody
	public String getBaseUserInfo(HttpServletRequest request){
		return iBaseUserService.getBaseUserInfo(request.getHeader("userId"));
	}
}
