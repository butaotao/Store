package com.dachen.mediecinelibraryrealize.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.mediecinelibraryrealize.R;

public class BaseActivity extends Activity {
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

	public void showLoadingDialog() {
		/*
		 * if (mLoadingView == null) { mLoadingView =
		 * LayoutInflater.from(this).inflate( R.layout.loading_view, null);
		 * ((ViewGroup) this.getWindow().getDecorView()).addView(mLoadingView);
		 * } final ImageView iv = (ImageView) mLoadingView
		 * .findViewById(R.id.iv_progress_bar);
		 * iv.setImageResource(R.drawable.xlv_loadmore_animation);
		 * mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
		 * mAnimationDrawable.setOneShot(false); if (mAnimationDrawable != null)
		 * { mAnimationDrawable.start(); }
		 * 
		 * mLoadingView.setVisibility(View.VISIBLE);
		 */
		if (null != mDialog) {
			mDialog.show();
		}
	}

	public void closeLoadingDialog() {
		/*
		 * if (mLoadingView != null) { mLoadingView.setVisibility(View.GONE); }
		 * if (mAnimationDrawable != null) { mAnimationDrawable.stop(); }
		 */
		if (null != mDialog) {
			mDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MActivityManager.getInstance().popActivity(this);
	}

	private void initProgressDialog() {
		mDialog = new ProgressDialog(this, R.style.IMDialog);
		// mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage("正在加载");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null!=MedicineApplication.getCallApplicationInterface()){
			MedicineApplication.getCallApplicationInterface().resumeIm();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null!=MedicineApplication.getCallApplicationInterface()) {
			MedicineApplication.getCallApplicationInterface().pauseIm();
		}
	}
}
