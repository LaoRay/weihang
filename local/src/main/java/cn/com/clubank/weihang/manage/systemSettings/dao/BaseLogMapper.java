package cn.com.clubank.weihang.manage.systemSettings.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseLog;

public interface BaseLogMapper {

    int deleteByPrimaryKey(String logId);

    int insert(BaseLog record);

    int insertSelective(BaseLog record);

    BaseLog selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(BaseLog record);

    int updateByPrimaryKeyWithBLOBs(BaseLog record);

    int updateByPrimaryKey(BaseLog record);
    
    /**
     * 通过操作用户（模糊）、操作时间查询到的总条数
     * @param operateName 操作用户
     * @param operateTimeOne 操作时间1
     * @param operateTimeTwo 操作时间2
     * @return
     */
    int selectCount(@Param("operateName")String operateName,@Param("operateTimeOne")String operateTimeOne,@Param("operateTimeTwo")String operateTimeTwo);
    
    /**
     * 通过操作用户（模糊）、操作时间查询日志列表并分页
     * @param operateName 操作用户
     * @param operateTimeOne 操作时间1
     * @param operateTimeTwo 操作时间2
     * @param startIndex 起始索引
     * @param pageSize 每页行数
     * @return
     */
    List<Map<String,Object>> selectLogListAndPaged(@Param("operateName")String operateName,@Param("operateTimeOne")String operateTimeOne,@Param("operateTimeTwo")String operateTimeTwo,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}