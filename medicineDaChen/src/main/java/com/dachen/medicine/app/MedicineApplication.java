package com.dachen.medicine.app;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.SharedPreferenceConst;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.config.ContextConfig.EnvironmentType;
import com.dachen.medicine.config.FusionConfig;
import com.dachen.medicine.entity.User;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiaomi.mipush.sdk.MiPushClient;

public class MedicineApplication extends Application {
	public static SharedPreferences mSharedPreferences;
	public static MedicineApplication app;
	private ContextConfig mAppConfig;
	public static boolean isDebug;
	public static DisplayImageOptions mNormalImageOptions;
	public static String UserId;
	private MActivityManager mActivityManager;
	public static int tab;
	public String roomName;
	public String mAccessToken;
	public long mExpiresIn;
	public int mUserStatus;
	public boolean mUserStatusChecked = false;
	public User mLoginUser = new User();// 当前登陆的用户
	/* 文件缓存的目录 */
	public String mAppDir = Environment.getExternalStorageDirectory() + "/Android/data/com.dachen.dgroupassistant";
	public String mPicturesDir = mAppDir + "/pictures";
	public String mVoicesDir = mAppDir + "/voices";
	public String mVideosDir = mAppDir + "/videos";
	public String mFilesDir = mAppDir + "/files";
	public static boolean flag = false;
	
    public static final String APP_ID = "2882303761517428172";
    public static final String APP_KEY = "5271742866172";
    public static final String TAG = "your packagename";
	public static int widths;
	public static int heights;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		app = this;
		mkdir();
		if(shouldInit()){ 
			MiPushClient.registerPush(this, APP_ID, APP_KEY);
		}
		WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		widths = display.getWidth();
		heights = display.getHeight();
		mActivityManager = MActivityManager.getInstance();
		mAppConfig = ContextConfig.getInstance();
		// mAppConfig.setEnvironmentType(EnvironmentType.PUBLISH);
		//mAppConfig.setEnvironmentType(EnvironmentType.INNER);
		mSharedPreferences = getSharedPreferences(SharedPreferenceConst.LOCK,
				Context.MODE_PRIVATE);
		// ButterKnife.setDebug(BuildConfig.DEBUG);
		// Tools.uploadLog();
		initImageLoader();
		UserInfo.getInstance(this).setUserType(Constants.USERTYPE);
	}

	public static MedicineApplication getApplication() {
		return app;
	}
	  private boolean shouldInit() {
	        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
	        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
	        String mainProcessName = getPackageName();
	        int myPid =  android.os.Process.myPid();
	        for (RunningAppProcessInfo info : processInfos) {
	            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
	                return true;
	            }
	        }
	        return false;
	    }

	public static SharedPreferences getSharePreferences() {
		return mSharedPreferences;
	}

	public static void mkdir() {
		File imageFilePath = new File(ImageUtil.SDCARD_CACHE_IMG_PATH);
		if (!imageFilePath.exists()) {
			imageFilePath.mkdirs();
		}
		File downloadPath = new File(ImageUtil.SDCARD_CACHE_DOWNLOAD_PATH);
		if (!downloadPath.exists()) {
			downloadPath.mkdirs();
		}
	}

	public MActivityManager getActivityManager() {
		return this.mActivityManager;
	}
	
	private void initImageLoader() {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
		MemoryCache memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		mNormalImageOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.image_download_fail_icon)
				.showImageOnFail(R.drawable.image_download_fail_icon).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.defaultDisplayImageOptions(mNormalImageOptions)
				// .denyCacheImageMultipleSizesInMemory()
				// .discCache(new TotalSizeLimitedDiscCache(new
				// File(mPicturesDir), 50 * 1024 * 1024))
				.discCache(
						new UnlimitedDiscCache(new File(FusionConfig
								.getImageCachePath())))
				// 最多缓存50M的图片
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.memoryCache((MemoryCache) memoryCache)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(4)
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
