package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SignInRemindAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
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
    RelativeLayout rl_empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_remind);

    }

    private void initViews() {
        tv_week = getViewById(R.id.tv_week);
        tv_time = getViewById(R.id.tv_time);
        remind_list = getViewById(R.id.remind_list);
        btn_add = getViewById(R.id.btn_add);

        rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
        rl_empty.setVisibility(View.GONE);
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        tv_week.setText("星期" + TimeUtils.getWeekStr(dayInWeek));
        String time = com.dachen.medicine.common.utils.TimeUtils.getTimeDay3(curTime);
        tv_time.setText(time);
        List<Reminder> lists = new ArrayList<>();
        RemindDao dao = new RemindDao(this);
        lists = dao.queryAllByUserid();

        adapter = new SignInRemindAdapter(this,lists);
        remind_list.setAdapter(adapter);
        remind_list.setEmptyView(rl_empty);
        remind_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SigninRemindActivity.this, AddSigninRemindActivity.class);
                intent.putExtra("reminder", (Reminder) adapter.getItem(position));
                startActivity(intent);
            }
        });
        if (lists.size()==0){
            rl_empty.setVisibility(View.VISIBLE);
        }else {
            rl_empty.setVisibility(View.GONE);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }
}
