package com.dachen.dgroupdoctorcompany.archive;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dachen.common.json.ResultTemplate;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.archive.entity.ArchiveListResult;
import com.dachen.dgroupdoctorcompany.archive.http.ArchiveListRequest;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.imsdk.utils.ImUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp  2016/1/13.
 */
public class ArchiveSearchUi extends BaseActivity implements View.OnClickListener{
    private static final String TAG=ArchiveSearchUi.class.getName();
    private static final int REQ_CODE_ARCHIVE_DETAIL=101;
    private static final String SP_HISTORY="archive_search_history";

    private EditText etSearch;
    private TextView tvResultInfo;
    private TextView tvEmptyInfo;
    private View llResultInfo;
    private PullToRefreshListView mListView;
    private List<ArchiveItem> itemList;
    private ArchiveListAdapter mAdapter;
    private long reqTimeStamp;
    private int curPageIndex;
    private String keyWord;
    private ArrayList<String> historyList;
    private ArchiveSearchHistoryAdapter historyAdapter;
    private ListView lvHistory;
    private InputMethodManager mInputManager;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_search_ui);
        mFrom = getIntent().getStringExtra("from");
        initMyView();
    }

    private void initMyView(){
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        itemList=new ArrayList<>();
        llResultInfo=findViewById(R.id.ll_result_info);
        tvResultInfo= (TextView) findViewById(R.id.tv_result_info);
        tvEmptyInfo= (TextView) findViewById(R.id.tv_info);
        etSearch= (EditText) findViewById(R.id.et_search);
        mListView= (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPageIndex = 0;
                itemList.clear();
                mListView.setMode(PullToRefreshBase.Mode.BOTH);
                mAdapter.notifyDataSetChanged();
                startSearch(keyWord);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startSearch(keyWord);
            }
        });

        mAdapter=new SearchAdapter(this,itemList);
        mListView.setAdapter(mAdapter);
        //mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView.setEmptyView(findViewById(R.id.empty_container));
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    String text = etSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(text)) {
                        curPageIndex = 0;
                        itemList.clear();
                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
                        mAdapter.notifyDataSetChanged();
                        startSearch(text);
                    }
                    return true;
                }
                return false;
            }
        });
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                RequestQueue queue = VolleyUtil.getQueue(ArchiveSearchUi.this);
                queue.cancelAll(TAG);
            }
        });

        historyList=initHistory();
        historyAdapter=new ArchiveSearchHistoryAdapter();
        lvHistory= (ListView) findViewById(R.id.lv_history);
        lvHistory.setAdapter(historyAdapter);
        findViewById(R.id.empty_container).setVisibility(View.GONE);
    }

    private class SearchAdapter extends ArchiveListAdapter{
        public SearchAdapter(Context context, List<ArchiveItem> mList) {
            super(context, mList);
        }
        @Override
        protected void onClickItem(ArchiveItem item) {
//            Intent intent = new Intent(ArchiveSearchUi.this, ArchiveItemDetailUi.class);
//            intent.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, item);
//            startActivityForResult(intent, REQ_CODE_ARCHIVE_DETAIL);
            item.items = itemList;
            ArchiveUtils.goArchiveDetailActivity(ArchiveSearchUi.this,REQ_CODE_ARCHIVE_DETAIL, item, mFrom);
        }
    }

    private void startSearch(String key){
        if (mInputManager.isActive()) {
            mInputManager.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), 0);
        }
        keyWord = key;
        historyList.remove(keyWord);
        historyList.add(0, keyWord);
        saveHistory();
        reqTimeStamp=System.currentTimeMillis();
        SucListener sucListener=new SucListener(reqTimeStamp);
        //ArchiveSearchRequest req=new ArchiveSearchRequest( key,sucListener,new errListener(reqTimeStamp));
        ArchiveListRequest req = new ArchiveListRequest(curPageIndex, key, ArchiveUtils.CATE_ALL, sucListener, new errListener(reqTimeStamp));
        req.setTag(TAG);
        RequestQueue queue= VolleyUtil.getQueue(this);
        queue.cancelAll(TAG);
        queue.add(req);
        mDialog.show();
    }

    private class SucListener implements Response.Listener<String> {
        long ts;
        public SucListener(long ts) {
            this.ts=ts;
        }
        @Override
        public void onResponse(String s) {
            if(ts!=reqTimeStamp)
                return;
            mListView.onRefreshComplete();
            mDialog.dismiss();
            ResultTemplate<ArchiveListResult> res = JSON.parseObject(s, new TypeReference<ResultTemplate<ArchiveListResult>>() {
            });
            if (res.resultCode != 1 || res.data == null){
                return;
            }

            lvHistory.setVisibility(View.GONE);

            ArchiveListResult data = res.data;
            if (data.pageIndex != curPageIndex) {
                return;
            }
            curPageIndex++;
            if (curPageIndex >= data.pageCount) {
                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            itemList.addAll(data.pageData);


            if(itemList.size()==0){
                tvEmptyInfo.setText(String.format("没有\"%s\"的搜索结果",keyWord));
                llResultInfo.setVisibility(View.GONE);
            }else{
                llResultInfo.setVisibility(View.VISIBLE);
                tvResultInfo.setText(String.format("搜索结果(%d)", itemList.size()));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private class errListener implements Response.ErrorListener{
        long ts;
        public errListener(long ts) {
            this.ts=ts;
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if(ts!=reqTimeStamp){
                return;
            }
            ToastUtil.showErrorNet(mThis);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE_ARCHIVE_DETAIL){
            if(resultCode!= Activity.RESULT_OK)
                return;
            ArchiveItem item= (ArchiveItem) data.getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
            Intent i=new Intent();
            i.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM,item);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }

    private class ArchiveSearchHistoryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return historyList.size()+1;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView= LayoutInflater.from(mThis).inflate(R.layout.item_search_history, null);
                holder=new ViewHolder();
                holder.history_tv = (TextView) convertView.findViewById(R.id.history_tv);
                holder.delete_tv = (TextView) convertView.findViewById(R.id.delete_tv);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            if(position==0){
                holder.history_tv.setText("历史记录");
                holder.history_tv.setTextColor(0xffaaaaaa);
                holder.delete_tv.setVisibility(View.VISIBLE);
                holder.delete_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearHistory();
                    }
                });
            }else{
                final String str=historyList.get(position-1);
                holder.history_tv.setText(str);
                holder.history_tv.setTextColor(0xff333333);
                holder.delete_tv.setVisibility(View.GONE);
                holder.history_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keyWord = str;
                        startSearch(str);
                    }
                });
            }
            return convertView;
        }

        class ViewHolder{
            TextView history_tv;
            TextView delete_tv;
        }
    }
    private void clearHistory(){
        historyList.clear();
        saveHistory();
        historyAdapter.notifyDataSetChanged();
    }
    private ArrayList<String> initHistory(){
        SharedPreferences sp= getSharedPreferences(SP_HISTORY,MODE_PRIVATE);
        String userId= ImUtils.getLoginUserId();
        String historyStr=sp.getString(userId, "");
        List<String> list= null;
        try {
            list = JSON.parseArray(historyStr, String.class);
            return new ArrayList<>(list);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    private void saveHistory(){
        SharedPreferences sp= getSharedPreferences(SP_HISTORY,MODE_PRIVATE);
        String str= JSON.toJSONString(historyList);
        String userId= ImUtils.getLoginUserId();
        sp.edit().putString(userId,str).apply();
    }
}
