package com.dachen.medicine.db;

import android.net.Uri;

import com.dachen.medicine.db.table.TableInterface;

public interface MedicineURIField {
	/**
	 * The authority for the contacts provider
	 */
	public static final String AUTHORITY = "com.dachen.medicine";

	/**
	 * A content:// style uri to the authority for the rcsbaseline provider
	 */
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	/**
	 * 查询SQL操作保留字
	 */
	public static final String QUERY = "querySql";

	/**
	 * 执行SQL操作保留字
	 */
	public static final String EXECUTE = "executeSql";

	/**
	 * 查询URI
	 */
	public static final Uri QUERY_SQL_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + QUERY);

	/**
	 * 执行URI
	 */
	public static final Uri EXECUTE_SQL_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + EXECUTE);

	/**
	 * 下载任务表URI
	 */
	public static final Uri DOWNLOAD_TASK_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TableInterface.TABLE_USER);
}
