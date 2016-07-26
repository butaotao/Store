package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.DoctorFriendAdapter;
import com.dachen.dgroupdoctorcompany.adapter.HospitalAdapter;
import com.dachen.dgroupdoctorcompany.adapter.HospitalManagerChildrenAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.DoctorsList;
import com.dachen.dgroupdoctorcompany.entity.Hospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalManagers;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/5/20.
 */
public class DoctorFriendActivity extends BaseActivity implements HttpManager.OnHttpListener{
    ExpandableListView expandableListView;
    HospitalManagers managers = new HospitalManagers();
    DoctorFriendAdapter adapter;
    ViewStub vstub_title;
    DoctorDao dao;
    RelativeLayout rl_notcontent;
    EditText et_search;
    public static ArrayList<HospitalDes> hospitals;
    public static List<BaseSearch> docts;
    HospitalAdapter adapterdoctor;
    ListView listviewdoctor;
    RelativeLayout rl_emptview;
    RelativeLayout rl_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctorfriends);
        listviewdoctor = (ListView) findViewById(R.id.listviewdoctor);
        expandableListView = (ExpandableListView) findViewById(R.id.list);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this,  R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        rl_emptview = (RelativeLayout) findViewById(R.id.rl_emptview);
        rl_et = (RelativeLayout) findViewById(R.id.rl_et);
        tv.setOnClickListener(this);
        tv.setText("添加医生");
        setTitle("医生好友");
        enableBack();
        et_search = (EditText) this.findViewById(R.id.et_search);
        et_search.setOnClickListener(this);
        et_search.clearFocus();
        et_search.setInputType(InputType.TYPE_NULL);
        et_search.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // 在这里编写自己想要实现的功能
                            forSearch();
                        }
                        return false;
                    }
                });
        dao = new DoctorDao(this);
        hospitals = new ArrayList<>();
        docts = new ArrayList<>();
        docts.addAll(dao.queryAllByUserid());
        getDoctorFriend();
        //if (null!=docts&&docts.size()<=100){
        listviewdoctor.setVisibility(View.VISIBLE);
        expandableListView.setVisibility(View.GONE);
      //  if (docts.size()==0){
            rl_et.setVisibility(View.GONE);
       // }
        // adapter.notifyDataSetChanged();
      /*  }else {
            listviewdoctor.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            getHospitalInfo();
          }*/

        managers.data = hospitals;
        listviewdoctor.setEmptyView(rl_emptview);
        adapter = new DoctorFriendAdapter(this,managers,expandableListView);
        adapterdoctor = new HospitalAdapter(this,R.layout.adapter_hospital,docts);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        listviewdoctor.setAdapter(adapterdoctor);
        listviewdoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterdoctor.getItem(position) instanceof Doctor) {
                    Intent intent = new Intent(DoctorFriendActivity.this, DoctorDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("doctordetail", adapterdoctor.getItem(position));
                    intent.putExtra("doctordetail", bundle);
                    startActivity(intent);
                }
            }
        });
    }
    public void forSearch(){
        if (!TextUtils.isEmpty(et_search.getText())) {


            getSearchResult(et_search.getText().toString());

        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.tv_search:
                intent = new Intent(this,HospitalActivity.class);
                intent.putExtra("showsearch","showsearch");
                startActivity(intent);
                break;
            case R.id.et_search:
                //
                intent = new Intent(this, SearchContactActivity.class);
                intent.putExtra("seachdoctor","doctor");
                startActivity(intent);
                break;
        }
    }
    public void getSearchResult(String keyword){
        if (null!=dao.querySearch(keyword)){
            docts.clear();
            docts.addAll(dao.querySearch(keyword));
            adapterdoctor.notifyDataSetChanged();
            listviewdoctor.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        dao = new DoctorDao(this);
        if (null!=response){
            if (response instanceof Hospital){
                Hospital hospital = (Hospital)response;
                if (null==hospital.data||null==hospital.data.pageData||hospital.data.pageData.size()==0){
                    rl_notcontent.setVisibility(View.VISIBLE);
                    hospitals.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }
                ArrayList<HospitalDes> desc= hospital.data.pageData;
                for (int j=0;j<desc.size();j++){
                    List<Doctor>  doctors= dao.queryByDepId(hospital.data.pageData.get(j).id);
                    desc.get(j).doctors = (ArrayList<Doctor>) doctors;
                }
                managers.data = desc;
                adapter = new DoctorFriendAdapter(this,managers,expandableListView);
                expandableListView.setAdapter(adapter);
                expandableListView.setGroupIndicator(null);
                adapter.notifyDataSetChanged();
                //   rl_notcontent.setVisibility(View.GONE);
                // hospitals.clear();;
                //  hospitals.addAll((Collection<? extends HospitalDes>) hospital.data.pageData);
            }else if (response instanceof DoctorsList){
                DoctorsList doctorsList = (DoctorsList) response;
                if (null!=doctorsList&&null!=doctorsList.data&&doctorsList.data.size()>0){
                    for (int i=0;i<doctorsList.data.size();i++){
                        Doctor doctor = doctorsList.data.get(i);
                        doctor.userloginid = SharedPreferenceUtil.getString(DoctorFriendActivity.this, "id", "");
                        dao.addCompanyContact(doctor);
                        List<Doctor>  doctors = dao.queryByUserId(doctor.userId);
                    }
                }
            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    class PinyinComparator implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }

    public void getDoctorFriend(){
        //enterprise/doctor/search

        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());

        new HttpManager().get(this, Constants.FRIENDLIST, DoctorsList.class,
                maps, this,
                false, 1);
    }
}
