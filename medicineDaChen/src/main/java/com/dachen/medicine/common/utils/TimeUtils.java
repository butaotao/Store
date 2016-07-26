package com.dachen.medicine.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;

/**
 * 时间工具类
 * 
 * @author yuankangle
 * 
 */
public class TimeUtils {

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(long sdate) {
		boolean temp = false;
		try {
			Date time = new Date(sdate);
			Date today = new Date();
			if (time != null) {
				String nowDate = dateFormater.get().format(today);
				String timeDate = dateFormater.get().format(time);
				if (nowDate.equals(timeDate)) {
					temp = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		}
	};

	/**
	 * 返回当前时间
	 * 
	 * @author butaotao
	 * @return "yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String getTime() {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 格式化输出
		return df.format(tasktime);
	}
	/**
	 * 判断给定字符串时间是否为今日
	 *
	 * @param sdate
	 * @return boolean
	 */
	public static String getTime(long sdate) {
		boolean temp = false;
		String timeDate = "";
		try {
			if (0!=sdate){
				Date time = new Date(sdate);
				if (time != null) {
					timeDate = dateFormater.get().format(time);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDate;
	}
	/**
	 * 
	 * Description:获取当前时间
	 * 
	 * @author 2015-07-09 修改
	 * @return
	 */
	public static long getNowTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * Description: 获取时间戳
	 * 
	 * @author 2015-07-09 修改
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimestamp() {
		Date date = new Date();
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	public static String[] parseDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {

		}
		Calendar c = Calendar.getInstance();
		c.setTime(returnDate);
		String[] s = new String[7];
		s[0] = (c.get(Calendar.YEAR)) + "";
		s[1] = (c.get(Calendar.MONTH) + 1) + "";
		s[2] = c.get(Calendar.DATE) + "";
		s[3] = change((c.get(Calendar.DAY_OF_WEEK) - 1) + "");

		long mills = 0;
		mills = c.getTimeInMillis();
		s[4] = mills + "";
		s[5] = c.get(Calendar.HOUR_OF_DAY)+"";
		s[6] = c.get(Calendar.MINUTE)+"";
		return s;
	}
	public static String change(String s) {
		String week = s;
		if (s.equals("1")) {
			week = "一";
		} else if (s.equals("2")) {
			week = "二";
		} else if (s.equals("3")) {
			week = "三";
		} else if (s.equals("4")) {
			week = "四";
		} else if (s.equals("5")) {
			week = "五";
		} else if (s.equals("6")) {
			week = "六";
		} else if (s.equals("7")) {
			week = "日";
		}else if (s.equals("0")) {
			week = "日";
		}
		return week;
	}
	public static long getDays(String date1) {
		if (date1 == null || date1.equals(""))
			return 0;

		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
		} catch (Exception e) {
		}
		long day = date.getTime();
		return day;
	}


	/**
	 * 判断闹钟是否过期
	 *
	 * @param
	 * @param
	 * @param repeatDays
	 * @return
	 */
	public static boolean isAlarmExpired(  String begindata, int repeatDays) {
		long time = TimeUtils.getDays(begindata);
		// 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, repeatDays);

		long endTime = calendar.getTimeInMillis();
		endTime = time+24*60*60*1000*repeatDays;
		long nowTime = System.currentTimeMillis();
		boolean flag = false;
		if ((endTime-nowTime)>0) {
			flag = false;//m没有过期
		} else {
			flag = true;
		}
		return  flag;
	}

}
