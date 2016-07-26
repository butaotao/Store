package com.dachen.medicine.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MedieDes implements Serializable{
	public String name;
	public String weight;
	public String num;
	public String unit;
	public String companyName;
	public String numResone;
	//0少，1度，2平
	public int isAdd;
	public boolean isOpen;
	public String id;
	public ArrayList<MedieNum> lists;
}
