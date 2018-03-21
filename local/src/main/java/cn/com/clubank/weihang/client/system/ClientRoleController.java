package cn.com.clubank.weihang.client.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRole;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRoleModuleButton;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseButtonService;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseRoleModuleButtonService;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseRoleService;

/**
 * 后台：角色管理
 * 
 * @author Liangwl
 *
 */
@Controller
public class ClientRoleController {

	@Resource
	private IBaseRoleService iBaseRoleService;

	@Resource
	private IBaseButtonService iBaseButtonService;

	@Resource
	private IBaseRoleModuleButtonService iBaseRoleModuleButtonService;

	/**
	 * 后台：根据角色名称模糊查询并分页
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindRoleListAndPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findRoleListAndPaged(@RequestBody JSONObject param) {

		return iBaseRoleService.selectRoleListByNameAndPaged(param.getString("roleName"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 后台：新增或编辑角色
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/clientAddOrEditRole", method = RequestMethod.POST)
	@ResponseBody
	public String addOrEditRole(@RequestBody BaseRole role) {

		return iBaseRoleService.insertOrUpdateRole(role);
	}

	/**
	 * 后台：获得此模块下按钮
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindModuleButton", method = RequestMethod.POST)
	@ResponseBody
	public String findModuleButton(@RequestBody JSONObject param) {

		return iBaseButtonService.selectModuleButton(param.getString("moduleId"));
	}

	/**
	 * 后台：分配权限提交
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/clientAddRoleModuleButton", method = RequestMethod.POST)
	@ResponseBody
	public String addRoleModuleButton(@RequestBody BaseRoleModuleButton record) {

		return iBaseRoleModuleButtonService.insertRoleModuleButton(record);
	}

	/**
	 * 后台：获得角色详情
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindRoleDetail", method = RequestMethod.POST)
	@ResponseBody
	public String findRoleDetail(@RequestBody JSONObject param) {

		return iBaseRoleService.selectRoleDetail(param.getString("roleId"));
	}

	/**
	 * 后台：获得角色下用户
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindRoleUser", method = RequestMethod.POST)
	@ResponseBody
	public String findRoleUser(@RequestBody JSONObject param) {

		return iBaseRoleService.selectRoleUser(param.getString("roleId"));
	}

	/**
	 * 后台：获得角色列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindRoleList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindRoleList(@RequestBody JSONObject json) {
		return iBaseRoleService.clientFindRoleList(json.getString("userId"));
	}

	/**
	 * 后台：删除角色
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindDeleteRole", method = RequestMethod.POST)
	@ResponseBody
	public String deleteRole(@RequestBody JSONObject param) {

		return iBaseRoleService.deleteRole(param.getString("roleId"));
	}
}
