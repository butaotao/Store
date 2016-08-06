package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/5.
 */
public class OftenSignPlace extends Result{
    public Data data;
    public class Data{
        public ArrayList<PageData> pageData;
        public class PageData{
                public String coordinate;
                public String creator;
                public String creatorDate;
                public String drugCompanyId;
                public String fullAddress;
                public String id;
                public String simpleAddress;
                public String type;
                public int updator;
                public long updatorDate;

        }
    }
}
