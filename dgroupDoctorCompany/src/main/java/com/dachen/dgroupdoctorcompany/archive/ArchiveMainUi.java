package com.dachen.dgroupdoctorcompany.archive;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.imsdk.archive.entity.ArchiveItem;

/**
 * Created by Mcp on 2016/1/11.
 */
public class ArchiveMainUi extends BaseActivity implements View.OnClickListener {

    private static final int REQ_CODE_ARCHIVE_SEARCH = 102;

    private Button btnCategory;
    private LinearLayout vCategoryChoose;
    private View vCategoryBg;
    private ViewPager mViewPager;
    private ArchivePagerAdapter mAdapter;
    private ImageView ivCategoryArrow;

    private int curCategoryIndex;
    private final String[] CAT_ARR = new String[]{ArchiveUtils.CATE_ALL, ArchiveUtils.CATE_DOCUMENT, ArchiveUtils.CATE_PIC,
            ArchiveUtils.CATE_VIDEO, ArchiveUtils.CATE_MUSIC, ArchiveUtils.CATE_OTHER};
    private final int[] CAT_RES_ID_ARR = new int[]{R.id.tv_category_all, R.id.tv_category_document, R.id.tv_category_pic,
            R.id.tv_category_video, R.id.tv_category_music, R.id.tv_category_other};
    private boolean isCateShown;

    private ArchiveMainFragment uploadFrag;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_main_ui);
        mFrom = getIntent().getStringExtra("from");

        initFragment();

        initMyView();
    }

    private void initMyView() {
        btnCategory = (Button) findViewById(R.id.btn_category);
        vCategoryChoose = (LinearLayout) findViewById(R.id.v_category);
        vCategoryBg = findViewById(R.id.v_category_bg);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ivCategoryArrow = (ImageView) findViewById(R.id.iv_category_arrow);
        mAdapter = new ArchivePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCateChoose();
            }
        });
        vCategoryBg.setOnClickListener(this);
        for (int i = 0; i < CAT_RES_ID_ARR.length; i++) {
//            View temp= vCategoryChoose.getChildAt(i);
            View temp = findViewById(CAT_RES_ID_ARR[i]);
            final int index = i;
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    if (curCategoryIndex != index) {
                        uploadFrag.setCategory(CAT_ARR[index]);
                        //receiveFrag.setCategory(CAT_ARR[index]);
                        curCategoryIndex = index;
                        btnCategory.setText(tv.getText());
                    }
                    hideCateChoose();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.search:
                Intent i = new Intent(this, ArchiveSearchUi.class);
                i.putExtra("from", mFrom);
                startActivityForResult(i, REQ_CODE_ARCHIVE_SEARCH);
                break;
            case R.id.v_category_bg:
                hideCateChoose();
                break;
        }
    }

    private void initFragment() {
        uploadFrag = ArchiveMainFragment.getInstance(ArchiveMainFragment.TYPE_UPLOADED, mFrom);
    }

    private void toggleCateChoose() {
//        if(vCategoryChoose.isShown()){
        if (isCateShown) {
//            vCategoryChoose.setVisibility(View.GONE);
            hideCateChoose();
        } else {
            showCateChoose();
        }
    }

    private void hideCateChoose() {
//        vCategoryChoose.setVisibility(View.GONE);
//        ivCategoryArrow.setImageResource(R.drawable.arrow_down_white);
//        vCategoryChoose.setTranslationY();
        isCateShown = false;
        ObjectAnimator.ofFloat(vCategoryChoose, "translationY", 0, -vCategoryChoose.getHeight()).setDuration(200).start();
        ObjectAnimator arrowAnim = ObjectAnimator.ofFloat(ivCategoryArrow, "rotation", 180, 0);
        arrowAnim.setDuration(200);
        arrowAnim.start();
        vCategoryBg.startAnimation(makeBgHideAnim());
    }

    private AlphaAnimation makeBgHideAnim() {
        AlphaAnimation anim = new AlphaAnimation(1, 0);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vCategoryBg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return anim;
    }

    private AlphaAnimation makeBgShowAnim() {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(200);
        return anim;
    }

    private void showCateChoose() {
        isCateShown = true;
        vCategoryChoose.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(vCategoryChoose, "translationY", -vCategoryChoose.getHeight(), 0).setDuration(200).start();
//        ivCategoryArrow.setImageResource(R.drawable.arrow_up_white);
        ObjectAnimator arrowAnim = ObjectAnimator.ofFloat(ivCategoryArrow, "rotation", 0, 180).setDuration(200);
        arrowAnim.start();
        vCategoryBg.startAnimation(makeBgShowAnim());
        vCategoryBg.setVisibility(View.VISIBLE);
    }

    private class ArchivePagerAdapter extends FragmentPagerAdapter {

        public ArchivePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return uploadFrag;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_ARCHIVE_SEARCH) {
            if (resultCode != Activity.RESULT_OK)
                return;
            ArchiveItem item = (ArchiveItem) data.getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
            Intent i = new Intent();
            i.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, item);
            setResult(Activity.RESULT_OK, i);
            finish();
        } else if (requestCode == ArchiveMainFragment.REQ_CODE_ARCHIVE_DETAIL) {
            if (resultCode != Activity.RESULT_OK)
                return;
            ArchiveItem item = (ArchiveItem) data.getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
            Intent i = new Intent();
            i.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, item);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }


}
