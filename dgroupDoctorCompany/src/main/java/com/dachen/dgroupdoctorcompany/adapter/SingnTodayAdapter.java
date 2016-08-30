package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.views.DialogEditorText;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/8/27.
 */
public class SingnTodayAdapter extends android.widget.BaseAdapter{
    Context context;
     List<SignInList.Data.DataList.ListVisitVo> mDataLists = new ArrayList<>();
    public  SingnTodayAdapter(Context context){
        this.context = context;
    }
    public void addData(List<SignInList.Data.DataList.ListVisitVo> dataLists,boolean refresh){
        if(refresh){
            mDataLists.clear();
            mDataLists.addAll(dataLists);
        }else{
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
        SignInList.Data.DataList.ListVisitVo listVisitVo = mDataLists.get(position);
        childHolder  = new ChildHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_sign_in_child_today,null);
        childHolder.iv_editor = (ImageView) convertView.findViewById(R.id.iv_editor);
        childHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        childHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        childHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        convertView.setTag(childHolder);

        String address = listVisitVo.addressName;
        if(TextUtils.isEmpty(address)){
            address = listVisitVo.address;
        }
        if (null!=listVisitVo.singedTag&&listVisitVo.singedTag.size()>0){
            String des = "";
            String text = listVisitVo.singedTag.get(0);
            if (!TextUtils.isEmpty(text)&&text.length()>0){
                for (int i = 0;i<text.length();i++){
                    if (i!=text.length()-1){
                        des += text.charAt(i)+"\n";
                    }else {
                        des += text.charAt(i);
                    }

                }
            }
            childHolder.tvName.setText(des);
        }else {
            childHolder.tvName.setText("");
        }
        if (0!=listVisitVo.time){
            childHolder.tvTime.setText(TimeUtils.getTimesHourMinute(listVisitVo.time));
        }else {
            childHolder.tvTime.setText("");
        }
        childHolder.tvAddress.setText(address);
        childHolder.iv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEditorText dialog = new DialogEditorText((Activity) context);
                dialog.showDialog();
            }
        });
      //  childHolder.ivPicture.setBackgroundResource(R.drawable.icon_signle_visit);
//                         CustomImagerLoader.getInstance().loadImage(childHolder.ivPicture, headPic,
//                                R.drawable.baifang, R.drawable.baifang);




        return convertView;
    }
    public static class ChildHolder{
       public ImageView   iv_editor;
        public TextView    tvName;
        public TextView    tvTime;
        public TextView    tvAddress;
    }
}
