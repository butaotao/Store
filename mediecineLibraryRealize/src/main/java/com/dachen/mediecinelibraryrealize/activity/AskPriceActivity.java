package com.dachen.mediecinelibraryrealize.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.DrugPriceAdapter;
import com.dachen.mediecinelibraryrealize.entity.SomeBox;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/3/26.
 */
public class AskPriceActivity extends BaseActivity implements View.OnClickListener,OnHttpListener {
    private RelativeLayout    rl_back;
    private TextView          tv_title;
    private Button            mBtnSubmit;
    private ListView          mLvDrug;
    private TextView          mTvSuggest;
    private String            doctorAndGroupName;
    private String            recipe_id;
    private String            patient;
    ArrayList<SomeBox.patientSuggest> c_patient_drug_suggest_list = new ArrayList<SomeBox.patientSuggest>();
    DrugPriceAdapter          mDrugPriceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_price);
        initView();
        initData();
    }

    private void initView(){
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mLvDrug = (ListView) findViewById(R.id.lvDrug);
        mTvSuggest = (TextView) findViewById(R.id.tvSuggest);
        rl_back.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mLvDrug.setEmptyView(findViewById(R.id.empty_container));
        mDrugPriceAdapter = new DrugPriceAdapter(this,c_patient_drug_suggest_list);
        mLvDrug.setAdapter(mDrugPriceAdapter);
    }

    private void initData(){
        tv_title.setText("下单");
        doctorAndGroupName = getIntent().getStringExtra("doctorAndGroupName");
        recipe_id = getIntent().getStringExtra("recipe_id");
        patient = getIntent().getStringExtra("patient");
        if(!TextUtils.isEmpty(doctorAndGroupName)){
            mTvSuggest.setText("["+doctorAndGroupName+"]"+"开具的用药建议");
        }else if(!TextUtils.isEmpty(patient)){
            mTvSuggest.setText(patient+"自建药单");
        }else{
            mTvSuggest.setText("用药建议");
        }
        showLoadingDialog();
        new HttpManager().get(this,
                Params.getInterface("invoke", "c_Recipe.get_patient_recipe_item?"),
                SomeBox.class,
                Params.getMedicineList(recipe_id, ""),
                this, false, 2);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rl_back){
            finish();
        }else if(v.getId() == R.id.btnSubmit){
            ToastUtils.showToast(AskPriceActivity.this,"下单成功");
            MActivityManager.getInstance().finishActivity(BaiduMapDesActivity.class);
            finish();
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if(null != response){
            SomeBox box = (SomeBox)response;
            c_patient_drug_suggest_list = box.c_patient_drug_suggest_list;
            if(null != c_patient_drug_suggest_list && c_patient_drug_suggest_list.size()>0){
                mDrugPriceAdapter = new DrugPriceAdapter(this,c_patient_drug_suggest_list);
                mLvDrug.setAdapter(mDrugPriceAdapter);
                mDrugPriceAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();

    }
}
