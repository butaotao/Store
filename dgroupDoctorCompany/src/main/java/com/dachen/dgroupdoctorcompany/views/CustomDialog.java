package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import com.dachen.dgroupdoctorcompany.R;


public class CustomDialog extends Dialog {

    private CustomDialog(Context context, int theme) {
        super(context, theme);

    }
    public static class Builder implements android.view.View.OnClickListener {
        private Context context;
        private String title;
        private String message;
        private String positive;
        private String negative;
        private View contentView;
        private CustomClickEvent clickEvent;
        private CustomDialog dialog ;

        public Builder(Context context,CustomClickEvent clickEvent) {
            this.context = context;
            this.clickEvent=clickEvent;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositive(String positive) {
            this.positive = positive;
            return this;
        }

        public Builder setNegative(String negative) {
            this.negative = negative;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new CustomDialog(context, R.style.Dialog);
            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.comm_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.content)).setText(message);
            if(positive!=null && !positive.isEmpty()){
                ((TextView) layout.findViewById(R.id.sure)).setText(positive);
                ((TextView) layout.findViewById(R.id.sure)).setVisibility(View.VISIBLE);
                ((TextView)layout.findViewById(R.id.sure)).setOnClickListener(this);
            }
            if(negative!=null && !negative.isEmpty()){
                ((TextView) layout.findViewById(R.id.cancel)).setText(negative);
                ((TextView) layout.findViewById(R.id.cancel)).setVisibility(View.VISIBLE);
                ((TextView)layout.findViewById(R.id.cancel)).setOnClickListener(this);
            }
            if(title!=null && !title.isEmpty()){
                ((TextView) layout.findViewById(R.id.title)).setText(title);
                ((TextView) layout.findViewById(R.id.title)).setVisibility(View.VISIBLE);
            }
            setDialogAttribute();
            return dialog;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.sure:
                    if(clickEvent!=null){
                        clickEvent.onClick(dialog);
                    }
                    break;
                case R.id.cancel:
                    if(clickEvent!=null){
                        clickEvent.onDismiss(dialog);
                    }
                    break;
                default:
                    break;
            }

        }

        private void  setDialogAttribute(){
            Display d = ((Activity)context).getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
            Point point = new Point();
            d.getSize(point);
            p.width = (int)((float)point.x * 0.8f);
            //     p.height = (int)((float)point.y *0.4f);
            p.height=p.WRAP_CONTENT;
            dialog.getWindow().setAttributes(p);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }


    public  interface CustomClickEvent{

        void onClick(CustomDialog customDialog);
        void onDismiss(CustomDialog customDialog);
    }
}
