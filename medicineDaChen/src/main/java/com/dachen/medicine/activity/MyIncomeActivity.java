package com.dachen.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.incomelibrary.activity.BindBankCardActivity;
import com.dachen.incomelibrary.activity.SettlementActivity;
import com.dachen.incomelibrary.bean.AccountInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.MyIncomeData;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.CustomDialog;

import java.util.List;

/**
 * Created by Burt on 2016/1/23.
 */
public class MyIncomeActivity extends  BaseActivity implements View.OnClickListener,HttpManager.OnHttpListener {
    RelativeLayout rl_record;
    RelativeLayout rl_rule;
    LinearLayout ll_guize;
    TextView tv_title;
    RelativeLayout rl_back;
    RelativeLayout rl_waitaudit;
    LinearLayout ll_waitgive;
    RelativeLayout rl_allhavemoney;
    //待发放
    TextView tv_waitgive;
    //总收入
    TextView tv_allhavemoney;
    //待审核
    TextView tv_waitaudit;
    CustomDialog dialogNotBinding;

    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case ConstantsApp.HANDLER_GET_BANK_CARDS:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null && msg.obj instanceof List) {
                            List<AccountInfo> accountInfoList = (List<AccountInfo>) msg.obj;
                            if (accountInfoList!=null&&accountInfoList.size()==0){
                                isShowDialog();
                            }
                        }
                    }else{
                        ToastUtils.showToast(String.valueOf(msg.obj));
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myincome);
        ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.layout_sub_scantitletext, ll_sub);
        ll_guize = (LinearLayout) view.findViewById(R.id.ll_guize);
        TextView tv_guize = (TextView) view.findViewById(R.id.edit_title);
        tv_guize.setText("银行卡");
        ll_guize.setOnClickListener(this);
        rl_waitaudit = (RelativeLayout) findViewById(R.id.rl_waitaudit);
        rl_waitaudit.setOnClickListener(this);

        ll_waitgive = (LinearLayout) findViewById(R.id.ll_waitgive);
        ll_waitgive.setOnClickListener(this);
        //未发放 fee2
        tv_waitgive = (TextView) this.findViewById(R.id.tv_waitgive);
        //总收入fee3；
        tv_allhavemoney = (TextView) this.findViewById(R.id.tv_allhavemoney);
        //待审核 fee1;
        tv_waitaudit = (TextView) this.findViewById(R.id.tv_waitaudit);

        rl_allhavemoney = (RelativeLayout) findViewById(R.id.rl_allhavemoney);
        rl_allhavemoney.setOnClickListener(this);
        initView();
        getdata();
        HttpCommClient.getInstance().queryBindBankAccount(this, mHandler, ConstantsApp.HANDLER_GET_BANK_CARDS, SharedPreferenceUtil.getString("session", ""));
    }
    public void initView(){
        rl_record = (RelativeLayout) findViewById(R.id.rl_record);
        rl_rule = (RelativeLayout) findViewById(R.id.rl_rule);
        rl_record.setOnClickListener(this);
        rl_rule.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的收入");
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.rl_record){
            /*Intent intent = new Intent(this,MypopFeeRecordActivity.class);
            startActivity(intent);*/
            Intent intent = new Intent(this, MypopFeeRecordActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.rl_rule){
            Intent intent = new Intent(this, MypopFeeRoolerActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.ll_guize){
            //跳转到银行卡页面
//            ToastUtils.showToast("跳入银行卡界面");
            String id = SharedPreferenceUtil.getString("id", "");
            String userName = SharedPreferenceUtil.getString("username", "");
            String userType = SharedPreferenceUtil.getString("usertype", "");
            String session = SharedPreferenceUtil.getString("session", "");
            SettlementActivity.openUI(MyIncomeActivity.this,id,userName,userType,session);
           // isShowDialog();
        }else if(v.getId()==R.id.rl_back){
            finish();
        }else if(v.getId() == R.id.rl_waitaudit){
            //待审核金额(元)

            Intent intent = new Intent(this, MypopFeeActivity.class);
            intent.putExtra("selectpage","audit");
            startActivity(intent);
        }else if (v.getId() == R.id.ll_waitgive){
            //待发放
            Intent intent = new Intent(this, MypopFeeActivity.class);
            intent.putExtra("selectpage","waitgive");
            startActivity(intent);
        }else if (v.getId() == R.id.rl_allhavemoney){
            //总收入
            Intent intent = new Intent(this, MypopFeeActivity.class);
            intent.putExtra("selectpage","allhavemoney");
            startActivity(intent);
        }
    }//
    public void getdata(){
        new HttpManager().get(
                Params.getInterface("invoke", "c_tg_fee_store_user.get_total_tg_fee"),
                MyIncomeData.class,
                null,
                this, false, 2);
    }

    @Override
    public void onSuccess(Result response) {
        super.onSuccess(response);
        if (response instanceof MyIncomeData){
            MyIncomeData income = (MyIncomeData) response;
            if (null!=income.data){
                tv_waitgive = (TextView) this.findViewById(R.id.tv_waitgive);
                //总收入fee3；
                tv_allhavemoney = (TextView) this.findViewById(R.id.tv_allhavemoney);
                //待审核 fee1;
                tv_waitaudit = (TextView) this.findViewById(R.id.tv_waitaudit);
                tv_waitgive.setText(income.data.fee2);
                tv_allhavemoney.setText(income.data.fee3);
                tv_waitaudit.setText(income.data.fee1);
            }
        }
    }
    public void isShowDialog(){
        dialogNotBinding = new CustomDialog(this,"去绑定",null);
        dialogNotBinding.showDialog(null,
                "您还未绑定收款银行卡，为了不影响您的推广费发放，请尽快绑定银行卡", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = SharedPreferenceUtil.getString("id", "");
                        String userName = SharedPreferenceUtil.getString("username", "");
                        String userType = SharedPreferenceUtil.getString("usertype", "");
                        String session = SharedPreferenceUtil.getString("session", "");
                        BindBankCardActivity.openUI(MyIncomeActivity.this,id,userName,userType,session,"MyIncomeActivity");
                        dialogNotBinding.dimissDialog();
                    }
                },null);
    }

    @Override
    public void onBackPressed() {
        if(dialogNotBinding!=null){
            dialogNotBinding.dimissDialog();
        }
        super.onBackPressed();

    }
}
