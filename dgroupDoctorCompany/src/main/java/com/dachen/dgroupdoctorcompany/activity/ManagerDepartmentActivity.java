package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

/**
 * Created by Burt on 2016/5/20.
 */
public class ManagerDepartmentActivity extends BaseActivity{
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerdepartment);
        listview = (ListView) findViewById(R.id.listview);
        enableBack();
        setTitle("");
    }
}
