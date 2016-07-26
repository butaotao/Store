package com.dachen.medicine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.MipcaActivityCapture;
import com.dachen.medicine.activity.MipcaActivityCaptures;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class QRCodeFragment extends BaseFragment {
	// 总View，总视图
	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = LayoutInflater.from(mActivity).inflate(
				R.layout.activity_capture, null);
		Intent intent = new Intent(mActivity, MipcaActivityCaptures.class);
		startActivity(intent);
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initView();
	
		
	}
}