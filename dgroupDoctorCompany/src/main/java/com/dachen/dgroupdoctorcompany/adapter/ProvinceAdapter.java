package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/2/23.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.Province;


/**
 * [A brief description]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-9-14
 *
 **/
public class ProvinceAdapter extends BaseAdapter<Province> {

    private Context mContext;
    private ViewHolder holder;

    public ProvinceAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item_layout,null);
            holder = new ViewHolder();
            holder.tv_area=(TextView)convertView.findViewById(R.id.tv_area);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Province bean = dataSet.get(position);
        holder.tv_area.setText(bean.getName());
        return convertView;
    }

    public class ViewHolder{
        TextView tv_area;
    }

}
