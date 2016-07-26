package com.dachen.medicine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.db.MedicineURIField;
import com.dachen.medicine.db.table.TableUser;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 * 
 * @author lmc
 * 
 */
public class ModifyPasswordActivity extends BaseActivity implements
		OnHttpListener {

	private static final String TAG = ModifyPasswordActivity.class
			.getSimpleName();

	@Nullable
	@Bind(R.id.modifyPwdActivity_oldPwd)
	ClearEditText mOldPwdView;

	@Nullable
	@Bind(R.id.modifyPwdActivity_newPwd)
	ClearEditText mNewPwdView;

	@Nullable
	@Bind(R.id.modifyPwdActivity_confirmPwd)
	ClearEditText mConfirmPwdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pwd);
		ButterKnife.bind(this);

	}

	@Nullable
	@OnClick(R.id.modifyPwdActivity_back_btn)
	void onBackBtnClicked() {
		finish();
	}

	@Nullable
	@OnClick(R.id.modifyPwdActivity_submit)
	void onSubmitBtnClicked() {

		final String oldPwd = mOldPwdView.getText().toString();
		final String newPwd = mNewPwdView.getText().toString();
		String confirmPwd = mConfirmPwdView.getText().toString();




		if (TextUtils.isEmpty(oldPwd)) {
			mOldPwdView.requestFocus();
			ToastUtils.showToast( "请输入旧密码");
			return;
		}
		if (TextUtils.isEmpty(newPwd)) {
			mNewPwdView.requestFocus();
			ToastUtils.showToast("请输入新密码");
			return;
		}
		if (newPwd.length() < 6 || newPwd.length() > 18) {
			mNewPwdView.requestFocus();
			ToastUtils.showToast( "新密码长度为6-18位字符");
			return;
		}
		if (TextUtils.isEmpty(confirmPwd)) {
			mConfirmPwdView.requestFocus();
			ToastUtils.showToast("请输入确认密码");
			return;
		}
		if (!newPwd.equals(confirmPwd)) {
			mConfirmPwdView.requestFocus();
			ToastUtils.showToast( "新密码两次输入不一致");
			return;
		}


		String id = SharedPreferenceUtil.getString("id", null);
		//User user = UserInfoInDB.getUser(id);
		/*LogUtils.burtLog("users---" + user.getUserId() + "==="
				+ user.getToken());*/
		login( SharedPreferenceUtil.getString("id", ""), oldPwd, newPwd, SharedPreferenceUtil.getString("session", ""));

	}

	public void login(String userId, String oldPwd, String newPwd, String token) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("oldPassword", oldPwd);
		params.put("newPassword", newPwd);
		params.put("access_token", token);
		new HttpManager().post(Constants.MODIFY_PWD, LoginRegisterResult.class,
				params, this, false, 1);
	}

	@Override
	public void onSuccess(Result entity) {

		// TODO Auto-generated method stub
		if (entity instanceof LoginRegisterResult) { 
			if (entity.resultCode==1) { 
				LoginRegisterResult logins = (LoginRegisterResult) entity;
				SharedPreferenceUtil.putString("session",
						logins.data.getAccess_token());
				SharedPreferenceUtil.putString("id", logins.data.getUserId());

				User user = logins.data.getUser();
				LogUtils.burtLog("logins.data.access_token-----"
						+ logins.data.access_token);
				user.setPassword("jiayou123");
				user.setToken(logins.data.access_token);
				user.setDescription("bbt"); 
				LogUtils.burtLog("user===" + user);
				MedicineApplication
						.getApplication()
						.getContentResolver()
						.insert(MedicineURIField.DOWNLOAD_TASK_URI,
								TableUser.buildContentValues(user));
				ToastUtils.showToast("修改密码成功");
				finish();

			}else {
				ToastUtils.showToast(entity.resultMsg); 
			}
		}
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(getString(R.string.connect_error)); 
	}
}
