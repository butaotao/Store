package com.dachen.dgroupdoctorcompany.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSignInActivity;
import com.dachen.dgroupdoctorcompany.activity.CustomerVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectAddressActivity;
import com.dachen.dgroupdoctorcompany.activity.SelfVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.SignInActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.SinUtils;
import com.dachen.dgroupdoctorcompany.utils.HtmlTextViewEdit;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

/**
 * Created by Burt on 2016/8/27.
 */
public class CustomButtonFragment  extends Fragment {
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    MenuWithFABActivity activity;
    public FloatingActionMenu circleMenu;
    TextView tv_alertnotsign;
    public void setActivity(MenuWithFABActivity activity) {
            this.activity = activity;
    }
    public CustomButtonFragment() {
        this.activity = activity;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_with_custom_action_button, container, false);
            tv_alertnotsign = (TextView) rootView.findViewById(R.id.tv_alertnotsign);
            tv_alertnotsign.setText(HtmlTextViewEdit.getNotSignAlert());
            // Our action button is this time just a regular view!
            final Button centerActionButton = (Button) rootView.findViewById(R.id.centerActionButton);

            // Add some items to the menu. They are regular views as well!
            a = new TextView(getActivity());a.setGravity(Gravity.CENTER);
            a.setBackgroundResource(R.drawable.other_icon);
             b = new TextView(getActivity()); b.setGravity(Gravity.CENTER);
            b.setBackgroundResource(R.drawable.visit_icon);
            // c = new TextView(getActivity()); c.setGravity(Gravity.CENTER);
          //  c.setBackgroundResource(R.drawable.class_icon);
             d = new TextView(getActivity()); d.setGravity(Gravity.CENTER);
            d.setBackgroundResource(R.drawable.work_icon);
            int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
              FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            a.setLayoutParams(tvParams);
            b.setLayoutParams(tvParams);
         // c.setLayoutParams(tvParams);
            d.setLayoutParams(tvParams);

            int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);
            FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            SubActionButton.Builder subBuilder = new SubActionButton.Builder(getActivity());

            circleMenu = new FloatingActionMenu.Builder(getActivity())
                    .setStartAngle(-160) // A whole circle!
                    .setEndAngle(-20)
                    .setRadius(getResources().getDimensionPixelSize(R.dimen.radius_large))
                    .addSubActionView(a)
                    .addSubActionView(d)
                    .addSubActionView(b)
                    .attachTo(centerActionButton)
                    .build();
            circleMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingActionMenu menu) {
                    centerActionButton.setBackgroundResource(R.drawable.sing_close);
                    if (activity.mDataLists.size()>0){
                        d.setBackgroundResource(R.drawable.class_icon);
                    }else {
                        d.setBackgroundResource(R.drawable.work_icon);
                    }
                }

                @Override
                public void onMenuClosed(FloatingActionMenu menu) {

                    centerActionButton.setBackgroundResource(R.drawable.sign_flow_icon);
                }
            });
            a.setOnClickListener(new myOnclickListener());
             b.setOnClickListener(new myOnclickListener());
           // c.setOnClickListener(new myOnclickListener());
            d.setOnClickListener(new myOnclickListener());
            return rootView;
        }
     class myOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v  == a){
                if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){

                    Intent intent = new Intent(activity, AddSignInActivity.class);
                    intent.putExtra("name", activity.address);
                    intent.putExtra("longitude", activity.longitude);
                    intent.putExtra("latitude", activity.latitude);
                    intent.putExtra("mode", AddSignInActivity.MODE_WORKING);
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(activity,SelectAddressActivity.class);
                    intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
                    intent.putExtra("poi",activity.POI);
                    intent.putExtra("singmode",AddSignInActivity.SIGN_WORKING);
                    intent.putExtra("distance",activity.distance);
                    intent.putExtra("latitude",activity.latitude);
                    intent.putExtra("longitude",activity.longitude);
                    intent.putExtra("city",activity.city);
                    startActivity(intent);


                }
            }else if(v  == b){
                if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){
                    SinUtils.signDefault(activity, activity.address, activity.latitude + "," + activity.longitude, "拜访", true);
                }else {
                   // SinUtils.signDefault(activity, activity.address, activity.latitude + "," + activity.longitude, "拜访", false);
                    choicePlace();
                }
            }else  if(v  == d){
                if (activity.mDataLists.size()>0){
                    tv_alertnotsign.setVisibility(View.VISIBLE);
                    workSing("下班", AddSignInActivity.SIGN_OFFWORKING);
                }else {
                    workSing("上班",AddSignInActivity.MODE_WORKING);
                }
                }
            circleMenu.close(true);
            }

        }
    public void workSing(String work,int type){
        if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){

            SinUtils.signDefault(activity,activity.address,activity.latitude+","+activity.longitude,work,true);
        }else {
            Intent intent = new Intent(activity,SelectAddressActivity.class);
            intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
            intent.putExtra("poi",activity.POI);
            intent.putExtra("singmode",type);
            intent.putExtra("distance",activity.distance);
            intent.putExtra("latitude",activity.latitude);
            intent.putExtra("longitude",activity.longitude);
            intent.putExtra("city",activity.city);
            startActivity(intent);
        }
    }
    public void choicePlace(){
        Intent intent2 = new Intent(activity,SelectAddressActivity.class);
        intent2.putExtra("mode",AddSignInActivity.MODE_VISIT);
        intent2.putExtra("type","signle");
        intent2.putExtra("poi",activity.POI);
        intent2.putExtra("distance",activity.distance);
        intent2.putExtra("latitude",activity.latitude);
        intent2.putExtra("longitude",activity.longitude);
        intent2.putExtra("city",activity.city);
        startActivity(intent2);
    }
}
