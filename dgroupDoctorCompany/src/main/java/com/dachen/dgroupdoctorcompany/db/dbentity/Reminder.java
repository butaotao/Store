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
    public String hour;
    @DatabaseField(columnName = "userloginid")
    public String userloginid;
    @DatabaseField(columnName = "minute")
    public String minute;
    @DatabaseField(columnName = "time")
    public long time;
    @DatabaseField(columnName = "createTime")
    public long createTime;

    @ForeignCollectionField(eager = true)
    public Collection<WeekSinger> weeks;// 一条用药提醒的所有闹钟集合
}
