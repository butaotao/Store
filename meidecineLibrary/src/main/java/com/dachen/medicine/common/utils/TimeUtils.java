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
	 * 获得下一次铃响时间
	 * 
	 * @param hour
	 * @param minute
	 * @param repeatPeriod
	 * @return
	 */
	public static long getNextAlarmTimeInMillis(int hour, int minute, int repeatPeriod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 如果闹钟的时间今天已过，则加上离它下一次铃响的天数
		if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
			calendar.add(Calendar.DAY_OF_YEAR, repeatPeriod);
		}

		return calendar.getTimeInMillis();
	}
	public static Calendar getNextAlarmTimeInMillis(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		return calendar ;
	}
	public static long getTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);


		return calendar.getTimeInMillis();
	}
	/**
	 * 获得下一次铃响时间
	 *
	 * @param hour
	 * @param minute
	 * @param repeatPeriod
	 * @return
	 */
	public static long getAfterSenvenDayTimeInMillis(long time, int repeatPeriod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

			calendar.add(Calendar.DAY_OF_YEAR, repeatPeriod);


		return calendar.getTimeInMillis();
	}
	public static long getAlarmTimeInMillis(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis();
	}

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
	/**
	 * 判断给定字符串时间是否为今日
	 *
	 * @param sdate
	 * @return boolean
	 */
	public static String getTime(long sdate) {
		String timeDate = "";
		try {
			Date time = new Date(sdate);
			if (time != null) {
				 timeDate = dateFormater.get().format(time);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDate;
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		}
	};
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		}
	};

	public static String getTimeRecord(long sdate) {
		String timeDate = "";
		try {
			Date time = new Date(sdate);
			if (time != null) {
				timeDate = dateFormaterrecord.get().format(time);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDate;
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormaterrecordday = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM月dd日 ", Locale.CHINA);
		}
	};
	public static String getTimeRecordDay(long sdate) {
		String timeDate = "";
		try {
			Date time = new Date(sdate);
			if (time != null) {
				timeDate = dateFormaterrecordday.get().format(time);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDate;
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormaterrecord = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM月dd日  mm:ss", Locale.CHINA);
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
	 * 返回当前时间
	 * 
	 * @author butaotao
	 * @return "yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String getTimeDay() {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 格式化输出
		return df.format(tasktime);
	}
	public static String getTimeDay3(long time) {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		cale.setTimeInMillis(time);
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		// 格式化输出
		return df.format(tasktime);
	}
	public static String getTimeDay2() {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		// 格式化输出
		return df.format(tasktime);
	}
	public static String getTimes() {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		// 格式化输出
		return df.format(tasktime);
	}
	public static String getTimesHourMinute(long time) {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		cale.setTimeInMillis(time);
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		// 格式化输出
		return df.format(tasktime);
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
	public static String[] parseDate() {
		String strDate = getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {

		}
		Calendar c = Calendar.getInstance();
		c.setTime(returnDate); 
		String[] s = new String[6];
		s[0] = (c.get(Calendar.YEAR)) + "";
		s[1] = (c.get(Calendar.MONTH) + 1) + "";
		s[2] = c.get(Calendar.DATE) + "";
		s[3] = change((c.get(Calendar.DAY_OF_WEEK) - 1) + ""); 
		 
		long mills = 0; 
		mills = c.getTimeInMillis();
		s[4] = mills+"";
		s[5] = c.get(Calendar.HOUR_OF_DAY)+"";
		return s;
	}
	public static String getWeek(long time){
		String week = "星期";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			week += "六";
		}



		return week;
	}
	public static String change(String s){
		String week =s;
		if (s.equals("1")) {
			week = "一";
		}else if (s.equals("2")) {
			week = "二";
		}else if (s.equals("3")) {
			week = "三";
		}else if (s.equals("4")) {
			week = "四";
		}else if (s.equals("5")) {
			week = "五";
		}else if (s.equals("6")) {
			week = "六";
		}else if (s.equals("7")) {
			week = "日";
		}else if (s.equals("0")) {
			week = "日";
		}
		return week;
	}
	public static  String getTimeHourandMinutes(String hour,String minute){
		return String.format("%02d:%02d",  hour,  minute);
	}
}
