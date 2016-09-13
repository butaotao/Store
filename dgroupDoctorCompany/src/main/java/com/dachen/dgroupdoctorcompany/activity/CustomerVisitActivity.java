package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by weiwei on 2016/5/17.
 */
public class CustomerVisitActivity extends BaseActivity implements HttpManager.OnHttpListener, GaoDeMapUtils.LocationListener {
    public static final int REQUEST_SELECT_DOCTOR = 101;
    public static final int REQUEST_SELECT_ADDRESS = 102;

    public static final int MODE_FROM_SIGN = 1;//从地图列表选择页过来
    public static final int MODE_FROM_VIST_LIST = 2;//从拜访列表页过来
    private int mMode;

    private TextView mTvWeek;
    private TextView mTvDate;
    private TextView mTvSelected;
    private TextView mTvTime;
    private TextView mTvFloor;
    private TextView mTvAddress;
    private EditText mEtRemark;
    private Button mBtSubmit;
    private Button mBtSave;

    private double latitude;//纬度
    private double longitude;//经度
    private String city;//城市

    private long lastClickTime;
    private String mStrDoctorID;
    private String mStrDoctorName;
    private String mStrFloor;
    private String mStrAddress;
    private String mId;
    private String coordinate;
    private String state = "0";//0草稿，1提交

