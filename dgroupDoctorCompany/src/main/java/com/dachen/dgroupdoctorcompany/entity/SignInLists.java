package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/2下午5:50.
 * @描述 TODO
 */
public class SignInLists extends Result {

    public Object detailMsg;


    public DataBean data;

    public static class DataBean {
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        public String  sorter;
        /**
         * times : null
         * time : null
         * address : 宝安区
         * deviceNum : null
         * remark :
         * department : null
         * userName : null
         * day : null
         * longTime : 1472831823979
         * tag : ["下班"]
         * headPic : null
         * signedId : 57c9a14fb5222514578122e1
         * coordinate : 22.571391,113.86976
         */

        public List<PageDataBean> pageData;

        public static class PageDataBean {
            public String times;
            public String  time;
            public String address;
            public String deviceNum;
            public String remark;
            public String department;
            public String userName;
            public String day;
            public long longTime;
            public String headPic;
            public String signedId;
            public String coordinate;
            public List<String> tag;
        }
    }

}
