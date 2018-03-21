package cn.com.clubank.weihang.manage.systemSettings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseLog;

public interface BaseLogMapper {
    int deleteByPrimaryKey(String logId);

    int insert(BaseLog record);

    BaseLog selectByPrimaryKey(String logId);

    List<BaseLog> selectAll();

    int updateByPrimaryKey(BaseLog record);
    
    /**
     * 通过操作用户（模糊）、操作时间查询到的总条数
     * @param operateName 操作用户
     * @param operateTimeOne 操作时间1
     * @param operateTimeTwo 操作时间2
     * @return
     */
    int selectLogListAndPagedCount(@Param("operateName")String operateName,@Param("operateTimeOne")String operateTimeOne,@Param("operateTimeTwo")String operateTimeTwo);
    
    /**
     * 通过操作用户（模糊）、操作时间查询日志列表并分页
     * @param operateName 操作用户
     * @param operateTimeOne 操作时间1
     * @param operateTimeTwo 操作时间2
     * @param startIndex 起始索引
     * @param pageSize 每页行数
     * @return
     */
    List<BaseLog> selectLogListAndPaged(@Param("operateName")String operateName,@Param("operateTimeOne")String operateTimeOne,@Param("operateTimeTwo")String operateTimeTwo,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}