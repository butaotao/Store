package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.HospitalActivity;
import com.dachen.dgroupdoctorcompany.activity.HospitalManagerActivity;
import com.dachen.dgroupdoctorcompany.activity.SearchActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.Hospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalList;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/22.
 */
public class HospitalListAdapter extends BaseCustomAdapter<BaseSearch> implements HttpManager.OnHttpListener{
    private final Context context;
    private final int resId;
    String fromActivity;
    SearchActivity.RefreshData refreshData;
    List<BaseSearch> objects;
    public HospitalListAdapter(Context context, int resId, List<BaseSearch> objects,String fromActivity,SearchActivity.RefreshData refreshData) {
        super(context, resId, objects);
        this.context = context;
        this.resId = resId;
        this.fromActivity = fromActivity;
        this.objects = objects;
        this.refreshData = refreshData;
    }
    public HospitalListAdapter(Context context, int resId, List<BaseSearch> objects,String fromActivity) {
        super(context, resId, objects);
        this.context = context;
        this.resId = resId;
        this.fromActivity = fromActivity;
        this.objects = objects;
    }
    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, final int position) {
       BaseSearch base =  getItem(position);
        ViewHolder holder = (ViewHolder)baseViewHolder;
         if (TextUtils.isEmpty(fromActivity)){
            holder.rl_item_addone.setVisibility(View.VISIBLE);
            holder.rl_item.setVisibility(View.GONE);
        }else {
             holder.rl_item_addone.setVisibility(View.GONE);
             holder.rl_item.setVisibility(View.VISIBLE);
         }
        if (base instanceof SearchRecords){
            SearchRecords records = (SearchRecords) base;
            holder.name.setText("" + records.searchresult);
            holder.tv_add.setVisibility(View.GONE);
            holder.tv_name.setText("" + records.searchresult);
        }else if(base instanceof HospitalList.Data.HospitalDes){
            final HospitalList.Data.HospitalDes hospitald = (HospitalList.Data.HospitalDes)base;
            boolean flag = false;
            for (int i=0;i<HospitalActivity.hospitals.size();i++){
                if (HospitalActivity.hospitals.get(i) instanceof  HospitalDes){
                    HospitalDes des = ( HospitalDes) HospitalActivity.hospitals.get(i);
                    if ((des.id+"").equals(hospitald.id)){
                        flag = true;
                    }
                }

            }
            if (!flag){

                holder.tv_add.setText("添加");
                holder.tv_add.setTextColor(context.getResources().getColor(R.color.color_1B9ADE));
                holder.tv_add.setBackgroundResource(R.drawable.bulk_1b9ade);
                holder.tv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ToastUtils.showToast(context,"add=="+name);
                        // ImageToast.makeToast( context);
                        addHospital(hospitald.id);
                    }
                });
            }else {
                holder.tv_add.setText("已添加");
                holder.tv_add.setTextColor(context.getResources().getColor(R.color.color_grayline1dp));

                holder.tv_add.setBackgroundResource(R.drawable.btn_bulk_gray);
            }
            holder.tv_add.setVisibility(View.VISIBLE);
            final String name = hospitald.name;
            holder.name.setText("" + name);
            holder.tv_name.setText("" + name);
            if (hospitald.select){
                holder.radionbutton.setChecked(true);
            }else {
                holder.radionbutton.setChecked(false);
            }
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hospitald.select){
                        hospitald.select = false;
                    }else {
                        if (HospitalManagerActivity.totalNumHospital+getTotal(objects)>=HospitalManagerActivity.larSize){

                            hospitald.select = false;
                            objects.set(position,hospitald);
                            if (refreshData!=null){
                                refreshData.refreshData(objects);
                            }
                            notifyDataSetChanged();
                            showMoreFiftyHosp();
                            return;
                        }

                        hospitald.select = true;
                    }
                    objects.set(position, hospitald);
                    if (refreshData!=null){
                        refreshData.refreshData(objects);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.radionbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hospitald.select){
                        hospitald.select = false;
                    }else {
                        if (HospitalManagerActivity.totalNumHospital+getTotal(objects)>=HospitalManagerActivity.larSize){
                            hospitald.select = false;
                            objects.set(position,hospitald);
                            if (refreshData!=null){
                                refreshData.refreshData(objects);
                            }
                            notifyDataSetChanged();
                            showMoreFiftyHosp();
                            return;
                        }
                        hospitald.select = true;
                    }
                    objects.set(position,hospitald);
                    if (refreshData!=null){
                        refreshData.refreshData(objects);
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onSuccess(Result response) {
     //   ToastUtil.showToast(context,response.toString());
        if (response.resultCode==1){
            ToastUtil.showToast(context,"添加成功");
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    public class ViewHolder extends BaseViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.radionbutton)
        RadioButton radionbutton;
        @Nullable
        @Bind(R.id.tv_adds)
        TextView tv_add;
        @Bind(R.id.rl_item_addone)
        RelativeLayout rl_item_addone;
        @Bind(R.id.rl_item)
        RelativeLayout rl_item;
    }
    public void addHospital(final String id){
        //8091
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(context).getSesstion());
        maps.put("hospitalId",id);
        new HttpManager().post(context, Constants.DRUG+"saleFriend/addHospital", Result.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode==1){
                            Hospital hospital = new Hospital() ;
                        Hospital.Data data = hospital.new Data();
                        HospitalDes des=  new HospitalDes();


                        des.id = id;
                        HospitalActivity.hospitals.add(des);
                            ToastUtil.showToast(context,"添加成功");
                            notifyDataSetChanged();
                        }else {
                            ToastUtil.showToast(context,response.resultMsg);
                        }

                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }

    public void showMoreFiftyHosp() {
        final CustomDialog dialog = new CustomDialog(mActivity);
        dialog.showDialog("提示", "您添加医院数量已经超过50家，请先删除医院后再添加", R.string.know,
                0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dimissDialog();
                    }
                }, null);
    }
    public static int getTotal(List<BaseSearch> recordses){
        int total = 0;
        for (int i=0;i<recordses.size();i++){
            if (recordses.get(i)  instanceof HospitalList.Data.HospitalDes){
                HospitalList.Data.HospitalDes hospitalDes = (HospitalList.Data.HospitalDes) recordses.get(i);
                if(hospitalDes.select){
                    total = total+1;
                }
            }
        }
        return total;
    }
}
