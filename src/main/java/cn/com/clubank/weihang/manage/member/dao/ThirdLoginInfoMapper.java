package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.ThirdLoginInfo;
import java.util.List;

public interface ThirdLoginInfoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ThirdLoginInfo record);

	ThirdLoginInfo selectByPrimaryKey(String id);

	List<ThirdLoginInfo> selectAll();

	int updateByPrimaryKey(ThirdLoginInfo record);

	ThirdLoginInfo selectByThirdId(String thirdId);
}