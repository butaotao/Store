package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Burt on 2016/6/29.
 */
public class SiginDetailActivity extends BaseActivity implements HttpManager.OnHttpListener{
    TextView tvAddress;
    TextView tvDate;
    TextView tvTime;
    EditText etRemark;
    TextView tvFlag1;
    String remark;
    LinearLayout ll_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_detailactiviy);
        ll_state = (LinearLayout) findViewById(R.id.ll_state);
        enableBack();
        setTitle("考勤打卡");
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        etRemark = (EditText) findViewById(R.id.etRemark);
        tvFlag1 = (TextView) findViewById(R.id.tvFlag1);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        remark = getIntent().getStringExtra("remark");
        String day = getIntent().getStringExtra("day");
        String hour = getIntent().getStringExtra("hour");
        String tag = getIntent().getStringExtra("tag");
        String address = getIntent().getStringExtra("address");
        long longTime = getIntent().getLongExtra("longTime",0);
        if(!TextUtils.isEmpty(day) && !TextUtils.isEmpty(hour)){
            tvDate.setText(day);
            tvTime.setText(hour);
        }else if(longTime>0){
            SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(longTime);
            String strDate = s_format.format(date);
            String strTime= TimeFormatUtils.time_format_date(date);
            tvDate.setText(strDate);
            tvTime.setText(strTime);
        }else{
            tvDate.setText("");
            tvTime.setText("");
        }

        etRemark.setText(remark);
//        etRemark.setFocusable(false);
//        etRemark.setEnabled(false);
        tvFlag1.setText(tag);
        tvAddress.setText(address);
        ll_state.setVisibility(View.GONE);
       if (!TextUtils.isEmpty(tag)){
            ll_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
