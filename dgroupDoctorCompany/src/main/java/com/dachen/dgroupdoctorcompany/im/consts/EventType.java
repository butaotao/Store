package com.dachen.dgroupdoctorcompany.im.consts;

/**
 * 通知类型
 * 
 * 
 *  * 个人通讯录相关
    ADD_FRIEND("1","新的好友"), //加好友通知
	DEL_FRIEND("2","删好友"),
	PROFILE_CHANGE("3","个人资料变化"),
	GROUP_ADD_DOCTOR("4","加入医生集团"),
	GROUP_DELETE_DOCTOR("5","离开医生集团"),
//	FRIEND_REQ_CHANGE("8","新的好友"), //验证请求列表有变化
    
	 * 群组相关
	GROUP_ADDUSER("10","增加群组成员"),
	GROUP_DELUSER("11","删除群组成员"), 
	GROUP_QUIT("12","退出群聊"),
    GROUP_CHANGE_NAME("13","修改群组名称"), 
    GROUP_CHANGE_PIC("14","修改群组头像"); 

 * @author lmc
 *
 */
public class EventType {
	public static final String friend_add = "1";
	public static final String friend_delete = "2";
	public static final String friend_info_change = "3";
	
	// 医生集团的通讯录发生变更
	public static final String doctor_bloc_address_book_add = "4";
	public static final String doctor_bloc_address_book_delete = "5";

	public static final String group_add_user = "10";
	public static final String group_delete_user = "11";
	public static final String group_user_exit = "12";
	public static final String group_change_name = "13";
	public static final String group_change_avatar_image = "14";
	
	public static final String DOCTOR_ONLINE="7";  //医生上线
	public static final String DOCOTR_OFFLINE="8"; //医生下线
	public static final String DOCTOR_OFFLINE_SYSTEM_FORCE="9"; //医生被系统强制下线
	public static final String DOCTOR_DISTURB_ON="16"; //"医生开启免打扰"
	public static final String DOCTOR_DISTURB_OFF="17"; //"医生关闭免打扰"
	public static final String DOCTOR_NEW_CHECKIN="19";// 患者报到
	public static final String DOCTOR_APPLY_NUM="24";// 会诊变化

}
