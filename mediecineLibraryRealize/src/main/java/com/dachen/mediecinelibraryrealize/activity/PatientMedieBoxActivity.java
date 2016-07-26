package com.dachen.mediecinelibraryrealize.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientMedieBox;
import com.dachen.mediecinelibraryrealize.entity.PatientMedieBoxs;
import com.dachen.mediecinelibraryrealize.entity.PatientMedieBoxs.Info;
import com.dachen.mediecinelibraryrealizedoctor.activity.PreparedMedieActivity;
import com.dachen.mediecinelibraryrealizedoctor.utils.DataUtils;

public class PatientMedieBoxActivity extends BaseActivity  implements OnClickListener, OnHttpListener{
//    http://localhost:8080/web/api/invoke/b5cf0d22e10e4c0d90231a5120d876d8/c_Recipe.query?patient=1038
	//recipe/getRecipeListByPatientId  access_token patientId post
	ListView listview;
	List<Info> infos;
	AdapterPatientMedieBox adapter;
	TextView tv_title;
	RelativeLayout rl_back;
	String id;
	String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patientmediebox);
		listview = (ListView) findViewById(R.id.listview);
		View view = View.inflate(this, R.layout.layout_blank2,null); 
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		listview.addHeaderView(view);
		listview.addFooterView(view); 
		
	
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View views = vstub_title.inflate(this, R.layout.layout_plus_time, ll_sub);
		views.findViewById(R.id.rl_plus).setOnClickListener(this); 
		id = getIntent().getStringExtra("id");
	    name = getIntent().getStringExtra("name");
		tv_title.setText(name+"的药品清单");
		refreshData();
		infos = new ArrayList<Info>();
		adapter = new AdapterPatientMedieBox(this, infos,id);
	}
	public void refreshData(){
		showLoadingDialog();
		/* new HttpManager().get(this,
				Params.getInterface("invoke", "c_Recipe.query?__ORDER_BY__=state,created_time%20desc&"),
				PatientMedieBoxs.class,
				Params.getPatientInfoByID(id),
		 		this, false, 2);*/

	 	HashMap<String,String> maps = new HashMap<>();
		maps.put("access_token", UserInfo.getInstance(this).getSesstion());
		maps.put("patientId", id);//health
		new HttpManager().post(this,
				"org/recipe/getRecipeListByPatientId",
				PatientMedieBoxs.class,
				maps,
				this, false, 1);
	}
	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		if (null == response) {
			return;
		}else if (response instanceof PatientMedieBoxs){
			if (response.resultCode ==1){
				PatientMedieBoxs p = (PatientMedieBoxs) response;
				if (null!=p.info_list&&p.info_list.size()>0) {
					infos = p.info_list;
					//Collections.reverse(infos);
					adapter = new AdapterPatientMedieBox(this, infos,id);
					listview.setAdapter(adapter);
				}
			}else {
				ToastUtils.showResultToast(this,response);
			}

		}
	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
	 
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.rl_back) {
			onBackPressed();
		}else if (v.getId() == R.id.rl_plus) {
			DataUtils.cleanMediInfo(this);
			Intent intent = new Intent(this,PreparedMedieActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("id", id);
			startActivityForResult(intent,11);
		}
	}
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
	
			super.onActivityResult(requestCode, resultCode, data);
			refreshData();
			if (null!=data&&!TextUtils.isEmpty(data.getStringExtra("refresh"))) {
				refreshData();
			}
		}
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			Intent intent = new Intent();
			intent.putExtra("refresh", "refresh");
			intent.putExtra("size", infos.size());
			setResult(RESULT_OK,intent);
			finish();
		}
}
