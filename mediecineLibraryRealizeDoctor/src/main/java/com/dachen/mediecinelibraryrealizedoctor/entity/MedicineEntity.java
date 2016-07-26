package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineEntity extends Result implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String url;
	public String height;
	public String company;
	public int num;
	public String unit;
	public String total;
	public String sub_title;
	public int page_count;
	public Params params;//list_datas
	//data

	public ArrayList<MedicineInfo> info_list;
	public ArrayList<MedicineInfo> list_datas;
	public Long time;

	public void setTime(Long time) {
		this.time = time;
	}
	public class Goods implements Serializable{
		/**
		 *
		 */
		private static final long serialVersionUID = 6728152169271645951L;
		/**
		 *    "title": "胃药（胃药）",
		 "_type": "c_DrugDP",
		 "id": "e09ef43941454116ba5a95f302d634f8"
		 */
		//药品ID
		public String id;
		public String _type;
		public String title;

		@Override
		public String toString() {
			return "Goods [id=" + id + ", _type=" + _type + ", title="
					+ title + "]";
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



	public class Params implements Serializable{
		/**
		 *
		 */
		private static final long serialVersionUID = 4927066836193592793L;
		public String category;

		@Override
		public String toString() {
			return "Params [category=" + category + "]";
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

	public Long getTime() {
		// TODO Auto-generated method stub
		return time;

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
	public String toString() {
		return "MedicineEntity [name=" + name + ", url=" + url + ", height="
				+ height + ", company=" + company + ", num=" + num + ", unit="
				+ unit + ", total=" + total + ", sub_title=" + sub_title
				+ ", page_count=" + page_count + ", params=" + params
				+ ", info_list=" + info_list + ", time=" + time + "]";
	}

}
