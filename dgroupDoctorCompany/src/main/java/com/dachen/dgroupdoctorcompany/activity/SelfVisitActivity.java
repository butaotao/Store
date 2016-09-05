package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.StringUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.GoodsGroupsModel;
import com.dachen.dgroupdoctorcompany.entity.VisitEditEnableBean;
import com.dachen.dgroupdoctorcompany.entity.VisitMember;
import com.dachen.dgroupdoctorcompany.entity.VisitMemberResponse;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.dgroupdoctorcompany.views.NoScrollGridView;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;
import com.dachen.imsdk.net.UploadEngine7Niu;
import com.dachen.imsdk.utils.FileUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * [独立拜访]
 *
 * @author zhouyuandong
 * @version 1.0
 * @date 2016-6-24
 **/
public class SelfVisitActivity extends BaseActivity implements View.OnClickListener, HttpManager.OnHttpListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, GaoDeMapUtils.LocationListener {

    private static final int REQUEST_PICK = 1001;
    public static final int REQUEST_SELECT_DOCTOR = 101;
    public static final int REQUEST_SELECT_ADDRESS = 102;
    public static final int REQUEST_SELECT_MEDIA = 103;

    public static final int MODE_FROM_VIST_LIST_ITEM = 3;//从拜访列表item过来
    public static final int MODE_FROM_VIST_LIST = 2;//从拜访列表页过来
    public static final int MODE_FROM_SIGN = 1;//从签到页过来
    public static final int MODE_FROM_SIGN_LIST = 4;//从签到列表过来
    private int mMode;

    private RelativeLayout rl_back;
    private TextView tv_title_save;
    private TextView tv_title;
    private RelativeLayout location_ray;
    private TextView tv_time_location;
    private TextView tv_address;
    private TextView tvWeek;
    private TextView tvDate;
    private RelativeLayout vSelect;
    private TextView tvSelected;
    private RelativeLayout variety_ray;
    private TextView tv_variety;
    private ImageView visitor_avatar;
    private EditText etRemark;
    private NoScrollGridView gridview;
    private String ADDPIC = "add";
    private List<String> selectedPicture;
    private ArrayList<String> uploadList = new ArrayList<String>();
    private List<String> urlArray;
    private GridAdapter mAdapter;
    private String mStrDoctorID = "";
    private String mStrDoctorName = "";
    private double latitude;//纬度
    private double longitude;//经度
    private String mStrFloor = "";
    private String mStrAddress;
    private String address;
    private String mId;
    private String coordinate;
    private String strTime;
    private String city;//城市
    private GaoDeMapUtils mGaoDeMapUtils;
    private String deviceId;
    private String state = "1";//0草稿，1提交
    private String mStrMedia;
    private String orginId;
    private LayoutInflater inflater;
    public boolean isShowDel;
    private ImageView address_arrow;
    private TextView desp2;
    private ImageView name_arrow;
    private ImageView variety_arrow;
    private boolean isSelectAddress;
    private boolean isOrigin = false;
    private TextView del_desp;
    private LinearLayout ll_visit;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_visit);
        setTitle("");
        initView();

        initData();
    }

    public void initView() {
        inflater = LayoutInflater.from(this);
        mGaoDeMapUtils = new GaoDeMapUtils(this.getApplicationContext(), this);
        mMode = this.getIntent().getIntExtra("mode", MODE_FROM_VIST_LIST_ITEM);
        urlArray = new ArrayList<String>();
        rl_back = getViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_title_save = getViewById(R.id.tv_title_save);
        tv_title_save.setVisibility(View.VISIBLE);
        tv_title_save.setOnClickListener(this);
        tv_title = getViewById(R.id.tv_title);
        ll_visit = getViewById(R.id.ll_visit);
        ll_visit.setOnClickListener(this);
        if(MODE_FROM_VIST_LIST_ITEM == mMode){
            tv_title.setText("拜访详情");
        }else if(MODE_FROM_SIGN == mMode || MODE_FROM_SIGN_LIST == mMode){
            tv_title.setText("客户拜访");
        }else if (MODE_FROM_VIST_LIST == mMode){
            tv_title.setText("单独拜访");
        }
        location_ray = getViewById(R.id.location_ray);
        location_ray.setOnClickListener(this);
        tv_time_location = getViewById(R.id.tv_time_location);
        tv_address = getViewById(R.id.tv_address);
        tvWeek = getViewById(R.id.tvWeek);
        tvDate = getViewById(R.id.tvDate);
        vSelect = getViewById(R.id.vSelect);
        vSelect.setOnClickListener(this);
        tvSelected = getViewById(R.id.tvSelected);
        variety_ray = getViewById(R.id.variety_ray);
        variety_ray.setOnClickListener(this);
        tv_variety = getViewById(R.id.tv_variety);
        visitor_avatar = getViewById(R.id.visitor_avatar);
        etRemark = getViewById(R.id.etRemark);
        gridview = getViewById(R.id.gridview);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);
        address_arrow = getViewById(R.id.address_arrow);
        name_arrow = getViewById(R.id.name_arrow);
        variety_arrow = getViewById(R.id.variety_arrow);
        desp2 = getViewById(R.id.desp2);
        String loginUserId = SharedPreferenceUtil.getString(this, "id", "");
        String headUrl = SharedPreferenceUtil.getString(this, loginUserId + "head_url", "");
        //头像
        if (!TextUtils.isEmpty(headUrl))
