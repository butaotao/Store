package com.dachen.medicine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.income.IncomeActivity;
import com.dachen.medicine.bean.IncomeDetailBean;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianWei on 2016/3/16.
 */
public class IncomeMonthDetailAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<IncomeDetailBean> mData = new ArrayList<IncomeDetailBean>();
    private String mFrom;

    public IncomeMonthDetailAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addData(List<IncomeDetailBean> data) {
        if (data != null) {
            mData.addAll(data);
        }
    }

    public List<IncomeDetailBean> getData() {
        return mData;
    }

    public void addChildData(List<IncomeDetailBean.Data> data) {
        if (data != null) {
            mData.get(mData.size() - 1).list.addAll(data);
        }
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mData != null && mData.get(groupPosition).list != null) {
            return mData.get(groupPosition).list.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mData != null) {
            return mData.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mData != null && mData.get(groupPosition).list != null) {
            return mData.get(groupPosition).list.get(childPosition);
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_income_month_detail_group, null);
            convertView.setTag(holder);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_month);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tvMonth.setText(mData.get(groupPosition).yM);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_income_month_detail_child, null);
            convertView.setTag(holder);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        IncomeDetailBean.Data data = mData.get(groupPosition).list.get(childPosition);
        String manager = SharedPreferenceUtil.getString("shop_manager", "");
        holder.tvTime.setText(data.formatDate);
        if (!TextUtils.isEmpty(data.bizText)) {
            if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_GIVE)) {
                if ("11".equals(data.logType)) {
                    holder.tvTitle.setText(data.bizText);
                } else {
                    holder.tvTitle.setText(data.bizText + "【" + data.drugTitle + "】");
                }
            } else {
                holder.tvTitle.setText(data.bizText + data.drugCode);
            }
        } else {
            if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_GIVE)) {
                if ("11".equals(data.logType)) {
                    holder.tvTitle.setText("收到银行打款");
                } else {
                    if (TextUtils.isEmpty(manager)){
                        holder.tvTitle.setText((data.clerkName == null ? "" : data.clerkName) + "售出【" + data.drugTitle + "】");
                    }else {
                        holder.tvTitle.setText((data.clerkName == null ? "我" : data.clerkName) + "售出【" + data.drugTitle + "】");
                    }
                }
            } else {
                if (TextUtils.isEmpty(manager)){
                    holder.tvTitle.setText((data.clerkName == null ? "" : data.clerkName) + "售出" + data.drugCode);
                }else {
                    holder.tvTitle.setText((data.clerkName == null ? "我" : data.clerkName) + "售出" + data.drugCode);
                }
            }
        }
        float money = (float) data.money / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        if ("11".equals(data.logType)){
            holder.tvMoney.setTextColor(Color.RED);
        }else{
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }
        holder.tvMoney.setText(String.valueOf(df.format(money)) + "元");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView tvMonth;
    }

    class ChildViewHolder {
        TextView tvTime;
        TextView tvTitle;
        TextView tvMoney;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public String getFrom() {
        return mFrom;
    }
}
