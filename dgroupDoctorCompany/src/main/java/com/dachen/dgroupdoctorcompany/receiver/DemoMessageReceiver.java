package com.dachen.dgroupdoctorcompany.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.activity.SplashActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.utils.PushUtils;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

/**
 * Created by Burt on 2016/3/16.
 */
public class DemoMessageReceiver extends PushMessageReceiver {
    private static final String TAG = DemoMessageReceiver.class.getSimpleName();

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mStartTime;
    private String mEndTime;
    String des="";
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        }
        LogUtils.burtLog("mAlias==" + mAlias + "==" + mTopic);
    }
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        des = message.getDescription();

        LogUtils.burtLog("mMessage=="+mMessage+"=="+message);
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        }
        LogUtils.burtLog("mAlias=="+mAlias+"=="+mTopic);
        Map<String, String> extra = message.getExtra();
        Logger.d(TAG, "extra=" + extra);
        if (extra != null) {
            String groudId = extra.get("groupId");
            String rtype = extra.get("rtype");
            Log.w(TAG, "onNotificationMessageClicked is OPEN UI.  groudId:" + groudId);
            ChatActivityUtilsV2.openUI(context, groudId, rtype);

        }else
        if (mMessage.contains("type=1")) {
            //MypopFeeActivity
            Intent intent = new Intent(context,SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }


    }
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        }
    }
    public void regeisterXiaoMi(Context context){
      /*  LogUtils.burtLog("mRegId=="+ SharedPreferenceUtil.getString("mRegId", ""));
        HashMap<String, String> infaces = new HashMap<String, String>();
        infaces.put("interface1", Constants.XIAOMI);*/

//        if (BuildUtils.isHuaweiSystem() || TextViewUtils.isEmpty(ImUtils.getLoginUserId())) return;
        if (TextUtils.isEmpty(ImUtils.getLoginUserId())) return;
        PushUtils.registerDevice(Constants.USER_TYPEC, SharedPreferenceUtil.getString(context, "mRegId", ""), null);

//        new HttpManager().post(context, Constants.XIAOMI + "", ResultData.class,
//                Params.getReginsterXiaoMiReceiverRe(context, SharedPreferenceUtil.getString(context, "id", ""),
//                        SharedPreferenceUtil.getString(context, "mRegId", ""),
//                        SharedPreferenceUtil.getString(context, "session", ""), Constants.USER_TYPE), new HttpManager.OnHttpListener<Result>() {
//                    @Override
//                    public void onSuccess(Result response) {
//                        if (response instanceof ResultData) {
//                            ResultData resultData = (ResultData) response;
//                        }
//                    }
//
//                    @Override
//                    public void onSuccess(ArrayList<Result> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Exception e, String errorMsg, int s) {
//
//                    }
//                }, false,
//                3);
    }
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                SharedPreferenceUtil.putString(context,"mRegId", mRegId);
                regeisterXiaoMi(context);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
            String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
    }
}