package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/3/2.
 */
/*
    Suneee Android Client, FineAdapter
    Copyright (c) 2015 Suneee Tech Company Limited
*/

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

/**
 * [医生集团适配器]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-5-23
 *
 **/
public class CircleCreateGroupAdapter extends android.widget.BaseAdapter {

    private ViewHolder holder;
    List<BaseSearch> listdata;
    Context context;
    public CircleCreateGroupAdapter(Context context, List<BaseSearch> data) {
        this.context = context;
        this.listdata =data;
    }


    class ViewHolder {
        ImageView img;


    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.circle_create_group_item, null);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BaseSearch data = listdata.get(position);
        if (data instanceof CompanyContactListEntity){
            CompanyContactListEntity datas = (CompanyContactListEntity)data;
            if (TextUtils.isEmpty(datas.headPicFileName)){
                holder.img.setBackgroundResource(R.drawable.ic_default_head);
            }else {
//                ImageLoader.getInstance().displayImage(""+datas.headPicFileName, holder.img);
                CustomImagerLoader.getInstance().loadImage(holder.img,""+datas.headPicFileName);
            }

        }
        //ImageLoader.getInstance().displayImage(data.getHeadPicFileName(), holder.img, CommonUitls.getHeadOptions());

        return convertView;
    }
}


