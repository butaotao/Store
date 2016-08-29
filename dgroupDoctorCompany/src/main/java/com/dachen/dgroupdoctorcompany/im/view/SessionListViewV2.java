package com.dachen.dgroupdoctorcompany.im.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.dachen.common.async.SimpleResultListenerV2;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.NewFriendActivity;
import com.dachen.dgroupdoctorcompany.adapter.BaseCustomAdapter;
import com.dachen.dgroupdoctorcompany.im.activity.FeedbackChatActivity;
import com.dachen.dgroupdoctorcompany.im.activity.PublicNotifyActivity;
import com.dachen.dgroupdoctorcompany.im.adapter.ChatGroupMenuAdapter;
import com.dachen.dgroupdoctorcompany.im.adapter.SessionListAdapterV2;
import com.dachen.dgroupdoctorcompany.im.utils.AppImUtils;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.imsdk.consts.SessionGroupId;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.imsdk.service.ImRequestManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot1.event.EventBus;


/**
 * 会话列表
 */
public class SessionListViewV2 extends ListView {

    private static final String TAG = SessionListViewV2.class.getSimpleName();
    public static final String ITEM_TOP="置顶";
    public static final String ITEM_NO_TOP="取消置顶";
    public static final String ITEM_DEL="删除";

    protected Context context;
    protected Activity ui = null;
    private ChatGroupDao mDao;
    private ArrayList<ChatGroupPo> mList = new ArrayList<>();
    private SessionListAdapterV2 mAdapter;
    boolean isAddHeaderView = false;
    private Dialog menuDialog;

