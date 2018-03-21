package cn.com.clubank.weihang.manage.member.dao;

import java.util.List;

import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;

/**
 * 极光注册码信息表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface JpushInfoMapper {
	
    int deleteByPrimaryKey(String uuId);
    
    int deleteByPhoneId(String phoneId);

    int insert(JpushInfo record);

    JpushInfo selectByPrimaryKey(String uuId);

    List<JpushInfo> selectAll();

    int updateByPrimaryKey(JpushInfo record);

    JpushInfo selectByPhoneId(String phoneId);
    
	int updateByPhoneId(JpushInfo jpushInfo);

	List<JpushInfo> selectByUserId(String userId);
}