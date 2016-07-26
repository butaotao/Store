package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.Serializable;
import java.util.List;

import com.dachen.medicine.entity.Result;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodBizType;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodType;
import com.dachen.mediecinelibraryrealizedoctor.entity.SomeBox.patientSuggest.Unit;



public class MedicienInfoFactory extends Result  implements Serializable{
	// search列表的数据
	public String title;// "马来酸氯苯那敏那敏 ( 氯敏 ) 敏（感冒灵）" //点击药品名出现的列表
	public String manufacturer;// "Teikoku  Seiyaku  Co.,Ltd. Sanbonmatsu Factory"
	public boolean check;
	public String image_url;
	public GoodBizType biz_type; 
	public String trade_name;// "感冒灵" //商品名
	public String id;// ac0db1bc875946268674b59183f31c81
	public String specification;
	public GoodType type;;
	public List<GoodsUsagesGoods> goods_usages;
	public String goods$manufacturer;// 制药厂
	public String goods$pack_specification;// "0.1g*20片"
	public Goods goods;  
	public String pack_specification;
	public Company company;
	public Unit unit;
	public PreparedMedie.Form form;
	public String general_name;
	public String number;
	@Override
	public String toString() {
		return "MedicienInfo [title=" + title + ", manufacturer="
				+ manufacturer + ", trade_name=" + trade_name + ", id=" + id
				+ ", goods$manufacturer=" + goods$manufacturer
				+ ", goods$pack_specification=" + goods$pack_specification
				+ ", goods=" + goods + "]";
	}

	public class Goods {
		public String _type;// c_DrugOctA
		public String id;// 109a00a246314da984496aef2c2c9189
		public String title;// : "健胃消食片（哇哈哈）"
		@Override
		public String toString() {
			return "Goods [_type=" + _type + ", id=" + id + ", title=" + title
					+ "]";
		}
		
	}
	/* "company": {
        "name": "华润三九集团有限公司",
        "_type": "c_DrugFactory",
        "id": "b614dad19dc54511a340febaf3067f41",
        "title": "华润三九集团有限公司"
      },*/

	public class  Company {
        public String name;
        public String _type;
        public String id;
        public String title;
      }


}
