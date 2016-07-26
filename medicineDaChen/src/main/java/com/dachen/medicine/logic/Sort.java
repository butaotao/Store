package com.dachen.medicine.logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dachen.medicine.entity.CdrugRecipeitem;

public class Sort {
		public static void sortMedie(List<CdrugRecipeitem> items){
			 Collections.sort(items, new Comparator<CdrugRecipeitem>() { 
				 
		            @Override 
	
		            public int compare(CdrugRecipeitem user1, CdrugRecipeitem user2) {  
		                long id1 = user1.createtime; 
	
		                long id2 = user2.createtime; 
	 
		                return (int)(id2-id1);
	
		            } 
	
		        }); 
	
		} 
}
