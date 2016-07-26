package com.dachen.medicine.activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.QrScanAdapter;
import com.dachen.medicine.adapter.QrScanHavingBuyAdapter;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.CustomUtils;
import com.dachen.medicine.common.utils.DrugList;
import com.dachen.medicine.common.utils.JsonUtils.CdrugRecipeitemGiveChange;
import com.dachen.medicine.common.utils.JsonUtils.DrugListTranslate;
import com.dachen.medicine.common.utils.JsonUtils.ScanTiaoXingMaInfoTranslate;
import com.dachen.medicine.common.utils.NetUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugGive;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.CdrugRecipeitemGive;
import com.dachen.medicine.entity.DragList;
import com.dachen.medicine.entity.ScanTiaoXingMaInfo;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.logic.Sort;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.CustomDialog;
import com.dachen.medicine.view.DialogChoicePatientGallery;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
public class MipcaActivityCapture extends  BaseActivity implements Callback,OnClickListener,OnHttpListener{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	ListView listView;
	QrScanAdapter adapter;
	QrScanHavingBuyAdapter havingBuy;
	TextView tv_title;
	public static List<CdrugRecipeitem> listScaning;
	public  List<CdrugRecipeitem> listScaning_showIntitleBar;
	public static List<CdrugRecipeitem> listScaning_false;
	RelativeLayout ll_sub;
	Button edit_title;
	ListView listview_havebuy;
	Button btn_overscaning;
	String scandCode ="";
	CustomDialog customDialog;
	String code ="";
	LinearLayout ll_listviews;
	String coderesults ="";
	RelativeLayout iv_back;
	boolean getdata;
	boolean isStep;
	//在服务器端，未找到该药监码对应的品种
	public static final String SCORD_NOTFOUND_ONSERVER = "#00#";
	DialogChoicePatientGallery dialog ;
	//所有买的药
	List<CdrugRecipeitem>  listmedies;
	//多买或者少买的药品
	public  List<CdrugRecipeitem>  listmedies_more_less;
	public  List<CdrugRecipeitem> listdrug;
	boolean isflag;
	TextView tv_back;
	LinearLayout panelHandle;
	boolean open = true;
	RelativeLayout rl_all;
	//赠药标记
	public static final int ZENGSONG = 4;
	Handler handlers = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:

					if (!coderesults.equals("")&&coderesults.length()>=4) {
						if (!NetUtil.checkNetworkEnable(MipcaActivityCapture.this)) {
							ToastUtils.showToast("网络不可用，请稍后重试！");
							return;
						}
						Intent intent = new Intent(MipcaActivityCapture.this,SalesClerkActivity.class);
						if (coderesults!=null) {
							if (getGiveInfo()){

							}else {
								intent.putExtra("result", coderesults);
								startActivity(intent);
								finish();
							}

						}
					}


