package com.dachen.dgroupdoctorcompany.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.dachen.common.DCommonSdk;
import com.dachen.common.media.SoundPlayer;
import com.dachen.common.utils.QiNiuUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.common.PinyinResource;
import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.im.utils.AppImUtils;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.out.ImNetworkReceiver;
import com.dachen.imsdk.utils.BuildUtils;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceConst;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.NetConfig;
import com.huawei.android.pushagent.api.PushManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Burt on 2016/2/18.
 */
public class CompanyApplication extends MultiDexApplication{
    public static SharedPreferences mSharedPreferences;
    private ContextConfig mAppConfig;
    public static Application context;
    public String mAppDir = Environment.getExternalStorageDirectory() + "/Android/data/com.dachen.dgroupdoctorcompany";
    public String mPicturesDir = mAppDir + "/pictures";
    public String mVoicesDir = mAppDir + "/voices";
    public String mVideosDir = mAppDir + "/videos";
    public String mFilesDir = mAppDir + "/files";
    public static  float mScreenWidth;
    public static  float mScreenHeight;
    public static final String APP_ID = "2882303761517443980";
    public static final String APP_KEY = "5101744335980";
    private ImNetworkReceiver mImNetworkReceiver;
    public static Map<String, String> PINYIN_TABLE;
    public static Map<String, String> MUTIL_PINYIN_TABLE;
    public static Map<String, String> CHINESE_MAP;
    public static int initContactList = 1;
    public static SoundPlayer player;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = this;
        mAppConfig = ContextConfig.getInstance();
        PINYIN_TABLE = PinyinResource.getPinyinResource(this);
        MUTIL_PINYIN_TABLE = PinyinResource.getMutilPinyinResource(this);
        CHINESE_MAP = PinyinResource.getChineseResource(this);
        // mAppConfig.setEnvironmentType(EnvironmentType.PUBLISH);
        com.dachen.medicine.config.UserInfo.getInstance(context).setUserType(Constants.USER_TYPE);
        mAppConfig.setEnvironmentType(ContextConfig.EnvironmentType.INNER);
        mSharedPreferences = getSharedPreferences(SharedPreferenceConst.LOCK,
                Context.MODE_PRIVATE);
        initData();
        if(shouldInit()){
            if(BuildUtils.isHuaweiSystem()){
                PushManager.requestToken(this);
//                PushManager.requestToken(this);
            }else{
                MiPushClient.registerPush(this, APP_ID, APP_KEY);
            }
        }
        // ButterKnife.setDebug(BuildConfig.DEBUG);
        // Tools.uploadLog();
        //login_user_info
         UserInfo.getInstance(this).setPackageName(this,getPackageName());
        com.dachen.medicine.config.UserInfo.getInstance(this).setVersion(HttpManager.getVersion(this));

        new MedicineApplication(this,null);
        initAppDir();
        initImageLoader();
        SQLiteHelper.getHelper(context).getWritableDatabase();

        DCommonSdk.mContext=this;
        ImVideo.getInstance().injectListener();
        ImSdk.getInstance().initSdk(this, mAppDir, mVoicesDir, mVideosDir, mPicturesDir);
        AppImUtils.initImAct();
        calcScreenSize();
        UserInfo userInfo = UserInfo.getInstance(context);
        ImSdk.getInstance().initUser(userInfo.getSesstion(),
                userInfo.getId(), userInfo.getUserName(), userInfo.getNickName(), userInfo.getHeadUrl());


       String num = com.dachen.medicine.config.UserInfo.getInstance(this).getIpNum();

        if (num.equals("1")){
//康哲环境
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_KANGZHE);
            ContextConfig.IP =NetConfig.KANG_ZHE;
        }else if(num.equals("2")){
            //nei
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_3_7);
            ContextConfig.IP = NetConfig.API_INNER_URL;
        } else if(num.equals("0")){
            //已切换到外网
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_ALI_YUN);
            ContextConfig.IP =NetConfig.API_OTER_URL;
        }else if(num.equals("3")){
            //已切换到外网
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_KANGZHE_TEST);
            ContextConfig.IP = NetConfig.KANG_ZHE_TEST;
        }
        ImSdk.getInstance().changeIp(ContextConfig.IP);
        new MedicineApplication(CompanyApplication.getInstance(),new Configs());
//        String sha1 = CommonUitls.sHA1(context);
//        System.out.print(sha1);
        mImNetworkReceiver=ImNetworkReceiver.registerReceiver(this);
    }
    public static int getInitContactList(){
        return initContactList;
    }
    public static void setInitContactList(int initContact){
        initContactList = initContact;
    }
    @Override
    public void onTerminate() {
        unregisterReceiver(mImNetworkReceiver);
        super.onTerminate();
    }
    private void initData() {
        player = new SoundPlayer(this);

    }

    public static SoundPlayer getMediaPlayer() {
        if (player != null) {
            return player;
        } else return new SoundPlayer(context);
    }
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid =  android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
    public static SharedPreferences getSharePreferences() {
        return mSharedPreferences;
    }
    public static  Application getInstance(){
        return  context;
    }

    private void initAppDir() {
        File file = getExternalFilesDir(null);
        if (file == null) {
//            Log.e(TAG, "init appdir file == null");
//            Log.e(TAG, "init appdir file mAppDir:" + mAppDir);
            // 没有则，主动创建。
            new File(mAppDir).mkdirs();
            new File(mPicturesDir).mkdirs();
            new File(mVoicesDir).mkdirs();
            new File(mVideosDir).mkdirs();
            new File(mFilesDir).mkdirs();
            return;
        } else {
//            Log.e(TAG, "init appdir file != null");
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        mAppDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!file.exists()) {
            file.mkdirs();
        }
        mPicturesDir = file.getAbsolutePath();
        file = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (!file.exists()) {
            file.mkdirs();
        }
        mVoicesDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (!file.exists()) {
            file.mkdirs();
        }
        mVideosDir = file.getAbsolutePath();

        file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists()) {
            file.mkdirs();
        }
        mFilesDir = file.getAbsolutePath();
    }
    private  void calcScreenSize() {
         mScreenWidth = getResources().getDisplayMetrics().widthPixels;
         mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    /******************* 初始化图片加载 **********************/
    // 显示的设置
    public static DisplayImageOptions mNormalImageOptions;
    public static DisplayImageOptions mAvatarRoundImageOptions;
    public static DisplayImageOptions mAvatarNormalImageOptions;
    public static DisplayImageOptions mAvatarCircleImageOptions;

    private void initImageLoader() {

        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        mNormalImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.image_download_fail_icon)
                .showImageOnFail(R.drawable.image_download_fail_icon)
                .build();

        mAvatarRoundImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(10))
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageOnLoading(R.drawable.ic_default_head)
                .build();

        mAvatarNormalImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageOnLoading(R.drawable.ic_default_head)
                .build();

        mAvatarCircleImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(360))
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageOnLoading(R.drawable.ic_default_head)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(mAvatarRoundImageOptions)
                        // .denyCacheImageMultipleSizesInMemory()
                        //		.discCache(new TotalSizeLimitedDiscCache(new File(mPicturesDir), 50 * 1024 * 1024))
                .discCache(new UnlimitedDiscCache(new File(mPicturesDir)))
                        // 最多缓存50M的图片
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        //		.memoryCache((MemoryCache) memoryCache)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(4)
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return   version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
