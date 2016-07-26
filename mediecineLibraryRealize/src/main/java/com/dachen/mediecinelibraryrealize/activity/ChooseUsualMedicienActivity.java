package com.dachen.mediecinelibraryrealize.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.CustomDialog;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.MedicienEntity;
import com.dachen.mediecinelibraryrealize.entity.MedicienInfo;
import com.dachen.mediecinelibraryrealize.entity.MedicienInfoBean;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChooseUsualMedicienActivity extends BaseActivity implements
        OnClickListener, OnKeyListener, OnHttpListener, AdapterView.OnItemLongClickListener {

    private TextView mTv_title;
    private RelativeLayout mRl_back;
    private ListView mLv_showusualmedicien;
    ViewHolder viewHolder;
    private FrameLayout mLayout_search;
    private int clickPosition = -1;
    private int removePosition = -1;
    private boolean isGetData = false;// 是否是从服务器获取数据
    MyAdapter myAdapter;
    EditText et_search;
    RelativeLayout rl_nocontent;
    /**
     * 总药品信息的集合
     */
    List<MedicienInfo> mList = new ArrayList<MedicienInfo>();
    boolean isDeleteDrug = false;//当前是否在删除常备药

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        // 从服务器获取数据
        getData();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               ((InputMethodManager) getSystemService(Context
                                       .INPUT_METHOD_SERVICE)).
                                       hideSoftInputFromWindow(ChooseUsualMedicienActivity.this
                                                       .getCurrentFocus().getWindowToken(),
                                               InputMethodManager.HIDE_NOT_ALWAYS);
                           }

                       },
                998);

    }

    private String list2JsonString(List<MedicienInfo> list) {
        StringBuilder sbBuilder = new StringBuilder();
        boolean flag = false;
        try {
            sbBuilder.append("json:[");

            for (int i = 0; i < list.size(); i++) {

                MedicienInfo medicienInfo = list.get(i);
                if (medicienInfo.isSelect) {
                    flag = true;
                    String trade_name = medicienInfo.trade_name;// json:[{"trade_name":"感冒灵",
                    // "goods":"goodsid"}]

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trade_name", trade_name);
                    jsonObject.put("id", medicienInfo.id);

                    if (!TextUtils.isEmpty(medicienInfo.goodsid)) {
                        jsonObject.put("goods", medicienInfo.goodsid);
                    }

                    if (i != list.size() - 1) {
                        sbBuilder.append(jsonObject.toString() + ",");
                    } else {
                        sbBuilder.append(jsonObject.toString() + "]");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (flag) {

            return sbBuilder.toString();

        }
        return "";
    }

    /**
     * @param id 从服务器删除数据
     */
    private void postdelete(String id) {
        HashMap<String, String> interfaces = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getSesstion();
        interfaces.put("access_token", access_token);
        interfaces.put("id", id);
        new HttpManager().post(this,Constants.DELETE_DRUG,
                MedicienInfoBean.class,
                interfaces, this, false, 1);
    }

    /**
     * 上传数据到服务器
     */
    private void postData(List<MedicienInfo> list) {
        if (null != list && list.size() > 0) {
            showLoadingDialog();
            String listId = getNewGoodsId();
            if (!TextUtils.isEmpty(listId)) {
                addData();
            } else {
                finish();
            }

        }
    }

    /**
     * 从服务器获取数据
     */
    private void getData() {
        showLoadingDialog();
        Map<String, String> params = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getSesstion();
        String userId = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getId();
        params.put("access_token", access_token);
        params.put("userId",userId);
        new HttpManager().post(this, Constants.GET_DRUG_LIST,
                MedicienEntity.class,
                params, this, false, 1);
        isGetData = true;
    }

    /**
     * 1、初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_chooseusualmedicien);
        // title_bar
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
        mRl_back.setOnClickListener(this);
        mTv_title.setText("常备药品");
        myAdapter = new MyAdapter(mList);
        // 自选常备药
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnClickListener(this);
        et_search.clearFocus();
        et_search.setInputType(InputType.TYPE_NULL);
        mLayout_search = (FrameLayout) findViewById(R.id.layout_search);
        mLayout_search.setOnClickListener(this);
        mLv_showusualmedicien = (ListView) findViewById(R.id.lv_showusualmedicien);
        rl_nocontent = (RelativeLayout) findViewById(R.id.rl_nocontent);
        // 设置适配器
        mLv_showusualmedicien.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        mLv_showusualmedicien.setOnItemLongClickListener(this);
    }

    @Override
    public void onFailure(Exception arg0, String arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * 解析服务器获取的数据
     */
    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response) {
            if (response instanceof MedicienEntity) {
                if(response.getResultCode() == 1){
                    MedicienEntity bean = (MedicienEntity) response;
                    if (null != bean.data && bean.data.size() > 0) {
                        List<MedicienInfo>infoList = dataFromate(bean.data);//将接收的数据转化为MedicienInfo
                        // 清空总列表数据集合
                        mList.clear();
                        mList.addAll(infoList);

                        if (null == mList || mList.size() < 1) {
                            mLv_showusualmedicien.setVisibility(View.GONE);
                            isGetData = false;
                            rl_nocontent.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            mLv_showusualmedicien.setVisibility(View.VISIBLE);
                            rl_nocontent.setVisibility(View.GONE);
                            // 刷新数据
                            isGetData = false;
                            MyAdapter myAdapter = new MyAdapter(mList);
                            mLv_showusualmedicien.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();
                        }
                    } else {
                        rl_nocontent.setVisibility(View.VISIBLE);
                    }
                }else{
                    String resultMsg = response.getResultMsg();
                    String msg = TextUtils.isEmpty(resultMsg)?"数据出错":resultMsg;
                    ToastUtils.showToast(ChooseUsualMedicienActivity.this,msg);
                }

            } /*
            else if (!TextUtils.isEmpty(response.resultMsg)) {
                if (response.resultMsg.contains("OK")) {
                    if (removePosition != -1) {
                        mList.remove(removePosition);
                        MyAdapter myAdapter = new MyAdapter(mList);
                        mLv_showusualmedicien.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                        removePosition = -1;
                    }
                } else if (response.resultMsg.contains(":true")) {
                    finish();
                } else if (response.resultMsg.contains("type")) {
                    finish();
                }else {
                    finish();
                }
            }*/
            else{
                if(isDeleteDrug){//当前是删除操作
                    isDeleteDrug = false;
                    if(response.getResultCode() == 1){
                        if (removePosition != -1) {
                            mList.remove(removePosition);
                            MyAdapter myAdapter = new MyAdapter(mList);
                            mLv_showusualmedicien.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();
                            removePosition = -1;
                        }
                    }else{
                        String resultMsg = response.getResultMsg();
                        String msg = TextUtils.isEmpty(resultMsg)?"数据出错":resultMsg;
                        ToastUtils.showToast(ChooseUsualMedicienActivity.this,msg);
                    }
                }else{
                    finish();
                }

            }
        }
        //ToastUtils.showToast(response.resultMsg);
        //finish();
    }

    @Override
    public void onSuccess(ArrayList arg0) {

    }

    private List<MedicienInfo> dataFromate(List<MedicienEntity.Data>src){
        List<MedicienInfo> medicienInfoList = new ArrayList<>();
        for(int i=0;i<src.size();i++){
            MedicienEntity.Data data = src.get(i);
            MedicienInfo medicienInfo = new MedicienInfo();
            medicienInfo.title = data.goodsGroupGeneralName;
            medicienInfo.trade_name = data.goodsGroupGeneralName;
            medicienInfo.id = data.id;
            medicienInfo.goodsid = data.goodsGroupId;
            medicienInfo.isSelect = false;
            medicienInfoList.add(medicienInfo);
        }

        return medicienInfoList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_back) {
            // 上传数据
            if (null != mList && mList.size() > 0) {
                postData(mList);
            } else {
                finish();
            }

            //
        } else if (v.getId() == R.id.et_search) {
            // 进入透明搜索页面
            Intent intent = new Intent(this,
                    TransParentSearchMedicienActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
	/*	if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(
					ChooseUsualMedicienActivity.this,
					PatientMedieBoxActivity2.class);
			String jsonString = list2JsonString(mList);
			if (!TextUtils.isEmpty(jsonString)&&jsonString.contains("json")){
				intent.putExtra("jsonString", jsonString);
				ChooseUsualMedicienActivity.this.setResult(300,
						intent);
			}
			// 传递数据

			finish();
		}*/
        return true;
    }

    private String getNewGoodsId(){
        String strIds = "";
        if(null!=mList && mList.size()>0){
            for(int i=0;i<mList.size();i++){
                MedicienInfo item = mList.get(i);
                if(item.isSelect){
                    String id = item.id;
                    if(!TextUtils.isEmpty(id)){
                        if(TextUtils.isEmpty(strIds)){
                            strIds = id;
                        }else{
                            strIds = strIds+","+id;
                        }
                    }
                }
            }

        }
        return strIds;
    }

    private void addData(){
        String listId = getNewGoodsId();
        if(TextUtils.isEmpty(listId)){
            return ;
        }
        HashMap<String, String> interfaces = new HashMap<String, String>();
        String access_token = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getSesstion();
        String userId = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getId();
        String userType = UserInfo.getInstance(ChooseUsualMedicienActivity.this).getUserType();
        interfaces.put("access_token", access_token);
        interfaces.put("userId",userId);
        interfaces.put("userType",userType);
//        Gson gson = new Gson();
//        String goodsGroupIds = gson.toJson(listId);
        interfaces.put("goodsGroupIds", listId);
        new HttpManager().post(this, Constants.ADD_DRUG_LIST, Result.class,
                interfaces, this, false, 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
        String jsonString = list2JsonString(mList);
        if (!TextUtils.isEmpty(jsonString) && jsonString.contains("json")) {
            if (!TextUtils.isEmpty(jsonString) && jsonString.contains("json")) {
                HashMap<String, String> interfaces = new HashMap<String, String>();
                interfaces.put("list_datas", jsonString);
                new HttpManager().post(this, Params.getInterface("invoke",
                        "c_user_goods_CB.create_CB_batch"), Result.class,
                        interfaces, this, false, 2, false);
            }
        }*/
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:// 透明activity返回的数据
            {
                if (data != null) {
                    String breedname = data.getExtras().getString("breedname");// 接受传递的商品名
                    String id = data.getExtras().getString("id");
                    MedicienInfo medicienInfo = new MedicienInfo();
                    //ToastUtils.showToast("透明activity返回的数据" + breedname);
                    medicienInfo.trade_name = breedname + "";
                    medicienInfo.goodsid = id;
                    medicienInfo.id = id;
                    // 数据有改变，加入changelist,并加入mlist用于展示数据
                    //addNewList.add(medicienInfo);
                    boolean haveAddFlag = false;
                    for (int i = 0; i < mList.size(); i++) {
                        if (!TextUtils.isEmpty(mList.get(i).trade_name) && mList.get(i)
                                .trade_name.equals(breedname)) {
                            haveAddFlag = true;
                            break;
                        }
                    }
                    if (!haveAddFlag) {
                        medicienInfo.isSelect = true;
                        // 刷新视图
                        mList.add(0,medicienInfo);
                    }

                    myAdapter.notifyData();
                    myAdapter = new MyAdapter(mList);
                    mLv_showusualmedicien.setAdapter(myAdapter);
                }
                break;
            }
            case 2:// 有制药公司名返回的数据
                if (data != null) {

                    Bundle bundle = data.getBundleExtra("medicienInfo");
                    MedicienInfo medicienInfo = (MedicienInfo) bundle.get("medicienInfo");

                    MedicienInfo infos = mList.get(clickPosition);
                    infos = medicienInfo;
                    infos.goodsid = medicienInfo.id;
                    infos.id = mList.get(clickPosition).id;
                    infos.goods$manufacturer = medicienInfo.manufacturer;
                    infos.isSelect = true;
                    mList.set(clickPosition, infos);
                    myAdapter = new MyAdapter(mList);
                    mLv_showusualmedicien.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                    myAdapter.notifyData();
                }
                break;

            default:
                break;
        }
        if (null != mList && mList.size() > 0) {
            rl_nocontent.setVisibility(View.GONE);
        } else {
            rl_nocontent.setVisibility(View.VISIBLE);
        }
    }

    private MedicienInfo tag;


    class MyAdapter extends BaseAdapter {
        List<MedicienInfo> mInfo_lists;

        public MyAdapter(List<MedicienInfo> mInfo_list) {
            this.mInfo_lists = mInfo_list;
        }

        @Override
        public int getCount() {
            if (mInfo_lists.size() == 0) {

                return 0;
            } else {
                return mInfo_lists.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return mInfo_lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView != null) {

                viewHolder = (ViewHolder) convertView.getTag();
            } else {

                viewHolder = new ViewHolder();

                convertView = View.inflate(ChooseUsualMedicienActivity.this,
                        R.layout.item, null);

                // 主item布局

                viewHolder.tv_content = (LinearLayout) convertView
                        .findViewById(R.id.ll_item_content);
				/*viewHolder.tv_delete = (TextView) convertView
						.findViewById(R.id.tv_delete);*/
                viewHolder.mIv_lv_usualmedicien = (ImageView) convertView
                        .findViewById(R.id.iv_lv_usualmedicien);
                viewHolder.mTv_lv_medicienname = (TextView) convertView
                        .findViewById(R.id.tv_lv_medicienname);
                viewHolder.mTv_lv_company = (TextView) convertView
                        .findViewById(R.id.tv_lv_company);
                viewHolder.mTv_lv_company.setVisibility(View.GONE);
                viewHolder.mRL_lv_right_arrow = (RelativeLayout) convertView
                        .findViewById(R.id.rl_lv_right_arrow);

                convertView.setTag(viewHolder);
            }

            // 获取数据
            final MedicienInfo medicienInfo = (MedicienInfo) getItem(position);

            final ViewHolder finalHolder = viewHolder;

            // 设置数据并绑定数据
            viewHolder.mTv_lv_medicienname.setText(medicienInfo.trade_name);
            String manufacturer = "";
            if (!TextUtils.isEmpty(medicienInfo.goods$manufacturer)) {
                manufacturer = medicienInfo.goods$manufacturer;
            } else if (!TextUtils.isEmpty(medicienInfo.manufacturer)) {
                manufacturer = medicienInfo.manufacturer;
            }
            if (!TextUtils.isEmpty(manufacturer)) {
                viewHolder.mTv_lv_company.setVisibility(View.VISIBLE);
                viewHolder.mTv_lv_company.setText(medicienInfo.goods$manufacturer);
            } else {
                viewHolder.mTv_lv_company.setVisibility(View.VISIBLE);
                viewHolder.mTv_lv_company.setText("未定厂商");
            }
            if (medicienInfo.isSelect) {
                viewHolder.mIv_lv_usualmedicien.setBackgroundResource(R.drawable.select);
            } else {
                viewHolder.mIv_lv_usualmedicien.setBackgroundResource(R.drawable.medicien_unchecked);
            }
            LogUtils.burtLog("notifyData()===" + position);
            // 条目的点击事件，进行品种选择
            viewHolder.mRL_lv_right_arrow
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            tag = medicienInfo;
                            // 进入药品品种选择界面

                            //ToastUtils.showToast("tag=" + tag + "location");

                            Intent intent = new Intent(
                                    ChooseUsualMedicienActivity.this,
                                    PatientMedicienSearchListActivity.class);
                            intent.putExtra("trade_name",
                                    medicienInfo.trade_name);
                            intent.putExtra("position", position);
                            clickPosition = position;
							/*ToastUtils.showToast("trade_name"
									+ medicienInfo.trade_name);*/
                            startActivityForResult(intent, 2);
                        }
                    });
		/*	viewHolder.mIv_lv_usualmedicien.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (medicienInfo.isSelect==true) {
						medicienInfo.isSelect = false; 
						viewHolder.mIv_lv_usualmedicien.setBackgroundResource(R.drawable.medicien_unchecked);
					}else {
						medicienInfo.isSelect = true;
						viewHolder.mIv_lv_usualmedicien.setBackgroundResource(R.drawable.select);
					}
					mList.set(position, medicienInfo);
					notifyDataSetChanged();
				}
			});*/
			/*viewHolder.tv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finalHolder.srv_lv_item.dismissDelete();
					// 数据有改变，减掉mlist用于展示数据
					removePosition = position;
					postdelete(medicienInfo.id);

				}
			});*/
            viewHolder.tv_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final CustomDialog dialog = new CustomDialog(ChooseUsualMedicienActivity.this);
                    dialog.showDialog(null, "是否删除该常备药?", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isDeleteDrug = true;
                            removePosition = position;
                            postdelete(medicienInfo.id);
                            dialog.dimissDialog();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                        }
                    });
                    // 数据有改变，减掉mlist用于展示数据

                    return false;
                }
            });
            return convertView;
        }

        public void notifyData() {
            notifyDataSetChanged();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }

    class ViewHolder {

        // 主条目
        private TextView tv_delete;// 删除
        private LinearLayout tv_content;// 导入的内容

        // 导入内容的主条目
        private ImageView mIv_lv_usualmedicien;
        private TextView mTv_lv_medicienname;
        private TextView mTv_lv_company;
        private RelativeLayout mRL_lv_right_arrow;
    }

}
