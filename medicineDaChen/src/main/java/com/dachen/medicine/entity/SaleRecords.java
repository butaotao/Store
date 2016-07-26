package com.dachen.medicine.entity;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/4.
 */
public class SaleRecords extends Result{
    public Data data;
    public class Data{
        public ArrayList<SaleRecord> pageData;
        public int total;
    }
}
