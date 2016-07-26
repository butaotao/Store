package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/6/24.
 */
public class VisitGroupEntity extends Result {
    public Data data;
    public class Data{
        public String msg;
        public List<VistGroupItem>dataList;
        public class VistGroupItem{
            public String id;
            public String companyId;
            public Location loc;
            public class Location{
                public double lat;
                public double lng;
            }
            public String doctorId;
            public String doctorName;
            public String initatorId;
            public String initatorName;
            public long createTime;
            public int state;
            public String address;
            public String addressName;
            public String headPic;
            public List<GoodsGroups> goodsGroups;
            public class GoodsGroups{
                public String id;
                public String name;
            }
            public List<String>userGroups;
            public String remainTime;
        }
    }
}
