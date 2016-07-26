package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/29.
 */
public class VistDetails extends Result{
    public Data data;
    public class Data{

        public ArrayList<String> headPicList;
        public Visit visit;
    }
    public static class Visit{
        public ArrayList<Sum> summaryList;

        public String name;
        public String doctorName;
        public long time;
        public String addressName;
        public String address;
        public ArrayList<GoodsGroups> goodsGroups;
        public static class GoodsGroups{
            public String name;
            public String id;
        }
    }
    public static class Sum{
        public String name;
        public long time;
        public String headPic;
        public String userId;
        public ArrayList<String> imgUrls;
        public String remark;

    }
}
