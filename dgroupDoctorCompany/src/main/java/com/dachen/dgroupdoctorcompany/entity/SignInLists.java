package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/2下午5:50.
 * @描述 TODO
 */
public class SignInLists extends Result {
    public String  detailMsg;
    public DataBean data;
    public static class DataBean {
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        public String  sorter;

        public List<PageDataBean> pageData;

        public static class PageDataBean {
            public String  times;
            public long  time;
            public String address;
            public String  deviceNum;
            public String remark;
            public String  department;
            public String  userName;
            public String  day;
            public String  longTime;
            public String  headPic;
            public String signedId;
            public String coordinate;
            public List<String> tag;
        }
    }
}
