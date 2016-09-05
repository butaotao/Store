package com.dachen.dgroupdoctorcompany.im.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.dachen.common.async.SimpleResultListenerV2;
import com.dachen.common.json.GJson;
import com.dachen.common.utils.CommonUtils;
import com.dachen.common.utils.QiNiuUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.UIHelper;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.common.widget.UISwitchButton;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.ColleagueDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.DoctorDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.SmallEditViewUI;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.im.adapter.GroupChatSetingAdapter;
import com.dachen.dgroupdoctorcompany.im.bean.UpdateGroup2Bean;
import com.dachen.dgroupdoctorcompany.im.events.AddGroupUserEvent;
import com.dachen.dgroupdoctorcompany.utils.CallIntent;
import com.dachen.dgroupdoctorcompany.views.DialogGetPhote;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;
import com.dachen.imsdk.HttpErrorCode;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.activities.ImBaseActivity;
import com.dachen.imsdk.consts.SessionType;
import com.dachen.imsdk.consts.UserType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.entity.event.GroupSettingEvent;
import com.dachen.imsdk.entity.event.NewMsgEvent;
import com.dachen.imsdk.net.ImCommonRequest;
import com.dachen.imsdk.net.PollingURLs;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.UploadEngine7Niu;
import com.dachen.imsdk.service.ImRequestManager;
import com.dachen.imsdk.utils.CameraUtil;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.volley.custom.ObjectResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot1.event.EventBus;

/**
 * 群聊设置界面
 * <p/>
 * 使用例子： GroupChatSetingUI.openUI(context, groupId);
 *
 * @author lmc
 */
public class GroupChatSetingUI extends ImBaseActivity {

    private static final String TAG = GroupChatSetingUI.class.getSimpleName();
    public static final int REQUEST_CODE_UPDATE_GROUP = 10001;
    public static final int REQUEST_CODE_EDIT_TEXT = 10002;
    public static final int REQUEST_CODE_AVATAR = 10003;
    public static final int REQUEST_CODE_CROP_PIC = 10004;
    private static final int REQUEST_CODE_CAMERA = 10005;
    private static final int REQUEST_CODE_PICK_GALLERY = 10006;

    public static final String Key_groupId = "key_groupId";
    public static final String Key_NO_ADD = "key_no_add";

    private String groupId ;
    private boolean noAdd;
    private int extraItemNum = 2;

    private String groupName = null; // 组名称
    private boolean messgeRemind = false; // 消息提醒
    private Item deleteItem = null; // 要删除用户的Item
    private Uri mNewPhotoUri;

    private boolean b_executeObtainTask_successed = false;// executeObtainTask是否执行成功

    private int whatNotKnowTask = -1;
    // private int whatClearChatRecordTask = 0; // 清空本地的
    private int whatExitMultipleChatTask = 1;
    private int whatChangeGroupNameTask = 2;
    private int whatMessgeRemindYesTask = 3;
    private int whatMessgeRemindNoTask = 4;
    // private int whatAddUserTask = 5;
    private int whatDeleteUserTask = 6;
    private int whatFavYesTask = 7;
    private int whatFavNoTask = 8;

    private int execute_what = whatNotKnowTask;

    @Nullable
    @Bind(R.id.im_group_chat_ui_title)
    TextView im_group_chat_ui_title;

    @Nullable
    @Bind(R.id.im_group_chat_ui_gridView)
    GridView im_group_chat_ui_gridView;

    @Nullable
    @Bind(R.id.im_group_chat_ui_name_value)
    TextView im_group_chat_ui_name_value;

    @Nullable
    @Bind(R.id.im_group_chat_ui_exit_multiple_chat)
    Button im_group_chat_ui_exit_multiple_chat;

    @Nullable
    @Bind(R.id.im_group_chat_ui_clear_chat_record)
    RelativeLayout im_group_chat_ui_clear_chat_record;

    @Nullable
    @Bind(R.id.im_group_chat_ui_setting_name_layout)
    RelativeLayout im_group_chat_ui_setting_name_layout;

    @Bind(R.id.patient_layout)
    LinearLayout mPatientLayout;

    @Bind(R.id.avatar)
    ImageView mAvatar;

    @Bind(R.id.name)
    TextView mName;

    @Bind(R.id.sex)
    TextView mSex;

    @Bind(R.id.age)
    TextView mAge;

    @Bind(R.id.group_settting_layout)
    LinearLayout mGroupSettingLayout;
    @Bind(R.id.iv_avatar)
    ImageView iv_avatar;
    @Bind(R.id.layout_avatar)
    View layout_avatar;

