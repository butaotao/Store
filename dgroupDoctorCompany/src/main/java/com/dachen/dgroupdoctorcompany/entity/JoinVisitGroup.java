package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/6/28.
 */
public class JoinVisitGroup extends Result {
    public Data data;
    public class Data{
        public String msg;
        public List<VisitPeople> groupDetails;
        public DataList dataList;
        public class DataList{
            public String id;
            public String companyId;
            public String orgId;
            public Location loc;
            public class Location{
                public double lng;
                public double lat;
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
            public int remainTime;
            public List<String> userGroups;
            public List<GoodsGroups> goodsGroups;
            public class GoodsGroups{
                public String id;
                public String name;
            }
        }
    }
}
