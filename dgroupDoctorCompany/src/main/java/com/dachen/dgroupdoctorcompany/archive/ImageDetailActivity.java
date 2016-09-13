package com.dachen.dgroupdoctorcompany.archive;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.dachen.common.utils.BitmapUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.imsdk.archive.download.ArchiveLoader;
import com.dachen.imsdk.archive.download.ArchiveTaskInfo;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends ArchiveItemDetailUi {

    private List<ArchiveItem> mImageItems;
    private int mPosition;
    private ViewPager mViewPager;
    private ImageGalleryAdapter mImageGalleryAdapter;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = this.getIntent().getStringExtra("mode");
        if("VisitDetail".equals(mode)){
            findViewById(R.id.vBottom).setVisibility(View.GONE);
            findViewById(R.id.vLine).setVisibility(View.GONE);
        }
        mShare.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.archive_content_image_detail;
    }


    @Override
    protected void onContentViewLoaded(View v) {
        bindViews(v);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mImageItems = (List<ArchiveItem>) extras.getSerializable("imageItems");
                mPosition = extras.getInt("position");
            }
        }
        setUpViewPager();
    }


    private final ViewPager.OnPageChangeListener mViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager != null) {
                mPosition = position;
                setActionBarTitle(position);
                mItem = mImageItems.get(position);
                ArchiveTaskInfo info = ArchiveLoader.getInstance().getInfo(mImageItems.get(position));
                changeActionBtnStatus(info.state, mImageItems.get(position).getSizeStr());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void bindViews(View v) {
        mViewPager = (ViewPager) v.findViewById(R.id.vp);
    }

    private void setUpViewPager() {
        mImageGalleryAdapter = new ImageGalleryAdapter(this, mImageItems);
        mViewPager.setAdapter(mImageGalleryAdapter);
        mViewPager.setOnPageChangeListener(mViewPagerOnPageChangeListener);
        mViewPager.setCurrentItem(mPosition);

        setActionBarTitle(mPosition);
    }

    private void setActionBarTitle(int position) {
        if (mViewPager != null && mImageItems.size() >= 1) {
            int totalPages = mViewPager.getAdapter().getCount();
            if (tvTitle != null) {
                tvTitle.setText(String.format("%d/%d", (position + 1), totalPages));
            }
        }
    }

    @Override
    protected void onDownloadFinished() {
        View view = mViewPager.findViewWithTag("key_" + mPosition);
        if (view == null) {
            return;
        }
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);
        if (photoView == null) {
            return;
        }

        File file = ArchiveLoader.getInstance().getDownloadFile(mImageItems.get(mPosition));
        if (file != null && file.exists()) {
            int[] size = BitmapUtils.getImageWH(file.getAbsolutePath());
            size = BitmapUtils.getSuitableSize(size[0], size[1]);
            ImageLoader.getInstance().loadImage("file://" + file.getAbsolutePath(), new ImageSize(size[0], size[1]), ImageGalleryAdapter.sOptions,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            photoView.setImageBitmap(loadedImage);
                        }
                    });

        }

    }
}
