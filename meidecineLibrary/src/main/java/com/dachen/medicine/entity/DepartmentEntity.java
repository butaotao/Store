package com.dachen.medicine.entity;

public class DepartmentEntity {
	String name;
	String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DepartmentEntity [name=" + name + ", id=" + id + "]";
	}
 
 
}
