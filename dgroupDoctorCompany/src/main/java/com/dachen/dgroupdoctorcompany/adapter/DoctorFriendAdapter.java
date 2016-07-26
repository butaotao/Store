package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.DoctorDetailActivity;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalManagers;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.net.CustomImagerLoader;

import java.io.Serializable;

/**
 * Created by Burt on 2016/5/20.
 */
public class DoctorFriendAdapter extends BaseExpandableListAdapter {
    Context context;
    HospitalManagers managers;
    ExpandableListView expandableListView;
    public DoctorFriendAdapter(Context context,HospitalManagers managers,ExpandableListView expandableListView){
        this.context = context;
        this.managers = managers;
        this.expandableListView = expandableListView;
    }
    //重写ExpandableListAdapter中的各个方法
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return managers.data.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return managers.data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return managers.data.get(groupPosition).doctors.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return managers.data.get(groupPosition).doctors.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        HospitalDes des = (HospitalDes) getGroup(groupPosition);
        View view = View.inflate(context, R.layout.adapter_doctorfriends_parents,null);
        TextView tv_hospitalname = (TextView) view.findViewById(R.id.tv_hospitalname);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_num.setText(des.doctors.size()+"");
        tv_hospitalname.setText(des.name);
      /*  final RelativeLayout rl_parents = (RelativeLayout) view.findViewById(R.id.rl_parents);
        rl_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition);
                }
                notifyDataSetChanged();
                //
            }
        });*/
       /* if (expandableListView.isGroupExpanded(groupPosition)) {
            iv_down_close.setBackgroundResource(R.drawable.down_close);
        } else {
            iv_down_close.setBackgroundResource(R.drawable.down_open);
        }*/

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Doctor doctor = (Doctor) getChild(groupPosition, childPosition);

        View view = View.inflate(context,R.layout.adapter_doctorfriends_children,null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_customervisit = (TextView) view.findViewById(R.id.tv_customervisit);
        TextView tv_frenddes = (TextView) view.findViewById(R.id.tv_frenddes);
        TextView tv_customervisitposition = (TextView) view.findViewById(R.id.tv_customervisitposition);
        ImageView iv_headicon = (ImageView) view.findViewById(R.id.iv_headicon);
        RelativeLayout rl_companycontact = (RelativeLayout) view.findViewById(R.id.rl_companycontact);
        tv_name.setText(doctor.name);

        String url = "";
        if (null!=doctor.headPicFileName){
            url = doctor.headPicFileName;
        }
        //ImageLoader.getInstance().displayImage(url, hoder.iv_headicon);
        CustomImagerLoader.getInstance().loadImage(iv_headicon, url,
                R.drawable.head_icons_company, R.drawable.head_icons_company);
        tv_customervisit.setText(doctor.departments);
        tv_frenddes.setText(doctor.hospital);
        tv_customervisitposition.setText(doctor.title);
        rl_companycontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctordetail", (Serializable) getChild(groupPosition,childPosition));
                intent.putExtra("doctordetail", bundle);

                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
                                     int childPosition) {
      /*  Intent intent = new Intent(context, DoctorDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("doctordetail", (Serializable) getChild(groupPosition,childPosition));
        intent.putExtra("doctordetail", bundle);

        context.startActivity(intent);*/
        // TODO Auto-generated method stub
        return true;
    }
}
