package com.dachen.medicine.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.GetMedieInfo;
import com.dachen.medicine.activity.MipcaActivityCapture;
import com.dachen.medicine.activity.MipcaActivityCaptures;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.CustomUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.logic.Sort;
import com.dachen.medicine.view.DialogChoicePatientGallery;

public class QrScanAdapter extends BaseCustomAdapter<CdrugRecipeitem>{
	String isopen;
	List<CdrugRecipeitem> objects;

	Activity context;
	CdrugRecipeitem beans = null;
	public QrScanAdapter(Activity context, int resId,
			List<CdrugRecipeitem> objects) {
		super(context, resId, objects);
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		this.objects = objects;
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder viewholder,
			final int position) {
		// TODO Auto-generated method stub
		final CdrugRecipeitem bean = getItem(position);
		ViewHolder holder = (ViewHolder)viewholder;
		String name = bean.general_name;
		if (isopen.equals("1")) {
			name = bean.general_name;
		}else {
		  name = bean.trade_name;
		  if (name==null||name.equals("")) {
			  name = bean.general_name;
		}
		}
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		}  
		holder.tv_name.setText(name + "");

		try {
			beans = (CdrugRecipeitem) bean.deepCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		final DialogChoicePatientGallery dialog = new DialogChoicePatientGallery(position,context, 1,
				ScaningData.getlistdrug(), bean.scanCode,
				new GetMedieInfo() {
					@Override
					public void getdata(CdrugRecipeitem item) {
						// TODO Auto-generated method stub
						boolean addflag = false;
						if (null != item) {
							beans.manufacturer = item.manufacturer;
							beans.general_name = item.general_name;
							beans.trade_name = item.trade_name;
							//保留原来第一个的扫描码
							beans.scanCode = bean.scanCode;
							if (item.unit != null) {
								beans.unit.name = item.unit.name;
							}
							beans.pack_specification = item.pack_specification;
							beans.scaningNum = bean.scaningNum;
							beans.isAdd = 1;
							bean.createtime = System.currentTimeMillis();
							beans.id = item.id;
							//保留原来的是否找到条形码字段
							beans.foundCode = bean.foundCode;
							beans.requires_quantity = bean.requires_quantity;
							beans.bought_quantity = bean.bought_quantity;
							beans.numResone = bean.numResone;
							CdrugRecipeitem items = null;
							try {
								items = (CdrugRecipeitem) getItem(position).deepCopy();
							} catch (Exception e) {
								e.printStackTrace();
							}

							objects.set(position, beans);
							setData(beans, false, items, (ArrayList<CdrugRecipeitem>) objects);
							addflag = true;

						} else {
							objects.remove(bean);
							setData(beans, true,bean,(ArrayList<CdrugRecipeitem>) objects);
						}
						if (!addflag) {

						}
						ScaningData.setlistScaning_showIntitleBar(objects);
						Sort.sortMedie(objects);
						notifyDataSetChanged();
					}
				},bean.scanCode);
		if (bean.foundCode) {
			holder.btn_selectagain.setVisibility(View.GONE);
		}else {
			holder.btn_selectagain.setVisibility(View.VISIBLE);
			holder.btn_selectagain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (context instanceof MipcaActivityCaptures) {
						List<CdrugRecipeitem> listdrug = ScaningData.getListmedies();
						//if ( listdrug.size() == 0) {
							MipcaActivityCaptures captures = (MipcaActivityCaptures) context;
							HashMap<String, ArrayList<CdrugRecipeitem>> beans = new HashMap<String, ArrayList<CdrugRecipeitem>>();
							captures.produceData(beans);
						//}
					}
					// TODO Auto-generated method stub
					dialog.show();
				}
			});
		}
		 int num = CustomUtils.getTotalData(bean.requires_quantity, bean.bought_quantity, 0);
		 holder.tv_num.setText(bean.scaningNum+"/"+num+bean.unit.name);
	}
	public void setData(CdrugRecipeitem beenselectlast,boolean remove,
						CdrugRecipeitem cancelselect,ArrayList<CdrugRecipeitem> datas){
		List<CdrugRecipeitem> listdrug;
		listdrug = ScaningData.getListmedies();
		HashMap<String,CdrugRecipeitem> map = new HashMap<>();

	 	for (int i=0;i<datas.size();i++){
			if (null!=map.get(datas.get(i).id)){
				CdrugRecipeitem item =datas.get(i);
				ArrayList<MedieNum> lists = new ArrayList<>();
				for (int k=0;k<listdrug.size();k++){
					if (listdrug.get(k).id.equals(item.id)&&item.id.equals(beenselectlast.id)){
						if (listdrug.contains(cancelselect)){
							int potion = listdrug.indexOf(cancelselect);
							if (null!=listdrug.get(potion).lists){
								for (int j=0;j<listdrug.get(potion).lists.size();j++){
									if (!lists.contains(listdrug.get(potion).lists.get(j))){
										lists.add(listdrug.get(potion).lists.get(j));
									}
								}
								break;
							}else {
								break;
							}
						}
					}else if(listdrug.get(k).id.equals(item.id)){
						lists = listdrug.get(k).lists;
						break;
					}
				}
				if (null!=item.lists&&item.lists.size()>0){
					if (lists!=null){
						for (int j=0;j<lists.size();j++){
							if (!item.lists.contains(lists.get(j))) {
								item.lists.add(lists.get(j));
							}
						}
					}
				}else {
					item.lists = new ArrayList<>();
					item.lists.addAll( lists);
				}
				item.numResone = item.lists.size();
				map.put(item.id,item);
			}else {
				//titlebar 上面的数据
				CdrugRecipeitem item =datas.get(i);
				ArrayList<MedieNum> lists = new ArrayList<>();
				for (int k=0;k<listdrug.size();k++){
					if (listdrug.get(k).id.equals(item.id)&&item.id.equals(beenselectlast.id)){
						if (listdrug.contains(cancelselect)){
							int potion = listdrug.indexOf(cancelselect);
							if (null!=listdrug.get(potion).lists){
								for (int j=0;j<listdrug.get(potion).lists.size();j++){
									if (!lists.contains(listdrug.get(potion).lists.get(j))){
										lists.add(listdrug.get(potion).lists.get(j));
									}
								}
								break;
							}else {
								break;
							}
						}else {
							lists = listdrug.get(k).lists;
							break;
						}
					}else if(listdrug.get(k).id.equals(item.id)){
						lists = listdrug.get(k).lists;
						break;
					}
				}

				if (null!=item.lists&&item.lists.size()>0){
					item.lists.clear();
					item.lists.addAll(lists);
				}else {
					item.lists = new ArrayList<>();
					item.lists.addAll(lists);
				}
				item.numResone = lists.size();
				map.put(item.id,item);
			}
		}
		for (int i=0;i<listdrug.size();i++) {
			CdrugRecipeitem item = listdrug.get(i);
			if (map.containsKey(item.id)) {
				if (beenselectlast.id.equals(item.id)) {
					CdrugRecipeitem items = map.get(item.id);
					listdrug.set(i, items);
				} else if (cancelselect.id.equals(listdrug.get(i).id)) {
					item.scaningNum = 0;
					item.numResone = 0;
					item.lists = null;
					item.scanCode = "";
					listdrug.set(i, item);
				}
			} else {
				item.scaningNum = 0;
				item.numResone = 0;
				item.lists = null;
				item.scanCode = "";
				listdrug.set(i, item);
			}
		}

		MipcaActivityCaptures.listScaning.clear();
		MipcaActivityCaptures.listScaning.addAll(listdrug);

	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_num)
		TextView tv_num;
		@Bind(R.id.btn_selectagain)
		Button btn_selectagain;
	}
}
