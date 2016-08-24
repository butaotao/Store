package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/19上午10:49.
 * @描述 TODO
 */
public class CompanyListGuide extends BaseAdapter<String> {
    public CompanyListGuide(Context context) {
        super(context);
    }

    public CompanyListGuide(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_company_listguide, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == dataSet.size()-1) {
            holder.icptext.setTextColor(Color.parseColor("#555555"));
            holder.icptext.setClickable(false);
        }else {
            holder.icptext.setClickable(true);
            holder.icptext.setTextColor(Color.parseColor("#3cbaff"));
        }
        String text = dataSet.get(position);
        holder.icptext.setText(text);
        return convertView;
    }

    public class ViewHolder {
        public final TextView icptext;
        public final View root;

        public ViewHolder(View root) {
            icptext = (TextView) root.findViewById(R.id.i_cp_text);
            //  icptext.setTextSize(DisplayUtil.pixelToDp(mContext,34));
            this.root = root;
        }
    }
}
