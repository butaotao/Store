package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.ChoiceDep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/24.
 */
public class ChoiceAdpVertiAdapter extends android.widget.BaseAdapter{
   List<DepAdminsList> deps;
    Context context;
    public ChoiceAdpVertiAdapter(Context context, List<DepAdminsList> deps){
        this.deps = deps;
        this.context = context;
    }
    @Override
    public int getCount() {
        return deps.size();
    }

    @Override
    public Object getItem(int position) {
        return deps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        DepAdminsList dep = (DepAdminsList) getItem(position);
        if (convertView == null){
            convertView = View.inflate(context, R.layout.adapter_choicelist,null);
            holder = new ViewHolder();
            holder.tv_departchildren = (TextView) convertView.findViewById(R.id.tv_departchildren);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_departchildren.setText(dep.orgName);
        return convertView;
    }
    public class ViewHolder{
        TextView tv_departchildren;
    }
}
