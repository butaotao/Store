package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/18上午11:22.
 * @描述 TODO
 */
public class NetworkErrorView extends RelativeLayout implements View.OnClickListener {
    private RelativeLayout mRlNetworkerr;
    private ImageView mIvNetworkerrImg;
    private TextView mTvNetworkerrNote;

    private void assignViews() {
        mRlNetworkerr = (RelativeLayout) findViewById(R.id.rl_networkerr);
        mIvNetworkerrImg = (ImageView) findViewById(R.id.iv_networkerr_img);
        mTvNetworkerrNote = (TextView) findViewById(R.id.tv_networkerr_note);
    }


    private View mView;


    Context mContext;

    public NetworkErrorView(Context context) {
        super(context);
        initView(context);
    }

    public NetworkErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NetworkErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mView = View.inflate(mContext, R.layout.view_network_error, this);
        assignViews();
        mView.setOnClickListener(this);
    }

    public void setImg(int resId) {
        mIvNetworkerrImg.setImageResource(resId);
    }

    public void setNote(String note) {
        mTvNetworkerrNote.setText(note);
    }

    public void setBackgrandColor(int color) {
        mRlNetworkerr.setBackgroundColor(getResources().getColor(color));
    }

    public View getView() {
        return mView;
    }

    @Override
    public void onClick(View v) {
        // 跳转到系统的网络设置界面
        Intent intent = null;
        // 先判断当前系统版本
        if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            //intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WIFI_SETTINGS");
        }
        mContext.startActivity(intent);


    }
}
