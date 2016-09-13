package com.dachen.dgroupdoctorcompany.im.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.BaseCustomAdapter;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.activity.GroupChatSetingUI;
import com.dachen.dgroupdoctorcompany.im.activity.GroupChatSetingUI.Item;
import com.dachen.imsdk.consts.SessionType;
import com.dachen.imsdk.utils.ImUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 群聊设置适配器
 *
 * @author lmc
 *
 */
public class GroupChatSetingAdapter extends BaseCustomAdapter<Item> {

	private static final String TAG = GroupChatSetingAdapter.class.getSimpleName();

	private String groupId = null;
	private int sessionType = SessionType.not_know;
	private String mFrom;
	private GroupChatSetingUI ui;

	public GroupChatSetingAdapter(GroupChatSetingUI context, int resId, String from) {
		super(context, resId);
		ui=context;
		mFrom = from;
		init();
	}

	private void init() {

	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

	public int getSessionType() {
		return sessionType;
	}

	@Override
	protected void fillValues(BaseViewHolder baseViewHolder, final int position) {

		final ViewHolder holder = (ViewHolder) baseViewHolder;
		final Item item = getItem(position);

		// 设置图片
		if (item.isUser) {

			// 设置头像
			// String avatarUri =
			// FriendDao.getInstance().getAvatarThumbnailUri(item.userId);
			// if(avatarUri != null) {
			// ImageLoader.getInstance().displayImage(avatarUri,
			// holder.im_group_chat_gridview_imageView);
			// }else {
			// holder.im_group_chat_gridview_imageView.setImageResource(R.drawable.avatar_normal);
			// }
			//
			ImageLoader.getInstance().displayImage(item.avatarImageUri, holder.im_group_chat_gridview_imageView, CompanyApplication.mAvatarRoundImageOptions);

			if (item.isMy) {

			} else {

				// 删除用户按钮
				holder.im_group_chat_gridview_delete_user.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.w(TAG, "click ..im_group_chat_gridview_delete_user ");

						if(sessionType == SessionType.session_double){
							ToastUtil.showToast(mContext, "双人组不能删除群成员");
							return;
						}
						/**
						 * 删除某个用户
						 */
						removeGridView(item);
					}

				});

			}

		} else {
			if (item.extra == Item.Extra.ADD) {
				holder.im_group_chat_gridview_imageView.setImageResource(R.drawable.group_chat_add);
//				holder.im_group_chat_gridview_linear.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Log.w(TAG, "click im_group_chat_gridview_linear -> add");
////						CreateGroupActivity.updateGroupUIForResult(mActivity, groupId, sessionType, getUserIds(), mFrom
////								,true,GroupChatSetingUI.REQUEST_CODE_UPDATE_GROUP);
//						CallIntent.SelectPeopleActivityForResult(mActivity,groupId, getUsers() , sessionType, GroupChatSetingUI.REQUEST_CODE_UPDATE_GROUP);
//					}
//				});
			} else if (item.extra == Item.Extra.DELETE) {
				holder.im_group_chat_gridview_imageView.setImageResource(R.drawable.group_chat_delete);
//				holder.im_group_chat_gridview_linear.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Log.w(TAG, "click ..im_group_chat_gridview_linear .. delete...");
//						/**
//						 * 刷新删除按钮状态
//						 */
//						refreshDeleteStatus();
//					}
//
//				});
			}
		}

		// 处理删除按钮
		if (item.isShowDelete) {
			holder.im_group_chat_gridview_delete_user.setVisibility(View.VISIBLE);
		} else {
			holder.im_group_chat_gridview_delete_user.setVisibility(View.GONE);
		}

		// 设置名称
		holder.im_group_chat_gridview_name.setText(item.name);

	}

	/**
	 * 得到用户userIds数组
	 *
	 * @return
	 */
	private ArrayList<String> getUserIds() {
		ArrayList<String> userIds = new ArrayList<String>();
		List<Item> _item = getItems();
		// 筛选
		for (Item i : _item) {
			if (i != null && i.isUser == true && i.isMy == false) {
				userIds.add(i.userId);
			}
		}
		return userIds;
	}
	private ArrayList<CompanyContactListEntity> getUsers() {
		ArrayList<CompanyContactListEntity> users = new ArrayList<CompanyContactListEntity>();
		List<Item> _item = getItems();
		// 筛选
		for (Item i : _item) {
			if (i != null && i.isUser == true) {// && i.isMy == false
				CompanyContactListEntity user=new CompanyContactListEntity();
				user.userId=i.userId;
				users.add(user);
			}
		}
		return users;
	}

	/**
	 * 删除一个Item
	 *
	 * @param item
	 */
	private void removeGridView(Item item) {
		if (item == null) {
			return;
		}

//		Object obj = BaseActivity.mObserverUtil.getObject(GroupChatSetingUI.class);
		if (ui != null && ui.isFinishing() == false) {
//			ui.removeGridView(item);
			ui.alertRemoveUser(item);
		}
	}

	/**
	 * 刷新删除按钮状态
	 */
	private void refreshDeleteStatus() {
		// 把删除按钮显示出来
		List<Item> _list = getItems();
		for (Item i : _list) {
			if (i.isUser&&!ImUtils.getLoginUserId().equals(i.userId) ) {
				i.isShowDelete = !i.isShowDelete;
			}
		}
		// 刷新适配器
		notifyDataSetChanged();
	}

	static class ViewHolder extends BaseViewHolder {

		@Nullable
		@Bind(R.id.im_group_chat_gridview_linear)
		LinearLayout im_group_chat_gridview_linear;

		@Nullable
		@Bind(R.id.im_group_chat_gridview_imageView)
		ImageView im_group_chat_gridview_imageView;

		@Nullable
		@Bind(R.id.im_group_chat_gridview_delete_user)
		Button im_group_chat_gridview_delete_user;

		@Nullable
		@Bind(R.id.im_group_chat_gridview_name)
		TextView im_group_chat_gridview_name;

	}

	@Override
	protected BaseViewHolder getViewHolder() {
		return new ViewHolder();
	}

}
