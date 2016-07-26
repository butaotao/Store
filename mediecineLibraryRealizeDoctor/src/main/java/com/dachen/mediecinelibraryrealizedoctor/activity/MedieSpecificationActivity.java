package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.NetConfig;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.GetMedieById;
import com.dachen.mediecinelibraryrealizedoctor.entity.GetMedieByIdData;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.utils.ImageUrl;

public class MedieSpecificationActivity extends BaseActivity implements OnClickListener, OnHttpListener {
    WebView wv;
    HashMap<String, String> maps;
    TextView tv_guige;
    TextView tv_leibie;
    TextView tv_num;
    ImageView iv_image;
    RelativeLayout rl_back;
    TextView tv_title;
    LinearLayout ll_leibie;
    TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediespefication);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        ll_leibie = (LinearLayout) findViewById(R.id.ll_leibie);
        tv_name = (TextView) findViewById(R.id.tv_name);
        //Bundle beans = getIntent().getBundleExtra("MedicineInfo");
        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        tv_title.setText("药品详情");
        getData(id);

        maps = MedicineApplication.getMapConfig();
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        String urlsp = maps.get("url");
        //192.168.3.7:9002/web/api/goods_manual.jsp?id=AC0DB1BC875946268674B59183F31C81

        tv_guige = (TextView) findViewById(R.id.tv_guige);
        tv_leibie = (TextView) findViewById(R.id.tv_leibie);
        tv_num = (TextView) findViewById(R.id.tv_num);
        wv = (WebView) findViewById(R.id.web);
        WebSettings settings = wv.getSettings();
      /*  settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//设置，可能的话使所有列的宽度不超过屏幕宽度
        settings.setLoadWithOverviewMode(true);//设置webview自适应屏幕大小*/
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setUseWideViewPort(true);

        //wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }else if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
      /*  if (!TextUtils.isEmpty(linkUri)) {
            wv.loadUrl(linkUri);
            wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //wv.loadUrl(url);
                    wv.loadData("","text/html", "UTF-8");
                    return true;
                }
            });
        }*/
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.rl_back) {
            finish();
        }

    }

    @Override
    public void onFailure(Exception arg0, String arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Result arg0) {
        if (arg0.resultCode == 1) {
            if (arg0 instanceof GetMedieByIdData) {
                GetMedieByIdData infos = (GetMedieByIdData) arg0;
                if (null != infos.data) {
                    final GetMedieById info = infos.data;
                    // TODO Auto-generated method stub
                    if (!TextUtils.isEmpty(info.form)) {
                        ll_leibie.setVisibility(View.VISIBLE);
                        tv_leibie.setText(info.form);
                    } else {
                        ll_leibie.setVisibility(View.INVISIBLE);
                    }
                    if (null != info.specification) {
                        tv_guige.setText(info.specification);
                    }
                    if (!TextUtils.isEmpty(info.number)) {
                        tv_num.setText(info.number);
                    }
                    if (!TextUtils.isEmpty(info.manual)) {
                       // wv.loadData(""+info.manual, "text/html", "UTF-8");
                        wv.loadData("" + info.manual, "text/html; charset=UTF-8", null);//
                    }
                    String name = CompareDatalogic.getShowName2(info.general_name, info.general_name, info.title);

                    tv_name.setText(name);
                    String url = info.image;

                    if (!TextUtils.isEmpty(url)) {
                        String ip = maps.get("ip");

                        String urls = ImageUtil.getUrl(url, ip, maps.get("session"), 1);

                        CustomImagerLoader.getInstance().loadImage(iv_image,
                                urls);
                    } else {
                        iv_image.setImageResource(R.drawable.image_download_fail_icon);

                    }
                }
            }
        }else {
            ToastUtils.showResultToast(this, arg0);
        }
    }

    @Override
    public void onSuccess(ArrayList arg0) {
        // TODO Auto-generated method stub

    }

    public void getData(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", UserInfo.getInstance(this).getSesstion());
        params.put("id", id);
////goods/viewGoods

        new HttpManager().post(this, "org/goods/viewGoods",
                GetMedieByIdData.class,
                params, this, false, 1);
    }
}
