package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.CompanysTitle;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/29.
 */
public class TextHorizonAdapter extends android.widget.BaseAdapter{
    public Context context;
    public ArrayList<CompanyDepment.Data.Depaments> headPicList;
    public TextHorizonAdapter(Context context, ArrayList<CompanyDepment.Data.Depaments> headPicList){
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
            convertView = View.inflate(context, R.layout.adapter_horizontext,null);
            holder = new ViewHolder();
            holder.ivview  = (TextView) convertView.findViewById(R.id.ivview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivview.setText(headPicList.get(position).name);
        return convertView;
    }
    public static class ViewHolder{
        TextView ivview;
    }
}
