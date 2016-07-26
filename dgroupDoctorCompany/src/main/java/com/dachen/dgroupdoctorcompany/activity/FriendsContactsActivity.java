package com.dachen.dgroupdoctorcompany.activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dachen.common.widget.ClearEditText;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CircleContactsFriendsAdapter;
import com.dachen.dgroupdoctorcompany.adapter.GB2Alpha;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.ContactsInfo;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * [手机通讯录联系人加好友页面]
 *
 * @author zeng
 * @version 1.0
 * @date 2015-8-29
 *
 **/
public class FriendsContactsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, TextWatcher {

    private ListView refreshlistview;
    private CircleContactsFriendsAdapter adapter;
    private SideBar sideBar;
    private ClearEditText edit_search;
    private List<ContactsInfo> contactslist;
    private List<ContactsInfo> contactsSortlist = new ArrayList<ContactsInfo>(); //筛选后的列表
    // 手机联系人信息
    Map<String, List<String>> ListName = new HashMap();
    List<ContactsInfo> localList;
    List<ContactsInfo> SIMList;
    ContactsInfo contactsInfo;
    private String from;
    Context context;
    List<CompanyContactListEntity> allFriends;
    public String deptid;
    private MyThread mMyThread;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    if (msg.arg1 == 1) {
                        adapter.removeAll();
                        adapter.addData(contactslist);
                        adapter.notifyDataSetChanged();
                        closeLoadingDialog();
                    } else {
                        closeLoadingDialog();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_contacts_friends);
        context = this;
        localList = new ArrayList<ContactsInfo>();