					break;
				case 1:
					if (!TextUtils.isEmpty(scandCode)&&scandCode.length()<20){
						ToastUtils.showToast("药监码识别失败，请从新扫描");
						return;
					}
					boolean isScaning = false;
					for (int i=0;i<listScaning_false.size();i++){
						if(listScaning_false.get(i).scanCode.substring(0,7).equals(scandCode.substring(0, 7))){
							if (listScaning_false.get(i).manufacturer.contains("#0")){
								CdrugRecipeitem bean3 = new CdrugRecipeitem();
								bean3.general_name = getString(R.string.not_found_this_code);
								bean3.scanCode = scandCode;
								bean3.id = SCORD_NOTFOUND_ONSERVER;
								bean3.manufacturer = SCORD_NOTFOUND_ONSERVER;
								bean3.createtime = System.currentTimeMillis();
								listScaning_false.add(bean3);
								QrScanHavingBuyAdapter sAdapter = (QrScanHavingBuyAdapter)listview_havebuy.getAdapter();
								sAdapter.notifyDataSetChanged();
								return;
							}
						}

					}
					for (int i = 0; i < listScaning.size(); i++) {
						if (listScaning.get(i).scanCode!=null&&
								listScaning.get(i).scanCode
										.equals(scandCode)) {
							CdrugRecipeitem  bean = null;
							try {
								bean = (CdrugRecipeitem) listScaning.get(i).deepCopy();
							} catch (Exception e) {
								e.printStackTrace();
							}
							bean.isAdd = 0;
							listScaning_false.add(bean);
							listScaning.remove(i);
							QrScanHavingBuyAdapter sAdapter = (QrScanHavingBuyAdapter)listview_havebuy.getAdapter();
							sAdapter.notifyDataSetChanged();
							//在已经扫描的药品id中可以找到
							isScaning = true;
							break;
						}
					}
					for (int i = 0; i < listScaning_showIntitleBar.size(); i++) {
						if (listScaning_showIntitleBar.get(i).scanCode!=null&&
								listScaning_showIntitleBar.get(i).scanCode.substring(0,7)
										.equals(scandCode.substring(0,7))) {
							CdrugRecipeitem bean = null;
							try {
								bean = (CdrugRecipeitem) listScaning_showIntitleBar.get(i).deepCopy();
							} catch (Exception e) {
								e.printStackTrace();
							}
							bean.scanCode = scandCode;
							bean.isAdd = 0;
							if (isScaning) {
								if (bean.scaningNum>0) {
									if (bean.scaningNum ==1 ) {
										listScaning_showIntitleBar.remove(i);
									}else {
										bean.scaningNum = bean.scaningNum-1;
										bean.createtime = System.currentTimeMillis();
										listScaning_showIntitleBar.set(i, bean);
									}
								}else {
									listScaning_showIntitleBar.remove(i);
								}
							}
							setListViewHeightBasedOnChildren();
							Sort.sortMedie(listScaning_showIntitleBar);
							adapter.notifyDataSetChanged();
							setListViewHeightBasedOnChildren();
							break;
						}
					}
					if (coderesults!=null&&coderesults.toString().substring(0, 1).equals("8")&&coderesults.trim().length()!=20) {
						CdrugRecipeitem bean = new CdrugRecipeitem();
						bean.manufacturer = "#0011#";
						bean.scanCode = scandCode;
						bean.isAdd = 1;
						listScaning_false.add(bean);
						QrScanHavingBuyAdapter sAdapter = (QrScanHavingBuyAdapter)listview_havebuy.getAdapter();
						sAdapter.notifyDataSetChanged();
						return;
					}
					if (isScaning) {
						return;
					}
					getData();
					break;
				case 3:
					QrScanHavingBuyAdapter sAdapter = (QrScanHavingBuyAdapter)listview_havebuy.getAdapter();
					sAdapter.notifyDataSetChanged();

