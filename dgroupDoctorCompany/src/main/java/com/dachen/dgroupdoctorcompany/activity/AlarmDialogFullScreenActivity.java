package com.dachen.dgroupdoctorcompany.activity;

/**
 * Created by Burt on 2016/8/6.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.common.media.SoundPlayer;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.receiver.LocationReceiver;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 锁屏状态下显示的对话框
 *
 * @author gaozhuo
 * @date 2015年12月14日
 */
public class AlarmDialogFullScreenActivity extends BaseActivity   {

    @Bind(R.id.content)
    TextView mContent;

    private Reminder mAlarm;
    SoundPlayer player ;
    LocationReceiver receiver;
    boolean startSign;
    long latitude;
    long longitude;
    String address;
    long nowtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        player = new SoundPlayer(this);
        nowtime = getIntent().getLongExtra("nowtime",0);
        startSign = false;
        // 当处于锁屏状态时，允许此界面出现在锁屏界面之上，即不用解锁也能显示此界面
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        // 设置点击对话框区域外不关闭对话框
        setFinishOnTouchOutside(false);

        initData(getIntent());
        receiver = new LocationReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                latitude = intent.getLongExtra("latitude", 0L);
                longitude = intent.getLongExtra("longitude", 0L);
                address = intent.getStringExtra("address");
                startSign = SignInActivity.compareDistance(longitude,latitude,context);
            }
        };

    }

    protected int getLayoutResId() {
        return R.layout.activity_alarm_dialog_fullscreen;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        initData(getIntent());

    }



    private void initData(Intent intent) {
        if (intent == null) {
            return;
        }
        mAlarm = (Reminder) intent.getSerializableExtra("alarm");
    }

    @OnClick(R.id.left_btn)//跳过
    public void onLeftBtnClick(View v) {
        // 默认就是稍后提醒的，所以什么都不做
        player.stop();

        close();
    }

    @OnClick(R.id.right_btn)//服用
    public void onRightBtnClick(View v) {
        if (startSign){
            Intent intent = new Intent(this, AddSignInActivity.class);

            intent.putExtra("name", SignInActivity.address);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            intent.putExtra("mode", AddSignInActivity.MODE_WORKING);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, SelectAddressActivity.class);
            intent.putExtra("alarm", mAlarm);
            intent.putExtra("nowtime",nowtime);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        finish();
    }

    private void close() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}