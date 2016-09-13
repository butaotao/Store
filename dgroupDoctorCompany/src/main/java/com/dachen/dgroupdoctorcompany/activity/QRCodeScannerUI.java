package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.QRLogin;
import com.dachen.medicine.common.utils.CameraUtil;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * 二维码扫描界面
 *
 * @author lmc
 */
public class QRCodeScannerUI extends Activity implements
        ZXingScannerView.ResultHandler, View.OnClickListener {

    private static final String TAG = QRCodeScannerUI.class.getSimpleName();

    private ZXingScannerView mScannerView = null;
    // protected int findUserType = UserType.NotKnow;
    Context context;
    private Button mUiQrcodeScannerBack;
    private TextView mUiQrcodeScannerTitle;
    private ImageButton mUiQrcodeScannerChooseFromPhoto;
    private String mLitterApp;

    private void assignViews() {
        mUiQrcodeScannerBack = (Button) findViewById(R.id.ui_qrcode_scanner_back);
        mUiQrcodeScannerTitle = (TextView) findViewById(R.id.ui_qrcode_scanner_title);
        mUiQrcodeScannerChooseFromPhoto = (ImageButton) findViewById(R.id.ui_qrcode_scanner_choose_from_photo);
        mScannerView = (ZXingScannerView) findViewById(R.id.ui_qrcode_scanner_ZXingScannerView);
        mUiQrcodeScannerTitle.setText("扫描二维码");
        mUiQrcodeScannerChooseFromPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //mScannerView = new ZXingScannerView(this);
        setContentView(R.layout.ui_qrcode_scanner);
        context = this;
        assignViews();
        init();
        initListener();
    }

    private void initListener() {
        mUiQrcodeScannerBack.setOnClickListener(this);
        mUiQrcodeScannerChooseFromPhoto.setOnClickListener(this);
    }

    private void init() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
        mLitterApp = getIntent().getStringExtra("litterApp");
    }

    void onClick_ui_qrcode_scanner_back() {
        finish();
    }

    void onClick_ui_qrcode_scanner_choose_from_photo() {
        // 从相册选择图片
        CameraUtil.pickImageSimple(this, REQUEST_CODE_PICK_CROP_PHOTO);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    /*
     * (non-Javadoc) 二维码扫描结果回调
     *
     * @see
     * me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler#handleResult
     * (com.google.zxing.Result)
     */
    @Override
    public void handleResult(Result rawResult) {
        if (rawResult != null) {
            handleResult(rawResult.getText());
        }
    }

    /**
     * 回调结果
     *
     * @param text
     */
    public void handleResult(String text) {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executeTask(text);
    }

    /**
     * 访问网络
     */
    protected void executeTask(final String scanResult) {
        if (scanResult.startsWith("http://" )&& "litterApp".equals(mLitterApp)) { //轻应用二维码
            Intent data = new Intent().putExtra("qrString", scanResult);
            setResult(RESULT_OK, data);
            finish();
        } else if(scanResult.startsWith("login://")){  //web端登入
            String[] strings = scanResult.split("\\?");
            for (int i = 0; i < strings.length; i++) {
            }
            final String[] strings1 = strings[0].split("//");
            new HttpManager().post(this, Constants.QR_WEB_LONIN_VERIFY, QRLogin.class, Params.getQRWebKeyParams
                    (strings1[1]), new HttpManager.OnHttpListener<com.dachen.medicine.entity.Result>() {
                @Override
                public void onSuccess(com.dachen.medicine.entity.Result response) {
                    if (response instanceof QRLogin){

                        QRLogin result = (QRLogin)response;
                        if (result.resultCode == 1050002) {
                                Intent intent = new Intent(QRCodeScannerUI.this, WebQRLoginActivity.class);
                                intent.putExtra("scanResult", strings1[1]);
                                startActivity(intent);
                            finish();
                        }else /*if(result.resultCode  == 1050001)*/{
                            ToastUtil.showToast(getApplicationContext(),"登录授权已经过期，请刷新二维码后重新扫描");
                            finish();
                        }
                    }
                }
                @Override
                public void onSuccess(ArrayList<com.dachen.medicine.entity.Result> response) {
                }

                @Override
                public void onFailure(Exception e, String errorMsg, int s) {

                }
            }, false, 1);
        }else {
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(scanResult);
            intent.setData(content_url);
            startActivity(intent);
            finish();
        }

		/*
         * Log.w(TAG, "executeTask():scanResult:"+scanResult);
		 *
		 * //String scanResultUrl = scanResult+"&access_token="+
		 * UserSp.getInstance
		 * (DApplication.getUniqueInstance()).getAccessToken(""); Log.w(TAG,
		 * "executeTask():scanResultUrl:"+scanResultUrl);
		 *
		 * VolleyUtil.getQueue(this).cancelAll(this); StringRequest req = new
		 * StringRequest(scanResultUrl, new Listener<String>() {
		 *
		 * @Override public void onResponse(String response) { Log.w(TAG,
		 * "result:"+response);
		 *
		 * try {
		 *
		 * QRCodeScanner2Bean bean = GJson.parseObject(response,
		 * QRCodeScanner2Bean.class); if (bean != null) { if
		 * (HttpErrorCode.successed == bean.resultCode) { // 以前的旧代码 JSONObject
		 * jsonObject = new JSONObject(response); if (jsonObject.has("data")) {
		 * JSONObject result = jsonObject.getJSONObject("data"); Gson gson = new
		 * Gson(); User user = gson.fromJson(result.toString(), User.class); if
		 * (user != null) { if (findUserType==0 && (user.userType ==
		 * UserType.Doctor || user.userType == UserType.Patient)) {//首页扫描
		 * if(user.userType == UserType.Doctor)
		 * DoctorInfoActivity.openUI(context,
		 * UserSp.getInstance(context).getUserId(""),
		 * user.getUserId(),"notfriend"); else {
		 * AddFriendActivity.openUI(context, user); } } else if (findUserType ==
		 * user.userType) { // 通过二维码扫描结果得到的URI访问网络，得到用户相关信息，跳转到相关界面。
		 * if(user.userType == UserType.Doctor)
		 * DoctorInfoActivity.openUI(context,
		 * UserSp.getInstance(context).getUserId(""),
		 * user.getUserId(),"notfriend"); else {
		 * AddFriendActivity.openUI(context, user); } } else { if (user.userType
		 * == UserType.Doctor) { Toast.makeText(context, "添加医生请到“圈子”或“首页”进行添加！",
		 * Toast.LENGTH_LONG).show(); } else if (user.userType ==
		 * UserType.Patient) { Toast.makeText(context, "添加患者请到“患者”页面添加！",
		 * Toast.LENGTH_LONG).show(); } } } // 关闭 finish(); } }else{
		 * //ToastUtil.showToast(context, bean.resultMsg); } } } catch
		 * (JSONException e) { e.printStackTrace(); }catch (Exception e) {
		 * e.printStackTrace(); } }
		 *
		 * }, new ErrorListener() {
		 *
		 * @Override public void onErrorResponse(VolleyError error) { //
		 * showErrorMessage("错误："+error.getMessage()); }
		 *
		 * });
		 *
		 * req.setTag(this); req.setRetryPolicy(new DefaultRetryPolicy(10000, 0,
		 * 0)); VolleyUtil.getQueue(this).add(req);
		 */

    }

    // +++ 下面是采用相册选择的

    private static final int REQUEST_CODE_CAPTURE_CROP_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_CROP_PHOTO = 2;
    private static final int REQUEST_CODE_CROP_PHOTO = 3;
    private Uri mNewPhotoUri;
    // 选择头像的数据
    private File mCurrentFile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.w(TAG, "onActivityResult():requestCode:" + requestCode
                + ",resultCode:" + requestCode);

        if (requestCode == REQUEST_CODE_PICK_CROP_PHOTO) { // 选择一张图片,然后立即调用裁减
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    String path = CameraUtil.getImagePathFromUri(context,
                            data.getData());
                    Log.e(TAG, "path:" + path);
                    Uri uri = Uri.fromFile(new File(path));
                    if (uri != null) {
                        Log.e(TAG, "uri toString:" + uri.toString()); // true
                        // 有file://
                        Log.e(TAG, "uri getPath:" + uri.getPath()); // 没有file://
                    }
                    // 解码配置 - 配置需要解码的图片格式为二维码，字符集编码为UTF-8
                    Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(
                            DecodeHintType.class);
                    Collection<BarcodeFormat> decodeFormats = EnumSet
                            .noneOf(BarcodeFormat.class);
                    decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
                    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
                    hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

                    // 加载图片
                    // path:/storage/emulated/0/Coolpad/Screenshots/Screenshot_2015-04-11-15-29-03.png
                    // mNewPhotoUri:file:///storage/sdcard1/Android/data/com.dachen
                    // .dgroupdoctor/files/Pictures/07ea34b2d173487ebfb06f5064f73ca7.jpg
                    // pp = "file:///storage/sdcard1/Android/data/qq236.jpg";
                    // path = "/storage/emulated/0/Android/data/qq236.jpg"; //
                    // 不带file://

                    File file = new File(path);
                    Log.e(TAG, "file exists:" + file.exists());

                    try {
                        BinaryBitmap b = loadImageFromFile(path, context); // string
                        // file，不是uri
                        Result lResult = new MultiFormatReader().decode(b,
                                hints);
                        String lText = lResult.getText();
                        Log.e(TAG, "二维码数据:" + lText);

                        // 回调结果
                        handleResult(lText);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w(TAG, "IOException 错误.");
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        if (file != null && file.exists()) {
                            Log.w(TAG, "可能不是二维码图片.");
                             ToastUtil.showToast(context, "没有找到相关二维码信息");
                        }
                    }

                    mNewPhotoUri = CameraUtil.getOutputMediaFileUri(context,
                            CameraUtil.MEDIA_TYPE_IMAGE);
                    // CameraUtil.cropImage(this, o, mNewPhotoUri,
                    // REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
                } else {
                    // ToastUtil.showToast(context,
                    // R.string.c_photo_album_failed);
                }
                Log.e(TAG, "mNewPhotoUri:" + mNewPhotoUri);
                Log.e(TAG, "mCurrentFile:" + mCurrentFile);
            }
        }
    }

    public static BinaryBitmap loadImageFromAssets(String fileName,
                                                   Context context) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getResources()
                .getAssets().open(fileName));

        int lWidth = bitmap.getWidth();
        int lHeight = bitmap.getHeight();
        int[] lPixels = new int[lWidth * lHeight];
        bitmap.getPixels(lPixels, 0, lWidth, 0, 0, lWidth, lHeight);
        return new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(
                lWidth, lHeight, lPixels)));
    }

    public static BinaryBitmap loadImageFromFile(String fileName,
                                                 Context context) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);

        int lWidth = bitmap.getWidth();
        int lHeight = bitmap.getHeight();
        int[] lPixels = new int[lWidth * lHeight];
        bitmap.getPixels(lPixels, 0, lWidth, 0, 0, lWidth, lHeight);
        return new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(
                lWidth, lHeight, lPixels)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_qrcode_scanner_back:
                onClick_ui_qrcode_scanner_back();
                break;
            case R.id.ui_qrcode_scanner_choose_from_photo:
                onClick_ui_qrcode_scanner_choose_from_photo();
                break;
            default:
                break;
        }
    }
}
