package com.dachen.dgroupdoctorcompany.archive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.StringUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.archive.download.ArchiveLoader;
import com.dachen.imsdk.archive.download.ArchiveTaskInfo;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.volley.custom.ObjectResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot1.event.EventBus;

/**
 * Created by Mcp  2016/1/13.
 */
public abstract class ArchiveItemDetailUi extends BaseActivity implements View.OnClickListener, ActionSheet.ActionSheetListener {
    private static final String TAG = ArchiveItemDetailUi.class.getSimpleName();
    protected TextView tvTitle;
    private ImageButton ibCancelDl;
    private ProgressBar mProBar;
    private View vBtmAction;
    private View vBtmDownload;
    private Button btnAction;
    protected ArchiveItem mItem;
    private String mFrom;
    private FrameLayout mContainer;
    private Button mSave;
    private TextView mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_detail_ui);

        setTheme(R.style.ActionSheetStyleiOS7);
        EventBus.getDefault().register(this);
        mItem = (ArchiveItem) getIntent().getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
        mFrom = getIntent().getStringExtra("from");
        initMyView();
        refreshBtmView();
        loadContentView();

        if ("im".equals(mFrom)) {
//            isInMyFileList();
        }
    }

    private void loadContentView() {
        mContainer = (FrameLayout) findViewById(R.id.container);
        View content = LayoutInflater.from(this).inflate(getContentViewLayoutResId(), mContainer, false);
        if (content != null) {
            mContainer.addView(content);
            onContentViewLoaded(content);
        }
    }

    private void initMyView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mProBar = (ProgressBar) findViewById(R.id.progressBar);
        vBtmAction = findViewById(R.id.ll_action);
        vBtmDownload = findViewById(R.id.ll_downloading);
        btnAction = (Button) findViewById(R.id.btn_action);
        ibCancelDl = (ImageButton) findViewById(R.id.ibtn_cancel_download);
        mSave = (Button) findViewById(R.id.btn_save);
        mSave.setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        mShare = (TextView) findViewById(R.id.share);
        mShare.setOnClickListener(this);
