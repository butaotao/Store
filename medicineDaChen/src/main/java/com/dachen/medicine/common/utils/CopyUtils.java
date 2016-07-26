package com.dachen.medicine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CopyUtils {
	 public Object deepCopy(Object object) throws Exception  
	    {  
	       // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝  
	       ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	   
	       ObjectOutputStream oos = new ObjectOutputStream(bos);  
	  
	       oos.writeObject(object);  
	   
	          // 将流序列化成对象  
	       ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());  
	     
	       ObjectInputStream ois = new ObjectInputStream(bis);  
	    
	           return ois.readObject();  
	     } 
}
