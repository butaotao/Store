package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;
import com.dachen.medicine.volley.custom.ArrayResult;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/2/19.
 */
public class LoginVerify extends Result{
    public Logins for_api_login;
    public class Logins {
        public ArrayList<Verify> roles;
        public class Verify{
            public String title;
            public String value;
        }
    };

}
