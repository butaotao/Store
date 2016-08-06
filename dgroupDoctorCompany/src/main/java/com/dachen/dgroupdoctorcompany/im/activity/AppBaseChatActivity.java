package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dachen.dgroupdoctorcompany.archive.ArchiveMainUi;
import com.dachen.dgroupdoctorcompany.archive.ArchiveUtils;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.ChatMessageV2;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.out.ImMsgHandler;

import java.util.HashMap;

/**
 * Created by Mcp on 2016/3/5.
 */
public abstract class AppBaseChatActivity extends ChatActivityV2 {
    public static final int REQUEST_CODE_GROUP_SETTING = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBgColor(0xffeef0f0);
        mChatContentView.setBackgroundColor(0xffeef0f0);
    }

    public void goArchiveItem(ArchiveItem item, ChatMessagePo po) {
        item.po = po;
        item.sendUserId = po.fromUserId;
        item.receiveUserId = ImSdk.getInstance().userId;
        ArchiveUtils.goArchiveDetailActivity(this, REQUEST_CODE_ARCHIVE, item, "im");
    }

    protected void clickArchive() {
        Intent intent = new Intent(this, ArchiveMainUi.class);
        intent.putExtra("from", "add");
        startActivityForResult(intent, REQUEST_CODE_ARCHIVE);
    }

    protected GroupInfo2Bean.Data makeGroupInfo(){
        ChatGroupPo po=groupPo;
        if(po==null)
            return null;
        Data info= new GroupInfo2Bean.Data();
        info.gname = po.name;
        info.gpic = po.gpic;
        info.type = po.type;
        info.status = po.status;
        info.notify = po.getNotityState();
        if (mUserList != null && !mUserList.isEmpty()) {
            UserInfo[] userList = new UserInfo[mUserList.size()];
            mUserList.toArray(userList);
            info.userList = userList;
        }
        return info;
    }

    @Override
    protected ImMsgHandler makeMsgHandler() {
        return new CompanyImMsgHandler(this);
    }

    public void receivedMessage(ChatMessageV2 receivedMessage) {
        super.receivedMessage(receivedMessage);
        // 没有新消息了
        if (!receivedMessage.more) {
            if (mIsFirstPoll == true) {
                mIsFirstPoll = false;
                share();
            }
        }
    }

    private void share() {

        HashMap<String, Object> params = (HashMap<String, Object>) getIntent().getSerializableExtra(INTENT_EXTRA_SHARE_PARAM);
        if (params != null) {//有数据要分享
            if (params.get("share_files") != null) {//文件分享
                ArchiveItem item = (ArchiveItem) params.get("share_files");
                sendArchive(item);
                return;
            }
        }
    }
}
