package com.dachen.medicine.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dachen.medicine.R;

public class NotNetActivity extends BaseActivity implements OnClickListener{
	Button btn_tryagain;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		setContentView(R.layout.activity_nonet);
		//btn_tryagain = (Button) findViewById(R.id.btn_tryagain);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_tryagain:
			
			break;

		default:
			break;
		}
	};
}
