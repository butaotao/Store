package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSelfPartHospitalActivity;
import com.dachen.dgroupdoctorcompany.activity.HospitalManagerActivity;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalManager;
import com.dachen.dgroupdoctorcompany.entity.HospitalManagers;
import com.dachen.dgroupdoctorcompany.entity.HospitalMangerData;
import com.dachen.dgroupdoctorcompany.entity.MedicineManager;
import com.dachen.medicine.common.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/5/19.
 */
public class HospitalManagerChildrenAdapter extends BaseExpandableListAdapter {
        Context context;
    ArrayList<HospitalMangerData>  managers;
    ExpandableListView expandableListView;
    public HospitalManagerChildrenAdapter(Context context,ArrayList<HospitalMangerData> managers,ExpandableListView expandableListView){
        this.context = context;
        this.managers = managers;
        this.expandableListView = expandableListView;
    }
    //重写ExpandableListAdapter中的各个方法
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return managers.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return managers.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        if (managers.size()==0||null== managers.get(groupPosition)||null==managers.get(groupPosition).hospitalList){
            return 0;
        }
        return managers.get(groupPosition).hospitalList.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub

        return managers.get(groupPosition).hospitalList.get(childPosition);
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
        final HospitalMangerData manager = (HospitalMangerData) getGroup(groupPosition);
        View view = View.inflate(context, R.layout.adapter_hospitaimanagers_parents,null);
        final ImageView iv_down_close = (ImageView) view.findViewById(R.id.iv_down_close);
        TextView textView = (TextView) view.findViewById(R.id.tv_mediename);
        final TextView tv_edit = (TextView) view.findViewById(R.id.tv_edit);
        ImageView guanli_select = (ImageView) view.findViewById(R.id.guanli_select);
        View line1 = view.findViewById(R.id.line1);
        int height = (int)context.getResources().getDimension(R.dimen.adapterhospitalmanagerweight);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, height);
        view.setLayoutParams(lp);
      /*  line1.setVisibility(View.VISIBLE);
        if (groupPosition==0){
            line1.setVisibility(View.GONE);
        }*/
        iv_down_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition);

                }
                notifyDataSetChanged();
            }
        });
        if (expandableListView.isGroupExpanded(groupPosition)) {
            iv_down_close.setBackgroundResource(R.drawable.down_close);
            tv_edit.setTextColor(context.getResources().getColor(R.color.color_3cbaff));
            guanli_select.setBackgroundResource(R.drawable.guanli_select);
        } else {
            tv_edit.setTextColor(context.getResources().getColor(R.color.color_888888));
            iv_down_close.setBackgroundResource(R.drawable.down_open);
            guanli_select.setBackgroundResource(R.drawable.guanli_notselect);
        }
        textView.setText(manager.goodsGroupName);
        view.findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddSelfPartHospitalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("manager",manager);
                        intent.putExtra("manager", bundle);
                intent.putExtra("fromactivity","HospitalManagerActivity");
                context.startActivity(intent);
                HospitalManagerActivity.refresh = 1;
            }
        });
        view.findViewById(R.id.tv_mediename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        HospitalDes s = (HospitalDes) getChild(groupPosition, childPosition);

        View view = View.inflate(context, R.layout.adapter_hospitaimanagers_children, null);
        View view1 = view.findViewById(R.id.line1);
        View view2 = view.findViewById(R.id.line2);
        int height = (int)context.getResources().getDimension(R.dimen.adapterhospitalmanagerweight);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, height);
        view.setLayoutParams(lp);
        if (childPosition==managers.get(groupPosition).hospitalList.size()-1){
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
        }else {
            view2.setVisibility(View.GONE);
            view1.setVisibility(View.VISIBLE);
        }
        TextView tv = (TextView) view.findViewById(R.id.tv_haospital);
        tv.setText(s.name);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
                                     int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
}
