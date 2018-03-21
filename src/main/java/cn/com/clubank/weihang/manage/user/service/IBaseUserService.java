package cn.com.clubank.weihang.manage.user.service;

import com.alibaba.fastjson.JSONArray;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;

/**
 * 用户信息表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface IBaseUserService {

	/**
	 * 通过职位获得用户真实姓名和头像
	 */
	CommonResult gainNameIcon(Integer duty);

	/**
	 * 员工端登录
	 * 
	 * @param baseUser
	 * @param phoneId
	 * @return
	 */
	CommonResult baseUserLogin(BaseUser baseUser, String phoneId, Integer flatType);

	/**
	 * 员工pc端登录
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	CommonResult baseUserClientLogin(String account, String password, Integer flatType);

	/**
	 * 重置密码
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	CommonResult userResetPassword(String mobile, String password);

	/**
	 * 获的员工的个人信息
	 * 
	 * @param userId
	 *            员工ID
	 * @return
	 */
	String getBaseUserInfo(String userId);

	/**
	 * 后台：根据帐号（模糊）、姓名（模糊）、部门主键、状态分页查询列表
	 * 
	 * @param account
	 * @param realname
	 * @param departmentId
	 * @param status
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult clientFindBaseUserList(String account, String realname, String departmentId, Integer status,
			Integer pageIndex, Integer pageSize);

	/**
	 * 后台：根据用户主键查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	CommonResult clientFindBaseUserInfoByUserId(String userId);

	/**
	 * 新增编辑
	 * 
	 * @param baseUser
	 * @return
	 */
	CommonResult clientModifyBaseUser(BaseUser baseUser);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	CommonResult clientDeleteBaseUser(String userId);

	/**
	 * 设置用户状态
	 * 
	 * @param userId
	 * @param userStatus
	 * @return
	 */
	CommonResult clientSetBaseUserStatus(String userId, Integer userStatus);

	/**
	 * 分配角色
	 * 
	 * @param array
	 * @return
	 */
	CommonResult clientSetBaseUserRole(JSONArray array);
	
	/**
	 * 推荐用户
	 * @param userId 用户ID
	 * @return
	 */
	CommonResult settingSuggestedUsers(String userId);
	
	/**
	 * 取消推荐
	 * @param userId 用户ID
	 * @return
	 */
	CommonResult CancelSuggestedUsers(String userId);
	
	/**
	 * 查询前十个推荐用户
	 */
	CommonResult selectSuggestedUsers();
	
	/**
	 * 根据门店查询推荐用户
	 * @param shopId 所属门店ID
	 * @return
	 */
	CommonResult selectSuggestedUsersByShopId(String shopId);
	
	/**
	 * 修改密码
	 * @param userId
	 * @param oldPassword
	 * @param nowPassword
	 * @return
	 */
	CommonResult updatePassword(String userId, String oldPassword, String nowPassword, String reNowPassword);
	
	/**
	 * 重置密码
	 * @param userId
	 * @param oldPassword
	 * @param nowPassword
	 * @return
	 */
	CommonResult resetPassword(String userId);
}
