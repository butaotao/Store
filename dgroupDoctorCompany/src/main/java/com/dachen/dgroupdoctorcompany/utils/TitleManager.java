package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

/**
 * Created by Burt on 2016/6/28.
 */
public class TitleManager {
    public static void showText(Activity context,View viewlayout,View.OnClickListener listener,String title){
        RelativeLayout rl = (RelativeLayout) viewlayout.findViewById(R.id.ll_sub);
        ViewStub stub = (ViewStub) viewlayout.findViewById(R.id.vstub_title);
        View view = stub.inflate(context,  R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        tv.setOnClickListener(listener);
        tv.setText(title);
    }
}
