package com.dachen.medicine.entity;

/**
 * Created by Burt on 2016/1/26.
 */
public class MyIncomeData extends Result{
    public Data data;
    public class Data{
        //待审核 fee1;
        public String fee1;
        //未发放 fee2
        public String fee2;
        //总收入fee3；
        public String fee3;
    }
}
