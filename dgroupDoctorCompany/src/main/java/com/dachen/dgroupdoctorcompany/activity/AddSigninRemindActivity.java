package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.WeekSinger;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.AlarmBusiness;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;
import com.dachen.dgroupdoctorcompany.views.ItemContainer;
import com.dachen.dgroupdoctorcompany.views.TimePickerEx;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
 

/**
 * 添加签到提醒
 *
 */
public class AddSigninRemindActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = AddSigninRemindActivity.class.getSimpleName();


    private TextView tv_week,tv_time;
    private ListView remind_list;
    private ImageButton btn_add;
    private ItemContainer vLableContainer;
    private List<String> mListLable = new ArrayList<>();
    private List<String> mListLableSelect = new ArrayList<>();
    RemindDao dao;
    int hour;
    int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_add_signin_remind, null);
        setContentView(view);
        dao = new RemindDao(this);
        initViews();
        setTag();
        setTitle("签到提醒");
        TitleManager.showText(this, view, this, "保存");
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            data.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            seconds.add(i + "");
        }
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
    }
    private void setTag(){
        mListLable.add("每周一");
        mListLable.add("每周二");
        mListLable.add("每周三");
        mListLable.add("每周四");
        mListLable.add("每周五");
        mListLable.add("每周六");
        mListLable.add("每周日");
        for(int i=0;i<mListLable.size();i++){
            String strLableItem = mListLable.get(i);
            addLableItem(strLableItem,false);
        }
    }
    private void addLableItem(String lable,boolean isEdite){
        TextView textView = new TextView(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        textView.setLayoutParams(params);
        textView.setTextSize(14);
        textView.setText(lable);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, (int) CommonUitls.dpToPixel(4f, this), 0, 0);//biaoqian_dis  biaoqian
        textView.setBackgroundResource(R.drawable.biaoqian_dis);
     //   textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextColor(getResources().getColor(R.color.blue_3cbaff));

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewItem = (TextView) v;
                String lableItem = textViewItem.getText().toString();
                if (mListLableSelect.contains(lableItem)) {
                    mListLableSelect.remove(lableItem);
                    textViewItem.setBackgroundResource(R.drawable.biaoqian_dis);
                    textViewItem.setTextColor(getResources().getColor(R.color.blue_3cbaff));
                } else {
                    mListLableSelect.add(lableItem);
                    textViewItem.setBackgroundResource(R.drawable.biaoqian);
                    textViewItem.setTextColor(getResources().getColor(R.color.white));

                }
            }
        });
        vLableContainer.addView(textView);
    }

    private void initViews() {
        vLableContainer = getViewById(R.id.vLableContainer);
        vLableContainer.setItemNum(5);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_search:
                String  loginid= SharedPreferenceUtil.getString(this, "id", "");
                Reminder reminder = new Reminder();
                reminder.createTime = System.currentTimeMillis();
                reminder.hour = hour+"";
                reminder.minute = minute+"";
                reminder.userloginid = loginid;
                ArrayList<WeekSinger> weekSinger = new ArrayList<WeekSinger>();
                for (int i=0;i<mListLableSelect.size();i++){
                    WeekSinger s=new WeekSinger();
                    s.week = getstate(mListLableSelect.get(i));
                    weekSinger.add(s);
                }
                reminder.weeks = weekSinger;
                dao.addRemind(reminder);
                List<Reminder> reminders =  dao.queryAllByUserid();
                int rr = reminders.size();
                AlarmBusiness.setAlarm(this, reminder);
                break;

        }
    }
    public int getstate(String date){
        if (date.contains("日")){
            return 1;
        }else if (date.contains("一")){
            return 2;
        }else if (date.contains("二")){
            return 3;
        }else if (date.contains("三")){
            return 4;
        }else if (date.contains("四")){
            return 5;
        }else if (date.contains("五")){
            return 6;
        }else if (date.contains("六")){
            return 7;
        }

        return -1;
    }

}
