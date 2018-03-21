package cn.com.clubank.weihang.manage.systemSettings.dao;

import java.util.List;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseButton;

public interface BaseButtonMapper {

    int deleteByPrimaryKey(String bId);

    int insert(BaseButton record);

    int insertSelective(BaseButton record);

    BaseButton selectByPrimaryKey(String bId);

    int updateByPrimaryKeySelective(BaseButton record);

    int updateByPrimaryKey(BaseButton record);
    
    /**
     * 获得模块下按钮
     * @param moduleId
     * @return
     */
    List<BaseButton> selectModuleButtonByModuleId(String moduleId);
}