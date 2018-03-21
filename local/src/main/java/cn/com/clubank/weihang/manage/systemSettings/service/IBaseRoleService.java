package cn.com.clubank.weihang.manage.systemSettings.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRole;

public interface IBaseRoleService {

	/**
	 * 通过角色名模糊查询并分页
	 * 
	 * @param roleName
	 *            角色名称
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectRoleListByNameAndPaged(String roleName, Integer pageIndex, Integer pageSize);

	/**
	 * 新增或编辑角色
	 * 
	 * @param role
	 * @return
	 */
	String insertOrUpdateRole(BaseRole role);

	/**
	 * 获得角色详情
	 * 
	 * @param roleId
	 * @return
	 */
	String selectRoleDetail(String roleId);

	/**
	 * 获得角色下用户
	 * 
	 * @param roleId
	 * @return
	 */
	String selectRoleUser(String roleId);

	/**
	 * 获取角色列表
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	CommonResult clientFindRoleList(String userId);

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 *            角色主键
	 * @return
	 */
	String deleteRole(String roleId);
}
