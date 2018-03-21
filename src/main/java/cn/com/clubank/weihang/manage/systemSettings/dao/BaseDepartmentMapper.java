package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseDepartment;
import java.util.List;

public interface BaseDepartmentMapper {
	int deleteByPrimaryKey(String dId);

	int insert(BaseDepartment record);

	BaseDepartment selectByPrimaryKey(String dId);

	List<BaseDepartment> selectAll();

	int updateByPrimaryKey(BaseDepartment record);
}