package com.dachen.medicine.common.utils;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class IllEntity extends Result implements Serializable{
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DatabaseField(generatedId = true)
	public int _id;

	@DatabaseField
	String id;
	boolean leaf;
	public String getName() {
		return title;
	}

	public String parentname;
	public void setName(String name) {
		this.title = name;
	}
	@Expose
	@SerializedName("name")
	@DatabaseField
	public String title;
	public ArrayList<IllEntity> getChildren() {
		return children;
	}
	public String destitle;
	public void setChildren(ArrayList<IllEntity> children) {
		this.children = children;
	}

	@DatabaseField(dataType= DataType.SERIALIZABLE)
	ArrayList<IllEntity> children;
	@Expose
	@SerializedName("parentId")
	@DatabaseField
	String parent;
	@DatabaseField
	long createTime;
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "IllEntity [id=" + id + ", name=" + title + ", children="
				+ children + ", parent=" + parent + "]";
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
