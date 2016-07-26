package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.List;

import com.dachen.medicine.entity.Result;

public class Alarms extends Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7657477345319836281L;
	public List<AlarmInfo>  data;

	
}
