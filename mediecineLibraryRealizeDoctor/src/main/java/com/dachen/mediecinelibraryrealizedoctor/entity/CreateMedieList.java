package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class CreateMedieList {
/*	{        
	    doctor :        //医生id   
	    patient:        //患者id:  
	    user_id:        //患者用户id
	    group  :        //医生集团Id
	                               
	    c_recipe_drug_infos_list:          //药品明细
	    [
	      {
	        drug:        //药品id
	        period :     //用药周期[格式：数字+空格+单位（单位必须是秒、分、时、日、周、月、年,且必须对应的英文，且开头字母大写,例如Second,Minute,Day,Week,Month Year)]
	        times:        //用药次数
	        quantity:     //每次用量        
	        patients:     //患者所属人群
	        method:      //使用方法
	        unit:       //单位 -- 可以不传 。数据如：瓶、箱
	        requires_quantity://要求数量
	        }...,
	     
	    ]   */
	//public String doctor;
	//患者id:  
//    user_id:        //患者用户id


	//public String group; 
	public List<Medies> json;
	public class Medies{
		public String goodsId;//药品id
		public int goodsNumber;//品种数量
		public String patients;//适用人群
		public String periodUnit;//用药周期 单位（例如 天，周，月）
		public String periodNum;//用药周期 长度
		public String periodTimes;//用药周期 服药次数
		public String doseQuantity;//每次服药 数量
		public String doseUnit;//每次服药 单位（例如 粒，克，瓶）
		public String doseMothod;//服药方法（例如 口服）
		public String doseDays;//服药持续天数


	}
	public class Period implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = -6878443302593602882L;
		public String number;
		public String text;
		public String unit;
		@Override
		public String toString() {
			return ":{number:" + number + ", text:" + text + ", unit:"
					+ unit + "}";
		}
		public Object deepCopy() throws Exception
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(bos);

			oos.writeObject(this);


			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

			ObjectInputStream ois = new ObjectInputStream(bis);

			return ois.readObject();
		}
	}
}
