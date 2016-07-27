package com.dachen.dgroupdoctorcompany.im.utils;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.activity.JointVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectVisitPeople;
import com.dachen.dgroupdoctorcompany.activity.TogetherVisitActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.entity.VisitPeople;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.imsdk.consts.EventType;
import com.dachen.imsdk.entity.EventPL;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.VChatCallerCancelParam;
import com.dachen.imsdk.entity.VChatInviteParam;
import com.dachen.imsdk.entity.VChatRejectParam;
import com.dachen.imsdk.net.ImPolling;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.activity.VChatActivity;
import com.dachen.imsdk.vchat.activity.VChatInvitedActivity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

/**
 * Created by Mcp on 2016/5/3.
 */
public class ImEventUtils {
    private static String TAG = ImEventUtils.class.getSimpleName();

    public static void handleEvent(EventPL event) {
        if (event.eventType.equals("1")){
            if (null!=event.param&&null!=event.param.get("from")&&null!=event.param.get("to")){
                String from  =event.param.get("from");
                String to = event.param.get("to");
                GetAllDoctor.getInstance().addDoctor();
                GetAllDoctor.getInstance().getPeople();
                if (!to.equals(SharedPreferenceUtil.getString(CompanyApplication.context,"id",""))){
                    GetAllDoctor.getInstance().addSingDoctor(to);
                }else if(!from.equals(SharedPreferenceUtil.getString(CompanyApplication.context,"id",""))){
                    GetAllDoctor.getInstance().addSingDoctor(from);
                }
            }

        }else if(event.eventType.equals("2")){
            if (null!=event.param&&null!=event.param.get("from")&&null!=event.param.get("to")){
                String from  =event.param.get("from");
                String to = event.param.get("to");
                DoctorDao doctorDao = new DoctorDao(CompanyApplication.context);
                doctorDao.deleteByid(from);
                doctorDao.deleteByid(to);
                CompanyContactDao companyContactDao = new CompanyContactDao(CompanyApplication.context);
                companyContactDao.deleteByid(from);
                companyContactDao.deleteByid(to);
            }
        } else if ((EventType.V_CHAT_INVITE + "").equals(event.eventType)) {//视频邀请
            handleVChatInviteEvent(event);
        } else if ((EventType.V_CHAT_ADD_USER + "").equals(event.eventType)) {//视频邀请
            handleVChatAddEvent(event);
        } else if ((EventType.V_CHAT_REJECT + "").equals(event.eventType) || (EventType.V_CHAT_BUSY + "").equals(event.eventType)) {//视频拒绝
            handleVChatReject(event);
        } else if ((EventType.V_CHAT_CALLER_CANCEL + "").equals(event.eventType)) {//视频拒绝
            handleVChatCancel(event);
        }else if((EventType.VISIT_FLUSH + "").equals(event.eventType)){//刷新协同拜访
            handleVisitFlush(event);
        }else if((EventType.VISIT_JOIN + "").equals(event.eventType)){//加入协同拜访
            handleVisitJoin(event);
        }else if((EventType.VISIT_CONFIRM + "").equals(event.eventType)){//确认协同拜访
            handleVisitConfirm(event);
        }else if((EventType.VISIT_DELETE + "").equals(event.eventType)){//取消协同拜访(创建者)
            handleVisitDelete(event);
        }else if((EventType.VISIT_CANCEL + "").equals(event.eventType)){//取消协同拜访(参与者)
            handleVisitCancel(event);
        }
    }

    public static void handleVisitCancel(EventPL event){
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
        if (!isValidEvent(event)) return;
        String id = event.param.get("id");
        String headPic = event.param.get("headPic");
        String name = event.param.get("name");
        VisitPeople visitPeople = new VisitPeople();
        visitPeople.name = name;
        visitPeople.headPic = headPic;
        visitPeople.id = id;
        if(null != SelectVisitPeople.getInstance()){
            SelectVisitPeople.getInstance().removeView(visitPeople);
        }
    }

    public static void handleVisitDelete(EventPL event){
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
        if (!isValidEvent(event)) return;
        if(null != SelectVisitPeople.getInstance()){
            SelectVisitPeople.getInstance().createDeleteVisit();
        }
    }

