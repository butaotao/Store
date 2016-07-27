package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.ColleagueDetailActivity;
import com.dachen.dgroupdoctorcompany.archive.ArchiveRecentUi;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.events.AddGroupUserEvent;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.MoreItem;
import com.dachen.imsdk.out.ImMsgHandler;
import com.dachen.imsdk.vchat.activity.VChatMemberActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp on 2016/2/24.
 */
public class RepresentGroupChatActivity extends AppBaseChatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onLoadHeaderLayout(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.im_header_represent2doctor, parent, false);
    }
    @Override
    protected void onHeaderLayoutLoaded(View view) {
        //        super.onHeaderLayoutLoaded(view);
        //        setRightMenuImageResource(R.drawable.about_group);
        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(getIntent().getStringExtra(INTENT_EXTRA_GROUP_NAME));
        view.findViewById(R.id.btn_file).setOnClickListener(this);
        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.right_menu).setOnClickListener(this);
    }


    @Override
    protected List<MoreItem> getMorePanelData(int page) {
        List<MoreItem> items = new ArrayList<MoreItem>();
        items.add(new MoreItem(getString(R.string.chat_poto), R.drawable.im_tool_photo_button_bg));
        items.add(new MoreItem(getString(R.string.chat_camera), R.drawable.im_tool_camera_button_bg));
        items.add(new MoreItem(getString(R.string.chat_archive), R.drawable.im_tool_archive_icon_normal));

        String num = com.dachen.medicine.config.UserInfo.getInstance(this).getIpNum();
//        if(!"1".equals(num))
//            items.add(new MoreItem("视频通话", R.drawable.video_blue));
        return items;
    }
    @Override
    public void onPanelItemClick(int drawableId) {
        if(drawableId==R.drawable.im_tool_archive_icon_normal){
            clickArchive();
        }else if(drawableId == R.drawable.video_blue){
            Intent i = new Intent(this, VChatMemberActivity.class);
            i.putExtra(VChatMemberActivity.INTENT_MEMBER_LIST, new ArrayList<>(mUserList));
            i.putExtra(INTENT_EXTRA_GROUP_ID, mGroupId);
            startActivity(i);
        }else{
            super.onPanelItemClick(drawableId);
        }
    }
    public static void openUI(Context context, String groupName, String groudId, ArrayList<UserInfo> userList) {
        Intent intent = new Intent(context, RepresentGroupChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groudId);
        // intent.putParcelableArrayListExtra(INTENT_EXTRA_GROUP_USER_LIST,
        // userList);
        intent.putExtra(INTENT_EXTRA_GROUP_USER_LIST, userList);
        context.startActivity(intent);
    }


    @Override
    protected void onRightMenuClick(View v) {
        if(groupPo==null)
            return;
        GroupChatSetingUI.openUIForResult(this, mGroupId, makeGroupInfo(), getClass().getSimpleName(), true, REQUEST_CODE_GROUP_SETTING);
    }

    @Override
    protected int chatType() {
        return CHAT_TYPE_GROUP;
    }

    public void onEventMainThread(AddGroupUserEvent event) {
        if(event.groupId.equals(mGroupId))
            finish();
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
                Intent intent = new Intent(RepresentGroupChatActivity.this, ColleagueDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("peopledes",e);
                intent.putExtra("peopledes", bundle);
                startActivity(intent);
            }
        }
    }
}
