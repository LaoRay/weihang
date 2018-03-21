package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseArea;
import java.util.List;

public interface BaseAreaMapper {
    int deleteByPrimaryKey(String aId);

    int insert(BaseArea record);

    BaseArea selectByPrimaryKey(String aId);

    List<BaseArea> selectAll();

    int updateByPrimaryKey(BaseArea record);
    
	List<BaseArea> selectAreaListByParentId(String parentId);
}