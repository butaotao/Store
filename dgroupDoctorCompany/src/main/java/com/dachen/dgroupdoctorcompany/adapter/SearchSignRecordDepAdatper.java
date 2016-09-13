package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.UserInfos;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/7/1.
 */
public class SearchSignRecordDepAdatper extends android.widget.BaseAdapter{
    Context context;
    ArrayList<UserInfos> infos = new ArrayList<>();

    public SearchSignRecordDepAdatper(Context context){
        this.context = context;
    }

    public void addData(ArrayList<UserInfos> dataLists, boolean refresh){
        if(refresh){
            infos.clear();
            infos.addAll(dataLists);
        }else{
            infos.addAll(dataLists);
        }
    }

    public void clearData(){
        this.infos.clear();
    }

    public SearchSignRecordDepAdatper(Context context,ArrayList<UserInfos> infos){
        this.context = context;
        this.infos = infos;
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<infos.size()){
            return infos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfos info = (UserInfos) getItem(position);
        ViewHolder holder;
        if (null==convertView){
            convertView = View.inflate(context, R.layout.adapter_searchdeptrecord,null);
            holder = new ViewHolder();
            holder.iv_headicon = (ImageView) convertView.findViewById(R.id.iv_headicon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_frenddes = (TextView) convertView.findViewById(R.id.tv_frenddes);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CustomImagerLoader.getInstance().loadImage(holder.iv_headicon,info.headPic);
        holder.tv_name.setText(info.name);
        holder.tv_frenddes.setText(info.departmentNmae);
        return convertView;
    }
    public static class ViewHolder{
        ImageView iv_headicon;
        TextView tv_name;
        TextView tv_frenddes;
    }
}
