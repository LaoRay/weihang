package cn.com.clubank.weihang.common.util;

import java.util.Random;

/**
 * 字符串工具类
 * 
 * @author simplife
 *
 */
public class StringUtil {

	/**
	 * 第一个字符小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerCaseFirstChar(String str) {

		char ch = str.charAt(0);

		if (!Character.isLowerCase(ch)) {

			str = str.replace(str.charAt(0), (char) (str.charAt(0) + 32));

		}

		return str;

	}

	/**
	 * 首字符大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstChar(String str) {

		char ch = str.charAt(0);

		if (!Character.isUpperCase(ch)) {

			str = String.valueOf(((char) (str.charAt(0) - 32))) + str.substring(1);

		}

		return str;
	}

	/**
	 * 判断一个字符串是否为空
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isBlank(String src) {
		return src == null || src.length() == 0;
	}

	/**
	 * 判断一个字符串是否为空
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isBlank(Integer src) {
		return src == null;
	}

	/**
	 * 判断一个字符串是否不为空
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isNotBlank(String src) {
		return src != null && src.length() > 0;
	}

	/**
	 * 随机生成num位字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String randomString(int num) {
		String str = "";
		char[] chs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		Random rd = new Random();
		for (int i = 0; i < num; i++) {
			str += chs[rd.nextInt(36)];
		}
		return str;
	}

	public static String getVirtualCarNo() {
		char[] chs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char[] chw = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random rd = new Random();
		String virtualCarNo = "威H" + chw[rd.nextInt(10)] + chw[rd.nextInt(10)]
				+ chs[rd.nextInt(26)] + chs[rd.nextInt(26)] + chs[rd.nextInt(26)];
		return virtualCarNo;
	}
}
