package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.json.EmptyResult;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.utils.CompareDatalogic;
import com.dachen.dgroupdoctorcompany.utils.ExitActivity;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.adapter.ChatGroupAdapter;
import com.dachen.imsdk.adapter.MsgMenuAdapter;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.net.ImCommonRequest;
import com.dachen.imsdk.net.PollingURLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转发消息页
 */
public class ChatShareMsgActivity extends ImBaseActivity implements View.OnClickListener {
    public static final String TAG = "ChatShareMsgActivity";

    private ChatGroupAdapter mAdapter;
    private List<ChatGroupPo> mList=new ArrayList<>();
    private String msgId;
    private RelativeLayout rl_colleague,rl_doctor;
    private View center_line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vchat_menber_activity);
        ExitActivity.getInstance().addActivity(this);

        msgId = getIntent().getStringExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID);

        TextView tv = (TextView) findViewById(com.dachen.imsdk.R.id.tv_title);
        tv.setText("转发消息");

        findViewById(R.id.btn_confirm).setVisibility(View.GONE);
        findViewById(R.id.back_btn).setOnClickListener(this);
        rl_colleague = (RelativeLayout) findViewById(R.id.rl_colleague);
        rl_doctor = (RelativeLayout) findViewById(R.id.rl_doctor);
        center_line = findViewById(R.id.center_line);
        rl_colleague.setOnClickListener(this);
        rl_doctor.setOnClickListener(this);

        if(UserInfo.getInstance(getApplicationContext()).isMediePresent()){
            rl_doctor.setVisibility(View.VISIBLE);
        }else {
            rl_doctor.setVisibility(View.GONE);
            center_line.setVisibility(View.GONE);
        }


        mAdapter = new ChatGroupAdapter(this, mList);
        ListView lv = (ListView) findViewById(com.dachen.imsdk.R.id.list_view);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(itemClickListener);
        new InitTask().execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.rl_colleague:
//                ToastUtil.showToast(this,"新建同事会话");
                if (!CompareDatalogic.isInitContact()) {
                    ToastUtil.showToast(this,"通讯录初始化中...");
                    return;
                }
                Intent intent = new Intent(this, SelectPeopleActivity.class);
                intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_TYPE,-1);
                intent.putExtra("share", true);
                intent.putExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID,msgId);
                startActivity(intent);
//                CallIntent.SelectPeopleActivity(this, null, -1);
                break;
            case R.id.rl_doctor:
//                ToastUtil.showToast(this,"新建医生会话");
//                CallIntent.selectDoctor(this);
                Intent intent2 = new Intent(this, ChoiceDoctorForChatActivity.class);
                intent2.putExtra("share", true);
                intent2.putExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID,msgId);
                startActivity(intent2);
                break;
        }

    }

    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ChatGroupPo group = mList.get(position);
            CustomDialog.Builder builder = new CustomDialog.Builder(ChatShareMsgActivity.this,new CustomDialog.CustomClickEvent(){


                @Override
                public void onClick(CustomDialog customDialog) {
                    customDialog.dismiss();
                    forwardMsg(group.groupId);
                }

                @Override
                public void onDismiss(CustomDialog customDialog) {
                    customDialog.dismiss();
                }
            }).setTitle("确定发送给").setMessage(group.name).setPositive("确定").setNegative("取消");
            builder.create().show();

        }
    };
    private class InitTask extends AsyncTask<Void,Void,List<ChatGroupPo>> {
        @Override
        protected List<ChatGroupPo> doInBackground(Void... params) {
            ChatGroupDao dao=new ChatGroupDao();
            return dao.queryInBizTypeExcept(null,new String[]{"GROUP_0001","pub_010","pub_001","pub_002",});
        }

        @Override
        protected void onPostExecute(List<ChatGroupPo> chatGroupPos) {
            mList.addAll(chatGroupPos);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void forwardMsg(String groupId){
        Map<String,Object> reqMap=new HashMap<>();
        reqMap.put("msgId",msgId);
        reqMap.put("gid",groupId);
        reqMap.put("index",0);
        mDialog.show();
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                EmptyResult result= JSON.parseObject(s,EmptyResult.class);
                if(result.resultCode==1){
                    ToastUtil.showToast(mThis,"转发成功");
                    finish();
                }else{
                    ToastUtil.showToast(mThis,result.detailMsg);
                }
                mDialog.dismiss();
            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showErrorNet(mThis);
                mDialog.dismiss();
            }
        };
        StringRequest req = new ImCommonRequest(PollingURLs.forwardMsg(),reqMap,listener,errorListener);
        VolleyUtil.getQueue(this).add(req);
    }

}
