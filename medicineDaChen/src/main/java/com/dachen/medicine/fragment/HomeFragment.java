package com.dachen.medicine.fragment;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.SalesRecordAdapter;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.JsonUtils.SaleRecordTranslate;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Info;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.SaleRecord;
import com.dachen.medicine.entity.SaleRecords;
import com.dachen.medicine.entity.SalesRecord;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.MyScrollView;
import com.dachen.medicine.view.XRefreshBothView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeFragment extends BaseFragment implements OnChildClickListener,
		OnClickListener {

	// 总View，总视图
	private View mRootView;
	SalesRecordAdapter recordAdapter;
	List<Info> list;
	ImageView iconleft_circle;
	ImageView iconright_circle;
	int thisYear;
	int created;
	TextView textView2;
	TextView textView1;
	int page = 1;
	private MyScrollView myScrollView;// 自定义的MyScrollView
	private static final int REQUEST_STATE_NORMAL = 1;
	private static final int REQUEST_STATE_REFRESH = 2;
	private static final int XLIST_STATUS_DEFAULT = 0;
	private static final int XLIST_STATUS_LOAD_MORE = 2;
	private int mXListStatus = XLIST_STATUS_DEFAULT;
	private int mCurrentRequestState = REQUEST_STATE_NORMAL;
	private XRefreshBothView mXRefreshView;
	PullToRefreshListView mExpandList;
	boolean getyear;
	RelativeLayout rl_nosell;
	TextView tv_notsell_des;
	ImageView iv_iv;
	public static final String Action = "CHANGMEDIENAME";
	BroadCastReeiver reeiver;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = LayoutInflater.from(mActivity).inflate(
				R.layout.activity_salesrecord, null);
		ButterKnife.bind(mActivity);
		reeiver = new BroadCastReeiver();
		IntentFilter filter = new IntentFilter();//生成一个IntentFilter对象
		           filter.addAction(Action);//为IntentFilter添加一个Action
		mActivity.registerReceiver(reeiver, filter);//将BroadcastReceiver对象注册到系统当中

		return mRootView;
	}
	class BroadCastReeiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		list.clear();
		getInfo(thisYear, 0 + "");
	}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// setUpViews();
		iconleft_circle = (ImageView) mRootView
				.findViewById(R.id.iconleft_circle);
		iconright_circle = (ImageView) mRootView
				.findViewById(R.id.iconright_circle);
		textView2 = (TextView) mRootView.findViewById(R.id.textView2);
		textView1 = (TextView) mRootView.findViewById(R.id.textView1);
		iconleft_circle.setOnClickListener(this);
		iconright_circle.setOnClickListener(this);
		mExpandList = (PullToRefreshListView) mRootView
				.findViewById(R.id.lv_list_records);
		rl_nosell = (RelativeLayout) mRootView.findViewById(R.id.rl_nosell);
		rl_nosell.setVisibility(View.GONE);

		tv_notsell_des = (TextView) mRootView.findViewById(R.id.tv_notsell_des);

		iv_iv = (ImageView) mRootView.findViewById(R.id.iv_iv);
		mExpandList.getRefreshableView().setDivider(null);
		mExpandList.setShowIndicator(false);

		// mExpandList.getRefreshableView().setSelector(android.R.color.transparent);
		mExpandList
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getyear = false;
						list.clear();
						getInfo(created, "0");
						page = 0;
						mExpandList.postDelayed(new Runnable() {

							@Override
							public void run() {
								mExpandList.onRefreshComplete();
							}
						}, 4000);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getyear = false;
						if (page >= 0) {

							getInfo(created, page + "");
							page = page + 1;
							mExpandList.postDelayed(new Runnable() {

								@Override
								public void run() {
									mExpandList.onRefreshComplete();
								}
							}, 4000);
						}

					}
				});

		mExpandList.setMode(Mode.BOTH);
		mExpandList.getLoadingLayoutProxy(false, true).setPullLabel("下拉刷新...");// ");
		mExpandList.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"正在载入...");// 刷新时
		mExpandList.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"放开刷新...");// 下来达到一定距离时，显示的提示
		mExpandList.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
		Calendar calendar = Calendar.getInstance();
		created = calendar.get(Calendar.YEAR);
		thisYear = created;
		list = new ArrayList<Info>();
		getInfo(created,   "0");
		textView2.setText(created + "年销售记录");
		recordAdapter = new SalesRecordAdapter(mActivity,
				R.layout.adapter_sale_records, list);
		iconright_circle
				.setBackgroundResource(R.drawable.iconright_circle_noeffect);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		if (MedicineApplication.flag) {
			getyear = true;
			getInfo(thisYear, 0 + "");
		}
		MedicineApplication.flag = false;
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iconleft_circle:
			if (thisYear < created + 10) {
				created = created - 1;
				getInfo(created, 0 + "");
				textView2.setText(created + "年销售记录");
				getyear = true;
				page = 0;
			}
			if (thisYear == created + 10) {
				iconleft_circle
						.setBackgroundResource(R.drawable.iconleft_circle_noeffect);
				iconright_circle
						.setBackgroundResource(R.drawable.iconright_circle);
			} else {
				iconleft_circle
						.setBackgroundResource(R.drawable.iconleft_circle);
				iconright_circle
						.setBackgroundResource(R.drawable.iconright_circle);
			}
			break;
		case R.id.iconright_circle:
			created = created + 1;
			if (thisYear >= created) {
				getInfo(created, 0 + "");
				getyear = true;
			} else {
				created = created - 1;
			}
			page = 0;
			if (thisYear == created) {
				iconright_circle
						.setBackgroundResource(R.drawable.iconright_circle_noeffect);
				iconleft_circle
						.setBackgroundResource(R.drawable.iconleft_circle);
			} else {
				iconright_circle
						.setBackgroundResource(R.drawable.iconright_circle);
				iconleft_circle
						.setBackgroundResource(R.drawable.iconleft_circle);

			}
			textView2.setText(created + "年销售记录");
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getInfo(int beginTime, String page) {
		String filter1 = +beginTime + "-01-01";
		String filter2 = +(beginTime) + "-12-31";
		String encode1 = URLEncoder.encode(filter1);
		String encode2 = URLEncoder.encode(filter2);
		HashMap<String ,String> maps = new HashMap<>();
		maps.put("access_token", SharedPreferenceUtil.getString("session",""));
		maps.put("year", created + "");
		maps.put("pageIndex",page);
		maps.put("pageSize","10");
		String s = "goods/salesLog/getSalesLogListByPage";
		new HttpManager().get(mActivity, s, SaleRecords.class,
				maps, this,
				false, 3);
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSuccess(Result response) {
		rl_nosell.setVisibility(View.GONE);
		iv_iv.setVisibility(View.GONE);
		// TODO Auto-generated method stub
		mExpandList.onRefreshComplete();
		if (null != response) {
			if (response instanceof SaleRecords) {

				// list.clear();
				SaleRecords record = (SaleRecords) response;
				if (record.data == null||record.data.pageData==null) {
					return;
				}
				ArrayList<Info> pageData = SaleRecordTranslate.getInfoList(record.data.pageData);
				// record.info_list;
				if (getyear == true) {
					list.clear();
				}
				getyear = false;
				int total = 0;
				if (null != pageData && pageData.size() > 0) {

					for (int i = 0; i < pageData.size(); i++) {
						// LogUtils.burtLog("i=="+i);
						Info info = new Info();
						info = pageData.get(i);
						Info info2 = new Info();
						if (i == 0) {
							boolean flag = false;
							if (list.size() != 0) {
								for (int j = 0; j < list.size(); j++) {
									if (parseDate(info.datetime)[1]
											.equals(parseDate(list.get(j).datetime)[1])) {
										flag = true;
										break;
									}
								}
							}
							if (!flag) {
								info2.setMon(true);
								info2.datetime = info.datetime;
								info2.calendar = parseDate(info.datetime);
								list.add(info2);
							}
							// LogUtils.burtLog("info=="+info);
						} else {
							Info info3 = pageData.get(i - 1);
							// list.get(i).datetime;
							if (!info.saleMonth.equals(
									info3.saleMonth)) {
								info2.setMon(true);
								info2.datetime = info.datetime;
								info2.calendar = parseDate(info.datetime);
								list.add(info2);
								// LogUtils.burtLog("info=="+info);
							}

						}
						parseDate(info.datetime);
						info.setMon(false);
						info.calendar = parseDate(info.datetime);
						list.add(info);
					}
				}
				total = record.data.total;
				if (total == 0) {
					rl_nosell.setVisibility(View.VISIBLE);
					if (thisYear == created) {
						tv_notsell_des.setText("您还未售出任何药品，要加把劲了哦!");
					} else {
						tv_notsell_des.setText("当前年份没有销售记录!");
					}
				} else {
					rl_nosell.setVisibility(View.GONE);
				}
				String t = total+"";
				if ((total+"").length()>4){
					t = (total+"").substring(0,4)+"...";
				}
				textView1.setText(t + ""); // 您还未售出任何药品，要加把劲了哦！”//当前年份没有销售记录”
				recordAdapter = new SalesRecordAdapter(mActivity,
						R.layout.adapter_sale_records, list);
				mExpandList.setAdapter(recordAdapter);
			}
		}
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {

		// TODO Auto-generated method stub
		if (s == -1 && list.size() == 0) {
			rl_nosell.setVisibility(View.VISIBLE);
			tv_notsell_des.setText("网络不可用，请稍后重试。");
			iv_iv.setVisibility(View.VISIBLE);
			iv_iv.setBackgroundResource(R.drawable.not_net);
			// list = new ArrayList<Info>();
			ToastUtils.showToast("网络不可用，请稍后重试！");
		} else if (s == -1) {
			ToastUtils.showToast("网络不可用，请稍后重试！");
		} else {
			ToastUtils.showToast("系统繁忙");
		}

		/*
		 * recordAdapter = new SalesRecordAdapter(mActivity,
		 * R.layout.adapter_sale_records, list);
		 */
		mExpandList.setAdapter(recordAdapter);
		mExpandList.onRefreshComplete();

	};

	public static String[] parseDate(long strDate) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(strDate);
		String[] s = new String[5];
		s[0] = (c.get(Calendar.YEAR)) + "";
		s[1] = (c.get(Calendar.MONTH) + 1) + "";
		s[2] = c.get(Calendar.DATE) + "";
		s[3] = change((c.get(Calendar.DAY_OF_WEEK) - 1) + "");

		long mills = 0;
		mills = c.getTimeInMillis();
		s[4] = mills + "";
		return s;
	}

	public static String change(String s) {
		String week = s;
		if (s.equals("1")) {
			week = "一";
		} else if (s.equals("2")) {
			week = "二";
		} else if (s.equals("3")) {
			week = "三";
		} else if (s.equals("4")) {
			week = "四";
		} else if (s.equals("5")) {
			week = "五";
		} else if (s.equals("6")) {
			week = "六";
		} else if (s.equals("7")) {
			week = "日";
		}else if (s.equals("0")) {
			week = "日";
		}
		return week;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mActivity.unregisterReceiver(reeiver);
	}
}
