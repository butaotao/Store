package com.dachen.dgroupdoctorcompany.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dachen.common.utils.DeviceInfoUtil;
import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.dgroupdoctorcompany.service.HwPushService;
import com.dachen.imsdk.entity.HwPushBean;
import com.dachen.imsdk.entity.VChatInviteParam;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.utils.PushUtils;
import com.dachen.imsdk.vchat.activity.VChatInvitedActivity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.huawei.android.pushagent.api.PushEventReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mcp on 2016/7/11.
 */
public class HwPushReceiver extends PushEventReceiver {
    public static final String SP_KEY_TOKEN="huawei_token";
    public static final String TAG="HwPushReceiver";
    @Override
    public void onToken(Context context, String token, Bundle extras){
        String belongId = extras.getString("belongId");
//        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
//        showPushMessage(PustDemoActivity.RECEIVE_TOKEN_MSG, content);
        SharedPreferenceUtil.putString(context,SP_KEY_TOKEN, token);
        registerPush(CompanyApplication.getInstance());
    }

    public void registerPush(Context context){
        if (TextUtils.isEmpty(ImUtils.getLoginUserId())) return;
        PushUtils.registerDevice(Constants.USER_TYPEC, SharedPreferenceUtil.getString(context, SP_KEY_TOKEN, ""), null,true);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
//        if(!CommonUtils.getProcessName(context).equals(context.getPackageName())) //如果不是主进程  不现实。去除重复显示的问题
//            return false;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock =pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HwPushReceiver");
        wakeLock.acquire();
        try {
            String msgStr=new String(msg, "UTF-8");
            String content = "收到透传： " +msgStr;
            Logger.d(TAG, content);
            HwPushBean bean=JSON.parseObject(msgStr,HwPushBean.class);
            if(bean.notify){
                String groupId= (String) bean.param.get("groupId");
                if(groupId==null)return false;
                NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder b=new Notification.Builder(context).setContentTitle(bean.title).setContentText(bean.content).setSmallIcon(R.drawable.ic_launcher_icon);
                b.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
                b.setWhen(System.currentTimeMillis()).setAutoCancel(true);
                PendingIntent pIntent=PendingIntent.getService(context,0, new Intent(context, HwPushService.class).putExtra(HwPushService.EXTRA_MSG,msgStr),PendingIntent.FLAG_UPDATE_CURRENT);
                b.setContentIntent(pIntent);
                manager.notify(groupId.hashCode(),b.getNotification());
                return false;
            }
            JSONObject obj=bean.param;
            handleVideoIncomingMessage(context, obj);
//            showPushMessage(PustDemoActivity.RECEIVE_PUSH_MSG, content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            wakeLock.release();
        }
        return false;
    }

    /**
     * 处理视频来电推送消息
     * @param context
     * @param message
     */
    private void handleVideoIncomingMessage(Context context, JSONObject message) {
        if (DeviceInfoUtil.isRunningForeground(context) && DeviceInfoUtil.isInteractive(context))
            return;
        if (message == null) return;
        String inviteStr= (String) message.get("invite");
        if(inviteStr!=null){//多人视频通话
            VChatInviteParam param= JSON.parseObject(inviteStr,VChatInviteParam.class);
            Intent intent;
            if(MainActivity.getInstance()==null){
                intent = new Intent(context, MainActivity.class);
            }else{
                intent = new Intent(context, VChatInvitedActivity.class);
            }
            intent.putExtra(VChatInvitedActivity.INTENT_PARAM, param);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }
    }
    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            String msg= extras.getString(BOUND_KEY.pushMsgKey);
            String content = "收到知附加消息通： " + extras.getString(BOUND_KEY.pushMsgKey)+"notiId: "+notifyId;
            Logger.d(TAG, content);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            Map<String,Object> map=new HashMap<>();
            JSONArray jArr= JSON.parseArray(msg);
            for(int i=0;i<jArr.size();i++){
                JSONObject obj=jArr.getJSONObject(i);
                map.putAll(obj);
            }
            String groudId = (String) map.get("groupId");
            if (groudId != null) {
                String rtype = (String) map.get("rtype");
                Log.w(TAG, "onNotificationMessageClicked is OPEN UI.  groudId:" + groudId);
                ChatActivityUtilsV2.openUI(context, groudId, rtype);

            }
//            else if (msg.contains("type=1")) {
//                //MypopFeeActivity
//                Intent intent = new Intent(context,SplashActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if(TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
        }
        super.onEvent(context, event, extras);
    }
}
