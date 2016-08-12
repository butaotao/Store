package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weiwei on 2016/5/19.
 */
public abstract class VisitListAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private List<SignInList.Data.DataList.ListVisitVo> mDataLists = new ArrayList<>();

    public VisitListAdapter(Context context){
        this.mContext = context;
    }

    public void addData(List<SignInList.Data.DataList.ListVisitVo> dataLists,boolean refresh){
        if(refresh){
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        }else{
            mDataLists.addAll(dataLists);
        }
    }
    public VisitListAdapter(Context context,List<SignInList.Data.DataList.ListVisitVo> dataLists){
        this.mContext = context;
        this.mDataLists.clear();
        this.mDataLists.addAll(dataLists);
    }
    @Override
    public int getCount() {
        return mDataLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_visit_list,null);
            viewHolder = new ViewHolder();
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
            viewHolder.tvFlag = (TextView) convertView.findViewById(R.id.tvFlag);
            viewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvRemark = (TextView) convertView.findViewById(R.id.tvRemark);
            viewHolder.tvWeek = (TextView) convertView.findViewById(R.id.tvWeek);
            viewHolder.line1 = convertView.findViewById(R.id.line1);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.joint_txt = (TextView) convertView.findViewById(R.id.joint_txt);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SignInList.Data.DataList.ListVisitVo listVisitVo = mDataLists.get(position);
        if(null != listVisitVo){
            String type = listVisitVo.type;
            String headPic = listVisitVo.headPic;
            String doctorname = listVisitVo.doctorName;
            String remark = listVisitVo.remark;
            long time = listVisitVo.time;
            String address = listVisitVo.address;
            String state = listVisitVo.state;

            CustomImagerLoader.getInstance().loadImage(viewHolder.ivPicture, headPic,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);

//            ImageLoader.getInstance().displayImage(headPic, viewHolder.ivPicture, CommonUitls.getHeadOptions());

            if(TextUtils.isEmpty(doctorname)){
                viewHolder.tvName.setText("不记名拜访");
            }else{
                viewHolder.tvName.setText(doctorname);
            }

//            if("1".equals(state)){
//                viewHolder.tvFlag.setVisibility(View.GONE);
//                viewHolder.ivDelete.setVisibility(View.GONE);
//            }else{
//                viewHolder.tvFlag.setVisibility(View.VISIBLE);
//                viewHolder.ivDelete.setVisibility(View.VISIBLE);
//            }
//            viewHolder.line1.setVisibility(View.VISIBLE);
//            if(TextViewUtils.isEmpty(remark)){
//                viewHolder.tvRemark.setVisibility(View.GONE);
//                viewHolder.line1.setVisibility(View.GONE);
//            }else{
//                viewHolder.tvRemark.setVisibility(View.VISIBLE);
//                viewHolder.tvRemark.setText(remark);
//            }

            viewHolder.address.setText(listVisitVo.addressName);
            if("0".equals(listVisitVo.type)){//独立拜访
                viewHolder.joint_txt.setVisibility(View.GONE);
            }else if("2".equals(listVisitVo.type)){//协同拜访
                viewHolder.joint_txt.setVisibility(View.VISIBLE);
            }

            Date date = new Date(time);

            String strTime = TimeFormatUtils.format_date(date);
//            String strWeek = TimeFormatUtils.week_format_date(date);
//            viewHolder.tvDate.setText(strDate);
            viewHolder.tvWeek.setText(strTime);

//            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onDeleteItem(listVisitVo);
//                }
//            });
        }
        return convertView;
    }

    private class ViewHolder{
        public ImageView ivPicture;
        public TextView tvName;
        public TextView tvFlag;
        public ImageView ivDelete;
        public TextView tvDate;
        public TextView tvWeek;
        public TextView tvRemark;
        public View line1;
        public TextView address;
        public TextView joint_txt;

    }

    protected abstract void onDeleteItem(SignInList.Data.DataList.ListVisitVo item);

    public List<SignInList.Data.DataList.ListVisitVo> getmDataLists() {
        return mDataLists;
    }

    public void setmDataLists(List<SignInList.Data.DataList.ListVisitVo> mDataLists) {
        this.mDataLists = mDataLists;
    }
}
