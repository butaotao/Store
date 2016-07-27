package com.dachen.dgroupdoctorcompany.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.ColleagueDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.CompanyContactListActivity;
import com.dachen.dgroupdoctorcompany.activity.DoctorDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.DoctorFriendActivity;
import com.dachen.dgroupdoctorcompany.activity.EidtColleagueActivity;
import com.dachen.dgroupdoctorcompany.activity.SearchContactActivity;
import com.dachen.dgroupdoctorcompany.adapter.AdapterOftenContact;
import com.dachen.dgroupdoctorcompany.adapter.DepManagerAdapter;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.activity.MyFavChatGroupActivity;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.dgroupdoctorcompany.utils.CompareDatalogic;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.imsdk.consts.SessionType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot1.event.EventBus;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class AddressList extends BaseFragment implements View.OnClickListener{
	// 总View，总视图
	private View mRootView;
	TextView tv_login_title;
	TextView et_search;
	List<BaseSearch> listss;
	private ChatGroupDao dao;
	ListView listView; //adapter_oftencontac
	AdapterOftenContact oftenContactAdapter;
	DoctorDao doctorDao;
	CompanyContactDao contactDao;
	TextView companyname;
	RelativeLayout rl_docutorcontact;
	View line4;
	View listviewheader;
	View vContract;
	ListView listviewmanagerdepartment;
	DepAdminsListDao depDao ;
	public static String deptId = "-1";
	List<DepAdminsList> lists;
	DepManagerAdapter depManagerAdapter;
	View line2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao=new ChatGroupDao();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = LayoutInflater.from(mActivity).inflate(R.layout.activity_addresslist, null);
		depDao = new DepAdminsListDao(mActivity);
		lists = new ArrayList<>();
		lists.clear();
		lists.addAll(depDao.queryManager());
		depManagerAdapter = new DepManagerAdapter(mActivity,lists);
		listviewheader = (View) View.inflate(mActivity, R.layout.layout_header, null);
		line2 = listviewheader.findViewById(R.id.line2);
		line2.setVisibility(View.GONE);
		if (lists.size()>0){
			line2.setVisibility(View.VISIBLE);
		}
		vContract = listviewheader.findViewById(R.id.vContract);
		companyname = (TextView) listviewheader.findViewById(R.id.companyname);
		String name = SharedPreferenceUtil.getString(mActivity,"enterpriseName","");

		rl_docutorcontact = (RelativeLayout) listviewheader.findViewById(R.id.rl_docutorcontact);
		listviewmanagerdepartment = (ListView) listviewheader.findViewById(R.id.listviewmanagerdepartment);
		listviewmanagerdepartment.setAdapter(depManagerAdapter);
		SetListViewHeight();
		listviewmanagerdepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (CompareDatalogic.isInitContact()) {
					DepAdminsList dept = (DepAdminsList) depManagerAdapter.getItem(position);
					deptId = dept.orgId;
					Intent intent = new Intent(mActivity, EidtColleagueActivity.class);
					startActivityForResult(intent, 200);
				}else{
					ToastUtils.showToast(mActivity,"通讯录初始化中...");
				}
			}
		});
		line4 = listviewheader.findViewById(R.id.line4);
		if (!TextUtils.isEmpty(name)){
			companyname.setText(name);
		}
		if (UserInfo.getInstance(mActivity).isMediePresent()){
			rl_docutorcontact.setVisibility(View.VISIBLE);
			line4.setVisibility(View.VISIBLE);
		}else {
			rl_docutorcontact.setVisibility(View.GONE);
			line4.setVisibility(View.GONE);
		}
		if (depDao.queryManager().size()>0){
			line4.setVisibility(View.GONE);
		}
		listss = new ArrayList<>();
		listView = (ListView) mRootView.findViewById(R.id.listView);

		doctorDao = new DoctorDao(mActivity);
		contactDao = new CompanyContactDao(mActivity);


		tv_login_title = (TextView) mRootView.findViewById(R.id.tv_title);
		tv_login_title.setText("通讯录");
		listviewheader.findViewById(R.id.rl_companycontact).setOnClickListener(this);
		listviewheader.findViewById(R.id.rl_docutorcontact).setOnClickListener(this);
		listviewheader.findViewById(R.id.rl_contactmy).setOnClickListener(this);
		mRootView.findViewById(R.id.rl_back).setVisibility(View.INVISIBLE);
		listView.addHeaderView(listviewheader);
		FrameLayout layout_search1 = (FrameLayout) listviewheader.findViewById(R.id.layout_search1);
		layout_search1.setOnClickListener(this);
		et_search = (TextView) listviewheader.findViewById(R.id.et_search);
		et_search.setOnClickListener(this);
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initView();


	}
	/**
	 * ListView根据项数的大小自动改变高度
	 */
	private void SetListViewHeight() {
		ListAdapter listAdapter = listviewmanagerdepartment.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listviewmanagerdepartment);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
