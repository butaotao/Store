package com.dachen.mediecinelibraryrealizedoctor.activity;




import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.IllEntity;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoiceMedicineChildrenadapter;
import com.dachen.mediecinelibraryrealizedoctor.db.IllEntityDao;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.IllTree;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;
import com.dachen.mediecinelibraryrealizedoctor.entity.SharePrefrenceConst;
import com.dachen.mediecinelibraryrealizedoctor.utils.DataUtils;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DrugChange;

public class ChoiceMedicineActivity extends BaseSearchActivity implements
		OnHttpListener,OnClickListener,CleanAllRefreshinferface {
	MedicineEntity medicineEntity;
	ArrayList<IllEntity> original_parents;
	ListView listview_medicine_parents;
	IllEntity entity;
	TextView tv_patient;
	String title = "病种";
	RelativeLayout rl_back;
	boolean haveImage;
	boolean isLoading;
	TextView tv_title;
	ViewStub vstub_title;
	RelativeLayout ll_sub;
	RelativeLayout rl_search;
	View view ;
	List<IllEntity> listsTitle;
	ArrayList<MedicineInfo> lists_childrens;
	ArrayList<MedicineInfo> lists_childrensOrigain;
	ArrayList<MedicineInfo> list2;
	public static String groupID;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					title = "病种";
					if (listsTitle.size() > 0) {
						title = title + ">";
						for (int i = 0; i < listsTitle.size(); i++) {
							if (i != listsTitle.size() - 1) {
								title += listsTitle.get(i).getName() + ">";
							} else {
								title += listsTitle.get(i).getName();
							}
						}
						rl_search.setVisibility(View.GONE);
					}else {
						rl_search.setVisibility(View.VISIBLE);
					}
					tv_patient.setText(title);
					break;
				case 1:
					if (null==childrenAdapter){
						childrenAdapter = new ChoiceMedicineChildrenadapter(ChoiceMedicineActivity.this,
								R.layout.item_listview_choice_medicine_children,
								lists_childrens, lists_parents, choice);
						listview_medicine_children .setAdapter(childrenAdapter);
					}else {
						childrenAdapter.notifyDataSetChanged();
					}

					closeLoadingDialog();
					break;
				default:
					break;
			}

		};
	};
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);

		lists_childrensOrigain = new ArrayList<MedicineInfo>();
		initdata();
		getBuyCarData(true, false);
		getInterface();
		lists_childrens = new ArrayList<MedicineInfo>();
		list2 = new ArrayList<MedicineInfo>();
		groupID = getIntent().getStringExtra("doctorGroupid");
		if (TextUtils.isEmpty(groupID)) {
			groupID = "";
		}
		String time = SharedPreferenceUtil.getString(this,SharePrefrenceConst.SHARE_SAVE, "");
		if (TextUtils.isEmpty(time)||time.equals("save")){
			ChoiceMedicineLogic.saveObject(this, "", SharePrefrenceConst.MEDICINE);
		}
		listsTitle = new ArrayList<>();
		ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		view = vstub_title.inflate(this, R.layout.layout_search, ll_sub);
		this.findViewById(R.id.ll_layout).setBackgroundResource(R.color.translate);
		listview_medicine_children = (ListView) findViewById(R.id.listview_medicine_children);
		listview_medicine_parents = (ListView) findViewById(R.id.listview_medicine_parents);
        tv_patient = (TextView) findViewById(R.id.tv_patient);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("选择药品");
		tv_title.setOnClickListener(this);
		tv_patient.setText(title);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		rl_search = (RelativeLayout) view.findViewById(R.id.rl_search);
		rl_search.setOnClickListener(this);
		original_parents = new ArrayList<IllEntity>();
		getIllinfo();
		getMedieList("0");
		// getBuyCarData(true,true);
		listview_medicine_children
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
						//	LogUtils.burtLog("childrenAdapter.getItem(arg2)=="+childrenAdapter.getItem(arg2));
						if (childrenAdapter.getItem(arg2) instanceof IllEntity) {
							IllEntity en = (IllEntity) childrenAdapter.getItem(arg2);
							entity = en;
							boolean flag = false;
							for (int i = 0; i < listsTitle.size(); i++) {
								if (listsTitle.get(i).getName().contains(entity.getName())
										||entity.getParent().equals(listsTitle.get(i).getParent())) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								listsTitle.add(entity);
							}
							handler.sendEmptyMessage(0);
							if (null != entity.getChildren()
									&& entity.getChildren().size() > 0) {
								getParents(null, entity.getChildren());
								/*childrenAdapter = new ChoiceMedicineChildrenadapter(
										ChoiceMedicineActivity.this,
										R.layout.item_listview_choice_medicine_children,
										lists_childrens, lists_parents,
										choice);
								listview_medicine_children.setAdapter(childrenAdapter);*/
								handler.sendEmptyMessage(1);
							}else {
								haveImage = true;
							}
							if (entity==null){
								getMedieList("0");
							}else {
								getMedieList(entity.getId());
							}

						}else if (childrenAdapter.getItem(arg2) instanceof MedicineInfo) {

						}
					}
				});

	}
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_choicemedicine);
		showLoadingDialog();

	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	/*public void getMedieList(IllEntity entitys){
		if (null == entitys || null ==entitys.getId()) {
			return;
		}
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("interface1", "invoke");
		interfaces.put("interface2", Constants.GOOD_SELECT);

		String medieinfo ="";
		boolean isNull = false;
		try {
			if (!TextUtils.isEmpty(medieinfo)) {
				if ( medieinfo.equals("0")) {
					isNull = true;
				}else {
					isNull = false;
				}
			}
			if (!isNull) {
				if(!isLoading){
					if (null!=entitys) {
						showLoadingDialog();
						new HttpManager().get(ChoiceMedicineActivity.this,interfaces, MedicineEntity.class,
								Params.getGoodsInfo(groupID,entitys.getId(), "1"),
								ChoiceMedicineActivity.this,false, 2);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public void getMedieList(String id){


					if (!TextUtils.isEmpty(id)) {

						HashMap<String,String> maps = new HashMap<>();
						maps.put("access_token", UserInfo.getInstance(this).getSesstion());
						maps.put("diseaseType",id);
						if (!TextUtils.isEmpty(groupID)){
							maps.put("groupId",groupID);
						}
						maps.put("userId",UserInfo.getInstance(this).getId());
						//来源 0 平台 1 药企
						//goods/getGoodsListByGroupId
					 /*	new HttpManager().get(ChoiceMedicineActivity.this,interfaces, MedicineEntity.class,
								Params.getGoodsInfo(groupID,entitys.getId(), "1"),
								ChoiceMedicineActivity.this,false, 2);*/
							new HttpManager().post(ChoiceMedicineActivity.this,
									"org/goods/getGoodsListByIndications", DrugDtaList.class,
									maps,
									ChoiceMedicineActivity.this, false, 1);
					}

	}
