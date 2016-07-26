package com.dachen.dgroupdoctorcompany.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class WaitVerify extends BaseFragment implements View.OnClickListener{
	// 总View，总视图
	private View mRootView;
	TextView tv_login_title;
	EditText et_search;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = LayoutInflater.from(mActivity).inflate(
				R.layout.activity_waitverify, null);

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initView();
	
		
	}

	@Override
	public void onClick(View v) {

	}
}