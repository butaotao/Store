package com.dachen.medicine.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.EditListAdapter;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.SelectInterface;
import com.dachen.medicine.logic.ScaningData;

public class EditListActivity extends BaseActivity implements SelectInterface{
	ListView listview;
	EditListAdapter adapter;
	List<CdrugRecipeitem> listmedies; 
	List<CdrugRecipeitem> listmedies_more_less;
	List<CdrugRecipeitem> listdrug;
	TextView tv_title;
	TextView tv_back;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_editlist); 
		
	}
	@Override
	protected void setUpViews() { 
		super.setUpViews();
		listview = (ListView) findViewById(R.id.listview);
		
		enableBack();
		listmedies =  ScaningData.getListmedies();
		adapter = new EditListAdapter(this,R.layout.adapter_editlist,listmedies,this); 
		listview.setAdapter(adapter);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("编辑清单");
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setVisibility(View.VISIBLE);
	}
	@Override
	public int getSelect(CdrugRecipeitem listmedies_more_less, int isOver) {
		// TODO Auto-generated method stub
		for (int i = 0; i < listmedies.size(); i++) {
			if (listmedies.get(i).id.equals(listmedies_more_less.id)) {
				CdrugRecipeitem item = new CdrugRecipeitem();
				item = listmedies.get(i);
				item.isAdd = listmedies_more_less.isAdd;
				item.lists = listmedies_more_less.lists;
				if (null!=listmedies_more_less.lists){
					item.numResone = listmedies_more_less.lists.size();
					item.scanCode = listmedies_more_less.lists.size()+"";
				}

				listmedies.set(i, item);
				//ScaningData.setListmedies(listmedies);
				break;
			}
		}
		return 0;
	}
	   @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	         back();
	    }
	    @Override
	    public void onClick(View v) {
	        if (v.getId() == R.id.rl_back) {
	            this.onBackPressed(); 
	        }
	    }
	    public void back(){
	    	//

	         Intent intent = new Intent(this,SalesClerkActivity.class);
	         intent.putExtra("result","edt");
	         startActivity(intent);
	    }
}
