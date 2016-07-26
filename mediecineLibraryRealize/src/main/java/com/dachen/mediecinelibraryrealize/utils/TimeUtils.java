package com.dachen.mediecinelibraryrealize.utils;

import com.dachen.medicine.common.utils.Alarm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Burt on 2016/3/16.
 */
public class TimeUtils {
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
    public static long getDays2(String date1) {
        if (date1 == null || date1.equals(""))
            return 0;

        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;

        try {
            date = myFormatter.parse(date1);
            long day = date.getTime();
            return day;
        } catch (Exception e) {
        }
        return 0;
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
    //
    public static int[] parseDate2(long time) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        int[] s = new int[7];
        s[0] = (c.get(Calendar.YEAR)) ;
        s[1] = (c.get(Calendar.MONTH) + 1) ;
        s[2] = c.get(Calendar.DATE)  ;
        s[3] = 0;

        s[4] = 0  ;
        s[5] = c.get(Calendar.HOUR_OF_DAY)  ;
        s[6] = c.get(Calendar.MINUTE) ;
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

    /**
     * 判断闹钟是否过期
     *
     * @param
     * @param
     * @param repeatDays
     * @return
     */
    public static boolean isAlarmExpired(Alarm alarm ) {
        if (alarm.drugRemind==null){
            return false;
        }
        long time = TimeUtils.getDays(alarm.drugRemind.begindata);
        // 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, alarm.drugRemind.repeatDayIndex);

        long endTime = calendar.getTimeInMillis();
        //endTime = time+24*60*60*1000*alarm.drugRemind.repeatDayIndex;
        long nowTime = System.currentTimeMillis();
        boolean flag = false;

        if ((endTime-nowTime)>0) {
            flag = false;//m没有过期
        } else {
            flag = true;
        }
        return  flag;
    }
    /**
     * 判断闹钟是否过期
     *
     * @param
     * @param
     * @param
     * @return
     */
    public static boolean isAlarmShouldRing(Alarm alarm ) {
        if (alarm.drugRemind==null){
            return false;
        }

        long time = TimeUtils.getDays(alarm.drugRemind.begindata);
        if (isToday(time)){
            return  true;
        }
        // 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
        int keepday = alarm.drugRemind.repeatDayIndex;
        int d = 1;
            d = (keepday-1)/(alarm.drugRemind.repeatPeriodIndex+1);

        boolean flag = false;
        for (int i=0;i<=d;i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendar.add(Calendar.DAY_OF_YEAR, i*(alarm.drugRemind.repeatPeriodIndex+1));
            long ringtime = calendar.getTimeInMillis();

           if (isToday(ringtime)){
               flag = true;//
               break;
           };
        }
        return  flag;
    }


    public static long getTime(int  hour,int minute ,String begindata) {

        long time = TimeUtils.getDays( begindata);

        // 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

            long ringtime = calendar.getTimeInMillis();


        return  ringtime;
    }
    public static long getTimesby(int  hour,int minute ,int year) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long ringtime = calendar.getTimeInMillis();


        return  ringtime;
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

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
    };
    public static long getTime(String date1) {
        if (date1 == null || date1.equals(""))
            return 0;

        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
        } catch (Exception e) {
        }
        long day = date.getTime();
        return day;
    }
}
