package com.dachen.dgroupdoctorcompany.activity;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.HospitalManagerChildrenAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.AddSelfPartHospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalManager;
import com.dachen.dgroupdoctorcompany.entity.HospitalManagers;
import com.dachen.dgroupdoctorcompany.entity.HospitalMangerData;
import com.dachen.dgroupdoctorcompany.entity.MedicineManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

/**
 * Created by Burt on 2016/5/18.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HospitalManagerActivity extends BaseActivity implements HttpManager.OnHttpListener {
    ExpandableListView expandableListView;
    public ArrayList<HospitalMangerData> medicineManagers;
    MedicineManager managers = new MedicineManager();
    HospitalManagerChildrenAdapter adapter;
    ViewStub vstub_title;
    public static int totalNumHospital;
    public static int larSize = 50;
    public static int refresh = 1;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activityhospitalmanager);
        expandableListView = (ExpandableListView) findViewById(R.id.list);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        tv.setOnClickListener(this);
        tv.setText("新建");
        setTitle("分管品种及医院");
        enableBack();
        refresh = 1;

        medicineManagers = new ArrayList<>();

        //  Collections.sort(children.names, new PinyinComparator());


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh == 1) {
            showLoadingDialog();
            getRelationShip();
        }
        refresh = 0;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(this, SelfPartHospitalChoiceMedieActivity.class);
                intent.putExtra("","");
                startActivity(intent);
                refresh = 1;
                break;
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (response instanceof HospitalManager&&response.resultCode ==1) {
            HospitalManager manager = (HospitalManager) response;
            medicineManagers.clear();
            if (null != manager.data && null != manager.data.pageData) {
                ArrayList<HospitalMangerData> datas = manager.data.pageData;
                totalNumHospital = manager.data.total;
                medicineManagers.addAll(datas);
            }else {
                totalNumHospital = 0;
            }
            adapter = new HospitalManagerChildrenAdapter(this, medicineManagers, expandableListView);
            expandableListView.setAdapter(adapter);
            expandableListView.expandGroup(0);
            expandableListView.setGroupIndicator(null);
            expandableListView.setEmptyView(findViewById(R.id.rl_notcontent));
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    class PinyinComparator implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }//

    public void getRelationShip() {
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
        interfaces.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        new HttpManager().get(this,
                Constants.GETMANAGERHOSPITAL,
                HospitalManager.class,
                interfaces,
                this, false, 1);

    }
}
