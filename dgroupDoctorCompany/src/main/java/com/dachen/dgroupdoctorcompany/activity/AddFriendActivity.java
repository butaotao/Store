package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.UpLoadFriend;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/3/7.
 */
public class AddFriendActivity extends BaseActivity implements HttpManager.OnHttpListener{
    public final static int REQUEST_ADD_MEDIE = 101;

    @Bind(R.id.tv_addfriend)
    TextView tv_addfriend;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_frenddes)
    TextView tv_frenddes;
    @Bind(R.id.tv_customervisit)
    TextView tv_customervisit;
    @Bind(R.id.iv_headicon)
    ImageView iv_headicon;
    @Bind(R.id.tv_frendcompany)
    TextView tv_frendcompany;
    @Bind(R.id.et_edit)
    EditText et_edit;
    @Bind(R.id.tvSelected)
    TextView tvSelected;
    @Bind(R.id.tv_addfriendalertdes)
    TextView tv_addfriendalertdes;
    String id = "";
    public String name;
    public String phone;
    public String hospital;
    public String position;
    public String dep;
    public ArrayList<UpLoadFriend> fileInfos;
    String url;
    String json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);
        setTitle("添加医生");
        id =getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        hospital = getIntent().getStringExtra("hospital");
        position = getIntent().getStringExtra("dep");
        dep = getIntent().getStringExtra("position");
        tv_customervisit.setText(dep);
        tv_name.setText(name);
        tv_frendcompany.setText(hospital);
        tv_frenddes.setText(position);
        et_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if (key.length() > 25) {
                    et_edit.setText(key.substring(0, 25));
                    ToastUtil.showToast(AddFriendActivity.this, "最多只能输入25个字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //ImageLoader.getInstance().displayImage(url, iv_headicon);
        CustomImagerLoader.getInstance().loadImage(iv_headicon, url,
                R.drawable.head_icons_company, R.drawable.head_icons_company);
        tv_addfriendalertdes.setText(getTimeSpan("业务助理"));
    }
    private Spanned getTimeSpan( String point) {
        Spanned dTimeStr;//3cbaff
        dTimeStr = Html.fromHtml(
                "<u>" + point + "</u>"
        );
        //+ "<font color=\"#000000\">" + "去领" + "</font>");
        //Html.fromHtml("<u>使用html实现下划线样式</u>"));
        return dTimeStr;
    }
  /*  public String getFilesInfo(){
        fileInfos = new ArrayList<>();
        for (int i=0;i<3;i++){
            UpLoadFriend friend = new UpLoadFriend();
            for (int j=0;j<2;j++){
                UpLoadFriend.UploadFile filedes=  friend.new UploadFile();
                filedes.id = "id"+j+"";
                filedes.name= "name"+j;
                filedes.url = url;
                friend.fileList.add(filedes);
            }
            friend.goodsName = "goodsName=="+i;
            fileInfos.add(friend);
        }
        Gson gson = new Gson();
        json = gson.toJson(fileInfos);
        return json;
    }*/
    @OnClick(R.id.choiceMedie)
    void ChoiceMedie(){
        Intent intent = new Intent(AddFriendActivity.this,MedieManagementActivity.class);
        intent.putExtra("mode",MedieManagementActivity.MODE_ADD_FRIEND);
        startActivityForResult(intent,REQUEST_ADD_MEDIE);
    }
    @OnClick(R.id.tv_addfriend)
    void addFriend(){
        addDoctor(id);
    }
    @OnClick(R.id.rl_addfrienddes)
    void addFriendDes(){
        Intent intent = new Intent(this,AddFriendDesActivity.class);
        startActivity(intent);
    }
    public void addDoctor(String id){
        HashMap<String ,String > maps = new HashMap<>();
        String editText = et_edit.getText().toString();
        if (TextUtils.isEmpty(editText)){
            editText = "";
        }else if(editText.length()>25){
            ToastUtil.showToast(this,"验证信息最多只能25个字符");
        }
     //   ToastUtil.showToast(this,json);
        maps.put("access_token",  UserInfo.getInstance(this).getSesstion());
        maps.put("doctorId",id);
        maps.put("applyContent", editText);
        if (!TextUtils.isEmpty(json)){
            maps.put("drugList",json);
        }

        new HttpManager().post(this, "org/saleFriend/addFriend", Result.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        if (response.resultCode==1){
            ToastUtil.showToast(this, "发起申请成功");
            MActivityManager.getInstance().popActivity(this);
            MActivityManager.getInstance().popTo(MActivityManager.getInstance().currentActivity());
            //    MActivityManager.getInstance().popTo(MActivityManager.getInstance().currentActivity());

        /*    // Class<? extends Activity> claz1 = SearchDoctorActivity.class;
            Class<? extends Activity> claz2 = DoctorActivity.class;
            Class<? extends Activity> claz3 = HospitalActivity.class;
            Class<? extends Activity> claz4 = SearchDoctorResultActivity .class;
            MActivityManager.getInstance().finishAppointActivity(claz1);
            MActivityManager.getInstance().finishAppointActivity(claz2);
            MActivityManager.getInstance().finishAppointActivity(claz3);
            MActivityManager.getInstance().finishAppointActivity(claz4);*/
        }else {
            ToastUtil.showToast(this,"添加好友失败"+response.resultMsg);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_ADD_MEDIE && resultCode == Activity.RESULT_OK && null != data){
            String result = data.getStringExtra("doclist");
            int nselected = data.getIntExtra("selectedNum",0);
            if(nselected>0){
                tvSelected.setText(""+nselected);
            }
            json = result;
        }
        if (null==data&&!TextUtils.isEmpty(MedieDocumentActicity.json)){
            json = MedieDocumentActicity.json;
        }
    }
}
