package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by weiwei on 2016/4/9.
 */
public class VisitDetail extends Result {
    public String detailMsg;
    public Data   data;
    public class Data{
        public String msg;
        public Visit visit;
        public class Visit{
            public String userId;
            public String doctorId;
            public String doctorName;
            public long   time;
            public String remark;
            public String coordinate;
            public String address;
            public String type;
            public String id;
            public String headPic;
        }
    }
}
