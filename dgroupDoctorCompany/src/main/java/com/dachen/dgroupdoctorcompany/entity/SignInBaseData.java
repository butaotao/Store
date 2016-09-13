package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by weiwei on 2016/4/7.
 */
public class SignInBaseData extends Result {
    public Data  data;
    public class Data{
        public String msg;
        public Map map;
        public class Map{
            public String id;
            public int distance;
            public String point;
            public double createTime;
        }
    }
}
