package com.dachen.dgroupdoctorcompany.activity;

/**
 * Created by Burt on 2016/2/20.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.ErData;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆界面
 *
 * @author
 *
 */
public class MyQRActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MyQRActivity.class.getSimpleName();

    @Nullable
    @Bind(R.id.qr_name)
    TextView mName;
    @Nullable
    @Bind(R.id.qr_QR_code)
    ImageView qrCode_img;
    @Nullable
    @Bind(R.id.qr_avatar)
    ImageView avatar_img;

    String qrcodeUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);
        ButterKnife.bind(this);
//


        initViews();
        getAvatarImage();
    }
    private void getAvatarImage() {
        // 获取头像

        //TODO
			/*if(user!=null)
			{
				String userId = user.getUserId();
				String headPicFileName = user.getHeadPicFileName();
				String avatarUrl = StringUtils.getAvatarUrl(userId, headPicFileName);
				if(!TextUtils.isEmpty(avatarUrl))
				{
					ImageLoader.getInstance().displayImage(avatarUrl, head_icon);
				}
				else head_icon.setImageResource(R.drawable.head_icon);
			}
			else {//logins.data.getUser().getUserId()+"head_url", url);
*/				String avatarUri = SharedPreferenceUtil.getString(this,SharedPreferenceUtil.getString(this,"id", "") + "head_url", "");
        LogUtils.burtLog("avatarUri===" + avatarUri);
        if(!TextUtils.isEmpty(avatarUri))
        {
            ImageLoader.getInstance().displayImage(avatarUri, avatar_img);
        }
        else avatar_img.setImageResource(R.drawable.head_icon);
        //}
    }
     void initViews() {
         String name = SharedPreferenceUtil.getString(this,"username","");
            mName.setText(name);



        getQRCodeData();

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Nullable
    @OnClick(R.id.back_step_btn)
    void onBackBtnClicked() {
        finish();
    }

    private void getQRCodeData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", SharedPreferenceUtil.getString(this,"id",""));
        params.put("userType", Constants.USER_TYPE);
        params.put("access_token", SharedPreferenceUtil.getString(this,"session", ""));
       /* Map<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1",)*/
        new HttpManager().post(this, "health/qr/createQRImage",
                ErData.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result instanceof ErData){
                            ErData datas = (ErData) result;
                            if (null!=datas&&null!=datas.data&&!TextUtils.isEmpty(datas.data.url)){
                                qrcodeUrl = datas.data.url;
                                showAvatar(qrCode_img, qrcodeUrl);
                            }

                        }

                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },false, 1,"9000");



    }
    private void showAvatar(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv);
    }

}
