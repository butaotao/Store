package com.dachen.dgroupdoctorcompany.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.HospitalManagerActivity;
import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.activity.MedieManagementActivity;
import com.dachen.dgroupdoctorcompany.activity.MyInfoDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.MyQRActivity;
import com.dachen.dgroupdoctorcompany.activity.SettingActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.Group;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.im.activity.CustomerSessionActivity;
import com.dachen.dgroupdoctorcompany.im.activity.FeedbackChatActivity;
import com.dachen.dgroupdoctorcompany.im.events.UnreadEvent;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.volley.custom.ObjectResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot1.event.EventBus;

public class MyFragment extends BaseFragment implements OnClickListener,ActionSheetListener, HttpManager.OnHttpListener {
	public final static int REQUEST_USER_INFO = 101;
	// 总View，总视图
	View mRootView;
	@Nullable
	@Bind(R.id.tv_usericon)
	ImageView tv_usericon;//头像
	@Nullable
	@Bind(R.id.tv_title)
	TextView tv_login_title;//titlebar的显示
	@Nullable
	@Bind(R.id.tv_name)
	TextView tv_name;//名字
	@Nullable
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	View mUnreadCount;
	LinearLayout ll_yun;

	public ProgressDialog mDialog;
	private MainActivity mParent;
	/*	@Nullable
        @Bind(R.id.ll_controlmedie)*/
	LinearLayout ll_controlmedie;
	/*
        @Nullable
        @OnClick(R.id.ll_erweima)*/
	void showErweima(){
		//ToastUtils.showToast(mActivity,"二维码");
	}
	/*	@Nullable
        @OnClick(R.id.ll_controlmedie)*/
	void controlMedie(){
		//ToastUtils.showToast(mActivity,"分管药品");
	}
	/*@Nullable
	@OnClick(R.id.ll_settings)*/
	void controlSettings(){
		//ToastUtils.showToast(mActivity,"设置");
	}
	/*@Nullable
	@OnClick(R.id.ll_img_suggest)*/
	 /* void controlSuggest(){
		ToastUtils.showToast(mActivity,"意见反馈");
	}*/
	/*@Nullable
	@OnClick(R.id.ll_about)*/
	void controlAbout(){
		ToastUtils.showToast(mActivity,"关于");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initProgressDialog();
		mParent= (MainActivity) mActivity;
		mRootView = LayoutInflater.from(mActivity).inflate(
				R.layout.activity_fragmentmy, null);
		ButterKnife.bind(mActivity);
		ll_controlmedie = (LinearLayout) mRootView.findViewById(R.id.ll_controlmedie);
		ll_yun = (LinearLayout) mRootView.findViewById(R.id.ll_yun);
		if (UserInfo.getInstance(mActivity).isMediePresent()){
			ll_controlmedie.setVisibility(View.VISIBLE);
			ll_yun.setVisibility(View.VISIBLE);
		}else {
			ll_controlmedie.setVisibility(View.GONE);
			ll_yun.setVisibility(View.GONE);
		}
		mRootView.findViewById(R.id.ll_erweima).setOnClickListener(this);
		mRootView.findViewById(R.id.ll_controlmedie).setOnClickListener(this);
		mRootView.findViewById(R.id.ll_settings).setOnClickListener(this);
		mRootView.findViewById(R.id.ll_img_suggest).setOnClickListener(this);
		mRootView.findViewById(R.id.ll_yun).setOnClickListener(this);
		//mRootView.findViewById(R.id.ll_about).setOnClickListener(this);
		mRootView.findViewById(R.id.ll_logout).setOnClickListener(this);
		mRootView.findViewById(R.id.rl_myinfodetail).setOnClickListener(this);
		tv_usericon = (ImageView) mRootView.findViewById(R.id.tv_usericon);
		tv_phone = (TextView) mRootView.findViewById(R.id.tv_phone);
		tv_name = (TextView) mRootView.findViewById(R.id.tv_name);
		tv_login_title = (TextView) mRootView.findViewById(R.id.tv_title);
		tv_login_title.setText("我");
		mRootView.findViewById(R.id.rl_back).setVisibility(View.INVISIBLE);
		mUnreadCount=mRootView.findViewById(R.id.unread_count);
		new UnreadCountAsyncTask().execute();
		return mRootView;
	}

