package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * 图片查看器
 * 
 * @author gaozhuo
 * @date 2015年9月15日
 *
 */
public class PhotoViewerActivity extends BaseActivity {
	public static final String INTENT_EXTRA_IMAGE_URL = "intent_extra_image_url";

	private final DisplayImageOptions mImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true)
			.cacheOnDisc(true).resetViewBeforeLoading(true)
			.showImageForEmptyUri(R.drawable.image_download_fail_icon)
			.showImageOnFail(R.drawable.image_download_fail_icon).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_viewer);

		String imageUrl = getIntent().getStringExtra(INTENT_EXTRA_IMAGE_URL);

		if(TextUtils.isEmpty(imageUrl)){
			return;
		}

		PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
		final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
		attacher.setOnViewTapListener(new OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, android.R.anim.fade_out);
			}
		});

		if (!ImageLoader.getInstance().isInited()) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
			ImageLoader.getInstance().init(config);
		}

		ImageLoadingListener listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				//要加上这句
				attacher.update();
			}
		};

		if(imageUrl.startsWith("http") || imageUrl.startsWith("file") || imageUrl.startsWith("drawable")){
			ImageLoader.getInstance().displayImage(imageUrl, photoView, mImageOptions, listener);
		}else {
			ImageLoader.getInstance().displayImage("file://" + imageUrl, photoView, mImageOptions, listener);
		}
	}

}
