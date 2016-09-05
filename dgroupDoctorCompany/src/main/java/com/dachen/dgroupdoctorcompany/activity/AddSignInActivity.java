package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignLable;
import com.dachen.dgroupdoctorcompany.entity.VisitDetail;
import com.dachen.dgroupdoctorcompany.entity.WorkingDetail;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.dgroupdoctorcompany.views.ItemContainer;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weiwei on 2016/4/6.
 */
public class AddSignInActivity extends BaseActivity implements HttpManager.OnHttpListener, GaoDeMapUtils.LocationListener {
    public static final int            REQUEST_SELECT_DOCTOR = 101;
    public static final int            REQUEST_ADDRESS = 1200;

    public static final int            MODE_WORKING = 0;//上班打卡
    public static final int            SIGN_OFFWORKING = 2;//上班打卡
    public static final int            SIGN_WORKING = 1;//上班打卡
    public static final int            MODE_VISIT = 1;//拜访客户
    public int mSignMode;
    private int                        mMode;
    private RelativeLayout             vSelect;
    private TextView                   mTvAddress;
    private TextView                   mTvDate;
    private TextView                   mTvTime;
    private EditText                   mEtRemark;
    private Button                     mBtSubmit;
    private TextView                   mTvSelected;
    private TextView                   mTvName;
    private TextView                   mTvSave;
    private LinearLayout               vLable;
    private ItemContainer              vLableContainer;

    private String                     mId;//记录ID
    private String                     mStrLatitude;//纬度
    private String                     mStrLongitude;//经度
    private String                     mAddressName;//签到地点
    private String                     coordinate;
    private long                       lastClickTime;

//    private String                     singedTag;
    private List<TextView>             mTvLableList = new ArrayList<>();
    private GaoDeMapUtils              mGaoDeMapUtils;
    private double                     latitude;
    private double                     longitude;
    private Map<String,String>         mapLable2Id = new HashMap<>();
    private SoundPool                  mSoundPool;
    private int                        mSoundId;
    private List<String>               mListLable = new ArrayList<>();
    boolean allow;
    private String tabid;
    private String snippet;
    private String city;
    private LinearLayout ll_singtag;
    private long serverTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_signin);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        ll_singtag = (LinearLayout) findViewById(R.id.ll_singtag);

        vSelect = (RelativeLayout) findViewById(R.id.vSelect);
        mEtRemark = (EditText) findViewById(R.id.etRemark);
        mTvTime = (TextView) findViewById(R.id.tvTime);
        mTvDate = (TextView) findViewById(R.id.tvDate);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mBtSubmit = (Button) findViewById(R.id.btSubmit);
        mTvSelected = (TextView) findViewById(R.id.tvSelected);
        mTvName = (TextView) findViewById(R.id.tvName);
        mTvSave = (TextView) findViewById(R.id.tvSave);
