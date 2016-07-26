package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.EidtColleagueAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.CompanyContactDataUtils;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/1.
 */
public class EidtColleagueActivity extends BaseActivity{
    ListView listview;
    EidtColleagueAdapter adapter;
    List<BaseSearch> listData;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    closeLoadingDialog();
                    if (msg.obj != null) {
                        List<CompanyContactListEntity> list = (List<CompanyContactListEntity>) msg.obj;
                        listData.clear();
                        listData.addAll(list);
                        if (listData.size() > 0) {
                            CompanyContactListEntity entity = (CompanyContactListEntity) listData.get(0);
                            setTitle(entity.department);
                        } else {
                            setTitle("");
                        }
                        adapter.notifyDataSetChanged();

                    }
                    break;
            }
        }
    };
    private MyThread mMyThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityeditcolleageu);
        listview = (ListView) findViewById(R.id.listview);
        listData = new ArrayList<>();
        findViewById(R.id.btn_add).setOnClickListener(this);
        enableBack();
        setTitle("");
        showLoadingDialog();
        adapter = new EidtColleagueAdapter(EidtColleagueActivity.this, R.layout.adapter_editcolleague,
                listData);
        listview.setAdapter(adapter);
        GetAllDoctor.getInstance().getPeople(EidtColleagueActivity.this);
        mMyThread = new MyThread();
        mMyThread.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_add:
                Intent intent = new Intent(this,FriendsContactsActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 1001) {
//            if (data != null) {
//                if (!TextUtils.isEmpty(data.getStringExtra("position"))) {
//                    listData.remove(Integer.parseInt(data.getStringExtra("position")));
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
        mMyThread = new MyThread();
        mMyThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            List<CompanyContactListEntity> managerPeople = CompanyContactDataUtils.getManagerPeople(
                    EidtColleagueActivity.this, AddressList.deptId);
            Message message = new Message();
            message.what = 1001;
            message.obj = managerPeople;
            mHandler.sendMessage(message);

        }
    }
}
