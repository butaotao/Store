package com.dachen.dgroupdoctorcompany.js;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dachen.dgroupdoctorcompany.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageGalleryAdapter extends PagerAdapter {

    private final DisplayImageOptions mImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true)
            .cacheOnDisc(true).resetViewBeforeLoading(true)
            .showImageForEmptyUri(R.drawable.image_download_fail_icon)
            .showImageOnFail(R.drawable.image_download_fail_icon).build();

    private final List<String> mImageUrls;
    private final Context mContext;


    public ImageGalleryAdapter(Context context, List<String> imageItems) {
        mImageUrls = imageItems;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gallery_image, null);
        final ProgressBar progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        ImageLoadingListener listener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //要加上这句
                attacher.update();
                progress_bar.setVisibility(View.GONE);
            }
        };

        String imageUrl = mImageUrls.get(position);

        if (imageUrl.startsWith("http") || imageUrl.startsWith("file") || imageUrl.startsWith("drawable")) {
            ImageLoader.getInstance().displayImage(imageUrl, photoView, mImageOptions, listener);
        } else {
            ImageLoader.getInstance().displayImage("file://" + imageUrl, photoView, mImageOptions, listener);
        }

        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
