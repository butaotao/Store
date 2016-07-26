package com.dachen.medicine.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.dachen.medicine.bean.MedieNum;
public class CdrugRecipeitem extends Result implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 6619112927203648054L; 
	public String id = "";
	public int requires_quantity;
	public String manufacturer;
	public boolean is_own;
	public boolean givePresent;
	public State state;
	public String recipe_id;
	public String pack_specification;
	public String specification;
	//服务器存在这个要的条形码
	public String is_exists;
	public String general_name;
	public int bought_quantity;
	public Unit unit;
	public int type;
	public String trade_name;
	//0少，1多，2平 ,3缺货,4顾客减量，5无药检码,6顾客加量,7，药单没有的，用户自己购买的药品
	public int isAdd;
	public boolean notExitInlist;
	public int numResone = -11111;
	public String scanCode; 
	public boolean isOpen;
	//是否参加赠药
	public boolean is_join;
	//终端用户的选择， 默认是-2,当多时候是7，少的时候（无药检码1，顾客减量2，缺货3）
	public String isSlected = "-2";
	public ArrayList<MedieNum> lists;
	public int scaningNum;
	public boolean foundCode;
	public long createtime;
	public Data data;
	public String lastDrugStoreName;
	public DataPatient data1;
	public CdrugRecipeitem() {
		// TODO Auto-generated constructor stub
		unit = new Unit();
		data = new Data();
		data1 = new DataPatient();
	} /**
	 *   "bptydxsdphdjfs": 0,
    "qtqdxsdphdjfs": 0,
    "zsmdwypxhjfs": 0,
    "zyzdsxjfs": 0
 */
/*// "drug_store": {
    "name": "白石洲药店",
    "_type": "c_MonomerDrugStore",
    "id": "d62a1a9bc43f4f86ad4f668219f7acc9",
    "title": "白石洲药店"
  },*/
	public class DrugStore implements Serializable,Cloneable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 250570593376488189L;
		public String name;
		public String title;
	}
	 /*"data1": {
        "user": "00000000000000000000000000011931",
        "patient": "1228",
        "goods": "e26b99a80ba341f48313bc8a0f353c3a",
        "num_zjf": 0,
        "ybxfjf": 0,
        "num_syjf": 0
      },*/

	
	
	public class Data implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2381930045580643070L;
		public int bptydxsdphdjfs;
		public int qtqdxsdphdjfs;
		//购药所需最低积分
		public int zsmdwypxhjfs;
		//每单位所需积分
		public int zyzdsxjfs;
	}
	public class DataPatient implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -440924863177791750L;
		public int num_zjf;
		//已被消耗积分
		public int ybxfjf;
		//剩余积分
		public int num_syjf;
	}
	public class State implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6682460321818147857L;
		public String value;
		public String title;
		@Override
		public String toString() {
			return "State [value=" + value + ", title=" + title + "]";
		}
		public Object deepCopy() throws Exception  
	    {   
	       ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	   
	       ObjectOutputStream oos = new ObjectOutputStream(bos);  
	  
	       oos.writeObject(this);  
	   
	          // 将流序列化成对象  
	       ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());  
	     
	       ObjectInputStream ois = new ObjectInputStream(bis);  
	    
	           return ois.readObject();  
    } 
	}

	public class Unit implements Serializable{ 
		/**
		 * 
		 */
		private static final long serialVersionUID = -1267008602065256312L;
		public int id;
		public String _type;
		public String title = " ";
		public String name = " ";

		@Override
		public String toString() {
			return "Unit [id=" + id + ", _type=" + _type + ", title="
					+ title + ", name=" + name + "]";
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


	@Override
	public String toString() {
		return "CdrugRecipeitem [id=" + id + ", requires_quantity="
				+ requires_quantity + ", manufacturer=" + manufacturer
				+ ", is_own=" + is_own + ", state=" + state + ", recipe_id="
				+ recipe_id + ", pack_specification=" + pack_specification
				+ ", general_name=" + general_name + ", bought_quantity="
				+ bought_quantity + ", unit=" + unit + ", trade_name="
				+ trade_name + ", isAdd=" + isAdd + ", notExitInlist="
				+ notExitInlist + ", numResone=" + numResone + ", scanCode="
				+ scanCode + ", isOpen=" + isOpen + ", isSlected=" + isSlected
				+ ", lists=" + lists + ", scaningNum=" + scaningNum
				+ ", foundCode=" + foundCode + ", createtime=" + createtime
				+ ", data=" + data + ", data1=" + data1 + "]";
	}


	public Object deepCopy() throws Exception {
	       ByteArrayOutputStream bos = new ByteArrayOutputStream();

			ObjectOutputStream oos = null;

				oos = new ObjectOutputStream(bos);

				oos.writeObject(this);
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

				ObjectInputStream ois = null;

					ois = new ObjectInputStream(bis);


					return ois.readObject();




		}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		CdrugRecipeitem item= (CdrugRecipeitem)o; 
		if (!TextUtils.isEmpty(item.id)&&!TextUtils.isEmpty(this.id)) {
			if (item.id.equals(this.id)) {
				return true;
			}
		}
		return false;
	}
}