    UISwitchButton im_group_chat_ui_setting_messge_remind;
    UISwitchButton im_group_chat_ui_setting_fav;
    UISwitchButton switchTopChat;

    private GroupChatSetingAdapter mAdapter;
    // 是否设置过患者信息
    private boolean mIsSetPatientInfo;

    private ChatGroupDao dao;
    private DialogGetPhote mPicDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_group_chat_setting_ui);
        dao = new ChatGroupDao();
        ButterKnife.bind(this);
        init();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mDialog = null;
    }

    @OnItemClick(R.id.im_group_chat_ui_gridView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) parent.getItemAtPosition(position);
        if (item == null) return;
        if (!item.isUser) {
            if (item.extra == Item.Extra.ADD) {
                CallIntent.SelectPeopleActivityForResult(mThis, groupId, getUsers(), mAdapter.getSessionType(), GroupChatSetingUI.REQUEST_CODE_UPDATE_GROUP);
            } else if (item.extra == Item.Extra.DELETE) {
                refreshDeleteStatus();
            }
            return;
        }
        Intent intent;
        if (item.userType == UserType.MedCompany) {
            intent = new Intent(this, ColleagueDetailActivity.class);
            intent.putExtra("id", item.userId);
            startActivity(intent);
        } else if (item.userType == UserType.Doctor) {
            DoctorDao mDoctorDao = new DoctorDao(this);
            List<Doctor> l = mDoctorDao.queryByUserId(item.userId);
            if (l != null && l.size() > 0) {
                Doctor d = l.get(0);
                intent = new Intent(this, DoctorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doctordetail", d);
                intent.putExtra("doctordetail", bundle);
                startActivity(intent);
            }
        }

    }

    private void refreshDeleteStatus() {
        // 把删除按钮显示出来
        List<Item> _list = mAdapter.getItems();
        for (Item i : _list) {
            if (i.isUser && !ImUtils.getLoginUserId().equals(i.userId)) {
                i.isShowDelete = !i.isShowDelete;
            }
        }
        // 刷新适配器
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<CompanyContactListEntity> getUsers() {
        ArrayList<CompanyContactListEntity> users = new ArrayList<CompanyContactListEntity>();
        List<Item> _item = mAdapter.getItems();
        // 筛选
        for (Item i : _item) {
            if (i != null && i.isUser == true) {// && i.isMy == false
                CompanyContactListEntity user = new CompanyContactListEntity();
                user.userId = i.userId;
                users.add(user);
            }
        }
        return users;
    }

    private void init() {
        // 设置“清空聊天记录”控件的可见性
        setClearChatRecordVisibility(false); // TODO 暂时禁用
        // 组名称
        setGroupNameVisibility(false);
        // 设置“退出多人聊天”控件的可见性
        setExitMultipleChatVisibility(false);
        Intent i = this.getIntent();
        this.groupId = i.getStringExtra(Key_groupId);
        im_group_chat_ui_setting_messge_remind = (UISwitchButton) findViewById(R.id.im_group_chat_ui_setting_messge_remind);
        im_group_chat_ui_setting_messge_remind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (b_executeObtainTask_successed) {
                    if (isChecked) {
                        execute_what = whatMessgeRemindYesTask;
                    } else {
                        execute_what = whatMessgeRemindNoTask;
                    }
                    executeUpdateTask(execute_what);
                }
            }
        });
        im_group_chat_ui_setting_fav = (UISwitchButton) findViewById(R.id.im_group_chat_ui_setting_fav);
        im_group_chat_ui_setting_fav.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (b_executeObtainTask_successed) {
                    if (isChecked) {
                        execute_what = whatFavYesTask;
                    } else {
                        execute_what = whatFavNoTask;
                    }
                    executeUpdateTask(execute_what);
                }
            }
        });
        switchTopChat = (UISwitchButton) findViewById(R.id.switch_top_chat);
        switchTopChat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(!b_executeObtainTask_successed)return;
                final int act=isChecked?1:0;
                mDialog.show();
                ImRequestManager.topChatGroup(groupId, act, new SimpleResultListenerV2() {
                    @Override
                    public void onSuccess(String data) {
                        dao.setTopFlag(groupId,act);
                        EventBus.getDefault().post(new GroupSettingEvent(groupId,GroupSettingEvent.TYPE_TOP));
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(String msg) {
                        switchTopChat.setChecked(!isChecked,false);
                        mDialog.dismiss();
                    }
                });
            }
        });

        // GridView适合器
        mAdapter = new GroupChatSetingAdapter(this, R.layout.im_group_chat_gridview,
                getIntent().getStringExtra("from"));
        mAdapter.setGroupId(groupId);
        im_group_chat_ui_gridView.setAdapter(mAdapter);

