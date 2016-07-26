package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.PreparedMedieAdapters;
import com.dachen.mediecinelibraryrealizedoctor.entity.CustomDialog;
import com.dachen.mediecinelibraryrealizedoctor.entity.DoctorCollectionDataLists;
import com.dachen.mediecinelibraryrealizedoctor.entity.PatientCollectionDataLists;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;
import com.dachen.mediecinelibraryrealizedoctor.utils.DataUtils;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.CollectionsDtaChange;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DoctorCollectionsDtaChange;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DrugChange;

public class PreparedMedieActivity extends BaseSearchActivity implements OnHttpListener,OnClickListener,CleanAllRefreshinferface{
	PreparedMedieAdapters adapter;
	List<MedicineInfo> lists;
	List<MedicineInfo> lists_record;
	List<MedicineInfo> lists_ord;
	ListView listview;
	TextView tv_title;
	EditText et_search;
	RelativeLayout rl_classes;
	RelativeLayout rl_near;
	int clickPosition = 0 ;
	Button btn_add;
	Button btn_save;
	public String groupID;
	TextView tv_mymedie;
	boolean isChoice = false;
	boolean choiceMedicActivity = false;
	boolean startActivity = true;
	ArrayList<MedicineInfo> result2 = new ArrayList<MedicineInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preparedmedie);

	}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		showLoadingDialog();
		initdata();
		getInterface();
		getBuyCarData(true,false);
		String name = "" ;
		String id  ="";
		groupID ="";
		if (null!= getIntent().getStringExtra("doctorGroupid")) {
			name = UserInfo.getInstance(this).getUserName();
			id =  UserInfo.getInstance(this).getId();
			groupID = getIntent().getStringExtra("doctorGroupid");
		}else {
			name = getIntent().getStringExtra("name");
			id = getIntent().getStringExtra("id");
		}


		SharedPreferenceUtil.putString(this,"createmeidelist_username", name);
		SharedPreferenceUtil.putString(this, "createmeidelist_userid", id);
		listview = (ListView) findViewById(R.id.listview);
		findViewById(R.id.rl_back).setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("选择药品");
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setOnClickListener(this);
		et_search.clearFocus();
		et_search.setInputType(InputType.TYPE_NULL);
		tv_mymedie = (TextView) findViewById(R.id.tv_mymedie);
		if (UserInfo.getInstance(this).getUserType().equals("3")) {
			tv_mymedie.setText("常用药品库");
		}else {
			tv_mymedie.setText("我的常备药");
		}
		rl_classes = (RelativeLayout) findViewById(R.id.rl_classes);
		rl_classes.setOnClickListener(this);
		findViewById(R.id.rl_near).setOnClickListener(this);

		lists = new ArrayList<MedicineInfo>();
		adapter = new PreparedMedieAdapters(this, lists, choice,2);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				clickPosition = arg2;
				MedicineInfo media = (MedicineInfo) adapter.getItem(arg2);
				// TODO Auto-generated method stub
				Intent intent = new Intent(PreparedMedieActivity.this, MedicienSearchListActivity.class);
				if (!TextUtils.isEmpty(media.trade_name)) {
					intent.putExtra("trade_name", media.trade_name);
					intent.putExtra("id", media.id);
					//context.startActivity(intent);
					startActivityForResult(intent, 2);
				}

			}
		});
		startActivity = false;
		getInfo();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (null!=data) {
			Bundle bundle = data.getBundleExtra("choice");
			if (null!=bundle) {
				MedicineInfo entity =  (MedicineInfo) bundle.get("choice");
				lists.set(clickPosition, entity);
				adapter.isclick = 2;
				adapter.notifyDataSetChanged();
			}
		}
		if (requestCode==3||requestCode==4||requestCode==5) {
			getBuyCarData(true,false);
		}
		isChoice = true;
	}
	//http://192.168.3.7:9002/web/api/invoke/bf9a9f083f6348608a5791fcf90fa5c5/
	//c_Goods.select?trade_name=%E6%84%9F%E5%86%92%E7%81%B5

	public void getInfo(){
		if(!isChoice){
			showLoadingDialog();
			String s = "";
			HashMap<String, String> maps = new HashMap<String, String>();
			maps.put("access_token",UserInfo.getInstance(this).getSesstion());
			maps.put("userId", UserInfo.getInstance(this).getId()); // c_doctor_goods_CB.select

			//c_doctor_goods_CB.select
			if (UserInfo.getInstance(this).getUserType().equals("3")) {
				// ToastUtils.showToast("=="+UserInfo.getInstance(this).getUserType());
				s = "org/drugCollection/getDrugCollectionDetailListByUserId";
				new HttpManager().post (this,
						s,
						DoctorCollectionDataLists.class,
						maps,
						this,false, 1);
			}else {
				s = "org/drugCollection/getDrugCollectionListByUserId";
				new HttpManager().post (this,
						s,
						PatientCollectionDataLists.class,
						maps,
						this,false, 1);
			}


		}
		isChoice = false;

	}
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (UserInfo.getInstance(this).getUserType().equals("3")&&!startActivity){
			isChoice = false;
			getInfo();
		}
		if (UserInfo.getInstance(this).getUserType().equals("1")){
			closeLoadingDialog();
		}



	}

	@Override
	public void onSuccess(Result arg0) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		ArrayList<MedicineInfo> infos = null;
		if (arg0.resultCode ==1 ){
			if (arg0 instanceof DoctorCollectionDataLists){
				DoctorCollectionDataLists entity = (DoctorCollectionDataLists)arg0;
				infos = DoctorCollectionsDtaChange.getMedicineList(entity.data);
			}
			if (arg0 instanceof PatientCollectionDataLists) {
				PatientCollectionDataLists entity = (PatientCollectionDataLists) arg0;
				infos = CollectionsDtaChange.getCollections(entity.data);
			}
			if (infos==null){
				infos = new ArrayList<>();
			}

			if (null!=infos&&infos.size()>0) {

				result2.clear();
				lists.clear();


				result2 = infos;
				lists.addAll(infos);
				lists_ord = new ArrayList<MedicineInfo>(result2);

				refreshData();

			} else {
				lists.clear();
			}

			if(null==lists||lists.size()==0){
				tv_mymedie.setVisibility(View.GONE);
			}else {
				tv_mymedie.setVisibility(View.VISIBLE);
			}
			adapter.notifyDataSetChanged();
		}else {
			ToastUtils.showResultToast(this,arg0);
		}

	}
	public void refreshData(){

		ArrayList<MedicineInfo>  infos = DataUtils.getList(true, this, (ArrayList<MedicineInfo>) lists_ord,
				(ArrayList<MedicineInfo>) lists);
		lists.clear();
		lists.addAll(infos);
		if(null==lists||lists.size()==0){
			tv_mymedie.setVisibility(View.GONE);
		}else {
			tv_mymedie.setVisibility(View.VISIBLE);
		}
		if (UserInfo.getInstance(this).getUserType().equals("3")) {
			tv_mymedie.setText("常用药品库(" + lists.size() + ")");
		}else {
			tv_mymedie.setText("我的常备药(" + lists.size() + ")");
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v.getId()==R.id.rl_back) {
			dialogBack();
		}else if(v.getId() == R.id.et_search){
			Intent intent = new Intent(this,SearchActivity.class);
			startActivityForResult(intent,3);
		} else if (v.getId() == R.id.rl_classes) {
			choiceMedicActivity = true;
			showLoadingDialog();
			Intent intent = new Intent(this,ChoiceMedicineActivity.class);
			intent.putExtra("doctorGroupid",  groupID);
			startActivityForResult(intent,4);
		}else if (v.getId() == R.id.rl_near) {
			Intent intent = new Intent(this,CollectChoiceMedieActivity.class);
			startActivityForResult(intent,5);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (choiceMedicActivity){
			choiceMedicActivity = false;
			closeLoadingDialog();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		dialogBack();
	}
	public void dialogBack(){

		final CustomDialog dialog = new CustomDialog(this);

		List<MedicineInfo> info = MedicineList.getMedicineList().getShopingcard(PreparedMedieActivity.this);
		if (info==null||info.size()==0) {
			ClenAll(true);
			closeLoadingDialog();
			MActivityManager.getInstance().finishAllActivityMedie();
			return;
		}

		dialog.showDialog("", "您是否要提交已选择的药品清单？", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<MedicineInfo> info = MedicineList.getMedicineList().getShopingcard(PreparedMedieActivity.this);
				final List<MedicineInfo> infos = new ArrayList<>();
				for (int i=0;i<info.size();i++){
					try {
						infos.add((MedicineInfo) info.get(i).deepCopy());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				MedicineList.getMedicineList().setSaveCollectiongWhenFinish(PreparedMedieActivity.this,infos);
				dialog.dimissDialog();
				commitData();
				MActivityManager.getInstance().finishAllActivityMedie();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dimissDialog();
				MActivityManager.getInstance().finishAllActivityMedie();
			}
		});
	}
	@Override
	public void refreshClear(boolean clean) {
		// TODO Auto-generated method stub
		if (clean){
			if (null!=lists_ord) {
				lists.clear();
				lists.addAll(lists_ord);
			}
			for (int i = 0; i < lists.size(); i++) {
				MedicineInfo info = new MedicineInfo();
				try {
					info = (MedicineInfo) lists.get(i).deepCopy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				info.num =0;
				lists.set(i, info);

			}
			adapter.setNotfiDataSetChanged2(lists);
		}else {
			refreshData();
		}
	}
	@Override
	public CleanAllRefreshinferface getInterface() {
		// TODO Auto-generated method stub
		return this;
	}

}
