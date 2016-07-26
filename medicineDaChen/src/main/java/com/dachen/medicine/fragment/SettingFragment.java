package com.dachen.medicine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
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
import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.dachen.incomelibrary.utils.QiNiuUtils;
import com.dachen.medicine.R;
import com.dachen.medicine.activity.AboutActivity;
import com.dachen.medicine.activity.AccountSafeActivity;
import com.dachen.medicine.activity.LoginActivity;
import com.dachen.medicine.activity.PolicyGeneralizeActivity;
import com.dachen.medicine.activity.WebActivity;
import com.dachen.medicine.activity.WebActivitys;
import com.dachen.medicine.activity.income.IncomeActivity;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.common.utils.CameraUtil;
import com.dachen.medicine.common.utils.FileUtil;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.db.getInfo.UserInfoInDB;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.ResultData;
import com.dachen.medicine.entity.UploadAvatar2Bean;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.GJson;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.net.ResultTemplate;
import com.dachen.medicine.net.UploadEngine7Niu;
import com.dachen.medicine.net.VolleyUtil;
import com.dachen.medicine.view.CircleImageView;
import com.dachen.medicine.view.DialogGetPhote;
import com.dachen.medicine.view.UISwitchButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.apache.http.Header;

public class SettingFragment extends BaseFragment implements OnClickListener, ActionSheetListener, OnHttpListener {
    // 总View，总视图
    private View mRootView;
    RelativeLayout ll_logout;
    LinearLayout ll_about;
    LinearLayout ll_policygeneralize;
    TextView tv_username;
    TextView tv_telephone;
    RelativeLayout ll_modifypassword;
    String id;
    User user;
    public static final String GOTO_WEBACTIVITY = "WebActivity";
    public static final String GOTO_WEBFROM = "WebActivityFrom";
    @Bind(R.id.iv_select)
    UISwitchButton iv_select;
    public static int observer_update_image = 1;
    private static final int REQUEST_CODE_CAPTURE_CROP_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_CROP_PHOTO = 2;
    private static final int REQUEST_CODE_CROP_PHOTO = 3;
    public static final int FROM_GROUP_NOTICE=1;//集团通知
    public static final int FROM_COMPANY_NOTICE=2;//公司通知
    public static final int FROM_SERVICE_ARTICEL=5; //服務和隐私条款
    private Uri mNewPhotoUri;
    // 选择头像的数据
    private File mCurrentFile;
    CircleImageView head_icon;
    TextView tv_back;
    TextView tv_version;
    DialogGetPhote dialog;
    TextView tv_showfirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = LayoutInflater.from(mActivity).inflate(
                R.layout.fragmentsettings, null);
        ButterKnife.bind(mActivity);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // setUpViews();
        ll_logout = (RelativeLayout) mRootView.findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);
        ll_about = (LinearLayout) mRootView.findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
        //推广政策
        ll_policygeneralize = (LinearLayout) mRootView.findViewById(R.id.ll_policygeneralize);
        ll_policygeneralize.setOnClickListener(this);

        TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        tvTitle.setText("设置");
        tv_username = (TextView) mRootView.findViewById(R.id.tv_username);
        tv_telephone = (TextView) mRootView.findViewById(R.id.tv_telephone);
        ll_modifypassword = (RelativeLayout) mRootView
                .findViewById(R.id.ll_modifypassword);
        ll_modifypassword.setOnClickListener(this);
        tv_showfirst = (TextView) mRootView.findViewById(R.id.tv_showfirst);
        tv_back = (TextView) mRootView.findViewById(R.id.tv_back);
        tv_back.setVisibility(View.GONE);
        id = SharedPreferenceUtil.getString("id", null);
        user = UserInfoInDB.getUser(id);
        tv_version = (TextView) mRootView.findViewById(R.id.tv_version);
        tv_version.setText(SystemUtils.getSysteVersion(mActivity) + "");
        LogUtils.burtLog("user==" + user + "==" + id);
        if (null != user) {
            tv_username.setText("" + user.getName());
            tv_telephone.setText("" + user.getTelephone());
        }

        mRootView.findViewById(R.id.ll_popularize).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_back).setVisibility(View.GONE);
        iv_select = (UISwitchButton) mRootView.findViewById(R.id.iv_select);
        head_icon = (CircleImageView) mRootView.findViewById(R.id.head_icon);
        head_icon.setOnClickListener(this);
        getAvatarImage();
        iv_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    SharedPreferenceUtil.putString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
                    tv_showfirst.setText("药品优先显示商品名");
                } else {
                    SharedPreferenceUtil.putString("isopen_" + SharedPreferenceUtil.getString("id", ""), "0");
                    tv_showfirst.setText("药品优先显示通用名");
                }
                Intent intents = new Intent();
                intents.setAction(HomeFragment.Action);
                mActivity.sendBroadcast(intents);
            }
        });
        String isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
        if (isopen.equals("1")) {
            iv_select.setChecked(true);
            tv_showfirst.setText("药品优先显示商品名");
        } else {
            iv_select.setChecked(false);
            tv_showfirst.setText("药品优先显示通用名");
        }

        dialog = new DialogGetPhote(getActivity(), new DialogGetPhote.OnClickListener() {

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
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_logout:

                onLogoutBtnClicked();

                // 清除密码，将sp中保存的密码置为空
                SpUtil.setGesturePassword(mActivity,
                        "");

                break;
            case R.id.ll_about:
                intent = new Intent(mActivity, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_policygeneralize://推广政策
                intent = new Intent(mActivity, PolicyGeneralizeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_modifypassword:
                intent = new Intent(mActivity, AccountSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_popularize:
        /*	intent = new Intent(mActivity, MypopFeeActivity.class);
			startActivity(intent);*/
                intent = new Intent(mActivity, IncomeActivity.class);
//			intent = new Intent(mActivity, MyIncomeActivity.class);
                startActivity(intent);
                break;
           /* case R.id.iv_select:
		
			id = SharedPreferenceUtil.getString("id", null);
			User user = UserInfoInDB.getUser(id);
			String isopen = SharedPreferenceUtil.getString("isopen", "1");
			//LogUtils.burtLog("user.getBooleanOpen()==="+user.getBooleanOpen()+"===id==="+id);
			if(isopen.equals("1")){
				//iv_select.setBackgroundResource(R.drawable.disselected);
				if (null!=user) {
				user.setBooleanOpen("0");
				}
				SharedPreferenceUtil.putString("isopen", "1");
				tv_showfirst.setText("药品优先显示通用名");
			}else{
				//iv_select.setBackgroundResource(R.drawable.selected);
				user.setBooleanOpen("1");
				if (null!=user) {
				SharedPreferenceUtil.putString("isopen", "0");
				}
				tv_showfirst.setText("药品优先显示商品名");
				
			}
			//LogUtils.burtLog("=="+user);

			Intent intents = new Intent();
			             intents.setAction(HomeFragment.Action);
			             mActivity.sendBroadcast(intents);


			break;*/
            case R.id.head_icon:
                //onClick_fragmentMe_avatar();
                dialog.ShowDialog();
                break;
            default:
                break;
        }
    }
    @Nullable
    @OnClick(R.id.textView1)
    void OnClickServiceArticle() {
        Intent intent = new Intent(mActivity, WebActivitys.class);
        intent.putExtra(GOTO_WEBFROM, FROM_SERVICE_ARTICEL);
        intent.putExtra(GOTO_WEBACTIVITY, "health/web/attachments/declaration/declaration.html");
        startActivity(intent);
    }


    @Nullable
    @OnClick(R.id.textView3)
    void OnClickSecrecyArticle() {
        Intent intent = new Intent(mActivity, WebActivitys.class);
        intent.putExtra(GOTO_WEBFROM, FROM_SERVICE_ARTICEL);
        intent.putExtra(GOTO_WEBACTIVITY, "health/web/attachments/declaration/declaration.html");
        startActivity(intent);
    }
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        // TODO Auto-generated method stub
        if (index == 0) {
            takePhoto();
        } else {
            selectPhoto();
        }
    }

    private void takePhoto() {
        mNewPhotoUri = CameraUtil.getOutputMediaFileUri(mActivity, CameraUtil.MEDIA_TYPE_IMAGE);
        CameraUtil.captureImage(this, mNewPhotoUri, REQUEST_CODE_CAPTURE_CROP_PHOTO);
    }

    private void selectPhoto() {
        CameraUtil.pickImageSimple(this, REQUEST_CODE_PICK_CROP_PHOTO);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        // TODO Auto-generated method stub

    }

    void onClick_fragmentMe_avatar() {
        ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从手机相册选择")
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    protected FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
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
*/
        String avatarUri = SharedPreferenceUtil.getString(SharedPreferenceUtil.getString("id", "") + "head_url", "");
        LogUtils.burtLog("avatarUri===" + avatarUri);
        if (!TextUtils.isEmpty(avatarUri)) {
            CustomImagerLoader.getInstance().loadImage(head_icon,avatarUri);
        } else head_icon.setImageResource(R.drawable.head_icon);
        //}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE_CAPTURE_CROP_PHOTO) {// 拍照返回再去裁减
            if (resultCode == Activity.RESULT_OK) {
                if (mNewPhotoUri != null) {
                    Uri o = mNewPhotoUri;
                    mNewPhotoUri = CameraUtil.getOutputMediaFileUri(mActivity, CameraUtil.MEDIA_TYPE_IMAGE);
                    CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
                } else {
                    ToastUtils.showToast(R.string.c_photo_album_failed);
                }
            }
        } else if (requestCode == REQUEST_CODE_PICK_CROP_PHOTO) {// 选择一张图片,然后立即调用裁减
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    String path = CameraUtil.getImagePathFromUri(mActivity, data.getData());
                    Uri o = Uri.fromFile(new File(path));
                    mNewPhotoUri = CameraUtil.getOutputMediaFileUri(mActivity, CameraUtil.MEDIA_TYPE_IMAGE);
                    CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PHOTO, 1, 1, 300, 300);
                } else {
                    ToastUtils.showToast(R.string.c_photo_album_failed);
                }
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

                    CustomImagerLoader.getInstance().loadImage(head_icon,mNewPhotoUri.toString());
                } else {
                    ToastUtils.showToast(R.string.c_crop_failed);
                }
            }
        }

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
        File fileCompress;
        try {
            fileCompress = FileUtil.compressImageToFile(file.getPath(), 50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showToast(R.string.upload_avatar_failed);
            return;
        }
        UploadEngine7Niu.UploadObserver7Niu listener = new UploadEngine7Niu.UploadObserver7Niu() {
            @Override
            public void onUploadSuccess(String url) {
                setHeadPicToServer(url);
            }

            @Override
            public void onUploadFailure(String string) {
                ToastUtils.showToast(R.string.upload_avatar_failed);
            }
        };
        UploadEngine7Niu.uploadFileCommon(fileCompress.getPath(), listener, QiNiuUtils.BUCKET_AVATAR);
    }

    private void setHeadPicToServer(final String url) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ResultTemplate<Object> res = JSON.parseObject(s, new TypeReference<ResultTemplate<Object>>() {
                });
                if (res.resultCode == 1) {
                    final String loginUserId = SharedPreferenceUtil.getString("id", "");
                    String urls = url;
                    CustomImagerLoader.getInstance().loadImage(head_icon,mNewPhotoUri.toString());
                    ToastUtils.showToast(R.string.upload_avatar_success);
                    if (mNewPhotoUri != null && !mNewPhotoUri.toString().isEmpty()) {
                        SharedPreferenceUtil.putString(loginUserId + "head_url", urls);
                    }
                    // 发出observer
                    // BaseActivity.mObserverUtil.sendObserver(MeFragment.class,observer_update_image,mNewPhotoUri);
                } else if (res.resultCode == Result.CODE_TOKEN_ERROR) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(R.string.upload_avatar_failed);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(R.string.upload_avatar_failed);
            }
        };
        String urls = AppConfig.getUrl(Constants.USER_UPDATE, 1);
        StringRequest request = new StringRequest(Request.Method.POST, urls, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String access_token = SharedPreferenceUtil.getString("session", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("access_token", access_token);
                map.put("headPicFileName", url);
                return map;
            }
        };
        RequestQueue queue = VolleyUtil.getQueue(mActivity);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 1));
        request.setTag(this);
        queue.add(request);
    }

    /**
     * 执行上传用户头像任务
     *
     * @param
     */
	/*	private void executeUploadAvatarTask(File file) {
			if (!file.exists()) {// 文件不存在
				return;
			}
			// 显示正在上传的ProgressDialog
			RequestParams params = new RequestParams();
			final String loginUserId = SharedPreferenceUtil.getString("id", "");
			String access_token = SharedPreferenceUtil.getString("session", "");
			String oldAvatarName = SharedPreferenceUtil.getString(loginUserId+"head_url",""); 
		 
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
			LogUtils.burtLog("fullurl==="+fullurl); 
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
								File file = new File( data.tUrl );
								ImageLoader.getInstance().displayImage(mNewPhotoUri.toString(), head_icon);
								ToastUtils.showToast( R.string.upload_avatar_success);

								if (mNewPhotoUri != null && !mNewPhotoUri.toString().isEmpty()) {

									//	MeFragment.instance.updateAvatar(mNewPhotoUri.toString());
									SharedPreferenceUtil.putString(loginUserId+"head_url", mNewPhotoUri.toString());
								}
							}
						}
						else {
							ToastUtils.showToast( R.string.upload_avatar_failed);
						}
					} else {
						ToastUtils.showToast( R.string.upload_avatar_failed);
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					//				ProgressDialogUtil.dismiss(mProgressDialog);
					ToastUtils.showToast( R.string.upload_avatar_failed);
				}

			});
		} */
    void onLogoutBtnClicked() {  ///im/removeDeviceToken.action
        showLoadingDialog();
        //联网 从服务器登出
        new HttpManager().post(Constants.LOGOUT + "", LoginRegisterResult.class,
                Params.getLoginoutParams(SystemUtils.getDeviceId(mActivity)), new OnHttpListener<LoginRegisterResult>() {

                    @Override
                    public void onSuccess(Result response) {
                        closeLoadingDialog();
                        // TODO Auto-generated method stub
                        if (null == response) {
                            removeregeisterXiaoMi();
                            SharedPreferenceUtil.putString("session", "");
                            Intent intent = new Intent(mActivity, LoginActivity.class);
                            startActivity(intent);
                            mActivity.finish();

                            return;
                        } else {
                            if (response instanceof LoginRegisterResult) {
                                removeregeisterXiaoMi();
                                SharedPreferenceUtil.putString("session", "");
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                startActivity(intent);
                                mActivity.finish();

                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<LoginRegisterResult> response) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg,
                                          int s) {
                        closeLoadingDialog();
                        removeregeisterXiaoMi();
                        SharedPreferenceUtil.putString("session", "");
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        startActivity(intent);
                        mActivity.finish();
                    }
                },
                false, 1);

    }

    @Override
    public void onSuccess(Result response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(ArrayList response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        // TODO Auto-generated method stub

    }

    public void removeregeisterXiaoMi() {
        LogUtils.burtLog("mRegId==" + SharedPreferenceUtil.getString("mRegId", ""));
        HashMap<String, String> infaces = new HashMap<String, String>();
        infaces.put("interface1", Constants.XIAOMIREMOVE);
		/*	new HttpManager().post(infaces, ResultData.class,
					Params.getRemoveReginsterXiaoMiReceiver(SharedPreferenceUtil.getString("id", ""),
							SharedPreferenceUtil.getString("mRegId", ""),
							SharedPreferenceUtil.getString("session", "")), this,
					 1,80);*/
    }
}
