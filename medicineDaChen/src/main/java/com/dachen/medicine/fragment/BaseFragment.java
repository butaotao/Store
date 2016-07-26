package com.dachen.medicine.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;

public class BaseFragment extends Fragment implements OnHttpListener {
	protected Activity mActivity;
	protected View mLoadingView;
	private AnimationDrawable mAnimationDrawable;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
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
		if (mLoadingView == null) {
			mLoadingView = LayoutInflater.from(mActivity).inflate(
					R.layout.loading_view, null);
			((ViewGroup) mActivity.getWindow().getDecorView()).addView(mLoadingView);
		}
		final ImageView iv = (ImageView) mLoadingView
				.findViewById(R.id.iv_progress_bar);
		iv.setImageResource(R.drawable.xlv_loadmore_animation);
		mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
		mAnimationDrawable.setOneShot(false);
		if (mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
		mLoadingView.setVisibility(View.VISIBLE);
	}

	public void closeLoadingDialog() {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.GONE);
		}
		if (mAnimationDrawable != null) {
			mAnimationDrawable.stop();
		}
	}
}
