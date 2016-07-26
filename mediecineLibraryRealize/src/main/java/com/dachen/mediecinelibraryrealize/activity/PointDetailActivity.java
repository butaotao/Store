package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ScrollTabView;
import com.dachen.medicine.view.ScrollTabView.OnInitView;
import com.dachen.medicine.view.ScrollTabView.OnViewPagerSelectedListener;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientPointGet;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientPointXH;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints.Potient;
import com.dachen.mediecinelibraryrealize.entity.PointGivenEntity;
import com.dachen.mediecinelibraryrealize.entity.PointsDetail;
import com.dachen.mediecinelibraryrealize.entity.PointsGet;
import com.dachen.mediecinelibraryrealize.entity.PointsGet.Point;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.SomeBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PointDetailActivity extends BaseActivity implements OnHttpListener, OnClickListener {
    private ScrollTabView scrollTabView;
/*	 TextView name1;
     TextView weight1;
	 TextView des1;
	 TextView point1;
	 
	 TextView name2;
	 TextView weight2;
	 TextView des2;
	 TextView point2;
	 */

    ListView listview1;
    ListView listview2;
    AdapterPatientPointGet adaper2;
    AdapterPatientPointXH adaper1;
    RelativeLayout rl_back;
    String id = "";
    List<Potient> patientpoints2;
    List<Point> patientpoints1;
    TextView title;
    RelativeLayout rl_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        scrollTabView = new ScrollTabView(this, R.layout.activity_point, 2, true, new
                OnViewPagerSelectedListener() {
                    @Override
                    public void onPageSelected(int position) {

                    }
                });
        setContentView(scrollTabView);
        RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
        ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        View view = vstub_title.inflate(this, R.layout.layout_modi_time, ll_sub);
        TextView tv_save = (TextView) view.findViewById(R.id.tv_save);
        tv_save.setText("积分说明");
        rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
        rl_plus.setOnClickListener(this);
        String id = getIntent().getStringExtra("id");
        showLoadingDialog();
        getPointInfo(id);
        getPointInfo_page1(id);
        scrollTabView.setViewPagerItemView(new int[]{R.layout.layout_getpoint1, R.layout
                .layout_getpoint2}, new OnInitView() {

            @Override
            public void onInit(ArrayList<View> view) {
                initView(view.get(1), view.get(2));

            }

        });
    }

    private void initView(View v1, View v2) {
		/*   name1 = (TextView) v1.findViewById(R.id.tv_name);
		   weight1 =(TextView) v1.findViewById(R.id.tv_weight);
		   des1 = (TextView) v1.findViewById(R.id.tv_des);
		   point1 = (TextView) v1.findViewById(R.id.tv_point);
		   
		   name2 = (TextView) v2.findViewById(R.id.tv_name);
		   weight2 =(TextView) v2.findViewById(R.id.tv_weight);
		   des2 = (TextView) v2.findViewById(R.id.tv_des);
		   point2 = (TextView) v2.findViewById(R.id.tv_point);*/
        listview1 = (ListView) v1.findViewById(R.id.listview1);
        listview2 = (ListView) v2.findViewById(R.id.listview2);
        patientpoints1 = new ArrayList<Point>();
        patientpoints2 = new ArrayList<Potient>();
        adaper2 = new AdapterPatientPointGet(this, patientpoints2);
        adaper1 = new AdapterPatientPointXH(this, patientpoints1);
        listview1.setAdapter(adaper2);
        listview2.setAdapter(adaper1);
        id = getIntent().getStringExtra("id");
        title = (TextView) findViewById(R.id.tv_title);
        getPointInfo(id);
        getPointInfo_page1(id);
        title.setText("我的积分");
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

    }

    public void getPointInfo(String id) {

        HashMap<String, String> interfaces = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(PointDetailActivity.this).getSesstion();
        interfaces.put("access_token", access_token);
        interfaces.put("patient", id);
        new HttpManager().get(this, Constants.GET_GIVEN_DRUG_POINTS,
                PointGivenEntity.class,
                interfaces,
                this, false, 1);
    }

    public void getPointInfo_page1(String id) {
        HashMap<String, String> interfaces = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(PointDetailActivity.this).getSesstion();
        interfaces.put("access_token", access_token);
        interfaces.put("patient", id);
        new HttpManager().get(this, Constants.GET_PATIENT_POINT_DETAIL,
                PointsDetail.class,
                interfaces,
                this, false, 1);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.rl_back) {
            finish();
        } else if (v.getId() == R.id.rl_plus) {
            Intent intent = new Intent(this, PointExplain.class);
            intent.putExtra("id", id);
            startActivity(intent);
            getPointInfo(id);
        }
    }

    @Override
    public void onFailure(Exception arg0, String arg1, int arg2) {
        // TODO Auto-generated method stub
        closeLoadingDialog();
    }

    @Override
    public void onSuccess(Result arg0) {
        closeLoadingDialog();
        // TODO Auto-generated method stub
        if (null != arg0) {
            if (arg0 instanceof PointGivenEntity) {
                if(arg0.getResultCode() == 1){
                    PointGivenEntity pointGivenEntity = (PointGivenEntity) arg0;
                    PatientPoints p = new PatientPoints();
                    List<Potient> potientList = new ArrayList<>();
                    if(null!=pointGivenEntity.data && null!=pointGivenEntity.data.pageData){
                        for(int i=0;i<pointGivenEntity.data.pageData.size();i++){
                            PointGivenEntity.Data.PageData pageData = pointGivenEntity.data.pageData.get(i);
                            Potient potient = new Potient();
                            potient.num_hqjf = ""+pageData.pointsNum;
                            potient.num_xfjf = pageData.pointsNum;
                            MedicineEntity medicineEntity = new MedicineEntity();
                            MedicineEntity.Goods goods = medicineEntity.new Goods();
                            goods.id = pageData.goodsId;
                            goods.title = pageData.title;
                            goods._type = pageData.type;
                            potient.goods = goods;
                            SimpleDateFormat a_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long time = pageData.createdDate;
                            Date date = new Date(time);
                            String strDate = a_format.format(date);
                            potient.created_time = strDate;

                            SomeBox.patientSuggest.Unit unit = new SomeBox.patientSuggest.Unit();
                            unit.id = pageData.packUnit;
                            unit.name = pageData.packUnitText;
                            unit.title = pageData.packUnitText;
                            potient.goods$unit = unit;
                            potient.num_dh = pageData.goodsNum;

                            potientList.add(potient);
                        }

                        p.info_list = potientList;
                        patientpoints2.clear();
                        patientpoints2 = p.info_list;
                        Collections.reverse(patientpoints2);
                        adaper2.notifyDataSetChanged();
                        adaper2 = new AdapterPatientPointGet(this, patientpoints2);
                        listview2.setAdapter(adaper2);
                    }

                }else {
                    ToastUtils.showResultToast(this, arg0);
                }
            } else if (arg0 instanceof PointsDetail) {
                patientpoints1.clear();
                PointsDetail pointsDetail = (PointsDetail) arg0;
                PointsGet p = new PointsGet();
                List<Point> pointList = new ArrayList<>();
                if(arg0.getResultCode() == 1){
                    if(null!=pointsDetail.data && null!=pointsDetail.data.pageData){
                        for(int i=0;i<pointsDetail.data.pageData.size();i++){
                            PointsDetail.Data.PageData pageData = pointsDetail.data.pageData.get(i);
                            Point point = new Point();
                            if(pageData.isReceive == 1){
                                point.is_receive = true;
                            }else{
                                point.is_receive = false;
                            }
                            Point.Goods$unit goods$unit = new Point.Goods$unit();
                            goods$unit.id = pageData.packUnit;
                            goods$unit.title = pageData.packUnitText;
                            Point.Goods goods = new Point.Goods();
                            goods.title = pageData.title;
                            point.goods$unit = goods$unit;
                            point.goods = goods;
                            SimpleDateFormat a_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long time = pageData.createdDate;
                            Date date = new Date(time);
                            String strDate = a_format.format(date);
                            point.created_time = strDate;
                            point.id = pageData.id;
                            point.number = pageData.pointsNum;
                            point.num_hqjf = pageData.pointsNum;
                            point.num_dh = pageData.goodsNum;

                            pointList.add(point);
                        }
                    }
                }else {
                    ToastUtils.showResultToast(this, arg0);
                }
                p.info_list = pointList;
                if (null != p.info_list && p.info_list.size() > 0) {
                    patientpoints1 = p.info_list;
                    Collections.reverse(patientpoints1);
                    adaper1.notifyDataSetChanged();
                    adaper1 = new AdapterPatientPointXH(this, patientpoints1);
                    listview1.setAdapter(adaper1);
                    listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Point p = (Point) adaper1.getItem(position);

                            if (!p.is_receive) {
                                getPointForUser(p.id);
                            }

                        }
                    });
                }

            } else  {
                if (arg0.getResultCode()==1) {
                    getPointInfo_page1(id);
                }else{
                    String msg = TextUtils.isEmpty(arg0.getResultMsg())?"数据异常":arg0.getResultMsg();
                    ToastUtils.showToast(PointDetailActivity.this,msg);
                }
            }
        }
    }

    public void getPointForUser(String id) {
        showLoadingDialog();
        HashMap<String, String> interfaces = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(PointDetailActivity.this).getSesstion();
        interfaces.put("access_token", access_token);
        interfaces.put("id", id);
        new HttpManager().get(this,Constants.GET_POINTS,
                Result.class,
                interfaces,
                this, false, 1);
    }

    @Override
    public void onSuccess(ArrayList arg0) {
        // TODO Auto-generated method stub

    }
}
