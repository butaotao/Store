package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by weiwei on 2016/6/29.
 */
public class ConfirmVisit extends Result {
    public Data data;
    public class Data{
        public String msg;
        public Object dataList;
    }
}
