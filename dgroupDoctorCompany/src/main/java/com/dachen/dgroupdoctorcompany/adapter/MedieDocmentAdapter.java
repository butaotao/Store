package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.bean.FileSizeBean;
import com.dachen.common.utils.StringUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.MedieManagementActivity;
import com.dachen.dgroupdoctorcompany.entity.MedieDocument;
import com.dachen.imsdk.archive.ArchiveUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by weiwei on 2016/2/27.
 */
public abstract class MedieDocmentAdapter extends android.widget.BaseAdapter {

    private List<MedieDocument.Data.MedieDocumentItem> mList;
    private Context context;
    private LayoutInflater inflater;
    private SimpleDateFormat mFormat;
    private int mMode;
    private DisplayImageOptions opts;

    public MedieDocmentAdapter(Context context, List<MedieDocument.Data.MedieDocumentItem> mList,int mode) {
        this.context = context;
        this.mList = mList;
        this.inflater=LayoutInflater.from(context);
        this.mMode = mode;
        mFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        opts = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri( R.drawable.file_image)
                .showImageOnFail(R.drawable.file_image)
                .build();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_medie_document,parent,false);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvSize= (TextView) convertView.findViewById(R.id.tv_size);
            holder.tvTime= (TextView) convertView.findViewById(R.id.tv_time);
            holder.ivPic= (ImageView) convertView.findViewById(R.id.iv_file_pic);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        final MedieDocument.Data.MedieDocumentItem item=mList.get(position);
        if(MedieManagementActivity.MODE_USER_INFO == mMode){
            holder.checkBox.setVisibility(View.GONE);
        }else if(MedieManagementActivity.MODE_ADD_FRIEND == mMode){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isSelected = !item.isSelected;
                    onClickSelected(item.isSelected);
                }
            });
        }
        String mimeType = StringUtils.getMimeType(item.suffix);
        if(mimeType != null && mimeType.startsWith("image")) {
            ImageLoader.getInstance().displayImage(item.url + "?imageView2/3/h/100/w/100", holder.ivPic, opts);
        }else {
            holder.ivPic.setImageResource(ArchiveUtils.getFileIcon(item.suffix));
        }
        boolean isSelected = item.isSelected;
        if(isSelected){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.tvName.setText(item.name);
        holder.tvTime.setText(getDateString(item));
        holder.tvSize.setText(getSizeString(item));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MedieManagementActivity.MODE_ADD_FRIEND == mMode){
                    item.isSelected = !item.isSelected;
                    notifyDataSetChanged();
                    return;
                }
                onClickItem(item);
            }
        });
        return convertView;
    }

    private String getDateString(MedieDocument.Data.MedieDocumentItem item){
        long ts=item.lastUpdateDate;
        /*if(ts==0)
            ts=item.sendDate;*/
        return mFormat.format(new Date(ts));
    }

    private String getSizeString(MedieDocument.Data.MedieDocumentItem item){
        int size = 0;
        try {
            size = Integer.parseInt(item.size);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        FileSizeBean bean = new FileSizeBean(size);
        return bean.getSizeStr();
    }
    private class ViewHolder{
        public TextView tvName;
        public TextView tvTime;
        public TextView tvSize;
        public ImageView ivPic;
        public CheckBox checkBox;
    }

    //    protected abstract View.OnClickListener makeClickListener(ArchiveItem item);
    protected abstract void onClickItem(MedieDocument.Data.MedieDocumentItem item);

    protected abstract void onClickSelected(boolean isSelected);
}
