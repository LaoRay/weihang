package cn.com.clubank.weihang.common.weihpay;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.com.clubank.weihang.common.util.MD5Util;

public class WeixinSignUtils {

	public static String getMoney(String amount) {
		if (amount == null) {
			return "";
		}
		// 金额转化为分为单位
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	public static String signMd5_2(Map<String, String> packageParams, String merchantKey) {
		StringBuffer sb = new StringBuffer();
		Set<Map.Entry<String, String>> es = packageParams.entrySet();
		Iterator<Map.Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + merchantKey);
		String sign = MD5Util.string2MD5(sb.toString()).toUpperCase();
		return sign;
	}

	public static String getNonceStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String createXML(Map<String, String> map, String s) throws UnsupportedEncodingException {
		StringBuilder xml = new StringBuilder("<xml>");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			xml.append("<").append(key).append(">").append(val).append("</").append(key).append(">");
		}
		xml.append("<").append("sign").append(">").append(s).append("</").append("sign").append(">");
		xml.append("</xml>");
		String xml1 = xml.toString();
		return xml1;
	}
}