	private void initProgressDialog(){
		mDialog = new ProgressDialog(mActivity, R.style.IMDialog);
		//		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage("正在加载");
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void setUserInfo(){
		String name = SharedPreferenceUtil.getString(mActivity,"username", "");
		String telephone = SharedPreferenceUtil.getString(mActivity,"telephone", "");
		tv_name.setText("" + name);
		tv_phone.setText("" + telephone);
	}

	@Override
	public void onResume() {
		super.onResume();
		getInfo();
		setUserInfo();
		getAvatarImage();
	}
	private void getInfo(){
		//获取个人信息
		new HttpManager().get(mActivity, Constants.GET_INFO,
				LoginRegisterResult.class,
				Params.getInfoParams(mActivity), this, false, 1);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()){
			case R.id.ll_erweima :
				showErweima();
				intent = new Intent(mActivity, MyQRActivity.class);
				intent.putExtra("ercode","123");
				startActivity(intent);
				break;
			case R.id.ll_controlmedie :
				intent = new Intent(mActivity, HospitalManagerActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_settings :
				intent = new Intent(mActivity, SettingActivity.class);
				startActivity(intent);
				controlSettings();
				break;
			case R.id.ll_img_suggest :
				if(mParent.mGroupInfo!=null){
					toChat(mParent.mGroupInfo);
				}else{
					isCustomer(false);
				}
				break;
			case R.id.ll_about :
				controlAbout();
				break;
			case R.id.ll_logout:
				onLogoutBtnClicked();
				break;
			case R.id.rl_myinfodetail:
				intent  = new Intent(mActivity,MyInfoDetailActivity.class);
				startActivityForResult(intent,REQUEST_USER_INFO);
				break;
			case R.id.ll_yun:
				intent = new Intent(mActivity, MedieManagementActivity.class);
				startActivity(intent);
				controlMedie();
				break;
		}
	}

	void onLogoutBtnClicked(){  ///im/removeDeviceToken.action
		mDialog.show();
		CompanyApplication.setInitContactList(1);
		//联网 从服务器登出
		new HttpManager().post(mActivity, Constants.LOGOUT + "", LoginRegisterResult.class,
				Params.getLoginoutParams(mActivity, SystemUtils.getDeviceId(mActivity)), new HttpManager.OnHttpListener<LoginRegisterResult>() {

					@Override
					public void onSuccess(Result response) {
						// TODO Auto-generated method stub
						if (null == response) {
							//removeregeisterXiaoMi();
							SharedPreferenceUtil.putString(mActivity, "session", "");
							SharedPreferenceUtil.putLong(mActivity, "expires_in", 0L);
							Intent intent = new Intent(mActivity, LoginActivity.class);
							startActivity(intent);
							mActivity.finish();
							mDialog.dismiss();
							return;
						} else {
							if (response instanceof LoginRegisterResult) {
								//	removeregeisterXiaoMi();
								SharedPreferenceUtil.putString(mActivity, "session", "");
								SharedPreferenceUtil.putLong(mActivity, "expires_in", 0L);
								Intent intent = new Intent(mActivity, LoginActivity.class);
								startActivity(intent);
								mActivity.finish();
								mDialog.dismiss();
							}
						}
					}

					@Override
					public void onSuccess(ArrayList<LoginRegisterResult> response) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Exception e, String errorMsg,
										  int s) {
						//removeregeisterXiaoMi();
						SharedPreferenceUtil.putString(mActivity, "session", "");
						SharedPreferenceUtil.putLong(mActivity, "expires_in", 0L);
						// TODO Auto-generated method stub
						Intent intent = new Intent(mActivity, LoginActivity.class);
						startActivity(intent);
						mActivity.finish();
						mDialog.dismiss();
					}
				},
				false, 1);

	}
	public void removeregeisterXiaoMi(){
		/*LogUtils.burtLog("mRegId=="+SharedPreferenceUtil.getString("mRegId", ""));
		HashMap<String, String> infaces = new HashMap<String, String>();
		infaces.put("interface1", Constants.XIAOMIREMOVE);
		new HttpManager().post(infaces, ResultData.class,
				Params.getRemoveReginsterXiaoMiReceiver(SharedPreferenceUtil.getString("id", ""),
						SharedPreferenceUtil.getString("mRegId", ""),
						SharedPreferenceUtil.getString("session", "")), this,
				1,8090);*/
	}
	/**
	 * 获取头像
	 */
	private void getAvatarImage() {
		// 获取头像

		//TODO
			/*if(user!=null)
			{
				String userId = user.getUserId();
				String headPicFileName = user.getHeadPicFileName();
				String avatarUrl = StringUtils.getAvatarUrl(userId, headPicFileName);
				if(!TextViewUtils.isEmpty(avatarUrl))
				{
					ImageLoader.getInstance().displayImage(avatarUrl, head_icon);
				}
				else head_icon.setImageResource(R.drawable.head_icon);
			}
			else {//logins.data.getUser().getUserId()+"head_url", url);
*/				String avatarUri = SharedPreferenceUtil.getString(mActivity,SharedPreferenceUtil.getString(mActivity,"id", "") + "head_url", "");

		if(!TextUtils.isEmpty(avatarUri))
		{
			//ImageLoader.getInstance().displayImage(avatarUri, tv_usericon);
			CustomImagerLoader.getInstance().loadImage(tv_usericon, avatarUri,
					R.drawable.avatar_normal, R.drawable.avatar_normal); //R.drawable.head_icons_company
		}
		else tv_usericon.setImageResource(R.drawable.head_icon);
		//}
	}
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		if(null != response){
			if(response instanceof LoginRegisterResult){
				String userID = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");
				LoginRegisterResult userInfo = (LoginRegisterResult) response;
				if (null!=userInfo&&null!=((LoginRegisterResult) response).data){
					Message msg = Message.obtain();
					msg.obj = userInfo;
					if (userInfo.data==null||null==userInfo.data.majorUser){
						return;
					}
					CompanyContactListEntity entity = new CompanyContactListEntity();
					entity.userId = userInfo.data.userId+"";
					entity.id = userInfo.data.majorUser.id;
					entity.department = userInfo.data.majorUser.orgName;

					entity.headPicFileName = userInfo.data.majorUser.headPic;
					entity.position = userInfo.data.majorUser.title;
					entity.telephone = userInfo.data.majorUser.telephone;
					entity.userloginid = userID;
					entity.name = userInfo.data.majorUser.name;
					LoginRegisterResult logins = (LoginRegisterResult) response;
					UserLoginc.setUserInfo(logins, mActivity);
					SharedPreferenceUtil.putString(mActivity,"username",entity.name);
					SharedPreferenceUtil.putString(mActivity, "telephone", entity.telephone);
					SharedPreferenceUtil.putString(mActivity, SharedPreferenceUtil.getString(mActivity, "id", "") + "head_url",entity.headPicFileName);
					setUserInfo();
					getAvatarImage();
				}
			}
		}
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub

	}

	private void isCustomer(final boolean silent) {
		if(!silent){
			mDialog.show();
		}
		final String reqTag = "isCustomer";
		RequestQueue queue = VolleyUtil.getQueue(mActivity);
		queue.cancelAll(reqTag);
		StringRequest request = new StringRequest(Method.POST,  AppConfig.getUrl(Constants.PUB_ISCUSTOMER,3), new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(!silent){
					mDialog.dismiss();
				}
				handleResponse(response,silent);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if(!silent){
					mDialog.dismiss();
				}
				ToastUtil.showErrorNet(mActivity);
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("access_token", ImSdk.getInstance().accessToken);
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
		request.setTag(reqTag);
		queue.add(request);
	}

	private void handleResponse(String response,boolean silent) {
		ObjectResult<Group> result = JSON.parseObject(response, new TypeReference<ObjectResult<Group>>() {
		});
		if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null) {
			ToastUtil.showErrorNet(mActivity);
			return;
		}

		Group group = result.getData();
		if (group.isCustomer == 0&&group.group == null) {
			ToastUtil.showErrorNet(mActivity);
			return;
		}

		mParent.mGroupInfo=group;
		if(silent){
			new UnreadCountAsyncTask().execute();
		}else
			toChat(group);
	}

	private void toChat(Group group){
		if (group.isCustomer == 1) {// 客服
			Intent intent = new Intent(getActivity(), CustomerSessionActivity.class);
			startActivity(intent);

		} else if (group.isCustomer == 0) {// 普通用户
			if (group.group == null) {
				ToastUtil.showErrorNet(mActivity);
				return;
			}
			FeedbackChatActivity.openUI(mActivity, group.group.gname, group.group.gid, null);
			// 未读消息清零
			ChatGroupDao dao=new ChatGroupDao();
			dao.setUnreadZero(group.group.gid);
			// 发出通知
			EventBus.getDefault().post(new UnreadEvent(UnreadEvent.TYPE_CUSTOMER,this));
//			ObserverManager.getInstance().notifyNewMsg(null, true);
			mUnreadCount.setVisibility(View.GONE);
		}

	}

	class UnreadCountAsyncTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			return mParent.getMeUnreadCount();
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			showUnreadCount(result);
		}
	}

	private void showUnreadCount(Integer unreadCount) {
		if (unreadCount <= 0) {
			mUnreadCount.setVisibility(View.GONE);
		} else {
			mUnreadCount.setVisibility(View.VISIBLE);
		}
	}

	public void onEventMainThread(UnreadEvent event){
		if(this==event.from)
			return;
		if(event.type==UnreadEvent.TYPE_CUSTOMER){
			new UnreadCountAsyncTask().execute();
		}
	}
	public void onEventMainThread(NewMsgEvent event){
		new UnreadCountAsyncTask().execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(requestCode == REQUEST_USER_INFO){
			String loginUserId = SharedPreferenceUtil.getString(mActivity,"id", "");
			String headUrl = SharedPreferenceUtil.getString(mActivity,loginUserId+"head_url","");
			if(!TextUtils.isEmpty(headUrl))
			{
//				ImageLoader.getInstance().displayImage(headUrl, tv_usericon);
				CustomImagerLoader.getInstance().loadImage(tv_usericon,headUrl);
			}
			else tv_usericon.setImageResource(R.drawable.head_icon);

			String name = SharedPreferenceUtil.getString(mActivity,"username", "");
			tv_name.setText(""+ name);
		}
	}
}
