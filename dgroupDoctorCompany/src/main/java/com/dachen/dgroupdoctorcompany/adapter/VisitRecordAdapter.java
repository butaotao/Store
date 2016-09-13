package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.BaseRecordActivity;
import com.dachen.dgroupdoctorcompany.entity.SignedRecords;
import com.dachen.dgroupdoctorcompany.entity.TogetherVisit;
import com.dachen.dgroupdoctorcompany.entity.VistRecorddata;
import com.dachen.dgroupdoctorcompany.views.HorizontalListView;
import com.dachen.medicine.common.utils.DisplayUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/5/19.
 */
public class VisitRecordAdapter extends BaseExpandableListAdapter {
    Activity context;
    ArrayList<SignedRecords>  managers = new ArrayList<>();
    boolean isVisit;
    public VisitRecordAdapter(Activity context){
        this.context = context;
    }

    public void clearData(){
        this.managers.clear();
    }

    public void addData(ArrayList<SignedRecords> managers,boolean refresh,boolean isVisit){
        this.isVisit = isVisit;
        if(refresh){
            this.managers.clear();
            this.managers.addAll(managers);
        }else{
            this.managers.addAll(managers);
        }
    }

    public VisitRecordAdapter(Activity context, ArrayList<SignedRecords> managers,boolean isVisit ){
        this.context = context;
        this.managers = managers;
        this.isVisit = isVisit;
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
        if (isVisit){
            if(managers.size()==0||null== managers.get(groupPosition)||null==managers.get(groupPosition).vistlist){
                return 0;
            }
            return managers.get(groupPosition).vistlist.size();
        }else  if(managers.size()==0||null== managers.get(groupPosition)||null==managers.get(groupPosition).listSig){
            return 0;
        }
        return managers.get(groupPosition).listSig.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        if (isVisit){
            return managers.get(groupPosition).vistlist.get(childPosition);
        }else {
            return managers.get(groupPosition).listSig.get(childPosition);
        }

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
        int state = 0;
        if (context instanceof BaseRecordActivity){
            BaseRecordActivity activitys = (BaseRecordActivity)(context);
            if (null!=activitys.state){
                state = activitys.state.getState();
            }
        }

        final SignedRecords manager = (SignedRecords) getGroup(groupPosition);
        View view;
        if (state ==3||state==4){
            view = View.inflate(context, R.layout.layout_parentrecord,null);
            TextView tv_err = (TextView) view.findViewById(R.id.tv_err);
            TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_line1);
            String day = TimeUtils.getTimeRecordDay(manager.time);
            String week = TimeUtils.getWeek(manager.time);
            tv_data.setText(day+"  "+week);
            tv_err.setVisibility(View.GONE);
            if (null!=manager.state&&manager.state.equals("2")){
                tv_err.setVisibility(View.VISIBLE);
            }
            view.findViewById(R.id.rl_tip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    return;
                }
            });
        }else {
            String today = TimeUtils.getTimeDay();
            view = View.inflate(context, R.layout.adapter_visitrecord_parent,null);
            final ImageView iv_people = (ImageView) view.findViewById(R.id.iv_people);
            TextView textView = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_alert = (TextView) view.findViewById(R.id.tv_alert);
            RelativeLayout rl_emptview = (RelativeLayout) view.findViewById(R.id.rl_emptview);
            RelativeLayout rl_parents = (RelativeLayout) view.findViewById(R.id.rl_parents);
            CustomImagerLoader.getInstance().loadImage(iv_people, manager.headPic);
            textView.setText(manager.name);
            rl_emptview.setVisibility(View.GONE);
            if (groupPosition!=0){
                rl_emptview.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(manager.state)&&manager.state.equals("2")){
                tv_alert.setText("异常");
                tv_alert.setVisibility(View.VISIBLE);
            }else {
                tv_alert.setVisibility(View.GONE);
            }
            rl_parents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view =   View.inflate(context, R.layout.adapter_siginrecord_children, null);
        if (getChild(groupPosition,childPosition) instanceof TogetherVisit){
            TogetherVisit s = (TogetherVisit) getChild(groupPosition, childPosition);

            view = View.inflate(context, R.layout.adapter_siginrecord_children, null);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            TextView tv_gowork = (TextView) view.findViewById(R.id.tv_gowork);
            TextView tv_place = (TextView) view.findViewById(R.id.tv_place);
            RelativeLayout line3 = (RelativeLayout) view.findViewById(R.id.line3);
            RelativeLayout line_top = (RelativeLayout) view.findViewById(R.id.line_top);
            RelativeLayout rl_line1 = (RelativeLayout) view.findViewById(R.id.rl_line1);
            if (!TextUtils.isEmpty(s.time)){
                tv_time.setText(s.time);
            }else {
                tv_time.setText(TimeUtils.getTimesHourMinute( s.longTime));
            }

            if (null!=s.tag&&!TextUtils.isEmpty(s.tag.get(0))){
                tv_gowork.setText(s.tag.get(0));
                tv_gowork.setVisibility(View.VISIBLE);
            }else {
                tv_gowork.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_place.getLayoutParams();
                params.topMargin = DisplayUtil.dip2px(context.getApplicationContext(),3);
                tv_place.setLayoutParams(params);
            }

            rl_line1.setVisibility(View.GONE);
            if (childPosition==0){
                line_top.setVisibility(View.INVISIBLE);
                rl_line1.setVisibility(View.VISIBLE);
            }
            line3.setVisibility(View.GONE);
            if (childPosition==(getChildrenCount(groupPosition)-1)){
                line3.setVisibility(View.VISIBLE);
            }
            tv_place.setText(s.address);
        }else if (getChild(groupPosition,childPosition) instanceof VistRecorddata){
            VistRecorddata data = (VistRecorddata) getChild(groupPosition, childPosition);
            view = View.inflate(context, R.layout.adapter_visitrecord_children, null);

            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            TextView tv_gowork = (TextView) view.findViewById(R.id.tv_gowork);
            TextView tv_place = (TextView) view.findViewById(R.id.tv_place);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            RelativeLayout itemline = (RelativeLayout) view.findViewById(R.id.itemline);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_horizon);
            RelativeLayout rl_line1 = (RelativeLayout) view.findViewById(R.id.rl_line1);
            RelativeLayout line2 = (RelativeLayout) view.findViewById(R.id.line2);
            RelativeLayout line_top = (RelativeLayout) view.findViewById(R.id.line_top);
            rl_line1.setVisibility(View.GONE);
            if (childPosition==0){
                line_top.setVisibility(View.INVISIBLE);
                rl_line1.setVisibility(View.VISIBLE);
            }
            itemline.setVisibility(View.GONE);
            if (childPosition==(getChildrenCount(groupPosition)-1)){
                itemline.setVisibility(View.VISIBLE);
            }
            line2.setVisibility(View.VISIBLE);
            tv_time.setText(TimeUtils.getTimesHourMinute( data.time));
            tv_name.setText(data.doctorName);
            HorizontalListView horizonlistview = (HorizontalListView) view.findViewById(R.id.horizonlistview);
            rl.setVisibility(View.VISIBLE);
            if (null!=data.headPicList&&data.headPicList.size()>0){
                ImageHorizonAdapter adapter = new ImageHorizonAdapter(context,data.headPicList);

                horizonlistview.setAdapter(adapter);
                SetListViewHeight(horizonlistview);
                horizonlistview.setVisibility(View.VISIBLE);
            }else {
                line2.setVisibility(View.GONE);
                horizonlistview.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.type)&&data.type.equals("2")){
                tv_gowork.setText("协同");
            }else {
                tv_gowork.setVisibility(View.GONE);
            }

            tv_place.setText(data.addressName);


        }




        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
                                     int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    /**
     * ListView根据项数的大小自动改变高度
     */
    private void SetListViewHeight(HorizontalListView listviewmanagerdepartment) {
        ListAdapter listAdapter = listviewmanagerdepartment.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listviewmanagerdepartment);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
//
        ViewGroup.LayoutParams params = listviewmanagerdepartment.getLayoutParams();
        params.height = totalHeight ;
        ((ViewGroup.MarginLayoutParams)params).setMargins(0, 0, 0, 0);
        listviewmanagerdepartment.setLayoutParams(params);
    }
}
