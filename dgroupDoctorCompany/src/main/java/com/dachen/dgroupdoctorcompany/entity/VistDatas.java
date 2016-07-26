package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/29.
 */
public class VistDatas extends Result{
    public Data data;
    public class Data{
        public Datas data;
        public ArrayList<VistRecorddataList> pageData;
        public class Datas{
            public ArrayList<VistRecorddataList> pageData;
        }


    }
}
