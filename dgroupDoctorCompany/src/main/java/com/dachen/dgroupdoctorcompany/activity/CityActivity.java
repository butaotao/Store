package com.dachen.dgroupdoctorcompany.activity;

/**
 * Created by Burt on 2016/2/23.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CityAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Area;
import com.dachen.dgroupdoctorcompany.entity.AreaModel;
import com.dachen.dgroupdoctorcompany.entity.City;
import com.dachen.dgroupdoctorcompany.utils.JsonMananger;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * [地区页面]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-9-16
 *
 **/
public class CityActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener<ListView>,HttpManager.OnHttpListener{

    private final int GETAREA = 1698;

    private TextView tv_title;
    private Button btn_left;
    private PullToRefreshListView refreshlistview;
    private CityAdapter adapter;
    private String id;
    private String area;
    List<City> result;
    List<City> result2;
    List<AreaModel> areaList;
    ViewStub vstub_title;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(result != null&&result.size()>0){
                List<City> list  = (ArrayList<City>)result;
                adapter.setDataSet(list);
                adapter.notifyDataSetChanged();
            }

            if(adapter.getDataSet() == null || adapter.getDataSet().size() == 0){
                refreshlistview.setEmptyView(LayoutInflater.from(CityActivity.this).inflate(R.layout.layout_empty, null));
            }
            closeLoadingDialog();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_layout);

        id = getIntent().getStringExtra("id");
        area = getIntent().getStringExtra("area");
        result = new ArrayList<>();
        result2 = new ArrayList<>();
        setTitle("条件添加2");
        refreshlistview = getViewById(R.id.refreshlistview);
        refreshlistview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        refreshlistview.setMode(Mode.PULL_FROM_START);
        refreshlistview.setOnItemClickListener(this);
       // refreshlistview.setOnRefreshListener(this);
        adapter = new CityAdapter(this);
        refreshlistview.setAdapter(adapter);

        showLoadingDialog();
        getCity();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getCity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream intput = CityActivity.this.getAssets().open("city.json");
                    String city = inputSteamToString(intput);
                    //List<City> result = new ArrayList<City>();
                    List<City> list = JsonMananger.jsonToList(city, City.class);
                    if(list != null && list.size() > 0){
                        for(City bean : list){
                            if(bean.getProID().equals(id)){
                                result.add(bean);
                            }
                        }
                    }


                    InputStream intputs = getResources().getAssets().open("area.json");
                    String area = inputSteamToString(intputs);
                    areaList= JsonMananger.jsonToList(area, AreaModel.class);
                    if(areaList != null && areaList.size() > 0){

                        for (int i=0;i<result.size();i++){
                            City city1 = result.get(i);
                            City citys = new City();
                             for(AreaModel bean : areaList){

                                 boolean isfind = false;
                                   if (bean.getName().equals(city1.getName())){
                                       citys.setName(bean.getName());
                                       citys.setCityID(bean.getCode());
                                       break;

                                   }
                                 if (null!=bean.getChildren()&&bean.getChildren().size()!=0){
                                    for (int k=0;k<bean.getChildren().size();k++){

                                        Area models = bean.getChildren().get(k);
                                        if (models.getName().equals(city1.getName())){
                                            citys.setName(models.getName());
                                            citys.setCityID(models.getCode());
                                            isfind = true;
                                            break;
                                        }
                                    }
                                 }
                                 if (isfind){
                                    break;
                                 }
                               }
                            result.set(i,citys);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public String inputSteamToString(InputStream in) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while((count = in.read(data, 0 , 1024)) != -1){
            outStream.write(data, 0, count);
        }
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        City bean = adapter.getDataSet().get(position-1);
        /*Intent intent = new Intent(this, CityActivity.class);
        intent.putExtra("area", area + " " + bean.getName());
        setResult(RESULT_OK, intent);*/
     //   ToastUtils.showToast(this, bean.toString());
        //finish();id
        Intent intent = new Intent(this,HospitalListActivity.class);
        intent.putExtra("citiyid", bean.getCityID());
        startActivity(intent);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode, data);
        //ToastUtils.showToast(this,);
        //finish();
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        showLoadingDialog();
        getCity();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent = new Intent(this,ProvinceActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
