package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.City;

/**
 * Created by Burt on 2016/2/23.
 */
public class CityAdapter extends BaseAdapter<City> {

    private Context mContext;
    private ViewHolder holder;

    public CityAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item_layout, null);
            holder = new ViewHolder();
            holder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            holder.tv_arrow = (TextView) convertView.findViewById(R.id.tv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final City bean = dataSet.get(position);
        holder.tv_area.setText(bean.getName());
        holder.tv_arrow.setVisibility(View.GONE);
        return convertView;
    }

    public class ViewHolder {
        TextView tv_area;
        TextView tv_arrow;
    }
}