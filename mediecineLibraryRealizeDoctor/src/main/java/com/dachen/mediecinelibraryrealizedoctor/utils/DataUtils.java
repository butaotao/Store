package com.dachen.mediecinelibraryrealizedoctor.utils;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.mediecinelibraryrealizedoctor.activity.ChoiceMedicineLogic;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;

import java.util.ArrayList;

public class DataUtils {
	public static void cleanMediInfo(Context context){
		 MedicineList.getMedicineList().setShoppingcard(context,null);
}
	public static ArrayList<MedicineInfo> getList(boolean ispased,Context context,ArrayList<MedicineInfo> list3,
			ArrayList<MedicineInfo> lists_childrens){
		ArrayList<MedicineInfo> record = MedicineList.getMedicineList().getShopingcard(context);
		ArrayList<MedicineInfo> list2 = new ArrayList<>();

		for(MedicineInfo s: lists_childrens){
			MedicineInfo info =s;
			if (null==record||record.size()==0) {
				if (s.goods!=null&&!TextUtils.isEmpty(s.goods.id)) {
					if(!list2.contains(s)) {


						info.num = 0;

						list2.add(info);

					}
				}else if(ispased){
					if(!list2.contains(s)) {


						info.num = 0;

						list2.add(info);

					}
				}
			}else  {
				boolean add = false;
				for (int i = 0; i < record.size(); i++) {
					if (ispased){
						 if (null!=record.get(i)&&
								//对有名字的进行处理
								(s.goods!=null&&record.get(i).goods!=null&&record.get(i).equals(s)
										//对没有名字的药品处理
										||(s.goods==null&&!TextUtils.isEmpty(s.id)
										&&!TextUtils.isEmpty(record.get(i).id)&&
										record.get(i).id.equals(s.id)))){

							 if (!list2.contains(s)) {
								 info = record.get(i);
								 list2.add(info);
								 add = true;
								 break;
							 }

						 }
					}else {
						if (info.equals(record.get(i))){
							if (!list2.contains(s)) {
								info.num = record.get(i).num;
								list2.add(info);
								add = true;
								break;
							}
						}
					}

				}
				if (!add&&!list2.contains(s)) {
					info.num = 0;
					list2.add(info);
				}
			}
		}
		return list2;
	}
}
