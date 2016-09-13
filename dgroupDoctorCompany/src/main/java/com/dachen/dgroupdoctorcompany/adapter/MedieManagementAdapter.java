package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/22.
 */
public class MedieManagementAdapter extends BaseCustomAdapter<MedieEntity.Medie>{
    public MedieManagementAdapter(Context context, int resId, List<MedieEntity.Medie> objects) {
        super(context, resId, objects);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder = (ViewHolder) baseViewHolder;
        MedieEntity.Medie d= getItem(position);
        String name = "";
        if (null!=d.drugName){
            name = d.drugName;
        }
        if(d.selectedNum>0){
            holder.tvSelected.setVisibility(View.VISIBLE);
            holder.tvSelected.setText(""+d.selectedNum);
        }else{
            holder.tvSelected.setVisibility(View.GONE);
        }

    //   name =  CompareDatalogic.getShowName(d.goods$general_name,d.goods$general_name,name);
        holder.name.setText(name);
    }
    public class ViewHolder extends BaseViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tvSelected)
        TextView tvSelected;
    }
}