//        if("MedieDocumentActicity".equals(mFrom)){
//            mShare.setVisibility(View.GONE);
//        }else{
//            mShare.setVisibility(View.VISIBLE);
//        }
        if ("add".equals(mFrom) ||"im".equals(mFrom)) {
            mShare.setText("发送");
        } else if("MedieDocumentActicity".equals(mFrom)){
            mShare.setVisibility(View.GONE);
        }else {
            mShare.setText("转发");
        }

        ibCancelDl.setOnClickListener(this);
        if(null != mItem){
            tvTitle.setText(mItem.name);
        }
    }

    protected abstract int getContentViewLayoutResId();

    protected void onContentViewLoaded(View v) {

    }

    public void onEventMainThread(ArchiveLoader.DownloadChangeEvent event) {
        if (!StringUtils.strEquals(event.url, mItem.url))
            return;
        refreshBtmView();

        ArchiveTaskInfo info = ArchiveLoader.getInstance().getInfo(mItem);
        if (info != null && info.state == ArchiveTaskInfo.STATE_DOWNLOAD_OK) {
            onDownloadFinished();
        }
    }

    protected void onDownloadFinished(){

    }

    private void refreshBtmView() {
        if(null == mItem){
            return;
        }
        ArchiveTaskInfo info = ArchiveLoader.getInstance().getInfo(mItem);
        if (info.state == ArchiveTaskInfo.STATE_IN_DOWNLOADIN) {
            vBtmDownload.setVisibility(View.VISIBLE);
            vBtmAction.setVisibility(View.GONE);
            mProBar.setMax(info.totalLength);
            mProBar.setProgress(info.downLength);
        } else {
            vBtmDownload.setVisibility(View.GONE);
            vBtmAction.setVisibility(View.VISIBLE);

            changeActionBtnStatus(info.state, mItem.getSizeStr());

        }
    }

    protected void changeActionBtnStatus(int fileState, String fileSize) {
        if (fileState == ArchiveTaskInfo.STATE_DOWNLOAD_OK) {
            btnAction.setText("用其他应用打开");
            btnAction.setOnClickListener(openClickListener);
        } else {
            btnAction.setText("下载(" + fileSize + ")");
            btnAction.setOnClickListener(downloadClickListener);
        }
    }

    private View.OnClickListener downloadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null == mItem){
                return;
            }
            ArchiveLoader.getInstance().startDownload(mItem);
            refreshBtmView();
        }
    };
    private View.OnClickListener openClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null == mItem){
                return;
            }

            File f = ArchiveLoader.getInstance().getDownloadFile(mItem);
            Intent i = new Intent(Intent.ACTION_VIEW);
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            String type = myMime.getMimeTypeFromExtension(mItem.suffix);
            if (type == null)
                type = "*/*";
            i.setDataAndType(Uri.fromFile(f), type);
            try {
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast(mThis, "未找到可以打开此文件的程序");
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_save:
                saveFile();
                break;
            case R.id.ibtn_cancel_download:
                if(null == mItem){
                    return;
                }
                ArchiveLoader.getInstance().cancelDownload(mItem);
                break;
            case R.id.share:
                if ( "MeFragment".equals(mFrom)) {
                    ActionSheet.createBuilder(this, getSupportFragmentManager()).setCancelButtonTitle("取消")
                            .setOtherButtonTitles("转发给医生", "转发给患者").setCancelableOnTouchOutside(true).setListener(this).show();
                } else {
                    shareItem();
                }
                break;
        }
    }

    private void shareItem() {
        if(null == mItem){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, mItem);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        // TODO: 2016/3/4
//        Intent intent = null;
//        if (index == 0) {
//            intent = new Intent(this, EducationChatHistoryActivity.class);
//        } else if (index == 1) {
//            intent = new Intent(this, SelectPatientActivity.class);
//        }
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("share_files", mItem);
//        intent.putExtra("fileShareParams", params);
//        startActivity(intent);
    }

    /**
     * 保存文件
     */
    private void saveFile() {
        mDialog.show();
        final String reqTag = "saveFile";
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        queue.cancelAll(reqTag);
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.getUrl(Constants.SAVE_FILE,3), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ObjectResult<Void> result = JSON.parseObject(response, new TypeReference<ObjectResult<Void>>() {
                });
                if (result != null) {
                    if (result.getResultCode() == Result.CODE_SUCCESS) {
                        ToastUtil.showToast(ArchiveItemDetailUi.this, "保存成功");
                        mSave.setTextColor(getResources().getColor(R.color.gray));
                        mSave.setEnabled(false);
                    } else {
                        ToastUtil.showToast(ArchiveItemDetailUi.this, result.getResultMsg());
                    }
                    return;
                } else {
                    ToastUtil.showToast(ArchiveItemDetailUi.this, "保存失败");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(null == mItem){
                    return new HashMap<String, String>();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", ImSdk.getInstance().accessToken);
                params.put("fileId", mItem.fileId);
                params.put("receiveUserId", mItem.receiveUserId);
                params.put("sendUserId", mItem.sendUserId);
                Logger.d(TAG, params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    /**
     * 判断该文件是否在我的文件列表
     */
    private void isInMyFileList() {
        final String reqTag = "isInMyFileList";
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        queue.cancelAll(reqTag);
        StringRequest request = new StringRequest(Request.Method.POST,AppConfig.getUrl(Constants.IS_IN_MY_FILE_LIST,3), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(TAG, "response=" + response);
                ObjectResult<Boolean> result = JSON.parseObject(response, new TypeReference<ObjectResult<Boolean>>() {
                });

                if (result != null && result.getResultCode() == Result.CODE_SUCCESS && result.getData() == false) {//文件不存在
                    mSave.setTextColor(0xff5a88e0);
                    mSave.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(null == mItem){
                    return new HashMap<String, String>();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token",ImSdk.getInstance().accessToken);
                params.put("fileId", mItem.fileId);
                params.put("receiveUserId", mItem.receiveUserId);
                params.put("sendUserId", mItem.sendUserId);
                Logger.d(TAG, params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }


}
