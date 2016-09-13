package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddFriendActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/3/4.
 */
public class SearchDoctorAdapter extends BaseCustomAdapter<BaseSearch> implements HttpManager.OnHttpListener{
    Context context;
    public SearchDoctorAdapter(Context context, int resId, List<BaseSearch> objects) {

        super(context, resId, objects);
        this.context = context;
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHoder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        BaseSearch search = getItem(position);
         Doctor doctor = null;
        ViewHoder hoder = (ViewHoder)baseViewHolder;
        if(search instanceof Doctor){
            hoder.tv_searchrecord.setVisibility(View.GONE);
            doctor = (Doctor) search;
            String url = "";
            if (null!=doctor.headPicFileName){
                url = doctor.headPicFileName;
            }
            //ImageLoader.getInstance().displayImage(url, hoder.iv_headicon);
            CustomImagerLoader.getInstance().loadImage(hoder.iv_headicon, url,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);
            hoder.tv_name.setText(doctor.name);
            hoder.tv_customervisit.setText(doctor.title);
            hoder.tv_frenddes.setText(doctor.hospital);
            hoder.rl_companycontact.setVisibility(View.VISIBLE);
            final Doctor finalDoctor = doctor;
            hoder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddFriendActivity.class);
                    intent.putExtra("id", finalDoctor.userId);
                    intent.putExtra("url", finalDoctor.headPicFileName);
                    intent.putExtra("name", finalDoctor.name);
                    intent.putExtra("phone", finalDoctor.telephone);
                    intent.putExtra("hospital", finalDoctor.hospital);//position
                    intent.putExtra("dep", finalDoctor.title);
                    intent.putExtra("position", finalDoctor.departments);
                    context.startActivity(intent);
                    //addDoctor(doctor.userId);
                }
            });
        }else if (search instanceof SearchRecords){
            hoder.tv_searchrecord.setVisibility(View.VISIBLE);
            SearchRecords records = (SearchRecords) search;
            hoder.tv_searchrecord.setText("" + records.searchresult);
            hoder.rl_companycontact.setVisibility(View.GONE);
        }




    }

    @Override
    public void onSuccess(Result response) {
            if (response.resultCode==1){
                ToastUtil.showToast(mContext,"添加好友成功");
            }else {
                ToastUtil.showToast(mContext,"添加好友失败"+response.resultMsg);
            }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        ToastUtil.showToast(mContext,"添加好友失败");
    }

    public class ViewHoder extends BaseCustomAdapter.BaseViewHolder{
        @Nullable
        @Bind(R.id.iv_headicon)
        ImageView iv_headicon;
        @Nullable
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Nullable
        @Bind(R.id.tv_customervisit)
        TextView tv_customervisit;
        @Nullable
        @Bind(R.id.tv_frenddes)
        TextView tv_frenddes;
        @Nullable
        @Bind(R.id.tv_adds)
        TextView tv_adds;
        @Nullable
        @Bind(R.id.ll_item)
        LinearLayout ll_item;
        @Nullable
        @Bind(R.id.tv_searchrecord)
        TextView tv_searchrecord;
        @Nullable
        @Bind(R.id.rl_companycontact)
        RelativeLayout rl_companycontact;
    }
    public void addDoctor(String id){
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token",  UserInfo.getInstance(mContext).getSesstion());
        maps.put("doctorId",id);
        new HttpManager().post(mContext, Constants.DRUG+"saleFriend/addFriend", Result.class,
                maps, this,
                false, 1);
    }


}
