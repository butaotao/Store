package com.dachen.mediecinelibraryrealize.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdviceAdapter;
import com.dachen.mediecinelibraryrealize.entity.MedicineInfosList;
import com.dachen.mediecinelibraryrealize.entity.SomeBox;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest;
import com.dachen.mediecinelibraryrealize.entity.SomeBox2;
import com.dachen.mediecinelibraryrealize.utils.StringUtils;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.google.gson.Gson;

public class AdviceActivity extends BaseActivity  implements OnClickListener, OnHttpListener{
	ArrayList<patientSuggest> c_patient_drug_suggest_list ;
	AdviceAdapter adapter;
	ListView listview;
	RelativeLayout rl_back;
	TextView tv_title;
	Button open;
	boolean flagOpen;
	View view;
	ViewStub stub;
	String patientid;
	View ll_alert;
	public static final String ADVICEMEDIEKEY = "advicemedikey";
	String list;
	TextView tv_footdes;
	String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice); 
		final String id = getIntent().getStringExtra("recipe_id");
		 name = getIntent().getStringExtra("name");
		 patientid = getIntent().getStringExtra("patient");
		c_patient_drug_suggest_list = new ArrayList<patientSuggest>();
		listview = (ListView) findViewById(R.id.listview);
		View viewfoot = View.inflate(this,R.layout.layout_advice,null);
		viewfoot.setVisibility(View.GONE);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this); 
		view= View.inflate(this, R.layout.layout_mybox_title,null);
		tv_footdes = (TextView) viewfoot.findViewById(R.id.tv_footdes);
		//View viewfoot= View.inflate(this, R.layout.layout_blank3,null);
		//viewfoot.findViewById(R.id.view_line).setVisibility(View.GONE);
		listview.addHeaderView(view);
		//listview.addFooterView(viewfoot);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(name + "用药建议");
		view.findViewById(R.id.btn_recomend).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				Intent intent = new Intent(AdviceActivity.this,BaiduMapActivity.class);
				intent.putExtra("code", id);
				startActivity(intent); 
			}
		});
		open = (Button) view.findViewById(R.id.btn_open);
		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (flagOpen) {
					flagOpen = false;
					for (int i = 0; i < c_patient_drug_suggest_list.size(); i++) {
						patientSuggest s = c_patient_drug_suggest_list.get(i);
						s.flagOpen = false;
						c_patient_drug_suggest_list.set(i, s);
					}
					open.setText("全部展开");
				} else {
					for (int i = 0; i < c_patient_drug_suggest_list.size(); i++) {
						patientSuggest s = c_patient_drug_suggest_list.get(i);
						s.flagOpen = true;
						c_patient_drug_suggest_list.set(i, s);
					}
					open.setText("全部收起");
					flagOpen = true;
				}

				// TODO Auto-generated method stub

				adapter.notifyDataSetChanged();
			}
		});

		//http://192.168.3.7:9002/web/api/invoke/8805a5c859fe4800a94475c8c484da72/
		//c_Goods.select?trade_name=%E6%84%9F%E5%86%92%E7%81%B5
		/*HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("trade_name", value)*/

		list = getIntent().getStringExtra("from");
		/*if (null!=list){
			c_patient_drug_suggest_list = getPatientStore(patientid);
		}else {*/
			showLoadingDialog();//recipe/getRecipeById
			/*new HttpManager().get(this,
					Params.getInterface("invoke", "c_Recipe.get_patient_recipe_item?"),
					SomeBox.class,
					Params.getMedicineList(id, ""),
					this, false, 2);*/
		 new HttpManager().get(this,
					"org/recipe/getRecipeById",
				 SomeBox2.class,
					Params.getMedicineDetal(id, this),
					this, false, 1);
		//}
			if (null!=list){
				String s = "药品的使用:提醒已进入您的药箱，您可以通过”我-我的药箱“进行相关设置";
				SpannableString styledText = StringUtils.getStringDifSize(s,this);
				tv_footdes.setText(styledText, TextView.BufferType.NORMAL);

				//ll_alert.setVisibility(View.VISIBLE);
				listview.addFooterView(viewfoot);
				viewfoot.setVisibility(View.VISIBLE);
			}else {
				//ll_alert.setVisibility(View.GONE);
			}
		adapter = new AdviceAdapter(this,c_patient_drug_suggest_list,list);
		listview.setAdapter(adapter);
	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// ToastUtils.showToast("size1111 == "+ c_patient_drug_suggest_list.size());
		// TODO Auto-generated method stub
		if(response.resultCode==1){
			SomeBox2 box = (SomeBox2)response;
			SomeBox box1 = new SomeBox();
			c_patient_drug_suggest_list.clear();
			if (null!=box.data){
					tv_title.setText(box.data.patientName + "用药建议");
			if (null!=box.data.recipeDetailList&&box.data.recipeDetailList.size()>0){
				for (int i=0;i<box.data.recipeDetailList.size();i++){
					SomeBox2.patientSuggest.Uses uses=box.data.recipeDetailList.get(i);
					SomeBox.patientSuggest p = box1.new patientSuggest();
					SomeBox.patientSuggest.Data data = p.new Data();
					SomeBox.patientSuggest.Data1 data1 = p.new Data1();
					p.title = uses.title;
					p.general_name = uses.goodsGenralName;
					p.specification = uses.goodsSpecification;
					p.pack_specification = uses.goodsPackSpecification;
					p.unitname = uses.goodsPackUnit;
					SomeBox.patientSuggest.Unit unit = p.new Unit();
					SomeBox.patientSuggest.Drug  drug = p.new Drug();
					drug.title = uses.goodsTitle;
					p.drug = drug;
					unit.name = uses.goodsPackUnit;
					unit.title = uses.goodsPackUnit;
					p.title = uses.goodsTitle;

					p.unit = unit;
					p.manufacturer = uses.goodsManufacturer;
					p.requires_quantity = uses.goodsNumber;

					p.days = uses.doseDays;
					p.type = uses.type;

					SomeBox.patientSuggest.Uses u = p.new Uses();
					SomeBox.patientSuggest.Uses.Period period = u.new Period();
					u.method = uses.doseMothod;
					String doseUnit = "";
					if (!TextUtils.isEmpty(uses.doseUnitName)){
						doseUnit = uses.doseUnitName;
					}
					u.doseUnitName = uses.doseUnitName;
					u.quantity = uses.doseQuantity;
					u.times =  uses.periodTimes;

					period.number = uses.periodNum;
					period.text = uses.periodNum+uses.periodUnit;
					period.unit = uses.periodUnit;
					data1.num_syjf = uses.leftPointsNum;
					data.zsmdwypxhjfs =uses.lowPointsNum;
					data.zyzdsxjfs = uses.consumePointsNum;
					p.data1 = data1;
					p.data =data;
					p.data1 = data1;
					u.period = period;

					p.drugid =uses.goodsId;
							p.c_drug_usage_list = new ArrayList<>();
					p.c_drug_usage_list.add(u);
					c_patient_drug_suggest_list.add(p);
				}
				if(c_patient_drug_suggest_list.size()<1){
					view.setVisibility(View.GONE);
					return;
				}else {
					view.setVisibility(View.VISIBLE);
				}
			}

			}
			 adapter = new AdviceAdapter(this,c_patient_drug_suggest_list,list);
			 listview.setAdapter(adapter);
		}else {
			ToastUtils.showResultToast(this, response);
		}
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		}
	}

}
