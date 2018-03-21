package cn.com.clubank.weihang.client.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuthLog;
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
	 * 后台：根据登录账号（模糊）、姓名（模糊）、部门主键、状态分页查询列表
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
	
	/**
	 * 后台：推荐用户
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientSetSuggestedUsers", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientSetSuggestedUsers(@RequestBody JSONObject param) {
		return baseUserService.settingSuggestedUsers(param.getString("userId"));
	}
	
	/**
	 * 后台：取消推荐
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientCancelSuggestedUsers", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientCancelSuggestedUsers(@RequestBody JSONObject param) {
		return baseUserService.CancelSuggestedUsers(param.getString("userId"));
	}
	
	/**
	 * 后台：查询今日之星(查询前十个推荐用户)
	 * @return
	 */
	@RequestMapping(value = "/clientFindSuggestedUsers", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindSuggestedUsers() {
		return baseUserService.selectSuggestedUsers();
	}
	
	/**
	 * 后台：查询门店的推荐
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindStoreSuggestedUsers", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindStoreSuggestedUsers(@RequestBody JSONObject param) {
		return baseUserService.selectSuggestedUsersByShopId(param.getString("shopId"));
	}
	
	/**
	 * 后台：修改密码
	 * @return
	 */
	@WeihAuthLog(logModule = "用户管理", logFeatures = "修改密码")
	@RequestMapping(value = "/clientUserUpdatePassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientUserUpdatePassword(@RequestBody JSONObject param) {
		return baseUserService.updatePassword(param.getString("userId"), param.getString("oldPassword"), param.getString("nowPassword"), param.getString("reNowPassword"));
	}
	
	/**
	 * 后台：重置密码
	 * @return
	 */
	@WeihAuthLog(logModule = "用户管理", logFeatures = "重置密码")
	@RequestMapping(value = "/clientUserResetPassword", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientUserResetPassword(@RequestBody JSONObject param) {
		return baseUserService.resetPassword(param.getString("userId"));
	}
}
