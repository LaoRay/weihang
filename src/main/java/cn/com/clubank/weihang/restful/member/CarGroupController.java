package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.service.CarGroupService;

/**
 * 集团组
 * 
 * @author YangWei
 *
 */
@Controller
public class CarGroupController {

	@Resource
	private CarGroupService carGroupService;
	
	/**
	 * 获取集团组详情
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/memberGetGroupInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberGetGroupInfo(@RequestBody JSONObject param) {
		return carGroupService.memberGetGroupInfo(param.getString("groupId"));
	}
	
	/**
	 * 添加集团组车辆
	 * @param groupId
	 * @param carIds
	 * @return
	 */
	@RequestMapping(value = "/memberAddGroupCars", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberAddGroupCars(@RequestBody JSONObject param) {
		return carGroupService.addGroupCars(param.getString("groupId"), param.getString("carIds"));
	}
	
	/**
	 * 删除集团组车辆
	 * @param groupId
	 * @param carIds
	 * @return
	 */
	@RequestMapping(value = "/memberDeleteGroupCars", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberDeleteGroupCars(@RequestBody JSONObject param) {
		return carGroupService.deleteGroupCars(param.getString("carIds"));
	}
	
	/**
	 * 查询集团组车辆列表
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/memberFindGroupCarList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindGroupCarList(@RequestBody JSONObject param) {
		return carGroupService.findGroupCarList(param.getString("groupId"));
	}
	
	/**
	 * 查询集团组帐号消费记录
	 * @param groupId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/memberFindGroupAccountRecord", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindGroupAccountRecord(@RequestBody JSONObject param) {
		return carGroupService.findGroupAccountRecord(param.getString("groupId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 查询集团组帐号充值记录
	 * @param groupId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/memberFindGroupRechargeRecord", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindGroupRechargeRecord(@RequestBody JSONObject param) {
		return carGroupService.memberFindGroupRechargeRecord(param.getString("groupId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
}
