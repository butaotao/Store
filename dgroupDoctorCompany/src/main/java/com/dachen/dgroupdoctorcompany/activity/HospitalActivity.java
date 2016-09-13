package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.HospitalAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.Hospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.utils.CustomDialog;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/2/22.
 */
public class HospitalActivity extends BaseActivity implements HttpManager.OnHttpListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    ListView listview;
    HospitalAdapter adapter;
    CustomDialog dialog;
    public static List<BaseSearch> hospitals;
    public static List<Doctor> docts;
    ViewStub vstub_title;
    View view;
    RelativeLayout ll_sub;
    @Nullable
    @Bind(R.id.rl_plus)
    RelativeLayout rl_plus;
    PopupWindow popupWindow;
    DoctorDao dao;
    RelativeLayout rl_notcontent;
    RelativeLayout rl_notcontenttext;
    TextView et_search;
    RelativeLayout rl_et;
    String showsearch;
    TextView tv_notcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        ButterKnife.bind(this);
        dao = new DoctorDao(this);
        setTitle("医生好友");
        listview = (ListView) findViewById(R.id.listview);
        tv_notcontent = (TextView) findViewById(R.id.tv_notcontent);
        getHospitalInfo();
        rl_notcontent = (RelativeLayout) findViewById(R.id.rl_notcontent);
        rl_notcontenttext = (RelativeLayout) findViewById(R.id.rl_notcontenttext);
        listview.setOnItemLongClickListener(this);
        hospitals = new ArrayList<>();
        showsearch = getIntent().getStringExtra("showsearch");

        rl_et = (RelativeLayout) findViewById(R.id.rl_et);
        if (TextUtils.isEmpty(showsearch)) {
            rl_et.setVisibility(View.GONE);
            //docts = dao.queryAllByUserid();
            setTitle("医生好友");
            ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
            vstub_title = (ViewStub) findViewById(R.id.vstub_title);
            view = vstub_title.inflate(this, R.layout.layout_plus_text, ll_sub);
            TextView viewImage = (TextView) view.findViewById(R.id.tv_plus1);
            viewImage.setText("添加医院");
            //viewImage.setBackgroundResource(R.drawable.add);
            viewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HospitalActivity.this, SearchActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addHospital", (Serializable) hospitals);
                    intent.putExtra("addHospital", bundle);
                    startActivity(intent);
                }
            });
        } else {
            rl_et.setVisibility(View.VISIBLE);
            setTitle("添加医生");
        }

        adapter = new HospitalAdapter(this, R.layout.adapter_hospital, hospitals);
        listview.setAdapter(adapter);
        et_search = (TextView) this.findViewById(R.id.et_search);
        et_search.setOnClickListener(this);
        et_search.clearFocus();
        et_search.setInputType(InputType.TYPE_NULL);
        et_search.setOnClickListener(this);

        dialog = new CustomDialog(this);
    }

    void showNotContent() {
        rl_et.setVisibility(View.GONE);
        if (TextUtils.isEmpty(showsearch)) {
            rl_notcontent.setVisibility(View.VISIBLE);
        } else {
            rl_notcontenttext.setVisibility(View.VISIBLE);
        }
     //  String s = "您当前未能找到任何分管医院，请先“添加分管医院”，再添加医生";
       String s = "当前没有可添加的医生，原因可能是您分管的医院目前暂无医生，或者暂未设置分管的医院，请“检查分管医院”是否已经正确设置";
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length() - 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new URLSpan(s), s.length() - 15, s.length() - 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.NORMAL), s.length() - 8, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_notcontent.setText(ss);
        tv_notcontent.setOnClickListener(this);
    }

    void hideNotContent() {
        if (TextUtils.isEmpty(showsearch)) {
            rl_notcontent.setVisibility(View.GONE);
        } else {
            rl_et.setVisibility(View.VISIBLE);
            rl_notcontenttext.setVisibility(View.GONE);
        }
    }

    @Nullable
    @OnClick(R.id.iv_plus1)
    void ChoiceMedie() {
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("addHospital", (Serializable) hospitals);
        intent.putExtra("addHospital", bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNotContent();
        getHospitalInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_privince:
                Intent intent = new Intent(this, ProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_keyword:
                intent = new Intent(this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("addHospital", (Serializable) hospitals);
                intent.putExtra("addHospital", bundle);
                startActivity(intent);
                break;
            case R.id.et_search:
                //
                intent = new Intent(this, SearchDoctorActivity.class);
                intent.putExtra("searchText", "");
                intent.putExtra("hospitId", "");
                startActivity(intent);
                startActivity(intent);
                break;
            case R.id.tv_notcontent:
                intent = new Intent(this, HospitalManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void getHospitalInfo() {
        showLoadingDialog();
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        maps.put("pageIndex","1");
        maps.put("pageSize","1000");
        maps.put("includeNoneDoctor", "0");
        new HttpManager().post(this, Constants.GETNOTSIGNHOSPITAL, Hospital.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();

        dao = new DoctorDao(this);
        if (null != response) {
            if (response instanceof Hospital) {
                UserLoginc.addDoctor(this, false, false);
                Hospital hospital = (Hospital) response;
                if (null == hospital.data || null == hospital.data.pageData || hospital.data.pageData.size() == 0) {
                    showNotContent();
                    hospitals.clear();
                    adapter = new HospitalAdapter(this, R.layout.adapter_hospital, hospitals);
                    listview.setAdapter(adapter);
                    return;
                }
                hideNotContent();
                hospitals.clear();

                hospitals.addAll((Collection<? extends HospitalDes>) hospital.data.pageData);
                // hospitals.addAll( dao.queryAllByUserid();
                // adapter.notifyDataSetChanged();
                adapter = new HospitalAdapter(this, R.layout.adapter_hospital, hospitals);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (TextUtils.isEmpty(showsearch)) {
                            if (adapter.getItem(position) instanceof HospitalDes) {
                                HospitalDes des = (HospitalDes) adapter.getItem(position);
                                String name = "";
                                if (null != des.name) {
                                    name = des.name;
                                }
                                //   if (null!=dao.queryByDepId(des.id)&&dao.queryByDepId(des.id).size()>0){
                                Intent intent = new Intent(HospitalActivity.this, DoctorActivity.class);
                                intent.putExtra("id", des.id);
                                intent.putExtra("name", name);
                                startActivity(intent);
                                //  }
                            } else if (adapter.getItem(position) instanceof Doctor) {
                                Intent intent = new Intent(HospitalActivity.this, DoctorDetailActivity.class);
                                intent.putExtra("doctordetail", (Doctor) adapter.getItem(position));
                                startActivity(intent);
                            }
                        } else {
                            if (adapter.getItem(position) instanceof HospitalDes) {
                                HospitalDes des = (HospitalDes) adapter.getItem(position);
                                Intent intent = new Intent(HospitalActivity.this, SearchDoctorDeptResultActivity.class);
                                intent.putExtra("hospitId", des.id);
                                intent.putExtra("hospitName", des.name);
                                startActivity(intent);
                                //
                            }
                        }
                    }
                });
            } else if (response.resultCode == 1) {
                // ToastUtil.showToast(HospitalActivity.this,"删除成功");
                getHospitalInfo();
            }
        }
    }


    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    public void showpopup() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popupwindow_mybox, null);

        RelativeLayout rl_bottom = (RelativeLayout) layout.findViewById(R.id.rl_bottom);
        popupWindow = new PopupWindow(layout);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.getContentView().findViewById(R.id.ll_privince).setOnClickListener(this);
        popupWindow.getContentView().findViewById(R.id.ll_keyword).setOnClickListener(this);
        // 控制popupwindow的宽度和高度自适应
/*	rl_bottom.measure(View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED); */
        //   popupWindow.setWidth(rl_bottom.getMeasuredWidth()+30);
        // popupWindow.setHeight((rl_bottom.getMeasuredHeight() + 20));

        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
     /*       // 控制popupwindow点击屏幕其他地方消失
       popupWindow.setBackgroundDrawable(this.getResources().getDrawable(
	                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
*/
        popupWindow.setOutsideTouchable(true);//
        popupWindow.showAsDropDown(rl_plus, 0, 3);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getItem(position) instanceof HospitalDes) {
            final HospitalDes des = (HospitalDes) adapter.getItem(position);
            List<Doctor> doctors = dao.queryByDepId(des.id + "");
            String doctorsNames = "";

            if (null != doctors && doctors.size() > 0) {
                boolean isshowmore = false;
                for (int i = 0; i < doctors.size(); i++) {
                    if (i < 5) {
                        if (i != 0) {
                            doctorsNames += "," + doctors.get(i).name;
                        } else {
                            doctorsNames = doctors.get(i).name;
                        }

                    } else if (i == 5) {
                        doctorsNames += "...";
                        isshowmore = true;
                        break;
                    }
                }

                showDialogDeleteHospitalWithNullDoctor(des, true, doctorsNames, isshowmore);
            } else {
                showDialogDeleteHospitalWithNullDoctor(des, false, "", false);
            }
        }

        return true;
    }

    public void showDialogDeleteHospitalWithNullDoctor(final HospitalDes des, final boolean show, final String name, final boolean isshow) {
        dialog.showDialog("提示", "您确定要删除该医院？", R.string.sure, R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                if (show) {
                    showDialogDeleteHospitalWithDoctor(des, name, isshow);
                } else {
                    deleteHospital(des.id + "");
                }

                // ToastUtil.showToast(HospitalActivity.this,des.name);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        });
    }

    public void showDialogDeleteHospitalWithDoctor(final HospitalDes des, String name, boolean isshow) {
        String alert = "";
        if (isshow) {
            alert = "\n" +
                    "您医院下医生有" + name + "\n" +
                    "等医生好友，您确定要删除？";
        } else {
            alert = "\n" +
                    "您医院下医生有" + name + "\n" +
                    "医生好友，您确定要删除？";
        }
        dialog.showDialog("提示", alert, R.string.godelete, R.string.haveknow, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deleteHospital(des.id);
                dialog.dimissDialog();
                deleteHospital(des.id + "");
                //ToastUtil.showToast(HospitalActivity.this,des.name);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dimissDialog();
    }

    public void deleteHospital(final String id) {
        //8091
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("hospitalId", id);//
        new HttpManager().post(this, Constants.DELETEHOSTIPAL, Result.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        dao.deleteByDepID(id);
                        getHospitalInfo();
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }
}
