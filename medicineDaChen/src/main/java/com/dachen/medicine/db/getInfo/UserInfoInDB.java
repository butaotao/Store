package com.dachen.medicine.db.getInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.db.MedicineURIField;
import com.dachen.medicine.db.table.TableUser;
import com.dachen.medicine.entity.User;

public class UserInfoInDB {
	public static User getUser(final String string) {
		User material = null;
		{
			Cursor cursor = null;
			try {
				cursor = MedicineApplication
						.getApplication()
						.getContentResolver()
						.query(MedicineURIField.DOWNLOAD_TASK_URI, null,
								TableUser.COLUMN_NAME_USERID + " = " + string,
								null, null);
				if (cursor.moveToFirst()) {
					material = buildMaterial(cursor);
					LogUtils.burtLog("material===" + material);
				}
			} catch (Exception e) {
				LogUtils.burtLog("material111===");
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
				LogUtils.burtLog("material222===");
			}
		}
		return material;
	}

	public static User buildMaterial(Cursor cursor) {
		User info = new User();

		info.setUserId(cursor.getString(cursor
				.getColumnIndex(TableUser.COLUMN_NAME_USERID)));
		info.setTelephone(cursor.getString(cursor
				.getColumnIndex(TableUser.COLUMN_NAME_LOGIN_TELEPHONE)));
		info.setName(cursor.getString(cursor
				.getColumnIndex(TableUser.COLUMN_NAME_ID)));
		info.setToken(cursor.getString(cursor
				.getColumnIndex(TableUser.COLUMN_NAME_TOKEN)));
		info.setBooleanOpen(cursor.getString(cursor.
				getColumnIndex(TableUser.COLUMN_NAME_BOOLEANOPEN)));
		
		return info;
	}
	
	  /**
     * 插入一条演示方案
     * 
     * @param info
     * @return
     */
    public static int updateScheme(User info)
    {   
    	getUser(info.getUserId()); 
        ContentValues values = buildContentValues(info);
        int ret = update(MedicineURIField.DOWNLOAD_TASK_URI, values, TableUser.COLUMN_NAME_USERID + " = " + info.getUserId(), null);
        LogUtils.burtLog("ret==="+ret+"===info.getUserId()==="+info.getUserId());
        if (ret == 0)
        { 
            return -1;
        }
        return 0;
    }
    public static int update(Uri uri, ContentValues values, String where,
            String[] selectionArgs)
    {
        return MedicineApplication
				.getApplication()
				.getContentResolver().update(uri, values, where, selectionArgs);
    }
    public static ContentValues buildContentValues(User info)
    {
        ContentValues values = new ContentValues();   
        values.put(TableUser.COLUMN_NAME_USERID, "123");  
        return values;
        }
}
