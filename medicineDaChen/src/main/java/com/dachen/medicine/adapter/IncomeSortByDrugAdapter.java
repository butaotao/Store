package com.dachen.medicine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.bean.IncomeListPageData;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;

/**
 * Created by TianWei on 2016/3/15.
 */
public class IncomeSortByDrugAdapter extends BaseCustomAdapter<IncomeListPageData> {
    public IncomeSortByDrugAdapter(Context context, int resId,
                                   List<IncomeListPageData> objects) {
        super(context, resId, objects);
    }

    public IncomeSortByDrugAdapter(Context context, int resId) {
        super(context, resId);
    }

    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        IncomeListPageData item = getItem(position);
        ViewHolder holder = (ViewHolder) baseViewHolder;
        if (item != null) {
            holder.tvName.setText(item.drugName);
            holder.tvPackSpecification.setText(item.packSpecification);
            holder.tvCompany.setText(item.companyName);
            float money = (float) item.money / 100;
            DecimalFormat df = new DecimalFormat("0.00");
            holder.tvMoney.setText(String.valueOf(df.format(money)) + "元");
        }
    }

    class ViewHolder extends BaseViewHolder {

        @Bind(R.id.tv_name)
        TextView tvName;

        @Bind(R.id.tv_pack_specification)
        TextView tvPackSpecification;

        @Bind(R.id.tv_company)
        TextView tvCompany;

        @Bind(R.id.tv_money)
        TextView tvMoney;

    }
}
