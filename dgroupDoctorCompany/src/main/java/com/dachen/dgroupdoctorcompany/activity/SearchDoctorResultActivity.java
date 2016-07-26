package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SearchDoctorAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.SearchDoctorListEntity;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchDoctorResultActivity extends BaseActivity implements OnClickListener,OnHttpListener {
	int totalNum;
	RelativeLayout rl_list;
	//ChoicedItemadapter choicedAdapter;
	ImageView iv_search;
	//ClearEditText et_search;
	int page = 1;
	ViewStub vstub_title;
	public String hospitId;
	SearchDoctorAdapter adapter;
	List<Doctor> doctors;
	ListView listview;
	DoctorDao dao;
	List<BaseSearch> doctorsSelect ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_doctor);
		listview = (ListView) findViewById(R.id.listview);
		//this.findViewById(R.id.rl_titlebar).setBackgroundResource(R.color.title_bg_color);
		doctors = new ArrayList<>();
		dao = new DoctorDao(this);
		Bundle bundle = getIntent().getBundleExtra("doctorsSelect");
		String depart = getIntent().getStringExtra("depart");
		doctorsSelect = new ArrayList<>();
		if (null!=bundle){
			 doctorsSelect = (List<BaseSearch>) bundle.getSerializable("doctorsSelect");
		}
		setTitle(""+depart);
		//getSearchResultWithNull("");
		adapter = new SearchDoctorAdapter(this,R.layout.adapter_searchdoctor,doctorsSelect);
		listview.setAdapter(adapter);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		//this.findViewById(R.id.tv_title).setVisibility(View.GONE);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
		/*View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);

		view.findViewById(R.id.tv_search).setOnClickListener(this);
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);
		et_search = (ClearEditText) view.findViewById(R.id.et_search);

		et_search.setOnClickListener(this);*/
		hospitId = getIntent().getStringExtra("hospitId");
		/*et_search.setOnEditorActionListener( new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event){
				LogUtils.burtLog("actionId===="+actionId);
				if (actionId == EditorInfo.IME_ACTION_DONE)
				{
					// 在这里编写自己想要实现的功能
					forSearch();
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
				SearchDoctorListEntity searchDoctorList = (SearchDoctorListEntity) response;
				ArrayList<SearchDoctorListEntity.SearchDoctorInfo>  infos = searchDoctorList.data;
				if (null!=infos&&infos.size()>0){
					for (int i=0;i<infos.size();i++){
						if (null!=infos.get(i).enterpriseUserList&&infos.get(i).enterpriseUserList.size()>0){
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
		if(v.getId() == R.id.iv_search){
			forSearch();
		}else if(v.getId() == R.id.tv_search){
			finish();
		}
	}

	public void forSearch(){
		/*if (!TextUtils.isEmpty(et_search.getText())) {
			String searchText = et_search.getText().toString().trim();
			getSearchResult(searchText);
		}*/
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
