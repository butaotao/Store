package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;

/**
 * Created by Burt on 2016/8/22.
 */
public class EditColleageDepartmentActivity extends CompanyContactListActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AddressList.deptId = "-1";
        super.onCreate(savedInstanceState);
        TitleManager.showText(context, layoutView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectDept();
            }
        }, "保存");
    }

    @Override
    public int getContent() {
        return editColleageDep;
    }
    public void onColleagueEdit(CompanyContactListEntity c2,int position){
       return;
    }

    public CompanyDepment.Data.Depaments getSelectDept(){
        for (int i=0;i<list.size();i++){
            if (list.get(i) instanceof CompanyDepment.Data.Depaments ){
                CompanyDepment.Data.Depaments depaments = (CompanyDepment.Data.Depaments)list.get(i);
                if (depaments.check == true){
                    return depaments;
                }
            }

        }
        return null;
    }
}