//        mBtSubmit.setOnClickListener(this);
        findViewById(R.id.vSelect).setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        findViewById(R.id.vAddress).setOnClickListener(this);
        mEtRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if(key.length()>500){
                    ToastUtil.showToast(AddSignInActivity.this,"备注字数显示上限是500字");
                    mEtRemark.setText(key.substring(0,500));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vLable = (LinearLayout) this.findViewById(R.id.vLable);
        vLableContainer = (ItemContainer) this.findViewById(R.id.vLableContainer);
        mTvLableList.clear();

    }

    private void initData(){
        mGaoDeMapUtils = new GaoDeMapUtils(this.getApplicationContext(),this);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        mSoundId = mSoundPool.load(AddSignInActivity.this, R.raw.sign_add, 1);
        lastClickTime = System.currentTimeMillis();
        mMode = this.getIntent().getIntExtra("mode", MODE_WORKING);
        mSignMode = this.getIntent().getIntExtra("singmode",-1);
        tabid = this.getIntent().getStringExtra("tabid");
        serverTime = this.getIntent().getLongExtra("time",0);
        snippet = getIntent().getStringExtra("snippet");
        city = getIntent().getStringExtra("city");
        if (mSignMode ==  AddSignInActivity.SIGN_OFFWORKING){
            mListLable.add("下班");
        }else if(mSignMode ==  AddSignInActivity.SIGN_WORKING){
            mListLable.add("上班");
        }
        if(mMode == MODE_WORKING){
            setTitle("考勤打卡");
            vSelect.setVisibility(View.GONE);
        }else if(mMode == MODE_VISIT){
            setTitle("拜访客户");
            vSelect.setVisibility(View.VISIBLE);
        }
        mId = this.getIntent().getStringExtra("id");
        allow = getIntent().getBooleanExtra("allow",false);
        if(TextUtils.isEmpty(mId)){   //说明当前是添加签到
            latitude = this.getIntent().getDoubleExtra("latitude",0);
            longitude = this.getIntent().getDoubleExtra("longitude", 0);
            String latitu = this.getIntent().getStringExtra("latitude");
            String longit = this.getIntent().getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitu)){
                longitude = Double.parseDouble(latitu);
            }
            if (!TextUtils.isEmpty(longit)){
                latitude = Double.parseDouble(longit);
            }
            mStrLatitude = String.valueOf(latitude);
            mStrLongitude = String.valueOf(longitude);
            coordinate = mStrLatitude+","+mStrLongitude;
            mAddressName = this.getIntent().getStringExtra("name");
            mTvAddress.setText(mAddressName);
            long timeStamp = this.getIntent().getLongExtra("time",0);
            Log.d("zxy :", "188 : AddSignInActivity : initData : timeStamp = "+timeStamp);
            initCurrentDate(serverTime);
            mBtSubmit.setText("签到完成");
            setClickable(true);

        }else{
//            mBtSubmit.setText("保存");
            findViewById(R.id.ivFlag).setVisibility(View.GONE);
            findViewById(R.id.ivGo).setVisibility(View.INVISIBLE);

            setClickable(false);
            coordinate = this.getIntent().getStringExtra("coordinate");
            mListLable = this.getIntent().getStringArrayListExtra("lable");

            initSigninData();
        }
        CharSequence text = mEtRemark.getText();
        initSignLable();
        if (!TextUtils.isEmpty(text)) {
            mEtRemark.requestFocus();
            mEtRemark.setSelection(mEtRemark.getText().length());
        }
    }

    private void addLableItem(final String lable,boolean isEdite){
        TextView textView = new TextView(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        textView.setLayoutParams(params);
        textView.setTextSize(14);
        textView.setText(lable);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, (int) CommonUitls.dpToPixel(4f, this), 0, 0);

        if(isEdite){
            textView.setBackgroundResource(R.drawable.biaoqian_dis);
            textView.setTextColor(getResources().getColor(R.color.blue_3cbaff));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lable.equals("拜访")){
                        ll_singtag.setVisibility(View.GONE);
                    }else {
                        ll_singtag.setVisibility(View.VISIBLE);
                    }
                    TextView textViewItem = (TextView) v;
                    String lableItem = textViewItem.getText().toString();
                    if(mListLable.contains(lableItem)){
                        mListLable.remove(lableItem);
                        ll_singtag.setVisibility(View.VISIBLE);
                        textViewItem.setBackgroundResource(R.drawable.biaoqian_dis);
                        textViewItem.setTextColor(getResources().getColor(R.color.blue_3cbaff));
                    }else{
                        mListLable.add(lableItem);
                        textViewItem.setBackgroundResource(R.drawable.biaoqian);
                        textViewItem.setTextColor(getResources().getColor(R.color.white));
                        int childSize = vLableContainer.getChildCount();
                        for(int i=0;i<childSize;i++){
                            TextView child = (TextView) vLableContainer.getChildAt(i);
                            String strChild = child.getText().toString();
                            if(!lableItem.equals(strChild)){
                                if(strChild.contains(strChild)){
                                    mListLable.remove(strChild);
                                }
                                child.setBackgroundResource(R.drawable.biaoqian_dis);
                                child.setTextColor(getResources().getColor(R.color.blue_3cbaff));
                            }
                        }
                    }
                }
            });
        }else{
            textView.setBackgroundResource(R.drawable.biaoqian);
            textView.setTextColor(getResources().getColor(R.color.white));
        }
        vLableContainer.addView(textView);
    }

    private void initSignLable(){
        if(TextUtils.isEmpty(mId)&&mSignMode==-1){
            new HttpManager().get(this, Constants.GET_SIGNED_LABLE, SignLable.class,
                    Params.getInfoParams(AddSignInActivity.this),
                    this,false,4);
        }else{
            if(mListLable==null || mListLable.size()<=0){
                vLable.setVisibility(View.GONE);
            }else{
                vLable.setVisibility(View.VISIBLE);
                for(int i=0;i<mListLable.size();i++){
                    String strLableItem = mListLable.get(i);
                    addLableItem(strLableItem,false);
                }
            }
        }

    }

    private void setClickable(boolean isclicck){
        findViewById(R.id.vAddress).setClickable(isclicck);
        findViewById(R.id.vSelect).setClickable(isclicck);

    }

    private void initSigninData(){
        String address = this.getIntent().getStringExtra("address");
        String remark = this.getIntent().getStringExtra("remark");
        long time = this.getIntent().getLongExtra("time",0);
        mTvAddress.setText(address);
        initCurrentDate(time);

        if(!TextUtils.isEmpty(remark)){
            mEtRemark.setText(remark);
        }
    }

    private void initCurrentDate(long time){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd");
        Date date ;
        if(time>0){
            date = new Date(time);
        }else{
            date = new Date();
        }

        String strDate = s_format.format(date);
        String strTime= TimeFormatUtils.time_format_date(date);

        mTvTime.setText(strTime);
        mTvDate.setText(strDate);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(CommonUitls.isFastDoubleClick(lastClickTime)){
            lastClickTime = System.currentTimeMillis();
            return;
        }
        switch (v.getId()){
            case R.id.tvSave:
                mTvSave.setEnabled(false);
                mTvSave.setClickable(false);
                if(TextUtils.isEmpty(mId)){
                    mGaoDeMapUtils.startLocation();
                }else{
                    createOrUpdateSignin();
                }

                break;
            case R.id.vSelect:
                Intent intent = new Intent(AddSignInActivity.this,ChoiceDoctorForChatActivity.class);
                intent.putExtra("where","AddSignInActivity");
                startActivityForResult(intent,REQUEST_SELECT_DOCTOR);
                break;
            case R.id.vAddress:
                Intent addressIntent = new Intent(this,SelectAddressActivity.class);
                addressIntent.putExtra("select_type",1);
                addressIntent.putExtra("address_name", mTvAddress.getText().toString());
                startActivityForResult(addressIntent,REQUEST_ADDRESS);
//                finish();
                break;
            default:
                break;
        }
    }

    private void createOrUpdateSignin(){
        String remark = mEtRemark.getText().toString();
        String address = mTvAddress.getText().toString();
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();

        String signLable = tabid;
        if(null != mListLable){
            for(int i=0;i<mListLable.size();i++){
                String singedTag = mListLable.get(i);
                String lableId = mapLable2Id.get(singedTag);
                if(!TextUtils.isEmpty(lableId)){
                    if(TextUtils.isEmpty(signLable)){
                        signLable = lableId;
                    }else{
                        signLable = signLable+","+lableId;
                    }
                }
            }
        }
        String orgId = GetUserDepId.getUserDepId(this);
        showLoadingDialog();
        if (TextUtils.isEmpty(signLable)){
            signLable = "";
        }
        new HttpManager().post(this, Constants.CREATE_OR_UPDATA_SIGIN_IN, Result.class,
                Params.getWorkingParams(AddSignInActivity.this, deviceId, remark, mId, coordinate, address, signLable, orgId),
                this, false, 4);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if(null!=response){
            if(response instanceof WorkingDetail){
                WorkingDetail workingDetail = (WorkingDetail) response;
                WorkingDetail.Data data = workingDetail.data;
                if(null != data){
                    WorkingDetail.Data.Signed signed = data.signed;
                    String address = signed.address;
                    long time = signed.time;
                    String remark = signed.remark;
                    coordinate = signed.coordinate;
                    mTvAddress.setText(address);
                    mEtRemark.setText(remark);
                    initCurrentDate(time);
                }
            }else if(response instanceof VisitDetail){

            }else if(response instanceof SignLable){
                if(response.getResultCode() == 1){
                    SignLable signLable = (SignLable) response;
                    if(null!=signLable && null!=signLable.data){
                        List<SignLable.Data.SignLableItem> lableItemList = signLable.data.data;
                        for(int i=0; i<lableItemList.size();i++){
                            SignLable.Data.SignLableItem signLableItem = lableItemList.get(i);
                            String strLable = signLableItem.label;
                            String strLableId = signLableItem.id;
                            mapLable2Id.put(strLable,strLableId);
                            addLableItem(strLable,true);
                        }

                    }
                }
            }else{
                if(response.getResultCode() == 1){
                    playAddSign();
                    if(TextUtils.isEmpty(mId)){
                        ToastUtil.showToast(AddSignInActivity.this, "签到成功");
                        Intent intent = new Intent(AddSignInActivity.this,MenuWithFABActivity.class);
                        startActivity(intent);
                        finish();
                        MActivityManager.getInstance().finishActivity(SelectAddressActivity.class);
                        if(null != mListLable){
                                if (mListLable.size()>0&&!TextUtils.isEmpty(mListLable.get(0))&&mListLable.get(0).equals("拜访")){
                                    intent = new Intent(AddSignInActivity.this, SelfVisitActivity.class);
                                    intent.putExtra("address", mAddressName);
                                    intent.putExtra("longitude", longitude);
                                    intent.putExtra("latitude", latitude);
                                    intent.putExtra("addressname", city + mAddressName + snippet);
                                    intent.putExtra("mode", CustomerVisitActivity.MODE_FROM_SIGN);
                                    intent.putExtra("city", city);
                                    startActivity(intent);
                                }
                        }
                    }else{
                        ToastUtil.showToast(AddSignInActivity.this,"保存成功");
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }else{
                    ToastUtil.showErrorData(AddSignInActivity.this);
                    mTvSave.setEnabled(true);
                    mTvSave.setClickable(true);
                }
            }

        }else{
            ToastUtil.showErrorNet(AddSignInActivity.this);
            mTvSave.setEnabled(true);
            mTvSave.setClickable(true);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        ToastUtil.showErrorNet(AddSignInActivity.this);
        mTvSave.setEnabled(true);
        mTvSave.setClickable(true);
    }

    @Override
    public void onLocation(Object object) {
        if(null != object){
            Map<String,Object> map = (Map<String, Object>) object;
            final double latitude1 = (double) map.get("latitude");
            final double longitude1 = (double) map.get("longitude");
            final String city1 = (String) map.get("city");
            if(0 == latitude && 0 == longitude){
                ToastUtil.showToast(AddSignInActivity.this,"请先选择地点");
                mTvSave.setEnabled(true);
                mTvSave.setClickable(true);
                return;
            }else{
                LatLng startLatlng = new LatLng(latitude,longitude);

//                final double latitude1 = 40.37;
//                final double longitude1 = 116.85;
//                final String city1 = "北京";
                LatLng endLatlng = new LatLng(latitude1,longitude1);
                float distance = AMapUtils.calculateLineDistance(startLatlng,  endLatlng);
                if(distance>250.0f&&!allow){//范围为250米
                    final CustomDialog dialog = new CustomDialog(this);
                    dialog.showDialog("定位地点", "当前定位地点和你选择的地点不符,请重新选择地点签到.", R.string.select_address,
                            0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MActivityManager.getInstance().finishActivity(SelectAddressActivity.class);
                                    Intent intentAddress = new Intent(AddSignInActivity.this,SelectAddressActivity.class);
                                    intentAddress.putExtra("poi",SelectAddressActivity.POI);
                                    intentAddress.putExtra("distance","250");
                                    intentAddress.putExtra("latitude",latitude1);
                                    intentAddress.putExtra("longitude",longitude1);
                                    intentAddress.putExtra("city",city1);
                                    intentAddress.putExtra("mode",AddSignInActivity.MODE_WORKING);
                                    startActivity(intentAddress);
                                    dialog.dimissDialog();
                                    finish();
                                }
                            }, null);
                }else{//如果在范围内就提交这条记录
                    createOrUpdateSignin();
                }
            }
        }else{
            ToastUtil.showToast(AddSignInActivity.this,"定位失败");
        }
    }

    private void playAddSign(){
        mSoundPool.play(mSoundId,1, 1, 0, 0, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADDRESS:
                    latitude = intent.getDoubleExtra("latitude",0);
                    longitude = intent.getDoubleExtra("longitude",0);
                    mStrLatitude = String.valueOf(latitude);
                    mStrLongitude = String.valueOf(longitude);
                    coordinate = mStrLatitude+","+mStrLongitude;
                    mAddressName = intent.getStringExtra("name");
                    mTvAddress.setText(mAddressName);
                    break;
                default:
                    break;
            }
        }
    }
}
