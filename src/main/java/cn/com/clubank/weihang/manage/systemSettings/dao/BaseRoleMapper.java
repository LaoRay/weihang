package cn.com.clubank.weihang.manage.systemSettings.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRole;

public interface BaseRoleMapper {
	int deleteByPrimaryKey(String roleId);

	int insert(BaseRole record);

	int insertSelective(BaseRole record);

	BaseRole selectByPrimaryKey(String roleId);
	
	BaseRole selectBySort(Integer sort);

	int updateByPrimaryKeySelective(BaseRole record);

	int updateByPrimaryKey(BaseRole record);

	/**
	 * 通过角色名称模糊查询到的总数
	 * 
	 * @param roleName
	 *            角色名称
	 * @return
	 */
	int selectCountByName(@Param("roleName") String roleName);

	/**
	 * 通过角色名称模糊查询并分页
	 * 
	 * @param roleName
	 *            角色名称
	 * @param startIndex
	 *            起始索引
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<BaseRole> selectRoleListAndPaged(@Param("roleName") String roleName, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);

	/**
	 * 获得角色下用户
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<Map<String, Object>> selectRoleUserByRoleId(String roleId);
	
	/**
	 * 获得用户的角色
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<Map<String, Object>> selectRoleUserByUserId(String userId);

	/**
	 * 获取所有角色列表
	 * 
	 * @return
	 */
	List<BaseRole> selectRoleList();
}