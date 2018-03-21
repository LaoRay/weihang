package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRoleModuleButton;

public interface BaseRoleModuleButtonMapper {

    int deleteByPrimaryKey(String brmbId);
    
    /**
     * 通过角色ID删除数据
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);

    int insert(BaseRoleModuleButton record);

    int insertSelective(BaseRoleModuleButton record);

    BaseRoleModuleButton selectByPrimaryKey(String brmbId);

    int updateByPrimaryKeySelective(BaseRoleModuleButton record);

    int updateByPrimaryKey(BaseRoleModuleButton record);
}