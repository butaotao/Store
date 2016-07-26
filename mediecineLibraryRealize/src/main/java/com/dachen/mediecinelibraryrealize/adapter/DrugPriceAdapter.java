package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.SomeBox;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealize.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/3/26.
 */
public class DrugPriceAdapter extends BaseAdapter{
    private Context                 mContext;
    private List<SomeBox.patientSuggest>            mListData;
    public DrugPriceAdapter(Context context, ArrayList<SomeBox.patientSuggest>data){
        this.mContext = context;
        this.mListData = data;
    }
    @Override
    public int getCount() {
        if(null == mListData){
            return 0;
        }
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        if(null == mListData){
            return null;
        }
        return mListData.get(position);
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
            convertView =View.inflate(mContext, R.layout.item_drug_price,null);
            viewHolder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
            viewHolder.ivFlagSelected = (ImageView) convertView.findViewById(R.id.ivFlagSelected);
            viewHolder.ivSubtract = (ImageView) convertView.findViewById(R.id.ivSubtract) ;
            viewHolder.tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            viewHolder.tvDrugName = (TextView) convertView.findViewById(R.id.tvDrugName);
            viewHolder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SomeBox.patientSuggest data = mListData.get(position);
        if(null != data){
            String name = "";
            String goodsname = "";
            if (null!=data.drug){
                goodsname = data.drug.title;
            }else if(null!=data.title){
                goodsname = data.title;
            }
            name = CompareDatalogic.getShowName(data.general_name,data.trade_name,goodsname);
            viewHolder.tvDrugName.setText(name);
            viewHolder.tvCompany.setText(data.manufacturer);
            viewHolder.tvNum.setText(data.requires_quantity);
            String pack_specification = data.pack_specification;
            String unit="";
            if(null != data.unit){
                unit = data.unit.name;
            }

            String specification = data.specification;//没片药的重量
            int nSpecification = StringUtils.getNum(specification);
            if(0!=nSpecification){
                if(!android.text.TextUtils.isEmpty(unit)){
                    viewHolder.tvUnit.setText(""+nSpecification+"/g"+" "+pack_specification+"/"+unit);
                }else{
                    viewHolder.tvUnit.setText(""+nSpecification+"/g"+" "+pack_specification);
                }

            }else{
                if(!android.text.TextUtils.isEmpty(unit)){
                    viewHolder.tvUnit.setText(pack_specification+"/"+unit);
                }else{
                    viewHolder.tvUnit.setText(pack_specification);
                }

            }

            try{
                int num = Integer.valueOf(viewHolder.tvNum.getText().toString());
                if(num>1){
                    viewHolder.ivSubtract.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ivSubtract.setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            final String strNum = viewHolder.tvNum.getText().toString();
            viewHolder.ivSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        int num = Integer.valueOf(strNum)-1;
                        data.requires_quantity = ""+ num;
                        notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        int num = Integer.valueOf(strNum)+1;
                        data.requires_quantity = ""+ num;
                        notifyDataSetChanged();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.ivFlagSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return convertView;
    }

    private class ViewHolder{
        public ImageView         ivFlagSelected;
        public TextView          tvDrugName;
        public TextView          tvUnit;
        public TextView          tvCompany;
        public ImageView         ivSubtract;
        public ImageView         ivAdd;
        public TextView          tvNum;

    }
}
