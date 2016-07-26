package com.dachen.mediecinelibraryrealize.entity;
 

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 使用方法
 * 
 * try { Dao<Friend, Integer> dao =
 * SQLiteHelper.getHelper(context).getDao(Friend.class); } catch (SQLException
 * e) { e.printStackTrace(); }
 * 
 * @author lmc
 *
 */
public class SQLiteHelpers extends OrmLiteSqliteOpenHelper {

	private static final String TAG = SQLiteHelpers.class.getSimpleName();

	public static final String DATABASE_NAME = "dachen_patients.db";
	/*
	 * == 2 1) 新增表AuthMessage.class 2) 给Friend表新增几个列 7) (8)加入simpleuserinfo表 .
	 * 新增表DoctorGroupSearchHistory.class
	 **/
	private static final int DATABASE_VERSION = 8;

	private Context context = null;

	private static SQLiteHelpers instance; 

	public SQLiteHelpers(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connSource) { 
		try { 
			// 用药提醒
			TableUtils.createTableIfNotExists(connSource, DrugRemind.class);
			// 用药提醒的闹钟
			TableUtils.createTableIfNotExists(connSource, Alarm.class);
 

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized SQLiteHelpers getHelper(Context context) {
		context = context.getApplicationContext();
		if (instance == null) {
			synchronized (SQLiteHelpers.class) {
				if (instance == null)
					instance = new SQLiteHelpers(context);
			}
		}
		return instance;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connSource, int oldVersion, int newVersion) {
	 
		try {
			if (oldVersion <= 7) {
			 
				// 用药提醒
				TableUtils.dropTable(connSource, DrugRemind.class, true);
				// 用药提醒的闹钟
				TableUtils.dropTable(connSource, Alarm.class, true);
			}
			onCreate(db, connSource);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

 
	@Override
	public void close() {
		super.close(); 
	}

}
