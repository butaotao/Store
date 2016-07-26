package com.dachen.dgroupdoctorcompany.entity;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/7.
 */
public class UpLoadFriend {
    public UpLoadFriend(){
        fileList = new ArrayList<>();
    }
    public String goodsName;
    public ArrayList<UploadFile> fileList;
    public class UploadFile{
        public String id;
        public String name;
        public String url;
    }


}
