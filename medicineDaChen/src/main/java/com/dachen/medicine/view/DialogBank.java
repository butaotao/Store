package com.dachen.medicine.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.AbstractWheelTextAdapter;

public class DialogBank extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    private OnClickListener listener;
    TextView btnSelectBank;
    TextView btnCancelSelectBank; 
    LinearLayout spaceforcancel;
    String methodOne;
    String methodTwo;
    TextView dialog_methodtwo;
    TextView dialog_showmethodone;
	private WheelViews wvProvince;
	private int maxsize = 24;
	private int minsize = 14;
	int currentItem =3;
	 AddressTextAdapter adapter;
    public DialogBank(Activity activity, OnClickListener l,String methodOne,String methodTwo) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.listener = l;
        this.methodOne = methodOne;
        this.methodTwo = methodTwo;
    }
    public DialogBank(Activity activity, OnClickListener l) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.listener = l; 
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choicecity);
        wvProvince = (WheelViews) findViewById(R.id.wv_address_province);
		wvProvince.setVisibleItems(5);
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("中国银行");
		list.add("建设银行");
		list.add("农业银行");
		list.add("招商银行");
		list.add("花旗银行");
		list.add("汇丰银行");
		list.add("渣打银行");
		list.add("平安银行");
		list.add("交通银行");
		list.add("北京银行");
		list.add("中国邮政");
		list.add("包商银行");
		list.add("工商银行");
		list.add("西安银行");
		list.add("美洲银行");
		  adapter = new AddressTextAdapter(getContext(), list, currentItem, maxsize, minsize);
		wvProvince.setViewAdapter(adapter);
		wvProvince.setCurrentItem(3);
		wvProvince.addChangingListener(new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelViews wheel, int oldValue, int newValue) { 
			String currentText = (String) adapter.getItemText(wheel.getCurrentItem()); 
			setTextviewSize(currentText, adapter);
		}
	});

	wvProvince.addScrollingListener(new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelViews wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelViews wheel) { 
			String currentText = (String) adapter.getItemText(wheel.getCurrentItem());
			setTextviewSize(currentText, adapter);
		}
	});
	 DisplayMetrics metric = new DisplayMetrics();
     mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
     WindowManager.LayoutParams p = getWindow().getAttributes();
     p.width = (int) (metric.widthPixels);
     Window window = getWindow();
     window.setAttributes(p);
     window.setGravity(Gravity.BOTTOM); 
     setCancelable(true);
    // this.setCanceledOnTouchOutside(true);
     initView();
 } 
	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}
	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}
       

    private void initView() {
    	btnSelectBank = (TextView) findViewById(R.id.btnSelectBank);
    	btnCancelSelectBank = (TextView) findViewById(R.id.btnCancelSelectBank); 
    	spaceforcancel  = (LinearLayout) findViewById(R.id.spaceforcancel);
    
    	btnSelectBank.setOnClickListener(this);
    	spaceforcancel.setOnClickListener(this);
    	btnCancelSelectBank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spaceforcancel:
            	listener.btnPhotoClick(v);
                this.dismiss();
                break;
            case R.id.btnSelectBank:
            	listener.btnCameraClick(v);
                listener.btnCameraClick(v);
                this.dismiss();
                break; 
            case R.id.btnCancelSelectBank:
            	listener.btnPhotoClick(v);
            	this.dismiss();
            	break;
            default:
                break;
        }
    }



    public interface OnClickListener {
        public void btnCameraClick(View v);//

        public void btnPhotoClick(View v);//
    }
}
