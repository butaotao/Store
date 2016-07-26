package com.dachen.mediecinelibraryrealizedoctor.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.config.UserInfo;
import com.dachen.mediecinelibraryrealizedoctor.R;

public abstract class BaseActivity extends Activity{
	protected View mLoadingView;
	private AnimationDrawable mAnimationDrawable;
	public ProgressDialog mDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	MActivityManager.getInstance().pushActivity(this);
	initProgressDialog();
}
@Override
public void setContentView(int layoutResID) {
	// TODO Auto-generated method stub
	super.setContentView(layoutResID);
	RelativeLayout titlebar = (RelativeLayout) this.findViewById(R.id.rl_titlebars);

	if (null!=titlebar) {
		String type = UserInfo.getInstance(this).getUserType();
		if (type.equals("1")) {
			//30b2cc
			titlebar.setBackgroundColor(getResources().getColor(R.color.color_23BA91));
		}else if (type.equals("3")) {
			titlebar.setBackgroundColor(getResources().getColor(R.color.color_5578BA));
		}else if (type.equals("")) {

		}
	}else {
		String type = UserInfo.getInstance(this).getUserType();

	}

}
public void showLoadingDialog() {
	 /*if (mLoadingView == null) {
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

	mLoadingView.setVisibility(View.VISIBLE); */
	if (null!=mDialog) {
		mDialog.show();
	}

}

public void closeLoadingDialog() {
	/*if (mLoadingView != null) {
		mLoadingView.setVisibility(View.GONE);
	}
	if (mAnimationDrawable != null) {
		mAnimationDrawable.stop();
	}*/
	if (null!=mDialog) {
		mDialog.dismiss();
	}

}

private void initProgressDialog(){
	mDialog = new ProgressDialog(this, R.style.IMDialog);
	//		mDialog.setCancelable(true);
	mDialog.setCanceledOnTouchOutside(false);
	mDialog.setMessage("正在加载");
}
}
