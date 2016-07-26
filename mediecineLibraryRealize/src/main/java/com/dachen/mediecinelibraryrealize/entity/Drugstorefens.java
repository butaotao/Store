package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;

public class Drugstorefens extends Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8972537944062603391L;
	public double latitude;
	public boolean zcyb;
	public String lxdz;
	public String lxrsj;
	public boolean zcsy;
	public String name;
	public String id ;
	public String num_on_stack;
	public String num_cert;
	public double longitude;
	public String mm_str ;
	public String yysjd ;
	public String fen;
	public String sybz;
	public boolean is_charge ;
	public String address;
	public String companyName;
	public String area;
	public ArrayList<DrugstorefensDes.DrugList> list;
}