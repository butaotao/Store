package com.dachen.medicine.app;

public class Constants {
    public static String USER_LORGIN_AUTO =  "user/login/auto";
    public final static String USERTYPE = "10";
    /**
     * 网络连接超时
     **/
    public static final int HTTP_REQUEST_CONNECT_TIMEOUT = 198;

    /**
     * 网络连接异常
     **/
    public static final int HTTP_REQUEST_NET_WORK_EXCEPTION = 199;

    /**
     * 请求成功状态码
     **/
    public static final int HTTP_REQUEST_SUCCESS_CODE = 200;

    public static final int HTTP_REQUEST_NOT_LOGIN = 302;
    public static String USER_UPDATE = "user/update";
    /**
     * 错误返回码
     **/
    public static final int HTTP_REQUEST_ERROR_CODE = 500;
    /**
     * 我 登出
     */

    public static String LOGOUT = "user/logout";
    // 登录接口
    public static final String LOGIN = "user/login";
    public static final String XIAOMI = "health/user/registerDeviceToken";
    public static final String XIAOMIREMOVE = "im/removeDeviceToken.action";
    // 注册借口
    public static final String REGISTER = "sms/verify/telephone";
    /**
     * 请求验证码
     */
    public static String SEND_AUTH_CODE = "sms/randcode/getSMSCode";

    public static String GOOD_SELECT = "c_GoodsIndication.select?";

    // 上传个人头像
    public static String UploadAvatarServlet = "upload/UploadAvatarServlet";


    public static String CODE_ID = "c_Recipe.getDrug_from_mc?";
    public static String ALL_ILL_INFO = "c_SFFL.select_explorer";
    /**
     * 扫二维码后得到的药单
     */
    public static String SALAESCLERK = "c_Recipe.qr_code?";
    /**
     * 药品销售记录
     */
    public static String SALAE_SRECORDS = "c_DrugSalesLog.query?";
    /**
     * 修改密码
     */
    public static String MODIFY_PWD = "user/updatePassword";

    public static String RECIPEGETRECIPE_ITEMS_GIVE = "c_user_JF.get_goods_toDH?";
    public static String RECIPEGETRECIPE_ITEMS = "c_Recipe.get_recipeitems?";
    //得到用户信息
    public static String PATIENT_INFO = "c_Recipe.qr_code?";
    //提交药单
    public static String SAVE_TO_SERVER = "goods/salesLog/patientBuyDrug";
    //提交赠药订单
    public static String SAVE_TO_SERVER_GIVE = "goods/salesLog/patientToExchangeGoods";
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
     * 收入基本信息
     */
    public static String INCOME_BASE_INFO = "tradeIncomelog/getIncomeInfo";

    /**
     * 总收入列表
     */
    public static String INCOME_ALL_LIST = "tradeIncomelog/totalIncomeList";

    /**
     * 待审核收入列表
     */
    public static String INCOME_UN_CHECK_LIST = "tradeIncomelog/unAuditedAmtList";

    /**
     * 总收入详情
     */
    public static String INCOME_ALL_DETAIL = "tradeIncomelog/totalIncomeDetail";

    /**
     * 待审核收入详情
     */
    public static String INCOME_UN_CHECK_DETAIL = "tradeIncomelog/unAuditedAmtDetail";

    /**
     * 账户余额详情
     */
    public static String INCOME_BALANCE_DETAIL = "tradeIncomelog/accountBalanceDetail";

    //获取版本号
    public static String GET_VERSION = "appService/getVersion";
}
