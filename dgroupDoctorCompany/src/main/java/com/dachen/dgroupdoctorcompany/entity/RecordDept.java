package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/7/1.
 */
public class RecordDept extends Result{
    public Data data;
    public class Data{
        public ArrayList<UserInfos> data;
    }
}
