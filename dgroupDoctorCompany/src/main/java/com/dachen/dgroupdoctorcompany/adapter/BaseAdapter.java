package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/2/23.
 */
import android.content.Context;
import android.view.LayoutInflater;

import com.dachen.dgroupdoctorcompany.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * [适配器基类]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2014-10-30
 *
 **/
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected List<T> dataSet;
    protected Context mContext;
    protected LayoutInflater mInflater;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public BaseAdapter(Context context) {
        this(context, new ArrayList());
    }

    public BaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.dataSet = data;
        mInflater = LayoutInflater.from(mContext);
    }

    public Context getContext() {
        return this.mContext;
    }

    public void addData(T data) {
        if (data != null) {
            this.dataSet.add(data);
        }
    }

    public void addData(int index, T data) {
        if (data != null) {
            this.dataSet.add(index, data);
            notifyDataSetChanged();
        }
    }

    public void addData(Collection<T> data) {
        if (data != null) {
            this.dataSet.addAll(data);
        }
    }

    public void addData(int index, Collection<T> data) {
        if (data != null) {
            this.dataSet.addAll(index, data);
        }
    }

    public void removeData(Collection<T> data) {
        if (data != null) {
            this.dataSet.removeAll(data);
        }
    }

    public void removeAll() {
        this.dataSet.clear();
    }

    public void remove(T data) {
        if (data != null) {
            this.dataSet.remove(data);
        }
    }

    public void remove(int position) {
        this.dataSet.remove(position);
    }

    public List<T> subData(int index, int count) {
        return this.dataSet.subList(index, index + count);
    }

    @Override
    public int getCount() {
        return dataSet == null ? 0 : this.dataSet.size();
    }

    @Override
    public T getItem(int position) {
        return this.dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<T> dataSet) {
        this.dataSet = dataSet;
    }

    public DisplayImageOptions getHeadOptions() {
        return getOptions(R.drawable.ic_default_head);
    }

    public DisplayImageOptions getOptions() {
        return getOptions(R.drawable.no_images);
    }

    public DisplayImageOptions getOptions(int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(resId)
                .showImageOnFail(resId).displayer(new RoundedBitmapDisplayer(3)).cacheInMemory(true).cacheOnDisc(true)
                .build();
        return options;
    }
}