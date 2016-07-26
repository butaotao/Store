package com.dachen.dgroupdoctorcompany.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.Role;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Burt on 2016/2/29.
 */
public class SQLiteHelper  extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_NAME = "dachen_company.db";
    private static final int DATABASE_VERSION = 11;

    private Context context = null;

    private static SQLiteHelper instance;
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public static synchronized SQLiteHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (SQLiteHelper.class) {
                if (instance == null)
                    instance = new SQLiteHelper(context);
            }
        }
        return instance;
    }
    @Override
    public void close() {
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connSource) {
        Logger.d("yehj", "onCreate--->db");
        try {
           TableUtils.createTableIfNotExists(connSource,  CompanyContactListEntity.class);
            TableUtils.createTableIfNotExists(connSource,  Role.class);
            TableUtils.createTableIfNotExists(connSource,SearchRecords.class);
            TableUtils.createTableIfNotExists(connSource,Doctor.class);
            TableUtils.createTableIfNotExists(connSource,DepAdminsList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connSource, int oldVersion, int newVersion) {
        Logger.d("yehj", "onUpgrade--->db");
      try {
             if (oldVersion < 4) {
              TableUtils.dropTable(connSource, CompanyContactListEntity.class, true);


            }
          if (oldVersion < 11) {
              TableUtils.dropTable(connSource, DepAdminsList.class, true);


          }

            onCreate(db, connSource);

         } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
