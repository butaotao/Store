package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodBizType;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodType;

public class SearchMedicineInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354718864098594104L;
	public String image_url = "";
	public GoodBizType biz_type;
	public String search; 
	public String abbr;
	public String number;
	public String image;
	public String title;
	public boolean is_group_goods;
	public boolean is_doctor_cb;
	public From form;
	public List<GoodsUsagesGoods> goods_usages; 
	public class Biztype  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2508716870040607157L;
		public String name;
		public String _type;
		public String title;
		public String id;
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
		@Override
		public String toString() {
			return "Biztype [name=" + name + ", _type=" + _type + ", title="
					+ title + ", id=" + id + "]";
		}
		
	}
	public String _type;
	public String specification;
	public GoodType type;
	public class Type  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1227021021794414085L;
		public String type;
		public String entity;
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
	public String manufacturer;
	public String trade_name;
	public Unit unit;
	public From from;
	public class Unit implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8071477609688158851L;
		public String name;
		public String _type;
		public int id;
		public String title;
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
	public class From implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 176965438593143547L;
		public String name;
		public String _type;
		public String id;
		public String title;
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
		@Override
		public String toString() {
			return "From [name=" + name + ", _type=" + _type + ", id=" + id
					+ ", title=" + title + "]";
		}
		
	}
	public boolean in_process;
	public class Company implements Serializable{
		 
		/**
		 * 
		 */
		private static final long serialVersionUID = 6920860544096606958L;
		public String name;
		public String _type;
		public String id;
		public String title;
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
		@Override
		public String toString() {
			return "Company [name=" + name + ", _type=" + _type + ", id=" + id
					+ ", title=" + title + "]";
		}
		
	}
	public Company  company = new Company();
	public String pack_specification;
	public String id;
	public String general_name; 
	public boolean is_closed;
	public String id_doctor_cb;
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
	@Override
	public String toString() {
		return "SearchMedicineInfo [image_url=" + image_url + ", biz_type="
				+ biz_type + ", search=" + search + ", goods_usages="
				+ goods_usages + ", _type=" + _type + ", specification="
				+ specification + ", manufacturer=" + manufacturer
				+ ", trade_name=" + trade_name + ", unit=" + unit + ", from="
				+ from + ", in_process=" + in_process + ", company=" + company
				+ ", pack_specification=" + pack_specification + ", id=" + id
				+ ", general_name=" + general_name + ", abbr=" + abbr
				+ ", is_closed=" + is_closed + "]";
	} 
	
}