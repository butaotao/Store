package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/5/25.
 */
public class DeleteColleActivity extends BaseActivity implements View.OnClickListener,HttpManager.OnHttpListener{
    TextView tv_namedes;
    TextView tv_departdes;
    TextView tv_positiondes;
    TextView tv_phonedes;
    CompanyContactListEntity entity;
    CompanyContactDao companyContactDao;
    Button btn_delete;
    private String mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletecolle);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        setTitle("编辑员工");
        tv_namedes = (TextView) findViewById(R.id.tv_namedes);
        tv_departdes = (TextView) findViewById(R.id.tv_departdes);
        tv_positiondes = (TextView) findViewById(R.id.tv_positiondes);
        tv_phonedes = (TextView) findViewById(R.id.tv_phonedes);
        Bundle bundle = getIntent().getBundleExtra("colleage");
        mPosition = getIntent().getStringExtra("position");
        entity = (CompanyContactListEntity) bundle.getSerializable("colleage");
        tv_namedes.setText(entity.name);
        tv_departdes.setText(entity.department);
        tv_positiondes.setText(entity.position);
        tv_phonedes.setText(entity.telephone);
        companyContactDao = new CompanyContactDao(DeleteColleActivity.this);
        enableBack();
        showBtn();

    }
    public void showBtn(){
        String userid = SharedPreferenceUtil.getString(this,"id","");
        if (userid.equals(entity.userId)){
            btn_delete.setBackgroundColor(getResources().getColor(R.color.red_88f95442));
            btn_delete.setFocusable(false);
            btn_delete.setClickable(false);
        }else {
            btn_delete.setBackgroundColor(getResources().getColor(R.color.red_f95442));
            btn_delete.setFocusable(true);
            btn_delete.setClickable(true);
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_delete:
                showCustomerDialog();
                break;
        }
    }
    private void deletePeople() {
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        //组织id
        maps.put("orgId",entity.id);//orgId
        maps.put("userId",entity.userId);
        new HttpManager().post(this, "org/drugCompany/dept/departEmployee", CompanyDepment.class,
                maps, this,
                false, 1);
    }
    public void showCustomerDialog(){
        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("温馨提示", "若将该员工离职，TA将无法登录本企业所有应用。您确定要将其离职？",R.string.cancel,R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                deletePeople();
            }
        });
    }
    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response) {
            if (response.resultCode == 1) {
                companyContactDao.deleteByid(entity.userId);
                Intent intent = new Intent(this, EidtColleagueActivity.class);
                intent.putExtra("position", mPosition);
                setResult(1001, intent);
                finish();
            } else {
                if (!TextUtils.isEmpty(response.resultMsg)) {
                    ToastUtil.showToast(this, response.resultMsg);
                } else {
                    ToastUtil.showToast(this, "离职该员工失败");
                }
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
