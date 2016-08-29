package com.dachen.dgroupdoctorcompany.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dachen.dgroupdoctorcompany.activity.WebActivityForCompany;
import com.dachen.dgroupdoctorcompany.im.utils.CompanyImMsgHandler;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.ImgTextMsgV2;
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
public class PublicNotifyActivity extends AppBaseChatActivity {

	private static final String TAG = PublicNotifyActivity.class.getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(getIntent().getStringExtra(INTENT_EXTRA_GROUP_NAME));
		// 隐藏右侧菜单
		setRightMenuVisibility(View.GONE);

	}
	
	@Override
	protected int chatType() {
		return CHAT_TYPE_PUB;
	}

	/**
	 * 单聊入口
	 * 
	 * @param context
	 * @param groupName
	 */
	public static void openUI(Context context, String groupName,String groupId) {
		Intent intent = new Intent(context, PublicNotifyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(INTENT_EXTRA_GROUP_NAME, groupName);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
		context.startActivity(intent);
	}

	/**
	 * 为底部+号提供数据
	 */
	@Override
	protected List<MoreItem> getMorePanelData(int page) {
		List<MoreItem> items = new ArrayList<MoreItem>();
		return items;
	}

	@Override
	protected ImMsgHandler makeMsgHandler() {
		return new CompanyImMsgHandler(this){
			@Override
			public boolean menuHasRetract() {
				return false;
			}

			@Override
			public void onClickNewMpt16(ChatMessagePo chatMessage, ChatAdapterV2 adapter, View v) {
				ImgTextMsgV2 mpt=getMptInMulti(chatMessage);
				if (mpt == null) {
					return;
				}
				final String url = mpt.url;
				if(TextUtils.isEmpty(url))return;
				Intent intent = new Intent(mContext, WebActivityForCompany.class);
				intent.putExtra("url", url).putExtra(WebActivityForCompany.INTENT_NO_CACHE, true).putExtra(WebActivityForCompany.INTENT_SHOW_TITLE,true);
				mContext.startActivity(intent);
			}
		};
	}
}
