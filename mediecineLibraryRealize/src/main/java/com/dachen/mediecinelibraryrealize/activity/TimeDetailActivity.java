package com.dachen.mediecinelibraryrealize.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterTimeDetail;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;

public class TimeDetailActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	List<AlarmsTimeInfo> infos;
	ListView listview;
	AdapterTimeDetail adapter;
	TextView tv_title;
	RelativeLayout rl_back;
	TextView tv_data;
	int position;
	TextView tv_time;
	public String Patientid;
	RelativeLayout rl_nocontent;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_detail); 
		Bundle bundle = getIntent().getBundleExtra("times");
		if (null!= bundle) {
			 infos= (List<AlarmsTimeInfo>) bundle.get("times");
		}else {
			infos = new ArrayList<AlarmsTimeInfo>();
		}
		rl_nocontent = (RelativeLayout) findViewById(R.id.rl_nocontent);
		position = getIntent().getIntExtra("position", 0);
		String period = getIntent().getStringExtra("period");
		String pe = getIntent().getStringExtra("pe");
		Patientid = getIntent().getStringExtra("Patientid");
		LogUtils.burtLog("infos.size()"+infos.size());
		listview = (ListView) findViewById(R.id.listview); 
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(period+"药物"); 
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		if (infos==null||infos.size()==0){
			rl_nocontent.setVisibility(View.VISIBLE);
		}else {
			rl_nocontent.setVisibility(View.GONE);
		}
		adapter = new AdapterTimeDetail(this, infos,Patientid);
		listview.setAdapter(adapter);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_data.setText(TimeUtils.getTimeDay2()+"  周"+TimeUtils.parseDate()[3]);
		tv_time.setText(pe+" 您按时吃药了吗？");
	} 
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {

			onBackPressed();

		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MActivityManager.getInstance().finishActivity(PatientMedieBoxActivity2.class);
		Intent intent = new Intent(this,PatientMedieBoxActivity2.class);
		intent.putExtra("position", position);
		startActivity(intent);
		overridePendingTransition(R.anim.i_slide_out_right, R.anim.i_slide_in_right);
		finish();
	}

}
