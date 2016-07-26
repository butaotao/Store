package com.dachen.medicine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.dachen.medicine.db.table.TableUser;

public class MedicineDBLiteHelper extends SQLiteOpenHelper {
	public static MedicineDBLiteHelper sSingleton;

	public MedicineDBLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		// TODO Auto-generated constructor stub
		super(context, DBConfig.DBNAME, null, DBConfig.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TableUser.getCreateSql());
		// db.execSQL(TableScheme.getCreateSql());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public static synchronized MedicineDBLiteHelper getInstance(Context context) {
		// if (sSingleton == null)
		// {
		sSingleton = new MedicineDBLiteHelper(context);
		// }
		return sSingleton;
	}

	/**
	 * 
	 * 构造器创建数据库
	 * 
	 * @param context
	 *            context
	 */
	private MedicineDBLiteHelper(Context context) {
		super(context, DBConfig.DBNAME, null, DBConfig.VERSION);
	}
}
