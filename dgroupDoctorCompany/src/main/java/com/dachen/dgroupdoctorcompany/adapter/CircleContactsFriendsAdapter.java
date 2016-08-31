package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/5/16.
 */

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.FriendsContactsActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.ContactsInfo;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CircleContactsFriendsAdapter extends BaseAdapter<ContactsInfo>implements SectionIndexer {

    private boolean isCreateGroup;
    private HashSet<String> hashset;
    private FriendsContactsActivity activity;
    private String from;
    Activity mContext;

    public CircleContactsFriendsAdapter(Activity mContext, FriendsContactsActivity activity, String from) {
        super(mContext);
        this.activity = activity;
        this.from = from;
        this.mContext = mContext;
    }


    public class ViewHolder {
        public TextView tvTitle;
        public TextView tvLetter;
        public TextView tv_desc;
        public ImageView img;
        public RadioButton btn_radio;
        public Button add_btn;
        public TextView verify_txt;
        public TextView accept_txt;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.circle_contacts_friends_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            viewHolder.img = (ImageView) view.findViewById(R.id.img);
            viewHolder.btn_radio = (RadioButton) view.findViewById(R.id.btn_radio);
            viewHolder.add_btn = (Button) view.findViewById(R.id.add_btn);
            viewHolder.verify_txt = (TextView) view.findViewById(R.id.verify_txt);
            viewHolder.accept_txt = (TextView) view.findViewById(R.id.accept_txt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        final ContactsInfo mContent = dataSet.get(position);
        if (position == 0) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getLetter());
        } else {
            String lastCatalog = dataSet.get(position - 1).getLetter();
            if (mContent.getLetter().equals(lastCatalog)) {
                viewHolder.tvLetter.setVisibility(View.GONE);
            } else {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getLetter());
            }
        }
        viewHolder.tvLetter.setVisibility(View.GONE);
        viewHolder.tvTitle.setText(mContent.getName());
        viewHolder.tv_desc.setText(mContent.getPhone());
        viewHolder.img.setImageResource(R.drawable.ic_default_head);
        viewHolder.img.setVisibility(View.GONE);
        if (mContent.isOnlyFriend()) {
            viewHolder.add_btn.setVisibility(View.VISIBLE);
            viewHolder.verify_txt.setVisibility(View.GONE);
           // viewHolder.accept_txt.setVisibility(View.VISIBLE);
           viewHolder.add_btn.setText("已添加");
            viewHolder.add_btn.setBackgroundResource(R.drawable.btn_bulk_gray);
            viewHolder.add_btn.setTextColor(mContext.getResources().getColor(R.color.color_grayline1dp));
            viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            viewHolder.add_btn.setVisibility(View.VISIBLE);
            viewHolder.verify_txt.setVisibility(View.GONE);
            viewHolder.accept_txt.setVisibility(View.GONE);
            viewHolder.add_btn.setBackgroundResource(R.drawable.bulk_3cbaff);
            viewHolder.add_btn.setText("添加");
            viewHolder.add_btn.setTextColor(mContext.getResources().getColor(R.color.color_3cbaff));
            viewHolder.add_btn.setOnClickListener(new isOpenedClick(viewHolder, mContent, position));
        }


        return view;
    }

    class isOpenedClick implements View.OnClickListener {

        private int position;
        private ViewHolder holder;
        private ContactsInfo contactsInfo;

        public isOpenedClick(ViewHolder holder, ContactsInfo contactsInfo, int pos) { // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
            this.holder = holder;
            this.contactsInfo = contactsInfo;
        }

        @Override
        public void onClick(View v) {

                addPhoneFriend(contactsInfo, holder);
        }
    }


    /**
     *


     添加组织用户的来源（0-web端 1-app端部门管理员邀请）


     * 用户好友 - 加手机联系人为好友
     */
    public void addPhoneFriend(final ContactsInfo contactsInfo, final ViewHolder viewHolder) {
        if (contactsInfo == null) {
            return;
        }
        String finalPhone=contactsInfo.getPhone();
        if(!TextUtils.isEmpty(finalPhone)&&finalPhone.startsWith("+86")){
            finalPhone=finalPhone.replace("+86","").replace(" ","");
        }

        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(mContext).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(mContext, "enterpriseId", ""));
        maps.put("orgId",""+ AddressList.deptId);
        maps.put("telephone",finalPhone);
        maps.put("addSource","1");
        String userName = "";
        if (!TextUtils.isEmpty(contactsInfo.getName())){
            userName = contactsInfo.getName();
        }
        maps.put("name",userName);
        new HttpManager().post(mContext, Constants.DRUG+"companyUser/addMajorUser", CompanyDepment.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode==1&&response.resultMsg.equals("success")){
                            ToastUtil.showToast(mContext, "添加成功");
                            contactsInfo.setOnlyFriend(true);
                            viewHolder.add_btn.setText("已添加");
                            viewHolder.add_btn.setBackgroundResource(R.drawable.btn_bulk_gray);
                            viewHolder.add_btn.setTextColor(mContext.getResources().getColor(R.color.color_grayline1dp));
                            viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                            GetAllDoctor.getInstance().getPeople(new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if (mContext instanceof FriendsContactsActivity){
                                        FriendsContactsActivity activity = (FriendsContactsActivity) mContext;
                                    }

                                }
                            });

                        }else {
                            if (!TextUtils.isEmpty(response.resultMsg)){

                                ToastUtil.showToast(mContext,response.resultMsg);
                            }else {
                                ToastUtil.showToast(mContext,"添加失败");
                            }

                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }

    public void getResponse(String response, final ViewHolder viewHolder, final String phone) {

    }


    private void sendInviteMsg(final String phone) {
        if (phone == null) {
            return;
        }

    }

    public Object[] getSections() {
        return null;
    }

    public int getSectionForPosition(int position) {
        return 0;
    }

    public int getPositionForSection(int section) {
        ContactsInfo mContent;
        String l;
        if (section == '!') {
            return 0;
        } else {
            for (int i = 0; i < getCount(); i++) {
                mContent = (ContactsInfo) dataSet.get(i);
                if (mContent != null) {
                    l = mContent.getLetter();
                    if (l != null) {
                        char firstChar = l.toUpperCase().charAt(0);
                        if (firstChar == section) {
                            return i + 1;
                        }
                    }
                }

            }
        }
        return -1;
    }


}