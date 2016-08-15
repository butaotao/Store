package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.TabPagerAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.fragment.DoctorFriend;
import com.dachen.dgroupdoctorcompany.fragment.WaitVerify;
import com.dachen.medicine.view.NoScrollerViewPager;
import com.dachen.medicine.view.SegementView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/24.
 */
public class AddDoctorActivity extends BaseActivity implements View.OnClickListener{
    @Nullable
    @Bind(R.id.patient_contacts_viewPager)
    NoScrollerViewPager mViewPager;
    private List<Fragment> mFragmentList;

    private TabPagerAdapter mPagerAdapter;
    ViewStub vstub_title;
    View view ;
    RelativeLayout ll_sub;
    @Nullable
    @Bind(R.id.rl_plus)
    RelativeLayout rl_plus;
    @Nullable
    @Bind(R.id.patient_contacts_segementview)
    SegementView mSegementView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adddoctor);
        ButterKnife.bind(this);
        ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        view = vstub_title.inflate(this, R.layout.layout_plus_medie, ll_sub);
        view.findViewById(R.id.rl_plus).setOnClickListener(this);
        ImageView viewImage = (ImageView) view.findViewById(R.id.iv_plus1);
        viewImage.setBackgroundResource(R.drawable.add);
        String name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)){
            setTitle(name);
        }
        initData();
        initEvent();
    }
    void toSearchActivity(){
        Intent intent = new Intent(this,SearchDoctorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_plus:
                toSearchActivity();
                break;
        }
    }

    private void initData() {
        mFragmentList = new ArrayList<Fragment>();
        DoctorFriend add1 = new DoctorFriend();
        WaitVerify add2 = new WaitVerify();
        mFragmentList.add(add1);
        mFragmentList.add(add2);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setFragments(mFragmentList);

        mViewPager.setAdapter(mPagerAdapter);


    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mSegementView.setOnSelectedListener(new SegementView.OnSelectedListener() {

            @Override
            public void onSelected(boolean leftSelected) {
                if (leftSelected) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(1);
                }
            }
        });

    }
}
