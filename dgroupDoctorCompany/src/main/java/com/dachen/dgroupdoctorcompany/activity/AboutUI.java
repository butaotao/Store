package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VersionUtils;
import com.dachen.common.widget.dialog.MessageDialog;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.VersionInfo;
import com.dachen.dgroupdoctorcompany.service.VersionUpdateService;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 关于界面
 *
 * @author WANG
 */
public class AboutUI extends BaseActivity {
    public static final String GOTO_WEBACTIVITY = "WebActivity";
    public static final String GOTO_WEBFROM = "WebActivityFrom";
    private static final String TAG = AboutUI.class.getSimpleName();
    public static final int FROM_GROUP_NOTICE = 1;//集团通知
    public static final int FROM_COMPANY_NOTICE = 2;//公司通知
    public static final int FROM_SERVICE_ARTICEL = 5; //服務和隐私条款

    @Nullable
    @Bind(R.id.version)
    TextView ui_about_version;

    @Nullable
    @Bind(R.id.textView1)
    TextView textView1;

    @Nullable
    @Bind(R.id.textView3)
    TextView textView3;

    @Bind(R.id.versionUpdate)
    TextView mVersionUpdate;

    protected String title = null; // 标题
    protected String text = null; // 填写内容
    protected int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_about);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化
     */
    protected void init() {

        Intent i = this.getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                //				this.title = b.getString(key_title);
                //				this.text = b.getString(key_text);
                //				this.findUserType = b.getInt(key_findUserType);
            }
        }

        //		Log.w(TAG, "title:"+title);
        //		Log.w(TAG, "text:"+text);
        //		Log.w(TAG, "findUserType:"+findUserType);

//		setTitle(this.title);

        //		if (findUserType == UserType.Patient) {
        //			ui_find_user_editText.setHint("请输入对方手机号");
        //		}else if (findUserType == UserType.Doctor) {
        //			ui_find_user_editText.setHint("请输入医生号或者手机号");
        //		}

        // 显示版本号
        showVersion();
        setTitle("关于");

    }

    /**
     * 显示版本号
     */
    protected void showVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            String version = info.versionName;
            ui_about_version.setText("V" + version);
        }
    }


    /**
     * 设置内容
     *
     * @param text
     */
    //	protected void setText(String text) {
    //		this.text = text;
    //		ui_my_introduce_editText.setText(text);
    //	}

    /**
     * 打开界面
     *
     * @param context
     * @param title
     * @param userType
     */
    public static void openUI(Context context, String title, int userType) {
        Intent i = new Intent(context, AboutUI.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //		i.putExtra(key_title, title);
        //		i.putExtra(key_text, text);
        //		i.putExtra(key_findUserType, findUserType);
        context.startActivity(i);
    }

    public static void openUI(Context context) {
        Intent i = new Intent(context, AboutUI.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Nullable
    @OnClick(R.id.textView1)
    void OnClickServiceArticle() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(GOTO_WEBFROM, FROM_SERVICE_ARTICEL);
        intent.putExtra(GOTO_WEBACTIVITY, "health/web/attachments/declaration/declaration.html");
        startActivity(intent);
    }


    @Nullable
    @OnClick(R.id.textView3)
    void OnClickSecrecyArticle() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(GOTO_WEBFROM, FROM_SERVICE_ARTICEL);
        intent.putExtra(GOTO_WEBACTIVITY, "health/web/attachments/declaration/declaration.html");
        startActivity(intent);
    }

    @OnClick(R.id.versionUpdate)
    void onVersionUpdateClick(View v) {
        mVersionUpdate.setTextColor(0x503db4ff);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVersionUpdate.setTextColor(0xff3db4ff);
            }
        }, 300);
        getVersion();

    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        new HttpManager().post(this, Constants.GET_VERSION, VersionInfo.class,
                Params.getVersionParams(this), new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.getResultCode() == 1) {
                            if (response instanceof VersionInfo) {
                                final VersionInfo versionInfo = (VersionInfo) response;
                                if (versionInfo.data != null && VersionUtils.hasNewVersion(AboutUI.this, versionInfo.data.version)) {
                                    final MessageDialog messageDialog = new MessageDialog(AboutUI.this, "取消", "马上更新", versionInfo.data.info);
                                    messageDialog.setBtn1ClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            messageDialog.dismiss();
                                        }
                                    });
                                    messageDialog.setBtn2ClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            messageDialog.dismiss();
                                            Intent intent = new Intent(AboutUI.this, VersionUpdateService.class);
                                            intent.putExtra("desc", getString(R.string.app_name));
                                            intent.putExtra("fileName", "medicine_release_v" + versionInfo.data.version + ".apk");
                                            intent.putExtra("url", versionInfo.data.downloadUrl);
                                            startService(intent);
                                        }
                                    });
                                    messageDialog.show();
                                    return;
                                }
                            }
                        }

                        ToastUtil.showToast(AboutUI.this, "当前版本即是最新版本");
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {
                        ToastUtil.showToast(AboutUI.this, "当前版本即是最新版本");
                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtil.showToast(AboutUI.this, "当前版本即是最新版本");

                    }
                },
                false, 3);
    }


}
