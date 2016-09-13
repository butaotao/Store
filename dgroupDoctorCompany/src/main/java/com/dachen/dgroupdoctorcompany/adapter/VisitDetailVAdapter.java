package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.archive.ImageDetailActivity;
import com.dachen.dgroupdoctorcompany.entity.VistDetails;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/29.
 */
public class VisitDetailVAdapter extends android.widget.BaseAdapter {
    Context context;
    ArrayList<VistDetails.Sum> sums;

    public VisitDetailVAdapter(Context context, ArrayList<VistDetails.Sum> sums) {
        this.context = context;
        this.sums = sums;
    }

    @Override
    public int getCount() {
        return sums.size();
    }

    @Override
    public Object getItem(int position) {
        return sums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder;
        final VistDetails.Sum sum = (VistDetails.Sum) getItem(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_visitdetail, null);
            hoder = new ViewHoder();
            hoder.horizonlistview = (GridView) convertView.findViewById(R.id.horizonlistview);
            hoder.iv_people = (ImageView) convertView.findViewById(R.id.iv_people);
            hoder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            hoder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            hoder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        ArrayList<String> list = new ArrayList<>();
        if (null != sum.imgUrls) {
            list = sum.imgUrls;
        }

        ImageHorizonAdapter adapter = new ImageHorizonAdapter(context, list);
        hoder.horizonlistview.setAdapter(adapter);
        SetListViewHeight(hoder.horizonlistview);
        hoder.horizonlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> listImage = new ArrayList<>();
                if (null != sum.imgUrls) {
                    listImage = sum.imgUrls;
                }
                List<ArchiveItem> imageItems = new ArrayList<ArchiveItem>();
                for(int i=0;i<listImage.size();i++){
                    ArchiveItem archiveItem = new ArchiveItem();
                    archiveItem.suffix = "png";
                    archiveItem.url = listImage.get(i);
                    archiveItem.name = "";
                    imageItems.add(archiveItem);
                }
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra("imageItems", (ArrayList<ArchiveItem>) imageItems);
                intent.putExtra("position", position);
                intent.putExtra("from", "MedieDocumentActicity");//隐藏转发按钮
                intent.putExtra("mode", "VisitDetail");//隐藏转发按钮
                context.startActivity(intent);
            }
        });
        CustomImagerLoader.getInstance().loadImage(hoder.iv_people, sum.headPic);
        hoder.tv_name.setText(sum.name);
        if(TextUtils.isEmpty(sum.remark)){
            hoder.tv_des.setHint("无");
        }else{
            hoder.tv_des.setText(sum.remark);
        }
       // String time = TimeUtils.getTimeRecord(sum.time);
        hoder.tv_time.setText(TimeUtils.getTimeDay3(sum.time)+TimeUtils.getTimesHourMinute(sum.time));
        return convertView;
    }

    public static class ViewHoder {
        ImageView iv_people;
        TextView tv_name;
        TextView tv_time;
        TextView tv_des;
        GridView horizonlistview;
    }

    private void SetListViewHeight(GridView listview) {
        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int line = 0;
        int size = listAdapter.getCount() / 4;
        int last = listAdapter.getCount() % 4;
        if (size == 0 && last > 0) {
            line = 1;
        } else if (size == 0 && last == 0) {
            line = 0;
        } else if (size == 1 && last > 0) {
            line = 2;
        } else if (size == 1 && last == 0) {
            line = 1;
        } else if (size == 2) {
            line = 2;
        } else if (size > 1) {
            line = 2;
        }

        for (int i = 0; i < (line); i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
//
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listview.setLayoutParams(params);
    }
}
