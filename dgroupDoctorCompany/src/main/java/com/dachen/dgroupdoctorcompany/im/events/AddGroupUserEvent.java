package com.dachen.dgroupdoctorcompany.im.events;

/**
 * Created by Mcp on 2016/3/4.
 */
public class AddGroupUserEvent {

    public String groupId;

    public AddGroupUserEvent(String groupId) {
        if(groupId==null)
            groupId="";
        this.groupId = groupId;
    }
}
