package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseModule;

public interface BaseModuleMapper {

    int deleteByPrimaryKey(String moduleId);

    int insert(BaseModule record);

    int insertSelective(BaseModule record);

    BaseModule selectByPrimaryKey(String moduleId);

    int updateByPrimaryKeySelective(BaseModule record);

    int updateByPrimaryKey(BaseModule record);
}