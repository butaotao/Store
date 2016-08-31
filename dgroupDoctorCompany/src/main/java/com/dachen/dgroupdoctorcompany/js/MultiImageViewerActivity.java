package com.dachen.dgroupdoctorcompany.js;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

import java.util.List;

public class MultiImageViewerActivity extends Activity {
    private List<String> mImageUrls;
    private int mPosition;
    private ViewPager mViewPager;
    private ImageGalleryAdapter mImageGalleryAdapter;
    private TextView mCurrentPage;
    private TextView mTotalPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_viewer);

        initVariables();

        initView();
    }

    private void initView() {
        mCurrentPage = (TextView) findViewById(R.id.currentPage);
        mTotalPage = (TextView) findViewById(R.id.totalCount);
        mCurrentPage.setText(mPosition + 1 + "");
        mTotalPage.setText(mImageUrls.size() + "");

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mImageGalleryAdapter = new ImageGalleryAdapter(this, mImageUrls);
        mViewPager.setAdapter(mImageGalleryAdapter);
        mViewPager.setCurrentItem(mPosition);

    }

    private void initVariables() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", 0);
        mImageUrls = intent.getStringArrayListExtra("imageUrls");
    }


    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            mCurrentPage.setText(mPosition + 1 + "");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
