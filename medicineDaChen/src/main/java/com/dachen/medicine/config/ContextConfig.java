package com.dachen.medicine.config;

import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.NetConfig;

import java.io.File;

/**
 * app相关配置、开发测试环境等
 *
 * @author 蒋诗朋
 */
public class ContextConfig {

    private static ContextConfig instance;
    public static final  String KANG_ZHE_TEST = "120.25.84.65";

    public static final String APP_TEST_API_URL = "http://xg.mediportal.com.cn"+":"+ NetConfig.port;


    public static String API_INNER_URL = "192.168.3.7";

    public static final  String KANG_ZHE = "xg.mediportal.com.cn";

    /**
     * apiUrl
     */
    public static String API_BASE_URL = "http://" + API_INNER_URL + ":"+ NetConfig.port;

    /**
     * 外网
     */
    public static String API_OTER_URL = "120.24.94.126";
    //192.168.3.7   192.168.3.138:8080 192.168.3.7:9002 192.168.3.138:8080


    public static final String APP_PUBLISH_API_URL = "http://120.24.94.126"; // 服务器ip或域名



    private String mHeaderUrl;

    private ContextConfig() {

    }

    public static ContextConfig getInstance() {
        if (instance == null) {
            instance = new ContextConfig();
        }
        return instance;
    }


    public String getApiUrl(int net) {
        String nets = SharedPreferenceUtil.getString("netdes", KANG_ZHE);
        API_BASE_URL = "http://" + nets + ":"+ NetConfig.port + "/health";
        if (net == 2) {
            if (nets.contains(KANG_ZHE)) {
                mHeaderUrl = "http://" + nets + ":"+ NetConfig.port+"/web/api"; ;
            } else {
                mHeaderUrl = "http://" + nets + ":"+ NetConfig.port + "/web/api"; ;
            }

        } else if (net == 1) {
            mHeaderUrl = API_BASE_URL;
        } else if (net == 3) {
            mHeaderUrl = "http://" + nets +":"+ NetConfig.port +"/org";
        }
        return this.mHeaderUrl;
    }

    public String getApiUrl(int net, int port) {
        String nets = SharedPreferenceUtil.getString("netdes", KANG_ZHE);
        API_BASE_URL = "http://" + nets + ":" + port;
        if (net == 2) {
            if (nets.contains(KANG_ZHE)) {
                mHeaderUrl = "http://" + nets +":"+ NetConfig.port+ "/web/api"; ;
            } else {
                mHeaderUrl = "http://" + nets +":"+ NetConfig.port+ "/web/api"; ;
            }

        } else if (net == 1) {
            mHeaderUrl = API_BASE_URL;
        }
        return this.mHeaderUrl;
    }

    public String getUploadUrl(int net) {
        String nets = SharedPreferenceUtil.getString("netdes", KANG_ZHE);

        String url = "";
        url = "http://" + nets + ":9000/";
        return url;
    }

    public String getUploadUrl(int net, int port) {
        String nets = SharedPreferenceUtil.getString("netdes", KANG_ZHE);
        String url = "";

        url = "http://" + nets + ":" + port;
        return url;
    }



    public enum EnvironmentType {
        DEV, INNER, TEST, PUBLISH, API_QUJUNLI_URL
    }

}
