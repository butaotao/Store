package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.DoctorsList;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2DoctorChatActivity;
import com.dachen.dgroupdoctorcompany.utils.CustomDialog;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/2/25.
 */
public class DoctorDetailActivity extends BaseActivity implements HttpManager.OnHttpListener,SessionGroupCallback{
    @Nullable
    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Nullable
    @Bind(R.id.tv_hospital_des)
    TextView tv_hospital_des;
    @Nullable
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Nullable
    @Bind(R.id.tv_part)
    TextView tv_part;
    @Nullable
    @Bind(R.id.tv_position)
    TextView tv_position;
    @Nullable
    @Bind(R.id.tv_phone)
    TextView tv_phone;
   /* @Nullable
    @Bind(R.id.tv_site)
    TextView tv_site;*/
    @Nullable
    @Bind(R.id.tv_goodat)
    TextView tv_goodat;
    @Nullable
    @Bind(R.id.btn_sendinfo)
    TextView btn_sendinfo;

    @Nullable
    @Bind(R.id.btn_oftencontact)
    TextView btn_oftencontact;
    Doctor info;
    DoctorDao doctorDao;
    String phonenum = "";
    @Nullable
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Nullable
    @Bind(R.id.tv_back)
    TextView tv_back;
    CustomDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctordetail);
        ButterKnife.bind(this);
        doctorDao = new DoctorDao(this);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.transparent));
        findViewById(R.id.line_titlebar).setVisibility(View.GONE);
        Bundle bundle = getIntent().getBundleExtra("doctordetail");
        dialog = new CustomDialog(this);
        tv_back.setTextColor(getResources().getColor(R.color.white));
        iv_back.setBackgroundResource(R.drawable.back_xhdpi);
        if (bundle!=null){
            info = (Doctor) bundle.getSerializable("doctordetail");
            tv_hospital_des.setText(info.hospital);
            tv_name.setText(info.name);
            tv_position.setText(info.title);
            tv_part.setText(info.departments);
            tv_goodat.setText(info.skill);
            tv_phone.setText(info.telephone);
           // setTitle(info.hospital);
            setTitle("");
            phonenum = info.telephone;
           // ImageLoader.getInstance().displayImage(info.headPicFileName, head_icon);
            CustomImagerLoader.getInstance().loadImage(head_icon, info.headPicFileName,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);
        }else {
            setTitle("");
        }
    }
    @Nullable
    @OnClick(R.id.iv_phonecall)
    void callPhone(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "" + phonenum));

        startActivity(intent);

    };
    @Nullable
    @OnClick(R.id.iv_messagesend)
    void sendMessage(){
//        Uri uri = Uri.parse("smsto://"+phonenum);
        Uri uri = Uri.parse("smsto:"+phonenum);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);

        intent.putExtra("sms_body", "");

        startActivity(intent);

    }

    @OnClick(R.id.btn_oftencontact)
    void onDeleteDoctor(){
        deleteDoctor(info.userId);

    }
    @OnClick(R.id.btn_sendinfo)
    void sendMsg(){
        createChatGroup(info.userId);
    }
    public void deleteDoctor(final String id){
        //enterprise/doctor/search


        dialog.showDialog(null, "确定要删除该医生好友?",R.string.sure,R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                HashMap<String ,String > maps = new HashMap<>();
                maps.put("access_token", UserInfo.getInstance(DoctorDetailActivity.this).getSesstion());
                maps.put("userId",id);

                new HttpManager().post(DoctorDetailActivity.this, "org/saleFriend/deleteFriend", DoctorsList.class,
                        maps, DoctorDetailActivity.this,
                        false, 1);
                dialog.dimissDialog();
                // ToastUtil.showToast(HospitalActivity.this,des.name);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        });




    }

    @Override
    public void onSuccess(Result response) {
        if (response.resultCode==1){
            closeLoadingDialog();
            doctorDao.deleteByid(info.userId);
            ToastUtil.showToast(this, "删除医生");
            finish();
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }

    private void createChatGroup(String userId){
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        SessionGroup groupTool=new SessionGroup(this);
        groupTool.setCallback(this);
        groupTool.createGroup(userIds, "3_10");
    }

    @Override
    public void onGroupInfo(Data data, int what) {
        ChatGroupDao dao=new ChatGroupDao();
        dao.saveOldGroupInfo(data);
        Represent2DoctorChatActivity.openUI(this, data.gname, data.gid, info.userId);
    }

    @Override
    public void onGroupInfoFailed(String msg) {
        ToastUtil.showToast(this, "创建会话失败");
    }
}
