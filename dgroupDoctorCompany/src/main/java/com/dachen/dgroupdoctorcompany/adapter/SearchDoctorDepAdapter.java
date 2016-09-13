package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.SearchDoctorListEntity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/22.
 */
public class SearchDoctorDepAdapter extends BaseCustomAdapter<SearchDoctorListEntity.SearchDoctorInfo>{
    private final Context context;
    private final int resId;

    public SearchDoctorDepAdapter(Context context, int resId, List<SearchDoctorListEntity.SearchDoctorInfo> objects) {
        super(context, resId, objects);
        this.context = context;
        this.resId = resId;
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        SearchDoctorListEntity.SearchDoctorInfo hospitald = (SearchDoctorListEntity.SearchDoctorInfo)getItem(position);
        ViewHolder holder = (ViewHolder)baseViewHolder;
        String name = "";
         if (null!=hospitald.departments){
            name = hospitald.departments;
        }
        holder.name.setText(""+name);
    }
    public class ViewHolder extends BaseViewHolder{
        @Bind(R.id.name)
        TextView name;
    }
}
