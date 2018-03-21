package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseUserRole;

public interface BaseUserRoleMapper {

    int deleteByPrimaryKey(String urId);
    
    /**
     * 通过角色ID删除数据
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);

    int insert(BaseUserRole record);

    int insertSelective(BaseUserRole record);

    BaseUserRole selectByPrimaryKey(String urId);
    
    int updateByPrimaryKeySelective(BaseUserRole record);

    int updateByPrimaryKey(BaseUserRole record);

	int deleteByUserId(String userId);
}