package com.dachen.mediecinelibraryrealize.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.dachen.medicine.entity.Result;
import com.dachen.mediecinelibraryrealizedoctor.entity.PreparedMedie;

public class GetMedieById extends Result{
	/*"form": {
		"name": "中片剂",
				"_type": "c_Form",
				"id": 68,
				"title": "中片剂"
	},*/
	public GoodBizType biz_type;
	public String abbr;
	public String number;
	public String image;
	public String pack_specification;
	public String specification;
	public GoodType type;
	public String title;
	public String general_name;
	public PreparedMedie.Form form;
	public class GoodType implements Serializable{
		public String value;
		//非处方药
		public String title;
		public String entity;
		@Override
		public String toString() {
			return "GoodType [value=" + value + ", title=" + title + "]";
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
	}public class GoodBizType implements Serializable{
		//中成药
		public String name;
		public String _type;
		public int id;
		//中成药
		public String title;
	}
}
