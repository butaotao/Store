package com.dachen.medicine.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.dachen.medicine.R;

/**
 * 
 * @描述： @自定义加载进度框
 * @作者： @蒋诗朋
 * @创建时间： @2014-11-27
 */

public class CustomLoadingDialog {
	private LayoutInflater mInflater;
	private Context mContext;
	private View mView;
	private Dialog mDialog;
	private ImageView mIvLoadingView;
	private AnimationDrawable mAnimationDrawable;

	public CustomLoadingDialog(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		mView = mInflater.inflate(R.layout.layout_loading_dialog, null);
		initDialog();
	}

	private void initDialog() {
		mDialog = new Dialog(mContext, R.style.dialog_style);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.translate);
		mDialog.setContentView(mView);
		mDialog.getWindow().setGravity(Gravity.CENTER);
	}

	public final void showDialog() {
		mIvLoadingView = (ImageView) mView.findViewById(R.id.iv_progress_bar);
		mIvLoadingView.setImageResource(R.drawable.xlv_loadmore_animation);
		mAnimationDrawable = (AnimationDrawable) mIvLoadingView.getDrawable();
		mAnimationDrawable.setOneShot(false);
		if (mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
		mDialog.show();
	}

	public final void dimissDialog() {
		try {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mAnimationDrawable != null) {
			mAnimationDrawable.stop();
			mIvLoadingView.setImageDrawable(null);
		}
	}
}
