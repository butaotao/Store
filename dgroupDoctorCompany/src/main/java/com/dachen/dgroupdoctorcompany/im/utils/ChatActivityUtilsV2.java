package com.dachen.dgroupdoctorcompany.im.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2DoctorChatActivity;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2RepresentChatActivity;
import com.dachen.dgroupdoctorcompany.im.activity.RepresentGroupChatActivity;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.db.po.ChatGroupPo.ChatGroupParam;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.utils.ImUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivityUtilsV2 {
    private static final String TAG = ChatActivityUtilsV2.class.getSimpleName();

    /**
     * 会话列表进聊天界面入口
     *
     * @param context
     * @param po
     */
    public static void openUI(Context context, ChatGroupPo po) {
        if (po == null) {
            return;
        }
        ChatGroupParam p = JSON.parseObject(po.param, ChatGroupParam.class);
        if (po.type == ChatGroupPo.TYPE_DOUBLE) {// 单聊

            if (po.bizType == null) {
                return;
            }

            // 医生-医生聊天
            if (po.bizType.equals("10")) {
                Represent2RepresentChatActivity.openUI(context, po.name, po.groupId, getSingleTargetId(po));
                return;
            }else if(po.bizType.equals("3_10")||po.bizType.equals("3_3")){
                Represent2DoctorChatActivity.openUI(context, po.name, po.groupId, getSingleTargetId(po));
                return;
            }
        } else if (po.type == ChatGroupPo.TYPE_MULTI) {// 群聊
            if (po.bizType == null) {
                return;
            }
            if (po.bizType.equals("10")) {// 医生和医生群聊
                ArrayList<UserInfo> userList = getUserList(po);
                RepresentGroupChatActivity.openUI(context, po.name, po.groupId, userList);
                return;
            }

        } else if (po.type == ChatGroupPo.TYPE_SERVICE) {
        }
    }
    /**
     * 推送消息进聊天界面入口
     *
     * @param context
     * @param groupId
     */
    public static void openUI(final Context context, final String groupId, String rtype) {

        if ("3_3_1".equals(rtype)) {//视频呼叫，暂不做处理
            return;
        }
        ChatGroupDao dao = new ChatGroupDao(context, ImUtils.getLoginUserId());
        ChatGroupPo po = dao.queryForId(groupId);
        if (po != null) {
            openUI(context, po);
        } else {// 刚下单时sessionMessageDB == null
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("rtype", rtype);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            return;
        }
    }


    public static String getSingleTargetId(ChatGroupPo po) {
        String targetId = "";
        if (TextUtils.isEmpty(po.groupUsers)) {
            return targetId;
        }
        List<UserInfo> list = JSON.parseArray(po.groupUsers, UserInfo.class);
        for (UserInfo chatGroupUser : list) {
            if (!chatGroupUser.id.equals(ImUtils.getLoginUserId())) {
                targetId = chatGroupUser.id;
                break;
            }
        }
        return targetId;
    }

    public static UserInfo getSingleTarget(ChatGroupPo po) {
        UserInfo info = null;
        if (TextUtils.isEmpty(po.groupUsers)) {
            return null;
        }
        List<UserInfo> list = JSON.parseArray(po.groupUsers, UserInfo.class);
        for (UserInfo chatGroupUser : list) {
            if (!chatGroupUser.id.equals(ImUtils.getLoginUserId())) {
                info = chatGroupUser;
                break;
            }
        }
        return info;
    }

    public static ArrayList<UserInfo> getUserList(ChatGroupPo po) {
        ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
        List<UserInfo> tList = JSON.parseArray(po.groupUsers, UserInfo.class);
        if (tList != null)
            userList.addAll(tList);
        return userList;
    }


}