        //        allFriends = dao.queryAll(SharedPreferenceUtil.getString(this, "id", ""));
        //如果是健康关怀分享
        from = getIntent().getStringExtra("from");
        deptid = AddressList.deptId;
        initViews();
        enableBack();
        showLoadingDialog();
        mMyThread = new MyThread();
        mMyThread.start();
    }

    /**
     * 页面初始化
     */
    void initViews() {
      /*  TextView tv_position = (TextView) LayoutInflater.from(this).inflate(R.layout.listview_position, null);
        tv_position.setVisibility(View.INVISIBLE);
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(tv_position, lp);
        sideBar = getViewById(R.id.sideBar);
        sideBar.setTextView(tv_position);*/
        findViewById(R.id.btn_addbyphonenum).setOnClickListener(this);
        setTitle("添加成员");
        edit_search = getViewById(R.id.edit_search);
        edit_search.addTextChangedListener(this);
        adapter = new CircleContactsFriendsAdapter(this, FriendsContactsActivity.this, from);
        refreshlistview = getViewById(R.id.refreshlistview);
        refreshlistview.setOnItemClickListener(this);
        refreshlistview.setEmptyView(findViewById(R.id.layout_contactnotpeople));
        adapter.removeAll();
        adapter.addData(contactslist);
        refreshlistview.setAdapter(adapter);
        //sideBar.setListView(refreshlistview.getRefreshableView());


    }

    public void refreshView(){
        showLoadingDialog();
        mMyThread.run();
    }



    /**
     * 健康关怀分享发送短信
     *
     * @param telephone
     */
    public void submitCareOrder(String telephone) {

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        String filter = edit_search.getText().toString().trim();
        contactsSortlist.clear();
        if (contactslist != null && contactslist.size() > 0) {
            for (int i = 0; i < contactslist.size(); i++) {
                ContactsInfo contactsInfo = contactslist.get(i);
                // 获取筛选的数据
                if (!TextUtils.isEmpty(filter) && (filter.equals(contactsInfo.getName()) || contactsInfo.getName().contains(filter))) {
                    contactsSortlist.add(contactsInfo);
                }
            }
        }
        if (!TextUtils.isEmpty(filter)) {
            adapter.removeAll();
            adapter.addData(contactsSortlist);
            adapter.notifyDataSetChanged();
            sideBar.setVisibility(View.GONE);
        } else {
            adapter.removeAll();
            adapter.addData(contactslist);
            adapter.notifyDataSetChanged();
            sideBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onleftClick(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.layout_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;*/
            case R.id.btn_addbyphonenum:
                startActivityForResult( (new Intent(this, AddFriendByPhone.class)),200);
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == Activity.RESULT_OK) {

        }
    }

    /**
     * 读取手机通讯录联系人
     */
    public void GetContactsInfo() {

        localList.clear();
        getLocalContactsInfos(); //也可以读取sim卡联系人
        //		getSIMContactsInfos(); //不需要这个方法也可以读取sim卡联系人

        contactslist = localList;
        Collections.sort(contactslist, new PinyinComparator());
    }

    // ----------------得到本地联系人信息-------------------------------------
    public List<ContactsInfo> getLocalContactsInfos() {
        ContentResolver cr = context.getContentResolver();
        String str[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
        Cursor cur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, str, null,
                null, null);

        if (cur != null) {
            while (cur.moveToNext()) {
                contactsInfo = new ContactsInfo();
                contactsInfo.setPhone((cur.getString(cur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));// 得到手机号码
                contactsInfo.setPhone(contactsInfo.getPhone().replace(" ", ""));
                contactsInfo.setName(cur.getString(cur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                GB2Alpha gb2alpha = new GB2Alpha();
                contactsInfo.setLetter(gb2alpha
                        .String2Alpha(contactsInfo.getName()));

                if (ListName.get(contactsInfo.getName()) == null) //判断一个名字存在多于3个号码，则显示最多3个号码
                {
                    List<String> phones = new ArrayList<String>();
                    phones.add(contactsInfo.getPhone());
                    ListName.put(contactsInfo.getName(), phones);
                } else {
                    if (ListName.get(contactsInfo.getName()).size() >= 3) {
                        continue;
                    } else {
                        List<String> phones = ListName.get(contactsInfo.getName());
                        phones.add(contactsInfo.getPhone());
                        ListName.put(contactsInfo.getName(), phones);
                    }
                }
                localList.add(contactsInfo);

            }
        }
        cur.close();
        return localList;
    }


    /**
     * [A brief description]
     *
     * @author huxinwu
     * @version 1.0
     * @date 2015-8-14
     **/
    class PinyinComparator implements Comparator<ContactsInfo> {
        public int compare(ContactsInfo o1, ContactsInfo o2) {
            if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
                return -1;
            } else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
                return 1;
            } else {
                return o1.getLetter().compareTo(o2.getLetter());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class MyThread extends Thread{

        private List<String> phoneNumberList = new ArrayList<>();

        @Override
        public void run() {
            super.run();
            CompanyContactDao dao = new CompanyContactDao(FriendsContactsActivity.this);
            GetContactsInfo();
            if (contactslist != null && contactslist.size() > 0) {
                phoneNumberList.clear();
                for (int i = 0; i < contactslist.size(); i++) {
                    ContactsInfo info = contactslist.get(i);
                    String phone = info.getPhone().trim();
                    if (!TextUtils.isEmpty(phone)) {
                        phone = phone.replace(" ", "").replace("-", "").replace("+86", "");
                        info.setPhone(phone);
                    }
                    phoneNumberList.add(info.getPhone());
                }
            }
            List<CompanyContactListEntity> list = dao.queryByTelephones(phoneNumberList);
            for (CompanyContactListEntity entity : list) {
                for (ContactsInfo info : contactslist) {
                    if (!TextUtils.isEmpty(entity.telephone) && !TextUtils.isEmpty(info.getPhone())) {
                        if ((entity.telephone).equals(info.getPhone())) {
                            info.setOnlyFriend(true);
                        }
                    }
                }
            }
            Message message = new Message();
            message.what = 1001;
            message.arg1 = 1;
            mHandler.sendMessage(message);
        }
    }
}