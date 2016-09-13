package com.dachen.dgroupdoctorcompany.db.dbentity;

import com.dachen.medicine.common.utils.Alarm;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Burt on 2016/8/3.
 */
@DatabaseTable(tableName = "table_companyremindersigner")
public class Reminder implements Serializable{
    @DatabaseField(generatedId=true)
    public int _id;
    @DatabaseField(columnName = "hour")
    public int hour;
    @DatabaseField(columnName = "userloginid")
    public String userloginid;
    @DatabaseField(columnName = "minute")
    public int minute;
    @DatabaseField(columnName = "time")
    public long time;
    @DatabaseField(columnName = "createTime")
    public long createTime;
    @DatabaseField(columnName = "updateTime")
    public long updateTime;
    @DatabaseField(columnName = "isonce")
    public int times;
    @DatabaseField(columnName = "weekday")
    public String weekday;
    @DatabaseField(columnName = "isOpen")
    public int isOpen;
    @ForeignCollectionField(eager = true)
    public Collection<WeekSinger> weeks;// 一条用药提醒的所有闹钟集合
}
