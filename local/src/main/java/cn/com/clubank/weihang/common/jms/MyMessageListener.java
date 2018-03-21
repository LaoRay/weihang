package cn.com.clubank.weihang.common.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage text = (TextMessage) message;
		String str;
		try {
			// 取消息内容
			str = text.getText();
			System.out.println(str);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
