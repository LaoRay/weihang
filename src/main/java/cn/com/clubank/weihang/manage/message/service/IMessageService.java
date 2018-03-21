package cn.com.clubank.weihang.manage.message.service;

import java.util.List;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;

/**
 * 消息表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface IMessageService {

	/**
	 * 消息推送
	 * 
	 * @param customerIdList
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	boolean pushMessage(MsgList msg, List<String> customerIdList);
	
	/**
	 * 消息推送
	 * 
	 * @param customerIdList
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	boolean pushMessage(MsgList msg, String... objId);
	
	/**
	 * 根据目标（用户或客户）Id查询消息列表
	 * 
	 * @param customerIdList
	 * @param msg
	 * @param status 状态：0 未读，1 已读
	 * @return
	 * @throws Exception
	 */
	CommonResult findByTargetId(String targetId, Integer status, Integer pageIndex, Integer pageSize);
	
	/**
	 * 根据目标（用户或客户）Id查询未读消息量
	 * 
	 * @param customerIdList
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	CommonResult getUnreadTotalByTargetId(String targetId);
	
	/**
	 * 已读
	 * @param msgId
	 * @return
	 */
	CommonResult haveRead(String msgId, String targetId);
	
	/**
	 * 批量删除消息
	 * @param msgIds
	 * @return
	 */
	CommonResult msgDelete(String mrIds);
}
