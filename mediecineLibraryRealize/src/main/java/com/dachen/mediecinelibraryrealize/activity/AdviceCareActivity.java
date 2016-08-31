package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
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
import com.dachen.mediecinelibraryrealize.adapter.AdviceAdapter;
import com.dachen.mediecinelibraryrealize.adapter.AdviceCareAdapter;
import com.dachen.mediecinelibraryrealize.entity.MedicineInfosList;
import com.dachen.mediecinelibraryrealize.entity.MedieCare;
import com.dachen.mediecinelibraryrealize.entity.SomeBox;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest;
import com.dachen.mediecinelibraryrealize.utils.StringUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class AdviceCareActivity extends BaseActivity  implements OnClickListener, OnHttpListener{
	ArrayList<patientSuggest> c_patient_drug_suggest_list ;
	AdviceCareAdapter adapter;
	ListView listview;
	RelativeLayout rl_back;
	TextView tv_title;
	boolean flagOpen;
	ViewStub stub;
	String patientid;
	View ll_alert;
	public static final String ADVICEMEDIEKEY = "advicemedikey";
	String list;
	TextView tv_footdes;
	String carePlanId= "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_care);
		 patientid = getIntent().getStringExtra("patient");
		c_patient_drug_suggest_list = new ArrayList<patientSuggest>();
		listview = (ListView) findViewById(R.id.listview);
		View viewfoot = View.inflate(this,R.layout.layout_advice,null);
		viewfoot.setVisibility(View.GONE);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		carePlanId = getIntent().getStringExtra("carePlanId");
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("用药关怀");

		 HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
		interfaces.put("carePlanId", carePlanId);
		list = getIntent().getStringExtra("from");

		HashMap<String, String> interfacess = new HashMap<String, String>();
		interfacess.put("interface1", "pack/carenew/findMedicalCare");
			showLoadingDialog();
		new HttpManager().get(this,
				interfacess,
				MedicineInfosList.class, interfaces, this, false, 3);
		View view = View.inflate(this,R.layout.layout_foot,null);
		listview.addFooterView(view);
		adapter = new AdviceCareAdapter(this,c_patient_drug_suggest_list,list);
		listview.setAdapter(adapter);
	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		if(null!=response&&response.resultCode==1){
			MedicineInfosList medieCare= (MedicineInfosList) response;
			c_patient_drug_suggest_list = getPatientStore(medieCare);
			 adapter = new AdviceCareAdapter(this,c_patient_drug_suggest_list,list);
			 listview.setAdapter(adapter);
		}else {
			ToastUtils.showResultToast(this, response);
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
		if (v.getId() == R.id.rl_back) {
			finish();
		}
	}
	public ArrayList<patientSuggest> getPatientStore(MedicineInfosList me){
		ArrayList<MedicineInfosList.MedicineInfo> lists = me.data;
		ArrayList<patientSuggest> plist = new ArrayList<>();
		if (lists!=null){
			for (int i=0;i<lists.size();i++){
				MedicineInfosList.MedicineInfo info = lists.get(i);
				SomeBox s= new SomeBox();
				patientSuggest ps = s.new patientSuggest();
				ps.general_name = info.generalName;
				ps.title = info.title;
				ps.manufacturer = info.manufacturer;
				ps.title = info.title;
				ps.drugid = info.goodsId;
				ps.manufacturer = info.manufacturer;
				ps.pack_specification = info.packSpecification;

				ps.dateSeq = info.dateSeq;
				ps.image = info.imageUrl;
				if (null!=info.totalQuantity){
					ps.requires_quantity = info.totalQuantity.quantity+"";
					ps.units  = info.totalQuantity.unit;
					ps.days = info.totalQuantity.days;

				}
				ps.takeMedicalTime = info.takeMedicalTime;
				//	ps.c_drug_usage_list ;
				ArrayList<patientSuggest.Uses> uses = new ArrayList<>();
				patientSuggest.Uses u = ps.new Uses();
				if (null!=info.usage){
					u.patients	 = info.usage.patients;
					u.period = info.usage.period;
					u.quantity = info.usage.quantity;
					u.times = info.usage.times+"";
					u.unit = info.usage.unit;
					u.periodUnit = info.usage.periodUnit;
					u.method = info.usage.remarks;
					u.periodNum = info.usage.periodNum;
					u.patients = info.usage.patients;
				}else {
					u.patients	 = "";
					u.period = null;
					u.quantity = "";
					u.times = "";
				}
				uses.add(u);
				ps.c_drug_usage_list = uses;
				plist.add(ps);
			}
		}

		return plist;
	}
}
