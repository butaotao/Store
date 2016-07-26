package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/1.
 */
public class CompanyDepment extends Result {
    public Data data;

    public class Data {

        public ArrayList<Depaments> departments;
        public ArrayList<Integer> users;

        public class Depaments extends BaseSearch {
            public int creator;
            public String creatorDate;
            public String desc;
            @Expose
            @SerializedName("drugCompanyId")
            public String enterpriseId;
            public String id;
            public String name;
            public int updator;
            public String updatorDate;
            public String parentId;
            //0已分配，1未分配
            public String type;

        }

    }
    /*{
        "data": {
        "departments": [
        {
            "creator": 216,
                "creatorDate": 1456555874748,
                "desc": "康泽药业",
                "enterpriseId": "56d10990b472651e1cfdc819",
                "id": "56d147623906402eb0af5517",
                "name": "康泽药业",
                "updator": 216,
                "updatorDate": 1456555874750
        },
        {
            "creator": 12270,
                "creatorDate": 1456727960833,
                "desc": "",
                "enterpriseId": "56d10990b472651e1cfdc819",
                "id": "56d3e35b4203f357dc705d36",
                "name": "wangzl",
                "updator": 12270,
                "updatorDate": 1456727960833
        }
        ],
        "users": [

        ]
    },
        "resultCode": 1
    }*/
}
