package com.dachen.dgroupdoctorcompany.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.PersonModel;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.medicine.net.CustomImagerLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/6/27.
 */
public class MemberAdapter extends BaseAdapter<PersonModel> {

    private ViewHolder holder;

    /**
     * @param context
     */
    public MemberAdapter(Context context) {
        super(context);
    }

    class ViewHolder {
        TextView member_name;
        ImageView member_avatar;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.member_item_layout, null);
            holder = new ViewHolder();
            holder.member_avatar = (ImageView) convertView.findViewById(R.id.member_avatar);
            holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.member_name.setText(dataSet.get(position).getName());
//        ImageLoader.getInstance().displayImage(dataSet.get(position).getHeadPic(), holder.member_avatar, CommonUitls.getHeadOptions());
        CustomImagerLoader.getInstance().loadImage(holder.member_avatar, dataSet.get(position).getHeadPic(),
                R.drawable.ic_default_head, R.drawable.ic_default_head);
        return convertView;
    }
}
