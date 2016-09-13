package com.dachen.dgroupdoctorcompany.archive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.imsdk.consts.MessageType;
import com.dachen.imsdk.db.dao.ChatMessageDao;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.ChatMessageV2.ArchiveMsgParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Mcp on 2016/3/4.
 */
public class ArchiveRecentUi extends BaseActivity {
    public static final int REQ_CODE_ARCHIVE_DETAIL = 101;

    private String groupId;
    private ListView lv;
    private List<ArchiveItem> itemList;
    private ArchiveListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_recent_ui);
        lv= (ListView) findViewById(R.id.listView);
        lv.setEmptyView(findViewById(R.id.empty_container));
        groupId=getIntent().getStringExtra(ChatActivityV2.INTENT_EXTRA_GROUP_ID);
        itemList=makeItemList();
        mAdapter=new RecentAdapter(this,itemList);
        lv.setAdapter(mAdapter);
    }

    private List<ArchiveItem> makeItemList(){
        ArrayList<ArchiveItem> list=new ArrayList<>();
        ChatMessageDao dao=new ChatMessageDao();
        List<ChatMessagePo> msgList=dao.queryForType(groupId, MessageType.file,false);
        HashSet<String> keys=new HashSet<>();
        for(ChatMessagePo msg:msgList){
            ArchiveMsgParam p= JSON.parseObject(msg.param,ArchiveMsgParam.class);
            if(p==null)continue;
            if(keys.contains(p.key))continue;
            keys.add(p.key);
            ArchiveItem item = new ArchiveItem(p.key, p.uri, p.name, p.format, p.size);
            item.sendDate=msg.sendTime;
            if (!list.contains(item)){
                list.add(item);
            }

        }
        return list;
    }

    private class RecentAdapter extends ArchiveListAdapter{

        public RecentAdapter(Context context, List<ArchiveItem> mList) {
            super(context, mList);
        }

        @Override
        protected void onClickItem(ArchiveItem item) {
            item.items = itemList;
            ArchiveUtils.goArchiveDetailActivity(mThis, REQ_CODE_ARCHIVE_DETAIL, item, "im");
        }
    }
    public static void openUI(ChatActivityV2 act,String groupId){
        Intent i=new Intent(act,ArchiveRecentUi.class);
        i.putExtra(ChatActivityV2.INTENT_EXTRA_GROUP_ID,groupId);
        act.startActivityForResult(i,ChatActivityV2.REQUEST_CODE_ARCHIVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_ARCHIVE_DETAIL) {
            if (resultCode != Activity.RESULT_OK)
                return;
            ArchiveItem item = (ArchiveItem) data.getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
            Intent i = new Intent();
            i.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, item);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }
}
