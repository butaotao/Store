package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.MedieDocmentAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.archive.ArchiveUtils;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.MedieDocument;
import com.dachen.dgroupdoctorcompany.entity.UpLoadFriend;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by weiwei on 2016/2/27.
 */
public class MedieDocumentActicity extends BaseActivity implements HttpManager.OnHttpListener{
    public static final int REQ_CODE_ARCHIVE_DETAIL = 101;

    private TextView                      mTvSelected;
    private PullToRefreshListView         mLvDocument;
    private List<MedieDocument.Data.MedieDocumentItem>    mMedieDocumentList;
    private MedieDocmentAdapter           mAdapter;
    public static String json = "";
    private int curPageIndex = 0;
    private int pageSize = 10;
    private String drugId = "";
    public String mFrom = "MedieDocumentActicity";
    private int mMode;
    private String name;
    private int mSelected = 0;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medie_document);
        name = getIntent().getStringExtra("name");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        mTvSelected = (TextView) findViewById(R.id.tvSelected);
        mLvDocument = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mLvDocument.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPageIndex = 0;
                mMedieDocumentList.clear();
                mLvDocument.setMode(PullToRefreshBase.Mode.BOTH);
                mAdapter.notifyDataSetChanged();
                fetchData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                fetchData();

            }
        });
    }

    private void initData() {
        mMode = this.getIntent().getIntExtra("mode",MedieManagementActivity.MODE_USER_INFO);
        if(MedieManagementActivity.MODE_USER_INFO == mMode){
            setTitle("品种资料");
            mTvSelected.setVisibility(View.GONE);
        }else if(MedieManagementActivity.MODE_ADD_FRIEND == mMode){
            setTitle("选择云盘资料");
            mTvSelected.setVisibility(View.VISIBLE);
        }
        mMedieDocumentList = new ArrayList<MedieDocument.Data.MedieDocumentItem>();
        mAdapter = new MainAdapter(MedieDocumentActicity.this,mMedieDocumentList,mMode);
        mLvDocument.setAdapter(mAdapter);
        mLvDocument.setMode(PullToRefreshBase.Mode.BOTH);
        mLvDocument.setEmptyView(findViewById(R.id.empty_container));
        mLvDocument.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mTvSelected.setOnClickListener(this);

        drugId = this.getIntent().getStringExtra("drugId");
        fetchData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvSelected:
                onBack();
                break;
        }
    }

    private void fetchData(){
        //获取医药云盘资料
        new HttpManager().get(this, Constants.MEDIE_DOCUMENT, MedieDocument.class,
                Params.getMedieDocParams(MedieDocumentActicity.this,drugId,curPageIndex,pageSize),this,false,1);
    }

    private void getIsSelectedFile(){
        for(int i=0;i<mMedieDocumentList.size();i++){
            MedieDocument.Data.MedieDocumentItem item = mMedieDocumentList.get(i);
            HashMap<String,UpLoadFriend> mapFile = MedieManagementActivity.instance.sMapSelectedFile.get(name);
            if(null == mapFile){
                return;
            }
            UpLoadFriend friend = mapFile.get(name);
            if(null == friend){
                return;
            }
            for(int j=0;j<friend.fileList.size();j++){
                UpLoadFriend.UploadFile file = friend.fileList.get(j);
                String fileID = item.fileId;
                String id = file.id;
                if(fileID.equals(id)){
                    item.isSelected = true;
                }
            }
        }
    }

    @Override
    public void onSuccess(Result response) {
        if(null != response){
            if(response instanceof MedieDocument){
                mLvDocument.onRefreshComplete();
                MedieDocument medieDocument = (MedieDocument)response;
                if(null != medieDocument.data.pageData){
                    curPageIndex++;
                    mMedieDocumentList.addAll(medieDocument.data.pageData);
                    if(MedieManagementActivity.MODE_ADD_FRIEND == mMode){
                        getIsSelectedFile();
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }
        }

    }

    @Override
    public void onSuccess(ArrayList response) {
        /*if(null != response){
            mMedieDocumentList.clear();
            mMedieDocumentList.addAll(response);
            mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    private class MainAdapter extends MedieDocmentAdapter {
        public MainAdapter(Context context, List<MedieDocument.Data.MedieDocumentItem> mList,int mode) {
            super(context, mList,mode);
        }

        @Override
        protected void onClickItem(MedieDocument.Data.MedieDocumentItem item) {
            String fileId = item.fileId;
            String url = item.url;
            String name = item.name;
            String suffix = item.suffix;
            String size = item.size;
            ArchiveItem archiveItem = new ArchiveItem(fileId,url,name,suffix,size);
            List<ArchiveItem> itemList = new ArrayList<ArchiveItem>();
            for(int i=0;i<mMedieDocumentList.size();i++){
                MedieDocument.Data.MedieDocumentItem medieDocumentItem = mMedieDocumentList.get(i);
                ArchiveItem archiveItemList = new ArchiveItem(medieDocumentItem.fileId,medieDocumentItem.url,medieDocumentItem.name,medieDocumentItem.suffix,
                        medieDocumentItem.size);
                itemList.add(archiveItemList);
            }
            archiveItem.items = itemList;
            ArchiveUtils.goArchiveDetailActivity(MedieDocumentActicity.this, REQ_CODE_ARCHIVE_DETAIL, archiveItem, mFrom);
        }

        @Override
        protected void onClickSelected(boolean isSelected) {

        }
    }
    /*public String getFilesInfo(){
        List<UpLoadFriend> fileInfos = new ArrayList<>();
        for (int i=0;i<3;i++){
            UpLoadFriend friend = new UpLoadFriend();
            for (int j=0;j<2;j++){
                UpLoadFriend.UploadFile filedes=  friend.new UploadFile();
                filedes.id = "id"+j+"";
                filedes.name= "name"+j;
                filedes.url = url;
                friend.fileList.add(filedes);
            }
            friend.goodsName = "goodsName=="+i;
            fileInfos.add(friend);
        }
        Gson gson = new Gson();
       String json = gson.toJson(fileInfos);
        return json;
    }*/
    private void onBack(){
        if(MedieManagementActivity.MODE_ADD_FRIEND == mMode) {
            List<UpLoadFriend> fileInfos = new ArrayList<>();

            UpLoadFriend friend = new UpLoadFriend();
            for (int i = 0; i < mMedieDocumentList.size(); i++) {
                MedieDocument.Data.MedieDocumentItem item = mMedieDocumentList.get(i);
                if (item.isSelected) {
                    UpLoadFriend.UploadFile filedes = friend.new UploadFile();
                    filedes.id = item.fileId;
                    filedes.name = item.name;
                    filedes.url = item.url;
                    friend.fileList.add(filedes);
                }
            }
            friend.goodsName = name;
            HashMap<String,UpLoadFriend> mapFiles = new HashMap<>();
            mapFiles.put(name,friend);
            MedieManagementActivity.instance.sMapSelectedFile.put(name,mapFiles);

            for(String key:MedieManagementActivity.instance.sMapSelectedFile.keySet()){
                HashMap<String,UpLoadFriend> mapItem = MedieManagementActivity.instance.sMapSelectedFile.get(key);
                UpLoadFriend uploadFriend = mapItem.get(key);
                fileInfos.add(uploadFriend);
            }

            Gson gson = new Gson();
            json = gson.toJson(fileInfos);
            Intent intent = new Intent();
            intent.putExtra("doclist",json);
            setResult(Activity.RESULT_OK,intent);
        }
        finish();
    }

}
