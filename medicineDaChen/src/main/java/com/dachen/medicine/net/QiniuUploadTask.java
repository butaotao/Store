package com.dachen.medicine.net;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Burt on 2016/4/5.
 */
public class QiniuUploadTask  {
    private String filePath;
    private String bucket;
    private String tokenUrl;
    private String appAccessToken;
    private UpListener mListener;
    private Context context;
    private boolean cancelled;
    private StringRequest tokenReq;
    private String key;

    private static UploadManager upManager;

    public QiniuUploadTask(String filePath, String bucket, UpListener listener,String tokenUrl,String appAccessToken,Context context) {
        this.filePath = filePath;
        this.bucket = bucket;
        this.tokenUrl=tokenUrl;
        this.appAccessToken=appAccessToken;
        this.context=context;
        key=makeKey();
        setListener(listener);
    }

    public void setListener(UpListener listener){
        if(listener==null){
            this.mListener=makeEmptyListener();
        }else{
            this.mListener=listener;
        }
    }

    public interface UpListener{
        void onFileUploadSuccess(String bucket, String key);
        void onFileUploadFailure(String msg);
    }

    private class MakeTokenRequest extends StringRequest {
        public MakeTokenRequest( Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, tokenUrl, listener, errorListener);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> m = new HashMap<String, String>();
            m.put("access-token", appAccessToken);
            return m;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bucket",bucket);
            return map;
        }

//        @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("access_token", appAccessToken);
//            map.put("bucket",bucket);
//            map.put("key",key);
//            return map;
//        }
    }

    /**
     * 生成token,发送逻辑在  {@code sendFileTo7Niu;}
     */
    public void execute(){
        Response.Listener<String> tokenSucListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ResultTemplate<TokenResult> res= JSON.parseObject(s, new TypeReference<ResultTemplate<TokenResult>>() {
                });
                if(res.resultCode!=1||res.data==null){
                    mListener.onFileUploadFailure("获取token失败");
                    return;
                }
                sendFileTo7Niu(key, res.data.upToken);
            }
        };
        Response.ErrorListener tokenErrListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mListener.onFileUploadFailure("获取token失败");
            }
        };
        tokenReq=new MakeTokenRequest(tokenSucListener,tokenErrListener);
        RequestQueue queue = VolleyUtil.getQueue(context);
        queue.add(tokenReq);
    }

    /**
     * 发送文件到7牛
     */
    private void sendFileTo7Niu(String fileKey,String token){
        UpCompletionHandler sucHandler=new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                if(responseInfo.isOK()){
//                    String url=QiNiuUtils.getFileUrl(bucket, key);
                    mListener.onFileUploadSuccess(bucket, key);
                }else{
                    mListener.onFileUploadFailure(responseInfo.error);
                }
            }

        };
        UploadOptions opts=new UploadOptions(null, null, false, null, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return cancelled;
            }
        });
        getUploadManager().put(filePath, fileKey, token, sucHandler, opts);

    }

    private static class TokenResult{
        public String upToken;
//        public String token;
    }
    private static UpListener makeEmptyListener(){
        return  new UpListener() {
            @Override
            public void onFileUploadSuccess(String bucket, String key) {
            }
            @Override
            public void onFileUploadFailure(String msg) {
            }
        };
    }
    private static synchronized UploadManager getUploadManager(){
        if(upManager==null){
            upManager=new UploadManager();
        }
        return upManager;
    }
    private static String makeKey(){
        String key= UUID.randomUUID().toString();
        return  key.replace("-","");
    }
    public void cancel(){
        if(tokenReq!=null){
            tokenReq.cancel();
        }
        cancelled=true;
    }
}
