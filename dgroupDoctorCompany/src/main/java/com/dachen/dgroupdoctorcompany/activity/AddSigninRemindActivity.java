package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.WeekDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.WeekSinger;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.AlarmBusiness;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;
import com.dachen.dgroupdoctorcompany.views.ItemContainer;
import com.dachen.dgroupdoctorcompany.views.TimePickerCustomer;
import com.dachen.dgroupdoctorcompany.views.TimePickerEx;
import com.dachen.dgroupdoctorcompany.views.WheelView;
import com.dachen.dgroupdoctorcompany.views.wheel.TimePicker;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;

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
    WeekDao weekDao;
    int hour;
    int minute;
    TimePickerCustomer timePicker;
    Reminder changereminder;
    LinearLayout rl_deletealert;
    int isOpen = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_add_signin_remind, null);
        setContentView(view);
        changereminder = (Reminder) getIntent().getSerializableExtra("reminder");
        dao = new RemindDao(this);
        weekDao = new WeekDao(this);
        isOpen = 1;
        initViews();
        setTag();
        setTitle("签到提醒");
        TitleManager.showText(this, view, this, "保存");
        timePicker = (TimePickerCustomer) findViewById(R.id.minute_pv);
        if (changereminder!=null){
            hour = changereminder.hour;
            minute = changereminder.minute;
            rl_deletealert.setVisibility(View.VISIBLE);

            String h = String.format("%02d", hour);
            String m = String.format("%02d", minute);
            timePicker.hours.setItems(timePicker.hourList,h+"");
            timePicker.mins.setItems(timePicker.minuteList, m + "");
            isOpen = changereminder.isOpen;
        }else {
            long curTime = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(curTime));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            String h = String.format("%02d", hour);
            String m = String.format("%02d", minute);
            timePicker.hours.setItems(timePicker.hourList,h+"");
            timePicker.mins.setItems(timePicker.minuteList, m + "");

            isOpen = 1;
            rl_deletealert.setVisibility(View.GONE);
        }

        timePicker.setOnChangeListener(new TimePickerCustomer.OnChangeListener() {
            @Override
            public void onChange(int hours, int munites) {
                hour = hours;
                minute = munites;
            }
        });
       // test();
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
    public void test(){
        final WheelView wheelView = (WheelView) findViewById(R.id.minute_pv);
        final List<String> lists = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            lists.add("test:" + i);
        }
        wheelView.lists(lists).fontSize(35).showCount(5).selectTip(" ").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                ToastUtils.showToast(AddSigninRemindActivity.this,"current select:" + wheelView.getSelectItem() + " index :" + index + ",result=" + lists.get(index));
            }
        }).build();
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
        showHaveSetweekday(lable, textView);
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
        rl_deletealert = (LinearLayout) findViewById(R.id.rl_deletealert);
        rl_deletealert.setOnClickListener(this);
        vLableContainer = getViewById(R.id.vLableContainer);
        vLableContainer.setItemNum(5);
    }
    public void showHaveSetweekday(String day,TextView textViewItem){
        if (null!=changereminder){
            String t = changereminder.weekday;
            boolean flag = false;
            if (t.contains("1")&&day.contains("周一")){
                flag = true;
                mListLableSelect.add("每周一");
            }else if(t.contains("2")&&day.contains("周二")){
                mListLableSelect.add("每周二");
                flag = true;
            }else if(t.contains("3")&&day.contains("周三")){
                mListLableSelect.add("每周三");
                flag = true;
            }else if(t.contains("4")&&day.contains("周四")){
                mListLableSelect.add("每周四");
                flag = true;
            }else if(t.contains("5")&&day.contains("周五")){
                mListLableSelect.add("每周五");
                flag = true;
            }else if(t.contains("6")&&day.contains("周六")){
                mListLableSelect.add("每周六");
                flag = true;
            }else if(t.contains("7")&&day.contains("周日")){
                mListLableSelect.add("每周日");
                flag = true;
            }
            if (flag){
                textViewItem.setBackgroundResource(R.drawable.biaoqian);
                textViewItem.setTextColor(getResources().getColor(R.color.white));
            }

        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_search:
                String  loginid= SharedPreferenceUtil.getString(this, "id", "");
                Reminder reminder = new Reminder();
                if (null != changereminder ){
                    reminder.createTime = changereminder.createTime;
                }else {
                    reminder.createTime = System.currentTimeMillis();
                }
                reminder.updateTime = System.currentTimeMillis();
                reminder.hour = timePicker.hours.getSelectedIndex();
                reminder.minute = timePicker.mins.getSelectedIndex();
                reminder.userloginid = loginid;
                reminder.isOpen = isOpen;
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
                AlarmBusiness.setAlarm(this, reminder);
                ToastUtils.showToast(this,"保存成功!");
                finish();
                break;
            case R.id.rl_deletealert:
                if (null!=changereminder){
                    AlarmBusiness.cancelAlarm(this, changereminder);
                    dao.deleteByCreateTime(changereminder.createTime);
                    ToastUtils.showToast(this,"删除成功!");
                }

                finish();
                break;
        }
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

}
