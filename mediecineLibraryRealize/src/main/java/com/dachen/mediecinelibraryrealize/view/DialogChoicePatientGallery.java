package com.dachen.mediecinelibraryrealize.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;

import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.CollectionMedieActivity;
import com.dachen.mediecinelibraryrealize.activity.PatientMedieBoxActivity2;
import com.dachen.mediecinelibraryrealize.adapter.ImageGalleryAdapter;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.PatientMedieBoxs;
import com.dachen.mediecinelibraryrealize.entity.Patients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/1/11.
 */
public class DialogChoicePatientGallery implements View.OnClickListener,AdapterView.OnItemSelectedListener,HttpManager.OnHttpListener {
    Context context;
    Dialog dialog;
    ImageGalleryAdapter adapter;
    List<Patients.patient> patients;
    Gallery myGallery;
    Button button1;
    Button button2;
    CollectionMedieActivity.RefreshActivity refreshActivity;
    int position;
    public DialogChoicePatientGallery(Context con,List<Patients.patient> patients,CollectionMedieActivity.RefreshActivity refreshActivity) {
        this.context = con;
        dialog = new Dialog(context, R.style.addresspickerstyle);
        View mView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_choicepatient, null);
        this.refreshActivity = refreshActivity;
        button1 = (Button) mView.findViewById(R.id.button1);
        button2 = (Button) mView.findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        myGallery = (Gallery) mView.findViewById(R.id.myGallery);
        float mWidth = con.getResources().getDisplayMetrics().widthPixels;
        float mHeight = con.getResources().getDisplayMetrics().heightPixels;
        float mDensity = con.getResources().getDisplayMetrics().density;
        dialog.setContentView(mView);
        dialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (mWidth );
        lp.height = (int) mHeight;
        this.patients = patients;
        adapter  = new ImageGalleryAdapter(context,patients);
        myGallery.setAdapter(adapter);
        //myGallery.setOnItemClickListener(new OnItemClickListenerImpl());
        myGallery.setOnItemSelectedListener(this);
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
    if (v.getId()==R.id.button1){


        refreshData(patients.get(position).id);
        refreshActivity.refresh(patients.get(position));
            dialog.dismiss();;
        }
        if (R.id.button2 == v.getId()){
            dialog.dismiss();;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectItem(position);
        this.position = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void refreshData(String id){
        new HttpManager().get(context,
                Params.getInterface("invoke", "c_Recipe.query?__ORDER_BY__=state,created_time%20desc&"),
                PatientMedieBoxs.class,
                Params.getPatientInfoByID(id),
                this, false, 2);
    }

    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
