package cn.com.clubank.weihang.manage.message.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.message.pojo.MsgList;

/**
 * 消息表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface MsgListMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table msg_list
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String msgId);
    
    /**
     * 根据消息id逻辑删除
     * @param msgId
     * @return
     */
    int deleteByMsgId(String msgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table msg_list
     *
     * @mbggenerated
     */
    int insert(MsgList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table msg_list
     *
     * @mbggenerated
     */
    MsgList selectByPrimaryKey(String msgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table msg_list
     *
     * @mbggenerated
     */
    List<MsgList> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table msg_list
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MsgList record);
    
    /**
     * 查询消息列表
     * @param targetId 目标（用户或客户）id
     * @param start
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> findByTargetId(@Param("targetId") String targetId, @Param("status") Integer status, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
}