package com.dachen.dgroupdoctorcompany.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

import java.util.List;

/**
 * Created by Mcp on 2016/8/25.
 */
public class ChatGroupMenuAdapter extends BaseAdapter {
    public static final String INTENT_EXTRA_MSG_ID="msgId";
    public static final String INTENT_EXTRA_GROUP_ID="groupId";

    private Context mContext;
    private List<String> items;

    public ChatGroupMenuAdapter(Context context, List<String> items) {
        mContext=context;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.msg_menu_item,parent,false);
        }
        TextView tv= (TextView) convertView.findViewById(R.id.text_view);
        final String action= items.get(position);
        tv.setText(action);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(action==null)return;
//                if(action.equals(ITEM_DEL)){
//                }else if(action.equals(ITEM_TOP)){
//                }else if(action.equals(ITEM_NO_TOP)){
//                }
//                mDialog.dismiss();
//            }
//        });

        return convertView;
    }
}