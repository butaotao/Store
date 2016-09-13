package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;

import java.util.List;

/**
 * Created by Burt on 2016/6/24.
 */
public class ChoiceAdpHorizonAdapter extends android.widget.BaseAdapter{
    List<DepAdminsList> deps;
    Context context;
    public ChoiceAdpHorizonAdapter(Context context,List<DepAdminsList> deps){
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
            convertView = View.inflate(context, R.layout.adapter_choicehorizon,null);

            holder = new ViewHolder();
            holder.tv_departparent = (TextView) convertView.findViewById(R.id.tv_departparent);
            holder.viewred = (View) convertView.findViewById(R.id.viewred);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_departparent.setText(dep.orgName);
        holder.viewred.setVisibility(View.GONE);
        holder.tv_departparent.setTextColor(context.getResources().getColor(R.color.color_666666));
        if (position == (getCount()-1)){
            holder.tv_departparent.setTextColor(context.getResources().getColor(R.color.color_3cbaff));
            holder.viewred.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    public class ViewHolder{
        TextView tv_departparent;
        View viewred;
    }
}
