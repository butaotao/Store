package com.dachen.mediecinelibraryrealize.activity;

/**
 * Created by Burt on 2016/6/3.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.mediecinelibraryrealize.adapter.MedicienSearchListAdapter;
import com.dachen.mediecinelibraryrealizedoctor.activity.*;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoBean;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoFactory;
import com.dachen.mediecinelibraryrealizedoctor.entity.MeidecineSearchList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealize.adapter.MedicienSearchListAdapter;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoBean;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoFactory;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MeidecineSearchList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author znouy 药品搜索列表
 */
public class PatientMedicienSearchListActivity extends com.dachen.mediecinelibraryrealizedoctor.activity.BaseActivity implements
        View.OnClickListener, HttpManager.OnHttpListener {
    MedicienSearchListAdapter myAdapter;
    private ListView mLv_showsearchlist;
    private List<MedicienInfoFactory> mInfo_list  ;
    private TextView mTv_title;
    private RelativeLayout mRl_back;
    private String id = "";
    Intent intent  ;
    public int checkPosition  = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        initView();
        intent = new Intent();;

    }

    private void initData() {
//		String trade_name = getIntent().getStringExtra("trade_name");
//		if (!TextUtils.isEmpty(trade_name)) {
//			HashMap<String, String> interfaces = new HashMap<String, String>();
//
//			try {
//				String value = URLEncoder.encode(trade_name, "utf-8");
//				interfaces.put("trade_name", value);
//				new HttpManager().get(this,
//						Params.getInterface("invoke", "c_Goods.select"),
//						MedicienInfoBean.class, interfaces, this, false, 2);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//
//		}

        String id = this.getIntent().getStringExtra("id");
 //		id = "573c0c2ada1a9e20a0709f8e";
        Map<String, String> params = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(PatientMedicienSearchListActivity.this).getSesstion();
        params.put("access_token", access_token);
        params.put("groupId", id);
        params.put("source", ""+0);//来源 0 平台 1 药企
        new HttpManager().get(this, Constants.SEARCH_DRUG_BY_GROUPID,
                MeidecineSearchList.class,
                params,this, false, 1);

    }
    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);


    }
    private void initView() {
        setContentView(com.dachen.mediecinelibraryrealizedoctor.R.layout.activity_mediciensearchlist);
        ViewStub vstub_title = (ViewStub) findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.vstub_title);
        RelativeLayout ll_sub = (RelativeLayout) findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.ll_sub);
        View view = vstub_title.inflate(this, com.dachen.mediecinelibraryrealizedoctor.R.layout.viewstub_text, ll_sub);
        TextView tv = (TextView) view.findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.tv_save);
        tv.setText("确定");
        tv.setOnClickListener(this);
        // title_bar
        mTv_title = (TextView) findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.tv_title);
        mRl_back = (RelativeLayout) findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.rl_back);
        mRl_back.setOnClickListener(this);
        mInfo_list = new ArrayList<MedicienInfoFactory>();;
        String trade_name = getIntent().getStringExtra("trade_name");
        id = getIntent().getStringExtra("id");
        mTv_title.setText(trade_name);
        myAdapter = new MedicienSearchListAdapter(this,mInfo_list);
        mLv_showsearchlist = (ListView) findViewById(com.dachen.mediecinelibraryrealizedoctor.R.id.lv_showsearchlist);
        // mLv_showsearchlist.setAdapter(myAdapter);
        // myAdapter.notifyDataSetChanged();
        initData();
    }

    @Override
    public void onFailure(Exception arg0, String arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response) {
            MeidecineSearchList meidecineSearchList = (MeidecineSearchList) response;
            MedicienInfoBean bean = new MedicienInfoBean();
            if(response.getResultCode() == 1){
                if(null != meidecineSearchList.data && meidecineSearchList.data.size()>0){
                    List<MedicienInfoFactory> medicienInfoFactoryList= new ArrayList<>();
//					for(int i=0; i<meidecineSearchList.data.size();i++){
//						MeidecineSearchList.Data data = meidecineSearchList.data.get(i);
//						MedicienInfoFactory medicienInfoFactory = new MedicienInfoFactory();
//						medicienInfoFactory.title = data.title;
//						medicienInfoFactory.manufacturer = data.manufacturer;
//						medicienInfoFactory.check = data.
//					}

                    mInfo_list = bean.info_list;
                    if (null == mInfo_list || mInfo_list.size() < 1) {
                        mLv_showsearchlist.setVisibility(View.GONE);
                        return;
                    } else {
                        mLv_showsearchlist.setVisibility(View.VISIBLE);
                    }
                    // 刷新数据
//			mLv_showsearchlist.setAdapter(myAdapter);
                    myAdapter = new MedicienSearchListAdapter(this,mInfo_list);
                    myAdapter.notifyDataSetChanged();
                    mLv_showsearchlist.setAdapter(myAdapter);
                }
            }else {
                ToastUtils.showResultToast(this, response);
            }

        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.dachen.mediecinelibraryrealizedoctor.R.id.rl_back) {
            finish();
        }else if (v.getId() == com.dachen.mediecinelibraryrealizedoctor.R.id.tv_save){
            setResult(RESULT_OK, intent);
            finish();
        }

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

}
