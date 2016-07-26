package com.dachen.mediecinelibraryrealize.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Burt on 2016/6/7.
 */
public class PatientBox {
    public long creatorDate;
    public String doseDate;
    public String doseReminderId;
    public String id;
    public String patientId;
    public String reminderRecord;
    public String reminderTime;
    public String updatorDate;
    public String goodsName;
    public int timeType;
    @Expose
    @SerializedName("goodsTitle")
    public String title;
}
