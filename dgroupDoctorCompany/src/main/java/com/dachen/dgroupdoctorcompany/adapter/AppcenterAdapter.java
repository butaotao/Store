package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.MyAppBean;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/5上午9:46.
 * @描述 企业应用中心的条目adapter
 */
public class AppcenterAdapter extends BaseAdapter<MyAppBean.DataBean.PageDataBean> {
    List<MyAppBean.DataBean.PageDataBean> pageData;
    Context mContext;
    public AppcenterAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public AppcenterAdapter(Context context, List<MyAppBean.DataBean.PageDataBean> data) {
        super(context, data);
        this.pageData = data;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_appcenter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyAppBean.DataBean.PageDataBean bean = pageData.get(position);
        CustomImagerLoader.getInstance().loadImage(holder.ivappcenterimg,bean.pic);
        holder.tvacname.setText(bean.name);
        holder.tvacdes.setText(bean.desc);
        return convertView;
    }

    public class ViewHolder {
        public final ImageView ivappcenterimg;
        public final TextView tvacname;
        public final TextView tvacdes;
        public final RelativeLayout rlappcenteritem;
        public final View root;

        public ViewHolder(View root) {
            ivappcenterimg = (ImageView) root.findViewById(R.id.iv_appcenter_img);
            tvacname = (TextView) root.findViewById(R.id.tv_ac_name);
            tvacdes = (TextView) root.findViewById(R.id.tv_ac_des);
            rlappcenteritem = (RelativeLayout) root.findViewById(R.id.rl_appcenter_item);
            this.root = root;
        }
    }
}
