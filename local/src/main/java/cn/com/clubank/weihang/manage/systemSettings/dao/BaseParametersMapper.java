package cn.com.clubank.weihang.manage.systemSettings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseParameters;

/**
 * 系统参数表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface BaseParametersMapper {
    int deleteByPrimaryKey(String pId);

    int insert(BaseParameters record);

    BaseParameters selectByPrimaryKey(String pId);

    List<BaseParameters> selectAll();

    int updateByPrimaryKey(BaseParameters record);
    
    int updateByPrimaryKeySelective(BaseParameters record);

	BaseParameters selectByKeyCode(String keyCode);
	
	/**
	 * 通过参数名，参数编码模糊查询到的总数
	 * @param name 参数名
	 * @param keyCode 参数编码
	 * @return
	 */
	int selectCountByNameAndCode(@Param("name")String name,@Param("keyCode")String keyCode);
	
	/**
	 * 通过参数名，参数编码模糊查询并分页
	 * @param name 参数名
	 * @param keyCode 参数编码
	 * @param startIndex 起始索引
	 * @param pageSize 每页行数
	 * @return
	 */
	List<BaseParameters> selectParameterListAndPaged(@Param("name")String name,@Param("keyCode")String keyCode,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}