package com.dachen.dgroupdoctorcompany.entity;
import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/3/5.
 */

public class UserInfo extends Result {

    public Data data;
    public class Data{
        public String companyName;
        //orgId
        @Expose
        @SerializedName("orgId")
        public String id;
        public String name;
       // title
       @Expose
       @SerializedName("title")
        public String position;
        public String telephone;
        public ArrayList<Role> role;
        public String remarks;
        public int userId;
        //orgName
        @Expose
        @SerializedName("orgName")
        public String department;
        public String status;
        public String headPicFileName;
        public String userStatus;
        public int deptManager;
        public BizRoleConfig bizRoleConfig;
        public class BizRoleConfig{
            public String drugSales;
        }
    }
}
