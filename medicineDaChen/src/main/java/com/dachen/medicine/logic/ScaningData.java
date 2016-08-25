package com.dachen.medicine.logic;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.BaseActivity;
import com.dachen.medicine.activity.MainActivity;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.bean.Buydrugitems;
import com.dachen.medicine.bean.Id;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.db.getInfo.UserInfoInDB;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.SubmitInfo;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScaningData {
	//sCanFlag 为4的时候表示扫的是送药的条形码
	public static int sCanFlag = -1;
	public static CdrugRecipeitem item;
	public static List<CdrugRecipeitem> listScaning_showIntitleBar;
	public static List<CdrugRecipeitem> listdrug;
	public static List<CdrugRecipeitem> listmedies;
	public static List<CdrugRecipeitem> listmedies_more_less;
	public static List<CdrugRecipeitem> listScaningCompareScan;

	public static List<CdrugRecipeitem> getlistScaning_showIntitleBar() {
		if (null == listScaning_showIntitleBar) {
			listScaning_showIntitleBar = new ArrayList<CdrugRecipeitem>();
		}
		Collections.sort(listScaning_showIntitleBar, new Comparator<CdrugRecipeitem>() {
			@Override
			public int compare(CdrugRecipeitem lhs, CdrugRecipeitem rhs) {
				if (lhs.createtime>rhs.createtime){
					return 1;
				}
				return -1;
			}
		});
		return listScaning_showIntitleBar;
	}

	public static void  setlistScaning_showIntitleBar(List<CdrugRecipeitem> lists) {
		listScaning_showIntitleBar  =lists;
	}
	public static List<CdrugRecipeitem> getlistScaningCompareScan() {
		if (null == listScaningCompareScan) {
			listScaningCompareScan = new ArrayList<CdrugRecipeitem>();
		}
		return listScaningCompareScan;
	}

	public static void setListScaningCompareScan(List<CdrugRecipeitem> item) {
		listScaningCompareScan = item;
	}

	public static List<CdrugRecipeitem> getListMedies_more_less() {
		if (null == listmedies_more_less) {
			listmedies_more_less = new ArrayList<>();
		}
		return listmedies_more_less;
	}

	public static List<CdrugRecipeitem> getListmedies() {
		if (null == listmedies) {
			listmedies = new ArrayList<>();
		}
		for (CdrugRecipeitem is:listmedies){
			if (is.manufacturer.contains("#0")){
				listmedies.remove(is);
			}
		}
		return listmedies;
	}

	public static void setListMedies_more_less(List<CdrugRecipeitem> more_less) {
		listmedies_more_less = more_less;
	}

	public static void setListmedies(List<CdrugRecipeitem> istmedies) {
		listmedies = istmedies;
	}

	public static List<CdrugRecipeitem> getlistdrug() {
		if (null == listdrug) {
			listdrug = new ArrayList<CdrugRecipeitem>();
		}
		return listdrug;
	}

	public static List<CdrugRecipeitem> setlistdrug(List<CdrugRecipeitem> listdrugs) {
		listdrug = listdrugs;
		return listdrug;
	}

	public static CdrugRecipeitem getGaveInfo() {
		if (item == null) {
			item = new CdrugRecipeitem();
		}
		return item;
	}

	public static void setItem(CdrugRecipeitem items) {
		item = items;
	}

	public static int getGiveRequireNum(int zyzdsxjfs, int num_syjf) {
		int require = 0;
		if (0 != zyzdsxjfs) {
			require = num_syjf / zyzdsxjfs;
		}
		return require;
	}

	public static boolean saveSellInfoToServer(final BaseActivity  context, boolean shownum,boolean isgive ) {
		context.showLoadingDialog();
		User user = UserInfoInDB.getUser(SharedPreferenceUtil.getString("id", ""));
		String patientId = SharedPreferenceUtil.getString("patientid", "");
		String receiveId = SharedPreferenceUtil.getString("receivedId", "");
		List<Id> ids = new ArrayList<Id>();
		List<String> idgive = new ArrayList<>();
		for (int i = 0; i < listmedies.size(); i++) {


			Buydrugitems item = new Buydrugitems();
			if (listmedies.get(i).id.contains("#")) {
				continue;
			}
			item.goodsId = listmedies.get(i).id;
			String code;

			if (null == listmedies.get(i).lists || listmedies.get(i).lists.size() == 0) {
				item.quantity = "0";
				//	continue;
			} else {
				item.quantity = listmedies.get(i).lists.size() + "";
			}


			if (shownum) {
				if (listmedies.get(i).lists != null && listmedies.get(i).lists.size() > 0) {
					int selectNum = 0;

					for (int j2 = 0; j2 < listScaningCompareScan.size(); j2++) {
						if (listmedies.get(i).equals(listScaningCompareScan.get(j2))) {

							for (int j = 0; j < listScaningCompareScan.get(j2).lists.size(); j++) {
								if (listScaningCompareScan.get(j2).lists.get(j).isselect) {
									selectNum++;
								}
							}
							break;
						}
					}
					//item.num_dh = selectNum + "";
				}
			} else {
				//	item.num_dh = "0";
			}

			String selectId = listmedies.get(i).isSlected + "";
			if (listmedies.get(i).notExitInlist) {
				item.comment = "7";
			} else if ("1".equals(selectId) || "2".equals(selectId) || "3".equals(selectId) || "7".equals(selectId)) {
				item.comment = listmedies.get(i).isSlected + "";

			} else {

				item.comment = "";
				//continue;
			}
			String comment = item.comment;
			String goodsId = item.goodsId;
			if (null != listmedies.get(i).lists) {
				for (int j = 0; j < listmedies.get(i).lists.size(); j++) {
					MedieNum items = listmedies.get(i).lists.get(j);
					CdrugRecipeitem drug = new CdrugRecipeitem();
					drug = listmedies.get(i);
					drug.lists.get(j).num = items.num;
					if (!TextUtils.isEmpty(listmedies.get(i).id) && !listmedies.get(i).id.contains("#")) {
						Id id = new Id();
						id.goodsId = goodsId;
						id.drugCode = listmedies.get(i).id;
						id.comment = comment;
						id.quantity =  listmedies.get(i).lists.size()+"";
						ids.add(id);
						if (ScaningData.sCanFlag == 5&&isgive){
							idgive.add(listmedies.get(i).scanCode);
						}
					}

				}
			}
		}

		Gson s = new Gson();
		String json1 = "" + s.toJson(ids);
		//String jsonGive = s.toJson(idgive);;
		String jsonGive = "";
		for (int i=0;i<idgive.size();i++){
			if (i==0){
				jsonGive+=idgive.get(i);
			}else {
				jsonGive+=","+idgive.get(i);
			}
		}


		if (ScaningData.sCanFlag == 5&&isgive) {
			if (ids.size() == 0) {
				context.closeLoadingDialog();
				ToastUtils.showToast("请选择药品");
			} else {
				saveGiveInfo(patientId, item.id, ids.size() + "", jsonGive,context);
			}

		} else {
			new HttpManager().post(
					Constants.SAVE_TO_SERVER,
					SubmitInfo.class,
					Params.getMedieList(user.userId, patientId, receiveId,   json1),
					new HttpManager.OnHttpListener<Result>() {
						@Override
						public void onSuccess(Result response) {
							context.closeLoadingDialog();
							if (response instanceof SubmitInfo) {

								try {
									SubmitInfo patientinfo = (SubmitInfo) response;
									if (patientinfo.resultCode!=1) {

										ToastUtils.showToast(context.getResources().getString(R.string.toast_submit_fail));
										return;
									}

										MedicineApplication.flag = true;
										ToastUtils.showToast(context.getResources().getString(R.string.toast_submit_success));
										//MedicineApplication.flag = true;
										Intent intent = new Intent(context, MainActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										context.startActivity(intent);
										//	super.onBackPressed();
										//MedicineApplication.app.getActivityManager().finishAllActivityExceptMainActivity();

								} catch (Exception e) {
									// TODO: handle exception
									ToastUtils.showToast(context.getResources().getString(R.string.toast_submit_fail));
									//super.onBackPressed();
								}
							}
						}

						@Override
						public void onSuccess(ArrayList<Result> response) {

						}

						@Override
						public void onFailure(Exception e, String errorMsg, int s) {
							context.closeLoadingDialog();
						}
					}, false, 3);
		}
		return false;
	}


	public static void saveGiveInfo( String userid, String goodsid, String buynum, String json, final BaseActivity context ) {
		new HttpManager().post(context,
				 Constants.SAVE_TO_SERVER_GIVE,
				SubmitInfo.class,
				Params.getMedieListGiveMedieList(userid, goodsid, buynum, json),
				new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result response) {
						context.closeLoadingDialog();
						if (response instanceof SubmitInfo) {

							try {
								SubmitInfo patientinfo = (SubmitInfo) response;
								if (patientinfo.resultCode!=1) {

									ToastUtils.showToast(/*context.getResources().getString(R.string.toast_submit_fail)*/response.resultMsg);
									return;
								}
									MedicineApplication.flag = true;
									ToastUtils.showToast(context.getResources().getString(R.string.toast_submit_success));
									//MedicineApplication.flag = true;
									Intent intent = new Intent(context, MainActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									context.startActivity(intent);
									//	super.onBackPressed();
									//MedicineApplication.app.getActivityManager().finishAllActivityExceptMainActivity();

							} catch (Exception e) {
								// TODO: handle exception
								ToastUtils.showToast(context.getResources().getString(R.string.toast_submit_fail));
								//super.onBackPressed();
							}
						}
					}

					@Override
					public void onSuccess(ArrayList<Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						context.closeLoadingDialog();
					}
				}, false, 3);
	}
}