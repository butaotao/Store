package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.TogetherVisit;
import com.dachen.medicine.net.CustomImagerLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/29.
 */
public class ImageHorizonAdapter extends android.widget.BaseAdapter{
    public Context context;
    public ArrayList<String> headPicList;
    public ImageHorizonAdapter(Context context,ArrayList<String> headPicList){
        this.context = context;
        this.headPicList = headPicList;
    }
    @Override
    public int getCount() {
        return headPicList.size();
    }

    @Override
    public Object getItem(int position) {
        return headPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null){
            convertView = View.inflate(context, R.layout.adapter_horizonimage,null);
            holder = new ViewHolder();
            holder.imageview  = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CustomImagerLoader.getInstance().loadImage(holder.imageview,headPicList.get(position));
        return convertView;
    }
    public static class ViewHolder{
        ImageView imageview;
    }
}
