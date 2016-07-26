package com.dachen.dgroupdoctorcompany.entity;

import java.util.List;

/**
 * Created by weiwei on 2016/6/20.
 */
public class SignLable extends Results {
    public Data data;
    public class Data{
        public List<SignLableItem> data;
        public class SignLableItem{
            public String id;
            public String label;
            public String userId;
            public String createTime;
            public String issystem;
        }
    }
}
