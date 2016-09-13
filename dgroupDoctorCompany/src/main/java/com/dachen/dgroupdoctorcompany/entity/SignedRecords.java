package com.dachen.dgroupdoctorcompany.entity;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/27.
 */
public class SignedRecords {
    public String id;
    public String name;
    public String userId;
    public String headPic;
    public String telephone;
    //2为异常
    public String state;
    public String departmentNmae;
    public long time;
    public ArrayList<TogetherVisit> listSig;
    public ArrayList<VistRecorddata> vistlist;
    public String orgId;
    public String companyId;
    public boolean isVisit;
   /* {
        "id": null,
            "name": "GCTES",
            "headPic": "http://default.dev.file.dachentech.com.cn/user/default.jpg",
            "telephone": null,
            "state": "2",
            "listSig": [
        {
            "times": null,
                "time": "16:04",
                "address": "三维动画",
                "deviceNum": null,
                "remark": null,
                "department": null,
                "userName": null,
                "day": null,
                "tag": [
            "上班"
            ],
            "headPic": null,
                "signedId": "576a46764203f35f1f4cb013"
        }
        ]
    },
*/
}
