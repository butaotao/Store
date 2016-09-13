package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.DoctorAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.utils.HtmlTextViewEdit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/3/4.
 */
public class DoctorActivity extends BaseActivity {
    DoctorAdapter doctorAdapter;
    List<Doctor> lists;
    DoctorDao dao;
    ListView listView;
    ViewStub vstub_title;
    View view;
    RelativeLayout ll_sub;
    public String id;
    String name = "";
    RelativeLayout rl_notcontent;
    TextView tv_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        listView = (ListView) findViewById(R.id.listview);
        tv_des = (TextView) findViewById(R.id.tv_des);
        if (null != getIntent().getStringExtra("name")) {
            name = getIntent().getStringExtra("name");
        }
        id = getIntent().getStringExtra("id");
        setTitle(name);
        ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
      /*  view = vstub_title.inflate(this, R.layout.layout_plus_medie, ll_sub);
        ImageView viewImage = (ImageView) view.findViewById(R.id.iv_plus1);
          viewImage.setBackgroundResource(R.drawable.add);
        */

        view = vstub_title.inflate(this, R.layout.layout_plus_text, ll_sub);
        TextView viewImage = (TextView) view.findViewById(R.id.tv_plus1);
        viewImage.setText("添加医生");

        findViewById(R.id.rl_plus).setOnClickListener(this);
        dao = new DoctorDao(this);
        lists = dao.queryByDepID(id);
        rl_notcontent = (RelativeLayout) findViewById(R.id.rl_notcontent);
        tv_des.setText(HtmlTextViewEdit.getTimeSpan());
        if (null == lists||lists.size()==0) {
            lists = new ArrayList<>();
            rl_notcontent.setVisibility(View.VISIBLE);
        }else {
            rl_notcontent.setVisibility(View.GONE);
        }
        doctorAdapter = new DoctorAdapter(this, R.layout.adapter_doctor, lists);
        listView.setAdapter(doctorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DoctorActivity.this, DoctorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctordetail", (Serializable) doctorAdapter.getItem(position));
                intent.putExtra("doctordetail", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lists = dao.queryByDepID(id);
        if (null == lists||lists.size()==0) {
            lists = new ArrayList<>();
            rl_notcontent.setVisibility(View.VISIBLE);
        }else {
            rl_notcontent.setVisibility(View.GONE);
        }
        doctorAdapter = new DoctorAdapter(this, R.layout.adapter_doctor, lists);
        listView.setAdapter(doctorAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_plus:
                intent = new Intent(this, SearchDoctorDeptResultActivity.class);
                intent.putExtra("hospitId", id);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
                break;
        }
    }

}