/*	public void back() {
		if (null == entity || null == entity.getParent()) {
			// dialogBack();
			Intent intent = new Intent();
			setResult(4, intent);
			finish();
			return;
		}
		if (entity.getParent().contains("/")) {
			MActivityManager.getInstance().finishAllActivityMedie();
		} else {
			entity = maps.get(entity.getParent());
			lists_childrens.clear();
			getMedieList(entity);
			IllEntity e= entity;
			ArrayList<IllEntity> ills ;
			if (null==entity){
				ills = null;
			}else {
				ills = e.getChildren();
			}
			getParents(ills);

		}
		handler.sendEmptyMessage(0);
		handler.sendEmptyMessage(1);
	}*/




	TreeMap<String, IllEntity> maps = new TreeMap<String, IllEntity>();

	/*public void getTreeMap(ArrayList<IllEntity> entity) {
		for (int i = 0; i < entity.size(); i++) {
			IllEntity en = entity.get(i);
			if (null != en.getChildren() && en.getChildren().size() != 0) {
				IllEntity e = null;
				if (null!=en.getParent()){
					e= maps.get(en.getParent());
					if (e!=null){
						if (!TextUtils.isEmpty(e.destitle)){
							en.destitle = e.destitle+">"+en.getName();
						}else {
							en.destitle = en.getName();
						}
					}else {
						en.destitle = en.getName();
					}
				}
				maps.put(en.getId(), en);
				for (int j = 0; j < en.getChildren().size(); j++) {
					getTreeMap(en.getChildren());
				}
			} else {
				IllEntity e = null;
				if (null!=en.getParent()){
					e= maps.get(en.getParent());
					if (e!=null) {
						if (!TextUtils.isEmpty(e.destitle)) {
							en.destitle = e.destitle + ">" + en.getName();
						} else {
							en.destitle = en.getName();
						}
					}else {
						en.destitle = en.getName();
					}
				}
				maps.put(en.getId(), en);
			}
		}
	}*/
	public void back() {
		if (null == entity || null == entity.getParent()) {
			// dialogBack();
			Intent intent = new Intent();
			setResult(4, intent);
			finish();
			return;
		}
		if (entity.getParent().contains("/")) {
			//getParents(original_parents );
			MActivityManager.getInstance().finishAllActivity();
		} else {
			entity = maps.get(entity.getParent());
			lists_childrens.clear();
			if (entity==null){
				getMedieList("0");
			}else {
				getMedieList(entity.getId());
			}

			getParents(entity, null);

		}
		listsTitle.remove(listsTitle.size() - 1);
		handler.sendEmptyMessage(0);
		/*childrenAdapter = new ChoiceMedicineChildrenadapter(
				ChoiceMedicineActivity.this,
				R.layout.item_listview_choice_medicine_children,
				lists_childrens, lists_parents, choice);
		listview_medicine_children.setAdapter(childrenAdapter);*/
		handler.sendEmptyMessage(1);
	}
	public void getParents(IllEntity entity, ArrayList<IllEntity> entitys) {
		lists_parents.clear();
		ArrayList<IllEntity> entitylist;
		if (null == entity) {
			entitylist = entitys;
		} else {

			entitylist = entity.getChildren();
		}
		if (null == entitylist || entitylist.size() < 1) {
			entitylist = original_parents;
		}
		for (int i = 0; i < entitylist.size(); i++) {
			IllEntity ills = null;
			try {
				ills = (IllEntity) entitylist.get(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists_parents.add(ills);
		}
		//getMedieList(entity);
	}
	public void getParents( ArrayList<IllEntity> entitys) {
		lists_parents.clear();

		if (null == entitys || entitys.size() < 1) {
			return;
		}
		for (int i = 0; i < entitys.size(); i++) {
			IllEntity ills = null;
			try {
				ills = (IllEntity) entitys.get(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists_parents.add(ills);
		}

	}
	public void getTreeMap(ArrayList<IllEntity> entity) {
		for (int i = 0; i < entity.size(); i++) {
			IllEntity en = entity.get(i);
			//IllEntityDao.getInstance(this).create(en);
			if (null != en.getChildren() && en.getChildren().size() != 0) {
				maps.put(en.getId(), en);
				for (int j = 0; j < en.getChildren().size(); j++) {
					getTreeMap(en.getChildren());
					//IllEntityDao.getInstance(this).create(en.getChildren().get(j));
				}
			} else {
				maps.put(en.getId(), en);
			}
		}
	}


	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		closeLoadingDialog();

	}

	@Override
	public void onSuccess(ArrayList response) {

		// TODO Auto-generated method stub


	}

	@Override
	public void onSuccess(Result response) {
		isLoading = true;
		// TODO Auto-generated method stub
		if (response.resultCode ==1 ){
			if (response instanceof DrugDtaList) {
				DrugDtaList result = (DrugDtaList) response;
				ArrayList<DrugData> lists = result.data;
				ArrayList<MedicineInfo> infos = DrugChange.getMedicineList(lists);
				if (haveImage) {
					haveImage = false;
					lists_parents.clear();
				}
				lists_childrens.clear();
				if (infos.size()>0){

					lists_childrens.addAll(infos);
					lists_childrensOrigain.clear();
					if (null!=infos) {
						lists_childrensOrigain.addAll(infos);
					}

					if (lists_parents == null) {
						lists_parents =new ArrayList<IllEntity>();
					}

				}
				refreshData();
				isLoading = false;
			}else if(response instanceof IllTree){
				final IllTree t = (IllTree) response;
				if (t.data!=null&&t.data.size()>0){
					final ArrayList<IllEntity> entity = t.data;
					if (entity.size()==0) {
						closeLoadingDialog();
						return;
					}
					showLoadingDialog();
					new Thread(new Runnable() {
						@Override
						public void run() {
							analysisIllData(entity,true);
							original_parents = t.data;
							handler.sendEmptyMessage(1);
							getMedieList("0");
						}
					}).start();
				}

			}
		}else {
			ToastUtils.showResultToast(this, response);
		}

	}
	public void refreshData(){
		getBuyCarData(true, false);
		ArrayList<MedicineInfo> infos =DataUtils.getList(false, ChoiceMedicineActivity.this, list2, lists_childrens);
		list2.clear();
		list2.addAll(infos);

		lists_childrens.clear();
		lists_childrens.addAll(list2);
		handler.sendEmptyMessage(1);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId() == R.id.rl_back){
			back();
		}else if(v.getId() == R.id.btn_tra){
		}else if(v.getId()== R.id.rl_search){
			MActivityManager.getInstance().finishAllActivityMedie();
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);

		}else if (v.getId() == R.id.tv_title) {
			SharedPreferences sp = getSharedPreferences(SharePrefrenceConst.MEDICINE_INFO, 0);
			sp.edit().clear().commit();
		}
	}



	public void getIllinfo() {
		try {
			ArrayList<IllEntity> ill = ChoiceMedicineLogic.deSerializationIll(ChoiceMedicineLogic.getObject(ChoiceMedicineActivity.this,SharePrefrenceConst.MEDICINE));
			if (ill ==null||ill.size()==0) {

				getIlls();
			}else {
				if ((TimeUtils.getNowTime()-SharedPreferenceUtil.getLong(this,SharePrefrenceConst.SHARE_TIME, 0))>7*24*60*60*1000) {
					getIlls();
				}else {
					analysisIllData(ill,false);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ChoiceMedicineLogic.saveObject(this,"", SharePrefrenceConst.MEDICINE);
			getIlls();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

	}
	public void getIlls(){
		showLoadingDialog();
		/*HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("interface1", "tree");
		interfaces.put("interface2", Constants.ALL_ILL_INFO);
		new HttpManager()
				.get(this,interfaces, IllEntity.class, null, this, true, 2);*/
		//health/base/getDiseaseTree
		new HttpManager()
				.post(this, "base/getDiseaseTree", IllTree.class, null, this, false, 3);
	}
	public void analysisIllData(final ArrayList<IllEntity> entity,final boolean isSave ){
		getParents(entity);
		getTreeMap(entity);
		original_parents = entity;
		if (isSave) {
			try {
				ChoiceMedicineLogic.saveObject(ChoiceMedicineActivity.this,ChoiceMedicineLogic.serialize(entity), SharePrefrenceConst.MEDICINE);
				SharedPreferenceUtil.putLong(this, SharePrefrenceConst.SHARE_TIME, TimeUtils.getNowTime());
				SharedPreferenceUtil.putString(this, SharePrefrenceConst.SHARE_SAVE, "save1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		handler.sendEmptyMessage(1);
	}
	@Override
	public void onBackPressed() {
		back();
	}
	@Override
	public void refreshClear(boolean clean) {
		// TODO Auto-generated method stub
		if (clean){
			lists_childrens.clear();
			for (int i=0;i<lists_childrensOrigain.size();i++){
				MedicineInfo info = lists_childrensOrigain.get(i);
				info.num = 0;
				lists_childrensOrigain.set(i,info);
			}
			lists_childrens.addAll(lists_childrensOrigain);
			handler.sendEmptyMessage(1);
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