//
		ViewGroup.LayoutParams params = listviewmanagerdepartment.getLayoutParams();
		params.height = totalHeight + (listviewmanagerdepartment.getDividerHeight() * (listAdapter.getCount() - 1));
		((ViewGroup.MarginLayoutParams)params).setMargins(0, 0, 0, 0);
		listviewmanagerdepartment.setLayoutParams(params);
	}
	@Override
	public void onResume() {
		super.onResume();
		refreshOftenContact();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void refreshOftenContact(){
		List<String> starings = getRecent();
		listss.clear();
		List<CompanyContactListEntity> list1 = null;
		List<Doctor> list2 = null;

		for (int i=0;i<starings.size();i++){
			list1 = contactDao.queryByUserId(starings.get(i));
			list2 = doctorDao.queryByUserId(starings.get(i));

			if (list1 !=null && list1.size()>0){
				if (!listss.contains(list1.get(0))){
					listss.add(list1.get(0));
				}

			}else if(list2 != null && list2.size()>0){
				if (!listss.contains(list2.get(0))){
					listss.add(list2.get(0));
				}
			}
		}
		oftenContactAdapter = new AdapterOftenContact(mActivity,R.layout.adapter_oftencontact,listss);

		listView.setAdapter(oftenContactAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				if (position > 0) {
					BaseSearch search = oftenContactAdapter.getItem(position - 1);
					if (search != null) {
						if (search instanceof Doctor) {
							Doctor doctor = (Doctor) search;
							intent = new Intent(mActivity, DoctorDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("doctordetail", doctor);
							intent.putExtra("doctordetail", bundle);
							startActivity(intent);
						} else if (search instanceof CompanyContactListEntity) {
							CompanyContactListEntity contact = (CompanyContactListEntity) search;
							intent = new Intent(mActivity, ColleagueDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("peopledes", contact);
							intent.putExtra("peopledes", bundle);
							startActivity(intent);

						}
					}
				}

			}
		});
		if(listss.size()>0){
			vContract.setVisibility(View.VISIBLE);
		}else{
			vContract.setVisibility(View.GONE);
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		closeLoadingDialog();
	}

	@Override
	public void onClick(View v) {
		Intent intent ;
		switch (v.getId()){
			case R.id.rl_companycontact:
				//ToastUtils.showToast(mActivity,"企业通讯录");
				if (CompareDatalogic.isInitContact()) {
					showLoadingDialog();
					deptId = "-1";
					intent = new Intent(mActivity, CompanyContactListActivity.class);
					startActivity(intent);
				} else {
					ToastUtils.showToast(mActivity, "通讯录初始化中...");
				}
					/*intent = new Intent(mActivity, FriendsContactsActivity.class);
				startActivity(intent);*/
				break;
			case R.id.rl_docutorcontact:
				//ToastUtils.showToast(mActivity,"医生通讯录");
				/*intent = new Intent(mActivity, HospitalActivity.class);
				startActivity(intent);*/
				intent = new Intent(mActivity, DoctorFriendActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_contactmy:
//				CallIntent.SelectPeopleActivity(mActivity);
				intent = new Intent(mActivity, MyFavChatGroupActivity.class);
				startActivity(intent);
				break;

			case R.id.et_search:
			case R.id.layout_search1:
				if (CompareDatalogic.isInitContact()) {
					intent = new Intent(mActivity, SearchContactActivity.class);
					startActivity(intent);
				} else {
					ToastUtils.showToast(mActivity, "通讯录初始化中...");
				}
				break;

			/*case R.id.iv_companycontactcontrol:
				*//*intent = new Intent(mActivity, FriendsContactsActivity.class);
				startActivity(intent);*//*
				intent = new Intent(mActivity, CompanyContactListActivity.class);
				intent.putExtra("idDep",deptId);
				startActivity(intent);
				break;
			case R.id.companydepartname://companydepartname
				intent = new Intent(mActivity, DoctorFriendActivity.class);
				intent.putExtra("idDep","0");
				startActivity(intent);
				break;*/
			/*case R.id.iv_irr:
				intent = new Intent(mActivity, HospitalManagerActivity.class);
				startActivity(intent);
				break;*/
		}
	}

	private List<String> getRecent(){
		List<String> result=new ArrayList<>();
		List<ChatGroupPo> groupList=dao.queryInType(null,new Integer[]{SessionType.session_double});
		for(ChatGroupPo po:groupList){
			result.add(ChatActivityUtilsV2.getSingleTargetId(po) );
		}
		return  result;
	}
    public void onEventMainThread(NewMsgEvent event) {
        refreshOftenContact();
    }
}