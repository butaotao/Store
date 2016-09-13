package com.dachen.dgroupdoctorcompany.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.im.AppImConstants;
import com.dachen.dgroupdoctorcompany.im.adapter.CustomerSessionAdapter;
import com.dachen.dgroupdoctorcompany.im.events.UnreadEvent;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.imsdk.utils.ImUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot1.event.EventBus;

/**
 * 客服会话列表
 * 
 * @author gaozhuo
 * @date 2015年11月4日
 *
 */
public class CustomerSessionActivity extends BaseActivity  {
	private static final String TAG = CustomerSessionActivity.class.getSimpleName();
	private CustomerSessionAdapter mAdapter;
	private List<ChatGroupPo> mSessionMessages = new ArrayList<ChatGroupPo>();
	private ChatGroupDao dao;

	@Bind(R.id.pull_refresh_listview)
	PullToRefreshListView mPullToRefreshListView;

	@Bind(R.id.title)
	TextView mTitle;

	@Bind(R.id.right_menu)
	ImageButton mRightMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_session);
		ButterKnife.bind(this);
		dao=new ChatGroupDao(this, ImUtils.getLoginUserId());
		initActView();
		loadData();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		BusinessPolling.getInstance().executeTask();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		BusinessPolling.getInstance().cancelTask();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void loadData() {
		List<ChatGroupPo> sessionMessages = dao.queryInBizType(new String[]{AppImConstants.RTYPE_FEEDBACK_PUB_ADMIN});
		if (sessionMessages == null || sessionMessages.isEmpty()) {
			return;
		}
		mSessionMessages.clear();
		mSessionMessages.addAll(sessionMessages);
		mAdapter.notifyDataSetChanged();

	}

	private void initActView() {
		mTitle.setText("玄关健康团队");
		mRightMenu.setVisibility(View.GONE);
		mPullToRefreshListView.setShowIndicator(false);
		mPullToRefreshListView.setMode(Mode.DISABLED);
		mAdapter = new CustomerSessionAdapter(this, mSessionMessages, R.layout.im_session_message_listview);
		mPullToRefreshListView.setAdapter(mAdapter);

		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ChatGroupPo po = (ChatGroupPo) parent.getItemAtPosition(position);
				if (po == null) {
					return;
				}
				// 未读消息清零
				dao.setUnreadZero(po.groupId);
				po.unreadCount=0;
				mAdapter.notifyDataSetChanged();
				// 发出通知
//				ObserverManager.getInstance().notifyNewMsg(null, true);
				EventBus.getDefault().post(new UnreadEvent(UnreadEvent.TYPE_CUSTOMER));
				FeedbackChatActivity.openUI(CustomerSessionActivity.this, po.name, po.groupId, null, true);
//				EventBus.getDefault().post(new UnreadEvent(88001, 0));
			}
		});

	}

	@OnClick(R.id.back_btn)
	public void onBackClick(View v) {
		finish();
	}

	/**
	 * 会话列表更新时会调用这个方法，在这个方法里重新从数据库里取数据
	 */
//	@Override
//	public void onNewMsgReceived(Object msg, boolean isUpdate) {
//		Logger.d(TAG, "onNewMsgReceived");
//		loadData();
//	}
	public void onEventMainThread(NewMsgEvent event){
		loadData();
	}
}
