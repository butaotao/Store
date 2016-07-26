package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/3/5.
 */
public class AdapterOftenContact  extends BaseCustomAdapter<BaseSearch>{


    public AdapterOftenContact(Context context, int resId, List<BaseSearch> objects) {
        super(context, resId, objects);
    }


    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHoder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        BaseSearch baseSearch = getItem(position);
        ViewHoder hoder = (ViewHoder)baseViewHolder;
        if (baseSearch instanceof  Doctor){
            Doctor doctor = (Doctor)baseSearch;
            String url = "";
            if (null!=doctor.headPicFileName){
                url = doctor.headPicFileName;
            }
            CustomImagerLoader.getInstance().loadImage(hoder.iv_headicon,url);
            if(!TextUtils.isEmpty(doctor.name) && doctor.name.length() > 8){
                doctor.name = doctor.name.substring(0, 8)+"...";
            }
            hoder.tv_name.setText(doctor.name);
            hoder.tv_customervisit.setText(doctor.title);
            hoder.tv_frenddes.setText(doctor.hospital);
            hoder.tv_customerposition.setText(doctor.departments);
        }else if(baseSearch instanceof CompanyContactListEntity){
            CompanyContactListEntity contact = (CompanyContactListEntity)baseSearch;
            String url = "";
            if (null!=contact.headPicFileName){
                url = contact.headPicFileName;
            }
            CustomImagerLoader.getInstance().loadImage(hoder.iv_headicon, url,  R.drawable.head_icons_company, R.drawable.head_icons_company);
            if(!TextUtils.isEmpty(contact.name) && contact.name.length() > 8){
                contact.name = contact.name.substring(0, 8)+"...";
            }
            hoder.tv_name.setText(contact.name);
            hoder.tv_frenddes.setText(contact.department);
            if(TextUtils.isEmpty(contact.position)){
                hoder.tv_customervisit.setVisibility(View.GONE);
            }else {
                hoder.tv_customervisit.setVisibility(View.VISIBLE);
            }
            hoder.tv_customervisit.setText(contact.position);
            hoder.tv_customerposition.setText("");

            if(position == getCount()-1){
                hoder.line2.setVisibility(View.VISIBLE);
                hoder.line1.setVisibility(View.GONE);
            }else{
                hoder.line2.setVisibility(View.GONE);
                hoder.line1.setVisibility(View.VISIBLE);
            }
        }
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
        @Bind(R.id.tv_customerposition)
        TextView tv_customerposition;
        @Nullable
        @Bind(R.id.line1)
        View line1;

        @Nullable
        @Bind(R.id.line2)
        View line2;
    }
}
