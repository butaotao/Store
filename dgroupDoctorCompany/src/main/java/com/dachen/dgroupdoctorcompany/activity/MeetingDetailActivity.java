package com.dachen.dgroupdoctorcompany.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cms.mylive.MyLiveActivity;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.StringUtils;
import com.dachen.common.utils.TimeUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Meeting;
import com.dachen.dgroupdoctorcompany.utils.HtmlTextViewEdit;
import com.dachen.dgroupdoctorcompany.views.InputDialog;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

public class MeetingDetailActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = MeetingDetailActivity.class.getSimpleName();
    private TextView mMeetingTitle;
    private TextView mMeetingDate;
    private TextView mMeetingTime;
    private TextView mMeetingCount;
    private TextView mMeetingExpenses;
    private TextView mMeetingExpensesReal;
    private TextView mOrganizationPassword;
    private TextView mGuestPassword;
    private TextView mWatchPassword;
    private TextView mEdit;
    private Button mEnter;
    private LinearLayout mPasswordLayout;
    private TextView mLink;
    private TextView mCopyLink;
    private TextView mCopyWatchPassword;
    private TextView mCopyOrganizationPassword;
    private TextView mCopyGuestPassword;
    private ImageView iv_watch;
    private ImageView iv_organization;
    private ImageView iv_guest;
    private ImageView iv_link;
    private TextView mTips;
    private Meeting mMeeting;
    boolean isOpenWatch = false;
    boolean isOpenOrganization = false;
    boolean isOpenGuest = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        initVariables();
        initViews();
        fillView();
    }

    private void fillView() {
        setTitle(mMeeting.subject);
        mMeetingTitle.setText(mMeeting.subject);
        mMeetingDate.setText(TimeUtils.s_long_2_str(mMeeting.startDate));
        mMeetingTime.setText(TimeUtils.chat_long_2_str(mMeeting.startTime) + "-" + TimeUtils.chat_long_2_str(mMeeting.endTime));
        mMeetingCount.setText(mMeeting.attendeesCount + "人");
        mMeetingExpensesReal.setText("¥" + StringUtils.convertPrice(mMeeting.price) + "元");
        mMeetingExpenses.setText("¥" + mMeeting.attendeesCount * CreateAndEditMeetingActivity.EACH_PRICE + "元");
        mOrganizationPassword.setText(mMeeting.organizerToken);
        mGuestPassword.setText(mMeeting.panelistToken);
        mWatchPassword.setText(mMeeting.attendeeToken);
        mLink.setText(mMeeting.panelistJoinUrl);
    }

    private void initVariables() {
        mMeeting = (Meeting) getIntent().getSerializableExtra("meeting");
        if (mMeeting == null || mMeeting.id == null) {
            ToastUtil.showToast(mThis, "无效会议");
            finish();
        }
    }

    public void initViews() {
        mEdit = getViewById(R.id.edit);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMeeting();
            }
        });
        mMeetingTitle = getViewById(R.id.meeting_title);
        mMeetingDate = getViewById(R.id.meeting_date);
        mMeetingTime = getViewById(R.id.meeting_time);
        mMeetingCount = getViewById(R.id.meeting_count);
        mMeetingExpenses = getViewById(R.id.meeting_expenses);
        //findViewById(R.id.rl_open_watch).setOnClickListener(this);//iv_watch
        findViewById(R.id.rl_open_organization).setOnClickListener(this);//iv_organization
        findViewById(R.id.rl_open_guest).setOnClickListener(this);//iv_guest
        findViewById(R.id.rl_open_link).setOnClickListener(this);//iv_link
        iv_watch = (ImageView) findViewById(R.id.iv_watch);

        iv_organization = (ImageView) findViewById(R.id.iv_organization);
        iv_organization.setBackgroundResource(R.drawable.eyeopen);
        iv_guest = (ImageView) findViewById(R.id.iv_guest);
        iv_link = (ImageView) findViewById(R.id.iv_link);
        //设置删除线
        mMeetingExpenses.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mMeetingExpensesReal = getViewById(R.id.meeting_expenses_real);
        mOrganizationPassword = getViewById(R.id.organization_password);
        mGuestPassword = getViewById(R.id.guest_password);
        iv_guest.setBackgroundResource(R.drawable.eyeopen);
        mWatchPassword = getViewById(R.id.watch_password);
        mEnter = getViewById(R.id.enter);
        mLink = getViewById(R.id.link);
        mWatchPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        iv_watch.setBackgroundResource(R.drawable.eyeopen);

        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMeeting();
            }
        });
        findViewById(R.id.rl_open_watch).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isOpenWatch){
                        iv_watch.setBackgroundResource(R.drawable.eyeopen);
                        mWatchPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isOpenWatch = false;
                    }else {
                        iv_watch.setBackgroundResource(R.drawable.eyeclose);
                        mWatchPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isOpenWatch = true;
                    }

                }
                /*if (event.getAction() == MotionEvent.ACTION_UP) {
                    iv_watch.setBackgroundResource(R.drawable.eyeclose);
                    mWatchPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }*/
                return true;
            }
        });
        mOrganizationPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        findViewById(R.id.rl_open_organization).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isOpenOrganization){
                        iv_organization.setBackgroundResource(R.drawable.eyeopen);
                        mOrganizationPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isOpenOrganization = false;
                    }else {
                        iv_organization.setBackgroundResource(R.drawable.eyeclose);

                        mOrganizationPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isOpenOrganization = true;
                    }


                } /* if (event.getAction() == MotionEvent.ACTION_UP) {
                    iv_organization.setBackgroundResource(R.drawable.eyeclose);
                    mOrganizationPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }*/
                return true;
            }
        });
        mGuestPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        findViewById(R.id.rl_open_guest).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(isOpenGuest){
                        iv_guest.setBackgroundResource(R.drawable.eyeopen);
                        isOpenGuest = false;
                        mGuestPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }else {
                        iv_guest.setBackgroundResource(R.drawable.eyeclose);
                        mGuestPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isOpenGuest = true;
                    }


                }  /*if (event.getAction() == MotionEvent.ACTION_UP) {
                    iv_guest.setBackgroundResource(R.drawable.eyeclose);
                    mGuestPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }*/
                return true;
            }
        });
   /*     findViewById(R.id.rl_open_link).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iv_link.setBackgroundResource(R.drawable.eyeopen);
                    mLink.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }  if (event.getAction() == MotionEvent.ACTION_UP) {
                    iv_link.setBackgroundResource(R.drawable.eyeclose);
                    mLink.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                return true;
            }
        });*/

        mCopyLink = getViewById(R.id.copy_link);
        mCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spanned spandes =HtmlTextViewEdit.getMettingVistLink(mLink.getText().toString());
                copyToClipboard(spandes);
            }
        });
        mCopyWatchPassword = getViewById(R.id.copy_watch_password);
        mCopyWatchPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String designText = "【"+mMeeting.subject+"】会议定于"+TimeUtils.s_long_2_str(mMeeting.startDate)+" "
                        +TimeUtils.chat_long_2_str(mMeeting.startTime) + "-" + TimeUtils.chat_long_2_str(mMeeting.endTime)
                        +"进行,请大家按时参会。\n参会密码："+mWatchPassword.getText();
                copyToClipboard(designText);
            }
        });
        mCopyOrganizationPassword = getViewById(R.id.copy_organization_password);
        mCopyOrganizationPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(mOrganizationPassword.getText());
            }
        });
        mCopyGuestPassword = getViewById(R.id.copy_guest_password);
        mCopyGuestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spanned spandes = HtmlTextViewEdit.getMettingVistPassword("【" + mMeeting.subject + "】会议定于" + TimeUtils.s_long_2_str(mMeeting.startDate) + " "
                        + TimeUtils.chat_long_2_str(mMeeting.startTime) + "-" + TimeUtils.chat_long_2_str(mMeeting.endTime)
                        + "进行,组织方邀请您作为嘉宾参会。"
                        ,"参会方式:",
                        "•PC端浏览器中输入",
                        mLink.getText().toString(),
                        "•使用嘉宾密码" + mGuestPassword.getText() + "登录");
                copyToClipboard(spandes);
            }
        });
        mTips = getViewById(R.id.tips);
        mTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mThis, MeetingTipsActivity.class);
                startActivity(intent);
            }
        });
        mPasswordLayout = getViewById(R.id.password_layout);
        String userId = SharedPreferenceUtil.getString(mThis, "id", "");
        if (mMeeting != null && mMeeting.createUserId != null && mMeeting.createUserId.equals(userId) && mMeeting.status == 1) {//是会议创建者,并且会议未开始
            mPasswordLayout.setVisibility(View.VISIBLE);
            mEdit.setVisibility(View.VISIBLE);
        }

    }

    private void copyToClipboard(CharSequence text) {

        StringUtils.copyToClipboard(mThis, text);
        ToastUtil.showToast(mThis, "复制成功");
    }
    private void copyURLToClipboard(CharSequence text){
        StringUtils.copyToClipboardURL(mThis, text);
        ToastUtil.showToast(mThis, "复制成功");
    }
    private void enterMeeting() {
        InputDialog dialog = new InputDialog(mThis);
        dialog.setOnConfirmListener(new InputDialog.OnConfirmListener() {
            @Override
            public void onConfirm(Dialog dialog, String input) {
                if (!TextUtils.isEmpty(input)) {
                    if (input.equals(mMeeting.organizerToken) || input.equals(mMeeting.panelistToken)) {//组织密码和嘉宾密码不能在手机端登录
                        ToastUtil.showToast(mThis, "您输入的密码有误");
                        return;
                    }
                    String username = SharedPreferenceUtil.getString(mThis, "username", "");
                    start(mMeeting.domain, mMeeting.number, username, input, "0", "", "");
                } else {
                    ToastUtil.showToast(mThis, "请输入密码");
                }
            }
        });
        dialog.show();
    }

    private void editMeeting() {
        Intent intent = new Intent(mThis, CreateAndEditMeetingActivity.class);
        intent.putExtra("meeting", mMeeting);
        startActivity(intent);

    }

    private void start(String domain, String number, String nickName, String joinPwd, String serviceType, String loginAccount, String loginPwd) {
        Logger.d(TAG, "meeting param domain=" + domain);
        Logger.d(TAG, "meeting param number=" + number);
        Logger.d(TAG, "meeting param nickName=" + nickName);
        Logger.d(TAG, "meeting param joinPwd=" + joinPwd);

        Intent intent = new Intent(this, MyLiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("domain", domain);
        bundle.putString("number", number);
        bundle.putString("nickName", nickName);
        bundle.putString("joinPwd", joinPwd);
        bundle.putString("serviceType", serviceType);
        bundle.putString("loginAccount", loginAccount);
        bundle.putString("loginPwd", loginPwd);
        intent.putExtra("joinMeeting", bundle);
        startActivity(intent);
    }

}
