package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;

import java.util.LinkedList;

public class ExitActivity {
    private LinkedList<Activity> activityList = new LinkedList();
    private static ExitActivity instance;

    private ExitActivity() {
    }

    public static ExitActivity getInstance() {
        if (null == instance) {
            instance = new ExitActivity();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        //	System.exit(0);
    }
}
