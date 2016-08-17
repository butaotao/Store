package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;

import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.List;

/**
 * Created by Burt on 2016/8/17.
 */
public class ManagerColleagueActivity extends CompanyContactListActivity{
    CompanyContactDao companyContactDao;
    String idDep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        companyContactDao = new CompanyContactDao(ManagerColleagueActivity.this);
        List<CompanyContactListEntity> lists = companyContactDao.queryAll();
        idDep = SharedPreferenceUtil.getString(this,"enterpriseId","");
    }
}
