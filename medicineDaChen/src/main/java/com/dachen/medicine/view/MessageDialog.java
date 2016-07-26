package com.dachen.medicine.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dachen.medicine.R;

/**
 * Created by Burt on 2016/7/8.
 */
public class MessageDialog extends BaseDialog {

    /** TextView对象 **/
    private TextView tv_message;
    /** View对象 **/
    private View contentView;
    /** 能否取消 true表示不能取消，false表示可以取消 **/
    private boolean canNotCancel = false;


    /**
     * 可以取消、默认确定和取消、默认标题的MessageDialog
     * @param context
     * @param message
     */
    public MessageDialog(Context context, String message) {
        this(context, null, context.getString(R.string.common__confirm), context.getString(R.string.common__cancel), message, false);
    }

    /**
     * 可以取消、默认确定和取消的MessageDialog
     * @param context
     * @param title
     * @param message
     */
    public MessageDialog(Context context, String title, String message) {
        this(context, title, context.getString(R.string.common__confirm), null, message, false);
    }

    /**
     * 可以取消的MessageDialog
     * @param context
     * @param
     * @param btnText1
     * @param btnText2
     * @param message
     */
    public MessageDialog(Context context, String btnText1, String btnText2, String message) {
        this(context, null, btnText1, btnText2, message, false);
    }

    /**
     * 可以取消的MessageDialog
     * @param context
     * @param title
     * @param btnText1
     * @param btnText2
     * @param message
     */
    public MessageDialog(Context context, String title, String btnText1, String btnText2, String message) {
        this(context, title, btnText1, btnText2, message, false);
    }
    /**
     * 构造方法
     * @param context
     * @param title
     * @param btnText1
     * @param btnText2
     * @param message
     * @param canNotCancel
     */
    public MessageDialog(Context context, String title, String btnText1, String btnText2, String message, boolean canNotCancel) {
        super(context);
        this.canNotCancel = canNotCancel;
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_dialog_message, null);
        tv_message = (TextView) contentView.findViewById(R.id.tv_message);
        view_line.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            this.setTitle(title);
        }

        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }

        if(!TextUtils.isEmpty(btnText1)){
            this.setBtn1Text(btnText1);
        }

        if(TextUtils.isEmpty(btnText2)){
            this.setBtn2Visible(false);
        } else {
            this.setBtn2Visible(true);
            this.setBtn2Text(btnText2);
        }

    }

    @Override
    public View createContentView() {
        return contentView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (canNotCancel) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置提示消息
     * @param text 消息文本
     */
    @SuppressWarnings("unused")
    private void setMessage(String text) {
        setMessage(text, false);
    }

    /**
     * 设置提示消息
     * @param text 消息文本
     * @param isHtml 是否Html显示
     */
    private void setMessage(String text, boolean isHtml) {
        if (isHtml) {
            tv_message.setText(Html.fromHtml(text));
        } else {
            tv_message.setText(text);
        }
    }
}