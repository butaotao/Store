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
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.activity.SiginDetailActivity;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.entity.SignTodayInList;
import com.dachen.dgroupdoctorcompany.im.bean.UpdateGroup2Bean;
import com.dachen.dgroupdoctorcompany.views.DialogEditorText;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/8/27.
 */
public class SingnTodayAdapter extends android.widget.BaseAdapter{
    MenuWithFABActivity context;
     List<SignTodayInList.Data.DataList> mDataLists = new ArrayList<>();
    public  SingnTodayAdapter(MenuWithFABActivity context){
        this.context = context;
    }
    public void addData(List<SignTodayInList.Data.DataList> dataLists,boolean refresh){
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
        final SignTodayInList.Data.DataList listVisitVo = mDataLists.get(position);
        childHolder  = new ChildHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_sign_in_child_today,null);
        childHolder.iv_editor = (ImageView) convertView.findViewById(R.id.iv_editor);
        childHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        childHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        childHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        childHolder.tvdes = (TextView) convertView.findViewById(R.id.tvdes);
        childHolder.rl_click = (RelativeLayout) convertView.findViewById(R.id.rl_click);
        childHolder.rl_des = (RelativeLayout) convertView.findViewById(R.id.rl_des);
        convertView.setTag(childHolder);

        String address = listVisitVo.time;
        if(TextUtils.isEmpty(address)){
            address = listVisitVo.address;
        }
        childHolder.rl_des.setVisibility(View.VISIBLE);
        childHolder.tvdes.setText("");
        if (null!=listVisitVo.tag&&listVisitVo.tag.size()>0){
            String des = "";
            String text = listVisitVo.tag.get(0);
            if (!TextUtils.isEmpty(text)&&text.length()>0){
                if (text.length()<4){
                    for (int i = 0;i<text.length();i++){
                        if (i!=text.length()-1){
                            des += text.charAt(i)+"\n";
                        }else {
                            des += text.charAt(i);
                        }

                    }
                }else {
                    for (int i = 0;i<text.length();i++){
                        if (i!=0&&(i-1)%2==0&&i!=text.length()-1){
                            des += text.charAt(i)+"\n";
                        }else {
                            des += text.charAt(i);
                        }

                    }
                }

            }
            if (text.equals("拜访")){
                childHolder.rl_des.setVisibility(View.GONE);
                childHolder.tvName.setBackgroundResource(R.drawable.btn_leftcoryellow_all);
            }else if(text.equals("上班")){
                childHolder.tvName.setBackgroundResource(R.drawable.btn_leftcor_all);
            }else if(text.equals("下班")){
                childHolder.tvName.setBackgroundResource(R.drawable.btn_leftcorgreen39d7e6_all);
            }else  {
                childHolder.tvName.setBackgroundResource(R.drawable.btn_leftcorgreen_all);
            }
            childHolder.tvName.setText(des);

           // childHolder.rl_des.setVisibility(View.GONE);
        }else {
            childHolder.tvName.setBackgroundResource(R.drawable.btn_leftcorgreen_all);
            childHolder.tvName.setText("签\n到");
        }
       if (!TextUtils.isEmpty(listVisitVo.remark)){
            childHolder.tvdes.setText(listVisitVo.remark);
            childHolder.rl_des.setVisibility(View.VISIBLE);
        }else {
           childHolder.rl_des.setVisibility(View.GONE);
       }
        if (0!=listVisitVo.longTime){
            String destime = "上午";
            if (TimeUtils.isnoon(listVisitVo.longTime)){
                destime = "下午";
            }
            childHolder.tvTime.setText(destime +" "+TimeUtils.getTimesHourMinute(listVisitVo.longTime));
        }else {
            childHolder.tvTime.setText("");
        }
        childHolder.tvAddress.setText(address);
        childHolder.iv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEditorText dialog = new DialogEditorText((MenuWithFABActivity) context, listVisitVo);
                dialog.showDialog();
            }
        });
        childHolder.rl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MenuWithFABActivity){
                    MenuWithFABActivity activity = (MenuWithFABActivity) context;
                    if (null!=activity.fragment&&null!=activity.fragment.circleMenu){
                        activity.fragment.circleMenu.close(true);
                    }
                }

                Intent intent = new Intent(context, SiginDetailActivity.class);
                intent.putExtra("day", "");
                intent.putExtra("hour","");
                intent.putExtra("remark",listVisitVo.remark);
                intent.putExtra("address", listVisitVo.address);
                intent.putExtra("longTime", listVisitVo.longTime);
                intent.putExtra("signedid",listVisitVo.signedId);
                /*if(!TextUtils.isEmpty(listVisitVo.coordinate)&&listVisitVo.coordinate.contains(",")){
                    String[] array = listVisitVo.coordinate.split(",");
                    String latitude = array[0];
                    String longitude = array[1];
                    intent.putExtra("latitude", Double.valueOf(latitude));
                    intent.putExtra("longitude", Double.valueOf(longitude));
                }*/
                intent.putExtra("id",listVisitVo.visitId);
                if (null != listVisitVo.tag && listVisitVo.tag.size() > 0 && !TextUtils.isEmpty(listVisitVo.tag
                        .get(0))) {
                    intent.putExtra("tag", listVisitVo.tag.get(0));
                } else {
                    intent.putExtra("tag", "");
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public static class ChildHolder{
       public ImageView   iv_editor;
        public TextView    tvName;
        public TextView    tvTime;
        public TextView    tvAddress;
        public TextView    tvdes;
        public RelativeLayout rl_click;
        public RelativeLayout rl_des;
    }
}
