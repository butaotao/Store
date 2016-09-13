package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.ColleagueDetailActivity;
import com.dachen.dgroupdoctorcompany.archive.ArchiveRecentUi;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.events.AddGroupUserEvent;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.MoreItem;
import com.dachen.imsdk.out.ImMsgHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mcp on 2016/2/24.
 */
public class Represent2RepresentChatActivity extends AppBaseChatActivity{

    private boolean userExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userExist= CommonUitls.isExitUser(this,mUserId);
    }

    @Override
    protected View onLoadHeaderLayout(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.im_header_represent2doctor, parent, false);
    }
    @Override
    protected void onHeaderLayoutLoaded(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(getIntent().getStringExtra(INTENT_EXTRA_GROUP_NAME));
        mRightMenu = (ImageButton) view.findViewById(com.dachen.imsdk.R.id.right_menu);
        mRightMenu.setOnClickListener(this);
        view.findViewById(R.id.btn_file).setOnClickListener(this);
        view.findViewById(R.id.back_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_file:
                ArchiveRecentUi.openUI(this, mGroupId);
                break;
            default:
                super.onClick(v);
                break;
        }
    }
    @Override
    protected List<MoreItem> getMorePanelData(int page) {
        List<MoreItem> items = new ArrayList<MoreItem>();
        items.add(new MoreItem(getString(R.string.chat_poto), R.drawable.im_tool_photo_button_bg));
        items.add(new MoreItem(getString(R.string.chat_camera), R.drawable.im_tool_camera_button_bg));
        items.add(new MoreItem(getString(R.string.chat_archive), R.drawable.im_tool_archive_icon_normal));
        return items;
    }

    @Override
    public void onPanelItemClick(int drawableId) {
        if(drawableId==R.drawable.im_tool_archive_icon_normal){
            clickArchive();
        }else{
            super.onPanelItemClick(drawableId);
        }
    }

    public static void openUI(Context context, String groupName, String groudId, String userId) {
        Intent intent = new Intent(context, Represent2RepresentChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groudId);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    public static void openUI(Context context, String groupName, String groudId, String userId,HashMap<String, Object> params) {
        Intent intent = new Intent(context, Represent2DoctorChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groudId);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_SHARE_PARAM, params);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GROUP_SETTING) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
    @Override
    protected void onRightMenuClick(View v) {
        if(groupPo==null)
            return;
        GroupChatSetingUI.openUIForResult(this, mGroupId, makeGroupInfo(), getClass().getSimpleName(), true, REQUEST_CODE_GROUP_SETTING);
    }


    public void onEventMainThread(AddGroupUserEvent event) {
        if(event.groupId.equals(mGroupId))
            finish();
    }

    @Override
    protected ImMsgHandler makeMsgHandler() {
        return new R2RMsgHandler(this);
    }

    private class R2RMsgHandler extends CompanyImMsgHandler {
        public R2RMsgHandler(ChatActivityV2 mContext) {
            super(mContext);
        }

        @Override
        public void onClickOtherUser(ChatMessagePo chatMessage, ChatAdapterV2 adapter) {
            List<CompanyContactListEntity> l=mCompanyDao.queryByUserId(chatMessage.fromUserId);
            if(l!=null&&l.size()>0){
                CompanyContactListEntity e=l.get(0);
                Intent intent = new Intent(Represent2RepresentChatActivity.this, ColleagueDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("peopledes",e);
                intent.putExtra("peopledes", bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    protected boolean sendMessage(ChatMessagePo chatMessage) {
        if(!userExist){
            ToastUtil.showToast(mThis,"对方已经离职，无法发送消息。");
            return false;
        }
        return super.sendMessage(chatMessage);
    }
}
