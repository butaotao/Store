package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.GoodsGroupEntity;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/6/27.
 */
public abstract class ChoiceMediaAdapter extends android.widget.BaseAdapter{
    private List<GoodsGroupEntity.Data.PageData> mMedieList = new ArrayList<>();
    private Context mContext;

    public ChoiceMediaAdapter(Context context,List<GoodsGroupEntity.Data.PageData>list){
        this.mContext = context;
        mMedieList.clear();
        mMedieList.addAll(list);
    }

    public void notifyData(List<GoodsGroupEntity.Data.PageData>list){
        mMedieList.clear();
        mMedieList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMedieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMedieList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_org,null);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.mTvOrgName = (TextView) convertView.findViewById(R.id.tvOrgName);
            viewHolder.mIvFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mIvFlag.setVisibility(View.GONE);
        final GoodsGroupEntity.Data.PageData medie= mMedieList.get(position);
        if(null != medie){
            String name = "";
            if(!TextUtils.isEmpty(medie.tradeName)){
                name = medie.tradeName;
            }else{
                name = medie.generalName;
            }
            viewHolder.mTvOrgName.setText(name);
            viewHolder.mCheckBox.setChecked(medie.select);
            viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckBoxCheck(medie);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTvOrgName;
        private ImageView mIvFlag;
    }

    protected abstract void onCheckBoxCheck(GoodsGroupEntity.Data.PageData medie);
}
