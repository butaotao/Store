package com.dachen.dgroupdoctorcompany.js;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baoyz.actionsheet.ActionSheet;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.QiNiuUtils;
import com.dachen.dgroupdoctorcompany.activity.QRCodeScannerUI;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;
import com.dachen.imsdk.net.UploadEngine7Niu;
import com.dachen.imsdk.utils.CameraUtil;
import com.dachen.imsdk.utils.FileUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot1.event.EventBus;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/26下午2:04.
 * @描述 TODO
 */
public class BridgePlugin extends CordovaPlugin {
    private static final String TAG = "BridgePlugin";
    private static final int REQUEST_CODE_CAPTURE_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_PHOTO = 2;
    private static final int REQUEST_CODE_QR = 3;
    private static final String BASE64_HEAD = "data:image/jpeg;base64,";
    private CallbackContext mCallbackgetIdentity;
    private CallbackContext mCallbackgetFile;
    private CallbackContext mCallbackgetgetPhotos;
    private CallbackContext mCallbackgetcallCamera;
    private CallbackContext mCallbackcallQRScan;
    private CallbackContext mCallbacksetMenuButton;
    private CallbackContext mCallbacksetTitle;
    private CordovaInterface mCordova;
    private Uri mNewPhotoUri;
    private int mUploadingFileCount;
    private Object mIdentity;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mCordova = cordova;
    }

    @Override
    public boolean execute(String action, JSONArray params, CallbackContext callbackContext) throws JSONException {

        if ("get_identity".equals(action)) {
            Log.d("zxy", "execute: getIdentity");
            mCallbackgetIdentity = callbackContext;
            getIdentity();
            return true;
        } else if (action.equals("setTitle")) {
            Log.d("zxy :", "BridgePlugin : "+"execute: ");
            mCallbacksetTitle = callbackContext;
            setTitle(params);
            return true;
        } else if (action.equals("setMenuButton")) {
            Log.d("zxy", "execute: setMenuButton");
            mCallbacksetMenuButton = callbackContext;
            setMenuButton(params);
            return true;
        } else if (action.equals("callQRScan")) {
            Log.d("zxy", "execute: callQRScan");
            mCallbackcallQRScan = callbackContext;
            callQRScan();
            return true;
        }else if (action.equals("getPhotos")) {
            mCallbackgetgetPhotos = callbackContext;
            callPhoto();
            return true;
        } else if (action.equals("callCamera")) {
            mCallbackgetcallCamera = callbackContext;
            callCamera();
            return true;
        } else if (action.equals("see_bigPhoto")) {
            //seeBigPhoto();
            return true;
        } else if (action.equals("upload_photo")) {
            //UploadFile();
            return true;
        } else {
            return false;
        }

    }
    //得到个人信息
    public void getIdentity() {
        JSONObject json = new JSONObject();
        try {
            JSONObject Identity = getIdentityData(json);
            Log.d("zxy", "getIdentity: "+ JSON.toJSONString(Identity));
            mCallbackgetIdentity.success(Identity.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //设置菜单按钮
    private void setMenuButton(JSONArray params) {
        MenuButtonBean bean = null;
        try {
            String menu =  (String )params.get(0);
            Gson gson = new Gson();
            bean =  gson.fromJson(menu,MenuButtonBean.class);
            mCallbacksetMenuButton.success("setMenuButton");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(bean);
    }

    //调用二维码扫描
    private void callQRScan() {

        if (this.cordova != null) {
            Intent intent = new Intent(mCordova.getActivity(), QRCodeScannerUI.class);
            intent.putExtra("litterApp","litterApp");
            this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE_QR);
        }
    }

    //设置标题
    private void setTitle(JSONArray args) {
        try {
            TitleBean titleBean = new TitleBean();
            titleBean.title =  args.getString(0);
            EventBus.getDefault().post(titleBean);
            mCallbacksetTitle.success("setTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //调用相册
    private void callPhoto() {
        if (this.cordova != null) {
            Intent intent = new Intent(mCordova.getActivity(), CustomGalleryActivity.class);
            intent.putExtra(GalleryAction.INTENT_MULTI_PICK, true);
            intent.putExtra(GalleryAction.INTENT_MAX_NUM, 9);
            this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE_PICK_PHOTO);
        }
    }
    //调用相机
    private void callCamera() {
        if (this.cordova != null) {
            mNewPhotoUri = CameraUtil.getOutputMediaFileUri(mCordova.getActivity(), CameraUtil.MEDIA_TYPE_IMAGE);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mNewPhotoUri);
            this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE_CAPTURE_PHOTO);
        }
    }

    private void selectPicture() {
        ActionSheet.createBuilder(mCordova.getActivity(), ((FragmentActivity) mCordova.getActivity()).getSupportFragmentManager()).setCancelButtonTitle("取消")
                .setOtherButtonTitles("从相册选择", "拍照").setCancelableOnTouchOutside(true).setListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
            }
            @Override
            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                if(index == 0){
                    callPhoto();
                }else if(index == 1){
                    callCamera();
                }
            }
        }).show();
    }

    class UploadObserver implements UploadEngine7Niu.UploadObserver7Niu {
        private String localPath;

        public UploadObserver(String localPath) {
            this.localPath = localPath;
        }

        @Override
        public void onUploadSuccess(String url) {
            processResultFromUploadFile(localPath, url, true);
        }

        @Override
        public void onUploadFailure(String msg) {
            processResultFromUploadFile(localPath, msg, false);
        }
    }

    private void UploadFile(FileUploadParams params) {
        if (params == null || params.files == null) {
            return;
        }
        if (params.udomain == null) {
            params.udomain = QiNiuUtils.BUCKET_DOCTOR;
        }

        mUploadingFileCount += params.files.size();

        for (String file : params.files) {
            UploadEngine7Niu.uploadFileCommon(file, new UploadObserver(file), params.udomain);
        }
    }

    private void processResultFromUploadFile(String localPath, String url, boolean success) {
        mUploadingFileCount--;

        JObjectResult<FileEntity> jObjectResult = new JObjectResult<>();

        if(success){
            String key = url.substring(url.lastIndexOf("/") + 1, url.length());
            jObjectResult.setData(new FileEntity(localPath, url, key));
            jObjectResult.errorCode = 1;
        }else {
            jObjectResult.setData(new FileEntity(localPath, "", ""));
            jObjectResult.errorCode = 0;
            jObjectResult.errormsg = url;
        }

        Logger.d(TAG, "result=" + JSON.toJSONString(jObjectResult));

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, JSON.toJSONString(jObjectResult));
        if(mUploadingFileCount > 0){
            pluginResult.setKeepCallback(true);
        }else {//上传完成
            pluginResult.setKeepCallback(false);
        }
        mCallbackgetFile.sendPluginResult(pluginResult);
    }
    private void seeBigPhoto(ImageViewerParams params) {
        if (params == null) {
            return;
        }
        if (this.cordova != null) {
            Intent intent = new Intent(mCordova.getActivity(), MultiImageViewerActivity.class);
            intent.putExtra("position", params.page);
            intent.putStringArrayListExtra("imageUrls", (ArrayList<String>) params.files);
            this.cordova.startActivityForResult((CordovaPlugin) this, intent, -1);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (requestCode == REQUEST_CODE_PICK_PHOTO) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    processResultFromGallery(intent);
                }
            });
        } else if (requestCode == REQUEST_CODE_CAPTURE_PHOTO && resultCode == Activity.RESULT_OK) {// 拍照返回

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    processResultFromCamera(mNewPhotoUri.getPath());
                }
            });
        }else if (requestCode == REQUEST_CODE_QR && resultCode == Activity.RESULT_OK) {// 二维码返回
            String qrString = intent.getStringExtra("qrString");
            JSONObject qrJson = getQrJson(qrString);
            mCallbackcallQRScan.success(qrJson.toString());
            Log.d("zxy", "onActivityResult: json = "+qrJson.toString());
        }
    }

    private JSONObject getQrJson(String qrString) {
        JSONObject object = new JSONObject();
        try {
            object.put("resultCode","1");
            JSONObject object1= new JSONObject();
            object1.put("qrString",qrString);
            object.put("data",object1);
            object.put("resultMsg","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void processResultFromCamera(String filePath) {
        Log.d("zxy", "processResultFromCamera: ");
        if (filePath == null) {
            return;
        }
        List<ImageEntity> images = new ArrayList<>();
        try {
            File file = FileUtil.compressImageToFile2(filePath, 50);
            images.add(buildImageEntity(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JObjectResult<List<ImageEntity>> jObjectResult = new JObjectResult<>();
        jObjectResult.resultCode = 1;
        jObjectResult.resultMsg="";
        jObjectResult.setData(images);

        Logger.d(TAG, "result=" + JSON.toJSONString(jObjectResult));

        mCallbackgetcallCamera.success(JSON.toJSONString(jObjectResult));
    }

    private void processResultFromGallery(Intent intent) {
        String[] paths = intent.getStringArrayExtra(GalleryAction.INTENT_ALL_PATH);
        if (paths == null) {
            return;
        }
        boolean isOrigin = intent.getBooleanExtra(GalleryAction.INTENT_IS_ORIGIN, false);

        List<ImageEntity> images = new ArrayList<>();
        for (String path : paths) {
            File file = null;
            if (isOrigin) {
                file = new File(path);
            } else {
                try {
                    file = FileUtil.compressImageToFile2(path, 50);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            images.add(buildImageEntity(file));
        }
        JObjectResult<List<ImageEntity>> jObjectResult = new JObjectResult<>();
        jObjectResult.setData(images);
        jObjectResult.resultCode = 1;
        jObjectResult.resultMsg = "";
        Logger.d(TAG, "result=" + JSON.toJSONString(jObjectResult));
        mCallbackgetgetPhotos.success(JSON.toJSONString(jObjectResult));
    }
    private static ImageEntity buildImageEntity(File file) {
        byte[] byteCode = FileUtil.getFileByteCode(file);
        byte[] output = Base64.encode(byteCode, Base64.NO_WRAP);
        String bitmapBase64 = new String(output);
        return new ImageEntity(file.getAbsolutePath(), BASE64_HEAD + bitmapBase64);
    }


    private JSONObject getIdentityData(JSONObject json) throws JSONException {
        json.put("resultCode","1");
        JSONObject jsonData = new JSONObject();
        jsonData.put("openID",SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"openId",""));
        jsonData.put("userName", SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"username",""));
        jsonData.put("companyID", SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseId", ""));
        jsonData.put("companyName", SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", ""));
        String model = Build.MODEL;
        String system = ";Android "+android.os.Build.VERSION.RELEASE;
        jsonData.put("platform",model+system);
        json.put("data",jsonData);
        json.put("resultMsg","");
        return json;
    }

}
