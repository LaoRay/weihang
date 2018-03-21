package cn.com.clubank.weihang.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.member.pojo.CarGroup;

public interface CarGroupMapper {
	
    int deleteByPrimaryKey(String groupId);

    int insert(CarGroup record);

    CarGroup selectByPrimaryKey(String groupId);
    
    Map<String, Object> selectDetailByPrimaryKey(String groupId);
    
    CarGroup selectByAccount(String account);

    List<CarGroup> selectAll();

    int updateByPrimaryKey(CarGroup record);
    
    /**
	 * 分页查询：通过集团组名模糊查询，进行分页
	 * 
	 * @param cname 		分类名
	 * @param startIndex 	起始索引
	 * @param pageSize 		每页行数
	 * @return
	 */
    int findPageTotal(@Param("name") String name);
	List<CarGroup> findPage(@Param("name") String name, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);
	

}