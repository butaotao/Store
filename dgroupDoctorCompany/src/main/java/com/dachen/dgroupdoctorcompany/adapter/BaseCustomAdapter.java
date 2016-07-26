package com.dachen.dgroupdoctorcompany.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 
 * @author WANG
 * 
 * @param <T>
 *            T是实体类的泛型
 */
public abstract class BaseCustomAdapter<T> extends BaseAdapter {
	protected List<T> mObjects;
	protected Context mContext;
	protected Activity mActivity;
	protected LayoutInflater mInflater;
	protected int mItemLayoutId;
	protected int selectedPosition;

	public BaseCustomAdapter(Context context, int resId) {
		init(context, resId, new ArrayList<T>());
	}

	public BaseCustomAdapter(Context context, int resId, List<T> objects) {
		init(context, resId, objects);
	}

	public BaseCustomAdapter(Context context, int resId, T[] objects) {
		init(context, resId, Arrays.asList(objects));

	}

	public BaseCustomAdapter(Context context, int resId, List<T> objects,
							 ChoiceItemNumInterface iteminterface) {
		init(context, resId, objects);
	}

	private void init(Context context, int resId, List<T> objects) {
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}

		mContext = context.getApplicationContext();
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItemLayoutId = resId;
		mObjects = objects;
	}

	/**
	 * Returns the context associated with this array adapter. The context is
	 * used to create views from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mObjects == null ? 0 : mObjects.size();
	}

	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				mItemLayoutId);
	}

	/**
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @param resource
	 * @return
	 */
	protected View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {

		BaseViewHolder holder = null;
		View v = convertView;

		if (v == null) {
			v = mInflater.inflate(resource, parent, false);
			holder = getViewHolder();
			assert v != null;
			// initialize(holder, v);
			ButterKnife.bind(holder, v);
			v.setTag(holder);
		} else {
			holder = (BaseViewHolder) v.getTag();
		}
		fillValues(holder, position);

		return v;
	}

	public abstract static class BaseViewHolder {
	}

	protected abstract BaseViewHolder getViewHolder();

	// protected abstract void initialize(BaseViewHolder baseViewHolder, View
	// v);

	protected abstract void fillValues(BaseViewHolder baseViewHolder,
			int position);

	/*
	 * 扩展API
	 */

	public List<T> getItems() {
		return mObjects;
	}

	public void setItems(List<T> items) {
		this.mObjects = items;
		notifyDataSetChanged();
	}
	public void refreshItems(List<T> items){
		mObjects.clear();
		mObjects.addAll(items);
		notifyDataSetChanged();
	}

	public void addItem(T item) {
		mObjects.add(item);
		notifyDataSetChanged();
	}

	public void addItems(List<T> items) {
		this.mObjects.addAll(items);
		notifyDataSetChanged();
	}

	public void addItems(List<T> items, int count) {
		addItems(items.subList(0, count));
	}

	public void addItems(T[] array) {
		List<T> lst = new ArrayList<T>(Arrays.asList(array));
		addItems(lst);
	}

	public void addItems(T[] array, int count) {
		List<T> lst = new ArrayList<T>(Arrays.asList(array));
		addItems(lst, count);
	}

	public void setItem(int position, T item) {
		mObjects.set(position, item);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		mObjects.remove(index);
		notifyDataSetChanged();
	}

	public void remove(T item) {
		mObjects.remove(item);
		notifyDataSetChanged();
	}

	public void clear() {
		mObjects.clear();
		notifyDataSetChanged();
	}

	public int indexOf(T item) {
		return mObjects.indexOf(item);
	}

	public T getSelectedItem() {
		if (selectedPosition < 0 || selectedPosition >= getCount()) {
			return null;
		} else {
			return mObjects.get(selectedPosition);
		}
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
}
