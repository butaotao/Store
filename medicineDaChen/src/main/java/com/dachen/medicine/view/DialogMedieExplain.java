package com.dachen.medicine.view;



import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.CompareData;

/**
 * Created by Burt on 2016/1/11.
 */
public class DialogMedieExplain implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Context context;
    Dialog dialog;   
    Button button1;  
    TextView tv_name;
    TextView tv_special;
    TextView tv_pack_special;
    TextView tv_prucuctname;
    TextView tv_gavemedieva;
    LinearLayout ll_zeng;
    TextView btn_cancel;
    String id = "";
    public DialogMedieExplain(Context con, final CdrugRecipeitem item ) {
        this.context = con;
        dialog = new Dialog(context, R.style.addresspickerstyle);
        View mView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_medicineexplain, null);
        button1 = (Button) mView.findViewById(R.id.btn_cancel);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tv_special = (TextView) mView.findViewById(R.id.tv_special);
        tv_prucuctname = (TextView) mView.findViewById(R.id.tv_prucuctname);
        tv_gavemedieva = (TextView) mView.findViewById(R.id.tv_gavemedieva);
        tv_special = (TextView) mView.findViewById(R.id.tv_special);
        tv_pack_special = (TextView) mView.findViewById(R.id.tv_pack_special);
        tv_name.setText(CompareData.getName(item.general_name, item.trade_name, null));
        ll_zeng = (LinearLayout) mView.findViewById(R.id.ll_zeng);
         tv_special.setText(item.specification);
        tv_pack_special.setText(item.pack_specification);
        tv_prucuctname.setText(item.manufacturer);
        String unit = "盒";
        if (item.unit!=null) {
			unit = item.unit.title;
		}
        int fen = 0;
        if (item.data!=null) {
			fen = item.data.zsmdwypxhjfs;
		}
        tv_gavemedieva.setText("使用"+fen+"积分可兑换一"+unit+"药");
        if ((item.is_join&&CompareData.isShow(item))||item.givePresent) {
            ll_zeng.setVisibility(View.VISIBLE);
		}else {
            ll_zeng.setVisibility(View.GONE);
		}
     
        button1.setOnClickListener(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
      
        float mWidth = con.getResources().getDisplayMetrics().widthPixels;
        float mHeight = con.getResources().getDisplayMetrics().heightPixels;
        float mDensity = con.getResources().getDisplayMetrics().density;
        dialog.setContentView(mView);
        dialog.getWindow().setGravity(Gravity.CENTER);
       WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
       lp.width = (int) (mWidth );
      
      
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

    @Override
    public void onClick(View v) {
    if (v.getId()==R.id.btn_cancel){ 
            dialog.dismiss();;
        } 
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	  dialog.dismiss();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
