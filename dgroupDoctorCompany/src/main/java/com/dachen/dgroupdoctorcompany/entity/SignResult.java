package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Burt on 2016/9/8.
 */
public class SignResult extends Result{
    public Data data;
    public class Data{
        /*"data":{"msg":"OK","signed":{"id":"57d0ca5db522251fefca8bfc",
                "userId":1613448,"companyId":"579045b4b522251188c685a7",
                "time":1473301085108,"remark":"",
                "singedTagId":"57c8379e557982b2b752c6b6",
                "department":null,"day":"2016-09-08",
                "orgId":"57c83519b522256c1280481f","userName":".hhhhh",
                "state":null,"deviceId":"354224061581870",
                "address":"claybox公司","visitId":null}},*/
        public Signed signed;
        public class Signed{
            public String id;
        }
    }
}
