package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/2.
 */
public class SearchDrugDtaList extends Result{
    public Data data;
    public class Data{
        public ArrayList<DrugData> pageData;
    }

}
