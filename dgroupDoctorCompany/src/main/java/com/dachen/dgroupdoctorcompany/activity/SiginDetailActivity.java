package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
    LinearLayout ll_sign_tag;
    String signedid;
    String tag;
    Button btSubmit;
    public String id;
    RelativeLayout rl_btnsmit;
    boolean searchSing;
    String address;
    double latitude;
    double longitude;
    ImageView ivFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = View.inflate(this,R.layout.activity_signed_detailactiviy,null);
        setContentView(v);
        rl_btnsmit = (RelativeLayout) findViewById(R.id.rl_btnsmit);
        ll_state = (LinearLayout) findViewById(R.id.ll_state);
        ll_sign_tag = (LinearLayout) findViewById(R.id.ll_sign_tag);
        findViewById(R.id.vAddress).setOnClickListener(this);
        enableBack();
        setTitle("签到详情");
        ivFlag = (ImageView) findViewById(R.id.ivFlag);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        etRemark = (EditText) findViewById(R.id.etRemark);
        tvFlag1 = (TextView) findViewById(R.id.tvFlag1);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        signedid = getIntent().getStringExtra("signedid");
        remark = getIntent().getStringExtra("remark");
        String day = getIntent().getStringExtra("day");
        String hour = getIntent().getStringExtra("hour");
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        tag = getIntent().getStringExtra("tag");
        address = getIntent().getStringExtra("address");
        long longTime = getIntent().getLongExtra("longTime",0);
        String from = getIntent().getStringExtra("from");
        ivFlag.setVisibility(View.GONE);
        searchSing = false;
        if ("searchSign".equals(from) ) {
            etRemark.setEnabled(false);
            etRemark.setHint("");
            searchSing = true;
        }else {
            if ((!TextUtils.isEmpty(tag)&&!tag.equals("拜访"))||TextUtils.isEmpty(tag)){
                TitleManager.showText(this, v, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etRemark.getText().toString())){
                            ToastUtil.showToast(SiginDetailActivity.this,"请输入备注!");
                            return;
                        }
                        upDate(signedid,etRemark.getText().toString());
                    }
                }, "保存");
            }

        }
        btSubmit = (Button) findViewById(R.id.btSubmit);

        btSubmit.setVisibility(View.GONE);
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
        ll_sign_tag.setVisibility(View.VISIBLE);
        if(isVisit()){
            ll_sign_tag.setVisibility(View.GONE);
        }

        tvAddress.setText(address);
        ll_state.setVisibility(View.GONE);
       if (!TextUtils.isEmpty(tag)){
            ll_state.setVisibility(View.VISIBLE);
        }
        CharSequence text = etRemark.getText();
        if (!TextUtils.isEmpty(text)) {
            etRemark.requestFocus();
            etRemark.setSelection(etRemark.getText().length());
        }
        if (!searchSing){
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
                           {

                               public void run()
                               {
                                   if (etRemark.getVisibility()==View.VISIBLE){
                                       InputMethodManager inputManager =
                                               (InputMethodManager)etRemark.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                       inputManager.showSoftInput(etRemark, 0);
                                   }

                               }

                           },
                    998);

        }

    }
    private boolean isVisit(){
        if(!TextUtils.isEmpty(tag)&&tag.equals("拜访")){
            id = getIntent().getStringExtra("id");
            if (!TextUtils.isEmpty(id)){
                btSubmit.setVisibility(View.VISIBLE);
                rl_btnsmit.setVisibility(View.VISIBLE);
                btSubmit.setText("查看/编辑拜访详情");
                btSubmit.setOnClickListener(this);
            }

           return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btSubmit:
               /* Intent intent = new Intent(SiginDetailActivity.this, VisitDetailActivity.class);
                intent.putExtra("id", id+"");
                startActivity(intent);
*/

                Intent  intent = new Intent(SiginDetailActivity.this, SelfVisitActivity.class);
                    intent.putExtra("mode",SelfVisitActivity.MODE_FROM_VIST_LIST_ITEM);

                intent.putExtra("id", id+"");

                startActivity(intent);






                break;
            case R.id.vAddress:
              /*  intent = new Intent(this,MapDetailActivity.class);
                intent.putExtra("latitude", 0);
                intent.putExtra("longitude", 0);
                intent.putExtra("address", address);
                startActivity(intent);*/
                break;
        }

    }

    public void upDate(String id,String des){
        showLoadingDialog();
        HashMap<String,String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("id",id);
        maps.put("remark",des);
        new HttpManager().post(this, "sign/signed/updateSigned", Result.class,
                maps,
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {

                        closeLoadingDialog();
                        finish();
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        closeLoadingDialog();
                    }
                }, false, 1);
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
