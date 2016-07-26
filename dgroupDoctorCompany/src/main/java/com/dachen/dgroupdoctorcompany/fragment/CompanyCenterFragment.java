package com.dachen.dgroupdoctorcompany.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.MeetingListActivity;
import com.dachen.dgroupdoctorcompany.activity.RecordActivity;
import com.dachen.dgroupdoctorcompany.activity.SignInActivity;
import com.dachen.dgroupdoctorcompany.activity.VisitListActivity;
import com.dachen.dgroupdoctorcompany.activity.WebActivityForCompany;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.entity.H5Url;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/18.
 */
public class CompanyCenterFragment extends BaseFragment implements ExpandableListView.OnChildClickListener,
        View.OnClickListener ,OnHttpListener{
    private View mRootView;

    TextView tv_login_title;
    RelativeLayout rl_companycontact;
    RelativeLayout rl_sign_in;
    RelativeLayout rl_singrecord;
    DepAdminsListDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = LayoutInflater.from(mActivity).inflate(
                R.layout.activity_companycenter, null);
        ButterKnife.bind(mActivity);
        dao = new DepAdminsListDao(mActivity);
        mRootView.findViewById(R.id.rl_companycontact).setOnClickListener(this);
        mRootView.findViewById(R.id.rl_conferencemangerment).setOnClickListener(this);
        rl_singrecord = (RelativeLayout) mRootView.findViewById(R.id.rl_singrecord);
        rl_singrecord.setOnClickListener(this);
        rl_companycontact = (RelativeLayout) mRootView.findViewById(R.id.rl_companycontact);
        tv_login_title = (TextView) mRootView.findViewById(R.id.tv_title);
        tv_login_title.setText("企业中心");
        mRootView.findViewById(R.id.rl_back).setVisibility(View.INVISIBLE);
        if (UserInfo.getInstance(mActivity).isMediePresent()){
            rl_companycontact.setVisibility(View.VISIBLE);
        }else {
            rl_companycontact.setVisibility(View.GONE);
        }
        rl_singrecord.setVisibility(View.VISIBLE);
        if (dao.queryByUserId()==null||dao.queryByUserId().size()==0){
            rl_singrecord.setVisibility(View.GONE);
        }
//        rl_companycontact.setVisibility(View.GONE);
        rl_sign_in = (RelativeLayout) mRootView.findViewById(R.id.rl_sign_in);
        rl_sign_in.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // setUpViews();

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_conferencemangerment:
                //ToastUtils.showToast(mActivity,"会议管理");
                //Intent intent = new Intent(mActivity, MeetingManagementActivity.class);
                showLoadingDialog();
                Intent intent = new Intent(mActivity, MeetingListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_companycontact:
//                new HttpManager().get(mActivity, Constants.GET_VISIT_URL, H5Url.class, Params
//                        .getInfoParams(mActivity),this,false, 1);
                Intent visitIntent = new Intent(mActivity,VisitListActivity.class);
                startActivity(visitIntent);
                break;
            case R.id.rl_sign_in:
                showLoadingDialog();
                Intent signIntent = new Intent(mActivity,SignInActivity.class);
                startActivity(signIntent);
                break;
            case R.id.rl_singrecord:
                Intent singRecordIntent = new Intent(mActivity,RecordActivity.class);
                startActivity(singRecordIntent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeLoadingDialog();
    }

    @Override
    public void onSuccess(Result response) {
        if(null != response){
            if(response instanceof H5Url){
                H5Url h5Url = (H5Url) response;
                String url = h5Url.resultMsg;
                Intent intentContact = new Intent(mActivity, WebActivityForCompany.class);
                intentContact.putExtra("url",url);
                startActivity(intentContact);
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

    }

}
