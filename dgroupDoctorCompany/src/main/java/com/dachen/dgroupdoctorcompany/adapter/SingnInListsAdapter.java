package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SignInLists;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/2下午5:48.
 * @描述 TODO
 */
public class SingnInListsAdapter extends BaseAdapter<SignInLists> {
    private List<SignInLists.DataBean.PageDataBean> mDataLists = new ArrayList<>();

    public SingnInListsAdapter(Context context) {
        super(context);
    }

    public SingnInListsAdapter(Context context, List<SignInLists> data) {
        super(context, data);
    }


    public void addData(List<SignInLists.DataBean.PageDataBean> dataLists,boolean refresh){
        if(refresh){
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        }else{
            mDataLists.addAll(dataLists);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        SignInLists.DataBean.PageDataBean pageDataBean = mDataLists.get(position);
        Log.d("zxy :", "64 : SingnInListsAdapter : getView : pageDataBean"+pageDataBean.address);
        if(null!=pageDataBean){
            String type = pageDataBean.tag.get(0);
            String headPic = pageDataBean.headPic;
            String doctorname = pageDataBean.userName;
            String remark = pageDataBean.remark;
            long time = pageDataBean.longTime;

            if("1".equals(type)){//如果是上班签到
                childHolder.ivPicture.setBackgroundResource(R.drawable.icon_sign);
                childHolder.tvName.setText("考勤打卡");
                String address = pageDataBean.address;
                childHolder.tvAddress.setText(address);

                List<String> listLable = pageDataBean.tag;
                if(null!=listLable && listLable.size()>0){
                    childHolder.tvLable.setVisibility(View.VISIBLE);
                    childHolder.tvLable.setText(listLable.get(0));
                }else{
                    childHolder.tvLable.setVisibility(View.GONE);
                }
            }else if("0".equals(type)){//独立拜访
                String address = pageDataBean.address;
                if(TextUtils.isEmpty(address)){
                    address = pageDataBean.address;
                }
                childHolder.tvAddress.setText(address);
                childHolder.ivPicture.setBackgroundResource(R.drawable.icon_signle_visit);
//                         CustomImagerLoader.getInstance().loadImage(childHolder.ivPicture, headPic,
//                                R.drawable.baifang, R.drawable.baifang);

                if(TextUtils.isEmpty(pageDataBean.address)){
                    childHolder.tvName.setText("不记名拜访");
                }else{
                    childHolder.tvName.setText("拜访 "+doctorname);
                }

                childHolder.tvLable.setVisibility(View.GONE);

            }else if("2".equals(type)){//协同拜访
                String address = pageDataBean.address;
                if(TextUtils.isEmpty(address)){
                    address = pageDataBean.address;
                }
                childHolder.tvAddress.setText(address);
                childHolder.ivPicture.setBackgroundResource(R.drawable.icon_multi_visit);
//                         CustomImagerLoader.getInstance().loadImage(childHolder.ivPicture, headPic,
//                                R.drawable.baifang, R.drawable.baifang);

                if(TextUtils.isEmpty(pageDataBean.userName)){
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
        return convertView;
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
