package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/3/4.
 */
public class DoctorAdapter extends BaseCustomAdapter<Doctor>{


    public DoctorAdapter(Context context, int resId, List<Doctor> objects) {
        super(context, resId, objects);
    }


    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHoder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        Doctor doctor = getItem(position);
        ViewHoder hoder = (ViewHoder)baseViewHolder;

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
    }
}
