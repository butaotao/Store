package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.json.EmptyResult;
import com.dachen.common.toolbox.DCommonRequest;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VersionUtils;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.common.widget.dialog.MessageDialog;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Group;
import com.dachen.dgroupdoctorcompany.entity.ResultData;
import com.dachen.dgroupdoctorcompany.entity.VersionInfo;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.fragment.CompanyCenterFragment;
import com.dachen.dgroupdoctorcompany.fragment.FragmentUtils;
import com.dachen.dgroupdoctorcompany.fragment.InfomationFragment;
import com.dachen.dgroupdoctorcompany.fragment.MyFragment;
import com.dachen.dgroupdoctorcompany.im.AppImConstants;
import com.dachen.dgroupdoctorcompany.im.events.UnreadEvent;
import com.dachen.dgroupdoctorcompany.im.utils.AppImUtils;
import com.dachen.dgroupdoctorcompany.receiver.ChangeReceiver;
import com.dachen.dgroupdoctorcompany.receiver.HwPushReceiver;
import com.dachen.dgroupdoctorcompany.service.CallSmsSafeService;
import com.dachen.dgroupdoctorcompany.service.VersionUpdateService;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.dgroupdoctorcompany.views.GuiderDialog;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.imsdk.utils.BuildUtils;
import com.dachen.imsdk.utils.PushUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.volley.custom.ObjectResult;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot1.event.EventBus;

/**
 * Created by Burt on 2016/2/18.
 */

/**
 * @des 打开程序的主界面进来的时候需要判断程序是否设置过密码
 */
