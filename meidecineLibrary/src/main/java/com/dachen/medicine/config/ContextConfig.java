package com.dachen.medicine.config;

import java.io.File;

import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.net.NetConfig;

/**
 * app相关配置、开发测试环境等
 * 
 * @author 蒋诗朋
 * 
 */
public class ContextConfig {

	private static ContextConfig instance;


	
	
	public static String API_INNER_URL = NetConfig.API_INNER_URL;
	
	/**
	 * apiUrl
	 */
	public static String API_BASE_URL = "http://" + API_INNER_URL + ":80";
	/**
	 * 屈军力
	 */
	
	public static String IP = NetConfig.API_INNER_URL;
	private EnvironmentType mEnvironmentType;
	private ContextConfig() {

	}

	public static ContextConfig getInstance() {
		if (instance == null) {
			instance = new ContextConfig();
		}
		return instance;
	}

	public void setEnvironmentType(EnvironmentType type) {
		this.mEnvironmentType = type;
		switch (mEnvironmentType) {
		case TEST:

			MedicineApplication.isDebug = true;
			API_BASE_URL = NetConfig.APP_TEST_API_URL;
			IP = NetConfig.KANG_ZHE;
			break;
		case PUBLISH:

			MedicineApplication.isDebug = true;
			API_BASE_URL = "http://" + NetConfig.API_OTER_URL + ":80";
			IP = NetConfig.API_OTER_URL;
			// Logcat.isLog = false;
			break;
		case INNER:

			API_BASE_URL = "http://" + API_INNER_URL + ":80";
			MedicineApplication.isDebug = true;
			IP = NetConfig.API_INNER_URL;
			break;
			
		case API_QUJUNLI_URL:
			API_BASE_URL = "http://" + NetConfig.API_QUJUNLI_URL + ":80";
			MedicineApplication.isDebug = true;
			IP = NetConfig.API_QUJUNLI_URL;
			break;
			case PUBLISHTEST:
				API_BASE_URL = "http://" + NetConfig.KANG_ZHE_TEST + ":80";
				MedicineApplication.isDebug = true;
				IP = NetConfig.KANG_ZHE_TEST;
				break;
		}
	}
	public final EnvironmentType getEnvironment() {
		return this.mEnvironmentType;
	}


	public enum EnvironmentType {
		PUBLISHTEST, INNER, TEST, PUBLISH,API_QUJUNLI_URL
	}

}
