package com.dachen.medicine.net;

import com.alibaba.fastjson.JSON;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.QiNiuUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.ContextConfig;
import com.qiniu.android.storage.UploadManager;

/**
 * Created by Burt on 2016/4/5.
 */
public class UploadEngine7Niu {

    public static final String TAG = UploadEngine7Niu.class.getSimpleName();
    private static UploadManager upManager;

    private static synchronized UploadManager getUploadManager() {
        if (upManager == null) {
            upManager = new UploadManager();
        }
        return upManager;
    }


    public interface UploadObserver7Niu {
        void onUploadSuccess(String url);

        void onUploadFailure(String msg);
    }


    public static QiniuUploadTask uploadFileCommon(final String filePath, final UploadObserver7Niu observer,final String bucket){
        QiniuUploadTask.UpListener tListener = new QiniuUploadTask.UpListener() {
            @Override
            public void onFileUploadSuccess(String bucket, String key) {
                String uri = QiNiuUtils.getFileUrl(bucket, key);
                observer.onUploadSuccess(uri);
            }
            @Override
            public void onFileUploadFailure(String msg) {
                observer.onUploadFailure(msg);
            }
        };
        QiniuUploadTask task = new QiniuUploadTask(filePath, bucket,tListener,getUploadToken(), SharedPreferenceUtil.getString("session", ""), MedicineApplication.app);
        task.execute();
        return task;
    }
    public static String getUploadToken(){
        String nets = SharedPreferenceUtil.getString("netdes", ContextConfig.KANG_ZHE);
        return "http://"+nets + "/im/file/getUpToken.action";
    }

}