					break;
				case 4:
					continuePreview();
					break;
				case 5:
					continuePreview();
					ToastUtils.showToast("无效药监码，请确认是否扫描成普通商品条码");
					break;
				case 6:
					continuePreview();
					ToastUtils.showToast("无效条形码");
					break;
				default:
					break;
			}
		};
	};
	//QRCodeScannerUI
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		CameraManager.init(getApplication());
		rl_all = (RelativeLayout) findViewById(R.id.rl_all);
		rl_all.setOnClickListener(this);
		customDialog = new CustomDialog(MipcaActivityCapture.this,"知道了",null);
		panelHandle = (LinearLayout) findViewById(R.id.panelHandle);
		panelHandle.setOnClickListener(this);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		btn_overscaning = (Button) findViewById(R.id.btn_overscaning);
		tv_title = (TextView) findViewById(R.id.tv_title);
		hasSurface = false;
		isflag = false;
		inactivityTimer = new InactivityTimer(this);
		ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		tv_title.setText(getString(R.string.scan_sell));
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		ll_listviews = (LinearLayout) findViewById(R.id.ll_listviews);
		listview_havebuy = (ListView) this.findViewById(R.id.listview_havebuy);
		listView = (ListView) this.findViewById(R.id.listview);
		code = getIntent().getStringExtra("code");
		iv_back = (RelativeLayout) findViewById(R.id.rl_back);
		iv_back.setOnClickListener(this);
		listScaning = new ArrayList<CdrugRecipeitem>();
		listScaning_false = new ArrayList<CdrugRecipeitem>();
		listScaning_showIntitleBar  = ScaningData.getlistScaning_showIntitleBar();
		listmedies = new ArrayList<CdrugRecipeitem>();
		listdrug = ScaningData.getlistdrug();
		listmedies_more_less =ScaningData.getListMedies_more_less();

		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setVisibility(View.VISIBLE);
		isStep = false;
		adapter = new QrScanAdapter(MipcaActivityCapture.this, R.layout.qrscanadapter,
				listScaning_showIntitleBar);
		listView.setAdapter(adapter);
		if (code.contains("erweima")) {
			//扫描二维码界面
			listScaning_showIntitleBar.clear();
			ll_listviews.setVisibility(View.GONE);
		}else if(code.contains("tiaoxingma")){
			//扫描条形码界面（有购药清单）
			View view = vstub_title.inflate(this, R.layout.layout_sub_scantitle, ll_sub);
			ll_listviews.setVisibility(View.VISIBLE);
			String erweima_code = getIntent().getStringExtra("erweima_code");
			edit_title = (Button) view.findViewById(R.id.edit_title);
			edit_title.setOnClickListener(this);
			btn_overscaning.setOnClickListener(this);

			havingBuy = new QrScanHavingBuyAdapter
					(MipcaActivityCapture.this, R.layout.adapterhavingbuyscan, listScaning_false);
			if (!code.equals("tiaoxingma_MedieNumInconformityActivity")&&!code.equals("tiaoxingma_SalesClerkActivity")) {


				String s = "goods/salesLog/getRecipeitemsListById";
				HashMap<String,String> maps = new HashMap<>();
				maps.put("access_token",SharedPreferenceUtil.getString("session",""));
				maps.put("recipeId",erweima_code);
				//普通二维码请求
				new HttpManager().get(this,
						s,
						DrugList.class,
						maps,
						this, false, 3);
				if (ScaningData.sCanFlag==ZENGSONG) {
					listdrug = ScaningData.getlistdrug();
				}
			}else {
				isflag = true;
				listmedies = ScaningData.getListmedies();
				for (int i = 0; i < listmedies.size(); i++) {
					CdrugRecipeitem   drug = null;
					try {
						drug = (CdrugRecipeitem) listmedies.get(i).deepCopy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					drug.scaningNum = listmedies.get(i).scaningNum;
					drug.numResone = listmedies.get(i).scaningNum;
					if (null != listmedies.get(i).lists) {
						for (int j = 0; j < listmedies.get(i).lists.size(); j++) {
							MedieNum item = null;
							try {
								item = (MedieNum) listmedies.get(i).lists.get(j).deepCopy();
							} catch (Exception e) {
								e.printStackTrace();
							}
							drug.scanCode = item.num;
							drug.lists.get(j).num = item.num;
							listScaning.add(drug);
						}
					}
				}
				listView.setVisibility(View.VISIBLE);
				Sort.sortMedie(listScaning_showIntitleBar);
				adapter.notifyDataSetChanged();
			}
		}



		havingBuy = new QrScanHavingBuyAdapter(this, R.layout.adapterhavingbuyscan, listScaning_false);
		if(code.contains("tiaoxingma")){
			listview_havebuy.setAdapter(havingBuy);
		}
		listview_havebuy.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		getdata = false;
		if (code.contains("erweima")) {
			SharedPreferenceUtil.putInt("show_alert", 0);
		}else if(code.contains("tiaoxingma")){
			SharedPreferenceUtil.putInt("show_alert", 1);
		}
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		preview();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		continuePreview();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferenceUtil.putInt("show_alert", 0);

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CameraManager.get().closeDriver();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
	}
	@Override
	protected void onDestroy() {
		CameraManager.get().closeDriver();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(Result result, Bitmap barcode) {
		try {
			inactivityTimer.onActivity();
		} catch (Exception e) {
			// TODO: handle exception
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			super.onBackPressed();
		}
		if (dialog!=null&&dialog.isShow()) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub 
					try {
						Thread.sleep(4000);
						handlers.sendEmptyMessage(4);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

			return;
		}
		playBeepSoundAndVibrate();
		final String resultString = result.getText();
		//Toast.makeText(MipcaActivityCapture.this, resultString, Toast.LENGTH_SHORT).show();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "识别失败，请重新扫描!", Toast.LENGTH_SHORT).show();
		}else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					coderesults = resultString;

					try {
						if (code.contains("erweima")) {
							//coderesults = "dcb574d8060b472651abcd31f12";
							if (resultString.contains("dcb")) {
								handlers.sendEmptyMessage(0);
								Thread.sleep(1000);
								handlers.sendEmptyMessage(4);
							}else if(resultString.contains("dch")){
								handlers.sendEmptyMessage(0);
								Thread.sleep(1000);
								handlers.sendEmptyMessage(4);
							}else {
								Thread.sleep(600);
								handlers.sendEmptyMessage(5);

							}

						}else if(code.contains("tiaoxingma")){
							scandCode = resultString + "";
							boolean isNum = resultString.matches("[0-9]+");
							if (!isNum){
								handlers.sendEmptyMessage(5);
								return;
							}
							handlers.sendEmptyMessage(1);
							Thread.sleep(2000);
							handlers.sendEmptyMessage(4);
							//if (resultString.contains("81")) {

							/*}else{
								handlers.sendEmptyMessage(6);
							}*/

						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
				}
			}).start();
		}
	}
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	private void continuePreview() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		initCamera(surfaceHolder);
		if (handler != null) {
			handler.restartPreviewAndDecode();
		}
	}
	private void preview(){
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		initCamera(surfaceHolder);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<CdrugRecipeitem>> beans  = new HashMap<String, ArrayList<CdrugRecipeitem>>();
		switch (v.getId()) {
			case R.id.edit_title:
				if (listView.getVisibility()==View.VISIBLE) {
					listView.setVisibility(View.INVISIBLE);//
					edit_title.setText("显示购药清单");
				}else {
					listView.setVisibility(View.VISIBLE);
					edit_title.setText("隐藏购药清单");
				}
				break;
			case R.id.btn_overscaning:
				//把扫描的条码全部遍历到map中
				continuePreview();
				for (int i = 0; i < listScaning.size(); i++) {

					if (beans.containsKey(listScaning.get(i).id)) {
						ArrayList<CdrugRecipeitem> lists = beans.get(listScaning.get(i).id);
						lists.add(listScaning.get(i));
						beans.put(listScaning.get(i).id, lists);
					}else {
						ArrayList<CdrugRecipeitem> lists = new ArrayList<CdrugRecipeitem>();
						lists.add(listScaning.get(i));
						beans.put(listScaning.get(i).id, lists);
					}
				}
				if (listdrug==null) {
					return;
				}
				getNotSameList(beans,listdrug);
				if (ScaningData.sCanFlag==ZENGSONG) {
					ScaningData.sCanFlag = 5;
				}
				Intent intent = new Intent(this,SalesClerkActivity.class);
				intent.putExtra("result","red");
				intent.putExtra("size", listdrug.size());
				startActivity(intent);
				super.onBackPressed();
				break;
			case R.id.rl_back:
				continuePreview();
				if (code.contains("erweima")) {
				}else if(code.contains("tiaoxingma")){
					for (int i = 0; i < listScaning.size(); i++) {

						if (beans.containsKey(listScaning.get(i).id)) {
							ArrayList<CdrugRecipeitem> lists = beans.get(listScaning.get(i).id);
							lists.add(listScaning.get(i));
							beans.put(listScaning.get(i).id, lists);
						}else {
							ArrayList<CdrugRecipeitem> lists = new ArrayList<CdrugRecipeitem>();
							lists.add(listScaning.get(i));
							beans.put(listScaning.get(i).id, lists);
						}
					}
					if (listdrug==null) {
						return;
					}

					getNotSameList(beans,listdrug);
					intent = new Intent(this,SalesClerkActivity.class);
					intent.putExtra("result","red");
					intent.putExtra("size", listdrug.size());
					startActivity(intent);
				}
				super.onBackPressed();
				break;
			case R.id.panelHandle:

				if (open) {
					open = false;
					setListViewHeightBasedOnChildren();
				}else {

					open = true;
					setListViewHeightBasedOnChildren();
				}
				break;
			case R.id.rl_all:
				onPause();
				onStop();
				onResume();
				break;
			default:
				break;
		}
	}
	private void setListViewHeightBasedOnChildren() {
		listScaning_showIntitleBar = ScaningData.getlistScaning_showIntitleBar();
		int num=1;
		if (open) {
			num = 1;
		}else {
			num = 5;
		}
		android.widget.ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		if (null!=listScaning_showIntitleBar&&listScaning_showIntitleBar.size()>0) {
			if (num==1) {
				for (int i = 0; i < 1; i++) {
					View listItem = listAdapter.getView(0, null, listView);
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
					//ToastUtils.showToast("num=="+num);
				}
			}else if (listScaning_showIntitleBar.size()<=num) {
				for (int i = 0; i < listScaning_showIntitleBar.size(); i++) {
					View listItem = listAdapter.getView(0, null, listView);
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}
			}else if (listScaning_showIntitleBar.size()>num) {
				for (int i = 0; i < 5; i++) {
					View listItem = listAdapter.getView(0, null, listView);
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}
			}
			listView.setSelection(ListView.FOCUS_DOWN);
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	@Override
	public void onSuccess(com.dachen.medicine.entity.Result results)  {
		// TODO Auto-generated method stub 
		if (results.resultCode ==1){
			if (results instanceof DrugList) {
				DrugList list = (DrugList) results;
				getdata = true;
				ArrayList<CdrugRecipeitem>  lists = new ArrayList<>();
				if (null!=list&&null!=list.data){
					lists = DrugListTranslate.getRecipeitems(list.data.receiptList);
				}


				if(null!=lists&&lists.size()>0){
					listdrug.clear();
					listdrug .addAll(lists);

				}else {
					ToastUtils.showToast("数据异常");
				}

			}else if(results instanceof CdrugGive) {
				CdrugRecipeitem item = new CdrugRecipeitem();
				CdrugGive datas = (CdrugGive) results;
				CdrugRecipeitemGive.CdrugRecipeitemGiveInfo give = CdrugRecipeitemGiveChange.getData(datas);

				if (give==null||!give.is_join) {
					showDialog("本店暂不支持该药单中的\r\n药品进行积分兑换活动");

					return;
				}
				item.requires_quantity = ScaningData.getGiveRequireNum(give.zsmdwypxhjfs, give.num_syjf);
				if (0 == give.num_syjf || item.requires_quantity == 0) {
					showDialog("该赠药清单中已无可兑换的\r\n药品，不能提供赠药");
					return;
				}
				handlers.sendEmptyMessage(4);

				Intent intent = new Intent(MipcaActivityCapture.this,SalesClerkActivity.class);
				intent.putExtra("result", coderesults);
				startActivity(intent);
				finish();


			}
			isStep = false;
		}else {
			ToastUtils.showToast(results);
		}

	}
	//app在前台启动
	public static boolean isRunningForeground (Context context){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if(!TextUtils.isEmpty(cn.getClassName()) && cn.getClassName().contains("ipca"))
		{
			return true ;
		}

		return false ;
	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		if (s==-1) {
			ToastUtils.showToast("网络不可用，请稍后重试！");
		}
	}



	public void showDialog(String msg){

		customDialog.showDialog(null, msg, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				customDialog.dimissDialog();
				customDialog.dismissDialogWithClickView();
				finish();
			}
		}, null);
	}
	//lists 表示药单，maps表示扫描的所有
	public void getNotSameList(HashMap<String, ArrayList<CdrugRecipeitem>> maps,List<CdrugRecipeitem> lists ){

		listmedies = ScaningData.getListmedies();
		listmedies.clear();
		listmedies_more_less = ScaningData.getListMedies_more_less();
		listmedies_more_less.clear();
		//扫描的药在购物清单的情况
		for (int i = 0; i < lists.size(); i++) {
			//如果在扫描清单中（扫描了的药），map表示扫描的集合
			if (maps.containsKey(lists.get(i).id)) {
				CdrugRecipeitem des = null;
				try {
					des = (CdrugRecipeitem) maps.get(lists.get(i).id).get(0).deepCopy();
				} catch (Exception e) {
					e.printStackTrace();
				}

				des.requires_quantity = lists.get(i).requires_quantity;
				des.bought_quantity = lists.get(i).bought_quantity;
				int num = CustomUtils.getData(lists.get(i).requires_quantity,
						lists.get(i).bought_quantity, maps.get(lists.get(i).id).size());
				if(num>0){
					des.isAdd = 1;
					des.isSlected = "-1";
				}else if(num<0){

					des.isAdd = 0;
					des.isSlected = "-1";
				}else {
					des.isAdd = 2;
					des.isSlected = "";
				}

				List<MedieNum> nums = new ArrayList<MedieNum>();
				int meidanum = 0;
				ArrayList<CdrugRecipeitem> is = maps.get(lists.get(i).id);
				for (int j = 0; j < is.size(); j++) {
					CdrugRecipeitem item = is.get(j);
					if (item.lists==null||item.lists.size()==0){
						boolean add = false;
						if (nums.size()!=0){
							for (int m=0;m<nums.size();m++){
								String numms= nums.get(m).num;
								if (!TextUtils.isEmpty(numms)&&!TextUtils.isEmpty(item.scanCode)){
									if(numms.equals(item.scanCode)){
										add = true;
										break;
									}
								}

							}
						}else {
							if (!TextUtils.isEmpty(item.scanCode)){
								add = true;
								MedieNum n = new MedieNum();
								n.num = item.scanCode;
								des.foundCode = item.foundCode;
								meidanum = meidanum+1;
								nums.add(n);
							}

						}
						if (!add){
							if (!TextUtils.isEmpty(item.scanCode)){
								MedieNum n = new MedieNum();
								n.num = item.scanCode;
								des.foundCode = item.foundCode;
								meidanum = meidanum+1;
								nums.add(n);
							}

						}
					}else {
						for (int k =0;k<item.lists.size();k++){
							boolean add = false;
							if (nums.size()!=0){
								for (int f=0;f<nums.size();f++){
									String numss =item.lists.get(k).num;
									String numf = nums.get(f).num;
									if (!TextUtils.isEmpty(numf)&&!TextUtils.isEmpty(numss)){
										if(numf.equals(numss)){
											add = true;
											break;
										}
									}

								}
							}else {
								if (!TextUtils.isEmpty(is.get(j).lists.get(k).num)){
									add = true;
									MedieNum n = new MedieNum();
									MedieNum numj = is.get(j).lists.get(k);
									n.num = numj.num;
									des.foundCode = is.get(j).foundCode;
									meidanum = meidanum+1;
									nums.add(n);
								}

							}
							if (!add){
								if (!TextUtils.isEmpty(item.lists.get(k).num)){
									MedieNum n = new MedieNum();
									n.num = item.lists.get(k).num;
									des.foundCode = item.foundCode;
									meidanum = meidanum+1;
									nums.add(n);
								}

							}
						}
					}

				}
				des.numResone = nums.size();
				des.lists = (ArrayList<MedieNum>) nums;
				listmedies.add(des);
			}else {
				//如果在扫描清单中,却没有扫描， 那表示这种药少了，少的数目是需要的数目-已经购买的数目
				if (null != lists.get(i)) {
					CdrugRecipeitem des = null;
					try {
						des = (CdrugRecipeitem) lists.get(i).deepCopy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					des.isAdd = 0;
					des.isSlected = "-1";
					des.numResone = 0 ;
					if (des.bought_quantity ==0) {
						des.isAdd = 0;
					}else if (des.bought_quantity !=0) {
						des.isAdd = 1;
						int num = CustomUtils.getData(des.requires_quantity, des.bought_quantity, 0);
						if (num>0) {
							des.isAdd = 0;
						}
						else if (num<0){
							des.isAdd = 1;
						}else {
							des.isAdd = 2;
						}

					}
					listmedies.add(des);
				}
			}

		}
		//扫描的药没有在购物清单的情况
		Iterator iter = maps.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			Object val = entry.getValue();
			boolean flag = false;
			for (int j = 0; j < listmedies.size(); j++) {
				if (key.equals(listmedies.get(j).id)) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				CdrugRecipeitem des = new CdrugRecipeitem();
				ArrayList<CdrugRecipeitem> listbean = maps.get(key);
				CdrugRecipeitem bean = null;
				try {
					bean = (CdrugRecipeitem) listbean.get(0).deepCopy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				des = bean;

				des.numResone = maps.get(key).size();
				des.isAdd = 7;
				des.notExitInlist = true;
				des.isSlected = "-1";
				des.requires_quantity = 0;
				List<MedieNum> nums = new ArrayList<MedieNum>();
				for (int j = 0; j < maps.get(key).size(); j++) {
					MedieNum n = new MedieNum();
					n.num = maps.get(key).get(j).scanCode;
					nums.add(n);
				}
				des.lists = (ArrayList<MedieNum>) nums;
				listmedies.add(des);
			}
		}
		for (int i = 0; i < listmedies.size(); i++) {
			if (listmedies.get(i).isAdd==0||listmedies.get(i).isAdd==1||listmedies.get(i).isAdd==7) {
				listmedies_more_less.add(listmedies.get(i));
			}
		}
		listmedies = ScaningData.getListmedies();
	}
	@Override
	public void onBackPressed() {
		continuePreview();
		finish();
	}
	public boolean getGiveInfo(){
		if(coderesults.startsWith("dch")&&coderesults.length()>10) {


			String  s ="goods/salesLog/getPatientToExchangeGoodsMsg";
			HashMap<String,String> maps = new HashMap<>();
			maps.put("access_token",SharedPreferenceUtil.getString("session",""));
			maps.put("goodsId",coderesults.substring(3, coderesults.indexOf("and")));
			maps.put("patientId",coderesults.substring(coderesults.indexOf("and")+3));
			//专用赠药二维码请求
			new HttpManager().get(this,
					s,
					CdrugGive.class,
					maps,
					this, false, 3);
			return true;
		}
		return false;
	}
	public void getData(){
		String s = "goods/salesLog/getGoodsMsgByDrugCode";
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("access_token", SharedPreferenceUtil.getString("session",""));
		interfaces.put("drugCode", scandCode);
		final String code =scandCode;
		new HttpManager().get(this,
				s,
				ScanTiaoXingMaInfo.class,interfaces,
				new OnHttpListener<com.dachen.medicine.entity.Result>() {
					@Override
					public void onSuccess(com.dachen.medicine.entity.Result results)  {
						CdrugRecipeitem  bean = null;
						try {
							bean = (CdrugRecipeitem) new CdrugRecipeitem().deepCopy();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (results instanceof ScanTiaoXingMaInfo) {
							ScanTiaoXingMaInfo info = (ScanTiaoXingMaInfo)results;
							CdrugRecipeitem barcode = ScanTiaoXingMaInfoTranslate.getTiaoXingMaInfo(info.data);
							//CdrugRecipeitem barcode = (CdrugRecipeitem) results;

							if (null == barcode.is_exists) {
								return;
							}
							if (barcode.is_exists.contains("true")||!TextUtils.isEmpty(barcode.id)) {
								try {
									bean = getDrugRecipeData((CdrugRecipeitem) barcode.deepCopy(),code);
								} catch (Exception e) {
									e.printStackTrace();
								}
								bean.foundCode = true;
								bean.id = barcode.id;
								bean.scaningNum = 1;

								CdrugRecipeitem druglistdata = getDrugAccordingCode(bean);
								if(druglistdata!=null)
								{
									bean = druglistdata;
								}
								listScaning_false.add(bean);
								//扫描赠药条形码却没有在药单中发现此药
								if(ScaningData.sCanFlag==ZENGSONG&&null==druglistdata){
									ToastUtils.showToast("当前扫描不是药单中的药品，请核实");

									return;
								}
								CdrugRecipeitem bean2 = new CdrugRecipeitem();

								int positionSelect = -1;
								//如果服务器有，则在现有的
								for (int i = 0; i < listScaning_showIntitleBar.size(); i++) {
									if (listScaning_showIntitleBar.get(i).scanCode.substring(0, 7).equals(code.substring(0, 7))
											&&barcode.id.equals(listScaning_showIntitleBar.get(i).id)) {
										CdrugRecipeitem item = null;
										try {
											item = getDrugRecipeData((CdrugRecipeitem) listScaning_showIntitleBar.get(i).deepCopy(),code);
										} catch (Exception e) {
											e.printStackTrace();
										}
										bean2 = item;
										bean2.scaningNum = item.scaningNum+1;
										positionSelect = i;
										break;
									};
								}

								if (positionSelect==-1) {
									if (ScaningData.sCanFlag==ZENGSONG&&(bean.requires_quantity<bean.scaningNum)){
										ToastUtils.showToast( "已经扫描"+(bean.scaningNum)+"盒，不能超出最大可赠数量！");
										return;
									}
									listScaning_showIntitleBar.add( bean);
								}else {
									if (ScaningData.sCanFlag==ZENGSONG&&bean2.requires_quantity<bean2.scaningNum){
										ToastUtils.showToast( "已经扫描"+(bean2.scaningNum-1)+"盒，不能超出最大可赠数量！");
										return;
									}
									listScaning_showIntitleBar.set(positionSelect, bean2);
								}
								listScaning.add(bean);
								setListViewHeightBasedOnChildren();
								adapter.notifyDataSetChanged();
								Sort.sortMedie(listScaning_showIntitleBar);
								handlers.sendEmptyMessage(3);
							}else{
								if(ScaningData.sCanFlag==ZENGSONG){
									ToastUtils.showToast("未找到该品种，请另外选择药品扫描");
									return;
								}
								//在扫描的集合中寻找，比较前七位如果找到则数目加一个
								for (int i = 0; i < listScaning_showIntitleBar.size(); i++) {
									if (listScaning_showIntitleBar.get(i).scanCode.substring(0,7).equals(code.substring(0, 7))
											) {
										CdrugRecipeitem   item = null;
										try {
											item = getDrugRecipeData((CdrugRecipeitem) listScaning_showIntitleBar.get(i).deepCopy(),code);
										} catch (Exception e) {
											e.printStackTrace();
										}
										bean = item;
										bean.scaningNum = item.scaningNum+1;
										bean.id = item.id;
										listScaning.add(bean);
										listScaning_false.add(bean);
										listScaning_showIntitleBar.set(i, bean);
										Sort.sortMedie(listScaning_showIntitleBar);
										setListViewHeightBasedOnChildren();
										adapter.notifyDataSetChanged();
										handlers.sendEmptyMessage(3);
										return;
									};
								}
								if (null!=dialog&&dialog.isShow()) {
									new Thread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {
												Thread.sleep(4000);
												handlers.sendEmptyMessage(4);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}).start();
									return;
								}
								//如果在已经扫描的集合中没有找到则弹出对话框，让店员选择
								dialog = new DialogChoicePatientGallery(0,MipcaActivityCapture.this,0,
										listdrug,code,new GetMedieInfo() {
									@Override
									public void getdata(CdrugRecipeitem item) {
										// TODO Auto-generated method stub
										if (null!=item) {
											adapter.notifyDataSetChanged();
											handlers.sendEmptyMessage(3);
										}else {
											//选择以上都不是按钮
											handlers.sendEmptyMessage(3);
										}
										setListViewHeightBasedOnChildren();
										dialog =null;
									}
								},scandCode);
								if(isRunningForeground(MipcaActivityCapture.this)){
									dialog.show();
								}
							}
						}
					}

					@Override
					public void onSuccess(ArrayList<com.dachen.medicine.entity.Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {

					}
				}, false, 3);
	}
	public static CdrugRecipeitem getDrugRecipeData(CdrugRecipeitem item,String code){
		CdrugRecipeitem bean3 = null;
		try {
			bean3 = (CdrugRecipeitem) item.deepCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null!=item.unit&&!TextUtils.isEmpty(item.unit.name)){
			bean3.unit.name = item.unit.name;
		}else {
			bean3.unit.name = " ";
		}
		bean3.scanCode = code;
		bean3.isAdd = 1;
		bean3.createtime = System.currentTimeMillis();
		return bean3;
	}
	//扫描的条形码数据和药单做对比找到该药的具体信息
	public CdrugRecipeitem getDrugAccordingCode(CdrugRecipeitem barcode){
		CdrugRecipeitem bean = barcode;
		for (int i = 0; i < listdrug.size(); i++) {
			if (!TextUtils.isEmpty(barcode.id)&&!TextUtils.isEmpty(listdrug.get(i).id)) {
				if (barcode.id.equals(listdrug.get(i).id)) {
					bean.bought_quantity = listdrug.get(i).bought_quantity;
					bean.requires_quantity = listdrug.get(i).requires_quantity;
					bean.data = listdrug.get(i).data;
					bean.data1 = listdrug.get(i).data1;
					bean.unit = listdrug.get(i).unit;
					return bean;
				}
			}
		}
		return null;
	}
}