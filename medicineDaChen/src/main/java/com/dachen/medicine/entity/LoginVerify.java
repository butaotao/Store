package com.dachen.medicine.entity;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/15.
 */
public class LoginVerify extends Result{
    //data
    public Logins data;
    public class Logins {
        //companys
        public ArrayList<Verify> companys;
        public class Verify{
            public String companyType;
            public String duty;
            public String companyId;
        }
    };

}
