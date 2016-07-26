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
import com.dachen.dgroupdoctorcompany.adapter.EidtColleagueAdapter;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Mcp on 2016/3/2.
 */
public class MyFavChatGroupAdapter extends BaseCustomAdapter<ChatGroupPo> {

    public MyFavChatGroupAdapter(Context context, List<ChatGroupPo> objects) {
        super(context, R.layout.im_session_message_listview, objects);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder= (ViewHolder) baseViewHolder;
        ChatGroupPo po=getItem(position);
        ImageLoader.getInstance().displayImage(po.gpic, holder.session_message_avatar_image);
        holder.session_message_unread_count.setVisibility(View.GONE);
        holder.session_message_nick_name.setText(po.name);
        int size=0;
        List<UserInfo> uList= JSON.parseArray(po.groupUsers,UserInfo.class);
        if(uList!=null){
            size=uList.size();
        }
        holder.session_message_content.setText(size+"人");
        holder.session_message_time.setText(TimeUtils.getTimeStateForCircle(po.updateStamp));
    }

    public  class ViewHolder extends BaseViewHolder {
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
    }
}
