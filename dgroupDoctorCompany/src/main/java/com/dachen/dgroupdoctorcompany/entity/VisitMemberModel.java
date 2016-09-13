package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Administrator on 2016/6/29.
 */
public class VisitMemberModel extends BaseModel{

    private String msg;
    private VisitMember visit;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VisitMember getVisit() {
        return visit;
    }

    public void setVisit(VisitMember visit) {
        this.visit = visit;
    }
}
