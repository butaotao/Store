package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/7/4.
 */
public class GoodsGroupEntity extends Result{
    public Data data;
    public class Data{
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        public List<PageData> pageData;
        public class PageData{
            public String abbr;
            public String cloudCount;
            public String companyId;
            public String companyName;
            public long createdDate;
            public String generalName;
            public String id;
            public long lastModifiedDate;
            public String manufacturer;
            public String manufacturer2;
            public String medicineReps;
            public String pharmacoTypesText;
            public String productTypeText;
            public String title;
            public String tradeName;
            public List<PharmacoTypes> pharmacoTypes;
            public class PharmacoTypes{
                public String id;
                public String name;
            }
            public boolean select;
        }
    }
}
