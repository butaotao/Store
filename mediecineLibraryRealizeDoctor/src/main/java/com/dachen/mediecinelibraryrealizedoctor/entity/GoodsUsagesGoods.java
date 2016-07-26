package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
 

public class GoodsUsagesGoods  implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 3195172661007991469L;
	//药品使用人群（例如  "儿童"）
	public String patients;
	//药品使用次数（例如3）
	  public String  times ;

	  //药品使用数量（例如1粒）
	  public String  quantity;
	  //药品使用方法（method": "口服，一次10ml，一日2次，早晚服用。",
	  public String  method ; 
	/*  "period": {
          "number": 1,
          "text": "1 天",
          "unit": "Day"
        }*/
	  public Period period;
		public class Period implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = -6878443302593602882L;
			public int number;
			//periodNum
	        public String text;
			//periodTime
	        public String unit;
			//unit
			public String medieUnit;
			public String dataUnit;
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
	
	@Override
		public String toString() {
			return "GoodsUsagesGoods [patients=" + patients + ", times="
					+ times + ", quantity=" + quantity + ", method="
					+ method + ", period=" + period + "]";
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