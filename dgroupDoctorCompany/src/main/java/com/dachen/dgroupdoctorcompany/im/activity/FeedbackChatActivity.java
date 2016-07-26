package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.imsdk.entity.MoreItem;
import com.dachen.imsdk.out.ImMsgHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈聊天界面
 * 
 * @author gaozhuo
 * @date 2015年11月4日
 *
 */
public class FeedbackChatActivity extends AppBaseChatActivity {

	private static final String TAG = FeedbackChatActivity.class.getSimpleName();
	public static final String INTENT_EXTRA_GROUP_NAME = "intent_extra_group_name";
	public static final String INTENT_EXTRA_IS_ADMIN = "intent_extra_is_admin";
	public static final int observer_msg_what_user_info_change = 111;
	private boolean isAdmin;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(getIntent().getStringExtra(INTENT_EXTRA_GROUP_NAME));
		isAdmin=getIntent().getBooleanExtra(INTENT_EXTRA_IS_ADMIN, false);
		mChatContentView.setChatType(chatType());
		// 隐藏右侧菜单
		setRightMenuVisibility(View.GONE);

	}
	
	@Override
	protected int chatType() {
		if(isAdmin){
			return CHAT_TYPE_GROUP;
		}else{
			return CHAT_TYPE_PUB;
		}
	}

	/**
	 * 单聊入口
	 * 
	 * @param context
	 * @param groupName
	 * @param groudId
	 * @param userId
	 */
	public static void openUI(Context context, String groupName, String groudId, String userId) {
		openUI(context, groupName, groudId, userId, false);
	}
	public static void openUI(Context context, String groupName, String groudId, String userId,boolean isAdmin) {
		Intent intent = new Intent(context, FeedbackChatActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
		intent.putExtra(INTENT_EXTRA_GROUP_ID, groudId);
		intent.putExtra(INTENT_EXTRA_USER_ID, userId);
		intent.putExtra(INTENT_EXTRA_IS_ADMIN, isAdmin);
		context.startActivity(intent);
	}

	@Override
	protected void onRightMenuClick(View v) {
		//GroupChatSetingUI.openUI(this, mGroupId);
	}

	/**
	 * 为底部+号提供数据
	 */
	@Override
	protected List<MoreItem> getMorePanelData(int page) {
		List<MoreItem> items = new ArrayList<MoreItem>();
		items.add(new MoreItem(getString(R.string.chat_poto), R.drawable.im_tool_photo_button_bg));
		items.add(new MoreItem(getString(R.string.chat_camera), R.drawable.im_tool_camera_button_bg));
		return items;
	}

	@Override
	protected boolean showByRole() {
		return true;
	}

	@Override
	protected ImMsgHandler makeMsgHandler() {
		return new CompanyImMsgHandler(this){
			@Override
			public boolean menuHasRetract() {
				return false;
			}
		};
	}
}
