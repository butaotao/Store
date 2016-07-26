package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Burt on 2016/3/26.
 */
public class AlarmInfo extends Result implements Serializable {
    /**
     *{
     "data": [
     {
     "creator": 313,
     "creatorDate": 1464683134620,
     "goodsId": "000eb7eb95a046099ddf33e9746352a7",
     "id": "574d4a7ebae14d64d6de3fb9",
     "patientId": 1371,
     "reminderDays": 1,
     "reminderIntervals": 0,
     "reminderRinging": "班得瑞钢琴曲",
     "reminderTime1": "04:00",
     "startDate": 1464624000000,
     "status": 1,
     "updator": 313,
     "updatorDate": 1464683134620,
     "userId": 313
     }
     ],
     "resultCode": 1,
     "resultMsg": "success"
     }
     */
    private static final long serialVersionUID = -6961565060939190674L;
    public String doseReminderId;
    public String id;

    @Expose
    @SerializedName("creatorDate")
    public String number;
    //开始日期
    @Expose
    @SerializedName("startDate")
    public String start_date;
    //提醒间隔天数
    @Expose
    @SerializedName("reminderIntervals")
    public int days_remind_jg;
    //提醒持续天数
    @Expose
    @SerializedName("reminderDays")
    public int days_remind_cx;

    public int status;
    //提醒铃音
    @Expose
    @SerializedName("reminderRinging")
    public String remind_ly;
    //启用
    public boolean enable;
    //当前剩余药量
    public String goods_dqsy;
    //每次服药数量
    public String goods_mcfy;
    public String last_modified_time;
    public String reminderTime1;
    public String reminderTime2;
    public String reminderTime3;
    public String reminderTime4;

    //品种
    public Goods goods;
    public patient patient;
    public String goodsId;
    public String patientId;
    public String goodsName;
    public String patientName;
    public class Goods implements Serializable{
        public String title;
        public String _type;
        public String id;
    }
    public class patient implements Serializable{
        public int id;
        public String title;
        public String user_name;
    }
}