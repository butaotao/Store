package com.dachen.mediecinelibraryrealize.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.AlertTimeDialog.InterfaceGetData;
import com.dachen.mediecinelibraryrealize.entity.Music;
import com.dachen.mediecinelibraryrealize.entity.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class RingAlertDialog implements OnClickListener {
	private Activity mContext;
	private LayoutInflater mInflater;
	private InterfaceGetData interfaces;
	private View mRootView;
	private ListView mLv;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCLEL = "cancel";

	// 铃声名
	private static List<Music> mSoundNames = new ArrayList<Music>();

	private SoundPlayer mSoundPlayer;
	private Button mBtnSubmit;
	private Button mBtnCancel;
	private TextView mTvTitle;
	private int mWidth;
	private float mDensity;
	private String mDes;
	private TextView mTv_lv_item;
	private RadioButton mRb_lv;
	private TextView mtv;
	private InterfaceGetSound mInter;

	static {
		Music music = new Music();
		music.musicName = "drug_remind_01";
		music.des = "班得瑞钢琴曲";
		Music music1 = new Music();
		music1.musicName = "drug_remind_02";
		music1.des = "传统好听的特效铃声";
		Music music2 = new Music();
		music2.musicName = "drug_remind_03";
		music2.des = "单纯清脆和缓铃声";
		Music music3 = new Music();
		music3.musicName = "drug_remind_04";
		music3.des = "经典铃声";
		Music music4 = new Music();
		music4.musicName = "drug_remind_05";
		music4.des = "流水细腻轻柔铃声";
		Music music5 = new Music();
		music5.musicName = "drug_remind_06";
		music5.des = "清爽铃声";
		Music music6 = new Music();
		music6.musicName = "drug_remind_07";
		music6.des = "十分悦耳的铃声";
		Music music7 = new Music();
		music7.musicName = "drug_remind_08";
		music7.des = "天使简约优美铃声";
		Music music8 = new Music();
		music8.musicName = "drug_remind_09";
		music8.des = "唯美的铃声";
		Music music9 = new Music();
		music9.musicName = "drug_remind_10";
		music9.des = "温馨的铃声";
		Music music10 = new Music();
		music10.musicName = "drug_remind_11";
		music10.des = "预约舒缓适宜铃声";
		Music music11 = new Music();
		music11.des = "静音";
		mSoundNames.add(music);
		mSoundNames.add(music1);
		mSoundNames.add(music2);
		mSoundNames.add(music3);
		mSoundNames.add(music4);
		mSoundNames.add(music5);
		mSoundNames.add(music6);
		mSoundNames.add(music7);
		mSoundNames.add(music8);
		mSoundNames.add(music9);
		mSoundNames.add(music10);
		mSoundNames.add(music11);

	}

	public RingAlertDialog(Activity context,DrugRemind info, InterfaceGetSound inter) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mInter = inter;
		
		
		if (null!=info&&info.soundDesc!=null) {
			for (int i = 0; i < mSoundNames.size(); i++) {
				if (info.soundDesc.equals(mSoundNames.get(i).des)) { 
					Music music = new Music();
					music = mSoundNames.get(i); 
					music.isSelect = true; 
					mSoundNames.set(i, music);
					mDes = music.des;
					LogUtils.burtLog("mDes==="+mDes);
					break;
				}else {
					Music music = new Music();
					music = mSoundNames.get(i); 
					music.isSelect = false; 
					mSoundNames.set(i, music); 
				}
			}
		}
		if (TextUtils.isEmpty(mDes)) {
			mDes = "班得瑞钢琴曲";
	
			for (int i = 0; i < mSoundNames.size(); i++) {
				Music music =   mSoundNames.get(i); 
				if (i==0) {
					music.isSelect = true;
				}else {
					music.isSelect = false;
				}
				mSoundNames.set(i, music); 
				LogUtils.burtLog("music=="+music);
			}
		}
		initData();
		initView();
		initDialog();
		initEvent();
		
		

	}

	private void initEvent() {
		MyAdapter adapter = new MyAdapter(mSoundNames);

		mLv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	private void initDialog() {
		mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		mDensity = mContext.getResources().getDisplayMetrics().density;

		mDialog = new Dialog(mContext, R.style.dialog_style);
		mDialog.setCanceledOnTouchOutside(false);

		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.translate);
		mDialog.setContentView(mRootView);
		mDialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = mWidth;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mRootView = mInflater.inflate(R.layout.lv_ringalertdialog, null);

		mLv = (ListView) mRootView.findViewById(R.id.lv);
		// 带取消和确定的标题
		mBtnSubmit = (Button) mRootView.findViewById(R.id.btnSubmit);
		mBtnSubmit.setOnClickListener(this);
		mBtnSubmit.setTag(TAG_SUBMIT);
		mTvTitle = (TextView) mRootView.findViewById(R.id.tvTitle);
		mTvTitle.setText("选择铃声");
		mBtnCancel = (Button) mRootView.findViewById(R.id.btnCancel);
		mBtnCancel.setOnClickListener(this);
		mBtnCancel.setTag(TAG_CANCLEL);

	}

	private void initData() {
		mSoundPlayer = new SoundPlayer(mContext);

	}

	private class MyAdapter extends BaseAdapter {
		List<Music> mSoundName;

		public MyAdapter(List<Music> soundNames) {
			this.mSoundName = soundNames;
		}

		@Override
		public int getCount() {

			return this.mSoundName.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSoundName.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Music music = (Music) getItem(position);
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_lv_ringalert, null);
			}
			mTv_lv_item = (TextView) convertView.findViewById(R.id.tv_lv_item);
			mRb_lv = (RadioButton) convertView.findViewById(R.id.rb_lv);

			mTv_lv_item.setText(music.des);
			mRb_lv.setChecked(false);
			if (music.isSelect) {
				mRb_lv.setChecked(true);
			}

			final int location = position;

			mRb_lv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (int i = 0; i < mSoundName.size(); i++) {
						Music music = mSoundName.get(i);
						music.isSelect = false;
						mSoundName.set(i, music);
					}
					music.isSelect = true;
					mDes = music.des;
					mSoundName.set(location, music);
					notifyDataSetChanged();
					AudioManager audioManager = (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
					if("静音".equals(mDes)){
						if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
							audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//设置为静音模式
						}
                        mSoundPlayer.stop();
					}else{
						if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
							audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//设置为声音模式
						}
                        Uri uri = Uri
                                .parse("android.resource://com.dachen.dgrouppatient/raw/"
                                        + music.musicName);

                        mSoundPlayer.play(uri);
					}
				}
			});
			return convertView;
		}

	}

	private Dialog mDialog;

	public void showDialog() {
		mDialog.show();
	}

	public void closeDialog() {

		mSoundPlayer.stop();
		mDialog.dismiss();

	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag == TAG_SUBMIT) {
			closeDialog();
			
			mInter.getSound(mDes);
		} else {
			closeDialog();
		}
	}

	public interface InterfaceGetSound {
		public void getSound(String des);
	}

}
