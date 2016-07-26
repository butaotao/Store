package com.dachen.medicine.entity;

/**
 * apk版本信息
 *
 * @author gzhuo
 * @date 2016/6/13
 */
public class VersionInfo extends Result {
    public Data data;

    public class Data{
        public int id;
        public String code;
        public String name;
        public String info;
        public String device;
        public String version;
        public String downloadUrl;
    }
}
