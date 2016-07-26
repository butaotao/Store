package com.dachen.medicine.entity;

public class Patients {
	String name;

	@Override
	public String toString() {
		return "Patients [name=" + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
