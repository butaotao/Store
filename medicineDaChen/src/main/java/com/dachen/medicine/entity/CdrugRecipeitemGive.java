package com.dachen.medicine.entity;

import java.io.Serializable;

import com.dachen.medicine.entity.MedicineEntity.MedicineInfo.Goods;

public class CdrugRecipeitemGive extends Result implements Serializable{
	public CdrugRecipeitemGiveInfo data;
	public class CdrugRecipeitemGiveInfo {
		
	//本店是否参与兑换药
    public boolean is_join;
    //本店是否有药
    public boolean is_own;
        //购药所需最低积分
	public int zyzdsxjfs;

    //每单位所需积分
	public int zsmdwypxhjfs;
    public String patient;
    public String patient$user_name;
        public String sex;
  //剩余积分
    public int num_syjf;
    public GoodsType goods$type;

    public class GoodsType{
    	public String title;
    	public String value;
    }
    public String goods$company;  
    public String goods$specification; 
    public String goods$pack_specification;
    public String goods$general_name;
    public String goods$manufacturer;
    public String goods$trade_name;
    public GoodsUnit goods$unit;
    public class GoodsUnit{
    	public String name;
    } 
    public String goods$form;
    GoodsBizType goods$biz_type;
    public class GoodsBizType{
    public String name;
    public String title;
    } 
    public String goods$number;
    public String goods$abbr;
    public String goods$image;
    public Goods goods;
	@Override
	public String toString() {
		return "CdrugRecipeitemGive [is_join=" + is_join + ", is_own=" + is_own
				+ ", zyzdsxjfs=" + zyzdsxjfs + ", zsmdwypxhjfs=" + zsmdwypxhjfs
				+ ", patient=" + patient + ", patient$user_name="
				+ patient$user_name + ", num_syjf=" + num_syjf
				+ ", goods$type=" + goods$type + ", goods$company="
				+ goods$company + ", goods$specification="
				+ goods$specification + ", goods$pack_specification="
				+ goods$pack_specification + ", goods$general_name="
				+ goods$general_name + ", goods$manufacturer="
				+ goods$manufacturer + ", goods$trade_name=" + goods$trade_name
				+ ", goods$unit=" + goods$unit + ", goods$form=" + goods$form
				+ ", goods$biz_type=" + goods$biz_type + ", goods$number="
				+ goods$number + ", goods$abbr=" + goods$abbr
				+ ", goods$image=" + goods$image + ", goods=" + goods + "]";
	} 
	}
}
