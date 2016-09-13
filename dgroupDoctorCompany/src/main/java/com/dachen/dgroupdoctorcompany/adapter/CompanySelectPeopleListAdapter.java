package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/26.
 */
public class CompanySelectPeopleListAdapter extends BaseCustomAdapter<BaseSearch>{
    int depSize;
    public void setSize(int depSize){
        this.depSize = depSize;
    }
    public CompanySelectPeopleListAdapter(Context context, int resId, List<BaseSearch> objects, int depSize) {
        super(context, resId, objects);
        this.depSize = depSize;
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder = (ViewHolder) baseViewHolder;
        BaseSearch contact = getItem(position);
        CompanyContactListEntity c2 = null;
        CompanyDepment.Data.Depaments  c1 = null;
        holder.view_add1.setVisibility(View.GONE);
        if (contact instanceof CompanyContactListEntity){
             c2 = (CompanyContactListEntity)(contact);
        }else if(contact instanceof CompanyDepment.Data.Depaments){
             c1 = (CompanyDepment.Data.Depaments)(contact);
        }
        holder.view_add.setVisibility(View.GONE);
        if (position == depSize-1){
            holder.view_add.setVisibility(View.VISIBLE);
        }
        if (c2!=null){
            holder.rl_people.setVisibility(View.VISIBLE);
            holder.rl_depart.setVisibility(View.GONE);


            holder.tv_name_leader.setText("" + c2.name);
            holder.leader_position.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(c2.position)){
                holder.leader_position.setVisibility(View.GONE);
            }
            holder.leader_position.setText(c2.position);
            holder.leader_depart.setText(c2.department);
            holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_unselect);
            if (c2.select){
                //   holder.btn_radio.setSelected(true);
              holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_selected);
            }else {


            }

            if (c2.haveSelect==true||c2.userId.equals(SharedPreferenceUtil.getString(mContext,"id",""))){
                holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_disable);
            }
            if(!TextUtils.isEmpty(c2.headPicFileName))
            {
                CustomImagerLoader.getInstance().loadImage(holder.head_icon, c2.headPicFileName,
                        R.drawable.head_icons_company, R.drawable.head_icons_company);
            }
        }else if(c1!=null){
            holder.rl_depart.setVisibility(View.VISIBLE);
            holder.rl_people.setVisibility(View.GONE);
            holder.tv_depart.setText("" + c1.name);
            if (!TextUtils.isEmpty(c1.type)&&c1.type.equals("3")){
                holder.view_add1.setVisibility(View.VISIBLE);
            }


        }
    }

    public class ViewHolder extends BaseViewHolder{
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
        @Bind(R.id.btn_radio)
        RadioButton btn_radio;
        @Bind(R.id.view_add1)
        RelativeLayout view_add1;
        @Bind(R.id.leader_depart)
        TextView leader_depart;
    }
}
