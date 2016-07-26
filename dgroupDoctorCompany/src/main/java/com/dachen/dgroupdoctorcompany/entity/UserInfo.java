package com.dachen.dgroupdoctorcompany.entity;
import com.dachen.medicine.entity.Result;
import java.util.ArrayList;

/**
 * Created by weiwei on 2016/3/5.
 */

public class UserInfo extends Result {

    public Data data;
    public class Data{
        public String companyName;
        public String id;
        public String name;
        public String position;
        public String telephone;
        public ArrayList<Role> role;
        public String remarks;
        public int userId;
        public String department;
        public String status;
        public String headPicFileName;
        public String userStatus;
        public class Role{
            public String key;
            public String name;
        }
    }
}
