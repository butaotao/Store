package com.dachen.medicine.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dachen.medicine.R;
 

public class CustomDialog{
	private LayoutInflater 	mInflater;
	private Activity 		mContext;
	private View 			mView;
	private View 			mTitleDivide;
	private Dialog 			mDialog;
	private int 			mWidth;
	private float 			mDensity;
	private TextView 		mTvTitle;
	private TextView		mTvMsg;
	private Button 			mBtnConfirm;
	private Button	 	    mBtnCancel;
    private long startTime 	 = 0; 

	public CustomDialog(Activity context) {
		this.mInflater 	 = LayoutInflater.from(context);
		this.mContext 	 = context;
        initLayout();
		initDialog();
	}  
	public CustomDialog(Activity context,String ok,String cancel) {
		this.mInflater 	 = LayoutInflater.from(context);
		this.mContext 	 = context;
        initLayout();
		initDialog();
		if (!TextUtils.isEmpty(ok)) {
			mBtnConfirm.setText(ok);
		}
		if (!TextUtils.isEmpty(cancel)) {
			mBtnCancel.setText(cancel);
		}
	}  

    private void initLayout(){
        mView 		 	 = mInflater.inflate(R.layout.layout_alert_dialog, null);
        mTvTitle   		 = (TextView) mView.findViewById(R.id.tv_alert_title);
        mTitleDivide 	 = mView.findViewById(R.id.title_divide);
        mTvMsg 			 = (TextView) mView.findViewById(R.id.tv_alert_message);
        mWidth 			 = mContext.getResources().getDisplayMetrics().widthPixels;
        mDensity 		 = mContext.getResources().getDisplayMetrics().density;
        mBtnConfirm 	 = (Button) mView.findViewById(R.id.btn_confirm);
        mBtnCancel 	 	 = (Button) mView.findViewById(R.id.btn_cancel);

    }
	public void dismissDialogWithClickView(){
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dimissDialog();
			}
		});
	}
	private void initDialog() {
		mDialog = new Dialog(mContext, R.style.dialog_style);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.translate);
		mDialog.setContentView(mView);
		mDialog.getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width  = (int) (mWidth - 48 * mDensity);
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// popDialog.getWindow().setWindowAnimations(R.style.dialog_anim);
		mDialog.setCanceledOnTouchOutside(true);
	}
 

	public final void showDialog(String title,String msg, OnClickListener confirmListener, OnClickListener cancelListener){
        ;
        if(TextUtils.isEmpty(title)){
			mTitleDivide.setVisibility(View.GONE);
			mTvTitle.setVisibility(View.GONE);
		}else{
			mTitleDivide.setVisibility(View.VISIBLE);
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTitle.setText(title);
		}
		
		if(TextUtils.isEmpty(msg)){
			mTvMsg.setVisibility(View.GONE);
		}else{
			mTvMsg.setVisibility(View.VISIBLE);
			mTvMsg.setText(msg);
		}
		
		if(null != confirmListener){
			mBtnConfirm.setVisibility(View.VISIBLE);		
			mBtnConfirm.setOnClickListener(confirmListener);
		}else{
			mBtnConfirm.setVisibility(View.GONE);	
		}
		
		if(null != cancelListener){
			mBtnCancel.setVisibility(View.VISIBLE);		
			mBtnCancel.setOnClickListener(cancelListener);
		}else{
			mBtnCancel.setVisibility(View.GONE);	
		}
		if(!mContext.isFinishing())
			mDialog.show();
	}
	
	public final void showDialog(int title,int msg,
			OnClickListener confirmListener,
			OnClickListener cancelListener){
		if(title == -1){
			mTitleDivide.setVisibility(View.GONE);
			mTvTitle.setVisibility(View.GONE);
		}else{
			mTitleDivide.setVisibility(View.VISIBLE);
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTitle.setText(title);
		}
		
		if(msg == -1){
			mTvMsg.setVisibility(View.GONE);
		}else{
			mTvMsg.setVisibility(View.VISIBLE);
			mTvMsg.setText(msg);
		}
		
		if(null != confirmListener){
			mBtnConfirm.setVisibility(View.VISIBLE);		
			mBtnConfirm.setOnClickListener(confirmListener);
		}else{
			mBtnConfirm.setVisibility(View.GONE);	
		}
		if(null != cancelListener){
			mBtnCancel.setVisibility(View.VISIBLE);		
			mBtnCancel.setOnClickListener(cancelListener);
		}else{
			mBtnCancel.setVisibility(View.GONE);	
		}
		if(!mContext.isFinishing())
			mDialog.show();
	}



	public final void showDialog(String title,String msg,
			int confirmId,
			int cancelId,
			OnClickListener confirmListener,
			OnClickListener cancelListener){
		if(TextUtils.isEmpty(title)){
			mTitleDivide.setVisibility(View.GONE);
			mTvTitle.setVisibility(View.GONE);
		}else{
			mTitleDivide.setVisibility(View.VISIBLE);
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTitle.setText(title);
		}
		
		if(TextUtils.isEmpty(msg)){
			mTvMsg.setVisibility(View.GONE);
		}else{
			mTvMsg.setVisibility(View.VISIBLE);
			mTvMsg.setText(msg);
		}
		
		if(null != confirmListener){
			mBtnConfirm.setText(confirmId);
			mBtnConfirm.setVisibility(View.VISIBLE);		
			mBtnConfirm.setOnClickListener(confirmListener);
		}else{
			mBtnConfirm.setVisibility(View.GONE);	
		}
		
		if(null != cancelListener){
			mBtnCancel.setText(cancelId);
			mBtnCancel.setVisibility(View.VISIBLE);		
			mBtnCancel.setOnClickListener(cancelListener);
		}else{
			mBtnCancel.setVisibility(View.GONE);	
		}
		if(!mContext.isFinishing())
			mDialog.show();
	}

	public final void dimissDialog(){
		try {
			if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
