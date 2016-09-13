package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.json.ResultTemplate;
import com.dachen.common.utils.QiNiuUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.entity.Role;
import com.dachen.dgroupdoctorcompany.entity.UserInfo;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.net.UploadEngine7Niu;
import com.dachen.imsdk.utils.FileUtil;
import com.dachen.medicine.common.utils.CameraUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/2/19.
 */
public class MyInfoDetailActivity extends BaseActivity implements HttpManager.OnHttpListener{
    @Nullable
    @Bind(R.id.head_icon)
    ImageView head_icon;
    private Uri mNewPhotoUri;
    private static final int REQUEST_CODE_CAPTURE_CROP_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_CROP_PHOTO = 2;
    private static final int REQUEST_CODE_CROP_PHOTO = 3;

    private static final int MSG_UPDATE_INFO = 102;
    private File mCurrentFile;
    private MyHandler mMyHandler ;
    com.dachen.dgroupdoctorcompany.views.DialogGetPhote dialog;
    CompanyContactListEntity entity = new CompanyContactListEntity();
    TextView tv_name;
    TextView tv_contactmethod;
    TextView tv_company;
    TextView tv_part;
    TextView tv_position;
    String url = "";
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    private String mStrOrgId="";
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_UPDATE_INFO:

                    mStrOrgId = entity.id;
                    tv_name.setText(entity.name);
                    tv_contactmethod.setText(entity.telephone);

