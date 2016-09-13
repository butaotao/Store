package com.dachen.dgroupdoctorcompany.im.events;

/**
 * Created by Mcp on 2016/3/3.
 */
public class UnreadEvent {
    public static final int TYPE_CUSTOMER=1;

    public int type;
    public Object from;

    public UnreadEvent(int type) {
        this.type = type;
    }

    public UnreadEvent(int type, Object from) {
        this.type = type;
        this.from = from;
    }
}
