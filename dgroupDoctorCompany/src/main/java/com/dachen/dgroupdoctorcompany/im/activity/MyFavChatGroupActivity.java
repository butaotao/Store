package com.dachen.dgroupdoctorcompany.im.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.im.adapter.MyFavChatGroupAdapter;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.event.NewMsgEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot1.event.EventBus;

/**
 * Created by Mcp on 2016/3/2.
 */
public class MyFavChatGroupActivity extends BaseActivity {

    private ChatGroupDao mDao;
    private ListView mListView;
    private MyFavChatGroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav_chat_group);
        setTitle("我的群组");
        mDao=new ChatGroupDao();
        mListView= (ListView) findViewById(R.id.list_view);
        List<ChatGroupPo> list=new ArrayList<>();
        mAdapter=new MyFavChatGroupAdapter(this,list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(itemClickListener);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    public void onEventMainThread(NewMsgEvent event){
        if(isActive)
            new RefreshListTask().execute();
    }

    @Override
    protected void onResume() {
        new RefreshListTask().execute();
        super.onResume();
    }

    private OnItemClickListener itemClickListener=new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ChatGroupPo po=mAdapter.getItem(position);
            ChatActivityUtilsV2.openUI(MyFavChatGroupActivity.this,po);
        }
    };
    private class RefreshListTask extends AsyncTask<Void,Void,List<ChatGroupPo> >{
        @Override
        protected List<ChatGroupPo> doInBackground(Void... params) {
            return mDao.queryInBizTypeExcept(null, null, 2);
        }
        @Override
        protected void onPostExecute(List<ChatGroupPo> chatGroupPos) {
            mAdapter.getItems().clear();
            mAdapter.addItems(chatGroupPos);
        }
    }
}
