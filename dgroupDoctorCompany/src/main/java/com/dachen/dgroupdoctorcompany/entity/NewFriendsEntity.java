package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/7.
 */
public class NewFriendsEntity extends Result{
        public Data data;
   /* {
  "data": {
    "pageCount": 1,
    "pageData": [
      {
        "_id": "56e931094203f30c9a2c1b18",
        "fromUserId": 13363,
        "toUserId": 793,
        "status": 2,
        "createTime": 1458123036727,
        "updateTime": 1458123036727,
        "applyContent": "",
        "userReqType": 1,
        "fromUserName": "张三",
        "fromHeadPicFileName": "http://192.168.3.7:8081/avatar/o/3363/14577745760792.jpg",
        "toUserName": "huang",
        "toHeadPicFileName": "http://192.168.3.7:8081/avatar/o/793/14538768921393.jpg",
        "toUserTitle": "主治医师",
        "toUserHospital": "静海县静康门诊部"
      },*/


        public class Data{
            public ArrayList<FriendInfo> pageData;
            public class FriendInfo extends BaseSearch{
                public String _id;
                public String fromUserId;
                public String toUserId;
                public String status;
                public String createTime;
                public String updateTime;
                public String userReqType;
                public String fromUserName;
                public String fromHeadPicFileName;
                public String toUserName;
                public String toHeadPicFileName;
                public String toUserTitle;
                public String toUserHospital;
                public String toUserDepartment;
            }

    }
}
