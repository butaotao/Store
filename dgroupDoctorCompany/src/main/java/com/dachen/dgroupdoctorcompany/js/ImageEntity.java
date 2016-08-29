package com.dachen.dgroupdoctorcompany.js;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/26下午6:02.
 * @描述 TODO
 */
public class ImageEntity {
    public String file;//文件本地地址
    public String fileData;//图片base64编码

    public ImageEntity(String file, String fileData) {
        this.file = file;
        this.fileData = fileData;
    }
}