//            ImageLoader.getInstance().displayImage(headUrl, visitor_avatar, CommonUitls.getHeadOptions());
              CustomImagerLoader.getInstance().loadImage(visitor_avatar, headUrl,
                    R.drawable.ic_default_head, R.drawable.ic_default_head);
        else
            visitor_avatar.setImageResource(R.drawable.head_icon);

        del_desp = getViewById(R.id.del_desp);
        selectedPicture = new ArrayList<String>();
        selectedPicture.add(ADDPIC);
        mAdapter = new GridAdapter();
        gridview.setAdapter(mAdapter);

    }

    private void initData() {
        Log.d("zxy :", "191 : SelfVisitActivity : initData : ");
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        deviceId = TelephonyMgr.getDeviceId();
        orginId = GetUserDepId.getUserDepId(this);

        if (MODE_FROM_VIST_LIST_ITEM == mMode || MODE_FROM_SIGN_LIST==mMode) {
            Log.d("zxy :", "197 : SelfVisitActivity : initData : if");
//            mStrAddress = this.getIntent().getStringExtra("addressname");
//            mStrFloor = this.getIntent().getStringExtra("address");
//            latitude = this.getIntent().getDoubleExtra("latitude", 0);
//            longitude = this.getIntent().getDoubleExtra("longitude", 0);
//            city = this.getIntent().getStringExtra("city");
//            coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
//            tv_address.setText(mStrAddress);
            mId = this.getIntent().getStringExtra("id");
            time = this.getIntent().getLongExtra("time",0);
//            mStrDoctorName = this.getIntent().getStringExtra("doctorName");
//            mStrDoctorName = this.getIntent().getStringExtra("doctorId");
//            tvSelected.setText(mStrDoctorName);
//            long time = this.getIntent().getLongExtra("time", 0);
//            Date date = new Date(time);
//            String strDate = TimeFormatUtils.china_format_date(date);
//            String strWeek = TimeFormatUtils.week_format_date(date);
//            strTime = TimeFormatUtils.time_format_date(date);
//            tvWeek.setText(strWeek);
//            tvDate.setText(strDate);
//            tv_time_location.setText(strTime+" "+mStrFloor);
//            String remark = this.getIntent().getStringExtra("remark");
//            etRemark.setText(remark);
            location_ray.setEnabled(false);
            address_arrow.setVisibility(View.INVISIBLE);
            showLoadingDialog();
            new HttpManager().post(this, Constants.VISIT_DETAIL, VisitMemberResponse.class,
                    Params.getVisitDetail(SelfVisitActivity.this, mId),
                    this, false, 4);
            new HttpManager().post(this, Constants.VISIT_DETAIL_EDITEABLE, VisitEditEnableBean.class,
                    Params.getVisitDetail(SelfVisitActivity.this, mId),
                    this, false, 4);

        }else if(MODE_FROM_SIGN==mMode){
            Log.d("zxy :", "230 : SelfVisitActivity : initData : else if");
            mStrAddress = this.getIntent().getStringExtra("addressname");
            mStrFloor = this.getIntent().getStringExtra("address");
            latitude = this.getIntent().getDoubleExtra("latitude", 0);
            longitude = this.getIntent().getDoubleExtra("longitude", 0);
            time = this.getIntent().getLongExtra("time", 0);

            city = this.getIntent().getStringExtra("city");
            coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
            tv_address.setText(mStrAddress);
            Date date = new Date(time);
            String strDate = TimeFormatUtils.china_format_date(date);
            String strWeek = TimeFormatUtils.week_format_date(date);
            Log.d("zxy :", "239 : SelfVisitActivity : initData : strDate = "+strDate+", strWeek = "+strWeek);
            tvWeek.setText(strWeek);
            tvDate.setText(strDate);
            strTime = TimeFormatUtils.time_format_date(date);
            tv_time_location.setText(strTime+" "+mStrFloor);
            del_desp.setVisibility(View.VISIBLE);
        } else {
            Log.d("zxy :", "249 : SelfVisitActivity : initData : else");
            Date date = new Date();
            String strDate = TimeFormatUtils.china_format_date(date);
            String strWeek = TimeFormatUtils.week_format_date(date);
            Log.d("zxy :", "249 : SelfVisitActivity : initData : strDate = "+strDate+", strWeek = "+strWeek);
            strTime = TimeFormatUtils.time_format_date(date);
            tvWeek.setText(strWeek);
            tvDate.setText(strDate);
            mGaoDeMapUtils.startLocation();
            del_desp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLocation(Object object) {
        if (null != object) {
            if(!isSelectAddress){
                Map<String, Object> map = (Map<String, Object>) object;
                latitude = (double) map.get("latitude");
                longitude = (double) map.get("longitude");
                city = (String) map.get("city");
                mStrFloor = (String) map.get("floor");
                mStrAddress = (String) map.get("address");
                if(TextUtils.isEmpty(mStrFloor)){
                    mStrFloor = mStrAddress;
                }
                tv_time_location.setText(strTime + " " + mStrFloor);
                tv_address.setText(mStrAddress);
                coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
            }
        } else {
            ToastUtil.showToast(this, "定位失败");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_back:
                goBackMethod();
                break;
            case R.id.tv_title_save:
                tv_title_save.setEnabled(false);
                tv_title_save.setClickable(false);
                String str = "";
                if (urlArray != null) {
                    for (int i = 0; i < urlArray.size(); i++) {
                        str += urlArray.get(i) + ",";
                    }
                    if (!str.isEmpty()) {
                        str = str.substring(0, str.lastIndexOf(","));
                    }
                }

                showLoadingDialog();
                new HttpManager().post(this, Constants.CREATE_OR_UPDATA_VISIT, Result.class,
                        Params.getSelfVisitParams(SelfVisitActivity.this, mStrFloor, state, mStrDoctorID, mStrDoctorName, etRemark.getText().toString(), mId, coordinate, mStrAddress,
                                deviceId, orginId, mStrMedia, str),
                        this, false, 4);

                break;
            case R.id.vSelect:
                Intent intent = new Intent(this, ChoiceDoctorForChatActivity.class);
                intent.putExtra("where", "AddSignInActivity");
                startActivityForResult(intent, REQUEST_SELECT_DOCTOR);
                break;
            case R.id.location_ray:
                Intent intentAddress = new Intent(this, SelectAddressActivity.class);
                intentAddress.putExtra("select_mode", SelectAddressActivity.MODE_SELECT_ADDRESS);
                intentAddress.putExtra("poi", SelectAddressActivity.POI);
                intentAddress.putExtra("distance", 250);
                intentAddress.putExtra("latitude", latitude);
                intentAddress.putExtra("longitude", longitude);
                intentAddress.putExtra("city", city);
                startActivityForResult(intentAddress, REQUEST_SELECT_ADDRESS);
                break;
            case R.id.variety_ray:
                Intent media_intent = new Intent(this, ChoiceMedieaActivity.class);
                media_intent.putExtra("mode", ChoiceMedieaActivity.MODE_SINGLE_VISIT);
                media_intent.putExtra("mediea", mStrMedia);
                startActivityForResult(media_intent, REQUEST_SELECT_MEDIA);
                break;
            case R.id.ll_visit:
                 if(MODE_FROM_VIST_LIST_ITEM == mMode|| MODE_FROM_SIGN_LIST == mMode){
                     intent = new Intent(this,MapDetailActivity.class);
                     if(!TextUtils.isEmpty(coordinate)&&coordinate.contains(",")){
                         String[] array = coordinate.split(",");
                         String latitude = array[0];
                         String longitude = array[1];
                         intent.putExtra("latitude", Double.valueOf(latitude));
                         intent.putExtra("longitude", Double.valueOf(longitude));
                     }
                     if (!TextUtils.isEmpty(address)){
                         intent.putExtra("address",address);
                     }
                      startActivity(intent);
                    }else {
                      intentAddress = new Intent(this, SelectAddressActivity.class);
                     intentAddress.putExtra("select_mode", SelectAddressActivity.MODE_SELECT_ADDRESS);
                     intentAddress.putExtra("poi", "地名地址信息|医疗保健服务|商务住宅|交通设施服务|公司企业|公共设施");
                     intentAddress.putExtra("distance", 250);
                     intentAddress.putExtra("latitude", latitude);
                     intentAddress.putExtra("longitude", longitude);
                     intentAddress.putExtra("city", city);
                     startActivityForResult(intentAddress, REQUEST_SELECT_ADDRESS);
                 }

                break;
        }
    }

    private int getPicNum() {
        int num = selectedPicture.size();
        if (selectedPicture.contains(ADDPIC))
            num -= 1;
        return num;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK:
                    String[] all_path = intent.getStringArrayExtra(GalleryAction.INTENT_ALL_PATH);
                    if (all_path == null || all_path.length == 0)
                        return;
                    isOrigin = intent.getBooleanExtra(GalleryAction.INTENT_IS_ORIGIN, false);
                    for (String path : all_path)
                        uploadImage(path);
                    break;
                case REQUEST_SELECT_DOCTOR:
                    mStrDoctorID = intent.getStringExtra("doctorid");
                    mStrDoctorName = intent.getStringExtra("doctorname");
                    if (!TextUtils.isEmpty(mStrDoctorName)) {
                        tvSelected.setText(mStrDoctorName);
                    }
                    break;
                case REQUEST_SELECT_ADDRESS:
                    isSelectAddress = true;
                    mStrFloor = intent.getStringExtra("floor");
                    mStrAddress = intent.getStringExtra("address");
                    latitude = intent.getDoubleExtra("latitude", 0);
                    longitude = intent.getDoubleExtra("longitude", 0);
                    coordinate = String.valueOf(latitude) + "," + String.valueOf(longitude);
                    tv_time_location.setText(strTime + " " + mStrFloor);
                    tv_address.setText(mStrAddress);
                    break;
                case REQUEST_SELECT_MEDIA:
                    mStrMedia = intent.getStringExtra("mediea");
                    String medieaName = intent.getStringExtra("medieaName");
                    tv_variety.setText(medieaName);
                    break;
                default:
                    break;
            }
        }
    }

    private void uploadImage(String filePath) {
        try {
            int compress = 100;
            if(isOrigin){
                compress = 100;
            }else{
                compress = 60;
            }
            filePath = FileUtil.compressImage(filePath, compress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mDialog.setMessage("正在上传");
        mDialog.show();
        uploadList.add(filePath);
        UploadEngine7Niu.uploadPatientFile(filePath, new FileUpListener(filePath));
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response) {
            if (response instanceof VisitMemberResponse) {
                if (response.getResultCode() == 1) {
                    VisitMember member = ((VisitMemberResponse) response).getData().getVisit();
                    long time = member.getTime();
                    mStrAddress = member.getAddress();
                    address = member.getAddressName();
                    mStrFloor = member.getAddressName();
                    if (member.getDoctorName() != null) {
                        mStrDoctorName = member.getDoctorName();
                    }
                    if (member.getDoctorId() != null) {
                        mStrDoctorID = member.getDoctorId();
                    }
                    String remark = member.getRemark();
                    coordinate = member.getCoordinate();


                    List<String> picList = member.getImgUrls();
                    if (picList != null && picList.size() > 0) {
                        for (int i = 0; i < picList.size(); i++) {
                            addListPic(picList.get(i));
                        }
                    }

                    List<GoodsGroupsModel> groups = member.getGoodsGroups();
                    if (groups != null && groups.size() > 0) {
                        List<String> mediaNameStr = new ArrayList<String>();
                        List<String> mediaIdStr = new ArrayList<String>();
                        for (int i = 0; i < groups.size(); i++) {
                            mediaNameStr.add(groups.get(i).getName());
                            mediaIdStr.add(groups.get(i).getId());
                        }
                        Gson g = new Gson();
                        mStrMedia = g.toJson(groups);
                        String medieaName = g.toJson(mediaNameStr);
                        medieaName = medieaName.replace("[", "").replace("]", "").replaceAll("\"", "");
                        tv_variety.setText(medieaName);
                    }

                    tv_address.setText(mStrAddress);
                    Date date = new Date(this.time);//得到传过来的时间格式化
                    String strDate = TimeFormatUtils.china_format_date(date);
                    String strWeek = TimeFormatUtils.week_format_date(date);
                    strTime = TimeFormatUtils.time_format_date(date);
                    tvWeek.setText(strWeek);
                    tvDate.setText(strDate);
                    tv_time_location.setText(strTime + " " + mStrFloor);

                    long timeMillis = System.currentTimeMillis();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    String sp_time = sf.format(time);
                    String current_time = sf.format(timeMillis);
                    if (!sp_time.equals(current_time)) {//不是同一天，不可编辑
                        del_desp.setVisibility(View.INVISIBLE);
                        tv_title_save.setVisibility(View.GONE);
                        selectedPicture.remove(ADDPIC);
                        mAdapter.notifyDataSetChanged();
                        desp2.setVisibility(View.GONE);
                        variety_arrow.setVisibility(View.INVISIBLE);
                        name_arrow.setVisibility(View.INVISIBLE);
                        variety_ray.setEnabled(false);
                        vSelect.setEnabled(false);
                        tvSelected.setTextColor(getResources().getColor(R.color.gray_666666));
                        tv_variety.setTextColor(getResources().getColor(R.color.gray_666666));
                        tvSelected.setHintTextColor(getResources().getColor(R.color.gray_666666));
                        tv_variety.setHintTextColor(getResources().getColor(R.color.gray_666666));
                        if (TextUtils.isEmpty(mStrDoctorName)) {
                            tvSelected.setHint("无");
                        } else {
                            tvSelected.setText(mStrDoctorName);
                        }
                        if (TextUtils.isEmpty(remark)) {
                            etRemark.setHint("无");
                        } else {
                            etRemark.setText(remark);
                        }
                        if (TextUtils.isEmpty(tv_variety.getText().toString())) {
                            tv_variety.setHint("无");
                        }
                    } else {
                        del_desp.setVisibility(View.VISIBLE);
                        tv_title_save.setVisibility(View.VISIBLE);
                        etRemark.setText(remark);
                        if (!TextUtils.isEmpty(mStrDoctorName)) {
                            tvSelected.setText(mStrDoctorName);
                        }
                    }
                }
            }else if (response instanceof VisitEditEnableBean) {
                VisitEditEnableBean editEnable = (VisitEditEnableBean) response;
                if (editEnable.data!=null) {
                    boolean etRemarkEnable = editEnable.data.editStatus;
                    Log.d("zxy :", "455 : JointVisitActivity : onSuccess : VisitEditEnableBean "+((VisitEditEnableBean) response).data.editStatus);
                    setRemarkEnable(etRemarkEnable);
                }
            } else if (response instanceof Result) {
                if (response.getResultCode() == 1) {
                    if ("1".equals(state)) {
                        ToastUtil.showToast(SelfVisitActivity.this, "签到成功");
                    }
                    if (MODE_FROM_SIGN == mMode || MODE_FROM_SIGN_LIST == mMode) {
                        Intent intent = new Intent(this, MenuWithFABActivity.class);
                        startActivity(intent);

                        Intent broad_intent = new Intent();
                        broad_intent.setAction("action.to.signlisttoday");
                        sendBroadcast(broad_intent);
                    }
                    finish();
                }else{
                    ToastUtil.showToast(SelfVisitActivity.this, "提交失败");
                }
            }

        }
        tv_title_save.setEnabled(true);
        tv_title_save.setClickable(true);
    }

    /**
     * 判断是否可编辑
     * @param etRemarkEnable
     */
    private void setRemarkEnable(boolean etRemarkEnable) {
        if (etRemarkEnable) {
            etRemark.setEnabled(true);
        }else {
            etRemark.setEnabled(false);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        ToastUtil.showToast(SelfVisitActivity.this, "提交失败");
        tv_title_save.setEnabled(true);
        tv_title_save.setClickable(true);
    }

    private class FileUpListener implements UploadEngine7Niu.UploadObserver7Niu {
        String path;

        public FileUpListener(String path) {
            this.path = path;
        }

        @Override
        public void onUploadSuccess(String url) {
            addListPic(url);
            uploadList.remove(path);
            if (uploadList.size() == 0)
                mDialog.dismiss();
        }

        @Override
        public void onUploadFailure(String msg) {
            uploadList.remove(path);
            if (uploadList.size() == 0)
                mDialog.dismiss();
            ToastUtil.showToast(SelfVisitActivity.this, "图片上传失败");
        }
    }

    private void addListPic(String url) {
        selectedPicture.remove(ADDPIC);
        if (selectedPicture.size() < 8)
            selectedPicture.add(url);
        if (selectedPicture.size() < 8)
            selectedPicture.add(ADDPIC);

        mAdapter.notifyDataSetChanged();
        urlArray.add(url);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String temp = selectedPicture.get(i);
        if (temp.equals(ADDPIC)) {
            CustomGalleryActivity.openUi(mThis, true, REQUEST_PICK, 8 - getPicNum());
        } else {
            Intent intent = new Intent(this, PhotoViewerActivity.class);
            intent.putExtra(PhotoViewerActivity.INTENT_EXTRA_IMAGE_URL, StringUtils.thumbnailUrl2originalUrl(temp));
            startActivity(intent);
            isShowDel = false;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String temp = selectedPicture.get(i);
        if (!temp.equals(ADDPIC)) {
            isShowDel = true;
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mGaoDeMapUtils) {
            mGaoDeMapUtils.onDestory();
        }
    }

    class GridAdapter extends BaseAdapter {
        ViewGroup.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        @Override
        public int getCount() {
            return selectedPicture.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_picture_layout, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv_pic);
            ImageView img_del = (ImageView) convertView.findViewById(R.id.img_del);
            String path = selectedPicture.get(position);
            if (!TextUtils.isEmpty(path) && path.equals(ADDPIC)) {
                iv.setImageResource(R.drawable.ic_add_camera);
                img_del.setVisibility(View.GONE);
            } else {
                if (path.startsWith("http")) {
//                    ImageLoader.getInstance().displayImage(path, iv);

                    CustomImagerLoader.getInstance().loadImage(iv,path);
                } else {
                    ImageLoader.getInstance().displayImage("file://" + path, iv);
                }
                if (isShowDel) {
                    img_del.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.GONE);
                }
            }

            img_del.setTag(position);
            img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = Integer.parseInt(String.valueOf(v.getTag()));
                    delPicture(index);
                }
            });
            return convertView;
        }
    }

    public void delPicture(int index) {
        selectedPicture.remove(index);
        urlArray.remove(index);
        if (selectedPicture.size() < 8 && !selectedPicture.contains(ADDPIC)) {
            selectedPicture.add(ADDPIC);
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBackMethod();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBackMethod() {
        if (MODE_FROM_SIGN == mMode) {
            Intent intent = new Intent(this, MenuWithFABActivity.class);
            startActivity(intent);

        }
        finish();
    }
}
