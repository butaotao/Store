package com.dachen.medicine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * 用药提醒
 * 
 * @author gaozhuo
 * @date 2015年11月25日
 *
 */
public class DrugRemind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	public int _id;

	@DatabaseField
	public String ownerUserId;// 登录者的userId
	
	@DatabaseField
	public String patientId;// 登录用户下面所有的患者
	
	@DatabaseField
	public String id;// 闹钟id
	
	@DatabaseField
	public String idmedie;// 药品id

	@DatabaseField
	public String patientName;// 患者姓名

	@DatabaseField
	public String drugName;// 药品名称

	@DatabaseField
	public boolean isRemind;// 是否提醒

	@DatabaseField
	public int repeatPeriodIndex;// 提醒间隔天数

	@DatabaseField
	public int repeatDayIndex;// 提醒持续时间

	@DatabaseField
	public String soundName;// 铃声名

	@DatabaseField
	public String soundDesc;// 铃声描述
	
	@DatabaseField
	public String goods_mcfy;// 已经吃过的药量
	
	@DatabaseField
	public String goods_dqsy;// 剩余药量

	@DatabaseField
	public int soundIndex;// 铃声位置
	
	@DatabaseField
	public String begindata;// 开始服用时间
	@DatabaseField
	public long createTime;// 创建时间 
	 
 

	@ForeignCollectionField(eager = true)
	public Collection<Alarm> alarms;// 一条用药提醒的所有闹钟集合

	public DrugRemind() {
		// TODO Auto-generated constructor stub
		_id = -1;
//		ownerUserId = UserSp.getInstance(DApplication.getInstance()).getAccessToken("");
		isRemind = true;
		soundIndex = 2;
		repeatPeriodIndex = 1;
		repeatDayIndex = 3;
		createTime = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "DrugRemind [_id=" + _id + ", ownerUserId=" + ownerUserId + ", patientName=" + patientName
				+ ", drugName=" + drugName + ", isRemind=" + isRemind + ", repeatPeriodIndex=" + repeatPeriodIndex
				+ ", repeatDayIndex=" + repeatDayIndex + ", soundName=" + soundName + ", soundDesc=" + soundDesc
				+ ", soundIndex=" + soundIndex + ", alarms=" + alarms + "]";
	}
	public Object deepCopy() throws Exception
	{
		// 将该对象序列化成�?因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷�?
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(this);

		// 将流序列化成对象
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

		ObjectInputStream ois = new ObjectInputStream(bis);

		return ois.readObject();
	}
}
