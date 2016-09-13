package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/7/1.
 */
public class VisitRecordUserInfo extends Result{
    public Data data;
    public class Data{
        public ArrayList<PageData> pageData;
        public class PageData{
            public String headPicUrl;
            public String name;
            public String userId;
            public String remarks;
            public String orgName;
            public String drugCompanyId;
            public String id;
            public String orgId;
        }
    }
}
