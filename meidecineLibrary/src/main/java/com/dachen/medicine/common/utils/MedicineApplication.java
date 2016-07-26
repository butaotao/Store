package com.dachen.medicine.common.utils;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Environment;

import com.android.library.R;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
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

public class MedicineApplication {
	public static SharedPreferences mSharedPreferences;
	public static boolean isDebug = true;
	static Context context ;
	static User mLoginUser = new User();
	// 显示的设置
		public static DisplayImageOptions mNormalImageOptions;
		public static DisplayImageOptions mAvatarRoundImageOptions;
		public static DisplayImageOptions mAvatarNormalImageOptions; 
	public static MActivityManager mActivityManager;
	public MedicineApplication(Context contexts,CallApplicationInterface interfaces ) {
		// TODO Auto-generated constructor stub
		if (null==mSharedPreferences){
			mSharedPreferences = contexts.getSharedPreferences(SharedPreferenceConst.LOCK,
					Context.MODE_PRIVATE);
		}
		if (null==mActivityManager){
			mActivityManager = MActivityManager.getInstance();
		}
		if (null==this.context){
			this.context = contexts;
		}
		if (null==this.interfaces){
			this.interfaces = interfaces;
		}
		UserInfo.getInstance(contexts).setVersion(HttpManager.getVersion(contexts));
		initImageLoader();
	}

	public static String mAppDir = Environment.getExternalStorageDirectory() + "/Android/data/"+"123";
	public static String mPicturesDir = mAppDir + "/pictures";
	public static String mVoicesDir = mAppDir + "/voices";
	public static String mVideosDir = mAppDir + "/videos";
	public static String mFilesDir = mAppDir + "/files";
	static HashMap<String, String> maps;
	public static CallApplicationInterface interfaces;
  
/*	public MedicineApplication(Context contexts,CallApplicationInterface interfaces) {
		// TODO Auto-generated constructor stub
		mSharedPreferences = contexts.getSharedPreferences(SharedPreferenceConst.LOCK,
				Context.MODE_PRIVATE);
		mActivityManager = MActivityManager.getInstance();
		this.context = contexts;
		this.maps = maps;
		this.interfaces = interfaces;
	} */
	public static Context getApplication(Context context){

			return context.getApplicationContext();

	}
	public static CallApplicationInterface getCallApplicationInterface(){
		if (null!=interfaces){
			return interfaces;
		}
		return null;
	}
	public static SharedPreferences getSharePreferences(Context context) {
		if (null==mSharedPreferences){
			mSharedPreferences = context.getSharedPreferences(SharedPreferenceConst.LOCK,
					Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
	}
	public static MActivityManager getActivityManager() {
		return mActivityManager;
	}
	public static String getUrl(){
		if (null!=interfaces&&null!=interfaces.getInterfaceMaps()) {
			  maps = interfaces.getInterfaceMaps();
				String url = maps.get("url");
				return url;
		}else {
			return  UserInfo.getInstance(context).getUrl();
		}
	  
	}
	public static String getIP(){
		if (null!=interfaces&&null!=interfaces.getInterfaceMaps()) {
			maps = interfaces.getInterfaceMaps();
			String url = maps.get("ip");
			return url;
		}else {
			return  UserInfo.getInstance(context).getIp();
		}

	}
	public static String getSession(){

			return  UserInfo.getInstance(context).getSesstion();
	}
	public static String getPackageName(){
		if (null!=interfaces&&null!=interfaces.getInterfaceMaps()) {
	    maps = interfaces.getInterfaceMaps();
		String url = maps.get("packagename");
		return url;
		}else{
			return UserInfo.getInstance(context).getPackageName();
		}
	}
	public static String getUserType(){
		return  UserInfo.getInstance(context).getUserType();
	}
	public static HashMap<String, String> getMapConfig(){
	/*	if (null!=interfaces) {
	    maps = interfaces.getInterfaceMaps();
		return maps;
		}else {*/
			return UserInfo.getInstance(context).getInterfaceMaps();
		//}
	}
	public static CallApplicationInterface getdb(){
		if (null!=interfaces) {
		return interfaces;
		}else {
			return null;
		}
	}
	public static Bitmap getBitmap(String url){
		if (null!=interfaces) {
			return interfaces.getBitmap(url);
			}else {
				return null;
			}
	}
	private void initImageLoader() {

		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
		MemoryCacheAware<String, Bitmap> memoryCache;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}
if (null == mNormalImageOptions){
	mNormalImageOptions = new DisplayImageOptions.Builder()
			.bitmapConfig(Config.RGB_565)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.resetViewBeforeLoading(true)
			.showImageForEmptyUri(R.drawable.no_images)
			.showImageOnFail(R.drawable.no_images)
			.build();
}
		if (null ==mAvatarRoundImageOptions){
			mAvatarRoundImageOptions = new DisplayImageOptions.Builder()
					.bitmapConfig(Config.RGB_565)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.resetViewBeforeLoading(true)
					.displayer(new RoundedBitmapDisplayer(10))
					.showImageForEmptyUri(R.drawable.no_images)
					.showImageOnFail(R.drawable.no_images)
					.showImageOnLoading(R.drawable.no_images)
					.build();
		}


if (null==mAvatarNormalImageOptions){
	mAvatarNormalImageOptions = new DisplayImageOptions.Builder()
			.bitmapConfig(Config.RGB_565)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.resetViewBeforeLoading(true)
			.showImageForEmptyUri(R.drawable.no_images)
			.showImageOnFail(R.drawable.no_images)
			.showImageOnLoading(R.drawable.no_images)
			.build();

	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
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

	}
}
