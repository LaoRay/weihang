package cn.com.clubank.weihang.manage.message.dao;

import cn.com.clubank.weihang.manage.message.pojo.MsgRelation;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 消息关系表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface MsgRelationMapper {
    int deleteByPrimaryKey(String mrId);

    int insert(MsgRelation record);

    MsgRelation selectByPrimaryKey(String mrId);

    List<MsgRelation> selectAll();

    int updateByPrimaryKey(MsgRelation record);
    
    /**
	 * 根据目标（用户或客户）Id查询未读消息量
	 * 
	 * @param customerIdList
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	int getUnreadTotalByTargetId(@Param("targetId") String targetId);
	
	/**
	 * 已读
	 * @param msgId
	 * @return
	 */
	int haveRead(@Param("msgId") String msgId, @Param("targetId") String targetId);
}