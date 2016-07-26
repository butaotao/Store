package com.dachen.medicine.db;

import java.util.List;
import java.util.Vector;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.db.table.TableInterface;

public class MedicineProvider extends SQLiteContentProvider {
	private static final UriMatcher URIMATCHER;
	/**
	 * 是否需要根据帐号切换不同的db
	 */
	public static boolean isNeedChangeDb = false;
	/**
	 * 执行SQL操作的match
	 */
	private static final int EXECUTE = 1;

	/**
	 * 查询SQL操作的match
	 */
	private static final int QUERY = 2;

	/**
	 * 下载任务的match
	 */
	private static final int DOWNLOAD_TASK = 3;

	/**
	 * 演示方案
	 */
	private static final int DEMO_SCHEME = 4;

	/**
	 * 演示材料
	 */
	private static final int DEMO_MATERIAL = 5;

	/**
	 * 演示方案、材料关系
	 */
	private static final int DEMO_SCHEME_MATERIAL = 6;

	/**
	 * 演示模板
	 */
	private static final int DEMO_TEMPLATE = 7;

	/**
	 * 演示模板元素
	 */
	private static final int DEMO_TEMPLATE_ELEMENT = 8;

	/**
	 * 解决方案
	 */
	private static final int SOLUTION_TYPE = 9;

	/**
	 * 文件类型
	 */
	private static final int FILE_TYPE = 10;

	/**
	 * 数据库通知对象map
	 */
	private List<Uri> mNotifyChangeUri;

	static {
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);// TABLE_NAME
	}

	@Override
	public boolean onCreate() {
		super.onCreate();
		mDb = getDatabaseHelper().getWritableDatabase();
		mNotifyChangeUri = new Vector<Uri>();
		return mDb != null;
	}

	/**
	 * 获取databaseHelper对象
	 * 
	 * @param context
	 *            Context对象
	 * @return SQLiteOpenHelper 对象
	 */

	@Override
	protected SQLiteOpenHelper getDatabaseHelper(Context context) {
		return MedicineDBLiteHelper.getInstance(context);
	}

	/**
	 * 在事务中保存数据
	 * 
	 * @param uri
	 *            数据库表的URI
	 * @param values
	 *            数据封装对象
	 * @return 数据库表的URI
	 */

	@Override
	protected Uri insertInTransaction(Uri uri, ContentValues values) {
		String tableName = getTableNameByUri(uri);
		long rowId = mDb.insert(tableName, null, values);
		if (rowId > 0) {
			// 添加数据变更通知到列表
			addChangeNotify(uri);
		}
		return ContentUris.withAppendedId(uri, rowId);
	}

	/**
	 * 在事务中更新数据
	 * 
	 * @param uri
	 *            数据库表的URI
	 * @param values
	 *            数据封装对象
	 * @param selection
	 *            查询条件语句
	 * @param selectionArgs
	 *            查询条件参数
	 * @return 更新数据数量
	 */

	@Override
	protected int updateInTransaction(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		String tableName = getTableNameByUri(uri);
		int count = mDb.update(tableName, values, selection, selectionArgs);
		if (count > 0) {
			// 添加数据变更通知到列表
			addChangeNotify(uri);
		}
		return count;
	}

	/**
	 * 在事务中删除数据
	 * 
	 * @param uri
	 *            数据库表的URI
	 * @param selection
	 *            查询条件语句
	 * @param selectionArgs
	 *            查询条件参数
	 * @return 删除数据数量
	 */

	@Override
	protected int deleteInTransaction(Uri uri, String selection,
			String[] selectionArgs) {
		String tableName = getTableNameByUri(uri);
		int count = mDb.delete(tableName, selection, selectionArgs);
		if (count > 0) {
			// 添加数据变更通知到列表
			addChangeNotify(uri);
		}
		return count;
	}

	/**
	 * 通知数据变更<BR>
	 * 
	 * @param uri
	 *            数据库表的URI
	 */
	private void addChangeNotify(Uri uri) {
		mNotifyChangeUri.add(uri);
	}

	/**
	 * 根据Uri匹配出数据库表名<BR>
	 * 
	 * @param uri
	 *            数据库表的URI
	 * @return 数据库表名
	 */
	private String getTableNameByUri(Uri uri) {
		return TableInterface.TABLE_USER;
		/*
		 * if (uri == null) { return null; } int match = URIMATCHER.match(uri);
		 * switch (match) { case DOWNLOAD_TASK: return
		 * TableInterface.TABLE_USER; default: return null; }
		 */
	}

	/**
	 * 根据数据库表名匹配出Uri<BR>
	 * 
	 * @param tableName
	 *            数据库表名
	 * @return 数据库表Uri
	 */
	private Uri getUriByTableName(String tableName) {
		if (tableName == null) {
			return null;
		}
		if (tableName.equals(TableInterface.TABLE_USER)) {
			return MedicineURIField.DOWNLOAD_TASK_URI;
		}
		return null;
	}

	/**
	 * 数据库查询方法
	 * 
	 * @param uri
	 *            数据库表的URI
	 * @param projection
	 *            游标读取列
	 * @param selection
	 *            查询条件语句
	 * @param selectionArgs
	 *            查询条件参数
	 * @param sortOrder
	 *            排序条件
	 * @return 游标对象
	 */

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		String tableName = this.getTableNameByUri(uri);
		int match = URIMATCHER.match(uri);
		mDb = mOpenHelper.getWritableDatabase();
		switch (match) {
		case EXECUTE:
			tableName = sortOrder;
			Uri tempUri = getUriByTableName(tableName);
			addChangeNotify(tempUri);
			mDb.execSQL(selection, selectionArgs);
			notifyChange();
			break;
		case QUERY:
			cursor = mDb.rawQuery(selection, selectionArgs);
			break;
		default:
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(tableName);
			cursor = qb.query(mDb, projection, selection, selectionArgs, null,
					null, sortOrder, null);
			break;

		}
		if (cursor != null) {
			// 监测数据变更
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	/**
	 * 根据Uri获取类型
	 * 
	 * @param uri
	 *            数据库表的Uri
	 * @return 类型字符串
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 通知数据表发生变化
	 */

	@Override
	protected void notifyChange() {
		ContentResolver contentResolver = getContext().getContentResolver();
		synchronized (mNotifyChangeUri) {
			for (Uri uri : mNotifyChangeUri) {
				contentResolver.notifyChange(uri, null);
			}
			mNotifyChangeUri.clear();
		}

	}

	public static void changeDatabase(String name) {
		if (isNeedChangeDb) {
			Log.i("", "changeDatabase name: " + name);
			if (DBConfig.DATABASE_NAME.equals(name))
				return;
			DBConfig.DATABASE_NAME = name + DBConfig.DATABASE_NAME;
			if (mOpenHelper != null) {
				mOpenHelper.close();
			}
			mOpenHelper = MedicineDBLiteHelper.getInstance(MedicineApplication
					.getApplication());

			// 更换帐号后重新加载json数据
			// ((MedicineApplication)
			// MedicineApplication.getApplication()).initData();
			Log.i("", "change complete!");
		} else {
			// 更换帐号后重新加载json数据
			// ((MedicineApplication)
			// MedicineApplication.getApplication()).initData();
		}

	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
