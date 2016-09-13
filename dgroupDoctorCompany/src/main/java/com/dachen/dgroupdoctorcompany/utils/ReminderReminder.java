package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.AlarmBusiness;

import java.util.List;

/**
 * Created by Burt on 2016/8/17.
 */
public class ReminderReminder {
    public static ReminderReminder reminderReminder;
    public static ReminderReminder getReminderReminder(){
        if (reminderReminder == null){
            reminderReminder = new ReminderReminder();
        }
        return reminderReminder;
    }
    RemindDao dao;
    public void setReminders(Context context){
        dao = new RemindDao(context);
        List<Reminder> reminders =  dao.queryAllByUserid();
        int rr = reminders.size();
        for (int i=0;i<reminders.size();i++){
            AlarmBusiness.cancelAlarm(context, reminders.get(i));
            AlarmBusiness.setAlarm(context, reminders.get(i),false);
        }

    }
}
