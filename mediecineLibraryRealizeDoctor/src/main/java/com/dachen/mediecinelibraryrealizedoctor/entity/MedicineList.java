package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.mediecinelibraryrealizedoctor.activity.ChoiceMedicineLogic;

public class MedicineList {
	static MedicineList medicineList;
	boolean flag;
	boolean flagread;
	static Lock lock  ;// 锁  
	static ArrayList<MedicineInfo> near ;
	public static ArrayList<MedicineInfo> infoCurrent;
	public static MedicineList getMedicineList(){
		if (null==medicineList) {
			medicineList = new MedicineList();
			lock = new ReentrantLock();
		}
		return medicineList;
	}
	public static List<MedicineInfo> lists_children;
	public static 	ArrayList<MedicineInfo> shoppingcart;
public static List<MedicineInfo> getList(){
	if (null==lists_children) {
		lists_children = new ArrayList<MedicineInfo>();
	}
	return lists_children;
}
public ArrayList<MedicineInfo> getShopingcard(Context context){
	if (null==shoppingcart) {
		shoppingcart = new ArrayList<MedicineInfo>();
	}
/*	
	try {
		shoppingcart = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject
				 (context,"shoppingcart"),true);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}if (null==shoppingcart) {
		shoppingcart = new ArrayList<MedicineInfo>();
		LogUtils.burtLog("null=====shoppingcart");
	}*/
	return shoppingcart;
}
public  void setShoppingcard(Context context,ArrayList<MedicineInfo> shoppingcartss){
	
	shoppingcart = shoppingcartss;
	ArrayList<MedicineInfo> shoppingcarset = new ArrayList<MedicineInfo>();
	if (null!=shoppingcartss) {
		shoppingcarset = shoppingcartss;

	}else {
	}
}

public synchronized ArrayList<MedicineInfo> getCollectiong(Context context){ 
  
	 
	try {
		if (null==near) {
			near = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(context,"near"),false);
		}
		
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (near == null) {
		near = new ArrayList<MedicineInfo>();
	} 
	return near;
}
public  synchronized void setSaveCollectiong(final Context context,final ArrayList<MedicineInfo> selects){

	getCollectiong(context);
	if (near == null) {
		near = new ArrayList<MedicineInfo>();
	}else { 
		near.addAll(selects);
	}

	
}
public   void setSaveCollectiongWhenFinish(final Context context,List<MedicineInfo> infosshopingcard){

	try {
		ArrayList<MedicineInfo> infos  = (ArrayList<MedicineInfo>) infosshopingcard;
		ArrayList<MedicineInfo> infoss = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(context,"near"),false);
		if (null!=infos){
			if (null!=infoss){
				 infos.addAll(infoss);
			}

			ChoiceMedicineLogic.saveObject(context,ChoiceMedicineLogic.serialize(infos), "near");

		}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}

}
}
