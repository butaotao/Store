package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/27.
 */
public class SignedRecordLists extends Result{
    public Data data;
     public class Data {//data
        public ArrayList<SignedRecords> pageData;
        public class Records{
            public int pageCount;
            public int pageIndex;
            public int pageSize;
            public int start;
            public int total;
            public String sorter;
            public ArrayList<SignedRecords> pageData;
        }
        public Records data;
    }
}
