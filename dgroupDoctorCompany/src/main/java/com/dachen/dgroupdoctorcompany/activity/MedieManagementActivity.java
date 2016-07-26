package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.MedieManagementAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;
import com.dachen.dgroupdoctorcompany.entity.MedieManagements;
import com.dachen.dgroupdoctorcompany.entity.UpLoadFriend;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/2/22.
 */
public class MedieManagementActivity extends BaseActivity implements HttpManager.OnHttpListener{
    public static final int REQUEST_GET_MEDIE_DOC = 111;
    public static final int MODE_ADD_FRIEND = 1;
    public static final int MODE_USER_INFO = 2;
    private int mMode;
    private String strName="";

    ListView listView;
    MedieManagementAdapter adapter;
    List<MedieEntity.Medie> list;
    TextView tvSelected;
    public  HashMap<String,HashMap<String,UpLoadFriend>> sMapSelectedFile = new HashMap<>();
    public static MedieManagementActivity instance;
    public String json="";
    public int nSelected = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediemanagement);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty_container));
        tvSelected = (TextView) findViewById(R.id.tvSelected);
    }

    private void initData(){
        instance = this;
        mMode = this.getIntent().getIntExtra("mode",MODE_USER_INFO);
        if(MODE_USER_INFO == mMode){
            setTitle("我的云盘");
            tvSelected.setVisibility(View.GONE);
        }else if(MODE_ADD_FRIEND == mMode){
            setTitle("选择云盘资料");
            tvSelected.setVisibility(View.GONE);
        }

        list = new ArrayList<MedieEntity.Medie>();
        adapter = new MedieManagementAdapter(this,R.layout.adapter_mediemanagement,list);
        listView.setAdapter(adapter);
        getMedieInfo();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBack();
            }
        });
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    public void getMedieInfo(){
        String userId = SharedPreferenceUtil.getString(MedieManagementActivity.this,"id","");
        new HttpManager().get(this, Constants.MEDIE_MANAGEMENT , MedieEntity.class,
                Params.getMedieManagementParams(MedieManagementActivity.this, userId), this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        //ToastUtil.showToast(MedieManagementActivity.this,response.toString());
        if (null!=response){
            if (response instanceof MedieEntity){
                if (response.resultCode==1){
                    MedieEntity entity = (MedieEntity)response;
                    if (entity.data!=null&&entity.data.size()>0){
                        list.clear();
                        list.addAll(entity.data);

                        adapter = new MedieManagementAdapter(this,R.layout.adapter_mediemanagement,list);
                        listView.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                MedieEntity.Medie m =  (MedieEntity.Medie)adapter.getItem(position);
                                //showMedieInfo(m);
                                strName = m.drugName;
                                Intent intent = new Intent(MedieManagementActivity.this,MedieDocumentActicity.class);
                                intent.putExtra("drugId",m.id);
                                intent.putExtra("mode",mMode);
                                intent.putExtra("name",strName);
                                startActivityForResult(intent,REQUEST_GET_MEDIE_DOC);
                            }
                        });
                    }
                }else {
                    ToastUtil.showToast(this,response.resultMsg);
                }

            }
        }
    }
    public void showMedieInfo(MedieManagements.MedieManagement d){
        String name = "";
        if (null!=d.goods){
            name = d.goods.title;
        }

       // name =  CompareDatalogic.getShowName(d.goods$general_name, d.goods$general_name, name);
        ToastUtils.showToast(this,"药品资料_"+name);
    }
    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_GET_MEDIE_DOC && resultCode == Activity.RESULT_OK && null != data){
            nSelected = 0;
            if(null != sMapSelectedFile){
                for(String key:sMapSelectedFile.keySet()){
                    HashMap<String,UpLoadFriend> mapItem = MedieManagementActivity.instance.sMapSelectedFile.get(key);
                    UpLoadFriend uploadFriend = mapItem.get(key);
                    for(MedieEntity.Medie entity:list){
                        String drugName = entity.drugName;
                        String goodsName = uploadFriend.goodsName;
                        if(drugName.equals(goodsName)){
                            if(uploadFriend.fileList.size()>0){
                                entity.selectedNum = uploadFriend.fileList.size();
                                nSelected += uploadFriend.fileList.size();
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            }
            json = "";
            json = data.getStringExtra("doclist");
           /* List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            HashMap<String,String>map = new HashMap<String,String>();
            map.put(strName,result);
            list.add(map);

            Gson gson = new Gson();
            String json = gson.toJson(list);*/
        }
    }

    private void onBack(){
        if(MODE_ADD_FRIEND == mMode){
            Intent intent = new Intent();
            intent.putExtra("doclist",json);
            intent.putExtra("selectedNum",nSelected);
            setResult(Activity.RESULT_OK,intent);
        }
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0){
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
