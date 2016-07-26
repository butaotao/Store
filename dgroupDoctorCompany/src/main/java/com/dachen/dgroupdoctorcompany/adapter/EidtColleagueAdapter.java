package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.DeleteColleActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/6/1.
 */
public class EidtColleagueAdapter extends BaseCustomAdapter<BaseSearch> {
    List<BaseSearch> contacts;
    Activity context;
    public EidtColleagueAdapter(Activity context, int resId, List<BaseSearch> objects) {
        super(context, resId, objects);
        contacts = objects;
        this.context = context;
    }

    @Override
    protected ViewHolder getViewHolder() {
        return new ViewHolder();

    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder =(ViewHolder) baseViewHolder;
        final int mPosition = position;
        if (getItem(position) instanceof CompanyContactListEntity){

            final CompanyContactListEntity entity = (CompanyContactListEntity) getItem(position);
            if (null!=entity){
                String url =entity.url;
                if (TextUtils.isEmpty(url)){
                    url = "";
                }
                holder.tv_name.setText("");
                holder.tv_depart.setText("");
                if (!TextUtils.isEmpty(entity.name)){
                    holder.tv_name.setText(entity.name+"");
                }
              if(!TextUtils.isEmpty(entity.position)){
                  holder.tv_depart.setText(entity.position+"");
              }

                CustomImagerLoader.getInstance().loadImage(holder.iv_icon, url,
                        R.drawable.head_icons_company, R.drawable.head_icons_company);
            }
            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DeleteColleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("colleage", entity);
                    intent.putExtra("colleage", bundle);
                    intent.putExtra("position",mPosition+"");
                    context.startActivityForResult(intent, 200);

                }
            });
        }
    }

    public class ViewHolder extends BaseViewHolder{
        @Nullable
        @Bind(R.id.iv_edit)
        ImageView iv_edit;
        @Nullable
        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Nullable
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Nullable
        @Bind(R.id.tv_depart)
        TextView tv_depart;
        @Nullable
        @Bind(R.id.rl_dividerline1)
        RelativeLayout rl_dividerline1;
        @Nullable
        @Bind(R.id.ll_firstitem)
        LinearLayout ll_firstitem;
        @Nullable
        @Bind(R.id.layout_item)
        RelativeLayout layout_item;
    }
}
