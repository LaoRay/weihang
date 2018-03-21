package cn.com.clubank.weihang.common.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.sms.WeihSmsSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信消息监听器
 * 
 * @author LeiQY
 *
 */
@Slf4j
public class SmsMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			JSONObject json = JSON.parseObject(text);
			// 发送短信
			String result = WeihSmsSender.sendSms(json.get("mobile").toString(), json.getString("code"));
			System.out.println("短信发送结果打印================================");
			System.out.println(result);
		} catch (JMSException e) {
			log.error("短信发送失败", e);
		}
	}
}
