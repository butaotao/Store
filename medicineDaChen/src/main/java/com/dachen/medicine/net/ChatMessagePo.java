package com.dachen.medicine.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

/**
 * Created by Burt on 2016/4/5.
 */
public class ChatMessagePo {

    private static final long serialVersionUID = 1076369828533243186L;

    public static final int REQ_STATES_SENDING=0;
    public static final int REQ_STATES_SEND_OK=1;
    public static final int REQ_STATES_SEND_FAIL=2;
    public static final int REQ_STATES_UP_FILE=3;
    public static final int REQ_STATES_UP_FILE_OK=4;
    public static final int REQ_STATES_UP_FILE_FAIL=5;

    public int id;
    public static final String _id="id";
     
    public String msgId;
    public static final String _msgId="msgId";
     
    public String clientMsgId;
    public static final String _clientMsgId="clientMsgId";
     
    public String fromUserId;
     
    public long sendTime;
    public static final String _sendTime="sendTime";
     
    public int type;
    public static final String _type="type";
     
    public String content;
     
    public int status;
     
    public int requestState;
    public static final String _requestState="requestState";
     
    public int direction;
     
    public String groupId;
    public static final String _groupId="groupId";
     
    public String sourceId;
     
    public String fromClient;
     
    public String param;

  /*  public boolean isMySend(){
        String userId= ImSdk.getInstance().userId;
        if(TextUtils.isEmpty(userId))
            return false;
        return userId.equals(fromUserId);
    }*/
    public boolean isRead(){
        return status==1;
    }

    public boolean isUploaded(){
        if(requestState==REQ_STATES_UP_FILE_OK){
            return true;
        }
        ChatMessageV2.FileMsgBaseParam p= JSON.parseObject(param, ChatMessageV2.FileMsgBaseParam.class);
        if(p!=null && !TextUtils.isEmpty(p.key)){
            return true;
        }
        return false;
    }

}
