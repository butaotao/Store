package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/22.
 */
public class HospitalAdapter extends BaseCustomAdapter<BaseSearch>{
    private final Context context;
    private final int resId;

    public HospitalAdapter(Context context, int resId, List<BaseSearch> objects) {
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
        ViewHolder holder = (ViewHolder)baseViewHolder;
        if (getItem(position) instanceof HospitalDes){
            HospitalDes hospitald = (HospitalDes)getItem(position);

            String name = "";
            if (null!=hospitald&&null!=hospitald.name){
                name = hospitald.name;
            }
            holder.name.setText(""+name);
            holder.rl_docotorname.setVisibility(View.VISIBLE);
            holder.rl_companycontact.setVisibility(View.GONE);
        }else if (getItem(position) instanceof Doctor){
            Doctor d = (Doctor) getItem(position);
            holder.rl_companycontact.setVisibility(View.VISIBLE);
            holder.rl_docotorname.setVisibility(View.GONE);
            String url = d.headPicFileName;
            CustomImagerLoader.getInstance().loadImage(holder.iv_headicon, url,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);
            holder.namedoctor.setText(d.name);
            holder.tv_level.setText(d.title);
            holder.tv_part.setText(d.departments);
            holder.tv_location.setText(d.hospital);
        }

        if(position == getCount() -1){
            holder.layout_line.setVisibility(View.GONE);
            holder.layout_line1.setVisibility(View.VISIBLE);
        }else {
            holder.layout_line.setVisibility(View.VISIBLE);
            holder.layout_line1.setVisibility(View.GONE);
        }
    }
    public class ViewHolder extends BaseViewHolder{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.rl_companycontact)
        RelativeLayout rl_companycontact;
        @Bind(R.id.rl_docotorname)
        RelativeLayout rl_docotorname;
        @Bind(R.id.iv_headicon)
        ImageView iv_headicon;
        @Bind(R.id.namedoctor)
        TextView namedoctor;
        //
        @Bind(R.id.tv_location)
        TextView tv_location;
        @Bind(R.id.tv_part)
        TextView tv_part;
        @Bind(R.id.tv_level)
        TextView tv_level;

        @Bind(R.id.layout_line)
        View layout_line;

        @Bind(R.id.layout_line1)
        View layout_line1;
    }
}