//        GroupInfo2Bean.Data groupInfo = (GroupInfo2Bean.Data) getIntent().getSerializableExtra("groupInfo");
        ChatGroupPo groupInfo=dao.queryForId(groupId);
        noAdd = getIntent().getBooleanExtra(Key_NO_ADD, false);
        if (noAdd)
            extraItemNum = 0;
        if (groupInfo != null) {
            setGroupInfo(groupInfo);
        }
        // 执行获取任务
        executeObtainTask();
        mPicDialog=new DialogGetPhote(this,new DialogGetPhote.OnClickListener(){
            @Override
            public void btnPhotoClick(View v) {
                selectPhoto();
            }
            @Override
            public void btnCameraClick(View v) {
                takePhoto();
            }
        });
    }

    /**
     * 设置群聊名称
     *
     * @param name
     */
    private void setGroupChatName(String name) {
        this.groupName = name;
        im_group_chat_ui_name_value.setText(this.groupName);
    }

    /**
     * 设置消息提醒开关
     *
     * @param isChecked
     */
    private void setMessgeRemind(boolean isChecked) {
        this.messgeRemind = isChecked;
        im_group_chat_ui_setting_messge_remind.setChecked(this.messgeRemind);
    }

    private void setFav(boolean isChecked) {
        im_group_chat_ui_setting_fav.setChecked(isChecked);
    }

    /**
     * 设置消息提醒开关
     *
     * @param isNotify
     */
    private void setMessgeRemind(int isNotify) {
        setMessgeRemind(isNotify == 1);
    }

    /**
     * 设置“退出多人聊天”控件的可见性
     *
     * @param sessionType
     */
    private void setExitMultipleChatVisibility(int sessionType) {
        if (sessionType == SessionType.session_double) {
            setExitMultipleChatVisibility(false);
        } else {
            setExitMultipleChatVisibility(true);
        }
    }

    /**
     * 设置“清空聊天记录”控件的可见性
     *
     * @param enable
     */
    private void setClearChatRecordVisibility(boolean enable) {
        if (enable) {
            im_group_chat_ui_clear_chat_record.setVisibility(View.VISIBLE);
        } else {
            im_group_chat_ui_clear_chat_record.setVisibility(View.GONE);
        }
    }

    /**
     * 设置“退出多人聊天”控件的可见性
     */
    private void setExitMultipleChatVisibility(boolean enable) {
        if (enable) {
            im_group_chat_ui_exit_multiple_chat.setVisibility(View.VISIBLE);
        } else {
            im_group_chat_ui_exit_multiple_chat.setVisibility(View.GONE);
        }
    }

    /**
     * 名称
     *
     * @param enable
     */
    private void setGroupNameVisibility(boolean enable) {
        if (enable) {
            im_group_chat_ui_setting_name_layout.setVisibility(View.VISIBLE);
        } else {
            im_group_chat_ui_setting_name_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 更新标题
     */
    private void updateTitle(int itemCount) {
        String title = String.format(Locale.getDefault(), "聊天信息(%d人)", itemCount - extraItemNum);
        // String title = String.format(Locale.getDefault(), "聊天信息");
        im_group_chat_ui_title.setText(title);
    }

    /**
     * 更新标题
     */
    private void updateTitle(List<Item> items) {
        int userIdCount = 0;
        if (items != null) {
            userIdCount = items.size();
        }
        updateTitle(userIdCount);
    }

    /**
     * 数据对象
     *
     * @author lmc
     */
    public static class Item implements Serializable {

        public static enum Extra {
            NotKnow, ADD, // 表示是否为+号
            DELETE // 表示是否为-号
        }

        /**
         *
         */
        private static final long serialVersionUID = 4246023372580873963L;

        public String userId; // userId
        public String name; // 姓名
        public int userType; // 姓名
        public String avatarImageUri; // 头像Uri
        public boolean isMy; // 表示是否为自己，== false表示别人
        public boolean isUser; // 表示是否为用户
        public Extra extra = Extra.NotKnow; // 扩展
        public boolean isShowDelete = false; // 是否显示删除按钮
    }

    /**
     * 初始化GridItem
     *
     * @param userIds ，WEB端会将组里的全部返回来，包含自己userId
     */
    private void initGridItem(List<UserInfo> userIds) {
        ArrayList<Item> gridItem = new ArrayList<Item>();
        // 清空
        gridItem.clear();
        // 将自己加入<>
        String selfUserId = ImUtils.getLoginUserId();
//		Item myItem = new Item();
//		myItem.isMy = true;
//		myItem.isUser = true;
//		myItem.userId = selfUserId;
//		myItem.name = ImSdk.getInstance().userName;
//		myItem.avatarImageUri = ImSdk.getInstance().userAvatar;
//		myItem.extra = Item.Extra.NotKnow;
//		gridItem.add(myItem);
        // 加入其它人员
        for (GroupInfo2Bean.Data.UserInfo userId : userIds) {
            if (userId != null && userId.id != null) {
                Item item = new Item();
                item.isUser = true;
                item.userId = userId.id;
                item.name = userId.name;
                item.avatarImageUri = userId.pic;
                item.extra = Item.Extra.NotKnow;
                item.userType = userId.userType;
                if (userId.id.equalsIgnoreCase(selfUserId)) {
                    item.isMy = true;
                    gridItem.add(0, item);
                } else {
                    item.isMy = false;
                    gridItem.add(item);
                }
            }
        }
        if (!noAdd) {
            // 加入+号
            Item __item_add = new Item();
            __item_add.isMy = false;
            __item_add.isUser = false;
            __item_add.extra = Item.Extra.ADD;
            gridItem.add(__item_add);
            ChatGroupDao dao = new ChatGroupDao(this, ImUtils.getLoginUserId());
            ChatGroupPo po = dao.queryForId(groupId);
            if (po != null && po.type == 2) {//多人会话才有减号
                // 加入-号
                Item __item_delete = new Item();
                __item_delete.isMy = false;
                __item_delete.isUser = false;
                __item_delete.extra = Item.Extra.DELETE;
                gridItem.add(__item_delete);
            }
        }
        // 设置适配器
//		mAdapter.setItems(gridItem);
        mAdapter.refreshItems(gridItem);
    }

    /**
     * 过滤重复的
     *
     * @param userIds
     * @return
     */
    private List<String> filterUserId(List<String> userIds) {

        List<String> resultList = new ArrayList<String>();

        List<Item> itemList = mAdapter.getItems();

        for (String uId : userIds) {
            boolean isFind = false;
            for (Item i : itemList) {
                if (i.userId != null && i.isUser && i.isMy == false && i.userId.equalsIgnoreCase(uId)) {
                    isFind = true;
                    break;
                }
            }
            if (isFind) {
                resultList.add(uId);
            }
        }

        return resultList;
    }

    /**
     * 删除一个用户
     *
     * @param item
     */
    public void removeGridView(Item item) {
        this.deleteItem = item;
        executeUpdateTask(whatDeleteUserTask);
    }

    public void alertRemoveUser(final Item item) {
        new AlertDialog.Builder(this).setMessage("确定要删除组成员" + item.name).setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeGridView(item);
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 删除一个用户
     *
     * @param item
     */
    private void __removeGridView(Item item) {
        /**
         * 删除某个用户
         */
        mAdapter.remove(item);
        // 更新标题
        updateTitle(mAdapter.getItems());
    }

    /**
     * 打开界面
     * <p/>
     * 例子 userId=522&gid=X4BQ9NE66J
     *
     * @param context
     * @param groupId 组ID
     */
    public static void openUI(Context context, String groupId, GroupInfo2Bean.Data data, String from) {
        openUIForResult(context, groupId, data, from, false, 0);
    }

    public static void openUIForResult(Context context, String groupId, GroupInfo2Bean.Data data, String from,
                                       boolean forResult, int requestCode) {
        openUIForResult(context, groupId, data, from, forResult, requestCode, false);

    }

    public static void openUIForResult(Context context, String groupId, GroupInfo2Bean.Data data, String from,
                                       boolean forResult, int requestCode, boolean noAdd) {
        Intent i = new Intent(context, GroupChatSetingUI.class);
        i.putExtra(Key_groupId, groupId);
        i.putExtra(Key_NO_ADD, noAdd);
        i.putExtra("groupInfo", data);
        i.putExtra("from", from);
        if (forResult) {
            if (context instanceof Activity) {
                Activity act = (Activity) context;
                act.startActivityForResult(i, requestCode);
            } else {
                UIHelper.ToastMessage(context, "程序错误");
            }
        } else {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    @Nullable
    @OnClick(R.id.im_group_chat_ui_back)
    void onClick_im_group_chat_ui_back() {
        finish();
    }

    @Nullable
    @OnClick(R.id.im_group_chat_ui_setting_name_layout)
    void onClick_im_group_chat_ui_setting_name_layout() {
        String text = im_group_chat_ui_name_value.getText().toString();
        SmallEditViewUI.openUI(mThis,16, "编辑群聊名称", text, InputType.TYPE_CLASS_TEXT, REQUEST_CODE_EDIT_TEXT);
    }

    /**
     * 得到用户userIds数组
     *
     * @return
     */
    private List<String> getUserIds() {

        List<String> userIds = new ArrayList<String>();

        List<Item> _item = mAdapter.getItems();

        // 筛选
        for (Item i : _item) {
            if (i != null && i.isUser == true && i.isMy == false) {
                userIds.add(i.userId);
            }
        }

        return userIds;
    }

    /**
     * 格式化，用|分开
     *
     * @param userIds
     * @return
     */
    private String formatUserIds(List<String> userIds) {
        if (userIds == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < userIds.size(); n++) {
            if (n > 0) {
                sb.append('|');
            }
            sb.append(userIds.get(n));
        }

        return sb.toString();
    }

    @Override
    protected void onResume() {
        Log.w(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w(TAG, "onActivityResult():arg0:" + requestCode + ",arg1:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_UPDATE_GROUP) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == REQUEST_CODE_EDIT_TEXT) {
            groupName = data.getStringExtra(SmallEditViewUI.key_text);
            executeUpdateTask(whatChangeGroupNameTask);
        }else if(requestCode==REQUEST_CODE_PICK_GALLERY){
            String[] all_path = data.getStringArrayExtra(GalleryAction.INTENT_ALL_PATH);
            if (all_path == null||all_path.length==0) return;
            Uri o = Uri.fromFile(new File(all_path[0]));
            mNewPhotoUri = CameraUtil.getOutputMediaFileUri(mThis,CameraUtil.MEDIA_TYPE_IMAGE);
            CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PIC, 1, 1, 300, 300);
//            uploadAvatar(all_path[0]);
        } else if (requestCode == REQUEST_CODE_CROP_PIC) { // 裁减图片
            if (mNewPhotoUri != null) {
                uploadAvatar(mNewPhotoUri.getPath());
            } else {
                ToastUtil.showToast(mThis, R.string.c_crop_failed);
            }
        }else if(requestCode==REQUEST_CODE_CAMERA){
            if (mNewPhotoUri != null) {
                Uri o = mNewPhotoUri;
                mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
                CameraUtil.cropImage(this, o, mNewPhotoUri, REQUEST_CODE_CROP_PIC, 1, 1, 300, 300);
            } else {
                ToastUtils.showToast( mThis,R.string.c_photo_album_failed);
            }
        }
    }

    /**
     * 执行获取任务
     */
    private void executeObtainTask() {
        if (null!=mDialog){
            mDialog.show();
        }
        SessionGroup tool=new SessionGroup(mThis);
        tool.setCallbackNew(new SessionGroup.SessionGroupCallbackNew() {
            @Override
            public void onGroupInfo(ChatGroupPo po, int what) {
                setGroupInfo(po);
                // set true
                b_executeObtainTask_successed = true;
                mDialog.dismiss();
            }

            @Override
            public void onGroupInfoFailed(String msg) {
                ToastUtil.showToast(mThis, msg);
                mDialog.dismiss();
            }
        });
        tool.getGroupInfoNew(groupId);

//        RequestQueue queue = VolleyUtil.getQueue(this);
//        // queue.cancelAll(this);
//        String url = PollingURLs.getGroupInfo();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("access_token", ImSdk.getInstance().accessToken);
//        map.put("gid", groupId);
//        map.put("userId", ImUtils.getLoginUserId());
//        StringRequest request = new ImCommonRequest(url, map, new Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Logger.d(TAG, "executeObtainTask():onResponse():result:" + response);
//                if (null!=mDialog) {
//                    mDialog.dismiss();
//                }
//                GroupInfo2Bean bean = GJson.parseObject(response, GroupInfo2Bean.class);
//                if (bean != null) {
//                    if (bean.resultCode == HttpErrorCode.successed) {
//                        GroupInfo2Bean.Data data = bean.data;
//                        if (data != null) {
//                            setGroupInfo(data);
//                        }
//                        // set true
//                        b_executeObtainTask_successed = true;
//
//                    } else {
//                        // 提示错误信息
//                        ToastUtil.showToast(mThis, bean.detailMsg);
//                    }
//                }
//
//            }
//
//        }, new ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.w(TAG, "onErrorResponse()");
//                if (null!=mDialog) {
//                    mDialog.dismiss();
//                }
//                ToastUtil.showErrorNet(mThis);
//            }
//
//        });
//        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
//        queue.add(request);
    }

//    private void setGroupInfo(GroupInfo2Bean.Data data) {
//        UserInfo[] doctorList = data.userList;
//        // 健康关怀计划要显示单独显示患者信息
//        // 双人或者多人
//        mAdapter.setSessionType(data.type);
//        // 刷新数据
//        initGridItem(doctorList);
//        // 设置标题
//        updateTitle(mAdapter.getItems());
//        // 设置群聊名称
//        setGroupChatName(data.gname);
//        if (data.type == SessionType.session_multi) {
//            // 组名称
//            setGroupNameVisibility(true);
//        }
//        // 设置消息提醒开关
//        setMessgeRemind(data.notify);
//        boolean isFav = ChatGroupPo.getStateOnPos(data.status, 1) == 1;
//        setFav(isFav);
//        // 设置“退出多人聊天”控件的可见性
//        setExitMultipleChatVisibility(data.type);
//    }
    private void setGroupInfo(ChatGroupPo data) {
        ImageLoader.getInstance().displayImage(data.gpic,iv_avatar);
        List<UserInfo> doctorList = JSON.parseArray(data.groupUsers,UserInfo.class);
        // 健康关怀计划要显示单独显示患者信息
        // 双人或者多人
        mAdapter.setSessionType(data.type);
        // 刷新数据
        initGridItem(doctorList);
        // 设置标题
        updateTitle(mAdapter.getItems());
        // 设置群聊名称
        setGroupChatName(data.name);
        if (data.type == SessionType.session_multi) {
            // 组名称
            setGroupNameVisibility(true);
        }
        // 设置消息提醒开关
        boolean notify = ChatGroupPo.getStateOnPos(data.status, 0) == 1;
        setMessgeRemind(notify);
        boolean isFav = ChatGroupPo.getStateOnPos(data.status, 1) == 1;
        setFav(isFav);
        switchTopChat.setChecked(data.top==1,false);
        // 设置“退出多人聊天”控件的可见性
        if(data.type==SessionType.session_multi){
            layout_avatar.setVisibility(View.VISIBLE);
        }
        setExitMultipleChatVisibility(data.type);
    }

    private void setPatientInfo(UserInfo patient) {
        if (patient == null) {
            return;
        }
        mName.setText(patient.name);
        ImageLoader.getInstance().displayImage(patient.pic, mAvatar);

        if (!TextUtils.isEmpty(patient.id)) {
            getUserDetail(patient.id);
        }
    }

    /**
     * 从UserInfo[]中找到患者并将其返回
     *
     * @param userList
     * @return
     */
    private UserInfo getPatient(UserInfo[] userList) {
        if (userList == null || userList.length == 0) {
            return null;
        }
        for (UserInfo userInfo : userList) {
            if (userInfo != null && userInfo.userType == UserType.Patient) {
                return userInfo;
            }
        }
        return null;

    }

    /**
     * 移除患者
     *
     * @param userList
     * @param patient
     * @return
     */
    private UserInfo[] removePatient(UserInfo[] userList, UserInfo patient) {
        if (userList == null || userList.length == 0) {
            return userList;
        }
        List<UserInfo> list = Arrays.asList(userList);
        List<UserInfo> newList = new ArrayList<UserInfo>(list);
        newList.remove(patient);

        if (newList.size() > 0) {
            UserInfo[] doctorList = new UserInfo[newList.size()];
            newList.toArray(doctorList);
            return doctorList;
        }

        return null;
    }

    /**
     * 执行更新任务
     */
    private void executeUpdateTask(final int execute_what) {
        mDialog.show();
        RequestQueue queue = VolleyUtil.getQueue(this);
        String url = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("gid", groupId);
        map.put("fromUserId", ImUtils.getLoginUserId()); // fromUserId等于自己
        // if (execute_what == whatClearChatRecordTask) {
        // map.put("act", "6");
        if (execute_what == whatExitMultipleChatTask) {
//			map.put("act", "2");
            url = PollingURLs.delGroup();
            map.put("toUserId", ImUtils.getLoginUserId()); // 退出=fromUserId
            // }else if (execute_what == executeAddUserTask) {
            // map.put("act", "1");
            // map.put("toUserId", formatUserIds( getUserIds() ));
        } else if (execute_what == whatDeleteUserTask) {
//			map.put("act", "2");
            url = PollingURLs.delGroupUser();
            if (deleteItem != null) {
                map.put("toUserId", deleteItem.userId); // 这里只有一个toUserId
            }
        } else if (execute_what == whatMessgeRemindYesTask) {
            map.put("act", "8");
            url = PollingURLs.togglePush();
        } else if (execute_what == whatMessgeRemindNoTask) {
            map.put("act", "7");
            url = PollingURLs.togglePush();
        } else if (execute_what == whatChangeGroupNameTask) {
//			map.put("act", "3");
            url = PollingURLs.updateGroupName();
            map.put("name", groupName);
        } else if (execute_what == whatFavYesTask) {
            url = PollingURLs.favGroup();
            map.put("act", "1");
        } else if (execute_what == whatFavNoTask) {
            url = PollingURLs.favGroup();
            map.put("act", "0");
        }

        StringRequest request = new ImCommonRequest(url, map, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.w(TAG, "executeUpdateTask():onResponse():result:" + response);
                if (null!=mDialog) {
                    mDialog.dismiss();
                }
                UpdateGroup2Bean bean = GJson.parseObject(response, UpdateGroup2Bean.class);
                if (bean != null) {
                    if (bean.resultCode == HttpErrorCode.successed) {
                        UpdateGroup2Bean.data data = bean.data;
                        // if (execute_what == whatClearChatRecordTask) {
                        // map.put("act", "6");
                        // ToastUtil.showToast(context, "清空聊天记录成功");
                        if (execute_what == whatExitMultipleChatTask) {
                            /**
                             * 退出多人聊天
                             */
                            exitMultipleChat();
                        } else if (execute_what == whatDeleteUserTask) {
                            // 删除一个用户
                            __removeGridView(deleteItem);
                            ToastUtil.showToast(GroupChatSetingUI.this, "删除用户成功");
                        } else if (execute_what == whatMessgeRemindYesTask) {
                            setMessgeRemind(true);
                        } else if (execute_what == whatMessgeRemindNoTask) {
                            setMessgeRemind(false);
                        } else if (execute_what == whatChangeGroupNameTask) {
                            setGroupChatName(groupName);
                            // 发送消息修改聊天界面的标题
                            GroupSettingEvent event = new GroupSettingEvent(groupId, GroupSettingEvent.TYPE_NAME);
                            event.name = groupName;
                            EventBus.getDefault().post(event);
                        } else if (execute_what == whatFavYesTask || execute_what == whatFavNoTask) {
                            dao.updateGroupStatus(groupId, data.status);
                            GroupSettingEvent event = new GroupSettingEvent(groupId, GroupSettingEvent.TYPE_STATUS);
                            event.status = data.status;
                            EventBus.getDefault().post(event);
                        }
                    } else {
                        // 提示错误信息
                        ToastUtil.showToast(GroupChatSetingUI.this, bean.detailMsg);
                    }
                }

            }

        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (execute_what == whatMessgeRemindYesTask) {
                    setMessgeRemind(false);
                } else if (execute_what == whatMessgeRemindNoTask) {
                    setMessgeRemind(true);
                } else if (execute_what == whatFavNoTask) {
                    setFav(true);
                } else if (execute_what == whatFavYesTask) {
                    setFav(false);
                }
                if (null!=mDialog) {
                    mDialog.dismiss();
                }

                ToastUtil.showErrorNet(GroupChatSetingUI.this);
            }

        });

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        queue.add(request);
    }

    /**
     * 打开清空聊天记录对话框
     */
    private void showClearChatRecordDialog() {
        new AlertDialog.Builder(this).setMessage("是否清空聊天记录?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // 清空本地聊天记录
                        ImUtils.clearMessage(GroupChatSetingUI.this, groupId);
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }

        }).show();
    }

    @Nullable
    @OnClick(R.id.im_group_chat_ui_clear_chat_record)
    void onClick_im_group_chat_ui_clear_chat_record() {
        // 打开清空聊天记录对话框
        showClearChatRecordDialog();
    }

    /**
     * 打开退出多人聊天对话框
     */
    private void showExitMultipleChatDialog() {
        new AlertDialog.Builder(this).setMessage("是否退出多人聊天?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        executeUpdateTask(whatExitMultipleChatTask);
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }

        }).show();
    }

    /**
     * 退出多人聊天
     */
    private void exitMultipleChat() {
        // 从会话列表删除此会话
        // SessionMessageDBDao.deleteGroupId(groupId);
        dao.deleteById(groupId);
        // 发出消息通知控件刷新视图
//		ObserverManager.getInstance().notifyNewMsg(null, true);
        EventBus.getDefault().post(new NewMsgEvent());
        // 提示
        ToastUtil.showToast(this, "退出群聊成功");
        GroupSettingEvent event = new GroupSettingEvent(groupId, GroupSettingEvent.TYPE_EXIT);
        EventBus.getDefault().post(event);
        // 关闭聊天界面
        // TODO: 2016/3/2
//		AppManager.getAppManager().finishActivity(DoctorGroupChatActivity.class);
        // 关闭自己
        finish();
    }

    @Nullable
    @OnClick(R.id.im_group_chat_ui_exit_multiple_chat)
    void onClick_im_group_chat_ui_exit_multiple_chat() {
        // 打开退出多人聊天对话框
        showExitMultipleChatDialog();
    }

    /**
     * 根据userId获取用户详情
     *
     * @param userId
     */
    private void getUserDetail(final String userId) {
        final String reqTag = "getUserDetail";
        RequestQueue queue = VolleyUtil.getQueue(this);
        queue.cancelAll(reqTag);
        StringRequest request = new StringRequest(Method.POST, PollingURLs.getUserDetail(), new Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleResponse(response);
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", ImSdk.getInstance().accessToken);
                params.put("userId", userId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    private void handleResponse(String response) {
        ObjectResult<User> result = JSON.parseObject(response, new TypeReference<ObjectResult<User>>() {
        });
        if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null) {
            return;
        }

        User user = result.getData();
        if (user.sex == 1) {
            mSex.setText("男");
        } else if (user.sex == 2) {
            mSex.setText("女");
        } else {
            mSex.setVisibility(View.INVISIBLE);
        }

        mAge.setText(user.age + "");

    }

    public void onEventMainThread(AddGroupUserEvent event) {
        if (event.groupId.equals(groupId))
            finish();
    }

    @OnClick(R.id.layout_avatar)
    void onClickLayoutAvatar() {
//        CustomGalleryActivity.openUi(mThis,false, REQUEST_CODE_AVATAR);
        mPicDialog.show();
    }

    private void selectPhoto() {
        CustomGalleryActivity.openUi(this, false, REQUEST_CODE_PICK_GALLERY);
    }

    private void takePhoto() {
        mNewPhotoUri = CameraUtil.getOutputMediaFileUri(this, CameraUtil.MEDIA_TYPE_IMAGE);
        CameraUtil.captureImage(this, mNewPhotoUri, REQUEST_CODE_CAMERA);
    }

    private void uploadAvatar(String path){
        mDialog.show();
        UploadEngine7Niu.UploadObserver7NiuV2 callBack=new UploadEngine7Niu.UploadObserver7NiuV2() {
            @Override
            public void onUploadSuccess(String key, String url) {
                modifyAvatar(url);
            }

            @Override
            public void onUploadFailure(String msg) {
                mDialog.dismiss();
                if(!CommonUtils.checkNetworkEnable(mThis)){
                    ToastUtil.showToast(mThis,"无网络可用，请检查连接或设置");
                }else
                    ToastUtil.showToast(mThis,"头像上传失败");
            }
        };
        UploadEngine7Niu.uploadFileCommon(path,callBack, QiNiuUtils.BUCKET_GROUP_AVATAR,null);
    }

    private void modifyAvatar(final String picUrl){
        SimpleResultListenerV2 listener=new SimpleResultListenerV2(){
            @Override
            public void onSuccess(String dataStr) {
                ToastUtil.showToast(mThis,"头像设置成功");
                ImageLoader.getInstance().displayImage(picUrl,iv_avatar);
                GroupSettingEvent event=new GroupSettingEvent(groupId,GroupSettingEvent.TYPE_AVATAR);
                event.url=picUrl;
                EventBus.getDefault().post(event);
                mDialog.dismiss();
            }

            @Override
            public void onError(String msg) {
                mDialog.dismiss();
                ToastUtil.showToast(mThis,msg);
            }
        };
        String url=PollingURLs.updateGroupPic();
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("fromUserId",ImUtils.getLoginUserId());
        reqMap.put("gid",groupId);
        reqMap.put("name",picUrl);
        ImCommonRequest request=new ImCommonRequest(url,reqMap, ImRequestManager.makeSucListener(listener),ImRequestManager.makeErrListener(listener));
        VolleyUtil.getQueue(mThis).add(request);
        mDialog.show();
    }
}
