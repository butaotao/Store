package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SignInLists;
import com.dachen.dgroupdoctorcompany.utils.LogUtils;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/2下午5:48.
 * @描述 TODO
 */
public class SingnInListsAdapter extends android.widget.BaseAdapter {
    private List<SignInLists.DataBean.PageDataBean> mDataLists = new ArrayList<>();
    private Context mContext;
    private String backDate;

    public SingnInListsAdapter(Context context) {
        // super(context);
        mContext = context;
    }

    public SingnInListsAdapter(Context context, List<SignInLists.DataBean.PageDataBean> data) {
        // super(context, data);
        mContext = context;
        mDataLists = data;
    }


    public void addData(List<SignInLists.DataBean.PageDataBean> dataLists, boolean refresh) {
        if (refresh) {
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        } else {
            mDataLists.addAll(dataLists);
        }
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
        ChildHolder childHolder;
        //   if(null == convertView){
        childHolder = new ChildHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sign_in_child, null);
        childHolder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
        //   childHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        childHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        childHolder.vRemark = (LinearLayout) convertView.findViewById(R.id.vRemark);
        childHolder.tvRemark = (TextView) convertView.findViewById(R.id.tvRemark);
        childHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        childHolder.tvLable = (TextView) convertView.findViewById(R.id.tvLable);
        childHolder.tvSinginDate = (TextView) convertView.findViewById(R.id.tv_singin_date);
        convertView.setTag(childHolder);
       /* }else {
            childHolder  = (ChildHolder) convertView.getTag();
        }*/
        SignInLists.DataBean.PageDataBean pageDataBean = mDataLists.get(position);
        if (null != pageDataBean) {
            String headPic = pageDataBean.headPic;
            String doctorname = pageDataBean.userName;
            String remark = pageDataBean.remark;
            long time = pageDataBean.longTime;
            childHolder.tvLable.setVisibility(View.VISIBLE);
            childHolder.ivPicture.setBackgroundResource(R.drawable.icon_sign);
            if (pageDataBean.tag != null && pageDataBean.tag.size()>0 && !TextUtils.isEmpty(pageDataBean.tag.get(0))) {
                String type = pageDataBean.tag.get(0);
                if ("拜访".equals(type)) {   //如果是上班签到
                    String address = pageDataBean.address;
                    if (TextUtils.isEmpty(address)) {
                        address = pageDataBean.address;
                    }
                    childHolder.tvLable.setText(type);
                    childHolder.tvAddress.setText(address);

                } else {  //独立拜访
                    String address = pageDataBean.address;
                    childHolder.tvAddress.setText(address);
                    childHolder.tvLable.setText(type);
                }
            }else {
                childHolder.tvLable.setText("签到");
            }
            Date date = new Date(time);
            String strDate = TimeFormatUtils.china_format_date(date);//格式化日期X月X日
            childHolder.tvSinginDate.setVisibility(View.GONE);
            if (position == 0) {//第一条目显示日期
                childHolder.tvSinginDate.setVisibility(View.VISIBLE);
                childHolder.tvSinginDate.setText(strDate);
                backDate = strDate;
            } else {//如果后面的时间和前面的时间不一样者显示日期,否则不显示
                if (!backDate.equals(strDate)) {
                    childHolder.tvSinginDate.setVisibility(View.VISIBLE);
                    childHolder.tvSinginDate.setText(strDate);
                    backDate = strDate;
                }
            }
            String strTime = TimeFormatUtils.time_format_date(date);
            childHolder.tvTime.setText(strTime);
        }
        return convertView;
    }

    public static class ChildHolder {
        public ImageView ivPicture;
        //public TextView    tvName;
        public TextView tvTime;
        public LinearLayout vRemark;
        public TextView tvRemark;
        public TextView tvAddress;
        public TextView tvLable;
        public TextView tvSinginDate;
    }
}
