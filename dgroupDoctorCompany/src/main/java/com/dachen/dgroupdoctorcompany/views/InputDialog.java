package com.dachen.dgroupdoctorcompany.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-06-26
 * Time: 15:34
 * FIXME
 */

public class InputDialog extends Dialog {
    private Context mContext;
    private Button mConfirm;
    private Button mCancel;
    private EditText mEditText;
    private OnCancelListener mOncancelListener;
    private OnConfirmListener mOnConfirmListener;
    private TextView mTvTitle;

    public InputDialog(Context context) {
        super(context, R.style.InputDialog);
        mContext = context;
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        this.setCanceledOnTouchOutside(false);
        mConfirm = (Button) findViewById(R.id.confirm);
        mCancel = (Button) findViewById(R.id.cancel);
        mEditText = (EditText) findViewById(R.id.edittext);
        mTvTitle = (TextView) findViewById(R.id.title);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm(InputDialog.this, mEditText.getText().toString());
                }
                InputDialog.this.dismiss();

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOncancelListener != null) {
                    mOncancelListener.onCancel(InputDialog.this);
                }
                InputDialog.this.dismiss();

            }
        });

    }

    public void setTitle(String title){

        mTvTitle.setText(title);
    }

    public void setEditTextHint(String textHint){

        mEditText.setHint(textHint);
    }

    public void setEditInputType(){
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void setOncancelListener(OnCancelListener listener) {
        mOncancelListener = listener;
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mOnConfirmListener = listener;
    }

    public interface OnCancelListener {
        void onCancel(Dialog dialog);
    }

    public interface OnConfirmListener {
        void onConfirm(Dialog dialog, String input);
    }

}
