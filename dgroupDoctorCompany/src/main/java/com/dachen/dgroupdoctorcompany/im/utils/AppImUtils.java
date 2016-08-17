package com.dachen.dgroupdoctorcompany.im.utils;

import com.dachen.common.DCommonSdk;
import com.dachen.common.toolbox.CommonUiTools;
import com.dachen.common.toolbox.OnCommonRequestListener;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.im.service.VChatFloatService;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.activities.ImBaseActivity.BaseActRunnable;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.EventPL;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.imsdk.net.ImPolling;
import com.dachen.imsdk.out.OnImSdkListener;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.activity.VChatActivity;

import java.util.List;

import de.greenrobot1.event.EventBus;

/**
 * Created by Mcp on 2016/2/25.
 */
public class AppImUtils {
    public static void initImAct(){
        ImBaseActivity.ON_RESUME_RUN=new BaseActRunnable() {
            @Override
            public void run(ImBaseActivity act) {
                ImPolling.getInstance().onResume();
                if (act instanceof VChatActivity) {
                    VChatFloatService.startWork(act, VChatFloatService.ACTION_CLOSE);
                    return;
                }
            }
        };
        ImBaseActivity.ON_PAUSE_RUN=new BaseActRunnable() {
            @Override
            public void run(ImBaseActivity act) {
                ImPolling.getInstance().onPause();
            }
        };

        ImBaseActivity.ON_DESTROY_RUN=new BaseActRunnable() {
            @Override
            public void run(ImBaseActivity act) {
                if (VChatManager.getInstance().isInChat&& VChatActivity.getInstance()==null) {
                    VChatFloatService.startWork(act, VChatFloatService.ACTION_SHOW);
                }
            }
        };

        ImSdk.getInstance().setImSdkListener(new OnImSdkListener() {
            @Override
            public void onGroupList(List<ChatGroupPo> list) {

                EventBus.getDefault().post(new NewMsgEvent());
            }

            @Override
            public void onEvent(EventPL ele) {
                ImEventUtils.handleEvent(ele);
            }
        });
        DCommonSdk.setCommonRequestListener(new OnCommonRequestListener() {
            @Override
            public boolean onTokenErr() {
                // TODO: 2016/5/26
                return false;
            }

            @Override
            public boolean onUpdateVersionErr(String msg) {
                if(MainActivity.getInstance()!=null){
                    CommonUiTools.getInstance().appVersionUpdate(MainActivity.getInstance(),msg);
                }
                return true;
            }
        });
    }
    public static String[] getBizTypes(){
        return  new String[]{"3_3","3_10","10","pub_customer"};
    }
}