                    tv_company.setText(SharedPreferenceUtil.getString
                            (CompanyApplication.getInstance(),"enterpriseName",""));
                    tv_part.setText(entity.department);
                    SharedPreferenceUtil.putString(MyInfoDetailActivity.this,"department",entity.department);
                    tv_position.setText(entity.position);
                    if(!TextUtils.isEmpty(entity.headPicFileName))
                    {
//                        ImageLoader.getInstance().displayImage(userInfo.data.headPicFileName, head_icon);
                        CustomImagerLoader.getInstance().loadImage(head_icon,entity.headPicFileName);
                        String loginUserId = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"id", "");
                        SharedPreferenceUtil.putString(MyInfoDetailActivity.this,loginUserId+"head_url", entity.headPicFileName);
                    }
                    else head_icon.setImageResource(R.drawable.head_icon);

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetail);
        setTitle("个人信息");
        ButterKnife.bind(this);
        getAvatarImage();
        //telephone  username telephone
        String telephones = SharedPreferenceUtil.getString(this,"telephone","");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_contactmethod = (TextView) findViewById(R.id.tv_contactmethod);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_part = (TextView) findViewById(R.id.tv_part);
        tv_position = (TextView) findViewById(R.id.tv_position);
        findViewById(R.id.rl_contactmethod).setOnClickListener(this);
        tv_contactmethod.setText(telephones+"");
        companyContactDao = new CompanyContactDao(this);
        roleDao = new RoleDao(this);
        mMyHandler = new MyHandler();
        dialog = new com.dachen.dgroupdoctorcompany.views.DialogGetPhote(this, new com.dachen.dgroupdoctorcompany.views.DialogGetPhote.OnClickListener() {

            @Override
            public void btnPhotoClick(View v) {
                // TODO Auto-generated method stub
                selectPhoto();
            }
            @Override
            public void btnCameraClick(View v) {
                // TODO Auto-generated method stub

                takePhoto();
            }
        });
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.rl_position).setOnClickListener(this);
        findViewById(R.id.rl_part).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = SharedPreferenceUtil.getString(this,"username","");
        tv_name.setText(username+"");
        getInfo();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ll_about:
                intent.setClass(MyInfoDetailActivity.this,UpdateUserInfoActivity.class);
                intent.putExtra("mode",UpdateUserInfoActivity.MODE_UPDATE_NAME);
                startActivity(intent);
                break;
            case R.id.rl_position:
                String strJobTitle = tv_position.getText().toString().trim();
                intent.setClass(MyInfoDetailActivity.this,UpdateUserInfoActivity.class);
                intent.putExtra("mode",UpdateUserInfoActivity.MODE_UPDATE_JOB_TITLE);
                intent.putExtra("job_title",strJobTitle);
                intent.putExtra("part",mStrOrgId);
                startActivity(intent);
                break;
            case R.id.rl_part:
                intent.setClass(MyInfoDetailActivity.this, OrgActivity.class);
                intent.putExtra("user",entity);
                startActivity(intent);
                break;
            case R.id.rl_contactmethod:
                intent = new Intent(this,EditTelActivity.class);
                startActivity(intent);
                break;
         //
        }
    }

    private void getInfo(){
        //获取个人信息
        showLoadingDialog();
        new HttpManager().get(this, Constants.GET_INFO, LoginRegisterResult.class,
                Params.getInfoParams(MyInfoDetailActivity.this),this,false,1);
    }

    /**
     * 获取头像
     */
    private void getAvatarImage() {
        // 获取头像

        //TODO
			/*if(user!=null)
			{
				String userId = user.getUserId();
				String headPicFileName = user.getHeadPicFileName();
				String avatarUrl = StringUtils.getAvatarUrl(userId, headPicFileName);
				if(!TextViewUtils.isEmpty(avatarUrl))
				{
					ImageLoader.getInstance().displayImage(avatarUrl, head_icon);
				}
				else head_icon.setImageResource(R.drawable.head_icon);
			}
			else {//logins.data.getUser().getUserId()+"head_url", url);
*/				String avatarUri = SharedPreferenceUtil.getString(this,SharedPreferenceUtil.getString(this,"id", "") + "head_url", "");

        if(!TextUtils.isEmpty(avatarUri))
        {
            CustomImagerLoader.getInstance().loadImage(head_icon,avatarUri);
        }
        else head_icon.setImageResource(R.drawable.head_icon);
        //}
    }

    @Nullable
    @OnClick(R.id.ll_icon)
    void changeIcon(){
       // ToastUtils.showToast("更换头像");
        dialog.show();
    }




    /**
     * 执行上传用户头像任务
     *
     * @param file
     */
    private void executeUploadAvatarTask(File file) {
        if (!file.exists()) {// 文件不存在
            return;
        }
        // 显示正在上传的ProgressDialog
        mDialog.show();
        File fileCompress;
        try {
            fileCompress = FileUtil.compressImageToFile(file.getPath(), 50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtil.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_failed);
            return;
        }
        UploadEngine7Niu.UploadObserver7Niu listener=new UploadEngine7Niu.UploadObserver7Niu() {
            @Override
            public void onUploadSuccess(String url) {
                setHeadPicToServer(url);
            }
            @Override
            public void onUploadFailure(String string) {
                mDialog.dismiss();
                ToastUtil.showToast(MyInfoDetailActivity.this,R.string.upload_avatar_failed);
            }
        };
        UploadEngine7Niu.uploadFileCommon(fileCompress.getPath(), listener, QiNiuUtils.BUCKET_AVATAR);
    }
    private void setHeadPicToServer(final String url){

        this.url = url;

        new HttpManager().post(this, Constants.UPDATE_USER_NAME, Result.class, Params
                .updateUserIcon(this,url), this, false, 1);
    }


    private void selectPhoto() {
//        CameraUtil.pickImageSimple(this, REQUEST_CODE_PICK_CROP_PHOTO);
        CustomGalleryActivity.openUi(this, false, REQUEST_CODE_PICK_CROP_PHOTO);
    }

    private void takePhoto() {
        mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
        CameraUtil.captureImage(this, mNewPhotoUri, REQUEST_CODE_CAPTURE_CROP_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE_CAPTURE_CROP_PHOTO) {// 拍照返回再去裁减
            if (resultCode == Activity.RESULT_OK) {
                if (mNewPhotoUri != null) {
                    Uri o = mNewPhotoUri;
                    mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
                    CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
                } else {
                    ToastUtils.showToast( MyInfoDetailActivity.this,R.string.c_photo_album_failed);
                }
            }
        } else if (requestCode == REQUEST_CODE_PICK_CROP_PHOTO) {// 选择一张图片,然后立即调用裁减
            if (resultCode == Activity.RESULT_OK) {
//                if (data != null && data.getData() != null) {
//                    String path = CameraUtil.getImagePathFromUri(this, data.getData());
//                    Uri o = Uri.fromFile(new File(path));
//                    mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
//                    CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
//                } else {
//                    ToastUtils.showToast( MyInfoDetailActivity.this,R.string.c_photo_album_failed);
//                }
                String[] all_path = data.getStringArrayExtra(GalleryAction.INTENT_ALL_PATH);
                if (all_path == null||all_path.length==0){
                    ToastUtils.showToast( MyInfoDetailActivity.this,R.string.c_photo_album_failed);
                    return;
                }
                String path = all_path[0];
                Uri o = Uri.fromFile(new File(path));
                mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
                CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
            }
        } else if (requestCode == REQUEST_CODE_CROP_PHOTO) { // 裁减图片
            if (resultCode == Activity.RESULT_OK) {
                if (mNewPhotoUri != null) {
                    mCurrentFile = new File(mNewPhotoUri.getPath());
                    // 上传图片成功后，才设置头像
                    // .....

                    // 设置头像

                    /**
                     * 执行上传用户头像任务
                     *
                     * @param file
                     */
                    executeUploadAvatarTask(mCurrentFile);

//                    ImageLoader.getInstance().displayImage(mNewPhotoUri.toString(), head_icon);
                    CustomImagerLoader.getInstance().loadImage(head_icon,mNewPhotoUri.toString());
                } else {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.c_crop_failed);
                }
            }
        }

    }

    @Override
    public void onSuccess(Result response) {
        if(null != response){
            if(response.getResultCode() == 1){
                if(response instanceof LoginRegisterResult){
                    closeLoadingDialog();
                    String userID = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");
                    LoginRegisterResult userInfo = (LoginRegisterResult) response;
                    if (null!=userInfo&&null!=((LoginRegisterResult) response).data){
                        Message msg = Message.obtain();
                        msg.what = MSG_UPDATE_INFO;
                        msg.obj = userInfo;
                        if (userInfo.data==null||null==userInfo.data.majorUser){
                            return;
                        }
                        LoginRegisterResult logins = (LoginRegisterResult) response;
                        UserLoginc.setUserInfo(logins, MyInfoDetailActivity.this);
                        entity = companyContactDao.queryByUserid(userInfo.data.userId+"");
                        if (null ==entity){
                            entity = new CompanyContactListEntity();
                        }
                        entity.userId = userInfo.data.userId+"";
                        entity.id = userInfo.data.majorUser.id;
                        entity.department = userInfo.data.majorUser.orgName;

                        entity.headPicFileName = userInfo.data.majorUser.headPic;
                        entity.position = userInfo.data.majorUser.title;
                        entity.telephone = userInfo.data.majorUser.telephone;
                        entity.userloginid = userID;
                        entity.name = userInfo.data.majorUser.name;

                        mMyHandler.sendMessage(msg);
                    }
                }else {
                    if(response.resultCode==1){
                        mDialog.dismiss();
                        final String loginUserId = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"id", "");
                        ImSdk.getInstance().changeAvatar(url);
//                    ImageLoader.getInstance().displayImage(mNewPhotoUri.toString(), head_icon);
                        CustomImagerLoader.getInstance().loadImage(head_icon,mNewPhotoUri.toString());
                        ToastUtil.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_success);
                        if (mNewPhotoUri != null&& !mNewPhotoUri.toString().isEmpty()) {
                            SharedPreferenceUtil.putString(MyInfoDetailActivity.this,loginUserId+"head_url", url);
                        }

                        // 发出observer
                        // BaseActivity.mObserverUtil.sendObserver(MeFragment.class,observer_update_image,mNewPhotoUri);
                    }else if(response.resultCode==Result.CODE_TOKEN_ERROR||response.resultCode==Result.CODE_NO_TOKEN){
                        Intent intent  = new Intent(MyInfoDetailActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        mDialog.dismiss();
                        ToastUtil.showToast(MyInfoDetailActivity.this,R.string.upload_avatar_failed);
                    }
                }
            }else{
                closeLoadingDialog();
                String msg = response.getResultMsg();
                if(TextUtils.isEmpty(msg)){
                    msg = "请求异常";
                }
                ToastUtil.showToast(this,msg);
            }

        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
