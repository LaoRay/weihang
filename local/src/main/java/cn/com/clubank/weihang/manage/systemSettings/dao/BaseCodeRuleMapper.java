package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import java.util.List;

public interface BaseCodeRuleMapper {
	
    int deleteByPrimaryKey(String ruleId);

    int insert(BaseCodeRule record);

    BaseCodeRule selectByPrimaryKey(String ruleId);

    List<BaseCodeRule> selectAll();
    
    List<BaseCodeRule> selectByItemCode(String itemCode);

    int updateByPrimaryKey(BaseCodeRule record);
}