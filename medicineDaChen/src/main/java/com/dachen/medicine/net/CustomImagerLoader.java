package com.dachen.medicine.net;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomImagerLoader {

	/**
	 * 向UI发送图片加载消息的handler
	 */
	private Handler mHandler = new Handler();
	public static CustomImagerLoader mImageLoader;

	/**
	 * 获取图片加载器单例
	 * 
	 * @return 图片加载器单例
	 */
	public static CustomImagerLoader getInstance() {
		if (null == mImageLoader) {
			mImageLoader = new CustomImagerLoader();
		}

		return mImageLoader;
	}

	public void loadImage(final View viewHolder, final String imageUrl) {
		ImageLoader.getInstance()
				.displayImage(imageUrl, (ImageView) viewHolder);
	}

	/*  *//**
	 * 通知UI加载图片结束
	 * 
	 * @param drawable
	 *            图片drawable
	 * @param viewHolder
	 *            缓存viewHolder对象
	 * @param imageResId
	 *            imageview的资源ID
	 * @param listener
	 *            图片加载监听器
	 */
	/*
	 * private synchronized void notifyUI(final Drawable drawable, final View
	 * viewHolder, final int imageResId, final ImageLoadListener listener) {
	 * mHandler.post(new Runnable() {
	 * 
	 * @Override public void run() { // drawable不为空,发送加载成功消息 if (drawable !=
	 * null) { listener.onImageLoaded(viewHolder, drawable, imageResId); } //
	 * drawable为空,发送加载失败消息 else { listener.onError(viewHolder, imageResId); } }
	 * }); } public synchronized void downloadImageByUrl(final View viewHolder,
	 * String imageUrl, final int imageResId, final ImageLoadListener
	 * imageLoadListener) { notifyUI(drawable, viewHolder, imageResId,
	 * imageLoadListener);
	 * 
	 * }
	 */
}
