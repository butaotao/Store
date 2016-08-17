package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.adapter.CommonAdapter;
import com.dachen.common.toolbox.DCommonRequest;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.MeetingListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.ArrayWrapObject;
import com.dachen.dgroupdoctorcompany.entity.Meeting;
import com.dachen.dgroupdoctorcompany.entity.MettingUsers;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.volley.custom.ObjectResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会议列表
 *
 * @author gaozhuo
 * @date 2016/3/8
 */
public class MeetingListActivity extends BaseActivity {
    private static final String TAG = MeetingListActivity.class.getSimpleName();
    private static final int PAGE_SIZE = 15;
    private static final int REQUEST_CODE_ADD_MEETING = 10001;
    private static final int REQUEST_CODE_MEETING_DETAILS = 10002;
    private PullToRefreshListView mRefreshListView;
    private ImageView mAdd;
    private int mCurrentPageIndex = 0;
    private List<Meeting> mMeetingList = new ArrayList<Meeting>();
    private CommonAdapter<Meeting> mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        initView();
        setTitle("会议直播");
        getMeetingList();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(TAG, "onNewIntent");
        mCurrentPageIndex = 0;
        mMeetingList.clear();
        getMeetingList();
    }

    @Override
    public void initView() {
        super.initView();
        mRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listview);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPageIndex = 0;
                //mMeetingList.clear();
                getMeetingList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMeetingList();
            }
        });
        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mThis, MeetingDetailActivity.class);
                intent.putExtra("meeting", mMeetingList.get(position - 1));
                startActivityForResult(intent, REQUEST_CODE_MEETING_DETAILS);
            }
        });
        mRefreshListView.setEmptyView(getViewById(R.id.empty_view));
        mAdd = (ImageView) findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        mAdapter = new MeetingListAdapter(this, mMeetingList, R.layout.list_item_meeting);
        mRefreshListView.setAdapter(mAdapter);
    }

    private void add() {
        Intent intent = new Intent(MeetingListActivity.this, CreateAndEditMeetingActivity.class);
        startActivity(intent);
    }

    /**
     * 获取会议列表
     */
    private void getMeetingList() {
        showLoadingDialog();
        final String reqTag = "getMeetingList";
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        queue.cancelAll(reqTag);
        StringRequest request = new DCommonRequest(Request.Method.POST, AppConfig.getUrl(Constants.MEETING_LIST, 3),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Logger.d(TAG, "response=" + response);
                mRefreshListView.onRefreshComplete();
                handleResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                closeLoadingDialog();
                mRefreshListView.onRefreshComplete();
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", UserInfo.getInstance(mThis).getSesstion());
                params.put("pageIndex", mCurrentPageIndex + "");
                params.put("pageSize", PAGE_SIZE + "");
                params.put("companyId", SharedPreferenceUtil.getString(mThis, "enterpriseId", ""));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    private void handleResponse(String response) {
        ObjectResult<ArrayWrapObject<Meeting>> result = JSON.parseObject(response, new TypeReference<ObjectResult<ArrayWrapObject<Meeting>>>() {
        });
        if (result != null && result.getResultCode() == Result.CODE_SUCCESS) {
            ArrayWrapObject<Meeting> arrayWrapObject = result.getData();
            if (arrayWrapObject != null) {
                if (mCurrentPageIndex >= arrayWrapObject.pageCount - 1) {//没有更多数据了
                    mRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                if(mCurrentPageIndex == 0){
                    mMeetingList.clear();
                }
                mCurrentPageIndex++;
                List<Meeting> meetingList = arrayWrapObject.pageData;
                if (meetingList != null && !meetingList.isEmpty()) {
                    mMeetingList.addAll(meetingList);
                }
                users();
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    public void users(){
        Integer[] users = new Integer[mMeetingList.size()];
        String s="";
        for (int i=0;i<mMeetingList.size();i++){
            Meeting meeting = mMeetingList.get(i);
            if (!TextUtils.isEmpty(meeting.createUserId)){
                users[i] = Integer.parseInt(meeting.createUserId);
            }
            if (i==0){
                s += meeting.createUserId;
            }else {
                s += ","+meeting.createUserId;
            }

        }



        HashMap<String,String> params = new HashMap<>();
        params.put("access_token",UserInfo.getInstance(mThis).getSesstion());
        params.put("userIds",s);
        new HttpManager().post(this, Constants.DRUG+"user/getUsersByIds",
                MettingUsers.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        closeLoadingDialog();
                        if (result instanceof MettingUsers){
                            MettingUsers users = (MettingUsers)result;


                            if (null!=mMeetingList&&mMeetingList.size()>0){
                                for (int j=0;j<mMeetingList.size();j++){
                                    Meeting ms = mMeetingList.get(j);
                                    if (null!=users.data&&users.data.size()>0){
                                        for (int i=0;i<users.data.size();i++){
                                            MettingUsers.User m = users.data.get(i);
                                            if (!TextUtils.isEmpty(ms.createUserId)&&(
                                                    (m.userId + "").equals(ms.createUserId))){
                                                Meeting meeting = new Meeting();
                                                meeting.createUserId=m.userId+"";
                                                ms.headPicFileName = m.headPicFileName;
                                                ms.createUserName = m.name;
                                                mMeetingList.set(j,ms);
                                                break;
                                            }
                                        }
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }




                        }


                    }
                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        closeLoadingDialog();
                    }
                },false, 1);
    }
}