    private GaoDeMapUtils mGaoDeMapUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visit);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        mTvWeek = (TextView) findViewById(R.id.tvWeek);
        mTvDate = (TextView) findViewById(R.id.tvDate);
        mTvSelected = (TextView) findViewById(R.id.tvSelected);
        mTvTime = (TextView) findViewById(R.id.tvTime);
        mTvFloor = (TextView) findViewById(R.id.tvFloor);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mEtRemark = (EditText) findViewById(R.id.etRemark);
        mBtSubmit = (Button) findViewById(R.id.btSubmit);
        mBtSave = (Button) findViewById(R.id.btSave);

        findViewById(R.id.vSelect).setOnClickListener(this);
        findViewById(R.id.vSelectAddress).setOnClickListener(this);
        mBtSubmit.setOnClickListener(this);
        mBtSave.setOnClickListener(this);
    }

    private void initData() {
        mGaoDeMapUtils = new GaoDeMapUtils(this.getApplicationContext(), this);
        lastClickTime = System.currentTimeMillis();
        mMode = this.getIntent().getIntExtra("mode", MODE_FROM_VIST_LIST);
        mId = this.getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(mId)) {
            setTitle("新建拜访");
            setClickAble(true);
            Date date = new Date();
            String strDate = TimeFormatUtils.china_format_date(date);
            String strWeek = TimeFormatUtils.week_format_date(date);
            String strTime = TimeFormatUtils.time_format_date(date);
            mTvWeek.setText(strWeek);
            mTvDate.setText(strDate);
            mTvTime.setText(strTime);
            if (MODE_FROM_SIGN == mMode) {
                mStrAddress = this.getIntent().getStringExtra("addressname");
                mStrFloor = this.getIntent().getStringExtra("address");
                latitude = this.getIntent().getDoubleExtra("latitude", 0);
                longitude = this.getIntent().getDoubleExtra("longitude", 0);
                coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
                mTvFloor.setText(mStrFloor);
                mTvAddress.setText(mStrAddress);
            } else if (MODE_FROM_VIST_LIST == mMode) {
                initLocation();
            }

        } else {
            setTitle("拜访详情");
            setClickAble(false);
            mTvSelected.setTextColor(getResources().getColor(R.color.color_333333));
            findViewById(R.id.ivGo).setVisibility(View.INVISIBLE);
            findViewById(R.id.ivAddressFlag).setVisibility(View.INVISIBLE);
            long time = this.getIntent().getLongExtra("time", 0);
            Date date = new Date(time);
            String strDate = TimeFormatUtils.china_format_date(date);
            String strWeek = TimeFormatUtils.week_format_date(date);
            String strTime = TimeFormatUtils.time_format_date(date);
            mTvWeek.setText(strWeek);
            mTvDate.setText(strDate);
            mTvTime.setText(strTime);

            mStrDoctorID = this.getIntent().getStringExtra("doctorid");
            mStrDoctorName = this.getIntent().getStringExtra("doctorname");
            String addressName = this.getIntent().getStringExtra("addressName");
            String address = this.getIntent().getStringExtra("address");
            coordinate = this.getIntent().getStringExtra("coordinate");
            String remark = this.getIntent().getStringExtra("remark");
            mTvFloor.setText(addressName);
            mTvAddress.setText(address);
            mTvSelected.setText(mStrDoctorName);
            mTvSelected.setTextColor(getResources().getColor(R.color.color_333333));
            mEtRemark.setText(remark);

            String state = this.getIntent().getStringExtra("state");
            if ("1".equals(state)) {
                mEtRemark.setFocusable(false);
                mEtRemark.setEnabled(false);
                findViewById(R.id.vBottom).setVisibility(View.GONE);

                if (TextUtils.isEmpty(remark)) {
                    mEtRemark.setText("无");
                } else {
                    mEtRemark.setText(remark);
                }
            }
        }

    }

    private void setClickAble(boolean clickAble) {
        findViewById(R.id.vSelect).setClickable(clickAble);
        findViewById(R.id.vSelectAddress).setClickable(clickAble);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (CommonUitls.isFastDoubleClick(lastClickTime)) {
            lastClickTime = System.currentTimeMillis();
            return;
        }
        switch (v.getId()) {
            case R.id.vSelect:
                Intent intent = new Intent(CustomerVisitActivity.this, ChoiceDoctorForChatActivity.class);
                intent.putExtra("where", "AddSignInActivity");
                startActivityForResult(intent, REQUEST_SELECT_DOCTOR);
                break;
            case R.id.vSelectAddress:
                Intent intentAddress = new Intent(CustomerVisitActivity.this, SelectAddressActivity.class);
                intentAddress.putExtra("select_mode", SelectAddressActivity.MODE_SELECT_ADDRESS);
                intentAddress.putExtra("poi", SelectAddressActivity.POI);
                intentAddress.putExtra("distance", "250");
                intentAddress.putExtra("latitude", latitude);
                intentAddress.putExtra("longitude", longitude);
                intentAddress.putExtra("city", city);
                startActivityForResult(intentAddress, REQUEST_SELECT_ADDRESS);
                break;
            case R.id.btSubmit:
                state = "1";
                mBtSubmit.setClickable(false);
                onSubmit();
                break;
            case R.id.btSave:
                state = "0";
                mBtSave.setClickable(false);
                onSubmit();
                break;
        }
    }

    private void onSubmit() {
        String remark = mEtRemark.getText().toString();
        String address = mTvAddress.getText().toString();
        String addressName = mTvFloor.getText().toString();

        if (TextUtils.isEmpty(mStrDoctorName) && TextUtils.isEmpty(mStrDoctorID)) {
            ToastUtil.showToast(CustomerVisitActivity.this, "请选择客户");
            mBtSubmit.setClickable(true);
            mBtSave.setClickable(true);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showToast(CustomerVisitActivity.this, "请选择拜访地点");
            mBtSubmit.setClickable(true);
            mBtSave.setClickable(true);
            return;
        }
        if (TextUtils.isEmpty(addressName)) {
            ToastUtil.showToast(CustomerVisitActivity.this, "请选择拜访地点");
            mBtSubmit.setClickable(true);
            mBtSave.setClickable(true);
            return;
        }
        showLoadingDialog();
        new HttpManager().post(this, Constants.CREATE_OR_UPDATA_VISIT, Result.class,
                Params.getVisitParams(CustomerVisitActivity.this, addressName, state, mStrDoctorID, mStrDoctorName,
                        remark, mId, coordinate, address),
                this, false, 4);
    }

    private void initLocation() {
        mGaoDeMapUtils.startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_DOCTOR && resultCode == RESULT_OK && null != data) {
            mStrDoctorID = data.getStringExtra("doctorid");
            mStrDoctorName = data.getStringExtra("doctorname");
            if (!TextUtils.isEmpty(mStrDoctorName)) {
                mTvSelected.setText(mStrDoctorName);
            }
        } else if (requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK && null != data) {
            mStrFloor = data.getStringExtra("floor");
            mStrAddress = data.getStringExtra("address");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
            mTvFloor.setText(mStrFloor);
            mTvAddress.setText(mStrAddress);
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response && response.getResultCode() == 1) {
            if ("1".equals(state)) {
                ToastUtil.showToast(CustomerVisitActivity.this, "提交成功");
            } else {
                ToastUtil.showToast(CustomerVisitActivity.this, "保存成功");
            }
            finish();
            if (MODE_FROM_SIGN == mMode) {
                MActivityManager.getInstance().finishActivity(MenuWithFABActivity.class);
                Intent intent = new Intent(CustomerVisitActivity.this, VisitListActivity.class);
                startActivity(intent);
            }

        } else {
            mBtSubmit.setClickable(true);
            mBtSave.setClickable(true);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        mBtSubmit.setClickable(true);
        mBtSave.setClickable(true);
    }

    @Override
    public void onLocation(Object object) {
        if (null != object) {
            Map<String, Object> map = (Map<String, Object>) object;
            latitude = (double) map.get("latitude");
            longitude = (double) map.get("longitude");
            city = (String) map.get("city");
            mStrFloor = (String) map.get("floor");
            mStrAddress = (String) map.get("address");
            mTvFloor.setText(mStrFloor);
            mTvAddress.setText(mStrAddress);
            coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
        } else {
            ToastUtil.showToast(CustomerVisitActivity.this, "定位失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mGaoDeMapUtils) {
            mGaoDeMapUtils.onDestory();
        }
    }
}
