package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Administrator on 2016/6/29.
 */
public class VisitMemberResponse extends Result{

    private VisitMemberModel data;

    public VisitMemberModel getData() {
        return data;
    }

    public void setData(VisitMemberModel data) {
        this.data = data;
    }
}
