package com.dachen.mediecinelibraryrealizedoctor.activity; 

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.NetConfig;
import com.dachen.medicine.view.ScrollTabView;
import com.dachen.medicine.view.ScrollTabView.OnInitView;
import com.dachen.medicine.view.ScrollTabView.OnViewPagerSelectedListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.entity.GetMedieById;
import com.dachen.mediecinelibraryrealizedoctor.entity.GetMedieByIdData;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.CompareDatalogic;

public class GetMedieDetaiInfo extends Dialog implements View.OnClickListener,OnInitView,HttpManager.OnHttpListener {
    private Activity mActivity;
    private OnClickListener listener;
    LinearLayout lv_photo_cancel;
    LinearLayout lv_photo_choicephotofromalbum;
    LinearLayout lv_photo_choicephotofromcamera;
    LinearLayout spaceforcancel;
    String methodOne;
    String methodTwo;
    TextView dialog_methodtwo;
    TextView dialog_showmethodone;
    private ScrollTabView scrollTabView;
    MedicineInfo info;
    TextView tv_classes;
    TextView tv_sellclass;
    TextView tv_specification;
    TextView tv_packaging;
    TextView tv_num;
    TextView tv_generalname; 
    HashMap<String, String> maps ;
    private WebView wv;
	private String linkUri;
    public GetMedieDetaiInfo(Activity activity, OnClickListener l,String methodOne,String methodTwo) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.listener = l;
        this.methodOne = methodOne;
        this.methodTwo = methodTwo;
        
    }
    public GetMedieDetaiInfo(Activity activity,MedicineInfo info) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity; 
        this.info = info;
    }
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
     
        scrollTabView = new ScrollTabView(mActivity,R.layout.dialog_medieinfo,2,true,new OnViewPagerSelectedListener() {
			@Override
			public void onPageSelected(int position) {
               
			}
		});
		setContentView(scrollTabView); 
		 maps = MedicineApplication.getMapConfig();
	    	String ip = maps.get("ip");
	    	String urlsp = maps.get("url");
	    	 //192.168.3.7:9002/web/api/goods_manual.jsp?id=AC0DB1BC875946268674B59183F31C81
	    	if (null!=info.goods&&!TextUtils.isEmpty(info.goods.id)) {
	    		 linkUri = urlsp+"/goods_manual.jsp?id="+info.goods.id;
	    		 linkUri = linkUri.replace( NetConfig.MEDIELWEBFILES, "web/kangzhe");
			}
	       
		scrollTabView.setViewPagerItemView(new int[]{R.layout.layout_basemedieinfo,R.layout.layout_specification}, new OnInitView() {
			 
			@Override
			public void onInit(ArrayList<View> view) { 
				initView(view.get(1), view.get(2));
			}
			 
		});
		this.findViewById(R.id.iv_closed).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CloseDialog();
			}
		});
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (metric.widthPixels);
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true); 
        ImageView iv_pic = (ImageView) findViewById(R.id.iv_pic);
        String url = info.goods$image;
		String goodsname="";
			goodsname = info.title;
		String name = CompareDatalogic.getShowName(info.goods$general_name, info.goods$trade_name, goodsname);


       if(TextUtils.isEmpty(url)){
		   url = info.goods$image_url;
	   }
		if (!TextUtils.isEmpty(url)) {

			String urls = ImageUtil.getUrl(url,ip,maps.get("session"),1);
			CustomImagerLoader.getInstance().loadImage(iv_pic,
					urls); 
		} else {
			iv_pic .setImageResource(R.drawable.image_download_fail_icon);

		}
		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_weight = (TextView) findViewById(R.id.tv_weight);
		TextView tv_company = (TextView) findViewById(R.id.tv_company);
		findViewById(R.id.ll_cancel).setOnClickListener(this);
	//	String name = CompareDatalogic.getShowName(info.goods$general_name,info.trade_name,info.title);


		SpannableString names = com.dachen.mediecinelibraryrealizedoctor.utils.
				CompareDatalogic.getShowName3(info.goods$general_name, info.goods$trade_name,goodsname,
				 tv_name, info.goods$specification, mActivity);
		tv_name.setText(names);

		//tv_name.setText();
		if (!TextUtils.isEmpty(info.goods$specification)) {
			tv_weight.setText(info.goods$specification);
		}
		if (!TextUtils.isEmpty(info.goods$manufacturer)) {
			tv_company.setText(info.goods$manufacturer);
		}
    }

    private void initView(View v1,View v2) {  
    	//药品类别:
    	tv_classes = (TextView) v1.findViewById(R.id.tv_classes);
    	//药品经营类别:
    	tv_sellclass = (TextView) v1.findViewById(R.id.tv_sellclass);
    	//规格:
    	tv_specification = (TextView) v1.findViewById(R.id.tv_specification);
    	//包装规格:
    	tv_packaging = (TextView) v1.findViewById(R.id.tv_packaging);
    	//批准文号:
    	tv_num = (TextView) v1.findViewById(R.id.tv_num);
    	//通用名:
    	tv_generalname = (TextView) v1.findViewById(R.id.tv_generalname); 
    	
    	/*if (!TextUtils.isEmpty(info.goods$general_name)) {
    		tv_generalname.setText(info.goods$general_name);
		}*/

		String goodsname="";
		if (null!=info.goods){
			goodsname = info.goods.title;
		}
		String name = CompareDatalogic.getShowName(info.goods$general_name, info.goods$trade_name, goodsname);
		tv_generalname.setText(info.goods$general_name);
    	if (!TextUtils.isEmpty(info.goods$pack_specification)) {
			tv_packaging.setText(info.goods$pack_specification);
		}
    	if (null!=info.goods$form&&!TextUtils.isEmpty(info.goods$form.name)) {
    		tv_sellclass.setText(info.goods$form.name);
		}
    	if (null!=info.goods$type&&!TextUtils.isEmpty(info.goods$type.title)) {
    		tv_classes.setText(info.goods$type.title);
		}
    	if (!TextUtils.isEmpty(info.goods$number)) {
    		tv_num.setText(info.goods$number);
		} 
    	 if (!TextUtils.isEmpty(info.goods$specification)) {
    		 tv_specification.setText(info.goods$specification);
		}
		getData(info.goodId);
    	wv = (WebView) v2.findViewById(R.id.wv_advertise_detail);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(false);
		wv.getSettings().setUseWideViewPort(true);
		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 120) {
			wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
		}else if (mDensity == 160) {
			wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		}else if (mDensity == 240) {
			wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		}
		 /*if (!TextUtils.isEmpty(linkUri)) {
			 wv.loadUrl(linkUri); 
				wv.setWebViewClient(new WebViewClient() {

					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						// TODO Auto-generated method stub
						wv.loadUrl(url);
						return true;
					}
				});
		}*/
		
    }
	public void getData(String id) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", UserInfo.getInstance(mActivity).getSesstion());
		params.put("id", id);
		new HttpManager().post(mActivity, "org/goods/viewGoods",
				GetMedieByIdData.class,
				params, this, false, 1);
	}
    @Override
    public void onClick(View v) {
    	if (v.getId() == R.id.ll_cancel) {
    		CloseDialog();
		}
    }

    public void ShowDialog(){
		if(!isShowing()&&info.goods$manufacturer!=null){

			show();
			if (null!=scrollTabView){
				scrollTabView.setSelectedTab(0);
			}
		}

	}
	
	public void CloseDialog(){
		if(isShowing())
			dismiss();
	}

	@Override
	public void onSuccess(Result arg0) {
		if (arg0 instanceof GetMedieByIdData) {
			GetMedieByIdData infos = (GetMedieByIdData) arg0;
			if (infos.data!=null){
				GetMedieById info = infos.data;
			/*	tv_generalname.setText(info.general_name);
				if (!TextUtils.isEmpty(info.pack_specification)) {
					tv_packaging.setText(info.pack_specification);
				}
				if (!TextUtils.isEmpty(info.form)) {
					tv_sellclass.setText(info.form);
				}
				if (null!=info.title&&!TextUtils.isEmpty(info.title)) {
					tv_classes.setText(info.title);
				}
				if (!TextUtils.isEmpty(info.number)) {
					tv_num.setText(info.number);
				}
				if (!TextUtils.isEmpty(info.specification)) {
					tv_specification.setText(info.specification);
				}*/
				if (null!=infos.data&&!TextUtils.isEmpty(info.manual)) {
					wv.loadData("" + info.manual, "text/html; charset=UTF-8", null);//

				}
			}

		}
	}

	@Override
	public void onSuccess(ArrayList response) {

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {

	}

	public interface OnClickListener {
        public void btnCameraClick(View v);//

        public void btnPhotoClick(View v);//
    }



	@Override
	public void onInit(ArrayList<View> arg0) {
		// TODO Auto-generated method stub
		
	}
}
