package com.dachen.mediecinelibraryrealize.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientPoint;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints;
import com.dachen.mediecinelibraryrealize.entity.PointsGet;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints.Potient;

public class PatientPointsGetActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	ListView listview;
	AdapterPatientPoint  adaper;
	RelativeLayout rl_back;
	String id = "";
	List<Potient> patientpoints;
	TextView title;
	TextView tv_checkdetail;
	RelativeLayout rl_plus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patientpoints);
		listview = (ListView) findViewById(R.id.listview);

		patientpoints = new ArrayList<Potient>();
		adaper = new AdapterPatientPoint(this,patientpoints);
		id = getIntent().getStringExtra("id");
		title = (TextView) findViewById(R.id.tv_title);
		//getPointInfo(id);
		title.setText("我的积分");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_checkdetail = (TextView) findViewById(R.id.tv_checkdetail);
		tv_checkdetail.setOnClickListener(this);


		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_modi_time, ll_sub);
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
		TextView tv_save = (TextView) view.findViewById(R.id.tv_save);
		tv_save.setText("说明");
		rl_plus.setOnClickListener(this);


	}
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		if (null!=arg0&&arg0.resultCode==1) {
			PatientPoints  p = (PatientPoints) arg0;
			if (p.info_list!=null){
				patientpoints = p.info_list;
				adaper = new AdapterPatientPoint(this,patientpoints);
				listview.setAdapter(adaper);
			}else {
				ToastUtils.showResultToast(this,arg0);
			}

		}
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		}else if (v.getId() == R.id.tv_checkdetail) {
			Intent intent = new Intent(this,PointDetailActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}else if (v.getId() == R.id.rl_plus) {
			Intent intent = new Intent(this,PointExplain.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
	}
}
