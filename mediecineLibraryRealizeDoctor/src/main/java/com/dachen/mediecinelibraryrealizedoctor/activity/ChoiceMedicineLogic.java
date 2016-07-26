package com.dachen.mediecinelibraryrealizedoctor.activity;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.IllEntity;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.SearchMedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.SharePrefrenceConst;

public class ChoiceMedicineLogic   {


	public static  String serialize(ArrayList<IllEntity> person)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(person);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return serStr;
	}
	public static  String serialize(List< MedicineInfo> person)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(person);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return serStr;
	}
	public static  ArrayList< MedicineInfo> deSerialization(String str,boolean add)
			throws IOException, ClassNotFoundException {
		if (TextUtils.isEmpty(str)) {
		 
			return null;
		} 
		String redStr = java.net.URLDecoder.decode(str, "UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				redStr.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		ArrayList< MedicineInfo> person = (ArrayList< MedicineInfo>) objectInputStream
				.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		if (add) {
		
		ArrayList< MedicineInfo> person2  = new ArrayList< MedicineInfo>();
		for (int i = 0; i < person.size(); i++) {
			if (person.get(i).num!=0) {
				person2.add(person.get(i));
			}
		}
		return person2;
		
		}
		return person;
	}

	public static  String serializes(ArrayList<IllEntity> person)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(person);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return serStr;
	}
	public static  String serializes(List< SearchMedicineInfo> person)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(person);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return serStr;
	}
	public static  ArrayList< SearchMedicineInfo> deSerializations(String str)
			throws IOException, ClassNotFoundException {
		if (TextUtils.isEmpty(str)) {
		 
			return null;
		} 
		String redStr = java.net.URLDecoder.decode(str, "UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				redStr.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		ArrayList< SearchMedicineInfo> person = (ArrayList< SearchMedicineInfo>) objectInputStream
				.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		return person;
	}
	public static  ArrayList<IllEntity> deSerializationIll(String str)
			throws IOException, ClassNotFoundException {
		if (TextUtils.isEmpty(str)) {
		 
			return null;
		} 
		String redStr = java.net.URLDecoder.decode(str, "UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				redStr.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		ArrayList<IllEntity>  person = (ArrayList<IllEntity> ) objectInputStream
				.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		return person;
	}
	public static String getObject(Context context,String key) {
		HashMap<String, String> maps = MedicineApplication.getMapConfig();
		String id = maps.get("userid"); 
		if (TextUtils.isEmpty(id)) {
			id="";
		}
		SharedPreferences sp = context.getSharedPreferences(SharePrefrenceConst.MEDICINE_INFO, 0);
		return sp.getString(id+key, null);
	}

	public static void saveObject(Context context,String strObject, String key) {
		HashMap<String, String> maps = MedicineApplication.getMapConfig();
		String id = maps.get("userid"); 
		if (TextUtils.isEmpty(id)) {
			id="";
		}
		SharedPreferences sp = context.getSharedPreferences(SharePrefrenceConst.MEDICINE_INFO, 0);
		Editor edit = sp.edit();
		edit.putString(id+key, strObject);
		edit.commit();
	}  
}
