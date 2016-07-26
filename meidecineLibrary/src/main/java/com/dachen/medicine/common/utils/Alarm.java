package com.dachen.medicine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * 闹钟实体类，通过外键关联DrugRemind
 * 
 * @author gaozhuo
 * @date 2015年12月8日
 */
public class Alarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8402143563322114376L;

	@DatabaseField(generatedId = true)
	public int _id;

	@DatabaseField
	public int index;// 界面时间选择器的位置

	@DatabaseField
	public int number;// 时间选择器编号（0,1,2,3 对应界面4个时间选择器）

	@DatabaseField
	public int hour;

	@DatabaseField
	public int minute;

	@DatabaseField
	public long time;// 提醒时间
	//-1吃了，大于零跳过，等于零没吃没跳
	@DatabaseField
	public long isStep;
	//-1跳过，大于零吃了，等于零没吃
	@DatabaseField
	public long eat;
	
	@DatabaseField
	public int isEnable;
	//提醒次数
	@DatabaseField
	public int times;
	//响铃时间
	@DatabaseField
	public long ringTime;
	public boolean add;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "drug_remind_id")
	public DrugRemind drugRemind;
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
/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
	 if ( o instanceof Alarm){
		 Alarm atherAlarm =(Alarm) o;
		 if (atherAlarm.)
	 }

	}*/

}
