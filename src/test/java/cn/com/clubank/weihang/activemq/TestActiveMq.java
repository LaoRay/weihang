package cn.com.clubank.weihang.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMq {

	// 测试spring整合mq
	@Test
	public void testSpringActiveMq() throws Exception {
		// 初始化spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext-activemq.xml");
		// 从spring容器获取jmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		// 从spring容器获取获取destination对象
		Destination destination = (Destination) applicationContext.getBean("queueDestination");
		// 使用jmsTemplate对象发送消息
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 创建一个消息对象并返回
				TextMessage message = session.createTextMessage("queue message");
				return message;
			}
		});
	}

	// 发送消息
	@Test
	public void testQueueProducer() throws Exception {
		// 第一步：初始化一个spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext-activemq.xml");
		// 第二步：从容器中获得JMSTemplate对象。
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		// 第三步：从容器中获得一个Destination对象
		Queue queue = (Queue) applicationContext.getBean("queueDestination");
		// 第四步：使用JMSTemplate对象发送消息，需要知道Destination
		jmsTemplate.send(queue, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("spring activemq test");
				return textMessage;
			}
		});
	}

	// 测试消息监听
	@Test
	public void testQueueConsumer() throws Exception {
		// 初始化spring容器
		@SuppressWarnings({ "unused", "resource" })
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext-activemq.xml");
		// 等待
		System.in.read();
	}
}
