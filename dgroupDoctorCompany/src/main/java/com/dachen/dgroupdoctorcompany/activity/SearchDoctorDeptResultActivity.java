package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SearchDoctorAdapter;
import com.dachen.dgroupdoctorcompany.adapter.SearchDoctorDepAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.HospitalList;
import com.dachen.dgroupdoctorcompany.entity.SearchDoctorListEntity;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchDoctorDeptResultActivity extends BaseActivity implements OnClickListener,OnHttpListener {
	int totalNum;
	RelativeLayout rl_list;
	//ChoicedItemadapter choicedAdapter;
	ImageView iv_search;
	EditText et_search;
	int page = 1;
	ViewStub vstub_title;
	public String hospitId;
	SearchDoctorDepAdapter adapter;
	List<Doctor> doctors;
	List<Doctor> doctorsSelect;
	List<SearchDoctorListEntity.SearchDoctorInfo> depts;
	ListView listview;
	DoctorDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_doctordept);
		String name = getIntent().getStringExtra("hospitName");
		if (TextUtils.isEmpty(name)){
			setTitle("添加医生");
		}else {
			setTitle(name);
		}


		//	this.findViewById(R.id.rl_titlebar).setBackgroundResource(R.color.title_bg_color);
		listview = (ListView) findViewById(R.id.listview);
		doctors = new ArrayList<>();
		doctorsSelect = new ArrayList<>();
		dao = new DoctorDao(this);
		/*if (null!=dao.queryAllByUserid()){
			doctors = dao.queryAllByUserid();
		}*/
		depts = new ArrayList<>();
		getSearchResultWith("");
		adapter = new SearchDoctorDepAdapter(this,R.layout.adatper_searchdoctor_dept,depts);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				doctorsSelect.clear();
				SearchDoctorListEntity.SearchDoctorInfo infos = adapter.getItem(position);
				for (int i=0;i<infos.enterpriseUserList.size();i++){
					if (infos.enterpriseUserList.get(i).departments.equals(infos.departments)){
						doctorsSelect.add(infos.enterpriseUserList.get(i));
					}
				}
				Intent intent = new Intent(SearchDoctorDeptResultActivity.this, SearchDoctorResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("doctorsSelect", (Serializable) doctorsSelect);
				intent.putExtra("doctorsSelect",bundle);
				intent.putExtra("depart",infos.departments);
				startActivity(intent);
			}
		});
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		//this.findViewById(R.id.tv_title).setVisibility(View.GONE);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		//	RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
		//View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);

		//view.findViewById(R.id.tv_search).setOnClickListener(this);
		/*iv_search = (ImageView) view.findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);*/
		findViewById(R.id.rl_et).setVisibility(View.GONE);
		et_search = (EditText) this.findViewById(R.id.et_search);
		et_search.setOnClickListener(this);
		et_search.clearFocus();
		et_search.setInputType(InputType.TYPE_NULL);
		et_search.setOnClickListener(this);
		hospitId = getIntent().getStringExtra("hospitId");
		/*et_search.setOnEditorActionListener( new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event){
				LogUtils.burtLog("actionId===="+actionId);
				if (actionId == EditorInfo.IME_ACTION_DONE)
				{
					// 在这里编写自己想要实现的功能
					if(!TextUtils.isEmpty(et_search.getText())) {
						String searchText = et_search.getText().toString().trim();
						Intent intent = new Intent(SearchDoctorDeptResultActivity.this, SearchDoctorActivity.class);
						intent.putExtra("hospitId",hospitId);
						intent.putExtra("searchText",searchText);
						startActivity(intent);
					}else {
						ToastUtil.showToast(SearchDoctorDeptResultActivity.this,"请输入索搜关键字");
					}
				}
				return false;
			}
		});*/

		page = 1;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*et_search.requestFocus();

		et_search.setFocusable(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

						   public void run() {
							   InputMethodManager inputManager =
									   (InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							   inputManager.showSoftInput(et_search, 0);
						   }

					   },
				998);*/

	}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		if (response!=null&&response.resultCode==1){
			if (response instanceof SearchDoctorListEntity){
				doctors.clear();
				depts.clear();

				SearchDoctorListEntity searchDoctorList = (SearchDoctorListEntity) response;
				ArrayList<SearchDoctorListEntity.SearchDoctorInfo>  infos = searchDoctorList.data;
				if (null!=infos&&infos.size()>0){
					for (int i=0;i<infos.size();i++){
						if (null!=infos.get(i).enterpriseUserList&&infos.get(i).enterpriseUserList.size()>0){
							depts.add(infos.get(i));
							for (int j=0;j<infos.get(i).enterpriseUserList.size();j++){
								doctors.add(infos.get(i).enterpriseUserList.get(j));
							}
						}
					}
				}
				adapter.notifyDataSetChanged();
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
		super.onClick(v);
		if(v.getId() == R.id.et_search){
			//if(!TextUtils.isEmpty(et_search.getText())) {
			String searchText = et_search.getText().toString().trim();
			Intent intent = new Intent(this, SearchDoctorActivity.class);
			intent.putExtra("searchText","");
			intent.putExtra("hospitId",hospitId);
			startActivity(intent);
			//}else {
			//	ToastUtil.showToast(this,"请输入索搜关键字");
			//}
			//forSearch();
		}else if(v.getId() == R.id.tv_search){
			finish();
		}
	}

	public void forSearch(){
		if (!TextUtils.isEmpty(et_search.getText())) {
			String searchText = et_search.getText().toString().trim();
			getSearch(searchText);
		}
	}
	public void getSearch(String keyword){
		String searchWord =keyword;
		if(TextUtils.isEmpty(searchWord)){
			return;
		}
		getSearchResultWith(keyword);
	}
	public void getSearchResultWith(String keyword){

		HashMap<String ,String > maps = new HashMap<>();
		maps.put("access_token", UserInfo.getInstance(this).getSesstion());
		maps.put("hospitalId", hospitId);
		maps.put("keyword", keyword);
		new HttpManager().post(this, Constants.GETHOSPITAL, SearchDoctorListEntity.class,
				maps, this,
				false, 1);
		showLoadingDialog();
	}
	@Override
	public void onBackPressed() {
		finish();
		// super.onBackPressed();
		// this.mApplication.getActivityManager().finishActivity(this.getClass());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
	}

}
