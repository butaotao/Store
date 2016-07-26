package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.List;

import com.dachen.medicine.entity.Result;

public class ChoiceMedieEntity extends Result implements Serializable{
 
	public List<MedieEntity> set_datas;
	public class MedieEntity  implements Serializable{
		public String id;
		public String name;
		public String quantity;
		public String general_name;
		public String pack_specification;
		public int bought_quantity;
		public String title;
		public boolean select;
		@Override
		public String toString() {
			return "MedieEntity [id=" + id + ", name=" + name + ", quantity="
					+ quantity + ", pack_specification=" + pack_specification
					+ ", bought_quantity=" + bought_quantity + ", select="
					+ select + "]";
		}
		
	}
	@Override
	public String toString() {
		return "ChoiceMedieEntity [set_datas=" + set_datas + "]";
	}
	
}
