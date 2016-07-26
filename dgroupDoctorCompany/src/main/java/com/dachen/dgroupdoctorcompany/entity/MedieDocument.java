package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/2/27.
 */
public class MedieDocument extends Result {
    public Data data;
    public  class Data{
        public int total;
        public int start;
        public int pageSize;
        public int pageIndex;
        public int pageCount;
        public ArrayList<MedieDocumentItem> pageData;

        public class MedieDocumentItem{
            public String bucketType;
            public String fileId;
            public String hash;
            public long lastUpdateDate;
            public String lastUpdator;
            public String mimeType;
            public String name;
            public String size;
            public String spaceName;
            public String status;
            public String suffix;
            public String type;
            public String uploadDate;
            public String uploader;
            public String url;
            public boolean isSelected;
        }
    }

}
