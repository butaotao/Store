package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.dachen.dgroupdoctorcompany.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/4/1.
 */
public class AddressListAdapter extends android.widget.BaseAdapter {
    private List<PoiItem> data;
    private Context       mContext;

    public AddressListAdapter(Context context, ArrayList<PoiItem>listData){
        this.mContext = context;
        this.data = listData;
    }
    @Override
    public int getCount() {
        if(null == data){
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if(null == data){
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_select,null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.ivSelected);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(null != data){
            PoiItem poiItem = data.get(position);
            if(null != poiItem){
                String name = poiItem.getTitle();
                String city = poiItem.getCityName();
                String address = poiItem.getAdName();
                String snippet = poiItem.getSnippet();
                if(!TextUtils.isEmpty(name)){
                    viewHolder.tvName.setText(name);
                }else{
                    viewHolder.tvName.setText(city+address+snippet);
                }

                viewHolder.tvAddress.setText(city+address+snippet);
                if(poiItem.isIndoorMap()){
                    viewHolder.ivSelected.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ivSelected.setVisibility(View.GONE);
                }
//                viewHolder.ivSelected.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private class ViewHolder{
        public TextView      tvName;
        public TextView      tvAddress;
        public ImageView     ivSelected;
    }
}
