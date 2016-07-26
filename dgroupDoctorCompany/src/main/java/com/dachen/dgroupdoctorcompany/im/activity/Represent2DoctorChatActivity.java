package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.DoctorDetailActivity;
import com.dachen.dgroupdoctorcompany.archive.ArchiveRecentUi;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.MoreItem;
import com.dachen.imsdk.out.ImMsgHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp on 2016/2/24.
 */
public class Represent2DoctorChatActivity extends AppBaseChatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!CommonUitls.isExitUser(this,mUserId))
            mChatBottomView.setVisibility(View.GONE);
    }

    @Override
    protected View onLoadHeaderLayout(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.im_header_represent2doctor, parent, false);
    }
    @Override
    protected void onHeaderLayoutLoaded(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(getIntent().getStringExtra(INTENT_EXTRA_GROUP_NAME));
        view.findViewById(R.id.btn_file).setOnClickListener(this);
        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.right_menu).setOnClickListener(this);
    }

    @Override
    protected void onRightMenuClick(View v) {
        if(groupPo==null)
            return;
        GroupChatSetingUI.openUIForResult(this, mGroupId, makeGroupInfo(), getClass().getSimpleName(), true, REQUEST_CODE_GROUP_SETTING,true);
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
        Intent intent = new Intent(context, Represent2DoctorChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groudId);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected ImMsgHandler makeMsgHandler() {
        return new R2DMsgHandler(this);
    }

    private class R2DMsgHandler extends CompanyImMsgHandler{
        public R2DMsgHandler(ChatActivityV2 mContext) {
            super(mContext);
        }

        @Override
        public void onClickOtherUser(ChatMessagePo chatMessage, ChatAdapterV2 adapter) {
            List<Doctor> l=mDoctorDao.queryByUserId(chatMessage.fromUserId);
            if(l!=null&&l.size()>0){
                Doctor d=l.get(0);
                Intent intent = new Intent(Represent2DoctorChatActivity.this, DoctorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctordetail",d);
                intent.putExtra("doctordetail", bundle);
                startActivity(intent);
            }
        }
    }
}
