package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Burt on 2016/9/5.
 */
public class SmsSend extends Result{
    public Data data;
    public class Data{
        public String smsid;
    }
}
