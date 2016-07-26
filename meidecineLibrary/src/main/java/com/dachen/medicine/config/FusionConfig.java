package com.dachen.medicine.config;

import java.io.File;

import android.os.Environment;

import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.MemoryStatus;

/**
 * 
 * @描述： SD卡路径配置
 * @作者： @butaotao
 * @创建时间： @2015-6-23
 */
public class FusionConfig {
	// SD卡路径
	public static String SDCARDPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	// 上传的log文件名称
	public static String FileRecordName = "tounawang.txt";
	// 上传的log文件路径
	public static final String LOGPATH = SDCARDPATH + File.separator
			+ FileRecordName;

	/** 图片存放位置 **/
	public static final String SDCARD_CACHE_IMG_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/touna/images/";

	/** 下载的文件存放位置 **/
	public static final String SDCARD_CACHE_DOWNLOAD_PATH = SDCARDPATH
			+ "/touna/Download/";

	public static final String SDCARD_IMG_SETTING_USERPHOTO_PATH = SDCARD_CACHE_IMG_PATH;

	/**
	 * 图像缓存手机路径的目录，主要用于缓存
	 */
	public static final String CACHE_IMAGE_DATA_FLODER = Environment
			.getDataDirectory()
			+ "/data/"
			+ MedicineApplication.getPackageName()
			+ "/download/.imageCache/";
	/**
	 * 图像缓存SD卡路径的目录，主要用于缓存
	 */
	public static final String CACHE_IMAGE_SD_FLODER = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/medicinedachen/download/.imageCache/";

	/**
	 * 获取图片缓存文件目录
	 * 
	 * @return 文件路径
	 */
	public static String getImageCachePath() {
		if (MemoryStatus.externalMemoryAvailable()) {
			return FusionConfig.CACHE_IMAGE_SD_FLODER;
		} else {
			return FusionConfig.CACHE_IMAGE_DATA_FLODER;
		}
	}

}
