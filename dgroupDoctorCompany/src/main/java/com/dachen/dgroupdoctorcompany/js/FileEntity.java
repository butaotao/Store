package com.dachen.dgroupdoctorcompany.js;

/**
 * @author gzhuo
 * @date 2016/8/5
 */
public class FileEntity {
    public String file;//本地文件地址
    public String url;//上传后的服务器地址
    public String key;

    public FileEntity() {
    }

    public FileEntity(String file, String url, String key) {
        this.file = file;
        this.url = url;
        this.key = key;
    }
}
