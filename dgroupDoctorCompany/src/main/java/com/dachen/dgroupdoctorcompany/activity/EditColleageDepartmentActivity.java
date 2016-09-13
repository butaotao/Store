package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;

import java.util.Stack;

/**
 * Created by Burt on 2016/8/22.
 */
public class EditColleageDepartmentActivity extends OrgActivity  {
    Stack<CompanyDepment.Data.Depaments> departmentId = new Stack<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AddressList.deptId = "-1";
        super.onCreate(savedInstanceState);
       // GetAllDoctor.changeContact.clear();
        userId = getIntent().getStringExtra("userid");
      /*  TitleManager.showText(this, layoutView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "保存");*/
    }

    @Override
    public int getContent() {
        return 2;
    }


}
