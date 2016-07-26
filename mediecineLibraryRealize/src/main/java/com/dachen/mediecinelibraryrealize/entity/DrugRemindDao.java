package com.dachen.mediecinelibraryrealize.entity; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * 
 * @author gaozhuo
 * @date 2015年8月14日
 *
 */
public class DrugRemindDao {

	private static final String TAG = DrugRemindDao.class.getSimpleName();
	private Dao<DrugRemind, Integer> mDao;
	public static DrugRemindDao mInstance;

	public DrugRemindDao(Context context) {
			mDao =  MedicineApplication.getdb().getInterfacedbDrugRemind();
		 
	}

	public static DrugRemindDao getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DrugRemindDao(context);
		}
		return mInstance;
	}

	/**
	 * 添加
	 * 
	 * @param drugRemind
	 */
	public int add(DrugRemind drugRemind) {
		try {
			return mDao.create(drugRemind);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 添加，如果存在则更新
	 * 
	 * @param drugRemind
	 */
	public CreateOrUpdateStatus addOrUpdate(DrugRemind drugRemind) {
		try {
			return mDao.createOrUpdate(drugRemind);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断消息是否存在于数据库中
	 * 
	 * @param msgId
	 * @return
	 */
	public boolean isExist(String msgId) {
		try {
			List<DrugRemind> list = mDao.queryForEq("msgId", msgId);
			if (list != null && list.size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新记录
	 * 
	 * @param drugRemind
	 */
	public int update(DrugRemind drugRemind) {
		try {
			return mDao.update(drugRemind);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public List<DrugRemind> queryAll(String userid) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("ownerUserId", userid);
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	public List<DrugRemind> queryByPatientId(String userid) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("patientId", userid);
			List<DrugRemind> list = builder.query();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	public List<DrugRemind> queryByTime(String time) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("createTime", time);
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<DrugRemind> queryByTimeAndId(String time) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("createTime", time);
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<DrugRemind> queryByids(String id,String patientid) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("id", id).and().eq("patientId",patientid);
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<DrugRemind> queryByidsAndTime(String id) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("id", id) ;
			return builder.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public DrugRemind queryById(int id) {
		QueryBuilder<DrugRemind, Integer> builder = mDao.queryBuilder();
		try {
			return mDao.queryForId(id);
			// Where<DrugRemind, Integer> where = builder.where();
			// where.eq("ownerUserId",
			// UserSp.getInstance(DApplication.getInstance()).getAccessToken("")).eq("_id",
			// id);
			// return builder.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 清空聊天消息
	 * 
	 * @param groupId
	 * @return
	 */
	public int clearMessage(String groupId) {
		try {

			DeleteBuilder<DrugRemind, Integer> builder = mDao.deleteBuilder();
			Where<DrugRemind, Integer> where = builder.where();
			where.eq("gid", groupId);
			return builder.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public void clearAll( String userid) {
		List<DrugRemind> lists = queryByPatientId(userid) ;
		try {
				 mDao.delete(lists);

	 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void clearByID( String id,String patientid) {
		List<DrugRemind> lists = queryByids(id,patientid) ;
		try {
				 mDao.delete(lists);
			
	 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
