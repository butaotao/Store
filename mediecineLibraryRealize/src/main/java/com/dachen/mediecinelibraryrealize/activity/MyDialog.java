package com.dachen.mediecinelibraryrealize.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;

/**
 * 自定义dialog
 * 
 * @author sfshine
 * 
 */
public class MyDialog {
	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	Button sure;
	TextView textView;
	EditText editText;
	TextView tv_title;
	/**
	 * init the dialog
	 * 
	 * @return
	 */
	public MyDialog(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.addresspickerstyle);
		// dialog.setContentView(R.layout.dialog);
		View mView = LayoutInflater.from(context)
				.inflate(R.layout.dialog, null);
		textView = (TextView) mView.findViewById(R.id.textview);
		sure = (Button) mView.findViewById(R.id.button1);
		editText = (EditText) mView.findViewById(R.id.editText1);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		float mWidth = con.getResources().getDisplayMetrics().widthPixels;
		float mHeight = con.getResources().getDisplayMetrics().heightPixels;
		float mDensity = con.getResources().getDisplayMetrics().density;
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.translate_window);
		dialog.setContentView(mView);
		dialog.getWindow().setGravity(Gravity.CENTER);
		tv_title = (TextView) mView.findViewById(R.id.tv_title);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (mWidth );
		lp.height =(int)( mHeight);
		dialog.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
			  dialogcallback.dialogdo(editText.getText().toString());
				// dialogcallback.dialogdo(");
				dismiss();
			}
		});
		dialog.findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialogcallback.dialogdo("忽略数量");
						dismiss();
					}
				});
	}
	public void setTitle(String title){
		tv_title.setText(title);
	}
	/**
	 * 设定一个interfack接口,使mydialog可以處理activity定義的事情
	 * 
	 * @author sfshine
	 * 
	 */
	public interface Dialogcallback {
		public void dialogdo(String string);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}

	/**
	 * @category Set The Content of the TextView
	 * */
	public void setContent(String content) {
		textView.setText(content);
	}

	/**
	 * Get the Text of the EditText
	 * */
	public String getText() {
		return editText.getText().toString();
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}