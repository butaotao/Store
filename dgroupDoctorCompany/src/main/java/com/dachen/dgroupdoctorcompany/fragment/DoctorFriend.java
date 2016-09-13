package com.dachen.dgroupdoctorcompany.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.DoctorDetailActivity;
import com.dachen.medicine.common.utils.ToastUtils;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class DoctorFriend extends BaseFragment implements View.OnClickListener{
	// 总View，总视图
	private View mRootView;
	TextView tv_login_title;
	EditText et_search;
	TextView tv_doctorfrend;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = LayoutInflater.from(mActivity).inflate(
				R.layout.activity_doctorfriend, null);
		tv_doctorfrend = (TextView) mRootView.findViewById(R.id.tv_doctorfrend);
		tv_doctorfrend.setOnClickListener(this);
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
		if (v.getId()==R.id.tv_doctorfrend){
			Intent intent = new Intent(mActivity,DoctorDetailActivity.class);
			startActivity(intent);
		}
	}
}