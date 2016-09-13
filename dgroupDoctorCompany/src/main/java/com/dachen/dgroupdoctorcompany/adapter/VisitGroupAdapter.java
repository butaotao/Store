package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.VisitGroupEntity;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/6/24.
 */
public abstract class VisitGroupAdapter extends android.widget.BaseAdapter {
    private List<VisitGroupEntity.Data.VistGroupItem> mVistGroupItemList = new ArrayList<>();
    private Context mContext;

    public VisitGroupAdapter(Context context,List<VisitGroupEntity.Data.VistGroupItem> list){
        this.mContext = context;
        this.mVistGroupItemList.clear();
        this.mVistGroupItemList.addAll(list);
    }

    public void notifyData(List<VisitGroupEntity.Data.VistGroupItem> list){
        mVistGroupItemList.clear();
        mVistGroupItemList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mVistGroupItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVistGroupItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_visit_group,null);
            viewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvVisitName = (TextView) convertView.findViewById(R.id.tvVisitName);
            viewHolder.btAdd = (Button) convertView.findViewById(R.id.btAdd);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final VisitGroupEntity.Data.VistGroupItem vistGroupItem = mVistGroupItemList.get(position);
        if(null != vistGroupItem){
            String picture = vistGroupItem.headPic;
            String initatorName = vistGroupItem.initatorName;
            String doctorName = vistGroupItem.doctorName;
            String address = vistGroupItem.addressName;
            CustomImagerLoader.getInstance().loadImage(viewHolder.ivPicture, picture,
                    R.drawable.icon_visit_pic, R.drawable.icon_visit_pic);
            viewHolder.tvAddress.setText(address);
            viewHolder.tvName.setText(initatorName);
            if(!TextUtils.isEmpty(doctorName)){
                viewHolder.tvVisitName.setText("拜访 "+doctorName);
            }else{
                viewHolder.tvVisitName.setText("不记名拜访");
            }

            viewHolder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdd(vistGroupItem);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder{
        private ImageView ivPicture;
        private TextView tvName;
        private TextView tvVisitName;
        private Button btAdd;
        private TextView tvAddress;
    }

    public abstract void onClickAdd(VisitGroupEntity.Data.VistGroupItem vistGroupItem);
}
