package com.dachen.medicine.common.utils;

import com.dachen.medicine.entity.GoodsPointRule;
import com.dachen.medicine.entity.UserPoint;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/6.
 */
public class Drug {
       /*
        "receiptList": [
        {
            "boughtQuantity": 0,
                "generalName": "板蓝根冲剂",
                "goodsId": "573ee42339064030340e3f13",
                "goodsPointRule": {

        },
            "isJoin": 1,
                "isOwn": 1,
                "lastDrugStoreName": "",
                "manufacturer": "峨眉山药厂",
                "packSpecification": "302g/盒",
                "packUnit": "573c264788f40744d8a857e0",
                "packUnitText": "瓶",
                "recipeId": "574fb4b9bae14d15d21f79f9",
                "requireQuantity": 1,
                "specification": "规格55",
                "state": 1,
                "tradeName": "板蓝根",
                "userPoint": {
            "patientId": 1371,
                    "goodsId": "573ee42339064030340e3f13",
                    "leftPointsNum": 0,
                    "usedPointsNum": 0,
                    "userId": 313,
                    "totalPointsNum": 0
        }
        },  "userInfo": {
      "name": "段武举2",
      "id": 313
    }*/
    public int boughtQuantity;
    public String generalName;
    public String goodsId;
    public int isJoin;
    public int isOwn;
    public String lastDrugStoreName;
    public String manufacturer;
    public String packSpecification;
    public String packUnit;
    public String packUnitText;
    public String recipeId;
    public int requireQuantity;
    public String specification;
    public String state;
    public String tradeName;
    public GoodsPointRule goodsPointRule;
    public UserPoint userPoint;
    }
