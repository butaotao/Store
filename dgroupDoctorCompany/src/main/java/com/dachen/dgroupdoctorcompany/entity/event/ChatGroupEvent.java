package com.dachen.dgroupdoctorcompany.entity.event;

import com.dachen.imsdk.db.po.ChatGroupPo;

/**
 * Created by Mcp on 2016/8/31.
 */
public class ChatGroupEvent {
    public ChatGroupPo group;

    public ChatGroupEvent(ChatGroupPo group) {
        this.group = group;
    }
}
