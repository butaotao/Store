package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.dachen.dgroupdoctorcompany.utils.TitleManager;

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
    LinearLayout ll_list;
    View view;
    RelativeLayout rl_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this,R.layout.activity_signin_remind,null);
        setContentView(view);

        setTitle("签到提醒");
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_titlebar.setBackgroundColor(getResources().getColor(R.color.color_3cbaff));
        TitleManager.showImage(this, view, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninRemindActivity.this, AddSigninRemindActivity.class);
                startActivity(intent);
            }
        }, "签到提醒", R.drawable.add_white);
        changerTitleBar();
    }

    private void initViews() {
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        /*rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);//color_3cbaff
        rl_title.setBackgroundColor(getResources().getColor(R.color.color_3cbaff));*/

        remind_list = (ListView) view.findViewById(R.id.remind_list);
      //  btn_add = (ImageButton) view.findViewById(R.id.btn_add);
        ll_list = (LinearLayout) view.findViewById(R.id.ll_list);
        rl_empty = (RelativeLayout) view.findViewById(R.id.rl_empty);
        rl_empty.setVisibility(View.GONE);
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        tv_week.setText("星期" + TimeUtils.getWeekStr(dayInWeek));
        String time = com.dachen.medicine.common.utils.TimeUtils.getTimeDay(curTime);
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
        rl_empty.setVisibility(View.GONE);
        if (lists.size()==0){
            rl_empty.setVisibility(View.VISIBLE);
            ll_list.setVisibility(View.GONE);
        }else {
            rl_empty.setVisibility(View.GONE);
            ll_list.setVisibility(View.VISIBLE);
        }
//        link_service.setOnClickListener(this);
       // btn_add.setOnClickListener(this);
        changerTitleBar();
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
