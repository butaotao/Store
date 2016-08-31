package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/4/8.
 */
public class SignTodayInList extends Result {
    public String detailMsg;
    public Data   data;
    public class Data{
        public long timeStamp;
        public String   msg;
        public ArrayList<DataList> signedList;
        public class DataList{
            public String time;
            public String imgPath;
            public String address;
            public String remark;
            public String department;
            public String userName;
            public long longTime;
            public String[] tag;
            public String headPic;
            }
    }

    @Override
    public String toString() {
        return "SignInList{" +
                "detailMsg='" + detailMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
