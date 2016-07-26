package com.dachen.dgroupdoctorcompany.archive;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.dachen.common.utils.BitmapUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.imsdk.archive.download.ArchiveLoader;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class ImageGalleryAdapter extends PagerAdapter {

    private final List<ArchiveItem> mImageItems;
    private final Context mContext;

    public final static DisplayImageOptions sOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .resetViewBeforeLoading(true)
            .showImageForEmptyUri(R.drawable.image_download_fail_icon)
            .showImageOnFail(R.drawable.image_download_fail_icon)
            .build();

    public ImageGalleryAdapter(Context context, List<ArchiveItem> imageItems) {
        mImageItems = imageItems;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.photo_viewer, null);
        view.setTag("key_" + position);
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mImageItems.get(position).suffix);
        if (null != mimeType && mimeType.startsWith("image")) {
            if (mImageItems.get(position).isLocal()) {
                File file = ArchiveLoader.getInstance().getDownloadFile(mImageItems.get(position));
                if (file != null && file.exists()) {
                    int[] size = BitmapUtils.getImageWH(file.getAbsolutePath());
                    size = BitmapUtils.getSuitableSize(size[0], size[1]);
                    ImageLoader.getInstance().loadImage("file://" + file.getAbsolutePath(), new ImageSize(size[0], size[1]), sOptions,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    photoView.setImageBitmap(loadedImage);
                                }
                            });

                }
            } else {
                String imageUrl = mImageItems.get(position).url + "?imageView2/3/h/200/w/300";
                ImageLoader.getInstance().displayImage(imageUrl, photoView, CompanyApplication.mNormalImageOptions);
            }
        }

        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return mImageItems.size();
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
