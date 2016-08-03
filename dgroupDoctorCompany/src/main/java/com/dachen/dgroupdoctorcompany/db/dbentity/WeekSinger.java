package com.dachen.dgroupdoctorcompany.db.dbentity;

import com.dachen.medicine.common.utils.DrugRemind;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/3.
 */
@DatabaseTable(tableName = "table_companyreminderweek")
public class WeekSinger implements Serializable{
    @DatabaseField(columnName = "week")
    public int  week;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "remind_id")
    public Reminder reminder;
}
