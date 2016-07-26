package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/13.
 */
public class HospitalManager extends Result{
    public Data data;
    public class Data{
        public ArrayList<HospitalMangerData> pageData;
        public int total;
    }
}
