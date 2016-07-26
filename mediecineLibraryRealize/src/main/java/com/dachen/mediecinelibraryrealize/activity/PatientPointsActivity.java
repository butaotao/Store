package com.dachen.mediecinelibraryrealize.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientPointMain;
import com.dachen.mediecinelibraryrealize.adapter.ImageGalleryAdapter;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints;
import com.dachen.mediecinelibraryrealize.entity.PatientPointsItem;
import com.dachen.mediecinelibraryrealize.entity.PatientPointsItemData;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.entity.PointCanExchanges;
import com.dachen.mediecinelibraryrealize.entity.TotalPoints;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientPointsActivity extends BaseActivity implements OnHttpListener, OnClickListener, AdapterView.OnItemSelectedListener {
	ListView listview;
	AdapterPatientPointMain adaper;
	RelativeLayout rl_back;
	String id = "";
	List<PointCanExchanges.PointCanExchange> patientpoints;
	TextView title;
	TextView tv_checkdetail;
	RelativeLayout rl_plus;
	LinearLayout ll_nullinfodes;
	Gallery myGallery;
	List<Patients.patient> patients;
	Patients.patient P;
	ImageGalleryAdapter adaptergrllery;
	PopupWindow popupWindow;
	int select;
	Patients.patient p;
	TextView tv_save;
	LinearLayout ll_pointhave;
	TextView tv_havepoints;
	TextView tv_net;
	boolean isFirst;
	LinearLayout ll_pointnet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patientpoints);
		listview = (ListView) findViewById(R.id.listview);
		patientpoints = new ArrayList<PointCanExchanges.PointCanExchange>();
		myGallery = (Gallery) findViewById(R.id.myGallery);
		ll_nullinfodes = (LinearLayout) findViewById(R.id.ll_nullinfodes);
		ll_pointhave = (LinearLayout) findViewById(R.id.ll_pointhave);
		tv_havepoints = (TextView) findViewById(R.id.tv_havepoints);
		ll_pointnet = (LinearLayout) findViewById(R.id.ll_pointnet);
	/*	RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_plus_medie, ll_sub);*/
		final Bundle bundle = getIntent().getBundleExtra("patients");
		isFirst = true;
		if (bundle != null) {
			patients = (List<Patients.patient>) bundle.get("patients");

		}
		if (null == patients) {
			patients = new ArrayList<Patients.patient>();
		}
		id = getIntent().getStringExtra("id");
		//id = "11855";
		myGallery = (Gallery) findViewById(R.id.myGallery);
		if (patients.size() > 0) {
			myGallery.setVisibility(View.VISIBLE);
			for (int i = 0; i < patients.size(); i++) {
				if (id != null && patients.get(i).id != null && patients.get(i).id.equals(id)) {
					select = i;
				}
			}
		}
		adaptergrllery = new ImageGalleryAdapter(this, patients);
		myGallery.setAdapter(adaptergrllery);
		myGallery.setOnItemSelectedListener(this);

		adaper = new AdapterPatientPointMain(this, patientpoints);
		listview.setAdapter(adaper);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(PatientPointsActivity.this, ErcordingExChangeActivity.class);
				Bundle bundlePatient = new Bundle();
				PointCanExchanges.PointCanExchange point = (PointCanExchanges.PointCanExchange) adaper.getItem(position);
				bundlePatient.putSerializable("patient", p);
				bundlePatient.putSerializable("medie", point);
				intent.putExtra("patient", bundlePatient);

				String medieid = "";
				if (null != point.goods && TextUtils.isEmpty(point.goods.id)) {
					medieid = point.goods.id;
				}
				intent.putExtra("medicineid", medieid);
				if (CompareDatalogic.isShow(point)) {
					startActivity(intent);
				}

			}
		});
		adaptergrllery.setSelectItem(select);
		myGallery.setSelection(select);
		title = (TextView) findViewById(R.id.tv_title);
		//getPointInfo_page2(id);
		title.setText("我的积分");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_checkdetail = (TextView) findViewById(R.id.tv_checkdetail);
		//	tv_checkdetail.setOnClickListener(this);

		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_modi_time, ll_sub);
		tv_save = (TextView) view.findViewById(R.id.tv_save);
		tv_save.setText("说明");
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
		tv_net = (TextView) findViewById(R.id.tv_net);
		//	tv_save.setVisibility(View.GONE);
		rl_plus.setOnClickListener(this);


		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
							   long arg3) {
		// TODO Auto-generated method stub
		adaptergrllery.setSelectItem(position);  //当滑动时，事件响应，调用适配器中的这个方法。

		select = position;
		if (null != patients && patients.size() > 0) {
			p = patients.get(position);
			id = p.id;
			getAllPointInfo();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != patients && patients.size() > 0&&!isFirst) {
			p = patients.get(select);
			id = p.id;
			if(!TextUtils.isEmpty(id)){
//				getPointInfo_page2(p.id);
			}

		}
		isFirst = false;
		getAllPointInfo();
	}


	private void getAllPointInfo(){
		HashMap<String, String> interfaces = new HashMap<String, String>();
		String access_token = UserInfo.getInstance(PatientPointsActivity.this).getSesstion();
		interfaces.put("access_token", access_token);
		interfaces.put("patient", id);
		new HttpManager().get(this, Constants.GET_USER_POINTS,
				PatientPointsItemData.class,
				interfaces,
				this, false, 1);
	}
	/*	public void getPointInfo(String id){
			HashMap<String, String> interfaces = new HashMap<String, String>();
            interfaces.put("patient", id);
            new HttpManager().get (this,
                    Params.getInterface("invoke", "c_JFHDMX.select"),
                    PointsGet.class,
                    interfaces,
                     this,false, 2);
        }*/
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		if(arg0.getResultCode() == 1) {
			if (arg0 instanceof PatientPointsItemData) {
				PatientPointsItemData patientPointsItem = (PatientPointsItemData) arg0;
				PointCanExchanges ps = new PointCanExchanges();
				List<PointCanExchanges.PointCanExchange> pointCanExchangeList = new ArrayList<>();
				if (patientPointsItem.data != null && patientPointsItem.data.patientList != null &&
						patientPointsItem.data.patientList.size() > 0) {
					for (int i = 0; i < patientPointsItem.data.patientList.size(); i++) {
						PatientPointsItem.Data dataItem = patientPointsItem.data.patientList.get(i);
						PointCanExchanges.PointCanExchange pointCanExchange = new PointCanExchanges.PointCanExchange();
						PointCanExchanges.PointCanExchange.Goods goods = new PointCanExchanges.PointCanExchange.Goods();
						goods.title = dataItem.title;
						goods.id = dataItem.goodsId;
						pointCanExchange.goods = goods;
						pointCanExchange.goods$pack_specification = dataItem.packSpecification;
						pointCanExchange.goods$manufacturer = dataItem.manufacturer;
						PointCanExchanges.PointCanExchange.Unit unit = new PointCanExchanges.PointCanExchange.Unit();
						unit.name = dataItem.packUnitText;
						pointCanExchange.goods$unit = unit;
						pointCanExchange.num_syjf = dataItem.leftPointsNum;
						pointCanExchange.zsmdwypxhjfs = dataItem.consumePointsNum;
						pointCanExchange.zyzdsxjfs = dataItem.consumePointsNum;
						pointCanExchangeList.add(pointCanExchange);
					}
					ps.list_datas = pointCanExchangeList;
					patientpoints.clear();
					patientpoints.addAll(ps.list_datas);
					adaper.notifyDataSetChanged();
					adaptergrllery.setSelectItem(select);
					ll_nullinfodes.setVisibility(View.GONE);
					tv_save.setText("积分明细");
					if (null != patientPointsItem.data) {
						int countPoints = patientPointsItem.data.pointsCount;
						if (countPoints > 0) {
							ll_pointhave.setVisibility(View.VISIBLE);
							tv_havepoints.setText(getTimeSpan(countPoints + ""));
							tv_net.setText("去领");
							tv_net.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
							ll_pointnet.setVisibility(View.VISIBLE);
						} else {
							ll_pointhave.setVisibility(View.GONE);
							ll_pointnet.setVisibility(View.GONE);
						}
					}

					tv_save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PatientPointsActivity.this, PointDetailActivity.class);
							intent.putExtra("id", id);
							startActivity(intent);
						}
					});
					ll_pointhave.setVisibility(View.VISIBLE);


					findViewById(R.id.ll_pointnet).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PatientPointsActivity.this, PointDetailActivity.class);
							intent.putExtra("id", id);
							startActivity(intent);
						}
					});
				} else {
					patientpoints.clear();
					adaper.notifyDataSetChanged();
					adaptergrllery.setSelectItem(select);
					ll_nullinfodes.setVisibility(View.VISIBLE);
					ll_pointhave.setVisibility(View.GONE);
					tv_save.setText("积分明细");
					tv_havepoints.setText("");
					tv_net.setText("");
					tv_save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//PointExplain
							Intent intent = new Intent(PatientPointsActivity.this, PointDetailActivity.class);
							intent.putExtra("id", id);
							startActivity(intent);
							//getPointInfo(id);
						}
					});
				}
			} else if (arg0 instanceof TotalPoints) {
				if (arg0.getResultCode() == 1) {
					TotalPoints totalPoints = (TotalPoints) arg0;
					int countPoints = totalPoints.data;
					if (countPoints > 0) {
						ll_pointhave.setVisibility(View.VISIBLE);
						ll_pointnet.setVisibility(View.VISIBLE);
						tv_havepoints.setText(getTimeSpan(countPoints + ""));
						tv_net.setText("去领");
						tv_net.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
					} else {
						ll_pointhave.setVisibility(View.GONE);
						ll_pointnet.setVisibility(View.GONE);
					}
				} else {
					ll_pointhave.setVisibility(View.GONE);
					ll_pointnet.setVisibility(View.GONE);
				}
			}
		}else {
			ToastUtils.showResultToast(this, arg0);
		}
	}

	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();
		} else if (v.getId() == R.id.tv_checkdetail) {
			Intent intent = new Intent(this, PointDetailActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			//	getPointInfo_page2( id);
		} else if (v.getId() == R.id.rl_plus) {
			//	getPointInfo_page2(id);
			/*Intent intent = new Intent(this,PointExplain.class);
			intent.putExtra("id", id);
			startActivity(intent);
			getPointInfo(id);*/
			//showpopup();
		} else if (v.getId() == R.id.ll_points) {
			Intent intent = new Intent(this, PointDetailActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
	}

	public void getPointInfo_page2(String id) {
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("patient", id);
		new HttpManager().get(this,
				Params.getInterface("invoke", "c_user_JF.get_list_myJF"),
				PointCanExchanges.class,
				interfaces,
				this, false, 2);
	}

	public void getPointInfo(String id) {
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("patient", id);
		new HttpManager().get(this,
				Params.getInterface("invoke", "c_JFXHMX.select"),
				PatientPoints.class,
				interfaces,
				this, false, 2);
	}

	public void showpopup() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popupwindow_point, null);

		RelativeLayout rl_bottom = (RelativeLayout) layout.findViewById(R.id.rl_bottom);
		popupWindow = new PopupWindow(layout);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.getContentView().findViewById(R.id.ll_points).setOnClickListener(this);
		// 控制popupwindow的宽度和高度自适应
/*	rl_bottom.measure(View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED); */
		//   popupWindow.setWidth(rl_bottom.getMeasuredWidth()+30);
		// popupWindow.setHeight((rl_bottom.getMeasuredHeight() + 20));

		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
	 /*       // 控制popupwindow点击屏幕其他地方消失
       popupWindow.setBackgroundDrawable(this.getResources().getDrawable(
	                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
*/
		popupWindow.setOutsideTouchable(true);//
		popupWindow.showAsDropDown(rl_plus, 0, 3);

	}
	private Spanned getTimeSpan( String point) {
		Spanned dTimeStr;
		dTimeStr = Html.fromHtml(
				"<font color=\"#aaaaaa\">" + "您有    " + "</font>"
						+ "<font color=\"#ff9d6a\">" + point + "</font>"
						+ "<font color=\"#aaaaaa\">" + " 积分可领取," + "</font>");
		//+ "<font color=\"#000000\">" + "去领" + "</font>");
		//Html.fromHtml("<u>使用html实现下划线样式</u>"));
		return dTimeStr;
	}
}
