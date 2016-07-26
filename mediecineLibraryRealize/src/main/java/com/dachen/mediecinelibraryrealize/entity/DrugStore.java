package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;
 
public class DrugStore extends Result implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 7817151838006585146L;

	/* "latitude": "22.54737800",
      "zcyb": false,
      "fen": 28,
      "lxdz": "11",
      "lxrsj": "4",
      "zcsy": false,
      "name": "白石洲药店",
      "id": "D62A1A9BC43F4F86AD4F668219F7ACC9",
      "num_on_stack": 3,
      "num_cert": 2,
      "longitude": "113.97358500",
      "mm_str": "千里以外"*/
	
	public ArrayList<Drugstorefens> drugstorefens;
	@Override
	public String toString() {
		return "DrugStore [drugstorefens=" + drugstorefens + "]";
	}
	
}
