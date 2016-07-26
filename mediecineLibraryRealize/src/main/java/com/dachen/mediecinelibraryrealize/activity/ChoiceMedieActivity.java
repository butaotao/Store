package com.dachen.mediecinelibraryrealize.activity;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
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
import com.dachen.mediecinelibraryrealize.adapter.ChoiceMedieAdapter;
import com.dachen.mediecinelibraryrealize.entity.ChoiceMedieEntity;
import com.dachen.mediecinelibraryrealize.entity.ChoiceMedieEntity.MedieEntity;
import com.dachen.mediecinelibraryrealize.utils.JsonUtils.ChoiceMedieForAlarm;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DrugChange;

public class ChoiceMedieActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	ListView listview;
	String Patientid;
	public List<MedieEntity> listdata;
	ChoiceMedieAdapter adapter;
	RelativeLayout rl_plus;
	RelativeLayout rl_back;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choicemedie);
		listview = (ListView) findViewById(R.id.listview);
		Patientid = getIntent().getStringExtra("Patientid"); 
		listdata = new ArrayList<MedieEntity>();
		adapter = new ChoiceMedieAdapter(this,listdata,change);
		listview.setAdapter(adapter);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_modi_time, ll_sub);
		TextView tv = (TextView) view.findViewById(R.id.tv_save);
		tv.setText("确定");
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText("选择药品");
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus); 
		rl_plus.setOnClickListener(this);
		
		 
		getChoiceMedieInfo();
	}
	ChangeData change = new ChangeData() {
		 
		@Override
		public void change(List<MedieEntity> listdatas) {
			// TODO Auto-generated method stub
			listdata = listdatas;
			adapter.notifyDataSetChanged();
		}
	};
	void getChoiceMedieInfo(){
		String s = "org/recipe/getRecipeGoodsListByPatientId";
		HashMap<String, String> interfaces = new HashMap<String, String>();
		//患者
		interfaces.put("patientId", Patientid);
		interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
		new HttpManager().get (this, s,
				DrugDtaList.class,
				interfaces,
		 		this,false, 1);
	}
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
			if (arg0.resultCode == 1){


				if (arg0 instanceof DrugDtaList) {
					DrugDtaList result = (DrugDtaList) arg0;
					List<ChoiceMedieEntity.MedieEntity> set_datas = ChoiceMedieForAlarm.getMedicineList(result.data);
				if (set_datas.size()>0) {
					listdata = set_datas;
					adapter = new ChoiceMedieAdapter(this,listdata,change);
					listview.setAdapter(adapter);
				}
			}else {
				ToastUtils.showResultToast(this, arg0);
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
		if (v.getId() == R.id.rl_plus) {
			if (null!=listdata) {
				
			for (int i = 0; i < listdata.size(); i++) { 
				if (listdata.get(i).select) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("choice", (Serializable)listdata.get(i));
					intent.putExtra("choice", bundle);
					setResult(RESULT_OK, intent);
					break;
				}
			 }
			} 
			finish();
		}else if (v.getId() == R.id.rl_back){
			finish();
		}
		
	}
	public interface ChangeData{
		public void change(List<MedieEntity> listdata);
	}
}
