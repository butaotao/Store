package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

import java.util.ArrayList;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/12下午8:15.
 * @描述 TODO
 */
public class GuiderActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mVpGuider;
    private ArrayList<ImageView> mDataList;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);

    }

    private void initImageView(String guider) {
        mDataList = new ArrayList<>();
        if ("manager".equals(guider)) {
            ImageView imageView1 = new ImageView(getApplicationContext());
            imageView1.setBackgroundResource(R.drawable.manager_guider1);
            ImageView imageView2 = new ImageView(getApplicationContext());
            imageView2.setBackgroundResource(R.drawable.manager_guider2);
            ImageView imageView3 = new ImageView(getApplicationContext());
            imageView3.setBackgroundResource(R.drawable.manager_guider3);
            ImageView imageView4 = new ImageView(getApplicationContext());
            imageView4.setBackgroundResource(R.drawable.manager_guider4);
            mDataList.add(imageView1);
            mDataList.add(imageView2);
            mDataList.add(imageView3);
            mDataList.add(imageView4);

        } else if ("user".equals(guider)) {
            ImageView imageView1 = new ImageView(getApplicationContext());
            imageView1.setBackgroundResource(R.drawable.user_guider1);
            ImageView imageView2 = new ImageView(getApplicationContext());
            imageView2.setBackgroundResource(R.drawable.user_guider2);
            ImageView imageView3 = new ImageView(getApplicationContext());
            imageView3.setBackgroundResource(R.drawable.user_guider3);
            mDataList.add(imageView1);
            mDataList.add(imageView2);
            mDataList.add(imageView3);
        }
    }

    @Override
    public void initView() {
        super.initView();
        String guider = getIntent().getStringExtra("guider");
        initImageView(guider);
        mVpGuider = (ViewPager) findViewById(R.id.vp_guider);
        mPagerAdapter = new MyPagerAdapter(mDataList);
        mVpGuider.setAdapter(mPagerAdapter);
        mVpGuider.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mPagerAdapter.getCount() - 1) {//最后一页出现两秒后关闭
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    finish();
                }
            }.start();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyPagerAdapter extends PagerAdapter {
        ArrayList<ImageView> dataList;

        public MyPagerAdapter(ArrayList<ImageView> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = dataList.get(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentItem = mVpGuider.getCurrentItem();//点击滑到下一页
                    mVpGuider.setCurrentItem(currentItem + 1);
                    mPagerAdapter.notifyDataSetChanged();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = dataList.get(position);
            container.removeView(view);
        }
    }
}
