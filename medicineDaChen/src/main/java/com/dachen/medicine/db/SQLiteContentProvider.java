package com.dachen.medicine.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.net.Uri;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceConst;

public abstract class SQLiteContentProvider extends ContentProvider implements
		SQLiteTransactionListener {
	/**
	 * 批量处理时线程让行后的延时时长
	 */
	private static final int SLEEP_AFTER_YIELD_DELAY = 4000;

	/**
	 * 批量处理时最大的连续处理操作数
	 */
	private static final int MAX_OPERATIONS_PER_YIELD_POINT = 500;
	/**
	 * 各线程是否在批处理的标记
	 */
	private final ThreadLocal<Boolean> mApplyingBatch = new ThreadLocal<Boolean>();
	/**
	 * 数据库
	 */
	protected SQLiteDatabase mDb;

	/**
	 * 数据库操作帮助类
	 */
	protected static SQLiteOpenHelper mOpenHelper;

	/**
	 * 有改动需要notifyChange的标记
	 */
	private volatile boolean mNotifyChange;

	@Override
	public void onBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRollback() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 判断是否在执行批处理
	 * 
	 * @return 是否在执行批处理
	 */
	private boolean applyingBatch() {
		// 本线程有被赋值且为true时返回true
		return mApplyingBatch.get() != null && mApplyingBatch.get();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		boolean applyingBatch = applyingBatch();
		if (!applyingBatch) {
			mDb = mOpenHelper.getWritableDatabase();
			mDb.beginTransactionWithListener(this);
			try {
				result = insertInTransaction(uri, values);
				// 操作成功标记有数据改动
				if (result != null) {
					mNotifyChange = true;
				}
				mDb.setTransactionSuccessful();
			} finally {
				mDb.endTransaction();
			}
			onEndTransaction();
		}
		// 有批处理在执行时不需要另外开启事物
		else {
			result = insertInTransaction(uri, values);
			// 操作成功标记有数据改动
			if (result != null) {
				mNotifyChange = true;
			}
		}
		return result;
	}

	/**
	 * 返回一个SQLiteOpenHelper
	 * 
	 * @return SQLiteOpenHelper对象
	 */
	public static SQLiteOpenHelper getDatabaseHelper() {
		return mOpenHelper;
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				SharedPreferenceConst.LOCK, Context.MODE_PRIVATE);
		String name = mSharedPreferences.getString("userid", "");
		LogUtils.burtLog("name====" + name);
		if (!TextUtils.isEmpty(name)) {
			DBConfig.DATABASE_NAME = name + DBConfig.DATABASE_NAME;
		}

		// 获取openhelper
		mOpenHelper = getDatabaseHelper(context);
		return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 * 抽象方法，用于获取SQLiteOpenHelper
	 * 
	 * @param context
	 *            Context
	 * @return SQLiteOpenHelper
	 */
	protected abstract SQLiteOpenHelper getDatabaseHelper(Context context);

	/**
	 * 抽象方法，事物中对insert的处理，用于子类继承
	 * 
	 * @param uri
	 *            通用资源标志符 代表要操作的数据
	 * @param values
	 *            ContentValues 要插入的值
	 * @return 插入后的uri描述
	 * @see com.huawei.basic.android.im.component.database.SQLiteContentProvider
	 *      #insertInTransaction(android.net.Uri, android.content.ContentValues)
	 */
	protected abstract Uri insertInTransaction(Uri uri, ContentValues values);

	/**
	 * 抽象方法，事物中对update的处理，用于子类继承
	 * 
	 * @param uri
	 *            通用资源标志符 代表要操作的数据
	 * @param values
	 *            要更新的值
	 * @param selection
	 *            更新条件
	 * @param selectionArgs
	 *            String[] 更新条件参数值
	 * @return count 返回更新的条数
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 *      android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	protected abstract int updateInTransaction(Uri uri, ContentValues values,
			String selection, String[] selectionArgs);

	/**
	 * 抽象方法，事物中对delete的处理，用于子类继承
	 * 
	 * @param uri
	 *            通用资源标志符 代表要操作的数据
	 * @param selection
	 *            删除的条件
	 * @param selectionArgs
	 *            删除的条件参数值
	 * @return 返回删除的条数
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 *      java.lang.String, java.lang.String[])
	 */
	protected abstract int deleteInTransaction(Uri uri, String selection,
			String[] selectionArgs);

	/**
	 * 当有数据内容变动时，通知变动的抽象方法，用于子类继承
	 * 
	 * @see com.huawei.basic.android.im.component.database.SQLiteContentProvider#notifyChange()
	 */
	protected abstract void notifyChange();

	/**
	 * /** 事物开始时的操作
	 */
	protected void onBeginTransaction() {
	}

	/**
	 * 事物执行成功是的操作
	 */
	protected void beforeTransactionCommit() {
	}

	/**
	 * 事物执行结束后的操作
	 */
	protected void onEndTransaction() {
		// 如有变动，通知。
		if (mNotifyChange) {
			mNotifyChange = false;
			notifyChange();
		}
	}

	/**
	 * 结束并开启一个新事物
	 */
	protected void newTransaction() {
		mDb.setTransactionSuccessful();
		mDb.endTransaction();
		mDb = mOpenHelper.getWritableDatabase();
		mDb.beginTransactionWithListener(this);
	}
}
