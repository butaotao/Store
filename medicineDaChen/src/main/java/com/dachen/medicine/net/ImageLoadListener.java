package com.dachen.medicine.net;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 图片加载监听器<br>
 * 使用异步图片加载时,要实现该监听器,注入到异步加载器中<br>
 * 监听器提供图片加载结束处理方法onImageLoaded和onError重写两个方法实现各个调用处的不同处理
 * 
 * 
 */
public interface ImageLoadListener {
	/**
	 * 图片加载成功回调
	 * 
	 * @param viewHolder
	 *            注入的ViewHolder对象
	 * @param drawable
	 *            要加载的图片drawable对象
	 * @param imageResId
	 *            要加载图片的ImageView资源Id
	 */
	public void onImageLoaded(View viewHolder, Drawable drawable, int imageResId);

	/**
	 * 图片加载错误时回调
	 * 
	 * @param viewHolder
	 *            注入的ViewHolder对象
	 * @param imageResId
	 *            要加载图片的ImageView资源Id
	 */
	public void onError(View viewHolder, int imageResId);
}
