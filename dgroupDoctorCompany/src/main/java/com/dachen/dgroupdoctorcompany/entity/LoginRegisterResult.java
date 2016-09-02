package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.medicine.entity.Assistant;
import com.dachen.medicine.entity.Doctor;
import com.dachen.medicine.entity.Loc;
import com.dachen.medicine.entity.LoginLog;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.Settings;
import com.dachen.medicine.entity.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginRegisterResult extends Result {
	public LoginData data;

	public class LoginData {

		public String access_token;
		public String access_context;

		public String userId;

		public int expires_in;
		public ArrayList<DepAdminsList>  depAdminsList;
 		public DrugCompany drugCompany;
		public MajorUser majorUser;
		public class DrugCompany{
			public String name;
		}
		public class MajorUser{
			public int active;
			public BizRoleConfig bizRoleConfig;
			public class BizRoleConfig{
				public int drugSales;
				public int marketing;

			}
			public String drugCompanyId;
			public String employeeId;
			public String hidePhone;
			public String id;
			public String jobType;
			public String name;
			public String openId;
			public String orgId;
			public String orgName;
			public int rootManager;
			public int status;
			public int sysManager;
			public String telephone;
			public String title;
			public String treePath;

			@Expose
			@SerializedName("headPicUrl")
			public String headPic;
		}
	}

}
