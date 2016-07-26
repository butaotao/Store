package com.dachen.medicine.entity;

import com.dachen.medicine.volley.custom.ObjectResult;

public class IllEntity extends ObjectResult {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IllEntity[] getChildren() {
		return children;
	}

	public void setChildren(IllEntity[] children) {
		this.children = children;
	}

	String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String name;
	IllEntity children[];
	String parent;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
}
