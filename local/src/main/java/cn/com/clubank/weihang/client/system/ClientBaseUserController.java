package cn.com.clubank.weihang.client.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import cn.com.clubank.weihang.manage.user.service.IBaseUserService;

/**
 * 后台：系统设置--用户管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientBaseUserController {

	@Autowired
	private IBaseUserService baseUserService;

	/**
	 * 后台：根据帐号（模糊）、姓名（模糊）、部门主键、状态分页查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindBaseUserList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindBaseUserList(@RequestBody JSONObject json) {
		return baseUserService.clientFindBaseUserList(json.getString("account"), json.getString("userName"),
				json.getString("departmentId"), json.getInteger("userStatus"), json.getInteger("pageIndex"),
				json.getInteger("pageSize"));
	}

	/**
	 * 后台：根据用户主键查询用户信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindBaseUserInfoByUserId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindBaseUserInfoByUserId(@RequestBody JSONObject json) {
		return baseUserService.clientFindBaseUserInfoByUserId(json.getString("userId"));
	}

	/**
	 * 后台：新增编辑用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientModifyBaseUser", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientModifyBaseUser(@RequestBody BaseUser baseUser) {
		return baseUserService.clientModifyBaseUser(baseUser);
	}

	/**
	 * 后台：删除用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteBaseUser", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteBaseUser(@RequestBody JSONObject json) {
		return baseUserService.clientDeleteBaseUser(json.getString("userId"));
	}

	/**
	 * 后台：设置用户状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientSetBaseUserStatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientSetBaseUserStatus(@RequestBody JSONObject json) {
		return baseUserService.clientSetBaseUserStatus(json.getString("userId"), json.getInteger("userStatus"));
	}

	/**
	 * 后台：分配角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientSetBaseUserRole", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientSetBaseUserRole(@RequestBody JSONArray array) {
		return baseUserService.clientSetBaseUserRole(array);
	}
}
