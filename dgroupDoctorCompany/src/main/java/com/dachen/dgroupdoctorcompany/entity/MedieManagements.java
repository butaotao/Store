package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/2/22.
 */
public class MedieManagements extends Result{
   public  ArrayList<MedieManagement> list_datas ;
    public class MedieManagement{
        public class GoodsType{
            public String title;
            public String value;
        }
        public GoodsType goods$type;
        public String goods$company;
        public String goods$specification;
        public String goods$pack_specification;
        public String goods$bar_code;
        public String goods$general_name;
        public String goods$manufacturer;
        public String goods$trade_name;
        public Goods goods;
        public class GoodsUnit{
            public String id;
            public String name;
        }
        public String  goods$form;
        public class GoodsBizType{
            public int id;
            public String _type;
            public String name;
            public String title;
        }
        public String goods$number;
        public String goods$abbr;
        public String goods$image;
        public class Goods{
            public String id;
            public String title;
            public String _type;
        }

    }

}
