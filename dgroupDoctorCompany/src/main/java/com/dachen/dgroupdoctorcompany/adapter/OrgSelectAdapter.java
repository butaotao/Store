package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.OrgEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/5/20.
 */
public abstract class OrgSelectAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private List<OrgEntity.Data> mDepamentsList = new ArrayList<>();
    CompanyContactListEntity entity;

    public OrgSelectAdapter(Context context, List<OrgEntity.Data> data, CompanyContactListEntity entity) {
        this.mContext = context;
        this.mDepamentsList.clear();
        this.entity = entity;
        this.mDepamentsList.addAll(data);
    }

    public void update(List<OrgEntity.Data> data) {
        mDepamentsList.clear();
        mDepamentsList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDepamentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDepamentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_org, null);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.mTvOrgName = (TextView) convertView.findViewById(R.id.tvOrgName);
            viewHolder.mIvFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrgEntity.Data depaments = mDepamentsList.get(position);
        if (null != depaments) {
            String name = depaments.name;
            viewHolder.mTvOrgName.setText(name);
            ArrayList<OrgEntity.Data> arrayList = depaments.subList;
            if (arrayList.size() == 0) {
                viewHolder.mIvFlag.setVisibility(View.GONE);
            } else {
                viewHolder.mIvFlag.setVisibility(View.VISIBLE);
            }
            if (entity.id.equals(depaments.id)) {//设定默认中
                viewHolder.mCheckBox.setChecked(true);
                viewHolder.mCheckBox.setBackgroundResource(R.drawable.icon_pay_disable);
                viewHolder.mCheckBox.setClickable(false);
                viewHolder.mCheckBox.setOnCheckedChangeListener(null);
            } else {
                viewHolder.mCheckBox.setChecked(depaments.isCheck);
                if (depaments.isCheck) {
                    viewHolder.mCheckBox.setBackgroundResource(R.drawable.icon_pay_selected);
                } else {
                    viewHolder.mCheckBox.setBackgroundResource(R.drawable.icon_pay_unselect);
                }
                viewHolder.mCheckBox.setClickable(true);
                viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCheckBoxCheck(depaments);
                    }
                });
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private CheckBox mCheckBox;
        private TextView mTvOrgName;
        private ImageView mIvFlag;
    }

    protected abstract void onCheckBoxCheck(OrgEntity.Data item);
}
