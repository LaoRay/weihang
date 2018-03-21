package cn.com.clubank.weihang.manage.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.jpush.JiguangPush;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.JpushInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;
import cn.com.clubank.weihang.manage.message.dao.MsgListMapper;
import cn.com.clubank.weihang.manage.message.dao.MsgRelationMapper;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.pojo.MsgRelation;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息表业务逻辑层
 * 
 * @author Liangwl
 *
 */
@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

	@Autowired
	private JpushInfoMapper jpushInfoMapper;

	@Autowired
	private MsgRelationMapper msgRelationMapper;
	
	@Autowired
	private MsgListMapper msgListMapper;

	private JPushClient jpushClient = new JPushClient(JiguangPush.MASTER_SECRET, JiguangPush.APP_KEY, null,
			ClientConfig.getInstance());

	/**
	 * 推送消息
	 * 
	 * @param customerIdList
	 *            用户id数组
	 * @param msg
	 *            消息对象
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean pushMessage(MsgList msg, List<String> customerIdList) {
		if (StringUtil.isBlank(msg.getMsgId())) {
			msgListMapper.insert(msg);
		}
		try {
			for (String customerId : customerIdList) {
				push(msg, customerId);
			}
		} catch (Exception e) {
			log.info("推送消息失败：【"+ JSON.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm:ss") +"】");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean pushMessage(MsgList msg, String... objIds) {
		log.info("推送消息：对象【{}】,内容【{}】", objIds, JSON.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm:ss"));
		if (StringUtil.isBlank(msg.getMsgId())) {
			msgListMapper.insert(msg);
		}
		try {
			for (String objId : objIds) {
				push(msg, objId);
			}
		} catch (Exception e) {
			log.info("推送消息失败：【{}】", JSON.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm:ss"));
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void push(MsgList msg, String objId) throws APIConnectionException, APIRequestException {
		MsgRelation msgRelation = new MsgRelation();
		// 用户id
		msgRelation.setTargetId(objId);
		// 消息对象id
		msgRelation.setMsgId(msg.getMsgId());
		// 消息状态 0未读 1已读
		msgRelation.setStatus(Constants.INT_0);
		msgRelationMapper.insert(msgRelation);
		List<String> jpushIdList = getJpushId(objId);
		if (jpushIdList.size() == 0) {
			log.info("没有查询到推送服务注册id。【"+ JSON.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm:ss") +"】");
		}
		for (String jpushId : jpushIdList) {
			PushPayload payload = JiguangPush.buildPushObject_all_alias_alert(jpushId, msg.getContent());
			jpushClient.sendPush(payload);
			log.info("推送消息成功：jpushId【{}】，内容【{}】", jpushId, msg.getContent());
		}
	}
	
	/**
	 * 根据用户id获取极光注册码
	 * 
	 * @param customerId
	 * @return
	 */
	public List<String> getJpushId(String customerId) {
		List<JpushInfo> jpushInfoList = jpushInfoMapper.selectByUserId(customerId);
		List<String> list = new ArrayList<>();
		for (JpushInfo jpushInfo : jpushInfoList) {
			if (StringUtil.isNotBlank(jpushInfo.getJpushId())) {
				list.add(jpushInfo.getJpushId());
			}
		}
		return list;
	}

	@Override
	public CommonResult findByTargetId(String targetId, Integer status, Integer pageIndex, Integer pageSize) {
		if (pageIndex == null || pageSize == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "分页参数为空！");
		}
		if (status == null) {
			status = -1;
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功！", msgListMapper.findByTargetId(targetId, status, pageIndex * pageSize, pageSize));
	}

	@Override
	public CommonResult getUnreadTotalByTargetId(String targetId) {
		if (StringUtil.isBlank(targetId)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "获取成功", msgRelationMapper.getUnreadTotalByTargetId(targetId));
	}

	@Override
	public CommonResult haveRead(String msgId, String targetId) {
		log.info("消息已读  msgId：{}， targetId：{}", msgId, targetId);
		msgRelationMapper.haveRead(msgId, targetId);
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public CommonResult msgDelete(String mrIds) {
		if (StringUtil.isBlank(mrIds)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		for (String mrId : mrIds.split(",")) {
			//根据消息关系id逻辑删除消息关系表数据
			msgRelationMapper.deleteByMrId(mrId);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "处理成功");
	}
	
}
