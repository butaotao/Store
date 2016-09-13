package com.dachen.dgroupdoctorcompany.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSignInActivity;
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectAddressActivity;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.SinUtils;
import com.dachen.dgroupdoctorcompany.utils.HtmlTextViewEdit;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.view.CustomDialog;

/**
 * Created by Burt on 2016/8/27.
 */
public class CustomButtonFragment  extends Fragment {
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    long sixTime;
    boolean works;
    FloatingActionButton leftCenterButton;
    MenuWithFABActivity activity;
    public FloatingActionMenu circleMenu;
    TextView tv_alertnotsign;
    long time;
    public static final String WORK = "上班";
    public static final String OFFWORK = "下班";
    public static final String VISIT = "拜访";
    public void setActivity(MenuWithFABActivity activity,long time) {
            this.activity = activity;

        this.time = time;
        works = false;
        setdata();
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
            tv_alertnotsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog();
                }
            });
            // Our action button is this time just a regular view!

            // Add some items to the menu. They are regular views as well!
            a = new TextView(getActivity());a.setGravity(Gravity.CENTER);
            a.setBackgroundResource(R.drawable.other_icon);
             b = new TextView(getActivity()); b.setGravity(Gravity.CENTER);
            b.setBackgroundResource(R.drawable.visit_icon);
            // c = new TextView(getActivity()); c.setGravity(Gravity.CENTER);
          //  c.setBackgroundResource(R.drawable.class_icon);
             d = new TextView(getActivity()); d.setGravity(Gravity.CENTER);
            d.setBackgroundResource(R.drawable.work_icon);
            final int subsize = getResources().getDimensionPixelSize(R.dimen.subs_action_button_size);
              final FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(subsize, subsize);
            a.setLayoutParams(tvParams);
            b.setLayoutParams(tvParams);
         // c.setLayoutParams(tvParams);
            d.setLayoutParams(tvParams);
            int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);

            int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
            ImageView fabIconStar = new ImageView(activity);
            final FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            final FloatingActionButton.LayoutParams smallParams = new FloatingActionButton.LayoutParams(subsize, subsize);
            int redActionButtonContentMarginBottom =
                    getResources().getDimensionPixelSize(R.dimen.red_action_button_content_marginBottom);
            smallParams.setMargins(redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonContentMarginBottom+5);
            starParams.setMargins(redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonContentMarginBottom);
            fabIconStar.setLayoutParams(starParams);
            int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
            int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);

            FloatingActionButton.LayoutParams fabIconStarParams =
                    new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
            fabIconStarParams.setMargins(redActionButtonContentMargin,
                    redActionButtonContentMargin,
                    redActionButtonContentMargin,
                    redActionButtonContentMarginBottom);

            leftCenterButton = new FloatingActionButton.Builder(activity)
                    .setContentView(fabIconStar, fabIconStarParams)
                    .setBackgroundDrawable(R.drawable.sign_flow_icon)
                    .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                    .setLayoutParams(starParams)
                    .build();



         circleMenu = new FloatingActionMenu.Builder(getActivity())
        .setStartAngle(-158) // A whole circle!
        .setEndAngle(-22)
        .setRadius(getResources().getDimensionPixelSize(R.dimen.radius_large))
         .addSubActionView(a)
         .addSubActionView(d)
        .addSubActionView(b)
        .attachTo(leftCenterButton)
                    .build();
            circleMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingActionMenu menu) {
                    leftCenterButton.setBackgroundResource(R.drawable.sing_close);
                    leftCenterButton.setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER, smallParams) ;
                    tv_alertnotsign.setVisibility(View.GONE);
                    //activity.timeStamp; 1472594400000  1472594400000
                    long nowTime = activity.timeStamp;
                    long yesdayworktime = activity.ytdayWorkTime;
                    long yesdayworkofftime = activity.ytdayOffTime;
                    boolean offwork = haveOffWork();
                 /*   offwork = false;
                    yesdayworkofftime = 0;
                    nowTime = System.currentTimeMillis();*/

                    long twentyTime = com.dachen.medicine.common.utils.TimeUtils.getTime(nowTime,0, 0);
                    sixTime = com.dachen.medicine.common.utils.TimeUtils.getTime(nowTime,6, 0);
                    if (sixTime <= nowTime)//6点之后
                              {
                            workOrOff();
                    } else if (sixTime > nowTime&&
                            nowTime>=twentyTime) {
                        if (yesdayworktime==0){
                            workOrOff();
                        }else if(yesdayworktime!=0&&(yesdayworktime<yesdayworkofftime)){
                            workOrOff();
                        }else if(yesdayworktime!=0&&(yesdayworktime>yesdayworkofftime)){
                          if (offwork) {
                                //说明打过下班卡了
                              works =true;
                              d.setBackgroundResource(R.drawable.work_icon);
                            } else {
                                if (SharedPreferenceUtil.getLong(activity,sixTime+"",0)==0){
                                    tv_alertnotsign.setVisibility(View.VISIBLE);
                                    works =false;
                                    //说明没有打过下班卡
                                    d.setBackgroundResource(R.drawable.class_icon);
                                }else {
                                    works =true;
                                    d.setBackgroundResource(R.drawable.work_icon);
                                }

                             }
                        }else {
                            workOrOff();
                        }
                    }
                    if (haveWork()){
                          workOrOff();
                    }
                }
                @Override
                public void onMenuClosed(FloatingActionMenu menu) {
                     leftCenterButton.setLayoutParams(starParams);
                    leftCenterButton.setBackgroundResource(R.drawable.sign_flow_icon);
                }
            });
            a.setOnClickListener(new myOnclickListener());
             b.setOnClickListener(new myOnclickListener());
           // c.setOnClickListener(new myOnclickListener());
            d.setOnClickListener(new myOnclickListener());
            return rootView;
        }
    public void setdata() {
        if (null!=tv_alertnotsign){
            long nowTime = activity.timeStamp;
            long yesdayworktime = activity.ytdayWorkTime;
            long yesdayworkofftime = activity.ytdayOffTime;
            boolean offwork = haveOffWork();

            long twentyTime = com.dachen.medicine.common.utils.TimeUtils.getTime(nowTime, 0, 0);
            sixTime = com.dachen.medicine.common.utils.TimeUtils.getTime(nowTime, 6, 0);

                tv_alertnotsign.setVisibility(View.GONE);


            if (sixTime > nowTime && nowTime >= twentyTime) {
                if (yesdayworktime == 0) {

                } else if (yesdayworktime != 0 && (yesdayworktime < yesdayworkofftime)) {

                } else if (yesdayworktime != 0 && (yesdayworktime > yesdayworkofftime)) {
                    if (offwork) {
                        //说明打过下班卡了
                    } else {
                        if (SharedPreferenceUtil.getLong(activity, sixTime + "", 0) == 0) {
                            tv_alertnotsign.setVisibility(View.VISIBLE);

                        } else {
                            tv_alertnotsign.setVisibility(View.GONE);
                        }

                    }
                }
            }
        }
    }
    public void workOrOff(){
        boolean haveworksignall = haveWork();
        if (haveworksignall) {
            works =false;
            d.setBackgroundResource(R.drawable.class_icon);
        } else {
            works =true;
            d.setBackgroundResource(R.drawable.work_icon);
        }

    }
     class myOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            activity.compareDistance(activity.longitude,activity.latitude,activity);
            if(v  == a){
                if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){

                    Intent intent = new Intent(activity, AddSignInActivity.class);
                    intent.putExtra("name", activity.address);
                    intent.putExtra("longitude", activity.longitude);
                    intent.putExtra("latitude", activity.latitude);
                    intent.putExtra("addressname",activity.allAddress);
                    intent.putExtra("mode", AddSignInActivity.MODE_WORKING);
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(activity,SelectAddressActivity.class);
                    intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
                    intent.putExtra("poi",activity.POI);
                    intent.putExtra("time",activity.timeStamp);//传入服务器时间
                    intent.putExtra("distance",activity.distance);
                    intent.putExtra("latitude",activity.latitude);
                    intent.putExtra("longitude",activity.longitude);
                    intent.putExtra("city",activity.city);
                    startActivity(intent);


                }
            }else if(v  == b){
                if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){
                    SinUtils.signDefaultvisit(activity, activity.address,
                            activity.longitude, activity.latitude , activity.address,activity.allAddress,  VISIT, false,4);
                }else {
                   // SinUtils.signDefault(activity, activity.address, activity.latitude + "," + activity.longitude, "拜访", false);
                    choicePlace();
                }
            }else  if(v  == d){
                if (!works){
                    workSing(OFFWORK, AddSignInActivity.SIGN_OFFWORKING);
                }else {
                    workSing(WORK,AddSignInActivity.SIGN_WORKING);
                }
                }
            closeMenu();
            }

        }
    public void workSing(String work,int type){

        if (activity.lengh<=activity.allowDistance&&activity.lengh>=0&&!TextUtils.isEmpty(activity.address)){

            SinUtils.signDefault(activity,activity.address,activity.latitude+","+activity.longitude,work,1,-1);
        }else {
            SinUtils.signDefault(activity, activity.address, activity.latitude + "," + activity.longitude,
                    work, 1, type);
        }
    }

    public void choicePlace(){
        Intent intent2 = new Intent(activity,SelectAddressActivity.class);
        intent2.putExtra("mode",AddSignInActivity.MODE_VISIT);
        intent2.putExtra("type","signle");
        intent2.putExtra("poi",activity.POI);
        intent2.putExtra("time",activity.timeStamp);//传入服务器时间
        intent2.putExtra("distance",activity.distance);
        intent2.putExtra("latitude",activity.latitude);
        intent2.putExtra("longitude",activity.longitude);
        intent2.putExtra("city",activity.city);
        startActivity(intent2);
    }
    public boolean haveSignWork(){
        if (null!=activity.mDataLists&&activity.mDataLists.size()>0){
            for (int i=0;i<activity.mDataLists.size();i++){
                if (activity.mDataLists.get(i).tag!=null&&activity.mDataLists.get(i).tag.size()>0){
                    if (activity.mDataLists.get(i).tag.get(0).equals(WORK)||activity.mDataLists.get(i).
                            tag.get(0).equals(OFFWORK)){
                        return true;
                    }
                    ;
                }
            }
        }
        return false;
    }
    public boolean workSingToday(){
        if (null!=activity.mDataLists&&activity.mDataLists.size()>0){
            return true;
        }
        return false;
    }
    public boolean haveOffWork(){
        if (null!=activity.mDataLists&&activity.mDataLists.size()>0){
            for (int i=0;i<activity.mDataLists.size();i++){
                if (activity.mDataLists.get(i).tag!=null&&activity.mDataLists.get(i).tag.size()>0){
                    if (activity.mDataLists.get(i).tag.get(0).equals(OFFWORK) ){
                        return true;
                    }
                    ;
                }
            }
        }
        return false;
    }
    public boolean haveWork(){
        if (null!=activity.mDataLists&&activity.mDataLists.size()>0){
            for (int i=0;i<activity.mDataLists.size();i++){
                if (activity.mDataLists.get(i).tag!=null&&activity.mDataLists.get(i).tag.size()>0){
                    if (activity.mDataLists.get(i).tag.get(0).equals(WORK) ){
                        return true;
                    }
                    ;
                }
            }
        }
        return false;
    }
    public void showAlertDialog(){
        closeMenu();
        final com.dachen.medicine.view.CustomDialog dialog = new CustomDialog(activity);
        dialog.showDialog("", "确认昨天忘记了下班签到", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                tv_alertnotsign.setVisibility(View.GONE);
                SharedPreferenceUtil.putLong(activity, sixTime + "", sixTime);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        });
    }
    public void closeMenu(){
        if (null!=circleMenu){
            circleMenu.close(true);
        }
    }
}
