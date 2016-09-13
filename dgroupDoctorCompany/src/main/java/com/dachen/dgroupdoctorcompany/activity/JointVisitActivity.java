package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import com.dachen.dgroupdoctorcompany.entity.PersonModel;
import com.dachen.dgroupdoctorcompany.entity.VisitEditEnableBean;
import com.dachen.dgroupdoctorcompany.entity.VisitMember;
import com.dachen.dgroupdoctorcompany.entity.VisitMemberResponse;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.TimeFormatUtils;
import com.dachen.dgroupdoctorcompany.views.NoScrollGridView;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;
import com.dachen.imsdk.net.UploadEngine7Niu;
import com.dachen.imsdk.utils.FileUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * [协同拜访]
 *
 * @author zhouyuandong
 * @version 1.0
 * @date 2016-6-24
 *
 **/
public class JointVisitActivity extends BaseActivity implements View.OnClickListener,HttpManager.OnHttpListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final int REQUEST_PICK = 1001;
    public static final int  REQUEST_SELECT_DOCTOR = 101;
    public static final int  REQUEST_SELECT_ADDRESS = 102;
    public static final int  REQUEST_SELECT_MEDIA = 103;
    private final int GET_PERSON = 1700;

    public static final int MODE_FROM_VIST_LIST_ITEM = 3;//从拜访列表item过来
    public static final int MODE_FROM_VIST_LIST = 2;//从拜访列表页过来
    public static final int MODE_FROM_SIGN = 1;//从签到页过来
    public static final int MODE_FROM_SIGN_LIST = 4;//从签到列表过来

    private int mMode;

    private RelativeLayout rl_back;
    private TextView tv_title_save;
    private TextView tvWeek;
    private TextView tvDate;
    private TextView tv_time_location;
    private TextView tv_address;
    private ImageView visitor_avatar1,visitor_avatar2,visitor_avatar3;
    private RelativeLayout visitors_ray;
    private NoScrollGridView gridview;
    private String ADDPIC = "add";
    private List<String> selectedPicture;
    private ArrayList<String> uploadList = new ArrayList<String>();
    private List<String> urlArray;
    private GridAdapter mAdapter;
    private String deviceId;
    private String state = "1";//0草稿，1提交
    private String mStrMedia;
    private String orginId;
    private String mStrDoctorID;
    private String mStrDoctorName;
    private double latitude;//纬度
    private double longitude;//经度
    private String mStrFloor="";
    private String mStrAddress;
    private String address;
    private String mId;
    private String coordinate;
    private String strTime;
    private String city;//城市
    private EditText etRemark;
    private LayoutInflater inflater;
    public boolean isShowDel;
    private String jsonVisitPeople;
    private TextView desp2;
    private RelativeLayout vSelect;
    private TextView tvSelected;
    private RelativeLayout variety_ray;
    private TextView tv_variety;
    private ImageView visitor_avatar;
    private ImageView name_arrow;
    private ImageView variety_arrow;
    private boolean isOrigin = false;
    private TextView  del_desp;
    private LinearLayout ll_showmapdes;
    boolean etRemarkEnable = false;//拜访记录是否可编辑
    private long mTime;
    private String mRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_visit);
        //初始化
        initViews();
        initDate();
    }

    public void initViews() {
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        deviceId = TelephonyMgr.getDeviceId();
        orginId = GetUserDepId.getUserDepId(this);
        mMode = this.getIntent().getIntExtra("mode", MODE_FROM_VIST_LIST);
        inflater = LayoutInflater.from(this);
        urlArray = new ArrayList<String>();
        rl_back = getViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_title_save = getViewById(R.id.tv_title_save);
        tv_title_save.setOnClickListener(this);
        tv_title_save.setVisibility(View.VISIBLE);
        setTitle("客户拜访");
        if(MODE_FROM_VIST_LIST_ITEM == mMode){
            setTitle("拜访详情");
        }else if(MODE_FROM_SIGN == mMode || MODE_FROM_SIGN_LIST == mMode){
            setTitle("客户拜访");
        }
        tvWeek = getViewById(R.id.tvWeek);
        tvDate = getViewById(R.id.tvDate);
        ll_showmapdes = getViewById(R.id.ll_showmapdes);
        ll_showmapdes.setOnClickListener(this);
        tv_time_location = getViewById(R.id.tv_time_location);
        tv_address = getViewById(R.id.tv_address);
        vSelect = getViewById(R.id.vSelect);
        vSelect.setOnClickListener(this);
        tvSelected = getViewById(R.id.tvSelected);
        variety_ray = getViewById(R.id.variety_ray);
        variety_ray.setOnClickListener(this);
        tv_variety = getViewById(R.id.tv_variety);
        visitor_avatar = getViewById(R.id.visitor_avatar);
        visitors_ray = getViewById(R.id.visitors_ray);
        visitors_ray.setOnClickListener(this);
        name_arrow = getViewById(R.id.name_arrow);
        variety_arrow = getViewById(R.id.variety_arrow);
        visitor_avatar1 = getViewById(R.id.visitor_avatar1);
        visitor_avatar2 = getViewById(R.id.visitor_avatar2);
        visitor_avatar3 = getViewById(R.id.visitor_avatar3);
        etRemark = getViewById(R.id.etRemark);
        desp2 = getViewById(R.id.desp2);
        gridview = getViewById(R.id.gridview);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);
        selectedPicture = new ArrayList<String>();
        selectedPicture.add(ADDPIC);
        mAdapter = new GridAdapter();
        gridview.setAdapter(mAdapter);
        del_desp = getViewById(R.id.del_desp);
    }

    private void initDate() {

        mId = this.getIntent().getStringExtra("id");
        showLoadingDialog();
        new HttpManager().post(this, Constants.VISIT_DETAIL, VisitMemberResponse.class,
                Params.getVisitDetail(JointVisitActivity.this, mId),
                this, false, 4);
        if (MODE_FROM_VIST_LIST!=2){
            new HttpManager().post(this, Constants.VISIT_DETAIL_EDITEABLE, VisitEditEnableBean.class,
                    Params.getVisitDetail(JointVisitActivity.this, mId),
                    this, false, 4);
        }

        etRemarkEnable = true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
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
                        Params.getSelfVisitParams(JointVisitActivity.this, mStrFloor, state, mStrDoctorID,
                                mStrDoctorName, etRemark.getText().toString(), mId, coordinate, mStrAddress,
                                deviceId,orginId,mStrMedia,str,""),
                        this, false, 4);
                break;
            case R.id.visitors_ray:
                Intent intent = new Intent(this,VisitMemberActivity.class);
                intent.putExtra("jsonPeople",jsonVisitPeople);
                startActivity(intent);
                break;
            case R.id.vSelect:
                Intent name_intent = new Intent(this,ChoiceDoctorForChatActivity.class);
                name_intent.putExtra("where","AddSignInActivity");
                startActivityForResult(name_intent,REQUEST_SELECT_DOCTOR);
                break;
            case R.id.variety_ray:
                Intent media_intent = new Intent(this,ChoiceMedieaActivity.class);
                media_intent.putExtra("mode",ChoiceMedieaActivity.MODE_SINGLE_VISIT);
                media_intent.putExtra("mediea",mStrMedia);
                startActivityForResult(media_intent,REQUEST_SELECT_MEDIA);
                break;
            case R.id.ll_showmapdes:
                intent = new Intent(this,MapDetailActivity.class);
                if(!TextUtils.isEmpty(coordinate)&&coordinate.contains(",")){
                    String[] array = coordinate.split(",");
                    String latitude = array[0];
                    String longitude = array[1];
                    intent.putExtra("latitude", Double.valueOf(latitude));
                    intent.putExtra("longitude", Double.valueOf(longitude));
                    intent.putExtra("address",address);
                }


                startActivity(intent);
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
                    if(!TextUtils.isEmpty(mStrDoctorName)){
                        tvSelected.setText(mStrDoctorName);
                    }
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
            if(response instanceof VisitMemberResponse){
                if(response.getResultCode() == 1){
                    VisitMember member = ((VisitMemberResponse) response).getData().getVisit();
                    mTime = member.getTime();
                    mStrAddress = member.getAddress();
                    address = member.getAddressName();
                    mStrFloor = member.getAddressName();
                    mStrDoctorName = member.getDoctorName();
                    mRemark = member.getRemark();
                    coordinate = member.getCoordinate();
                    mStrDoctorID = member.getDoctorId();
                    List<String> picList = member.getImgUrls();
                    if(picList!=null&&picList.size()>0){
                        for(int i=0;i<picList.size();i++){
                            addListPic(picList.get(i));
                        }
                    }
                    tv_address.setText(mStrAddress);
                    tvSelected.setText(mStrDoctorName);
                    etRemark.setText(mRemark);
                    Date date = new Date(mTime);
                    String strDate = TimeFormatUtils.china_format_date(date);
                    String strWeek = TimeFormatUtils.week_format_date(date);
                    strTime = TimeFormatUtils.time_format_date(date);
                    tvWeek.setText(strWeek);
                    tvDate.setText(strDate);
                    tv_time_location.setText(strTime+" "+mStrFloor);
                    Gson g = new Gson();
                    List<GoodsGroupsModel> groups = member.getGoodsGroups();
                    if(groups!=null && groups.size()>0){
                        List<String> mediaNameStr = new ArrayList<String>();
                        List<String> mediaIdStr = new ArrayList<String>();
                        for(int i=0 ;i<groups.size();i++){
                            mediaNameStr.add(groups.get(i).getName());
                            mediaIdStr.add(groups.get(i).getId());
                        }
                        mStrMedia = g.toJson(groups);
                        String medieaName = g.toJson(mediaNameStr);
                        medieaName = medieaName.replace("[","").replace("]","").replaceAll("\"","");
                        tv_variety.setText(medieaName);
                    }

                    List<PersonModel> personList = member.getGroupDetails();
                    if(personList!=null && personList.size()>0){
                        jsonVisitPeople = g.toJson(personList);
                        for(int i=0;i<personList.size();i++){
                            if(personList.size()>=3){
                                visitor_avatar1.setVisibility(View.VISIBLE);
                                visitor_avatar2.setVisibility(View.VISIBLE);
                                visitor_avatar3.setVisibility(View.VISIBLE);
//                                ImageLoader.getInstance().displayImage(personList.get(0).getHeadPic(), visitor_avatar1, CommonUitls.getHeadOptions());
//                                ImageLoader.getInstance().displayImage(personList.get(1).getHeadPic(), visitor_avatar2, CommonUitls.getHeadOptions());
//                                ImageLoader.getInstance().displayImage(personList.get(2).getHeadPic(), visitor_avatar3, CommonUitls.getHeadOptions());

                                CustomImagerLoader.getInstance().loadImage(visitor_avatar1, personList.get(0).getHeadPic(),
                                        R.drawable.ic_default_head, R.drawable.ic_default_head);
                                CustomImagerLoader.getInstance().loadImage(visitor_avatar2, personList.get(1).getHeadPic(),
                                        R.drawable.ic_default_head, R.drawable.ic_default_head);
                                CustomImagerLoader.getInstance().loadImage(visitor_avatar3, personList.get(2).getHeadPic(),
                                        R.drawable.ic_default_head, R.drawable.ic_default_head);
                            }else{
                                if(personList.size()==1){
                                    visitor_avatar1.setVisibility(View.VISIBLE);
                                    visitor_avatar2.setVisibility(View.GONE);
                                    visitor_avatar3.setVisibility(View.GONE);
//                                    ImageLoader.getInstance().displayImage(personList.get(0).getHeadPic(), visitor_avatar1, CommonUitls.getHeadOptions());

                                    CustomImagerLoader.getInstance().loadImage(visitor_avatar1, personList.get(0).getHeadPic(),
                                            R.drawable.ic_default_head, R.drawable.ic_default_head);

                                }else if(personList.size()==2){
                                    visitor_avatar1.setVisibility(View.VISIBLE);
                                    visitor_avatar2.setVisibility(View.VISIBLE);
                                    visitor_avatar3.setVisibility(View.GONE);
//                                    ImageLoader.getInstance().displayImage(personList.get(0).getHeadPic(), visitor_avatar1, CommonUitls.getHeadOptions());
//                                    ImageLoader.getInstance().displayImage(personList.get(1).getHeadPic(), visitor_avatar2, CommonUitls.getHeadOptions());

                                    CustomImagerLoader.getInstance().loadImage(visitor_avatar1, personList.get(0).getHeadPic(),
                                            R.drawable.ic_default_head, R.drawable.ic_default_head);
                                    CustomImagerLoader.getInstance().loadImage(visitor_avatar2, personList.get(1).getHeadPic(),
                                            R.drawable.ic_default_head, R.drawable.ic_default_head);
                                }
                            }
                        }
                    }
                    setRemarkEnable();
                }
            } else if (response instanceof VisitEditEnableBean) {
                VisitEditEnableBean editEnable = (VisitEditEnableBean) response;
                if (null!=editEnable.data){
                    etRemarkEnable = editEnable.data.editStatus;
                    setRemarkEnable();
                }



            }else if(response instanceof Result){
                if(response.getResultCode() == 1){
                    if ("1".equals(state)) {
                        ToastUtil.showToast(JointVisitActivity.this, "提交成功");
                    }
                    if(MODE_FROM_VIST_LIST_ITEM == mMode){
                    }else if(MODE_FROM_SIGN == mMode || MODE_FROM_SIGN_LIST == mMode){
                        Intent intent = new Intent(this,MenuWithFABActivity.class);
                        startActivity(intent);
                        Intent broad_intent=new Intent();
                        broad_intent.setAction("action.to.signlisttoday");
                        sendBroadcast(broad_intent);
                    }else{
                        Intent intent = new Intent(this,VisitListActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }else {
                    ToastUtil.showToast(JointVisitActivity.this, response.getResultMsg());
                }
            }
        tv_title_save.setEnabled(true);
        tv_title_save.setClickable(true);
    }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
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
            ToastUtil.showToast(JointVisitActivity.this, "图片上传失败");
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
                if(isShowDel){
                    img_del.setVisibility(View.VISIBLE);
                }else{
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

    public String inputSteamToString(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1) {
            outStream.write(data, 0, count);
        }
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            goBackMethod();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void goBackMethod() {
        if(MODE_FROM_VIST_LIST_ITEM == mMode){
        }else if(MODE_FROM_SIGN == mMode){
            Intent intent = new Intent(this,MenuWithFABActivity.class);
            startActivity(intent);
        }else if(MODE_FROM_SIGN_LIST == mMode){
            finish();
        }else{
            Intent intent = new Intent(this,VisitListActivity.class);
            startActivity(intent);
        }
        finish();
    }
    /**
     * 判断是否可编辑
     */
    private void setRemarkEnable() {
        if (etRemarkEnable) {
            etRemark.setEnabled(true);
            desp2.setVisibility(View.VISIBLE);
            tv_title_save.setVisibility(View.VISIBLE);
            tvSelected.setText(mStrDoctorName);
            etRemark.setText(mRemark);
            del_desp.setVisibility(View.VISIBLE);
        }else {
            desp2.setVisibility(View.GONE);
            selectedPicture.remove(ADDPIC);
            etRemark.setEnabled(false);
            del_desp.setVisibility(View.INVISIBLE);
            tv_title_save.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            variety_arrow.setVisibility(View.INVISIBLE);
            name_arrow.setVisibility(View.INVISIBLE);
            variety_ray.setEnabled(false);
            vSelect.setEnabled(false);
            tvSelected.setTextColor(getResources().getColor(R.color.gray_666666));
            tv_variety.setTextColor(getResources().getColor(R.color.gray_666666));
            tvSelected.setHintTextColor(getResources().getColor(R.color.gray_666666));
            tv_variety.setHintTextColor(getResources().getColor(R.color.gray_666666));
            if(TextUtils.isEmpty(mStrDoctorName)){
                tvSelected.setHint("无");
            }else{
                tvSelected.setText(mStrDoctorName);
            }
            if(TextUtils.isEmpty(mRemark)){
                etRemark.setHint("无");
            }else{
                etRemark.setText(mRemark);
            }
            if (TextUtils.isEmpty(tv_variety.getText().toString())) {
                tv_variety.setHint("无");
            }
        }
    }
}
