package com.dachen.mediecinelibraryrealize.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;

public class PointExplain extends BaseActivity implements OnClickListener{
	TextView tv_title;
	RelativeLayout rl_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pointexplain);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_title.setText("积分说明");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		}
	}
}
