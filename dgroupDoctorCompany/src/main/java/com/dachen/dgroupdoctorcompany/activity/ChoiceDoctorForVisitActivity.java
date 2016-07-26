package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceDoctorForChatAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2RepresentChatActivity;
import com.dachen.dgroupdoctorcompany.js.MyJsInterface;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/3/4.
 */
public class ChoiceDoctorForVisitActivity extends BaseActivity {
    ListView listView;
    ChoiceDoctorForChatAdapter doctorForChatAdapter;
    List<Doctor> doctors;
    DoctorDao dao;
    private SessionGroup groupTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitychoicedoctor);
        setTitle("选择联系人");
        dao = new DoctorDao(this);
        doctors = dao.queryAllByUserid();
        if (null==doctors){
            doctors = new ArrayList<>();
        }
        listView = (ListView) findViewById(R.id.listView);
        doctorForChatAdapter = new ChoiceDoctorForChatAdapter(this,R.layout.layout_choicedoctor,doctors);
        listView.setAdapter(doctorForChatAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor doctor = doctorForChatAdapter.getItem(position);
//                ToastUtil.showToast(ChoiceDoctorForChatActivity.this, doctor.userId);
                MyJsInterface myjs = new MyJsInterface(ChoiceDoctorForVisitActivity.this);
                if (null != myjs.getDoctorInterface) {
                    myjs.getDoctorInterface.getDoctorInfo(doctor);
                }
                myjs.getDoctorInterface = null;
                finish();
            }
        });
    }
}
