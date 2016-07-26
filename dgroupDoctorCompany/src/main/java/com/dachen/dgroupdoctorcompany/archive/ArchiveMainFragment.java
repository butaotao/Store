package com.dachen.dgroupdoctorcompany.archive;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dachen.common.json.ResultTemplate;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.archive.entity.ArchiveListResult;
import com.dachen.dgroupdoctorcompany.archive.http.ArchiveListRequest;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mcp on 2016/1/12.
 */
public class ArchiveMainFragment extends Fragment {

    public static final int REQ_CODE_ARCHIVE_DETAIL = 101;
    private String TAG="ArchiveMainFragment";

    public static final int TYPE_UPLOADED = 1;
    public static final int TYPE_RECEIVED = 2;

    private View mView;
    private PullToRefreshListView mListView;
    private int type;
    private List<ArchiveItem> itemList;
    private ArchiveListAdapter mAdapter;
    private String curCategory = ArchiveUtils.CATE_ALL;
    private int curPageIndex;
    private String mode;

    private long reqTimeStamp;
    public boolean hasInit;
    public String mFrom;

    public static ArchiveMainFragment getInstance(int type, String from) {
        ArchiveMainFragment frag = new ArchiveMainFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putString("from", from);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        TAG = "ArchiveMainFragment" + type;
        mode = type == TYPE_UPLOADED ? ArchiveUtils.MODE_UPLOAD : ArchiveUtils.MODE_RECEIVE;
        mFrom = getArguments().getString("from");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.archive_main_fragment, container, false);
        mListView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPageIndex = 0;
                itemList.clear();
                mListView.setMode(PullToRefreshBase.Mode.BOTH);
                mAdapter.notifyDataSetChanged();
                fetchData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                fetchData();
            }
        });
        itemList = new ArrayList<>();
        mAdapter = new MainAdapter(getActivity(), itemList);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mView.findViewById(R.id.empty_container));
//        mListView.post(new Runnable() {
//            @Override
//            public void run() {
//                mListView.setCurrentMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                mListView.setRefreshing(true);
//            }
//        });
//        if(type==1){
//            checkInit();
//        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                checkInit();
            }
        }, 500);
        return mView;
    }

    private class MainAdapter extends ArchiveListAdapter {
        public MainAdapter(Context context, List<ArchiveItem> mList) {
            super(context, mList);
        }

        @Override
        protected void onClickItem(ArchiveItem item) {
            item.items = itemList;
            ArchiveUtils.goArchiveDetailActivity(getActivity(), REQ_CODE_ARCHIVE_DETAIL, item, mFrom);
        }
    }

    public void checkInit() {
        if (hasInit)
            return;
        mListView.post(new Runnable() {
            @Override
            public void run() {
                if (mListView.isRefreshing())
                    return;
                mListView.setCurrentMode(PullToRefreshBase.Mode.PULL_FROM_START);
                mListView.setRefreshing(true);
            }
        });
        hasInit = true;
    }

    public void setCategory(String category) {
        if (curCategory.equals(category))
            return;
        curCategory = category;
        itemList.clear();
        mAdapter.notifyDataSetChanged();
        if (!hasInit)
            return;
        mListView.setCurrentMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setRefreshing(true);
//        fetchData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void fetchData() {
        reqTimeStamp = System.currentTimeMillis();
        SucListener sucListener = new SucListener(reqTimeStamp);
        ArchiveListRequest req = new ArchiveListRequest(curPageIndex, null, curCategory, sucListener, new errListener(reqTimeStamp));
        req.setTag(TAG);
        RequestQueue queue = VolleyUtil.getQueue(getActivity());
        queue.cancelAll(TAG);
        queue.add(req);
    }

    private class SucListener implements Response.Listener<String> {
        long ts;

        public SucListener(long ts) {
            this.ts = ts;
        }

        @Override
        public void onResponse(String s) {
            Logger.d(TAG, "response=" + s);
            if (ts != reqTimeStamp)
                return;
            mListView.onRefreshComplete();
            ResultTemplate<ArchiveListResult> res = JSON.parseObject(s, new TypeReference<ResultTemplate<ArchiveListResult>>() {
            });
            if (res.resultCode != 1 || res.data == null)
                return;
            ArchiveListResult data = res.data;
            if (data.pageIndex != curPageIndex) {
                return;
            }
            curPageIndex++;
            if (curPageIndex >= data.pageCount) {
                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            itemList.addAll(data.pageData);

            mAdapter.notifyDataSetChanged();
        }
    }

    private class errListener implements Response.ErrorListener {
        long ts;

        public errListener(long ts) {
            this.ts = ts;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (ts != reqTimeStamp) {
                return;
            }
            mListView.onRefreshComplete();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQ_CODE_ARCHIVE_DETAIL) {
//            if (resultCode != Activity.RESULT_OK)
//                return;
//            ArchiveItem item = (ArchiveItem) data.getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
//            Intent i = new Intent();
//            i.putExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM, item);
//            getActivity().setResult(Activity.RESULT_OK, i);
//            getActivity().finish();
//        }
//    }

}
