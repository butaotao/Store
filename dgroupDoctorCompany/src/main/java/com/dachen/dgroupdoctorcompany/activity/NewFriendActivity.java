package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.AdapterNewFriend;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.Hospital;
import com.dachen.dgroupdoctorcompany.entity.NewFriendsEntity;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/3/7.
 */
public class NewFriendActivity extends BaseActivity implements HttpManager.OnHttpListener{
    ListView listview;
    AdapterNewFriend adapterNewFriend;
    List<NewFriendsEntity.Data.FriendInfo> baseSearches;
    public static final String NEWFRIENDGROUPID = "groupId";
    private String groupId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfriend);
        setTitle("新的好友");
        listview = (ListView) findViewById(R.id.listview);
        baseSearches = new ArrayList<>();
        adapterNewFriend = new AdapterNewFriend(this,R.layout.adapter_newfriend,baseSearches);
        listview.setAdapter(adapterNewFriend);
        if (null!=getIntent().getStringExtra(NEWFRIENDGROUPID)){
            groupId = getIntent().getStringExtra(NEWFRIENDGROUPID);
        }

        getNewFriends();

    }
    public void getNewFriends(){
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("pageSize","100");
        maps.put("groupId",groupId);//health/friends/getFriendReq
        new HttpManager().get(this, Constants.GETFRIEND, NewFriendsEntity.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
                                       closeLoadingDialog();
        if (null!=response&&response instanceof NewFriendsEntity){
            NewFriendsEntity friends =  (NewFriendsEntity) (response);
            if (friends.resultCode==1) {
                if (null!=friends.data)         {
                    baseSearches.clear();
                      baseSearches = friends.data.pageData;
                        adapterNewFriend = new AdapterNewFriend(this,R.layout.adapter_newfriend,baseSearches);
                        listview.setAdapter(adapterNewFriend);
                    adapterNewFriend.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
                    closeLoadingDialog();
    }
}
