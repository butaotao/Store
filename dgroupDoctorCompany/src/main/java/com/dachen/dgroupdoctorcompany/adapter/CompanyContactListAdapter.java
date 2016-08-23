package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.CompanyContactListActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/26.
 */
public class CompanyContactListAdapter extends BaseCustomAdapter<BaseSearch> {
    int depSize;
    Activity context;
    public void setSize(int depSize) {
        this.depSize = depSize;
    }

    public CompanyContactListAdapter(Activity context, int resId, List<BaseSearch> objects, int depSize) {
        super(context, resId, objects);
        this.depSize = depSize;
        this.context = context;
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder = (ViewHolder) baseViewHolder;
        final BaseSearch contact = getItem(position);
        CompanyContactListEntity c2 = null;
        CompanyDepment.Data.Depaments c1 = null;
        CompanyContactListActivity activity = null;
        if (context instanceof CompanyContactListActivity){
            activity = (CompanyContactListActivity) context;
        }
        holder.view_add1.setVisibility(View.GONE);
        if (contact instanceof CompanyContactListEntity) {
            c2 = (CompanyContactListEntity) (contact);
        } else if (contact instanceof CompanyDepment.Data.Depaments) {
            c1 = (CompanyDepment.Data.Depaments) (contact);
        }
        holder.view_add.setVisibility(View.GONE);
        if (position == depSize - 1 && depSize != getCount()) {
            holder.view_add.setVisibility(View.VISIBLE);
        }
        if (c2 != null) {

            if (activity.getContent()==0||activity.getContent()==1){
                holder.rl_people.setVisibility(View.VISIBLE);
            }else {
                holder.rl_people.setVisibility(View.GONE);
            }
            holder.rl_depart.setVisibility(View.GONE);


            holder.tv_name_leader.setText("" + c2.name);
            holder.leader_position.setText(c2.department);
            holder.tv_position.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(c2.position)) {
                holder.tv_position.setVisibility(View.GONE);
            }
            holder.tv_position.setText(c2.position);
            CustomImagerLoader.getInstance().loadImage(holder.head_icon, c2.headPicFileName,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);
            if (activity !=null){
                if (activity.getContent()==0){
                    if ("6".equals(c2.userStatus)) {
                        holder.mTvStatus.setVisibility(View.VISIBLE);//显示未激活
                    } else {
                        holder.mTvStatus.setVisibility(View.GONE);
                    }
                }else if (activity.getContent()==1){
                    holder.mTvStatus.setVisibility(View.GONE);
                    holder.iv_edit.setVisibility(View.VISIBLE);
                }
            }
        } else if (c1 != null) {
            holder.rl_depart.setVisibility(View.VISIBLE);
            holder.rl_people.setVisibility(View.GONE);
            holder.tv_depart.setText("" + c1.name);
            if (!TextUtils.isEmpty(c1.type) && c1.type.equals("1")) {
                holder.view_add1.setVisibility(View.VISIBLE);
            }
            if (activity.getContent()==activity.editColleageDep){
                if (c1.check){
                    holder. btn_radio.setChecked(true);
                }else {
                    holder. btn_radio.setChecked(false);
                }
                holder.btn_radio.setVisibility(View.VISIBLE);
                final CompanyContactListActivity finalActivity = activity;
                final CompanyDepment.Data.Depaments finalC = c1;
            holder.tv_depart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalActivity.getDepment(finalC,false);
                }
            });
                holder.btn_radio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalActivity.checkDepChecked(finalC);
                    }
                });
            }else {
                holder. btn_radio.setVisibility(View.GONE);
            }
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_depart)
        TextView tv_depart;
        @Bind(R.id.head_icon)
        ImageView head_icon;
        @Bind(R.id.tv_name_leader)
        TextView tv_name_leader;
        @Bind(R.id.leader_position)
        TextView leader_position;
        @Bind(R.id.view_add)
        LinearLayout view_add;
        @Bind(R.id.rl_depart)
        RelativeLayout rl_depart;
        @Bind(R.id.rl_people)
        RelativeLayout rl_people;
        @Bind(R.id.tvStatus)
        TextView mTvStatus;
        @Bind(R.id.view_add1)
        RelativeLayout view_add1;
        @Bind(R.id.tv_position)
        TextView tv_position;
        @Bind(R.id.iv_edit)
        ImageView iv_edit;
        @Bind(R.id.btn_radio)
        RadioButton btn_radio;
    }
}