public class MainActivity extends BaseActivity implements OnHttpListener,
        OnClickListener, GaoDeMapUtils.LocationListener {
    private static MainActivity instance;
    public static  boolean sShowInfomation = false;
    private long startTime = 0;
    private int fragment_index = 0;
    protected List<Fragment> fragments;
    int tab = -1;
    @Nullable
    @Bind(R.id.infomation_btn)
    ImageView infomation_btn;
    @Nullable
    @Bind(R.id.contactlistfragment_btn)
    ImageView contactlistfragment_btn;
    @Nullable
    @Bind(R.id.companycenter_btn)
    ImageView companycenter_btn;
    @Nullable
    @Bind(R.id.my_btn)
    ImageView my_btn;

    @Nullable
    @Bind(R.id.tv_infomation)
    TextView tv_infomation;
    @Nullable
    @Bind(R.id.mainActivity_settingsfragment_txt)
    TextView mainActivity_settingsfragment_txt;
    @Nullable
    @Bind(R.id.tv_companycenter)
    TextView tv_companycenter;
    @Nullable
    @Bind(R.id.tv_my)
    TextView tv_my;
    @Bind(R.id.me_num_tv)
    View me_num_tv;

    @Bind(R.id.home_num_tv)
    TextView home_num_tv;

    public Group mGroupInfo;
    private ChatGroupDao mDao;

    private GaoDeMapUtils mGaoDeMapUtils;
    private double latitude;//纬度
    private double longitude;//经度
    private String city;//城市
    private String mStrFloor;
    private String mStrAddressName;
    ChangeReceiver receiver;
    public static String action = "changeFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        mDao = new ChatGroupDao();
        ButterKnife.bind(this);
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
        }
        if (savedInstanceState == null) {
            FragmentUtils.changeFragment(getSupportFragmentManager(),
                    R.id.fragment_container, fragments, fragment_index);
        }
        init();
        UpdateConfig.setDebug(false); // 正式版设置为false，调试
        UmengUpdateAgent.setUpdateCheckConfig(false); // 正式版设置为false，如果正确完成不会出现任何提示，否则会以如下的toast提示您。
        UmengUpdateAgent.setUpdateOnlyWifi(false); // 正式版设置为false，需要在任意网络环境下都进行更新自动提醒
        UmengUpdateAgent.setUpdateAutoPopup(true);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.forceUpdate(MainActivity.this);//这行如果是强制更新就一定加上
        //forceUpdate();
        showItem();

        String login = getIntent().getStringExtra("login");
        if (!TextUtils.isEmpty(login)) {
            regeisterPush();
        }
        EventBus.getDefault().register(this);
        isCustomer(true);
        mGaoDeMapUtils = new GaoDeMapUtils(this.getApplicationContext(), this);

        getVersion();
        if (SharedPreferenceUtil.getString(this,"showguider","0").equals("0")){
            GuiderDialog dialog = new GuiderDialog(this);
            dialog.showDialog();
            SharedPreferenceUtil.putString(this,"showguider","1");
        }

     /*  Intent intent = new Intent(this,GuiderDialogActivity.class);
        startActivity(intent);*/

        receiver = new ChangeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                int tab = intent.getIntExtra("tab",0);
                if (tab==0){
                    fragment_index = 0;
                    clicks();
                    showItem();
                }
            }
        };;
        IntentFilter filters= new IntentFilter();
        filters.addAction(action);
        registerReceiver(receiver, filters);
    }


    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;// 系统版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        instance = null;
        if (null!=receiver){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sShowInfomation) {
            clicks();
            sShowInfomation = false;
        }
        UserInfo userInfo = UserInfo.getInstance(this);
      /*  ImSdk.getInstance().initUser(userInfo.getSesstion(),
                userInfo.getId(), userInfo.getUserName(), userInfo.getNickName(), userInfo.getHeadUrl());*/
        new HomeUnreadCountTask().execute();
        new UnreadCountAsyncTask().execute();
    }

    private void init() {
        InfomationFragment infomationFragment = new InfomationFragment();
        AddressList addressList = new AddressList();
        CompanyCenterFragment companyCenterFragment = new CompanyCenterFragment();
        MyFragment myFragment = new MyFragment();
        fragments.clear();
        fragments.add(infomationFragment);
        fragments.add(addressList);
        fragments.add(companyCenterFragment);
        fragments.add(myFragment);

        FragmentUtils.changeFragment(getSupportFragmentManager(),
                R.id.fragment_container, fragments, fragment_index);



    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @OnClick(R.id.infomation_layout)//消息
    public void clicks() {
        fragment_index = 0;
        FragmentUtils.changeFragment(getSupportFragmentManager(),
                R.id.fragment_container, fragments, fragment_index);
        showItem();
    }

    @OnClick(R.id.contactlistfragment_layout)//通讯录
    public void clickscontactlayout() {
        fragment_index = 1;
        FragmentUtils.changeFragment(getSupportFragmentManager(),
                R.id.fragment_container, fragments, fragment_index);
        Intent intent = new Intent(AddressList.action);
        sendBroadcast(intent);
        showItem();
    }

    @OnClick(R.id.companycenterfragment_layout)//企业中心
    public void clickscompanycenterlayout() {
        // ToastUtils.showToast("===clickSettings====");
        fragment_index = 2;
        FragmentUtils.changeFragment(getSupportFragmentManager(),
                R.id.fragment_container, fragments, fragment_index);
        showItem();
    }

    @OnClick(R.id.my_layout)//我的
    public void clicksmylayoutlayout() {
        // ToastUtils.showToast("===clickSettings====");
        if(!isActive)return;
        fragment_index = 3;
        FragmentUtils.changeFragment(getSupportFragmentManager(),
                R.id.fragment_container, fragments, fragment_index);
        showItem();
    }
    public void showItem(){
        infomation_btn.setBackgroundResource(R.drawable.infomation);
        contactlistfragment_btn.setBackgroundResource(R.drawable.addresslist);
        companycenter_btn.setBackgroundResource(R.drawable.companycenter);
        my_btn.setBackgroundResource(R.drawable.myfragement);

        tv_infomation.setTextColor(getResources().getColor(R.color.fragementnoselectcolor));
        mainActivity_settingsfragment_txt.setTextColor(getResources().getColor(R.color.fragementnoselectcolor));
        tv_companycenter.setTextColor(getResources().getColor(R.color.fragementnoselectcolor));
        tv_my.setTextColor(getResources().getColor(R.color.fragementnoselectcolor));

        if (fragment_index == 0) {
            infomation_btn.setBackgroundColor(getResources().getColor(R.color.white));
            infomation_btn.setBackgroundResource(R.drawable.infomation_select);
            tv_infomation.setTextColor(getResources().getColor(R.color.fragementselectcolor));
        } else if (fragment_index == 1) {
            contactlistfragment_btn.setBackgroundColor(getResources().getColor(R.color.white));
            contactlistfragment_btn.setBackgroundResource(R.drawable.addresslist_select);
            mainActivity_settingsfragment_txt.setTextColor(getResources().getColor(R.color.fragementselectcolor));
        } else if (fragment_index == 2) {
            companycenter_btn.setBackgroundColor(getResources().getColor(R.color.white));
            companycenter_btn.setBackgroundResource(R.drawable.companycenter_select);
            tv_companycenter.setTextColor(getResources().getColor(R.color.fragementselectcolor));
        } else if (fragment_index == 3) {
            my_btn.setBackgroundColor(getResources().getColor(R.color.white));
            my_btn.setBackgroundResource(R.drawable.myfragement_select);
            tv_my.setTextColor(getResources().getColor(R.color.fragementselectcolor));
        }
    }

    @Override
    public void onSuccess(ArrayList response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Result response) {
        // TODO Auto-generated method stub
        if (response instanceof ResultData) {
            ResultData resultData = (ResultData) response;
        }
    }


    private void isCustomer(final boolean silent) {
        final String reqTag = "isCustomer";
        RequestQueue queue = VolleyUtil.getQueue(this);
        queue.cancelAll(reqTag);
        StringRequest request = new DCommonRequest(Method.POST, AppConfig.getUrl(Constants.PUB_ISCUSTOMER, 3), new Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleCustomerResponse(response, silent);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", ImSdk.getInstance().accessToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    private void handleCustomerResponse(String response, boolean silent) {
        ObjectResult<Group> result = JSON.parseObject(response, new TypeReference<ObjectResult<Group>>() {
        });
        if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null) {
            ToastUtil.showErrorNet(this);
            return;
        }
        Group group = result.getData();
        if (group.isCustomer == 0 && group.group == null) {
            ToastUtil.showErrorNet(this);
            return;
        }
        mGroupInfo = group;
        if (silent) {
            new UnreadCountAsyncTask().execute();
        } else {
        }
    }

    class UnreadCountAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return getMeUnreadCount();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            showUnreadCount(result);
        }
    }

    public int getMeUnreadCount() {
        String groupType;
        if (mGroupInfo == null) {
            return 0;
        }
        if (mGroupInfo.isCustomer == 1) {
            groupType = AppImConstants.RTYPE_FEEDBACK_PUB_ADMIN;
            return mDao.getUnreadCount(new String[]{groupType});
        } else {
            return mDao.getUnreadCountForId(new String[]{mGroupInfo.group.gid});
        }
    }

    private void showUnreadCount(Integer unreadCount) {
        if (unreadCount <= 0) {
            me_num_tv.setVisibility(View.GONE);
        } else {
            me_num_tv.setVisibility(View.VISIBLE);
        }
    }

    private class HomeUnreadCountTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return mDao.getUnreadCount(AppImUtils.getBizTypes());
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result > 0) {
                home_num_tv.setText(String.valueOf(result));
                home_num_tv.setVisibility(View.VISIBLE);
            } else {
                home_num_tv.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onEventMainThread(UnreadEvent event) {
        if (this == event.from)
            return;
        if (event.type == UnreadEvent.TYPE_CUSTOMER) {
            new UnreadCountAsyncTask().execute();
        }
    }

    public void onEventMainThread(NewMsgEvent event) {
        new UnreadCountAsyncTask().execute();
        new HomeUnreadCountTask().execute();
    }

    public void regeisterPush() {
      /*  LogUtils.burtLog("mRegId=="+ SharedPreferenceUtil.getString("mRegId", ""));
        HashMap<String, String> infaces = new HashMap<String, String>();
        infaces.put("interface1", Constants.XIAOMI);*/
       /* new HttpManager().post(this, Constants.XIAOMI + "", ResultData.class,
                Params.getReginsterXiaoMiReceiverRe(this, SharedPreferenceUtil.getString(this, "id", ""),
                        SharedPreferenceUtil.getString(this, "mRegId", ""),
                        SharedPreferenceUtil.getString(this, "session", ""), Constants.USER_TYPE), this,false,
                3);*/
        String xmPushId=SharedPreferenceUtil.getString(this, "mRegId", "");
        if(BuildUtils.isHuaweiSystem()){
            if(!TextUtils.isEmpty(xmPushId)){
                PushUtils.removeDevice(xmPushId, new Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        EmptyResult res=JSON.parseObject(s,EmptyResult.class);
                        if(res!=null&&res.resultCode==1){
                            SharedPreferenceUtil.putString(mThis, "mRegId", "");
                        }
                    }
                });
            }
            PushUtils.registerDevice(Constants.USER_TYPEC,SharedPreferenceUtil.getString(this, HwPushReceiver.SP_KEY_TOKEN, ""),"",true);
        }else{
            PushUtils.registerDevice(Constants.USER_TYPEC,SharedPreferenceUtil.getString(this, "mRegId", ""),"");
        }

        /*new HttpManager().post(this, Constants.LOGIN + "", LoginRegisterResult.class,
                Params.getLoginParams(phoneNum, password, userType, this), this,
                false, 3);*/
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (startTime == 0) {
//			this.mApplication.getActivityManager().finishAllActivityExceptOne(
//					MainActivity.class);
            startTime = System.currentTimeMillis();
            MActivityManager.getInstance().finishAllActivityExceptMainActivity();
            ToastUtils.showToast(this, getString(R.string.once_back_again));
            return;
        }
        long endTime = System.currentTimeMillis();
        if (endTime - startTime < 2000) {
            MActivityManager.getInstance().finishAllActivity();
            this.finish();
        } else {
            startTime = System.currentTimeMillis();
            ToastUtils.showToast(this, getString(R.string.once_back_again));
        }
    }

    @Override
    public void onLocation(Object object) {
        if (null != object) {
            Map<String, Object> map = (Map<String, Object>) object;
            latitude = (double) map.get("latitude");
            longitude = (double) map.get("longitude");
            city = (String) map.get("city");
            mStrFloor = (String) map.get("floor");
            mStrAddressName = (String) map.get("address");
            SharedPreferenceUtil.putString(this, "city", city);
            SharedPreferenceUtil.putString(this, "floor", mStrFloor);
            SharedPreferenceUtil.putString(this, "address", mStrAddressName);
            SharedPreferenceUtil.putString(this, "latitude", String.valueOf(latitude));
            SharedPreferenceUtil.putString(this, "longitude", String.valueOf(longitude));
        }
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        new HttpManager().post(this, Constants.GET_VERSION, VersionInfo.class,
                Params.getVersionParams(this), new OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.getResultCode() == 1) {
                            if (response instanceof VersionInfo) {
                                final VersionInfo versionInfo = (VersionInfo) response;
                                if (versionInfo.data == null) {
                                    return;
                                }
                                if (VersionUtils.hasNewVersion(MainActivity.this, versionInfo.data.version)) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            final MessageDialog messageDialog = new MessageDialog(MainActivity.this, "取消", "马上更新", versionInfo.data.info);
                                            messageDialog.setBtn1ClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    messageDialog.dismiss();
                                                }
                                            });
                                            messageDialog.setBtn2ClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    messageDialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, VersionUpdateService.class);
                                                    intent.putExtra("desc", getString(R.string.app_name));
                                                    intent.putExtra("fileName", "company_release_v" + versionInfo.data.version + ".apk");
                                                    intent.putExtra("url", versionInfo.data.downloadUrl);
                                                    startService(intent);
                                                }
                                            });
                                            messageDialog.show();
                                        }
                                    }, 1000);
                                }
                            }
                        }


                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 3);
    }
}
