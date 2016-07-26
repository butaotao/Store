package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/6/1.
 */
public class MeidecineSearchList extends Result {
    public List<Data> data;
    public class Data{
        public String abbr;
        public long approvalDate;
        public boolean collected;
        public String companyId;
        public String companyName;
        public long createdDate;
        public String drugFormCode;
        public String form;
        public String formText;
        public String generalName;
        public String groupId;
        public String id;
        public String imageUrl;
        public String indications;
        public long lastModifiedDate;
        public String manageType;
        public String manageTypeText;
        public String manual;
        public String manufacturer;
        public String manufacturer2;
        public String number;
        public boolean ownedGroup;
        public String packSpecification;
        public String packUnit;
        public String packUnitText;
        public String pharmacoTypes;
        public String pharmacoTypesText;
        public String productType;
        public String productTypeText;
        public String specification;
        public String stateAudit;
        public String title;
        public String tradeName;
        public String type;
        public String valid;
        public List<DrugUsegeList> drugUsegeList;
        public class DrugUsegeList{
            public String method;
            public String patients;
            public String periodNum;
            public String periodTime;
            public String quantity;
            public String times;
            public String unit;
        }

    }
}
