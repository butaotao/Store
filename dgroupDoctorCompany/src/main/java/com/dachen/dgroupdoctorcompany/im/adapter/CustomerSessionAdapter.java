package com.dachen.dgroupdoctorcompany.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dachen.common.adapter.CommonAdapter;
import com.dachen.common.adapter.ViewHolder;
import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 客户会话列表Adapter
 * 
 * @author gaozhuo
 * @date 2015年11月4日
 *
 */
public class CustomerSessionAdapter extends CommonAdapter<ChatGroupPo> {

	public CustomerSessionAdapter(Context context, List<ChatGroupPo> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void bind(ViewHolder holder, ChatGroupPo data) {
		// 头像
		ImageLoader.getInstance().displayImage(data.gpic,
				(ImageView) holder.getView(R.id.session_message_avatar_image));
		// 昵称
		holder.setText(R.id.session_message_nick_name, data.name);
		// 最新一条消息
		holder.setText(R.id.session_message_content, data.lastMsgContent);
		// 时间
		holder.setText(R.id.session_message_time, TimeUtils.getTimeState(data.updateStamp));
		// 未读消息数
		if (data.unreadCount <= 0) {
			holder.setVisibility(R.id.session_message_unread_count, View.GONE);
		} else {
			holder.setVisibility(R.id.session_message_unread_count, View.VISIBLE);
			holder.setText(R.id.session_message_unread_count, data.unreadCount + "");
		}
	}

}