    public SessionListViewV2(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SessionListViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SessionListViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        if (isInEditMode())
            return;
        mDao = new ChatGroupDao();

        // 单击
        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG, "onItemClick():position:" + position + ",id:" + id);
                if (view == null) {
                    return;
                }
                ChatGroupPo po = (ChatGroupPo) getItemAtPosition(position);
                __onItemClick(po);

            }

        });

        // 长按
        setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG, "onItemLongClick():position:" + position + ",id:" + id);
                if (view == null) {
                    return true;
                }
                position = getHeaderViewPosition(position);
                ChatGroupPo messageDB = mList.get(position);
                __onLongClick(messageDB);
                return true;
            }

        });

        // 加入通知
        EventBus.getDefault().register(this);

        // 更新视图
        initData();

    }

    /**
     * 设置UI
     *
     * @param ui
     */
    public void setUI(Activity ui) {
        this.ui = ui;
    }

    protected void setEmptyView(int size) {
        Log.w(TAG, "setEmptyView():size:" + size);
        View emptyView = this.getEmptyView();
        if (emptyView != null) {
            Log.w(TAG, "setEmptyView():emptyView != null");
            if (size > 0) {
                Log.w(TAG, "setEmptyView() == View.GONE");
                emptyView.setVisibility(View.GONE);
            } else {
                Log.w(TAG, "setEmptyView() == View.VISIBLE");
                emptyView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.w(TAG, "setEmptyView():emptyView == null");
        }
    }

    /**
     * 删除一项
     */
    protected void showDeleteDialog(final ChatGroupPo group) {
        if (group == null) {
            return;
        }
        Log.w(TAG, "showDeleteDialog():sessionMessage:" + group.toString());

        new AlertDialog.Builder(ui).setMessage("删除?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // 从数据库里删除这一条
//				SessionMessageDBDao.deleteById(group._id);
                mDao.deleteById(group.groupId);
                // 发出通知，刷新视图
//				ObserverManager.getInstance().notifyNewMsg(null, true);
                mList.remove(group);
                mAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new NewMsgEvent(SessionListViewV2.this));
            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }

        }).show();
    }

    private void showMenuDialog(ChatGroupPo po) {
        Dialog dialog=new Dialog(context,R.style.MsgMenuDialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View v= LayoutInflater.from(context).inflate(R.layout.msg_menu,null);
        List<String> items=new ArrayList<>();
        if(po.top==0){
            items.add(ITEM_TOP);
        }else{
            items.add(ITEM_NO_TOP);
        }
        items.add(ITEM_DEL);
        ListView lv = (ListView) v.findViewById(R.id.list_view);
        lv.setAdapter(new ChatGroupMenuAdapter(context,items));
        lv.setOnItemClickListener(new MenuClickListener(po));
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(true);
        menuDialog=dialog;
        dialog.show();
    }

    private class MenuClickListener implements OnItemClickListener{
        ChatGroupPo po;

        public MenuClickListener(ChatGroupPo po) {
            this.po = po;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String action = (String) parent.getItemAtPosition(position);
            if (action == null) return;
            if (action.equals(ITEM_DEL)) {
                showDeleteDialog(po);
            } else if (action.equals(ITEM_TOP)) {
                topGroup(po,1);
            } else if (action.equals(ITEM_NO_TOP)) {
                topGroup(po,0);
            }
            menuDialog.dismiss();
        }
    };
    private void topGroup(final ChatGroupPo po, final int act){
        if(po==null)return;
        ImRequestManager.topChatGroup(po.groupId, act, new SimpleResultListenerV2() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.showToast(context,"请求成功");
                mDao.setTopFlag(po.groupId,act);
                updateView();
            }

            @Override
            public void onError(String msg) {
                ToastUtil.showToast(context,"请求失败 "+msg);
            }
        });
    }

    /**
     * 长按
     *
     * @param item
     */
    protected void __onLongClick(ChatGroupPo item) {
//        showDeleteDialog(item);
        showMenuDialog(item);
    }

    /**
     * 单击事件
     *
     * @param item
     */
    protected void __onItemClick(ChatGroupPo item) {
        if (item == null) {
            return;
        }

        Log.w(TAG, "__onItemClick():item:" + item.toString());
        mDao.setUnreadZero(item.groupId);
        EventBus.getDefault().post(new NewMsgEvent(this));
        item.unreadCount = 0;
        BaseAdapter ada;
        if (isAddHeaderView || getHeaderViewsCount() > 0) {
            HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) getAdapter();
            ada = (BaseAdapter) listAdapter.getWrappedAdapter();
            isAddHeaderView = true;
        } else {
            ada = (BaseAdapter) getAdapter();
        }
        ada.notifyDataSetChanged();

        Log.w(TAG, "item.toString:" + item.toString());

        if (item.type == ChatGroupPo.TYPE_DOUBLE || item.type == ChatGroupPo.TYPE_MULTI
                || item.type == ChatGroupPo.TYPE_GUIDE) {
            // 进入聊天界面
            ChatActivityUtilsV2.openUI(context, item);
        } else if (item.groupId.equals(SessionGroupId.auth_request_doctor) || item.groupId.equals(SessionGroupId
                .auth_request_patient)) {
            // 进入好友验证请求界面
//			SystemNotificationUI.openUI(context, item.groupId);
            Intent i = new Intent(context, NewFriendActivity.class);
            i.putExtra(NewFriendActivity.NEWFRIENDGROUPID, item.groupId);
            context.startActivity(i);
        } else if (item.bizType.equals("pub_customer")) {
            FeedbackChatActivity.openUI(context, item.name, item.groupId, null);
        } else if (item.bizType.equals("pub_org")) {
            PublicNotifyActivity.openUI(context, item.name, item.groupId);
        }

    }

    /**
     * 得到数据
     */
    protected List<ChatGroupPo> getData() {
        return mDao.queryInBizType(AppImUtils.getBizTypes());
    }

    /**
     * 得到适配器
     */
    protected BaseAdapter __getAdapter(List<ChatGroupPo> list) {
        Logger.d("yehj", "SessionListView------->>>>> __getAdapter");
        return new SessionListAdapterV2(context, R.layout.im_session_message_listview, list);
    }

    /**
     * 设置适配器
     */
    protected void __setAdapter(List<ChatGroupPo> list) {
        if (list == null) {
            Log.w(TAG, "__setAdapter() list == null");
        } else {
            Log.w(TAG, "__setAdapter() list.size():" + list.size());
        }

        // 适合器
        if (this.getAdapter() == null) {
            Log.w(TAG, "getAdapter() == null");
            // SessionListAdapter adapter = new SessionListAdapter(context,
            // R.layout.im_session_message_listview, list);
            // setAdapter(adapter);
            setAdapter(__getAdapter(list));
        } else {
            Log.w(TAG, "getAdapter() != null");
            BaseCustomAdapter<ChatGroupPo> adapter = (BaseCustomAdapter<ChatGroupPo>) getAdapter();
            if (adapter != null) {
                adapter.setItems(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 更新视图
     */
    public void updateView() {
        new GetSessionData().execute();
    }

    public void onEventMainThread(NewMsgEvent event) {
        if (this == event.from)
            return;
        updateView();
    }

    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    protected void initData() {
        mAdapter = (SessionListAdapterV2) __getAdapter(mList);
        setAdapter(mAdapter);
        int size = mList.size();
        setEmptyView(size);
    }

    @SuppressWarnings("ResourceType")
    private class GetSessionData extends AsyncTask<Void, Void, List<ChatGroupPo>> {
        @Override
        protected List<ChatGroupPo> doInBackground(Void... params) {
            List<ChatGroupPo> msgList = getData();
            return msgList;
        }

        @Override
        protected void onPostExecute(List<ChatGroupPo> msgList) {
            Logger.d("yehj", "onPostExecute");
            int size = msgList.size();
            mList.clear();
            mList.addAll(msgList);
            mAdapter.notifyDataSetChanged();
            // 设置空视图
            setEmptyView(size);
        }
    }

    //得到去除头的position
    private int getHeaderViewPosition(int position) {
        int count = getHeaderViewsCount();
        return position - count;
    }
}
