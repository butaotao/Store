package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SignInRemindAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 签到提醒
 *
 */
public class SigninRemindActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = SigninRemindActivity.class.getSimpleName();


    private TextView tv_week,tv_time;
    private ListView remind_list;
    private ImageButton btn_add;
    SignInRemindAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_remind);
        initViews();
    }

    private void initViews() {
        tv_week = getViewById(R.id.tv_week);
        tv_time = getViewById(R.id.tv_time);
        remind_list = getViewById(R.id.remind_list);
        btn_add = getViewById(R.id.btn_add);

        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        tv_week.setText("星期" + TimeUtils.getWeekStr(dayInWeek));
        tv_time.setText(TimeUtils.chat_long_2_str(curTime));
        List<SearchRecords> lists = new ArrayList<>();
        SearchRecords s1 = new SearchRecords();
        SearchRecords s2 = new SearchRecords();
        SearchRecords s3 = new SearchRecords();
        lists.add(s1);
        lists.add(s2);
        lists.add(s3);

        adapter = new SignInRemindAdapter(this,lists);
        remind_list.setAdapter(adapter);

//        link_service.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(this, AddSigninRemindActivity.class);
                startActivity(intent);
                break;

        }
    }

}
