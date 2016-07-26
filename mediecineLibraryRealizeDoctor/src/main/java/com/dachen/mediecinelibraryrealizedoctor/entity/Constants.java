package com.dachen.mediecinelibraryrealizedoctor.entity;

public class Constants {

	/** 网络连接超时 **/
	public static final int HTTP_REQUEST_CONNECT_TIMEOUT = 198;

	/** 网络连接异常 **/
	public static final int HTTP_REQUEST_NET_WORK_EXCEPTION = 199;

	/** 请求成功状�?�?**/
	public static final int HTTP_REQUEST_SUCCESS_CODE = 200;

	public static final int HTTP_REQUEST_NOT_LOGIN = 302;

	/** 错误返回�?**/
	public static final int HTTP_REQUEST_ERROR_CODE = 500;
	/**
	 * �?登出
	 */

	public static String LOGOUT = "user/logout";
	// 登录接口
	public static final String LOGIN = "user/login";
	// 注册借口
	public static final String REGISTER = "sms/verify/telephone";
	/**
	 * 请求验证�?
	 */
	public static String SEND_AUTH_CODE = "sms/randcode/getSMSCode";

	public static String GOOD_SELECT = "c_GoodsIndication.get_Goods?";

	// 上传个人头像
	public static String UploadAvatarServlet ="upload/UploadAvatarServlet";


	public static String CODE_ID = "c_Recipe.getDrug_from_mc?";
	public static String ALL_ILL_INFO = "c_SFFL.select_explorer";
	/**
	 * 扫二维码后得到的药单
	 */
	public static String SALAESCLERK = "c_Recipe.qr_code?";
	/**
	 * 药品�?��记录
	 */
	public static String SALAE_SRECORDS = "c_DrugSalesLog.query?";
	/**
	 * 修改密码
	 */
	public static String MODIFY_PWD = "user/updatePassword";

	public static String RECIPEGETRECIPE_ITEMS = "c_Recipe.get_recipeitems?";
	//得到用户信息
	public static String PATIENT_INFO = "c_Recipe.qr_code?";
	//提交药单
	public static String SAVE_TO_SERVER = "c_Recipe.drug_buy_bypatient?";
	/**
	 * 请求重置密码
	 */
	public static String PRE_RESET_PASSWD = "user/preResetPassword";
	/**
	 * 重置密码
	 */
	public static String RESET_PASSWD = "user/resetPassword";
	/**
	 * 用户 - 验证重设密码的验证码
	 */
	public static String VERIFYRESETPASSWORD = "user/verifyResetPassword";
	/**
	 * 常备药 - 根据用户ID获取常备药列表
	 */
	public static String GET_DRUG_LIST = "org/drugCollection/getDrugCollectionListByUserId";
	/**
	 * 药方 - 新增药方
	 */
	public static String ADD_RECIPE = "org/recipe/addRecipe";
	/**
	 * 品种 - 获取某个患者的积分列表
	 */
	public static String GET_USER_POINTS = "org/goods/points/getUserPointsByPatientId";
	/**
	 * 品种积分 - 获取可以接收的总的积分数
	 */
	public static String GET_TOTAL_POINTS = "org/goods/points/countUserGoodsPointsList";
	/**
	 * 品种组 - 根据关键字搜索品种组
	 */
	public static String SEARCH_GOODS = "org/goods/group/getGoodsGroupList";
	/**
	 * 常备药（药品收藏） - 批量新增常备药
	 */
	public static String ADD_DRUG_LIST = "org/drugCollection/addMutilDrugCollection";
	/**
	 * 常备药（药品收藏） - 删除常备药（药品收藏）
	 */
	public static String DELETE_DRUG = "org/drugCollection/deleteDrugCollection";
	/**
	 * 品种 - 查询指定品种组下面的品种列表
	 */
	public static String SEARCH_DRUG_BY_GROUPID = "org/goods/getGoodsListByGroupId";
	/**
	 * 品种积分 - 查询患者的获取到积分明细
	 */
	public static String GET_PATIENT_POINT_DETAIL = "org/goods/points/getUserReceivePointsDetailList";
	/**
	 * 品种积分 - 领取积分
	 */
	public static String GET_POINTS = "org/goods/points/receiveGoodsPoints";
	/**
	 * 品种积分 - 获取赠药积分
	 */
	public static String GET_GIVEN_DRUG_POINTS = "org/goods/points/getUserGivenPointsDetailList";
}
