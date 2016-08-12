package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.utils.TextViewUtils;

import java.util.List;

/**
 * Created by Burt on 2016/6/14.
 */
public class DepManagerAdapter extends android.widget.BaseAdapter{
    Context context;
    List<DepAdminsList> lists;
    public DepManagerAdapter(Context context,List<DepAdminsList> lists){
        this.context = context;
        this.lists = lists;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder = null ;
        DepAdminsList adminsList = lists.get(position);
        /*if (null!=convertView){
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }else {*/
            view = View.inflate(context, R.layout.adapter_depmanager,null);
            holder = new ViewHolder();
            holder.line2 = view.findViewById(R.id.line2);
            holder.textdes = (TextView) view.findViewById(R.id.textdes);
            holder.textdes2 = (TextView) view.findViewById(R.id.textdes2);
            holder.ll_all = (LinearLayout) view.findViewById(R.id.ll_all);
            holder.ll_tv_des = (LinearLayout) view.findViewById(R.id.ll_tv_des);
          /*  view.setTag(holder);
        }*/
        holder.line2.setVisibility(View.VISIBLE);
        if (position==(lists.size()-1)){
            holder.line2.setVisibility(View.GONE);
        }
        String depName = adminsList.orgName;
        String des = "";
        String s [] = null;
        boolean show = false;
        if (depName.contains("/")){
             s  = depName.split("/");
            if (s.length>2){
                for (int j=2;j<s.length-1;j++){
                    String k = s[j];
                    if (j==2){
                        des = k;
                    }else {
                        des =des +" · "+k;
                    }
                }
                show = true;
            }else {
                show = false;
            }
        }
        if (s!=null&&s.length>0&&show){
            if (!TextUtils.isEmpty(des)){
                des = des+" · ";
            }
            des = des.replace(".  .",".");
            holder.textdes.setText(des);
            holder.textdes2.setText(s[s.length -1]);
        }else {
            holder.textdes.setText(depName);
        }

        return view;
    }
    public static class ViewHolder{
        TextView textdes;
        TextView textdes2;
        View line2;
        LinearLayout ll_all;
        LinearLayout ll_tv_des;
    }
}
