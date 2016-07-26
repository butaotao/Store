package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodBizType;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMedieById extends Result{
	public String number;
	//imageUrl
	@Expose
	@SerializedName("imageUrl")
	public String image;
	//packSpecification
	@Expose
	@SerializedName("packSpecification")
	public String pack_specification;
	//specification
	public String specification;
	//generalName
	@Expose
	@SerializedName("generalName")
	public String general_name;
	public String title;
	//manageTypeText
	@Expose
	@SerializedName("productTypeText")
	public String form;
	public String manual;
}
