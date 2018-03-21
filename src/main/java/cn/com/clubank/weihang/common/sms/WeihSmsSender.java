package cn.com.clubank.weihang.common.sms;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信
 * 
 * @author LeiQY
 *
 */
@Slf4j
public class WeihSmsSender {

	private static final String APIID = "cf_sxwh";
	private static final String APIKEY = "b20fdd29831a2785a1138d8544bf24b3";
	private static final String URL = "http://106.wx96.com/webservice/sms.php?method=Submit";

	public static String sendSms(String mobile, String verifyCode) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(URL);

		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

		String content = new String("您的验证码是：" + verifyCode + "。请不要把验证码泄露给其他人。");
		NameValuePair[] data = { 
				new NameValuePair("account", APIID), 
				new NameValuePair("password", APIKEY),
				new NameValuePair("mobile", mobile), 
				new NameValuePair("content", content) };

		method.setRequestBody(data);

		try {
			client.executeMethod(method);
			String submitResult = method.getResponseBodyAsString();
			System.out.println(submitResult);

			Document doc = DocumentHelper.parseText(submitResult);
			Element root = doc.getRootElement();

			// 状态码
			String code = root.elementText("code");
			// 提示信息
			String msg = root.elementText("msg");
			// 流水号，发送失败为0
			String smsid = root.elementText("smsid");

			if ("2".equals(code)) {
				log.info("短信发送成功，验证码：{}， 流水号：{}", verifyCode, smsid);
			} else {
				log.error("短信发送异常，异常信息：{}", msg);
			}
			return submitResult;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("服务器内部错误，短信发送失败", e);
		}
		return null;
	}
}
