package com.dachen.dgroupdoctorcompany.im.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.BaseCustomAdapter;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.imsdk.consts.SessionGroupId;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.db.po.ChatGroupPo.ChatGroupNotifyParam;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;

/**
 * 会话列表适配器
 */
public class SessionListAdapterV2 extends BaseCustomAdapter<ChatGroupPo> {

	private static final String TAG = SessionListAdapterV2.class.getSimpleName();

	public SessionListAdapterV2(Context context, int resId) {
		super(context, resId);
	}

	public SessionListAdapterV2(Context context, int resId, List<ChatGroupPo> objects) {
		super(context, resId, objects);
	}

	@Override
	protected BaseViewHolder getViewHolder() {
		return new ViewHolder();
	}

	protected void setAvatarImage(ViewHolder holder, ChatGroupPo group) {
		if (group.type ==ChatGroupPo.TYPE_DOUBLE|| group.type == ChatGroupPo.TYPE_MULTI||"pub_news".equals(group.bizType)||"pub_customer".equals(group.bizType) ) {
			// 双人或者多人会话
			ImageLoader.getInstance().displayImage(group.gpic, holder.session_message_avatar_image, CompanyApplication.mAvatarRoundImageOptions);
		}else if (group.groupId.equals(SessionGroupId.auth_request_doctor) ||group.groupId.equals(SessionGroupId.auth_request_patient)) {
			// 好友验证请求
			holder.session_message_avatar_image.setImageResource(R.drawable.new_friends);
		} else if (group.groupId.equals(SessionGroupId.system_notification)) {
			// 系统通知
			holder.session_message_avatar_image.setImageResource(R.drawable.system_notification);
		} else {
			// 其它的
			holder.session_message_avatar_image.setImageResource(R.drawable.avatar_normal);
		}
//		if (po.clickType == SessionClickType.chat_double || po.clickType == SessionClickType.chat_multi ||po.groupType.equals("pub_news")||po.groupType.equals("pub_002")) {
//			// 双人或者多人会话
//			ImageLoader.getInstance().displayImage(po.avatarImageUri, holder.session_message_avatar_image);
//		} else if (po.clickType == SessionClickType.auth_request) {
//			// 好友验证请求
//			holder.session_message_avatar_image.setImageResource(R.drawable.new_friend);
//		} else if (po.clickType == SessionClickType.backlog) {
//			// 系统通知
//			holder.session_message_avatar_image.setImageResource(R.drawable.system_notification);
//		} else {
//			// 其它的
//			holder.session_message_avatar_image.setImageResource(R.drawable.avatar_normal);
//		}
	}
	/**
	 * 设置名称
	 */
	protected void setNickName(ViewHolder holder, String text) {
		holder.session_message_nick_name.setText( text );
	}

	/**
	 * 设置最新的一条消息
	 */
	protected void setMessageContent(ViewHolder holder, String text) {
		holder.session_message_content.setText( text );
	}

	/**
	 * 设置最新消息的时间
	 */
	protected void setMessageTime(ViewHolder holder, long time) {
		holder.session_message_time.setText( TimeUtils.getTimeStateForCircle(time) );
	}

	/**
	 * 设置未读消息数量
	 */
	protected void setUnReadMessageCount(ViewHolder holder, int unReadCount ) {
		holder.session_message_unread_count.setText( String.valueOf(unReadCount) );
		if (unReadCount <= 0) {
			holder.session_message_unread_count.setVisibility(View.GONE);
		}else{
			holder.session_message_unread_count.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void fillValues(BaseViewHolder baseViewHolder, int position) {

		ViewHolder holder = (ViewHolder) baseViewHolder;

		final ChatGroupPo group = getItem(position);

		//		Log.e(TAG, "getName:"+msg.name);
		//		Log.e(TAG, "getUserid:"+msg.userIds);
		//		Log.e(TAG, "getUnReadMsgCount:"+msg.unReadMessageNumber);
		//		Log.e(TAG, "msg.toString:"+msg.toString());

		// 设置头像
		setAvatarImage(holder, group);

		// 更新姓名
		setNickName(holder, group.name);

		// 设置最新的一条消息
		setMessageContent(holder, group.lastMsgContent);

		// 设置最新消息的时间
		setMessageTime(holder, group.updateStamp);

		// 设置未读消息数量
		setUnReadMessageCount(holder, group.unreadCount);

		if(position == getCount()-1){
			holder.layout_line.setVisibility(View.GONE);
			holder.layout_line1.setVisibility(View.VISIBLE);
		}else{
			holder.layout_line.setVisibility(View.VISIBLE);
			holder.layout_line1.setVisibility(View.GONE);
		}

        if(group.top==0){
            holder.layout_item.setBackgroundResource(R.drawable.selector_list_item_bg);
        }else{
            holder.layout_item.setBackgroundResource(R.drawable.selector_chat_top_item_bg);
        }
		holder.tvAt.setVisibility(View.GONE);
		ChatGroupNotifyParam notifyP= JSON.parseObject(group.notifyParam,ChatGroupNotifyParam.class);
		if(notifyP==null)return;
		if(notifyP.notify_type==1){
			holder.tvAt.setVisibility(View.VISIBLE);
		}

	}

	public static class ViewHolder extends BaseViewHolder {
		@Nullable
		@Bind(R.id.session_message_avatar_image)
		public ImageView session_message_avatar_image; // 头像
		@Nullable
		@Bind(R.id.session_message_unread_count)
		public TextView session_message_unread_count; // 头像右上角未读的消息条数
		@Nullable
		@Bind(R.id.session_message_nick_name)
		public TextView session_message_nick_name; // 名称
		@Nullable
		@Bind(R.id.session_message_content)
		public TextView session_message_content; // 消息内容
		@Nullable
		@Bind(R.id.session_message_time)
		public TextView session_message_time; // 时间
		@Bind(R.id.tv_at)
		public TextView tvAt; // AT

		@Bind(R.id.layout_line)
		public View layout_line; // AT

		@Bind(R.id.layout_line1)
		public View layout_line1; // AT
		@Bind(R.id.layout_item)
		public View layout_item; // AT
	}

}
