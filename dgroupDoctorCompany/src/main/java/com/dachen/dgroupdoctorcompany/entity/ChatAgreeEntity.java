package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Burt on 2016/3/7.
 */
public class ChatAgreeEntity extends Result{
  /*  {
        "data": {
        "age": 0,
                "createTime": 1438340695833,
                "doctor": {
            "check": {
                "checkTime": 1445852960428,
                        "checker": "李新华",
                        "checkerId": "11807",
                        "departments": "康复医学科",
                        "deptId": "KF",
                        "hospital": "静海县静康门诊部",
                        "hospitalId": "201310110025",
                        "licenseExpire": "2015-10-28",
                        "licenseNum": "454263",
                        "title": "主治医师"
            },
*/
    public DATA data;
            public class DATA {
                public String userId;
                public String name;
                public int userType;
                public String headPicFileName;
                public int sex;

                public String telephone;
                public Doctor doctor;
                public String status;
             public class Doctor{
                 public String skill;
                public Check check;
                public class Check{
                public String checker;
                public String checkerId;
                public String departments;
                public String deptId;
                public String hospital;
                public String hospitalId;
                public String title;

            }
        }
    }
}
