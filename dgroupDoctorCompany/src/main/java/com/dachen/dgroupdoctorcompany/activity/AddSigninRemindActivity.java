package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.views.ItemContainer;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_signin_remind);
        initViews();
    }

    private void initViews() {
        vLableContainer = getViewById(R.id.vLableContainer);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {


        }
    }

}
