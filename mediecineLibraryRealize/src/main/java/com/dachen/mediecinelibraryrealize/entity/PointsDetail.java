package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/6/2.
 */
public class PointsDetail extends Result {
    public Data data;
    public class Data{
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        public List<PageData>pageData;
        public class PageData{
            public int checkStatus;
            public long checkTime;
            public String companyId;
            public long createdDate;
            public String drugStoreId;
            public String goodsId;
            public String goodsNum;
            public String id;
            public int isReceive;
            public long lastModifiedDate;
            public String packUnit;
            public String packUnitText;
            public int patientId;
            public String pointsNum;
            public String ruleId;
            public String title;
            public int type;
            public String userId;
        }
    }
}