    public static void handleVisitJoin(EventPL event){
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
        if (!isValidEvent(event)) return;
        String id = event.param.get("id");
        String headPic = event.param.get("headPic");
        String name = event.param.get("name");
        VisitPeople visitPeople = new VisitPeople();
        visitPeople.name = name;
        visitPeople.headPic = headPic;
        visitPeople.id = id;
        if(null != SelectVisitPeople.getInstance()){
            SelectVisitPeople.getInstance().addVisitPeople(visitPeople);
        }
    }

    public static void handleVisitFlush(EventPL event){
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
        if (!isValidEvent(event)) return;
        if(null != TogetherVisitActivity.getInstance()){
            TogetherVisitActivity.getInstance().getVisitGroup();
        }
    }

    public static void handleVisitConfirm(EventPL event){
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
//        if (!isValidEvent(event)) return;
        Context context = CompanyApplication.getInstance();
        Intent intent = new Intent(context, JointVisitActivity.class);
        String id = event.param.get("id");
        intent.putExtra("id", id);
        if(null != SelectVisitPeople.getInstance()){
            intent.putExtra("mode",SelectVisitPeople.getInstance().from);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void handleVChatInviteEvent(EventPL event) {
        Logger.d(TAG, "isValidEvent=" + isValidEvent(event));
        if (!isValidEvent(event)) return;
        if (VChatInvitedActivity.getInstance() != null || VChatActivity.getInstance() != null) {//聊天中
            return;
        }
        if (event.param == null) return;
        VChatInviteParam param = JSON.parseObject(event.param.get("invite"), VChatInviteParam.class);
        if (param == null) return;
        Context context = CompanyApplication.getInstance();
        Intent intent = new Intent(context, VChatInvitedActivity.class);
        intent.putExtra(VChatInvitedActivity.INTENT_PARAM, param);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void handleVChatAddEvent(EventPL event) {
        if (event.param == null) return;
        VChatInviteParam param = JSON.parseObject(event.param.get("invite"), VChatInviteParam.class);
        if (param == null || param.roomId != VChatManager.getInstance().curRoomId) return;
        VChatManager.getInstance().addUserList(param.userList);
    }

    private static boolean isValidEvent(EventPL event) {
        //当前服务器时间
        long currentServerTime = System.currentTimeMillis() + ImPolling.getServerTimeDiff();
        return currentServerTime - event.ts < 30 * 1000;
    }

    public static void handleVChatReject(EventPL event) {
        if (event.param == null) return;
        VChatManager man = VChatManager.getInstance();
        VChatRejectParam param = JSON.parseObject(event.param.get("invite"), VChatRejectParam.class);
        /**
         * 存在的问题：A和B在视频通话中，这时A邀请C，C拒绝接听，结果A的界面会移除C，但B的界面不会移除C。
         * 所以去掉man.myInviteList.size()==0这个条件
         * 不知道会不会导致什么问题？？？
         */
        //if (param == null || param.roomId != man.curRoomId || man.myInviteList.size() == 0) return;

        if (param == null || param.roomId != man.curRoomId) return;

        man.removeUser(param.rejectId);
        UserInfo u = man.removeInvite(param.rejectId);
        VChatActivity act = VChatActivity.getInstance();
        if (u != null) {
            String msg = "";
            if (event.eventType.equals(EventType.V_CHAT_BUSY + "")) {
                msg = u.name + "忙线中";
            } else {
                if(param.reason == VChatRejectParam.REASON_NORMAL){
                    msg = u.name + "拒绝视频邀请";
                }else if(param.reason == VChatRejectParam.REASON_TIMEOUT){
                    msg = u.name + "没有接听";
                }

            }
            if (act != null) {
                act.showMyToast(msg);
            } else {
                ToastUtil.showToast(CompanyApplication.getInstance(), msg);
            }
        }
    }

    public static void handleVChatCancel(EventPL event) {
        if (event.param == null) return;
        VChatManager man = VChatManager.getInstance();
        VChatCallerCancelParam param = JSON.parseObject(event.param.get("invite"), VChatCallerCancelParam.class);
        if (VChatInvitedActivity.getInstance() != null && man.curRoomId == param.roomId) {
            ToastUtil.showToast(CompanyApplication.getInstance(), "视频邀请已取消");
            VChatInvitedActivity.getInstance().finish();
        }
    }

}
