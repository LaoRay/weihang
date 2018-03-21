package cn.com.clubank.weihang.common.weihpay;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import cn.com.clubank.weihang.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付util
 * 
 * @author YangWei
 *
 */
@Slf4j
@Component
@SuppressWarnings("deprecation")
public class WeihWeixinPayUtil {

	@Value("${weixin.appId}")
	private String appId;
	
	@Value("${weixin.mchId}")
	private String mchId;
	
	@Value("${weixin.mchKey}")
	private String mchKey;
	
	@Value("${weixin.notifyUrl}")
	private String notifyUrl;
	
	/**
	 * 微信签名
	 * @param out_trade_no	订单号
	 * @param total			支付金额
	 * @param trade_type	支付类型
	 * @param desc			商品描述
	 * @param ip			客户端ip
	 * @return
	 */
	public String sign(String out_trade_no, String total, String trade_type, String desc, String attach, String ip) {
		if (StringUtil.isBlank(trade_type)) {
			trade_type = "APP"; //默认APP
		}
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);// 应用id
		packageParams.put("mch_id", mchId);// 商户号
		packageParams.put("nonce_str", WeixinSignUtils.getNonceStr());// 随机字符串
		packageParams.put("body", desc);// 商品描述
		packageParams.put("out_trade_no", out_trade_no);// 商户号 
		packageParams.put("spbill_create_ip", ip); //客户端ip
		packageParams.put("total_fee", WeixinSignUtils.getMoney(total));// 订单总金额
		packageParams.put("notify_url", notifyUrl); //回调地址
		packageParams.put("trade_type", trade_type);
		packageParams.put("attach", attach);
		
		String sign = WeixinSignUtils.signMd5_2(packageParams, mchKey);
		log.info("微信支付签名--sign--=" + sign);
		return sign;
	}
	
	/**
	 * 微信退款
	 * @param transId
	 * @param total
	 * @return
	 */
	public boolean refund(String transId, String total, String refundTotal) {
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);// 应用id
		packageParams.put("mch_id", mchId);// 商户号
		packageParams.put("nonce_str", WeixinSignUtils.getNonceStr());// 随机字符串
		packageParams.put("transaction_id", transId);// 订单号
		packageParams.put("out_refund_no", "refund" + WeixinSignUtils.getNonceStr());// 退款单号
		packageParams.put("total_fee", WeixinSignUtils.getMoney(total));// 订单总金额
		packageParams.put("refund_fee", WeixinSignUtils.getMoney(refundTotal));// 退款总金额
		
		String sign = WeixinSignUtils.signMd5_2(packageParams, mchKey);
		log.info("微信退款签名sign=" + sign);

		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		String xml = null;
		try {
			xml = WeixinSignUtils.createXML(packageParams, sign.toUpperCase());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("微信退款xml=" + xml);
		String retur = null;
		try {
			retur = this.clientCustomSSLRefund(createOrderURL, xml);
			log.info("微信退款结果：" + retur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(retur)) {
			Map<String, String> map = parseXmlToMap(retur);
			String returnCode = (String) map.get("return_code");
			if (returnCode.equals("SUCCESS")) {
				String resultCode = (String) map.get("result_code");
				if (resultCode.equals("SUCCESS")) {
					log.info("微信退款成功:{}", (String) map.get("out_trade_no"));
					return true;
				}
			} else {
				log.info("微信退款失败 refundfail msg= " + (String) map.get("return_msg"));
			}
		}
		return false;
	}

	/**
	 * xml转map
	 * @param xml
	 * @return
	 */
	private static Map<String, String> parseXmlToMap(String xml) {
		// Map retMap = new HashMap();
		SortedMap<String, String> retMap = new TreeMap<String, String>();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			Element root = (Element) doc.getRootElement();// 指向根节点
			@SuppressWarnings("unchecked")
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	
	/**
	 * 调用url实现退款
	 * @param xml
	 * @return
	 */
	private String clientCustomSSLRefund(String url, String data) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream is = new FileInputStream(this.getClass().getClassLoader().getResource("apiclient_cert.p12").getFile());
		try {
			keyStore.load(is, mchId.toCharArray());// 这里写密码..默认是你的MCHID
		} finally {
			is.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray())// 这里也是写密码的
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpost = new HttpPost(url); // 设置响应头信息
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();

				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
}
