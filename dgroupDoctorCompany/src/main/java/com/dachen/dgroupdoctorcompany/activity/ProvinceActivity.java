package com.dachen.dgroupdoctorcompany.activity;

/**
 * Created by Burt on 2016/2/23.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.ProvinceAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Province;
import com.dachen.dgroupdoctorcompany.utils.JsonMananger;
import com.dachen.dgroupdoctorcompany.views.NoScrollerListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * [地区页面]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-9-16
 *
 **/
public class ProvinceActivity extends BaseActivity implements OnClickListener, OnItemClickListener {



    private TextView tv_title;
    private Button btn_left;
    private TextView tv_beijing;
    private TextView tv_shanghai;
    private TextView tv_guangzhou;
    private TextView tv_shenzhen;
    List<Province> result;
    private NoScrollerListView refreshlistview;
    private ProvinceAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (result != null&&result.size()>0) {
                List<Province> list = (ArrayList<Province>) result;
                adapter.setDataSet(list);
                adapter.notifyDataSetChanged();
            }

            if (adapter.getDataSet() == null || adapter.getDataSet().size() == 0) {
                refreshlistview.setEmptyView(LayoutInflater.from(ProvinceActivity.this).inflate(R.layout.layout_empty, null));
            }
            closeLoadingDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.province_layout);
        result = new ArrayList<>();
      /*  tv_title = getViewById(R.id.tv_title);
        tv_title.setText("选择地区");*//*
        btn_left = getViewById(R.id.btn_left);
        btn_left.setOnClickListener(this);*/
        setTitle("条件添加3");
        tv_beijing = getViewById(R.id.tv_beijing);
        tv_beijing.setOnClickListener(this);
        tv_shanghai = getViewById(R.id.tv_shanghai);
        tv_shanghai.setOnClickListener(this);
        tv_guangzhou = getViewById(R.id.tv_guangzhou);
        tv_guangzhou.setOnClickListener(this);
        tv_shenzhen = getViewById(R.id.tv_shenzhen);
        tv_shenzhen.setOnClickListener(this);

        refreshlistview = getViewById(R.id.refreshlistview);
        refreshlistview.setOnItemClickListener(this);
        adapter = new ProvinceAdapter(this);
        refreshlistview.setAdapter(adapter);

        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream intput = ProvinceActivity.this.getAssets().open("province.json");
                    String city = inputSteamToString(intput);
                    result = JsonMananger.jsonToList(city, Province.class);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Province bean = adapter.getDataSet().get(position);
        String id = bean.getProID();
        Intent intent;
        if ("1".equals(id) || "2".equals(id) || "9".equals(id) || "27".equals(id) ||
                "32".equals(id) || "33".equals(id) || "34".equals(id)) {
              intent = new Intent(this,HospitalListActivity.class);
          /*  intent.putExtra("area", bean.getName());
            setResult(RESULT_OK, intent);
            finish();*/
            if("1".equals(id)){
                intent.putExtra("province","110000");
                intent.putExtra("citiyid", "110000");
            }else if("2".equals(id)){
                intent.putExtra("province","120000");
                intent.putExtra("citiyid", "120000");
            }
            else if("9".equals(id)){
                intent.putExtra("province","310000");
                intent.putExtra("citiyid", "310000");
            }
            else if("27".equals(id)){
                intent.putExtra("province","500000");
                intent.putExtra("citiyid", "500000");
            }
            else if("32".equals(id)){
                intent.putExtra("province","710000");
                intent.putExtra("citiyid", "710000");
            }
            else if("33".equals(id)){
                intent.putExtra("province","820000");
                intent.putExtra("citiyid", "820000");
            }
            else if("34".equals(id)){
                intent.putExtra("province","810000");
                intent.putExtra("citiyid", "810000");

            }
            intent.putExtra("area", bean.getName());

        }else{
            intent = new Intent(this, CityActivity.class);
            intent.putExtra("id", bean.getProID());
            intent.putExtra("area", bean.getName());
            //startActivityForResult(intent, 100);
        }
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent(this,HospitalListActivity.class);
        switch (v.getId()) {
          /*  case R.id.btn_left:
                finish();
                break;
*/
            case R.id.tv_beijing:
                intent.putExtra("area", "北京市");
                intent.putExtra("citiyid", "110000");
                intent.putExtra("province","110000");
                break;

            case R.id.tv_shanghai:
                intent.putExtra("area", "上海市");
                intent.putExtra("citiyid", "310000");
                intent.putExtra("province","310000");
                break;

            case R.id.tv_guangzhou:
                intent.putExtra("area", "广东省 广州市");
                intent.putExtra("citiyid", "440100");
                break;

            case R.id.tv_shenzhen:
                intent.putExtra("area", "广东省 深圳市");
                intent.putExtra("citiyid", "440300");
                break;
        }
        startActivity(intent);
    }
}