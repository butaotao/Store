package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoFactory;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Burt on 2016/6/3.
 */
public class MedicienSearchListAdapter extends BaseAdapter{
    List<MedicienInfoFactory> mInfo_lists;
    Context context;
    public MedicienSearchListAdapter(Context context,List<MedicienInfoFactory> mInfo_list){
        this.mInfo_lists = mInfo_list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return mInfo_lists.size();

    }

    @Override
    public Object getItem(int position) {
        return mInfo_lists.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        // 获取数据
        MedicienInfoFactory medicienInfo = mInfo_lists.get(position);

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {

            viewHolder = new ViewHolder();
            convertView = View.inflate(context,
                    com.dachen.mediecinelibraryrealizedoctor.R.layout.item_listview_choosemediciendoctor, null);

            viewHolder.mIv_lv_usualmedicien = (ImageView) convertView
                    .findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.iv_lv_usualmedicien);
            viewHolder.mTv_lv_medicienname = (TextView) convertView
                    .findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.tv_lv_medicienname);
            viewHolder.mTv_lv_company = (TextView) convertView
                    .findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.tv_lv_company);
            // 箭头
            viewHolder.rl_lv_right_arrow = (RelativeLayout) convertView
                    .findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.rl_lv_right_arrow);


            convertView.setTag(viewHolder);
        }
        if (!medicienInfo.check){
            viewHolder.mIv_lv_usualmedicien.setImageResource(com.dachen.mediecinelibraryrealizedoctor.R.drawable.medicien_unchecked);
        }else {
            viewHolder.mIv_lv_usualmedicien.setImageResource(com.dachen.mediecinelibraryrealizedoctor.R.drawable.choice);
        }

        viewHolder.rl_lv_right_arrow.setVisibility(View.GONE);
        // 点击条目。将数据返回
        final ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                MedicienInfoFactory medicienInfo = mInfo_lists.get(position);
                Bundle bundle = new Bundle();
                for (int i = 0;i<mInfo_lists.size();i++){
                    MedicienInfoFactory medicien  = mInfo_lists.get(i);
                    if (i==position){
                        medicien.check = true;
                    }else {
                        medicien.check = false;
                    }
                    mInfo_lists.set(i,medicien);
                }
                MedicineInfo info = new MedicineInfo();
                info.goods$image_url = medicienInfo.image_url;
                if (null!=medicienInfo.goods$manufacturer) {
                    info.goods$manufacturer = medicienInfo.goods$manufacturer;
                }else {
                    info.goods$manufacturer = medicienInfo.manufacturer;
                }
                info.goods$pack_specification = medicienInfo.pack_specification;
                info.goods$specification = medicienInfo.specification;
                info.goods$general_name = medicienInfo.general_name;
                info.goods$biz_type = medicienInfo.biz_type;
                info.goods$type = medicienInfo.type;
                info.goods$trade_name = medicienInfo.trade_name;
                info.goods$number = medicienInfo.number;
                info.goods_usages_goods = medicienInfo.goods_usages;
                info.goods$type = medicienInfo.type;
                MedicineInfo.GoodForm form = info.new GoodForm();
                if (null!=medicienInfo.form){
                    form.name = medicienInfo.form.name;
                }
                info.goods$form = form;
                info.form = medicienInfo.form;
                info.id = medicienInfo.id;
                MedicineEntity entity = new MedicineEntity();
                MedicineEntity.Goods goods = entity.new Goods();
                goods.id = medicienInfo.id;
                goods.title = medicienInfo.title;
                info.goods = goods;
                bundle.putSerializable("choice", (Serializable)info);
                intent.putExtra("choice", bundle);
                notifyDataSetChanged();
            }
        });
        // 绑定数据
        viewHolder.mTv_lv_medicienname.setText(medicienInfo.title);
        viewHolder.mTv_lv_company.setText(medicienInfo.manufacturer);

        return convertView;
    }



class ViewHolder {
    private ImageView mIv_lv_usualmedicien;

    private TextView mTv_lv_medicienname;

    private TextView mTv_lv_company;

    private RelativeLayout rl_lv_right_arrow;

}
}
