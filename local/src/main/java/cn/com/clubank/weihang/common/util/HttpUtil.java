package cn.com.clubank.weihang.common.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

	private static final String ENCODING = "UTF-8";

	/**
	 * 参数为Map的post
	 * @param url
	 * @param paramsMap
	 * @param authorization
	 * @return
	 */
	public static String post(String url, Map<String, String> paramsMap, String authorization) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
				method.setHeader("Authorization", authorization.trim());
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity, ENCODING);
			}
		} catch (Exception e) {
			log.error("http request failed", e);
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				log.error("htpp close failed", e);
			}
		}
		return responseText;
	}

	/**
	 * 参数为Json形式的post
	 * @param url
	 * @param jsonParams
	 * @param authorization
	 * @return
	 */
	public static String post(String url, String jsonParams, String authorization) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (!StringUtils.isBlank(jsonParams)) {
				StringEntity entity = new StringEntity(jsonParams, ENCODING);// 解决中文乱码问题
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
				method.setHeader("Authorization", authorization.trim());
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity, ENCODING);
			}
		} catch (Exception e) {
			log.error("http request failed", e);
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				log.error("htpp close failed", e);
			}
		}
		return responseText;
	}

	/**
	 * get
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static String get(String url, Map<String, String> paramsMap) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			String getUrl = url + "?";
			if (paramsMap != null) {
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					getUrl += param.getKey() + "=" + URLEncoder.encode(param.getValue(), ENCODING) + "&";
				}
			}
			HttpGet method = new HttpGet(getUrl);
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			log.error("http request failed", e);
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				log.error("htpp close failed", e);
			}
		}
		return responseText;
	}

}
