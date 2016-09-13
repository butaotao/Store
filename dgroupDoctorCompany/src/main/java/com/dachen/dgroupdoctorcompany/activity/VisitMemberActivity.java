package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.json.JsonMananger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.MemberAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.PersonModel;
import com.dachen.dgroupdoctorcompany.im.activity.RepresentGroupChatActivity;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.views.NoScrollGridView;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [拜访人员信息]
 *
 * @author zhouyuandong
 * @version 1.0
 * @date 2016-6-24
 *
 **/
public class VisitMemberActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private RelativeLayout rl_back;
    private TextView create_groupchat;
    private TextView tv_title;
    private MemberAdapter mAdapter;
    private List<String> list;
    private NoScrollGridView gridview;
    private String jsonVisitPeople;
    private List<PersonModel> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_member);

        initView();
        GetAllDoctor.getInstance().getPeople(this);
    }

    public void initView() {
        rl_back = getViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        create_groupchat = getViewById(R.id.create_groupchat);
        create_groupchat.setOnClickListener(this);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("拜访人员信息");
        gridview = getViewById(R.id.gridview);
        mAdapter = new MemberAdapter(this);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(this);
        jsonVisitPeople = this.getIntent().getStringExtra("jsonPeople");
        if(!TextUtils.isEmpty(jsonVisitPeople)){
        personList = JsonMananger.jsonToList(jsonVisitPeople,PersonModel.class);
        mAdapter.setDataSet(personList);
        mAdapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.create_groupchat:
                list = new ArrayList<String>();
                for(int i=0;i<personList.size();i++){
                    list.add(personList.get(i).getId());
                }
                createChatGroup(list);
                break;
        }
    }

    private void createChatGroup(List<String> userIds ){
        SessionGroup groupTool=new SessionGroup(this);
        groupTool.setCallback(callback);
        groupTool.createGroup(userIds, "10");
    }

    private SessionGroupCallback callback=new SessionGroupCallback() {
        @Override
        public void onGroupInfo(Data data, int what) {
            String gname = data.gname;
            String gid = data.gid;
            ArrayList<Data.UserInfo> uids = new ArrayList<>(Arrays.asList(data.userList));
            ChatGroupDao dao = new ChatGroupDao();
            dao.saveOldGroupInfo(data);
            finish();
            RepresentGroupChatActivity.openUI(mThis, gname, gid, uids);
        }

        @Override
        public void onGroupInfoFailed(String msg) {
            ToastUtil.showToast(mThis, "创建会话失败");
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(VisitMemberActivity.this, ColleagueDetailActivity.class);
        intent.putExtra("id",personList.get(i).getId());
        startActivity(intent);
    }
}
