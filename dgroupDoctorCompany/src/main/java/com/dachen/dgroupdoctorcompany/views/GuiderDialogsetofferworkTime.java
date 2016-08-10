package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.WeekDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.WeekSinger;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.AlarmBusiness;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Burt on 2016/6/23.
 */
public class GuiderDialogsetofferworkTime extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    TextView tv_onwork;
    int isworkTime;
    RemindDao dao;
    WeekDao weekDao;
    TimePickerCustomer timePicker;
    private List<String> mListLable = new ArrayList<>();
    private List<String> mListLableSelect = new ArrayList<>();
    public GuiderDialogsetofferworkTime(Activity activity) {
        super(activity, R.style.dialog_with_alpha);
        mActivity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_worktimeoffer);
        isworkTime = 0;
        dao = new RemindDao(mActivity);
        weekDao = new WeekDao(mActivity);
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        findViewById(R.id.btn_gosetting).setOnClickListener(this);
        tv_onwork = (TextView) findViewById(R.id.tv_onwork);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(false);

        timePicker = (TimePickerCustomer) findViewById(R.id.minute_pv);
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        timePicker.hours.setItems(timePicker.hourList, 18 + "");
        timePicker.mins.setItems(timePicker.minuteList, 0 + "");
        timePicker.setOnChangeListener(new TimePickerCustomer.OnChangeListener() {
            @Override
            public void onChange(int hours, int munites) {
              /*  hour = hours;
                minute = munites;*/
            }
        });
        lp.width = (int) (metric.widthPixels)-100;; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gosetting) {
            setTag();
            String  loginid= SharedPreferenceUtil.getString(mActivity, "id", "");
            Reminder reminder = new Reminder();

            reminder.createTime = System.currentTimeMillis();
            reminder.updateTime = System.currentTimeMillis();
            reminder.hour = timePicker.hours.getSelectedIndex();
            reminder.minute = timePicker.mins.getSelectedIndex();
            reminder.userloginid = loginid;
            reminder.isOpen = 1;
            ArrayList<WeekSinger> weekSinger = new ArrayList<WeekSinger>();
            String wweekday = "";
            if (mListLableSelect.size()>0){
                int[] paixu = sort();
                for (int k=0;k<paixu.length;k++){
                    WeekSinger s=new WeekSinger();
                    s.week = paixu[k];
                    s.reminder = reminder;
                    weekSinger.add(s);
                    wweekday+=s.week+",";
                }
            }


            reminder.times = weekSinger.size();
            reminder.weeks = weekSinger;
            reminder.weekday = wweekday;
            dao.addRemind(reminder);
            for(WeekSinger s:weekSinger ){
                weekDao.addRemind(s);
            }
            List<Reminder> reminders =  dao.queryAllByUserid();
            int rr = reminders.size();
            AlarmBusiness.setAlarm(mActivity, reminder);


                closeDialog();
                GuiderDialogsetworkTimeOver dialogsetworkTimeOver = new GuiderDialogsetworkTimeOver(mActivity);
                dialogsetworkTimeOver.showDialog();

        }
    }

    public void showDialog(){
        if(!isShowing() ){

            show();

        }

    }
    public void showDialog(String id){



       showDialog();;

    }
    public void closeDialog(){
        if(isShowing())
            dismiss();
    }



    @Override
    public void onInit(ArrayList<View> arg0) {
        // TODO Auto-generated method stub

    }
    public int[]  sort(){
        int[] paixu = new int[mListLableSelect.size()];

        for (int i=0;i<mListLableSelect.size();i++){
            paixu[i] = getstate(mListLableSelect.get(i));
        }

        for(int i=0;i<paixu.length;i++)
        {
            for(int j=0;j<paixu.length-i-1;j++)
            {
                int temp=0;
                if(paixu[j]>paixu[j+1])
                {
                    temp=paixu[j];
                    paixu[j]=paixu[j+1];
                    paixu[j+1]=temp;
                }
            }
        }
        return paixu;
    }
    private void setTag(){
        mListLableSelect.add("每周一");
        mListLableSelect.add("每周二");
        mListLableSelect.add("每周三");
        mListLableSelect.add("每周四");
        mListLableSelect.add("每周五");

    }
    public int getstate(String date){
        if (date.contains("日")){
            return 7;
        }else if (date.contains("一")){
            return 1;
        }else if (date.contains("二")){
            return 2;
        }else if (date.contains("三")){
            return 3;
        }else if (date.contains("四")){
            return 4;
        }else if (date.contains("五")){
            return 5;
        }else if (date.contains("六")){
            return 6;
        }

        return -1;
    }
}
