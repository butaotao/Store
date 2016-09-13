package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/11.
 */
public class MettingUsers extends Result{
    public ArrayList<User> data;
    public class User{
        public long createTime;
        public String headPicFileName;
        public String name;
        public String telephone;
        public int userId;

    }
}
