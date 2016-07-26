package com.dachen.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.medicine.R;

public class NotFoundListActivity extends BaseActivity implements OnClickListener{
	Button btn_quitscan;
	ImageView iv_scan_again;
	ImageView iv_back;
	TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notfoundlist);
		btn_quitscan = (Button) findViewById(R.id.btn_quitscan);
		iv_scan_again = (ImageView) findViewById(R.id.iv_scan_again);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_back.setOnClickListener(this);
		btn_quitscan.setOnClickListener(this);
		iv_scan_again.setOnClickListener(this);
		tv_title.setText(getString(R.string.scan_sell));
	}
	@Override
	public void onClick(View v) {
		Intent intent ;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_quitscan:
			intent = new Intent(this,MainActivity.class);	 
			startActivity(intent);
			finish();
			break;
		case R.id.iv_scan_again:
		 intent = new Intent(this,MipcaActivityCaptures.class);
		intent.putExtra("code", "erweima"); 
		startActivity(intent);
		finish();
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}
}
