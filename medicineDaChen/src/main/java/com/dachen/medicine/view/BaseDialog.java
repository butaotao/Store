package com.dachen.medicine.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dachen.medicine.R;

/**
 * Created by Burt on 2016/7/8.
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    public TextView tvTitle;
    private FrameLayout mContent;
    private Button btn1;
    private Button btn2;
    public View view_line;
    private View.OnClickListener mBtn1ClickListener;
    private View.OnClickListener mBtn2ClickListener;

    private boolean isAutoDismiss1 = true;
    private boolean isAutoDismiss2 = true;

    /**
     * @param context
     */
    public BaseDialog(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
    }

    /**
     * init method
     */
    private void init() {

        this.getContext().setTheme(android.R.style.Theme_InputMethod);
        super.setContentView(R.layout.layout_common_dialog);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        mContent = (FrameLayout) findViewById(R.id.fl_content);
        view_line = (View) findViewById(R.id.view_line);
        btn1 = (Button) findViewById(R.id.btn_confirm);
        btn2 = (Button) findViewById(R.id.btn_cancel);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        Window window  = getWindow();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributesParams.dimAmount = 0.4f;

        @SuppressWarnings("deprecation")
        int width = (int) (window.getWindowManager().getDefaultDisplay().getWidth()*0.85);
        window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * set title
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * set title
     * @param resId
     * @see Dialog#setTitle(int)
     */
    public void setTitle(int resId) {
        setTitle(getContext().getResources().getString(resId));
    }

    /**
     * bulid the contentview of the dialog.
     * @return
     */
    public abstract View createContentView();


    @SuppressWarnings("deprecation")
    public void setContentView(View view) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        view.setLayoutParams(lp);
        mContent.addView(view);
    }

    /**
     * set the text of the btn1
     * @param text
     */
    public void setBtn1Text(String text) {
        btn1.setText(text);
    }

    /**
     * set the text of the btn2
     * @param text
     */
    public void setBtn2Text(String text) {
        btn2.setText(text);
    }

    /**
     * set the text of the btn1
     * @param resId
     */
    public void setBtn1Text(int resId) {
        btn1.setText(resId);
    }

    /**
     * set the text of the btn2
     * @param resId
     */
    public void setBtn2Text(int resId) {
        btn2.setText(resId);
    }

    public void setBottomLayoutGone(){
        findViewById(R.id.linearlayout_bottom_btn).setVisibility(View.GONE);
    }

    /**
     * 设置点击按钮1后时候是否关闭dialog
     * 默认为true
     * @param autoDismiss
     */
    public void setAutoDismiss1(boolean autoDismiss) {
        isAutoDismiss1 = autoDismiss;
    }

    /**
     * 设置点击按钮2后时候是否关闭dialog
     * 默认为true
     * @param autoDismiss
     */
    public void setAutoDismiss2(boolean autoDismiss) {
        isAutoDismiss2 = autoDismiss;
    }

    /**
     * 设置按钮1是否可见
     * @param visible
     */
    public void setBtn1Visible(boolean visible) {
        if (visible) {
            btn1.setVisibility(View.VISIBLE);
        } else {
            btn1.setVisibility(View.GONE);
        }
    }

    /**
     * 设置按钮2是否可见
     * @param visible
     */
    public void setBtn2Visible(boolean visible) {
        if (visible) {
            btn2.setVisibility(View.VISIBLE);
        } else {
            btn2.setVisibility(View.GONE);
        }
    }

    /**
     *
     *设置按钮1是否处于可点击状态
     * @param enable
     */
    public void setBtn1Enable(boolean enabled){
        btn1.setEnabled(enabled);
    }

    /**
     *
     *设置按钮2是否处于可点击状态
     * @param enable
     */
    public void setBtn2Enable(boolean enabled){
        btn2.setEnabled(enabled);
    }

    /**
     * 设置1按钮的监听
     * @param listener
     */
    public void setBtn1ClickListener(View.OnClickListener listener) {
        mBtn1ClickListener = listener;
    }

    /**
     * 设置2按钮的监听
     * @param listener
     */
    public void setBtn2ClickListener(View.OnClickListener listener) {
        mBtn2ClickListener = listener;
    }


    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id== R.id.btn_confirm){
            if (mBtn1ClickListener != null) {
                mBtn1ClickListener.onClick(v);
            }
            if (isAutoDismiss1) {
                dismiss();
            }
        }else if(id==R.id.btn_cancel){
            if (mBtn2ClickListener != null ) {
                mBtn2ClickListener.onClick(v);
            }
            if (isAutoDismiss2) {
                dismiss();
            }
        }
    }

}