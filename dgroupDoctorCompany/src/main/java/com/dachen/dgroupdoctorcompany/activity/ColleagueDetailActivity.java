package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2RepresentChatActivity;
import com.dachen.dgroupdoctorcompany.utils.ConditionLogic;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/2/25.
 */
public class ColleagueDetailActivity extends BaseActivity implements View.OnClickListener,SessionGroupCallback {
    @Nullable
    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Nullable
    @Bind(R.id.tv_name)
    TextView tv_name;
/*    @Nullable
    @Bind(R.id.tv_sex)
    TextView tv_sex;*/

    @Nullable
    @Bind(R.id.tv_depart)
    TextView tv_depart;
    @Nullable
    @Bind(R.id.tv_position)
    TextView tv_position;
    @Nullable
    @Bind(R.id.tv_phone)
    TextView tv_phone;
     @Nullable
    @Bind(R.id.btn_sendinfo)
    TextView btn_sendinfo;
    @Nullable
    @Bind(R.id.btn_oftencontact)
    TextView btn_oftencontact;
    String phonenum;

    @Nullable
    @Bind(R.id.rl_btns)
    RelativeLayout rl_btns;
    @Nullable
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Nullable
    @Bind(R.id.tv_back)
    TextView tv_back;
    LinearLayout ll_phone;
    private CompanyContactListEntity entity;
    private SessionGroup groupTool;
    String manager;
    private String mId;
    ImageView iv_lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleaguedetail);
        ButterKnife.bind(this);
        manager = getIntent().getStringExtra("manager");
        Bundle bundle = getIntent().getBundleExtra("peopledes");
        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        if (!TextUtils.isEmpty(manager)){
            ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
            RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
            View view = vstub_title.inflate(this,  R.layout.stub_viewtext, rl);
            TextView tv = (TextView) view.findViewById(R.id.tv_search);
            tv.setOnClickListener(this);
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.transparent));
        findViewById(R.id.line_titlebar).setVisibility(View.GONE);
        setTitle("");
        tv_back.setTextColor(getResources().getColor(R.color.white));
        iv_back.setBackgroundResource(R.drawable.back_xhdpi);

        if(getIntent().getStringExtra("id")!=null){
            mId = getIntent().getStringExtra("id");
        }

        if (null!=bundle){
            entity = (CompanyContactListEntity) bundle.getSerializable("peopledes");
            //ToastUtil.showToast(this,entity.name);
           // setTitle("" + entity.name);
            tv_name.setText(entity.name);
            tv_depart.setText(entity.department);
            tv_position.setText(entity.position);
            tv_phone.setText(entity.telephone);
            phonenum = entity.telephone;
            findViewById(R.id.btn_sendinfo).setOnClickListener(this);
            findViewById(R.id.btn_oftencontact).setOnClickListener(this);
            ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
                    ll_phone.setOnClickListener(this);
            String url = "";
            if (!TextUtils.isEmpty(entity.headPicFileName)){
                CustomImagerLoader.getInstance().loadImage( head_icon, entity.headPicFileName,
                        R.drawable.head_icons_company, R.drawable.head_icons_company);
            }
        }else{
            CompanyContactDao dao = new CompanyContactDao(this);
            List<CompanyContactListEntity> datas = dao.queryByUserId(mId);
            if(datas.size()>0){
                entity = datas.get(0);
                tv_name.setText(entity.name);
                tv_depart.setText(entity.department);
                tv_position.setText(entity.position);
                tv_phone.setText(entity.telephone);
                phonenum = entity.telephone;
                findViewById(R.id.btn_sendinfo).setOnClickListener(this);
                findViewById(R.id.btn_oftencontact).setOnClickListener(this);
                ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
                ll_phone.setOnClickListener(this);
                String url = "";
                if (!TextUtils.isEmpty(entity.headPicFileName)){
                    CustomImagerLoader.getInstance().loadImage( head_icon, entity.headPicFileName,
                            R.drawable.head_icons_company, R.drawable.head_icons_company);
                }
            }
        }
        if (entity!=null&&!TextUtils.isEmpty(entity.userId)){
            if (entity.userId.equals(UserInfo.getInstance(this).getId())){
                rl_btns.setVisibility(View.GONE);
            }else {
                rl_btns.setVisibility(View.VISIBLE);
            }
            groupTool=new SessionGroup(this);
            groupTool.setCallback(this);
        }
        if (entity!=null&&!ConditionLogic.isAllowCall(entity.userId)){
            ll_phone.setVisibility(View.GONE);
        }else {
            ll_phone.setVisibility(View.VISIBLE);
        }
        if (!ConditionLogic.isAllow(this)){
           tv_phone.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            if (null!=entity&&!TextUtils.isEmpty(entity.telephone)&&entity.telephone.length()>=6){
                tv_phone.setText(entity.telephone.substring(0,6));
            }
            iv_lock.setVisibility(View.VISIBLE);
        }else {
            iv_lock.setVisibility(View.GONE);
        }
    }
    @Nullable
    @OnClick(R.id.iv_phonecall)
    void callPhone(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ""+phonenum));

        startActivity(intent);

    }

    @Nullable
    @OnClick(R.id.iv_messagesend)
    void sendMessage(){
//        Uri uri = Uri.parse("smsto://"+phonenum);
        Uri uri = Uri.parse("smsto:"+phonenum);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);

        intent.putExtra("sms_body", "");

        startActivity(intent);

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId() ){
            case R.id.btn_sendinfo:
                if(entity==null)
                    return;
                createChatGroup(entity.userId);
                break;
            case R.id.btn_oftencontact:
                break;
            case R.id.ll_phone:
                break;
            case R.id.tv_search:
                if (null!=entity){
                    Intent intent = new Intent(this,DeleteColleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("colleage",entity);
                    intent.putExtra("colleage",bundle);
                    startActivity(intent);
                }
                break;
        }
    }
    private void createChatGroup(String userId){
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        groupTool.createGroup(userIds, "10");
    }

    @Override
    public void onGroupInfo(Data data, int what) {
        ChatGroupDao dao=new ChatGroupDao();
        dao.saveOldGroupInfo(data);
        Represent2RepresentChatActivity.openUI(this, data.gname, data.gid, entity.userId);
    }

    @Override
    public void onGroupInfoFailed(String msg) {
        ToastUtil.showToast(this, "创建会话失败");
    }
}
