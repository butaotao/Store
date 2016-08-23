package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;

/**
 * Created by Burt on 2016/8/22.
 */
public class EditColleageDepartmentActivity extends CompanyContactListActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContent() {
        return editColleageDep;
    }
    public void onColleagueEdit(CompanyContactListEntity c2,int position){
       return;
    }
}
