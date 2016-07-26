package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;

public class SearchMedicineEntity extends Result implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 852632886012310373L;
	/**
	 * 
	 */ 
	 
	public ArrayList<SearchMedicineInfo> info_list;
 
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
