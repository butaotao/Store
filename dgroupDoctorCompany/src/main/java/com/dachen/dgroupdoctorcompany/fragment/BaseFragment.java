package com.dachen.dgroupdoctorcompany.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;

import java.util.ArrayList;

public class BaseFragment extends Fragment implements OnHttpListener {
	protected Activity mActivity;
	protected View mLoadingView;
	public ProgressDialog mDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		initProgressDialog();
	}

	private void initProgressDialog(){
		mDialog = new ProgressDialog(mActivity, R.style.IMDialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage("正在加载");
	}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		
	}
	public void showLoadingDialog() {
//		if (mLoadingView == null) {
//			mLoadingView = LayoutInflater.from(mActivity).inflate(
//					R.layout.loading_view, null);
//			((ViewGroup) mActivity.getWindow().getDecorView()).addView(mLoadingView);
//		}
//		final ImageView iv = (ImageView) mLoadingView
//				.findViewById(R.id.iv_progress_bar);
//		iv.setImageResource(R.drawable.xlv_loadmore_animation);
//		mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
//		mAnimationDrawable.setOneShot(false);
//		if (mAnimationDrawable != null) {
//			mAnimationDrawable.start();
//		}
//		mLoadingView.setVisibility(View.VISIBLE);
		if(mDialog != null){
			mDialog.show();
		}
	}

	public void closeLoadingDialog() {
//		if (mLoadingView != null) {
//			mLoadingView.setVisibility(View.GONE);
//		}
//		if (mAnimationDrawable != null) {
//			mAnimationDrawable.stop();
//		}
		if(mDialog != null){
			mDialog.dismiss();
		}
	}
}
