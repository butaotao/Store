package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSelfPartHospitalActivity;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.DeleteAddSelfPartHospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;
import com.dachen.dgroupdoctorcompany.entity.ShowAddSelfPartHospitalsData;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/5/19.
 */
public class AddSelfPartHospitaAdapter extends android.widget.BaseAdapter{
    Activity context;
    ArrayList<HospitalDes> data;
    AddSelfPartHospitalActivity.RefreshData refreshData;
    String groupId;
    BaseActivity baseActivity;
    boolean issure;
    public AddSelfPartHospitaAdapter(Activity context,boolean issure, ArrayList<HospitalDes> data,
                                     AddSelfPartHospitalActivity.RefreshData refreshData,String groupId,BaseActivity baseActivity){
        this.context = context;
        this.data = data;
        this.refreshData = refreshData;
        this.groupId = groupId;
        this.baseActivity = baseActivity;
        this.issure = issure;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final HospitalDes me = (HospitalDes) getItem(position);
        if (null ==convertView){
            convertView = View.inflate(context, R.layout.adapter_addselfparthospital,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.line1 = convertView.findViewById(R.id.line1);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.line1.setVisibility(View.VISIBLE);
        if (position == (getCount()-1)){
            holder.line1.setVisibility(View.GONE);
        }
        holder.tv_name.setText(me.name);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                if (issure){
                    deleteHospital(me,null);
                }else {
                    showDeleteMedie(context, me);
                }


                   /* }
                }).start();*/
            }
        });

        return convertView;
    }
    public static class ViewHolder{
        TextView tv_name;
        ImageView iv_delete;
        View line1;
    }
    public void showDeleteMedie(Activity context, final HospitalDes me){

        final CustomDialog dialog = new CustomDialog(context);
        dialog.showDialog("提示", "您确定要取消医院分管关系吗？", R.string.cancel, R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                baseActivity.showLoadingDialog();
                if (!TextUtils.isEmpty(groupId)) {
                    deleteHospital(me, dialog);
                }
            }
        });
    }

    public void deleteHospital(final HospitalDes me, final CustomDialog dialog){
        String  s = "org/saleFriend/deleteHospital";
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(context).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(context, "id", ""));
        if (!TextUtils.isEmpty(me.id)){
            baseActivity.showLoadingDialog();
            maps.put("goodsGroupId", groupId);
            maps.put("hospitalId",me.id);
            new HttpManager().post(context, s, DeleteAddSelfPartHospital.class,
                    maps, new HttpManager.OnHttpListener<Result>() {
                        @Override
                        public void onSuccess(Result response) {
                            baseActivity.closeLoadingDialog();
                            if (response.resultCode == 1) {
                                data.remove(me);
                                notifyDataSetChanged();
                                refreshData.Refreshdata(data.size());
                                if (null != dialog) {
                                    dialog.dimissDialog();
                                }

                            }
                        }

                        @Override
                        public void onSuccess(ArrayList<Result> response) {
                        }

                        @Override
                        public void onFailure(Exception e, String errorMsg, int s) {
                            baseActivity.closeLoadingDialog();
                            ToastUtil.showToast(context, "删除失败");
                        }
                    },
                    false, 1);
            //showLoadingDialog();
        }
    }
}
