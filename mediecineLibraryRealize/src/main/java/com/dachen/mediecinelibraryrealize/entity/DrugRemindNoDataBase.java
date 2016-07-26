package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;


/**
 * 用药提醒
 * 
 * @author gaozhuo
 * @date 2015年11月25日
 *
 */
public class DrugRemindNoDataBase implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	public int _id;

	
	public String ownerUserId;// 登录者的userId

	 
	public String patientName;// 患者姓名

	 
	public String drugName;// 药品名称

	 
	public boolean isRemind;// 是否提醒

	 
	public int alarm1Position;// 闹钟1
	
	 
	public int hour1;
	
	 
	public int minute1;
	
	 
	public long alarm1Time;
	
	 
	public int alarm1Id;

	 
	public int alarm2Position;// 闹钟2
	
	 
	public int hour2;
	
	 
	public int minute2;
	
	 
	public long alarm2Time;
	
	 
	public int alarm2Id;

	 
	public int alarm3Position;// 闹钟3
	
	 
	public int hour3;
	
	 
	public int minute3;
	
	 
	public long alarm3Time;
	
	 
	public int alarm3Id;

	 
	public int alarm4Position;// 闹钟4
	
	 
	public int hour4;
	
	 
	public int minute4;
	
	 
	public long alarm4Time;
	
	 
	public int alarm4Id;

	 
	public int repeatPeriod;// 重复周期

	 
	public int repeatDay;// 重复天数

	 
	public long createTime;// 创建时间
 
	public String soundName;// 铃声名
 
	public String soundDesc;// 铃声描述
	 
	public int soundIndex;// 铃声位置
	
	public String patientId;
	
}
