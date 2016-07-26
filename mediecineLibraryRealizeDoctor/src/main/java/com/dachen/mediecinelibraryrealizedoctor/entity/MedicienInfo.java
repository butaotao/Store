package com.dachen.mediecinelibraryrealizedoctor.entity;
 

import java.io.Serializable;

public class MedicienInfo extends MedicienInfoBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5045714926894078026L;
	// search列表的数据
	public String title;// "马来酸氯苯那敏那敏 ( 氯敏 ) 敏（感冒灵）" //点击药品名出现的列表
	public String manufacturer;// "Teikoku  Seiyaku  Co.,Ltd. Sanbonmatsu Factory"


	public String trade_name;// "感冒灵" //商品名
	public String id;// ac0db1bc875946268674b59183f31c81
	public String goodsid;
	
	public String goods$manufacturer;// 制药厂
	public String goods$pack_specification;// "0.1g*20片"
	public Goods goods;
	public boolean isSelect; 
	 

	@Override
	public String toString() {
		return "MedicienInfo [title=" + title + ", manufacturer="
				+ manufacturer + ", trade_name=" + trade_name + ", id=" + id
				+ ", goodsid=" + goodsid + ", goods$manufacturer="
				+ goods$manufacturer + ", goods$pack_specification="
				+ goods$pack_specification + ", goods=" + goods + ", isSelect="
				+ isSelect + "]";
	}


	public class Goods implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8826495224421319384L;
		public String _type;// c_DrugOctA
		public String id;// 109a00a246314da984496aef2c2c9189
		public String title;// : "健胃消食片（哇哈哈）"
		@Override
		public String toString() {
			return "Goods [_type=" + _type + ", id=" + id + ", title=" + title
					+ "]";
		}
		
	}

}
