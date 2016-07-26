package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.MyMedicineBoxAdapter;
import com.dachen.medicine.entity.Patients;

public class MyMedicineBox extends BaseActivity {
	ListView listview_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymedicinebox);
		listview_name = (ListView) findViewById(R.id.listview_name);
		getData();
	}

	public void getData() {
		List<Patients> lists = new ArrayList<Patients>();
		for (int i = 0; i < 10; i++) {
			Patients p = new Patients();
			p.setName(i + "aaa");
			lists.add(p);
		}
		MyMedicineBoxAdapter adapter = new MyMedicineBoxAdapter(this,
				R.layout.adapter_name, lists);
		listview_name.setAdapter(adapter);
		listview_name.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
	}
}
