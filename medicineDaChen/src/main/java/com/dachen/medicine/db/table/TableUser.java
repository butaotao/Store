package com.dachen.medicine.db.table;

import android.content.ContentValues;

import com.dachen.medicine.entity.User;

public class TableUser {
	/** 表名 **/
	public static final String TABLE_NAME = "user_table";
	/**
	 * 用户Id
	 */
	public static String COLUMN_NAME_USERID = "userId";
	/**
	 * 用户类型
	 */
	public static int userType;
	/**
	 * 手机号
	 */

	public static String telephone;
	/**
	 * 用户姓名
	 */

	public static String COLUMN_NAME_ID = "name";
	/**
	 * 性别
	 */

	public static int COLUMN_NAME_SEX = 0;
	/**
	 * 用户签名信息
	 */

	public static String COLUMN_NAME_DESCRIPTION = "description";
	public static String COLUMN_NAME_TOKEN = "token";
	public static String COLUMN_HEADURL_TOKEN = "head_url";
	public static String COLUMN_NAME_PASSWORD = "password";

	public static String COLUMN_NAME_LOGIN_TIME = "logintime";
	public static String COLUMN_NAME_LOGIN_TELEPHONE = "telephone";
	public static String  COLUMN_NAME_BOOLEANOPEN = "booleanopen";

	public static String birthday;

	public static String companyId;

	public static String email;

	public static int status;

	public static String nickname;

	public static int isAuth;

	public static int age;

	public static String headPicFileName;

	public static String area; // 所在地
	/**
	 * 非业务ID,自增长
	 */
	public static final String ID = "_ID";

	public static final String getCreateSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(" CREATE TABLE ").append(TABLE_NAME);
		builder.append(" ( ");
		builder.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		builder.append(COLUMN_NAME_ID).append(" TEXT,");
		builder.append(COLUMN_NAME_PASSWORD).append(" TEXT,");
		builder.append(COLUMN_NAME_LOGIN_TIME).append(" TEXT,");
		builder.append(COLUMN_NAME_USERID).append(" TEXT,");
		builder.append(COLUMN_NAME_LOGIN_TELEPHONE).append(" TEXT,");
		builder.append(COLUMN_NAME_BOOLEANOPEN).append(" TEXT,");
		builder.append(COLUMN_NAME_TOKEN).append(" TEXT,");
		builder.append(COLUMN_HEADURL_TOKEN).append(" TEXT,");
		builder.append(COLUMN_NAME_DESCRIPTION).append(" TEXT);");
		return builder.toString();
	}

	/**
	 * 
	 * @param info
	 * @return
	 */
	public static ContentValues buildContentValues(User info) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, info.getName());
		values.put(COLUMN_NAME_PASSWORD, info.getPassword());
		values.put(COLUMN_NAME_BOOLEANOPEN, info.getBooleanOpen());
		// values.put(COLUMN_NAME_LOGIN_TIME, info.loginLog.getLoginTime());
		values.put(COLUMN_NAME_USERID, info.getUserId());
		values.put(COLUMN_NAME_LOGIN_TELEPHONE, info.getTelephone());
		values.put(COLUMN_NAME_DESCRIPTION, info.getDescription());
		values.put(COLUMN_NAME_TOKEN, info.getToken());
		values.put(COLUMN_HEADURL_TOKEN, info.headUrl);
		return values;
	}
}
