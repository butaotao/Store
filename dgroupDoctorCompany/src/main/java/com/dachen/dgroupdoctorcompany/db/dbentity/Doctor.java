package com.dachen.dgroupdoctorcompany.db.dbentity;

import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Burt on 2016/3/4.
 */
@DatabaseTable(tableName = "table_companydoctor")
public class Doctor extends BaseSearch{


   /* "data": [
    {
        "departments": "康复医学科",
            "enterpriseUserList": [
        {
            "departments": "康复医学科",
                "headPicFileName": "http://192.168.3.7:8081/avatar/o/793/14538768921393.jpg",
                "hospital": "静海县静康门诊部",
                "hospitalId": "201310110025",
                "name": "huang",
                "sex": 1,
                "skill": "细菌性传染病,螺旋体病,营养障碍疾病,青春期疾病,神经肌肉系统疾病,荨麻疹类皮肤病,考虑考虑考虑考虑考虑考虑考虑考虑考虑考",
                "telephone": "18689208527",
                "title": "主治医师",
                "userId": 793
        }
        ]
    }
    ],
            "resultCode": 1
*/
    @DatabaseField(generatedId=true)
    public int _id;

    @DatabaseField(columnName = "departments")
    public String departments;

    @DatabaseField(columnName = "headPicFileName")
    public String headPicFileName;

    @DatabaseField(columnName = "hospital")
    public String hospital;

    @DatabaseField(columnName = "hospitalId")
    public String hospitalId;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "sex")
    public int sex;

    @DatabaseField(columnName = "skill")
    public String skill;

    @DatabaseField(columnName = "telephone")
    public String telephone;

    @DatabaseField(columnName = "title")
    public String title;

    @DatabaseField(columnName = "userId")
    public String userId;

    @DatabaseField(columnName = "userloginid")
    public String userloginid;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Doctor){
            Doctor d = (Doctor) o;
         return !TextUtils.isEmpty(d.userId) && d.userId.equals(this.userId) && !TextUtils.isEmpty(this.userId);
        }
         return  false;
    }

}
