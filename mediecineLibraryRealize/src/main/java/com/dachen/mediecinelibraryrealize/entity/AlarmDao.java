package com.dachen.mediecinelibraryrealize.entity;


import android.content.Context;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author gaozhuo
 * @date 2015年12月8日
 */
public class AlarmDao {

	private static final String TAG = AlarmDao.class.getSimpleName();
	private Dao<Alarm, Integer> mDao;
	private static AlarmDao mInstance;
	private static Context contexts;
	private AlarmDao(Context context) {
		mDao = MedicineApplication.getdb().getInterfacedbAlarm();

	}

	public static AlarmDao getInstance(Context context) {
		if (mInstance == null) {
			contexts= context;
			mInstance = new AlarmDao(context);
		}
		return mInstance;
	}

	/**
	 * 添加
	 *
	 * @param alarm
	 */
	public int create(Alarm alarm) {
		try {
			return mDao.create(alarm);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public List<Alarm> queryByids(String id) {
		QueryBuilder<Alarm, Integer> builder = mDao.queryBuilder();
		try {
			Where<Alarm, Integer> where = builder.where();
			where.eq("id", id);
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<Alarm> getAlarmsByUserId(String userid){
		QueryBuilder<Alarm,Integer>  builder = mDao.queryBuilder();
		try {
			builder.where().eq("hour",12);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Dao<DrugRemind,Integer> drug = MedicineApplication.getdb().getInterfacedbDrugRemind();
		QueryBuilder<DrugRemind,Integer> userBuilder = drug.queryBuilder();

		try {
			userBuilder.where().eq("patientId",userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {//ownerUserId
			return builder.join(userBuilder).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	 public List<Alarm> queryByCreateTime(Alarm alarm) {
		 QueryBuilder<Alarm,Integer>  builder = mDao.queryBuilder();
		 try {
			 builder.where().eq("hour",alarm.hour).and().eq("minute",alarm.minute);
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		 Dao<DrugRemind,Integer> drug = MedicineApplication.getdb().getInterfacedbDrugRemind();
		 QueryBuilder<DrugRemind,Integer> userBuilder = drug.queryBuilder();
		 try {
			 if (null!=alarm.drugRemind&& !TextUtils.isEmpty(alarm.drugRemind.createTime+"")){
				 userBuilder.where().eq("createTime",alarm.drugRemind.createTime);
			 }else {
				 return new ArrayList<>();
			 }
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		 try {
			 return builder.join(userBuilder).query();
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		 return null;
	}
	/**
	 * 添加，如果存在则更新
	 *
	 * @param alarm
	 */
	public CreateOrUpdateStatus createOrUpdate(Alarm alarm) {
		try {
			return mDao.createOrUpdate(alarm);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新记录
	 *
	 * @param alarm
	 */
	public int update(Alarm alarm) {
		try {
			return mDao.update(alarm);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int delete(Collection<Alarm> alarms) {
		try {
			return mDao.delete(alarms);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public List<Alarm> queryAll() {
		try {
			return mDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Alarm queryById(int id) {
		try {
			return mDao.queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<Alarm> getAlarms(int hour,int minute/*,String userid*/){
		QueryBuilder<Alarm,Integer>  builder = mDao.queryBuilder();
		QueryBuilder userBuilder = MedicineApplication.getdb().getInterfacedbDrugRemind().queryBuilder();

		try {//ownerUserId
			return builder/*.join(userBuilder)*/.where().eq("hour",hour).and().eq("minute", minute)/*.and().eq("ownerUserId",userid)*/.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
