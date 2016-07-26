package com.dachen.mediecinelibraryrealize.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.entity.PointCanExchanges;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealize.utils.ErCording;
import com.google.zxing.WriterException;

public class ErcordingExChangeActivity extends Activity implements OnClickListener{


	ImageView iv_ercording;
	TextView tv_title;
	RelativeLayout rl_back;
	Patients.patient p;
	PointCanExchanges.PointCanExchange medie;
	ImageView img_listview;
	TextView tv_medicine_name;
	TextView tv_medicine_height;
	TextView tv_company;
	TextView tv_units;
	TextView tv_num;
	TextView tv_mediecinestorename;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ercord_exchange);
		Bundle bundle1 = getIntent().getBundleExtra("patient");
		if (bundle1!=null){
			 p = (Patients.patient) bundle1.getSerializable("patient");
			medie = (PointCanExchanges.PointCanExchange) bundle1.getSerializable("medie");
		}
		iv_ercording = (ImageView) findViewById(R.id.iv_ercording);
		if (p!=null) {
			if (null != medie.goods && !TextUtils.isEmpty(medie.goods.id)){
				Bitmap map = null;
				try {
					map = ErCording.cretaeBitmap("dch" + medie.goods.id + "and" + p.id);
				} catch (WriterException e) {
					e.printStackTrace();
				}
				iv_ercording.setImageBitmap(map);
			}
		}


		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("二维码");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		initView();
	}
	public void initView(){
		img_listview = (ImageView) findViewById(R.id.img_listview);
		tv_medicine_name = (TextView) findViewById(R.id.tv_medicine_name);
		tv_medicine_height = (TextView) findViewById(R.id.tv_medicine_height);
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_units = (TextView) findViewById(R.id.tv_units);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_mediecinestorename = (TextView) findViewById(R.id.tv_mediecinestorename);
		if (null!=medie){
			String urls = UserInfo.getInstance(this).getIP().get("url")+"/"+medie.goods$image+"?a="+UserInfo.getInstance(this).getSesstion();
			CustomImagerLoader.getInstance().loadImage(img_listview, ImageUtil.getEncodeUrl(urls));
			//ToastUtils.showToast("urls=="+urls);
			if (null!=medie.goods){
				tv_medicine_name.setText(medie.goods.title+"");
			}
			tv_medicine_height.setText(medie.goods$pack_specification);
			tv_company.setText(medie.goods$manufacturer);
			if (null!=medie.goods$unit){
				tv_units.setText(medie.goods$unit.name+"");
			}

			if(CompareDatalogic.isShow(medie)){
				tv_num.setText(medie.num_syjf/medie.zsmdwypxhjfs+"");
			}tv_mediecinestorename.setText("药店可支持兑换");
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		}
	}

}
