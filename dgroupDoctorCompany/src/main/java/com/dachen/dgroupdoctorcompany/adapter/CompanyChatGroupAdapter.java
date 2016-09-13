package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.adapter.ViewHolder;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.medicine.net.CustomImagerLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

//import com.dachen.imsdk.R;



/**
 * @项目名 MedicineProject
 * @Author: user on 2016/8/1120:02.
 * @描述 TODO
 */

public class CompanyChatGroupAdapter extends android.widget.BaseAdapter {
    private List<ChatGroupPo> mGroupList;
    private Context mContext;

    public CompanyChatGroupAdapter(Context mContext, List<ChatGroupPo> mGroupList) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
    }

    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=ViewHolder.get(mContext,convertView,parent, R.layout.chat_group_items,position);
        ImageView ivHead=holder.getView(R.id.session_message_avatar_image);
        TextView tvUnread=holder.getView(R.id.session_message_unread_count);
        TextView tvName=holder.getView(R.id.session_message_nick_name); // 名称
       // TextView tvContent=holder.getView(R.id.session_message_content); // 消息内容
       // TextView tvTime=holder.getView(R.id.session_message_time); // 时间

        ChatGroupPo group=mGroupList.get(position);
        CustomImagerLoader.getInstance().loadImage(ivHead,group.gpic,R.drawable.ic_default_head,R.drawable.ic_default_head);

        if(group.unreadCount==0){
            tvUnread.setVisibility(View.INVISIBLE);
        }else{
            tvUnread.setVisibility(View.VISIBLE);
            tvUnread.setText(group.unreadCount+"");
        }
        tvName.setText(group.name);
        //tvContent.setText(group.lastMsgContent);
       // tvTime.setText(TimeUtils.getTimeStateForCircle(group.updateStamp));
        return holder.getConvertView();
    }

}
