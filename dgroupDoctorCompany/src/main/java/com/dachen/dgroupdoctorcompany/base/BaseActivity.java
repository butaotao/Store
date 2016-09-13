package com.dachen.dgroupdoctorcompany.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.utils.SystemBarTintManager;
import com.dachen.imsdk.net.ImPolling;
import com.dachen.medicine.common.utils.MActivityManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Burt on 2016/2/18.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener{
    protected BaseActivity mThis=this;
    public ProgressDialog mDialog;
    protected View mLoadingView;
    public ImageView iv_back;
    TextView title;
    RelativeLayout rl_back;
    private AnimationDrawable mAnimationDrawable;
    public boolean isActive=true;
    boolean changeTitlebarColor;
    public RelativeLayout rl_titlebar;
    View line_titlebar;
    public void showLoadingDialog() {
        /*
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(this).inflate(
                    R.layout.loading_view, null);
            ((ViewGroup) this.getWindow().getDecorView()).addView(mLoadingView);
        }
        final ImageView iv = (ImageView) mLoadingView
                .findViewById(R.id.iv_progress_bar);
        iv.setImageResource(R.drawable.xlv_loadmore_animation);
        mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
        mAnimationDrawable.setOneShot(false);
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        }
        mLoadingView.setVisibility(View.VISIBLE);*/
        mDialog.show();
    }

    public void closeLoadingDialog() {
        /*
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }*/
        if (null!=mDialog&&mDialog.isShowing()){
            mDialog.dismiss();
        }

    }
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MActivityManager.getInstance().pushActivity(this);
        initProgressDialog();

    }

    private void initProgressDialog(){
        mDialog = new ProgressDialog(this, R.style.IMDialog);
        //		mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
        MActivityManager.getInstance().popActivity(this);
    }

    public void initView(){
        title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        if (null!=iv_back) {
            iv_back.setOnClickListener(this);
        }
        if (null!=rl_back){
            rl_back.setOnClickListener(this);
        }
    }
    public void setTitle(String titles){
        if (null!=title){
            title.setText(titles + "");
        }

    }
    public void setTitlecolor(int titleColor){
        if (null!=title){
            title.setTextColor(titleColor);
        }

    }
    public void enableBack(){
        if (null!=rl_back){
            rl_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if (null!=iv_back){
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    public void setBackInVisiable(){
        iv_back.setVisibility(View.INVISIBLE);
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_back:
                onBackPressed();
                break;
        }
    }
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isActive=true;

        MobclickAgent.onResume(this);
        ImPolling.getInstance().onResume();
    }
    public void setChangeTitlebarColor(boolean changeTitlebarColor){
        this.changeTitlebarColor = changeTitlebarColor;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isActive=false;
        MobclickAgent.onPause(this);
        ImPolling.getInstance().onPause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // this.mApplication.getActivityManager().finishActivity(this.getClass());
    }
    public void changerTitleBar(){
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        line_titlebar = findViewById(R.id.line_titlebar);
        if (rl_titlebar!=rl_titlebar){
            rl_titlebar.setBackgroundResource(R.color.color_3cbaff);
        }
        if (null!=line_titlebar){
            line_titlebar.setVisibility(View.GONE);
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setBackgroundResource(R.drawable.icon_back_n);
        setTitlecolor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setTextColor(Color.WHITE);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_3cbaff);


    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
