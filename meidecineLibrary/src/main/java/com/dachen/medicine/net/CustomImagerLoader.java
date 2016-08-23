package com.dachen.medicine.net;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.android.library.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;



import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CustomImagerLoader {
	
	/**
	 * 向UI发送图片加载消息的handler
	 */
	private Handler mHandler = new Handler();
	public static CustomImagerLoader mImageLoader;
	public static List<String> displayedImages  ;
	/**
	 * 获取图片加载器单例
	 * 
	 * @return 图片加载器单例
	 */
	public static CustomImagerLoader getInstance() {
		if (null == mImageLoader) {
			mImageLoader = new CustomImagerLoader();
		}
		if (null == displayedImages) {
			displayedImages = Collections.synchronizedList(new LinkedList<String>());
		}
		return mImageLoader;
	}
	public void loadImage(final View viewHolder, final String imageUrl){
		 loadImage(viewHolder,imageUrl,true,10);
	}
	public void loadImage(final View viewHolder, final String imageUrl,boolean iscircle){
		loadImage(viewHolder,imageUrl,true,360);
	}
	@SuppressWarnings("deprecation")
	public void loadImage(final View viewHolder,final String imageUrl,boolean small,int dushu) {
		 final DisplayImageOptions options;
		 
		 options =  new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.image_download_fail_icon)
			.showImageForEmptyUri(R.drawable.image_download_fail_icon)
			.showImageOnFail(R.drawable.image_download_fail_icon)
			 .bitmapConfig(Bitmap.Config.RGB_565)
				 .cacheOnDisk(true)
			.cacheInMemory(true) 
			.considerExifParams(true)
			.delayBeforeLoading(1)
			.displayer(new RoundedBitmapDisplayer(dushu))
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
	    ImageSize mImageSize = new ImageSize(32, 32);
		String image = imageUrl;
		if (small) {
			if (image != null && (image.startsWith("http")||image.startsWith("https"))) {
				image += "-small1";
			}
		}
		final String finalImage = image;
		/*ImageLoader.getInstance().loadImage(finalImage, mImageSize,options, new SimpleImageLoadingListener(){
				 
		            @Override
		            public void onLoadingComplete(String imageUri, View view,
		                    Bitmap loadedImage) {
		                super.onLoadingComplete(imageUri, view, loadedImage);
		                boolean firstDisplay = !displayedImages.contains(imageUri+viewHolder);
						if (firstDisplay) {
		                ImageView imageView = (ImageView) viewHolder;
		                imageView.setImageBitmap(loadedImage);
		                displayedImages.add(imageUri+viewHolder);
						}
		            } 
		            @Override
		            public void onLoadingCancelled(String imageUri, View view) {
		            // TODO Auto-generated method stub
		            super.onLoadingCancelled(imageUri, view);
		            boolean firstDisplay = !displayedImages.contains(imageUri+viewHolder);
					if (firstDisplay) {
		            ImageLoader.getInstance().displayImage(finalImage, (ImageView) viewHolder, options);
		            displayedImages.add(imageUri+viewHolder);
					}
		            }
		            @Override
		            public void onLoadingStarted(String imageUri, View view) {
		            // TODO Auto-generated method stub
		            super.onLoadingStarted(imageUri, view);
		            }
		            @Override
		            public void onLoadingFailed(String imageUri, View view,
		            	FailReason failReason) {
		            // TODO Auto-generated method stub
		            super.onLoadingFailed(imageUri, view, failReason);
		            }
		            
		        	
		        });  */
			   ImageLoader.getInstance().displayImage(finalImage, (ImageView) viewHolder, options);


	}
	public void loadImage(final View viewHolder, final String imageUrl,int loading,int failloading ){
		loadImage(viewHolder,imageUrl,loading,failloading,true);
	};
	@SuppressWarnings("deprecation")
	public void loadImage(final View viewHolder, final String imageUrl,int loading,int failloading,boolean small) {
		final DisplayImageOptions options;

		options =  new DisplayImageOptions.Builder()
				.showImageOnLoading(loading)
				.showImageForEmptyUri(failloading)
				.showImageOnFail(failloading)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheOnDisk(true)
				.cacheInMemory(true)
				.considerExifParams(true)
				.delayBeforeLoading(1)
				.displayer(new RoundedBitmapDisplayer(10))
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.build();
		ImageSize mImageSize = new ImageSize(32, 32);
		String image = imageUrl;
		if (small){
			image +="-small1";
		}
		final String finalImage = image;
/*		ImageLoader.getInstance().loadImage(finalImage+"", mImageSize,options, new SimpleImageLoadingListener(){

			@Override
			public void onLoadingComplete(String imageUri, View view,
										  Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				boolean firstDisplay = !displayedImages.contains(imageUri+viewHolder);
				if (firstDisplay) {
					ImageView imageView = (ImageView) viewHolder;
					imageView.setImageBitmap(loadedImage);
					displayedImages.add(imageUri+viewHolder);
				}
			}
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				super.onLoadingCancelled(imageUri, view);
				boolean firstDisplay = !displayedImages.contains(imageUri+viewHolder);
				if (firstDisplay) {
					ImageLoader.getInstance().displayImage(finalImage, (ImageView) viewHolder, options);
					displayedImages.add(imageUri+viewHolder);
				}
			}
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				super.onLoadingStarted(imageUri, view);
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,
										FailReason failReason) {
				// TODO Auto-generated method stub
				super.onLoadingFailed(imageUri, view, failReason);
			}


		});*/
		ImageLoader.getInstance().displayImage(finalImage, (ImageView) viewHolder, options);


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
