package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weiwei on 2016/4/8.
 */
public class SignInListAdapter extends BaseExpandableListAdapter  {
    private Context mContext;
    private List<SignInList.Data.DataList> mDataLists = new ArrayList<>();

    public SignInListAdapter(Context context){
        this.mContext = context;
    }

    public void addData(List<SignInList.Data.DataList> dataLists,boolean refresh){
        if(refresh){
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        }else{
            mDataLists.addAll(dataLists);
        }
    }

    public SignInListAdapter(Context context,ArrayList<SignInList.Data.DataList>dataLists){
        this.mContext = context;
        this.mDataLists.clear();
        this.mDataLists.addAll(dataLists);
    }
    // 返回父列表个数
    @Override
    public int getGroupCount() {
        return mDataLists.size();
    }

    // 返回子列表个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataLists.get(groupPosition).listVisitVo.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataLists.get(groupPosition).listVisitVo.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        GroupHolder groupHolder;
        if(null == convertView){
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sign_in_group,null);
            groupHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (GroupHolder) convertView.getTag();
        }
        SignInList.Data.DataList dataList = mDataLists.get(groupPosition);
        if(null != dataList){
            groupHolder.tvTime.setText(dataList.time);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ChildHolder childHolder;
     //   if(null == convertView){
            childHolder  = new ChildHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sign_in_child,null);
            childHolder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            childHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            childHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            childHolder.vRemark = (LinearLayout) convertView.findViewById(R.id.vRemark);
            childHolder.tvRemark = (TextView) convertView.findViewById(R.id.tvRemark);
            childHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            childHolder.tvLable = (TextView) convertView.findViewById(R.id.tvLable);
            convertView.setTag(childHolder);
       /* }else {
            childHolder  = (ChildHolder) convertView.getTag();
        }*/
        SignInList.Data.DataList dataList = mDataLists.get(groupPosition);

        if(null!=dataList){
            SignInList.Data.DataList.ListVisitVo listVisitVo = dataList.listVisitVo.get(childPosition);
            if(null != listVisitVo){
                String type = listVisitVo.type;
                String headPic = listVisitVo.headPic;
                String doctorname = listVisitVo.doctorName;
                String remark = listVisitVo.remark;
                long time = listVisitVo.time;

                if("1".equals(type)){//如果是上班签到
                    childHolder.ivPicture.setBackgroundResource(R.drawable.icon_sign);
                    childHolder.tvName.setText("考勤打卡");
                    String address = listVisitVo.address;
                    childHolder.tvAddress.setText(address);

                    List<String> listLable = listVisitVo.singedTag;
                    if(null!=listLable && listLable.size()>0){
                        childHolder.tvLable.setVisibility(View.VISIBLE);
                        childHolder.tvLable.setText(listLable.get(0));
                    }else{
                        childHolder.tvLable.setVisibility(View.GONE);
                    }
                }else if("0".equals(type)){//独立拜访
                    String address = listVisitVo.addressName;
                    if(TextUtils.isEmpty(address)){
                        address = listVisitVo.address;
                    }
                    childHolder.tvAddress.setText(address);
                    childHolder.ivPicture.setBackgroundResource(R.drawable.icon_signle_visit);
//                         CustomImagerLoader.getInstance().loadImage(childHolder.ivPicture, headPic,
//                                R.drawable.baifang, R.drawable.baifang);

                    if(TextUtils.isEmpty(listVisitVo.doctorName)){
                        childHolder.tvName.setText("不记名拜访");
                    }else{
                        childHolder.tvName.setText("拜访 "+doctorname);
                    }

                    childHolder.tvLable.setVisibility(View.GONE);

                }else if("2".equals(type)){//协同拜访
                    String address = listVisitVo.addressName;
                    if(TextUtils.isEmpty(address)){
                        address = listVisitVo.address;
                    }
                    childHolder.tvAddress.setText(address);
                    childHolder.ivPicture.setBackgroundResource(R.drawable.icon_multi_visit);
//                         CustomImagerLoader.getInstance().loadImage(childHolder.ivPicture, headPic,
//                                R.drawable.baifang, R.drawable.baifang);

                    if(TextUtils.isEmpty(listVisitVo.doctorName)){
                        childHolder.tvName.setText("不记名拜访");
                    }else{
                        childHolder.tvName.setText("拜访 "+doctorname);
                    }

                    childHolder.tvLable.setVisibility(View.VISIBLE);
                    childHolder.tvLable.setText("协同");
                }
//                if(TextViewUtils.isEmpty(remark)){
//                    childHolder.vRemark.setVisibility(View.GONE);
//                }else{
//                    childHolder.vRemark.setVisibility(View.VISIBLE);
//                }
//                childHolder.tvRemark.setText(remark);
                Date date = new Date(time);

                String strTime= TimeFormatUtils.time_format_date(date);
                childHolder.tvTime.setText(strTime);

            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder{
        public TextView     tvTime;
    }

    public static class ChildHolder{
        public ImageView   ivPicture;
        public TextView    tvName;
        public TextView    tvTime;
        public LinearLayout vRemark;
        public TextView    tvRemark;
        public TextView    tvAddress;
        public TextView    tvLable;
    }
}
