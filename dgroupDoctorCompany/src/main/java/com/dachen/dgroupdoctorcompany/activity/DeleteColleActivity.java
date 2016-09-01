package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    Button btn_setmanager;
    Button btn_setrepresent;
    private String mPosition;
    public int isManager;
    public int isPresent;
    RelativeLayout rl_editname;
    RelativeLayout rl_editdept;
    RelativeLayout rl_position;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(DeleteColleActivity.this, ManagerColleagueActivity.class);
            intent.putExtra("position", mPosition);
            setResult(1001, intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletecolle);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        btn_setmanager = (Button) findViewById(R.id.btn_setmanager);
        btn_setrepresent = (Button) findViewById(R.id.btn_setrepresent);
        btn_setmanager.setOnClickListener(this);
        btn_setrepresent.setOnClickListener(this);

        rl_editname = (RelativeLayout) findViewById(R.id.rl_editname);
        rl_editname.setOnClickListener(this);

        rl_editdept = (RelativeLayout) findViewById(R.id.rl_editdept);
        rl_editdept.setOnClickListener(this);
        //GetAllDoctor.changeContact.clear();
        rl_position = (RelativeLayout) findViewById(R.id.rl_position);
        rl_position.setOnClickListener(this);
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
         findViewById(R.id.rl_editdept).setOnClickListener(this);
        isManager =0;
        isPresent =0;
        companyContactDao = new CompanyContactDao(DeleteColleActivity.this);
        enableBack();
        showBtn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GetAllDoctor.changeContact!=null&&GetAllDoctor.changeContact.size()>0){
                Iterator<CompanyContactListEntity> entityIterator =GetAllDoctor.changeContact.iterator();
                while (entityIterator.hasNext()){
                    CompanyContactListEntity entitye =entityIterator.next();
                    if ((entity.userId+"").equals(entitye.userId)){
                        tv_namedes.setText(entitye.name);
                        tv_departdes.setText(entitye.department);
                        tv_positiondes.setText(entitye.position);
                        tv_phonedes.setText(entitye.telephone);
                        entity = entitye;
                        break;
                    }
                }
        }

    }

    public void showBtn(){
        String userid = SharedPreferenceUtil.getString(this,"id","");
        if (userid.equals(entity.userId)||showControlManager()){
            btn_delete.setBackgroundColor(getResources().getColor(R.color.red_88f95442));
            btn_delete.setFocusable(false);
            btn_delete.setClickable(false);
            btn_setrepresent.setBackgroundColor(getResources().getColor(R.color.color_8839cf78));
            btn_setmanager.setBackgroundColor(getResources().getColor(R.color.color_9ddcff));
            btn_setrepresent.setFocusable(false);
            btn_setrepresent.setClickable(false);
            btn_setmanager.setFocusable(false);
            btn_setmanager.setClickable(false);
        }else {
            btn_delete.setBackgroundColor(getResources().getColor(R.color.red_f95442));
            btn_delete.setFocusable(true);
            btn_delete.setClickable(true);

            if (entity.ispresent ==2){
               /* btn_setmanager.setBackgroundColor(getResources().getColor(R.color.color_9ddcff));
                btn_setmanager.setFocusable(false);
                btn_setmanager.setClickable(false);*/
                btn_setrepresent.setText("设置为医药代表");
            }else {
                btn_setrepresent.setText("取消医药代表");
               /* btn_setmanager.setBackgroundColor(getResources().getColor(R.color.color_39cf78));
                btn_setmanager.setFocusable(true);
                btn_setmanager.setClickable(true);*/
            }
            if (entity.deptManager==2){
                btn_setmanager.setText("设置为部门主管");
               /* btn_setrepresent.setBackgroundColor(getResources().getColor(R.color.color_8839cf78));
                btn_setrepresent.setFocusable(false);
                btn_setrepresent.setClickable(false);*/
            }else {
                btn_setmanager.setText("取消部门主管角色");
                /*btn_setrepresent.setBackgroundColor(getResources().getColor(R.color.color_39cf78));
                btn_setrepresent.setFocusable(true);
                btn_setrepresent.setClickable(true);*/
            }
        }

    }
    private boolean showControlManager(){
        if (entity!=null){
            if (entity.deptManager==1){
                String deptid = entity.id;
                String loginUserId = companyContactDao.queryByUserid(SharedPreferenceUtil.getString(this,"id","")).id;
                if(loginUserId.equals(deptid)){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_delete:
                showCustomerDialog();
                break;
            case R.id.btn_setmanager:
                showCustomerDialogSetManager();
                break;
            case R.id.btn_setrepresent:
                showCustomerDialogSetPresent();
                break;
            case R.id.rl_editdept:
                intent  = new Intent(this,EditColleageDepartmentActivity.class);
                intent.putExtra("userid",entity.userId);
                intent.putExtra("user",entity);
                startActivity(intent);
                break;
            case R.id.rl_editname:
                intent.setClass(DeleteColleActivity.this, UpdateUserInfoActivity.class);
                intent.putExtra("name", entity.name);
                intent.putExtra("id",entity.userId);
                intent.putExtra("entity",entity);
                intent.putExtra("mode",UpdateUserInfoActivity.MODE_UPDATE_NAME_MANAGER);
                startActivity(intent);
                break;
            case R.id.rl_position:
                intent.setClass(DeleteColleActivity.this, UpdateUserInfoActivity.class);
                intent.putExtra("mode", UpdateUserInfoActivity.MODE_UPDATE_JOB_TITLE_MANAGER);//
                intent.putExtra("job_title",entity.position);
                intent.putExtra("part",entity.id);
                intent.putExtra("entity",entity);
                intent.putExtra("id",entity.userId);
                startActivity(intent);
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
        new HttpManager().post(this, Constants.DRUG + "companyUser/quitMajorUser", CompanyDepment.class,
                maps, this,
                false, 1);
    }
    //1为是，2位否
    private void mangagerSet(int isManager) {
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        //组织id
        maps.put("orgId",entity.id);//orgId
        maps.put("userId",entity.userId);
        maps.put("employeeId",entity.employeeId);
        maps.put("isDeptManager",isManager+"");
        new HttpManager().post(this, Constants.DRUG + "companyUser/updateMajorUser", CompanyDepment.class,
                maps, this,
                false, 1);
    }
    //1为是，2位否
    private void representSet(int drugSales) {
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        //组织id
        maps.put("orgId",entity.id);//orgId
        maps.put("userId",entity.userId);
        maps.put("employeeId",entity.employeeId);
        maps.put("isDrugSales",drugSales+"");
        new HttpManager().post(this, Constants.DRUG + "companyUser/updateMajorUser", CompanyDepment.class,
                maps, this,
                false, 1);
    }
    public void showCustomerDialog(){
        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("温馨提示", "若将该员工离职，TA将无法登录本企业所有应用。您确定要将其离职？", R.string.cancel, R.string.sure, new View.OnClickListener() {
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
    public void showCustomerDialogSetManager(){
        String showmessage = "";
       final int addOrCancel = entity.deptManager;
        //1位加，2位取消
        if (addOrCancel==1){
            showmessage = "确定取消当前员工的主管权限?";
        }else if (addOrCancel ==2 ){
            showmessage = "确定将当前员工设置为当前部门的主管?";
        }
        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("温馨提示", showmessage,R.string.cancel,R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                int num = 1;
                if (addOrCancel==1){
                    num = 2;
                }else {
                    num = 1;
                }
                mangagerSet(num);
            }
        });
    }
    public void showCustomerDialogSetPresent(){
        String showmessage = "";
        final int addOrCancel = entity.ispresent;
        //1位加，2位取消
        if (addOrCancel==1){
            showmessage = "确定取消当前员工的医药代表角色?";
        }else if (addOrCancel ==2 ){
            showmessage = "确定将当前员工设置为医药代表角色?";
        }

        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("温馨提示", showmessage,R.string.cancel,R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                int num = 1;
                if (addOrCancel==1){
                    num = 2;
                }else {
                    num = 1;
                }
                representSet(num);
            }
        });
    }
    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response) {
            if (response.resultCode == 1) {

                GetAllDoctor.getInstance().getPeople(this, handler);

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
