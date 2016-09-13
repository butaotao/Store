package com.dachen.dgroupdoctorcompany.archive;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.StringUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mcp on 2016/1/12.
 */
public abstract class ArchiveListAdapter extends BaseAdapter {

    private List<ArchiveItem> mList;
    private Context context;
    private LayoutInflater inflater;
    private SimpleDateFormat mFormat;
    private DisplayImageOptions opts;

    public ArchiveListAdapter(Context context, List<ArchiveItem> mList) {
        this.context = context;
        this.mList = mList;
        this.inflater = LayoutInflater.from(context);
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        opts = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.archive_main_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvSize = (TextView) convertView.findViewById(R.id.tv_size);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_file_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ArchiveItem item = mList.get(position);
        String mimeType = StringUtils.getMimeType(item.suffix);
        if (mimeType != null && mimeType.startsWith("image")) {
            ImageLoader.getInstance().displayImage(item.url + "?imageView2/3/h/100/w/100", holder.ivPic, opts);
        } else {
            holder.ivPic.setImageResource(ArchiveUtils.getFileIcon(item.suffix));
        }
        holder.tvName.setText(item.name);
        holder.tvTime.setText(getDateString(item));
        holder.tvSize.setText(item.getSizeStr());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(item);
            }
        });
        return convertView;
    }

    private String getDateString(ArchiveItem item) {
        long ts = item.uploadDate;
        if (ts == 0)
            ts = item.sendDate;
        return mFormat.format(new Date(ts));
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvTime;
        public TextView tvSize;
        public ImageView ivPic;
    }

    //    protected abstract View.OnClickListener makeClickListener(ArchiveItem item);
    protected abstract void onClickItem(ArchiveItem item);
}
