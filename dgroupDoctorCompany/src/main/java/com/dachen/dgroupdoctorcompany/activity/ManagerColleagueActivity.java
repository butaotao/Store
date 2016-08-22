package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.CompanyContactDataUtils;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by Burt on 2016/8/17.
 */
public class ManagerColleagueActivity extends CompanyContactListActivity implements View.OnClickListener{
    CompanyContactDao companyContactDao;
    String idDep;
    String companyId;
    CompanyContactListEntity entity;
    Button btn_addpeople;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    if (null!=entity){
                        getOrganization(entity.id);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ShowContent() {
            @Override
            public int showcontent() {
                return 1;
            }
        };
        super.onCreate(savedInstanceState);
        btn_addpeople = (Button) findViewById(R.id.btn_addpeople);
        btn_addpeople.setOnClickListener(this);
        btn_addpeople.setVisibility(View.VISIBLE);
        companyContactDao = new CompanyContactDao(ManagerColleagueActivity.this);
        List<CompanyContactListEntity> lists = companyContactDao.queryAll();
        idDep = AddressList.deptId;
        companyId = UserInfo.getInstance(this).getCompanyId();
    }

    @Override
    public int getContent() {
        return CompanyContactListActivity.isManager;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 300) {
            Message message = new Message();
            message.what = 1001;
            mHandler.sendMessage(message);
        }
       /* mMyThread = new MyThread();
        mMyThread.start();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
       switch (v.getId()){
            case R.id.btn_addpeople:
                Intent intent = new Intent(this, FriendsContactsActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }
    public void onColleagueEdit(CompanyContactListEntity c2,int position){
        entity = c2;
        Intent intent = new Intent(ManagerColleagueActivity.this, DeleteColleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("colleage", c2);
        intent.putExtra("colleage", bundle);
        intent.putExtra("position",position+"");
        startActivityForResult(intent, 300);
    }
}
