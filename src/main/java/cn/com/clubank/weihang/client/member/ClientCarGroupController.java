package cn.com.clubank.weihang.client.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.service.CarGroupService;

/**
 * 集团组
 * 
 * @author YangWei
 *
 */
@Controller
public class ClientCarGroupController {

	@Resource
	private CarGroupService carGroupService;
	
	/**
	 * 保存、更新
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/clientMemberSaveOrUpdateGroup", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberSaveOrUpdateGroup(@RequestBody CarGroup info) {
		return carGroupService.saveOrUpdateGroup(info);
	}
	
	/**
	 * 获取集团组详情-pc端
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/clientMemberGetGroupInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberGetGroupInfo(@RequestBody JSONObject param) {
		return carGroupService.clientMemberGetGroupInfo(param.getString("groupId"));
	}
	
	/**
	 * 删除
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/clientGroupDelete", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientGroupDelete(@RequestBody JSONObject param) {
		return carGroupService.clientGroupDelete(param.getString("groupId"));
	}
	
	/**
	 * 后台-分页查询
	 * @param groupName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/clientMemberGroupFindPage", method = RequestMethod.POST)
	@ResponseBody
	public String memberGroupFindPage(@RequestBody JSONObject param) {
		return carGroupService.findPage(param.getString("groupName"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 充值
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/clientGroupRecharge", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientGroupRecharge(@RequestBody JSONObject param) {
		return carGroupService.clientGroupRecharge(param.getString("groupId"), param.getBigDecimal("total"), param.getInteger("accountPayType"), param.getString("desc"));
	}
	
	/**
	 * 查询集团组帐号消费记录
	 * @param groupId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/clientFindGroupAccountRecord", method = RequestMethod.POST)
	@ResponseBody
	public String clientFindGroupAccountRecord(@RequestBody JSONObject param) {
		return carGroupService.clientFindGroupAccountRecord(param.getString("groupId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
}
