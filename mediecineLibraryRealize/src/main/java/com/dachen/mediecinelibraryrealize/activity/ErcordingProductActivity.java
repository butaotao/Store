package com.dachen.mediecinelibraryrealize.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.utils.ErCording;
import com.google.zxing.WriterException;

public class ErcordingProductActivity extends Activity implements OnClickListener{
	
	    
	ImageView iv_ercording;  
	TextView tv_title;
	RelativeLayout rl_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ercord);
		iv_ercording = (ImageView) findViewById(R.id.iv_ercording); 
		String ercode = getIntent().getStringExtra("ercode");
		//Bitmap map = MedicineApplication.getBitmap("dcb" + ercode);
		Bitmap map =null;
		try {
			map = ErCording.cretaeBitmap("dcb" + ercode);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		if (null!=map){
			iv_ercording.setImageBitmap(map);
		}else {
			iv_ercording.setBackground(getResources().getDrawable(R.drawable.ercode));
		}

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("二维码");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		}
	}
	
}
