package com.dachen.dgroupdoctorcompany.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by weiwei on 2016/5/18.
 */
public class TimeFormatUtils {
    public static final SimpleDateFormat china_s_format = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat s_format_week = new SimpleDateFormat("EE");
    public static final SimpleDateFormat china_short_format = new SimpleDateFormat("M月dd日");
    public static final SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat f_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public static String short_china_format_date(Date date){
        return china_short_format.format(date);
    }

    public static String time_format_date(Date date){
        return time_format.format(date);
    }

    public static String china_format_date(Date date){
        return china_s_format.format(date);
    }

    public static String week_format_date(Date date){
        return s_format_week.format(date);
    }

    public static String format_date(Date date){
        return f_format.format(date);
    }

    public static String china_format_time(Date date){
        int nHour = date.getHours();
        int mMinute = date.getMinutes();
        String strTime="";
        if(nHour>12){
            if(mMinute<10){
                strTime = "下午"+(nHour-12)+":0"+mMinute;
            }else{
                strTime = "下午"+(nHour-12)+":"+mMinute;
            }
        }else{
            if(mMinute<10){
                strTime = "上午"+(nHour)+":0"+mMinute;
            }else{
                strTime = "上午"+(nHour)+":"+mMinute;
            }
        }
        return strTime;
    }
}
