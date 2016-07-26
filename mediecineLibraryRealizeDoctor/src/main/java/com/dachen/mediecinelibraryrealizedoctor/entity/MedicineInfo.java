package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity.Goods;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineInfo implements Serializable{

	/**
	 * 
	 */
	public boolean check;
	private static final long serialVersionUID = -6825800355703857404L;
	//药品系列id
	public String id;
	public String goodId;
	public String title;
	public String image_url;
	public String search;
	//药品类型（例如c_GoodsIndication）
	public String _type;
	public String goods$image;
	//制造商（例如 ：桂龙药业(安徽)有限公司）

	public String goods$manufacturer;
	//药品规范（例如：每袋5g）
	public String goods$specification;
	public GoodType goods$type;
	public Goods goods;
	//药品商品名(例如高钙片)
	public String goods$trade_name;
	//图片链接

	public String goods$image_url;
	//批准文号（例如：  国药准字Z20053117）
	public String goods$number;
	//通用名（例如盖中盖）
	public String goods$general_name;
	//购物车的药品数量
	public int num;
	//药品包装规范（例如每盒6袋）
	public String goods$pack_specification; 
	public String trade_name;
	//最小单位
	public String packUnitText;
	//药品使用方法
	public List<GoodsUsagesGoods> goods_usages_goods;
	public GoodForm goods$form;
	public class GoodForm  implements Serializable{
		public String name;
	}
	public GoodBizType goods$biz_type; 
	public PreparedMedie.Form form;
	public boolean is_group_goods;
	public boolean is_doctor_cb ;
	public String id_doctor_cb;
	public class GoodBizType implements Serializable{
		 //类别名字（例如 "中成药"）
		public String name;
		//类别（例如c_BizType）
		public String _type;
		//
		public int id;
		 //返回的和name相同类别名字（例如 "中成药"）
		public String title;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long time;
	public class GoodType implements Serializable{ 
	    //类型（例如非处方乙类药）
		public String value;
		//类型值（c_DrugOctB）
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
	}
	
	
	 
	@Override
	public String toString() {
		return "MedicineInfo [id=" + id + ", search=" + search + ", _type="
				+ _type + ", goods$image=" + goods$image
				+ ", goods$manufacturer=" + goods$manufacturer
				+ ", goods$specification=" + goods$specification
				+ ", goods$type=" + goods$type + ", goods=" + goods
				+ ", goods$trade_name=" + goods$trade_name
				+ ", goods$image_url=" + goods$image_url + ", goods$number="
				+ goods$number + ", goods$general_name=" + goods$general_name
				+ ", num=" + num + ", goods$pack_specification="
				+ goods$pack_specification + ", trade_name=" + trade_name
				+ ", goods_usages_goods=" + goods_usages_goods
				+ ", goods$biz_type=" + goods$biz_type + ", form=" + form
				+ ", is_group_goods=" + is_group_goods + ", is_doctor_cb="
				+ is_doctor_cb + ", time=" + time + "]";
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
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		MedicineInfo c = (MedicineInfo) o;
		if (null!=this.goods&&!TextUtils.isEmpty(this.goods.id)
				&&null!=c.goods&&!TextUtils.isEmpty(c.goods.id)) {
			if(this.goods.id.equals(c.goods.id)){
				//LogUtils.burtLog("======equals===========");
				   return true;
				  }
		}else {
			//LogUtils.burtLog("======notequals===========");
		}
		  
		  return false;
	}
}