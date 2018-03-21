package cn.com.clubank.weihang.common.jms;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.sms.SMSModel;
import cn.com.clubank.weihang.common.sms.SmsSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信消息监听器
 * 
 * @author LeiQY
 *
 */
@Slf4j
public class SmsMessageListener implements MessageListener {

	@Autowired
	private SmsSender smsSender;

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			JSONObject json = (JSONObject) JSONObject.parse(text);
			List<String> list = (List<String>) json.get("msText");
			String[] smsText = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				smsText[i] = list.get(i);
			}
			JSONObject jsonObject = (JSONObject) json.get("smsModel");
			SMSModel smsModel = JSONObject.toJavaObject(jsonObject, SMSModel.class);
			// 发送短信
			smsSender.sendSigleMsg(json.get("mobile").toString(), smsText, smsModel);
		} catch (JMSException e) {
			log.error("短信发送失败", e);
		}
	}
}
