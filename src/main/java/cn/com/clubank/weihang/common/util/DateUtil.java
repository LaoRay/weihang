package cn.com.clubank.weihang.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * 
 * @author LeiQY
 *
 */
public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 判断是否为今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		Date currentDate = new Date();
		if (sdf.format(currentDate).equals(sdf.format(date))) {
			return true;
		}
		return false;
	}

	/**
	 * 用字符串判断 "20170914"
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(String date) {
		Date currentDate = new Date();
		if (sdf.format(currentDate).equals(date)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为昨天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - Constants.INT_1);
		if (sdf.format(calendar.getTime()).equals(sdf.format(date))) {
			return true;
		}
		return false;
	}

	/**
	 * 以指定的格式来格式化日期
	 *
	 * @param date
	 *            Date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 得到当前的时间，精确到秒,共19位 返回格式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentTime() {
		Date NowDate = new Date();
		return formatter.format(NowDate);
	}

	/**
	 * 得到当前日期加上某一个整数的日期（正数往后推， 负数往前移动 ） 返回格式：yyyy-MM-dd
	 * 
	 * @param add_day
	 * @return
	 */
	public static String addNowTimeDay(int add_day) {
		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, add_day);// 把日期往后增加一天.整数往后推,负数往前移动
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到当前小时加上某一个整数的小时（正数往后推， 负数往前移动 ） 返回格式：yyyy-MM-dd HH
	 * 
	 * @param add_hour
	 * @return
	 */
	public static String addNowTimeHour(int add_hour) {
		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR, add_hour);// 把日期往后增加一天.整数往后推,负数往前移动
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
			return formatter.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 给某个时间加n小时
	 * 
	 * @param d
	 * @param hour
	 * @return
	 */
	public static Date addHour(Date d, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.HOUR, hour);
		Date date = c.getTime();
		return date;
	}

	/**
	 * 时间比较
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int compareDate(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			return 1;
		} else if (d1.getTime() < d2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 计算活动距离开始结束时长
	 * 
	 * @param begin
	 * @param now
	 * @param end
	 * @return
	 */
	public static String decrease(Date begin, Date now, Date end) {
		long t = 0;
		// 活动未开始
		String surplusTime = "";
		if (DateUtil.compareDate(begin, now) > 0) {
			t = begin.getTime() - now.getTime();
			surplusTime += "距开始 ";
		} else {
			t = end.getTime() - now.getTime();
			surplusTime += "距结束 ";
		}
		if (t <= 0) {
			return "已结束 0:0:0";
		}
		if (t / 1000 / 60 / 60 >= 24) {
			surplusTime += t / 1000 / 60 / 60 / 24 + "天";
		}
		surplusTime += (t / 1000 / 60 / 60 % 24 + ":" + t / 1000 / 60 % 60 + ":" + t / 1000 % 60);
		return surplusTime;
	}
}
