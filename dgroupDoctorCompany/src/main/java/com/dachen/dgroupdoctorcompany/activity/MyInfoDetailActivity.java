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
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
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

    TextView tv_name;
    TextView tv_contactmethod;
    TextView tv_company;
    TextView tv_part;
    TextView tv_position;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    private String mStrOrgId="";

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_UPDATE_INFO:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    if(null == userInfo){
                        return;
                    }
                    if(null == userInfo.data){
                        return;
                    }
                    mStrOrgId = userInfo.data.id;
                    tv_name.setText(userInfo.data.name);
                    tv_contactmethod.setText(userInfo.data.telephone);
                    tv_company.setText(userInfo.data.companyName);
                    tv_part.setText(userInfo.data.department);
                    SharedPreferenceUtil.putString(MyInfoDetailActivity.this,"department",userInfo.data.department);
                    tv_position.setText(userInfo.data.position);
                    if(!TextUtils.isEmpty(userInfo.data.headPicFileName))
                    {
//                        ImageLoader.getInstance().displayImage(userInfo.data.headPicFileName, head_icon);
                        CustomImagerLoader.getInstance().loadImage(head_icon,userInfo.data.headPicFileName);
                        String loginUserId = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"id", "");
                        SharedPreferenceUtil.putString(MyInfoDetailActivity.this,loginUserId+"head_url", userInfo.data.headPicFileName);
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
                intent.setClass(MyInfoDetailActivity.this,OrgActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getInfo(){
        //获取个人信息
        showLoadingDialog();
        new HttpManager().get(this, Constants.GET_INFO, UserInfo.class,
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
				if(!TextUtils.isEmpty(avatarUrl))
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
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ResultTemplate<Object> res= JSON.parseObject(s, new TypeReference<ResultTemplate<Object>>() {
                });
                if(res.resultCode==1){
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
                }else if(res.resultCode==Result.CODE_TOKEN_ERROR||res.resultCode==Result.CODE_NO_TOKEN){
                    Intent intent  = new Intent(MyInfoDetailActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    mDialog.dismiss();
                    ToastUtil.showToast(MyInfoDetailActivity.this,R.string.upload_avatar_failed);
                }
            }
        };
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
                ToastUtil.showToast(MyInfoDetailActivity.this,R.string.upload_avatar_failed);
            }
        };
        String urls =AppConfig.getUrl(Constants.USER_UPDATE, 3);
        StringRequest request=new StringRequest(Request.Method.POST,urls,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String access_token = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"session", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("access_token", access_token);
                map.put("headPicFileName", url);
                return map;
            }
        };
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 1));
        request.setTag(this);
        queue.add(request);
    }






    /**
     * 执行上传用户头像任务
     *
     * @param
     */
    /*private void executeUploadAvatarTask(File file) {
        if (!file.exists()) {// 文件不存在
            return;
        }
        // 显示正在上传的ProgressDialog
        RequestParams params = new RequestParams();
        final String loginUserId = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"id", "");
        String access_token = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,"session", "");
        String oldAvatarName = SharedPreferenceUtil.getString(MyInfoDetailActivity.this,loginUserId+"head_url","");

        params.put("access_token", access_token);
        params.put("userId", loginUserId);
        if(!TextUtils.isEmpty(oldAvatarName))
            params.put("oldAvatarName", oldAvatarName);
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String fullurl = "";
        fullurl = AppConfig.getuploadUrl(Constants.UploadAvatarServlet, 1);
        LogUtils.burtLog("fullurl===" + fullurl);
        client.post(AppConfig.getuploadUrl(Constants.UploadAvatarServlet, 1), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                UploadAvatar2Bean bean = GJson.parseObject(new String(arg2), UploadAvatar2Bean.class);
                if (bean != null) {
                    if (bean.resultCode == 1) {
                        // web会返回一个头像地址，保存到sp文件。
                        UploadAvatar2Bean.Data data = bean.data;
                        if (data != null) {
                            //UserSp.getInstance(context).setValue(UserSp.key_user_avatar, data.tUrl);
                            File file = new File(data.tUrl);
                            ImageLoader.getInstance().displayImage(mNewPhotoUri.toString(), head_icon);
                            ToastUtils.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_success);

                            if (mNewPhotoUri != null && !mNewPhotoUri.toString().isEmpty()) {

                                //	MeFragment.instance.updateAvatar(mNewPhotoUri.toString());
                                SharedPreferenceUtil.putString(MyInfoDetailActivity.this, loginUserId + "head_url", mNewPhotoUri.toString());
                                ImSdk.getInstance().changeAvatar(data.tUrl);
                            }
                        }
                    } else {
                        ToastUtils.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_failed);
                    }
                } else {
                    ToastUtils.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_failed);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                //				ProgressDialogUtil.dismiss(mProgressDialog);
                ToastUtils.showToast(MyInfoDetailActivity.this, R.string.upload_avatar_failed);
            }

        });
    }*/
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
                if(response instanceof UserInfo){
                    closeLoadingDialog();
                    String userID = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");
                    UserInfo userInfo = (UserInfo) response;
                    if (null!=userInfo&&null!=((UserInfo) response).data){
                        Message msg = Message.obtain();
                        msg.what = MSG_UPDATE_INFO;
                        msg.obj = userInfo;
                        CompanyContactListEntity entity = new CompanyContactListEntity();
                        entity.userId = userInfo.data.userId+"";
                        entity.id = userInfo.data.id;
                        entity.department = userInfo.data.department;
                        entity.headPicFileName = userInfo.data.headPicFileName;
                        entity.position = userInfo.data.position;
                        entity.status = userInfo.data.status;
                        entity.telephone = userInfo.data.telephone;
                        entity.userStatus = userInfo.data.status;
                        entity.userloginid = userID;
                        entity.name = userInfo.data.name;

                        if (null != userInfo.data.role && userInfo.data.role.size() > 0) {
                            for (int j = 0; j < userInfo.data.role.size(); j++) {
                                Role role = new Role();
                                role.companycontact = entity;
                                roleDao.addRole(role);
                            }
                        }
                        companyContactDao.addCompanyContact(entity);
                        mMyHandler.sendMessage(msg);
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
