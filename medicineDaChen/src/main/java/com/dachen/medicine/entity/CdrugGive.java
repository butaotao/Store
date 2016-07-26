package com.dachen.medicine.entity;

/**
 * Created by Burt on 2016/6/8.
 */
public class CdrugGive extends Result{
    public Data data;
    public class Data{
      /*  "consumePointsNum": 0,
                "form": "",
                "formText": "",
                "generalName": "",
                "goodsId": "57468db839064022a419b81f",
                "headPicFileName": "",
                "isJoin": 1,
                "isOwn": 1,
                "lastDrugStoreName": "",
                "leftPointsNum": 0,
                "lowPointsNum": 0,
                "packUnitText": "",
                "patientId": 1371,
                "patientName": "段段",
                "patientSex": 1,
                "tradeName": ""*/
        public int consumePointsNum;
        public String form;
        public String formText;
        public String generalName;
        public String goodsId;
        public String headPicFileName;
        public String isJoin;
        public String isOwn;
        public String lastDrugStoreName;
        public int leftPointsNum;
        public int lowPointsNum;
        public String packUnitText;
        public String patientId;
        public String patientName;
        public int patientSex;
        public String tradeName;
        public String title;
        public String packSpecification;
        public String manufacturer;
        public String specification;
    }
}
