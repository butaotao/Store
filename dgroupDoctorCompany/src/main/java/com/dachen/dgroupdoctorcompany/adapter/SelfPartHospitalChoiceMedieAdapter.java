package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/5/19.
 */
public class SelfPartHospitalChoiceMedieAdapter extends android.widget.BaseAdapter{
    Context context;
    ArrayList<MedieEntity.Medie> data;
    public SelfPartHospitalChoiceMedieAdapter(Context context,ArrayList<MedieEntity.Medie> data ){
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final MedieEntity.Medie me = (MedieEntity.Medie) getItem(position);
        if (null ==convertView){
            convertView = View.inflate(context, R.layout.adapter_selfparthospitalchoicemedie,null);
            holder = new ViewHolder();
            holder.radionbutton = (RadioButton) convertView.findViewById(R.id.radionbutton);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.radionbutton.setChecked(false);
        if (me.select){
            holder.radionbutton.setChecked(true);
        }
        holder.tv_name.setText(me.drugName);
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    MedieEntity mes = new MedieEntity();
                    MedieEntity.Medie medie = mes.new Medie();
                    medie = data.get(i);

                    medie.select = i == position;
                    data.set(i, medie);
                }

                holder.radionbutton.setChecked(true);
                notifyDataSetChanged();
            }
        });
        holder.radionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<data.size();i++){
                    MedieEntity.Medie medie = data.get(i);

                    medie.select = i == position;
                    data.set(i, medie);
                }

                holder.radionbutton.setChecked(true);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    public static class ViewHolder{
        TextView tv_name;
        RadioButton radionbutton;
        RelativeLayout rl_item;
    }
}
