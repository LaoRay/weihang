package cn.com.clubank.weihang.common.sms;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送工具类
 * 
 * @author LeiQY
 *
 */
@Component
@Slf4j
public class SmsSender {

	private static String authorization_base = "Basic ";

	private static String authorization_bearer = "Bearer ";

	@Autowired
	private JedisClient jedisClient;

	@Value("${ACCESS_TOKEN}")
	private String ACCESS_TOKEN;

	@Value("${BASE_URL}")
	private String BASE_URL;

	@Value("${TOKEN_URL}")
	private String TOKEN_URL;

	@Value("${SENDSMS_URL}")
	private String SENDSMS_URL;

	// 企业编号
	@Value("${ClIENTID}")
	private String ClientID;

	// 企业Secret
	@Value("${CLIENTSECRET}")
	private String Secret;

	// 企业账户编号
	@Value("${ACCOUNT}")
	private String Account;

	// 企业序列号
	@Value("${SERIALNUMBER}")
	private String SerialNumber;

	// 用户编号 0 PC 1WX 2APP
	@Value("${USERCODE}")
	private String UserCode;

	// 用户名称 Club10_PC、Club10_WX、Club10_APP、Club10_OTH
	@Value("${USERNAME}")
	public String UserName;

	/**
	 * 发送单人单条短信
	 * 
	 * @param phone
	 * @return
	 */
	public String sendSigleMsg(String phone, String[] msText, SMSModel sms) {
		try {
			MSKey msKey = new MSKey(UserCode, UserName, Account, SerialNumber, sms.getSerialCode());
			MSValue msValue = new MSValue(phone, msText, sms.getType());
			List<MSValue> MsList = new ArrayList<>();
			MsList.add(msValue);
			return requestUrl(new MSEntity(msKey, MsList));
		} catch (Exception e) {
			log.error("短信发送失败", e);
			e.printStackTrace();
			return null;
		}
	}

	private String requestUrl(MSEntity msEntity) throws Exception {
		// 请求参数
		String params = JSONObject.toJSON(msEntity).toString();
		// 第三方服务地址
		String url = BASE_URL + SENDSMS_URL;
		// 获取token
		String token = "";
		try {
			// 先从缓存中查找，如果token存在，就不必再去获取
			if (jedisClient != null) {
				token = jedisClient.get(ACCESS_TOKEN);
			}
		} catch (Exception e) {
			log.error("获取缓存ACCESS_TOKEN失败", e);
		}
		// 如果缓存中不存在，再去获取
		if (StringUtils.isBlank(token)) {
			token = getAccessToken();
		}
		String authorization = authorization_bearer + token;
		// 发送post请求
		return HttpUtil.post(url, params, authorization);
	}

	/**
	 * 发送多人同内容短信
	 * 
	 * @param msgList
	 * @return
	 * @throws Exception
	 */
	public String sendMultipleMsg(List<String> phoneNumbers, String[] msText, SMSModel sms) throws Exception {
		try {
			if (phoneNumbers == null || phoneNumbers.size() < 1) {
				return null;
			}
			MSKey msKey = new MSKey(UserCode, UserName, Account, SerialNumber, sms.getSerialCode());
			List<MSValue> MsList = new ArrayList<>();
			for (String phone : phoneNumbers) {
				MSValue msValue = new MSValue(phone, msText, sms.getType());
				MsList.add(msValue);
			}
			return requestUrl(new MSEntity(msKey, MsList));
		} catch (Exception e) {
			log.error("短信发送失败", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取token
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAccessToken() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		Date d0 = sdf.parse("1970-1-1 0:0:0");
		Date d1 = new Date();
		String tms = (d1.getTime() - d0.getTime()) / 10000 + "";
		String sign = DigestUtils.shaHex(tms + ClientID + Secret);
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credentials");
		params.put("tms", tms);
		params.put("sign", sign);
		String url = BASE_URL + TOKEN_URL;
		String authorization = authorization_base
				+ Base64.encodeBase64String((ClientID + ":" + Secret).getBytes(Charset.forName("US-ASCII")));
		String result = HttpUtil.post(url, params, authorization);
		JSONObject json = JSONObject.parseObject(result);
		String accessToken = json.get("access_token").toString();
		try {
			// 缓存accessToken
			if (jedisClient != null) {
				String expire = json.get("expires_in").toString();
				jedisClient.set(ACCESS_TOKEN, accessToken);
				jedisClient.expire(ACCESS_TOKEN, Integer.parseInt(expire));
			}
		} catch (Exception e) {
			log.error("缓存ACCESS_TOKEN失败", e);
		}
		return accessToken;
	}
}