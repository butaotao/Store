package com.dachen.dgroupdoctorcompany.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceDoctorForChatAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2DoctorChatActivity;
import com.dachen.dgroupdoctorcompany.views.InputDialog;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/3/4.
 */
public class ChoiceDoctorForChatActivity extends BaseActivity {
    public static final int        REQUEST_SELECT_ADDRESS = 102;
    ListView listView;
    ChoiceDoctorForChatAdapter doctorForChatAdapter;
    List<Doctor> doctors;
    DoctorDao dao;
    private SessionGroup groupTool;
    private String       where;
    private Button       btSubmit;
    private LinearLayout vAddress;
    private TextView     tvAddress;
    private String       type;
    private String       mStrAddress;
    private double                 latitude;//纬度
    private double                 longitude;//经度
    private String                 city;//城市
    private String                 mStrAddressName;
    private int from;

    private LinearLayout empty_view;
    private LinearLayout vBottom;
    private LinearLayout vInfo;
    private TextView tv_empty;
    private TextView mSearch;
    LinearLayout layout_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitychoicedoctor);
        setTitle("选择联系人");
        where = this.getIntent().getStringExtra("where");
        type = this.getIntent().getStringExtra("type");
        from = this.getIntent().getIntExtra("from",TogetherVisitActivity.MODE_FROM_VIST_LIST);
        if(null != where && "AddSignInActivity".equals(where)){
            setTitle("选择客户");
        }
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
                if(null != where && "AddSignInActivity".equals(where)){
                    if(null != type && "selectAddress".equals(type)){
                        Intent intent1 = new Intent(ChoiceDoctorForChatActivity.this,ChoiceMedieaActivity.class);
                        intent1.putExtra("mode",ChoiceMedieaActivity.MODE_MULTI_VISIT);
                        intent1.putExtra("doctorid",doctor.userId);
                        intent1.putExtra("doctorname",doctor.name);
                        intent1.putExtra("latitude",latitude);
                        intent1.putExtra("longitude",longitude);
                        intent1.putExtra("city",city);
                        intent1.putExtra("address",mStrAddress);
                        intent1.putExtra("addressName",mStrAddressName);
                        intent1.putExtra("from",from);
                        startActivity(intent1);
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("doctorid",doctor.userId);
                        intent.putExtra("doctorname",doctor.name);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    return;
                }

                groupTool = new SessionGroup(mThis);
                groupTool.setCallback(new CallBack(doctor.userId));
                createChatGroup(doctor.userId);
            }
        });

        layout_search = getViewById(R.id.layout_search);
        mSearch = getViewById(R.id.et_search);
        mSearch.setOnClickListener(this);
        layout_search.setOnClickListener(this);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        vAddress = (LinearLayout) findViewById(R.id.vAddress);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        empty_view = (LinearLayout) findViewById(R.id.empty_view);
        vBottom = (LinearLayout) findViewById(R.id.vBottom);
        vInfo = (LinearLayout) findViewById(R.id.vInfo);
        tv_empty = (TextView) findViewById(R.id.tv_empty);

        TextView tvInput = (TextView) findViewById(R.id.tvInput);
        TextView tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvInput.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvInput.getPaint().setAntiAlias(true);
        tvCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCancel.getPaint().setAntiAlias(true);
        if(doctors.size()>0){
            empty_view.setVisibility(View.GONE);
        }else{
            empty_view.setVisibility(View.VISIBLE);
        }
        if(null != where && "AddSignInActivity".equals(where)){
            vBottom.setVisibility(View.VISIBLE);
            tv_empty.setText("您当前还没有医生好友可选择");
            if(null != type && "selectAddress".equals(type)){
                vAddress.setVisibility(View.VISIBLE);
                btSubmit.setVisibility(View.GONE);
                vInfo.setVisibility(View.VISIBLE);

                mStrAddress = this.getIntent().getStringExtra("address");
                latitude = this.getIntent().getDoubleExtra("latitude",0);
                longitude = this.getIntent().getDoubleExtra("longitude",0);
                city = this.getIntent().getStringExtra("city");
                tvAddress.setText(mStrAddress);
                mStrAddressName = this.getIntent().getStringExtra("addressName");
            }else{
                vAddress.setVisibility(View.GONE);
                btSubmit.setVisibility(View.VISIBLE);
                vInfo.setVisibility(View.GONE);
            }
        }else{
            tv_empty.setText("您的通讯录还没有医生");
            vBottom.setVisibility(View.GONE);
        }

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDocName();
            }
        });
        vAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceDoctorForChatActivity.this,SelectAddressActivity.class);
                intent.putExtra("select_mode",SelectAddressActivity.MODE_SELECT_ADDRESS);
                intent.putExtra("poi","地名地址信息|医疗保健服务|商务住宅|交通设施服务|公司企业|公共设施");
                intent.putExtra("distance",250);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("city",city);
                startActivityForResult(intent,REQUEST_SELECT_ADDRESS);
            }
        });

        tvInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDocName();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChoiceDoctorForChatActivity.this,ChoiceMedieaActivity.class);
                intent1.putExtra("mode",ChoiceMedieaActivity.MODE_MULTI_VISIT);
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("longitude",longitude);
                intent1.putExtra("city",city);
                intent1.putExtra("address",mStrAddress);
                intent1.putExtra("addressName",mStrAddressName);
                intent1.putExtra("from",from);
                startActivity(intent1);
            }
        });

    }

    private void inputDocName(){
        InputDialog dialog = new InputDialog(ChoiceDoctorForChatActivity.this);
        dialog.setOnConfirmListener(
                new InputDialog.OnConfirmListener() {
            @Override
            public void onConfirm(Dialog dialog, String input) {
                if(!TextUtils.isEmpty(input)){
                    if(null != type && "selectAddress".equals(type)){
                        Intent intent1 = new Intent(ChoiceDoctorForChatActivity.this,ChoiceMedieaActivity.class);
                        intent1.putExtra("mode",ChoiceMedieaActivity.MODE_MULTI_VISIT);
                        intent1.putExtra("doctorname",input);
                        intent1.putExtra("latitude",latitude);
                        intent1.putExtra("longitude",longitude);
                        intent1.putExtra("city",city);
                        intent1.putExtra("address",mStrAddress);
                        intent1.putExtra("addressName",mStrAddressName);
                        intent1.putExtra("from",from);
                        startActivity(intent1);
                    }else{
                        Intent intent = new Intent();
//                    intent.putExtra("doctorid",doctor.userId);
                        intent.putExtra("doctorname",input);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }else{
                    ToastUtil.showToast(ChoiceDoctorForChatActivity.this,"请输入医生名称");
                }
            }
        });
        dialog.show();
        dialog.setTitle("请输入医生姓名");
        dialog.setEditTextHint("医生姓名");
        dialog.setEditInputType();
    }

    private void createChatGroup(String userId){
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        groupTool.createGroup(userIds, "10");
    }
    private class CallBack implements SessionGroupCallback{
        String userId;

        public CallBack(String userId) {
            this.userId = userId;
        }

        @Override
        public void onGroupInfo(Data data, int what) {
            ChatGroupDao dao=new ChatGroupDao();
            dao.saveOldGroupInfo(data);
            Represent2DoctorChatActivity.openUI(mThis, data.gname, data.gid, userId);
            finish();
        }

        @Override
        public void onGroupInfoFailed(String msg) {
            ToastUtil.showToast(mThis, "创建会话失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK && null != data) {
            mStrAddress = data.getStringExtra("floor");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            mStrAddressName = data.getStringExtra("address");
            tvAddress.setText(mStrAddress);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_search:
            case R.id.et_search:
                Intent intent = new Intent(this, SearchContactActivity.class);
                intent.putExtra("seachdoctor","doctor");
                intent.putExtra("selectMode", 2);    //搜索选择不返回（非1）
                startActivity(intent);
                break;
            case R.id.rl_back:
               finish();
                break;
        }
    }

}
