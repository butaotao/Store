package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.utils.ErCording;
import com.google.zxing.WriterException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/19.
 */
public class ErCordingActivity extends BaseActivity{
    @Nullable
    @Bind(R.id.iv_scanadd)
    ImageView iv_scanadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ercording);
        ButterKnife.bind(this);
        setTitle("二维码名片");
       String code = getIntent().getStringExtra("ercode");
        Bitmap map = null;
        try {
            map = ErCording.cretaeBitmap(code + "");
        } catch (WriterException e) {
            e.printStackTrace();
        }
        iv_scanadd.setImageBitmap(map);
    }
}
