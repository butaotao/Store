package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 小编辑视图
 * 
 * @author WANG
 *
 */
public class SmallEditViewUI extends BaseActivity {

	private static final String TAG = SmallEditViewUI.class.getSimpleName();

	public static final int observer_what_change_text = 111;

	public static final String key_title = "key_title";
	public static final String key_text = "key_text";
	public static final String key_inputtype = "key_inputtype";
	public static final String key_max = "key_max";

	protected Button ui_my_introduce_back; // 返回按钮

	protected EditText ui_my_introduce_editText; // 填写内容
	protected Button ui_my_introduce_button_ok; // 确定按钮

	protected TextView ui_my_introduce_title; // 标题
	protected TextView tvNumHint; // 标题

	protected String title = null; // 标题
	protected String text = null; // 填写内容
	protected int inputtype = InputType.TYPE_CLASS_TEXT; // 文本框的输入类型
	protected int maxNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_small_editview);
		ButterKnife.bind(this);

		ui_my_introduce_back = (Button)findViewById(R.id.back_btn);
		ui_my_introduce_title = (TextView)findViewById(R.id.title);
		ui_my_introduce_title = (TextView)findViewById(R.id.title);
		tvNumHint = (TextView)findViewById(R.id.tv_num_hint);

		ui_my_introduce_editText = (EditText)findViewById(R.id.ui_my_introduce_editText);
		ui_my_introduce_button_ok = (Button)findViewById(R.id.ui_my_introduce_button_ok);

		ui_my_introduce_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		ui_my_introduce_button_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Override_onClick(v);
			}

		});

		init();

	}

	/**
	 * 初始化
	 */
	protected void init() {

		Intent i = this.getIntent();
		this.title = i.getStringExtra(key_title);
		this.text = i.getStringExtra(key_text);
		this.inputtype = i.getIntExtra(key_inputtype,0);
		maxNum=i.getIntExtra(key_max,0);
		// 输入类型
		ui_my_introduce_editText.setInputType(inputtype);
		if(maxNum>0){
            ui_my_introduce_editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)} );
            ui_my_introduce_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    refreshNumHint(s.length());
                }
            });
		}else{
			tvNumHint.setVisibility(View.GONE);
		}
        ui_my_introduce_title.setText(title);
        setText(this.text);
	}

    private void refreshNumHint(int num){
        tvNumHint.setText(num+"/"+maxNum);
    }

	protected void Override_onClick(View v) {
		getText();
		Intent i=new Intent();
		i.putExtra(key_text,text);
		setResult(RESULT_OK, i);
		finish();
	}


	/**
	 * 设置内容
	 * 
	 * @param text
	 */
	protected void setText(String text) {
		this.text = text;
		ui_my_introduce_editText.setText(text);
		ui_my_introduce_editText.setSelection(ui_my_introduce_editText.getText().length());
	}
	
	/**
	 * 得到内容
	 * 
	 * @return
	 */
	protected String getText() {
		this.text = ui_my_introduce_editText.getText().toString();
		return this.text;
	}

	/**
	 * 打开界面
	 * 
	 * @param title
	 * @param text
	 */
	public static void openUI(Activity act,int maxNum, String title, String text, int InputType,int requestCode) {
		Intent i = new Intent(act, SmallEditViewUI.class);
//		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(key_max, maxNum);
		i.putExtra(key_title, title);
		i.putExtra(key_text, text);
		i.putExtra(key_inputtype, InputType);
		act.startActivityForResult(i,requestCode);
	}

}
