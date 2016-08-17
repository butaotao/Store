package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.toolbox.DCommonRequest;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.TimeUtils;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Meeting;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;
import com.dachen.dgroupdoctorcompany.views.TimePickerEx;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.volley.custom.ObjectResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * 创建和编辑会议
 *
 * @author gaozhuo
 * @date 2016/3/8
 */
public class CreateAndEditMeetingActivity extends BaseActivity {
    private static final String TAG = CreateAndEditMeetingActivity.class.getSimpleName();
    public static final int EACH_PRICE = 10;//每个人会议价格
    private EditText mMeetingTitle;
    private TextView mMeetingDate;
    private TextView mMeetingStartTime;
    private TextView mMeetingEndTime;
    private EditText mMeetingCount;
    private TextView mMeetingExpenses;
    private TextView mMeetingExpensesReal;
    private TextView mOrganizationPassword;
    private EditText mGuestPassword;
    private TextView mWatchPassword;
    private RelativeLayout mSelectDate;
    private RelativeLayout mSelectStartTime;
    private RelativeLayout mSelectEndTime;
    private Button mCancel;
    private Button mPublish;
    private Space mSpace;
    private Meeting mMeeting;
    private String mMeetingId;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_edit);
        initVariables();
        initViews();
        fillView();
    }

    private void initVariables() {
        mMeeting = (Meeting) getIntent().getSerializableExtra("meeting");
        if (mMeeting != null) {
            mMeetingId = mMeeting.id;
        }
    }

    private void fillView() {
        if (mMeeting != null) {
            setTitle("编辑会议");
            mMeetingTitle.setText(mMeeting.subject);
            mMeetingDate.setText(TimeUtils.s_long_2_str(mMeeting.startDate));
            mMeetingStartTime.setText(TimeUtils.chat_long_2_str(mMeeting.startTime));
            mMeetingEndTime.setText(TimeUtils.chat_long_2_str(mMeeting.endTime));
            mMeetingCount.setText(mMeeting.attendeesCount + "");
            mMeetingExpensesReal.setText("¥" + (int) (mMeeting.price / 100) + "元");
            mMeetingExpenses.setText("¥" + mMeeting.attendeesCount * EACH_PRICE + "元");
            mOrganizationPassword.setText(mMeeting.organizerToken);
            mGuestPassword.setText(mMeeting.panelistToken);
            mWatchPassword.setText(mMeeting.attendeeToken);
        } else {
            setTitle("新建会议");
        }
    }


    public void initViews() {
        mMeetingTitle = getViewById(R.id.meeting_title);
        mMeetingDate = getViewById(R.id.meeting_date);
        mMeetingStartTime = getViewById(R.id.meeting_start_time);
        mMeetingEndTime = getViewById(R.id.meeting_end_time);
        mMeetingCount = getViewById(R.id.meeting_count);
        mMeetingCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    mMeetingExpenses.setText("");
                } else {
                    mMeetingExpenses.setText("¥" + Integer.parseInt(s.toString()) * EACH_PRICE + "元");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mMeetingExpenses = getViewById(R.id.meeting_expenses);
        //设置删除线
        mMeetingExpenses.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mMeetingExpensesReal = getViewById(R.id.meeting_expenses_real);
        mOrganizationPassword = getViewById(R.id.organization_password);
        mGuestPassword = getViewById(R.id.guest_password);
        mWatchPassword = getViewById(R.id.watch_password);
        mSelectDate = getViewById(R.id.select_date_layout);
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        mSelectStartTime = getViewById(R.id.select_start_time_layout);
        mSelectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartTime();

            }
        });
        mSelectEndTime = getViewById(R.id.select_end_time_layout);
        mSelectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEndTime();
            }
        });
        mPublish = getViewById(R.id.publish);
        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
        mCancel = getViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mSpace = getViewById(R.id.space);

        if (mMeeting != null) {
            mCancel.setVisibility(View.VISIBLE);
            mSpace.setVisibility(View.VISIBLE);
        }
    }

    private void cancel() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mThis, new CustomDialog.CustomClickEvent() {
            @Override
            public void onClick(CustomDialog customDialog) {
                stopMeeting();
                customDialog.dismiss();
            }

            @Override
            public void onDismiss(CustomDialog customDialog) {
                customDialog.dismiss();
            }
        }).setTitle("取消会议").setMessage("您确定要取消会议吗?").setPositive("确定").setNegative("取消");
        builder.create().show();
    }

    private void publish() {
        if (!checkInput()) {
            return;
        }
        mMeeting = buildMeetingFromUI();
        if (TextUtils.isEmpty(mMeeting.id)) {
            createOrUpdateMeeting(true);
        } else {
            createOrUpdateMeeting(false);
        }
    }

    private void selectEndTime() {
        Calendar calendar = Calendar.getInstance();
        if (mMeeting != null) {
            calendar.setTimeInMillis(mMeeting.endTime);
        }
        TimePicker picker = new TimePickerEx(this, TimePicker.HOUR_OF_DAY);
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                mMeetingEndTime.setText(hour + ":" + minute);
                mMeetingEndTime.setTextColor(getResources().getColor(R.color.gray_333333));
            }
        });
        picker.show();
    }

    private void selectStartTime() {
        Calendar calendar = Calendar.getInstance();
        if (mMeeting != null) {
            calendar.setTimeInMillis(mMeeting.startTime);
        }
        TimePicker picker = new TimePickerEx(this, TimePicker.HOUR_OF_DAY);
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                mMeetingStartTime.setText(hour + ":" + minute);
                mMeetingStartTime.setTextColor(getResources().getColor(R.color.gray_333333));
            }
        });
        picker.show();
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        DatePicker picker = new DatePicker(this);
        picker.setRange(calendar.get(Calendar.YEAR), 2100);
        if (mMeeting != null) {
            calendar.setTimeInMillis(mMeeting.startDate);
        }
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mMeetingDate.setText(year + "-" + month + "-" + day);
                mMeetingDate.setTextColor(getResources().getColor(R.color.gray_333333));
            }
        });
        picker.show();
    }

    /**
     * 创建或修改会议
     */
    private void createOrUpdateMeeting(final boolean isCreate) {
        mDialog.setMessage("发布中...");
        mDialog.show();
        final String reqTag = "createOrUpdateMeeting";
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        queue.cancelAll(reqTag);
        String interfaceName = isCreate ? Constants.MEETING_CREATE : Constants.MEETING_UPDATE;
        StringRequest request = new DCommonRequest(Request.Method.POST, AppConfig.getUrl(interfaceName, 3), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(TAG, "response=" + response);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                handlerCreateMeetingResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", UserInfo.getInstance(mThis).getSesstion());
                if (!isCreate) {
                    params.put("id", mMeeting.id);
                }
                params.put("companyId", mMeeting.companyId);
                params.put("company", mMeeting.company);
                params.put("subject", mMeeting.subject);
                params.put("startDate", mMeeting.startDate + "");
                params.put("startTime", mMeeting.startTime + "");
                params.put("endTime", mMeeting.endTime + "");
                params.put("attendeesCount", mMeeting.attendeesCount + "");
                params.put("price", mMeeting.price + "");
                params.put("organizerToken", mMeeting.organizerToken);
                params.put("panelistToken", mMeeting.panelistToken);
                params.put("attendeeToken", mMeeting.attendeeToken);
                Logger.d(TAG, "params=" + params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    /**
     * 取消会议
     */
    private void stopMeeting() {
        mDialog.setMessage("取消中...");
        mDialog.show();
        final String reqTag = "stopMeeting";
        RequestQueue queue = VolleyUtil.getQueue(mThis);
        queue.cancelAll(reqTag);
        StringRequest request = new DCommonRequest(Request.Method.POST, AppConfig.getUrl(Constants.MEETING_STOP, 3), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(TAG, "response=" + response);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                handlerStopMeetingResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtil.showErrorNet(mThis);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", UserInfo.getInstance(mThis).getSesstion());
                params.put("meetingId", mMeeting.id);
                Logger.d(TAG, "params=" + params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 0));
        request.setTag(reqTag);
        queue.add(request);
    }

    private void handlerStopMeetingResponse(String response) {
        ObjectResult<Meeting> result = JSON.parseObject(response, new TypeReference<ObjectResult<Meeting>>() {
        });
        if (result != null && result.getResultCode() == Result.CODE_SUCCESS) {
            ToastUtil.showToast(mThis, "取消成功");
            Intent intent = new Intent(mThis, MeetingListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            ToastUtil.showToast(mThis, "取消失败");
        }
    }

    private void handlerCreateMeetingResponse(String response) {
        ObjectResult<Meeting> result = JSON.parseObject(response, new TypeReference<ObjectResult<Meeting>>() {
        });
        if (result != null && result.getResultCode() == Result.CODE_SUCCESS) {
            ToastUtil.showToast(mThis, "发布成功");
            final String meeting_create_key = SharedPreferenceUtil.getString(this, "id", "") + "_isFirstCreateMeeting";
            boolean isFirstCreate = SharedPreferenceUtil.getBoolean(mThis, meeting_create_key, true);
            if (isFirstCreate) {
                SharedPreferenceUtil.putBoolean(mThis, meeting_create_key, false);
                Intent intent = new Intent(mThis, MeetingTipsActivity.class);
                intent.putExtra("from", CreateAndEditMeetingActivity.class.getSimpleName());
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mThis, MeetingListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("meeting", result.getData());
                startActivity(intent);
            }

        } else {
            ToastUtil.showToast(mThis, "发布失败");
        }


    }

    private Meeting buildMeetingFromUI() {
        Meeting meeting = new Meeting();
        meeting.id = mMeetingId;
        meeting.companyId = SharedPreferenceUtil.getString(mThis, "enterpriseId", "");
        meeting.company = SharedPreferenceUtil.getString(mThis, "enterpriseName", "");
        meeting.subject = mMeetingTitle.getText().toString();
        meeting.startDate = TimeUtils.s_str_2_long(mMeetingDate.getText().toString());
        meeting.startTime = TimeUtils.f_str_3_long(mMeetingDate.getText().toString() + " " + mMeetingStartTime.getText().toString());
        meeting.endTime = TimeUtils.f_str_3_long(mMeetingDate.getText().toString() + " " + mMeetingEndTime.getText().toString());
        meeting.attendeesCount = Integer.parseInt(mMeetingCount.getText().toString());
        String realPrice = mMeetingExpensesReal.getText().toString();
        meeting.price = Integer.parseInt(realPrice.substring(1, realPrice.length() - 1)) * 100;
        meeting.organizerToken = mOrganizationPassword.getText().toString();
        meeting.panelistToken = mGuestPassword.getText().toString();
        meeting.attendeeToken = mWatchPassword.getText().toString();
        return meeting;
    }

    private boolean checkInput() {

        if (TextUtils.isEmpty(mMeetingTitle.getText().toString())) {
            ToastUtil.showToast(mThis, "会议标题不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mMeetingDate.getText().toString())) {
            ToastUtil.showToast(mThis, "开会日期不能为空");
            return false;
        }

        if (compareDate() == -1) {
            ToastUtil.showToast(mThis, "开会日期不能早于当前日期");
            return false;
        }

        if (TextUtils.isEmpty(mMeetingStartTime.getText().toString())) {
            ToastUtil.showToast(mThis, "开始时间不能为空");
            return false;
        }

        String[] startTime = mMeetingStartTime.getText().toString().split(":");

        //日期在当天才需要对开始时间进行检查
        if (compareDate() == 0) {
            if (isBeforeTime(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE))) {
                ToastUtil.showToast(mThis, "开始时间不能早于或等于当前时间");
                return false;
            }
        }


        if (TextUtils.isEmpty(mMeetingEndTime.getText().toString())) {
            ToastUtil.showToast(mThis, "结束时间不能为空");
            return false;
        }

        String[] endTime = mMeetingEndTime.getText().toString().split(":");

        if (isBeforeTime(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]))) {
            ToastUtil.showToast(mThis, "结束时间不能早于或等于开始时间");
            return false;
        }

        if (TextUtils.isEmpty(mMeetingCount.getText().toString())) {
            ToastUtil.showToast(mThis, "观看人数不能为空");
            return false;
        }

        if ("0".equals(mMeetingCount.getText().toString())) {
            ToastUtil.showToast(mThis, "观看人数不能为0");
            return false;
        }

//        if (TextViewUtils.isEmpty(mMeetingExpenses.getText().toString())) {
//            ToastUtil.showToast(mThis, "会议费用不能为空");
//            return false;
//        }

        String organizerToken = mOrganizationPassword.getText().toString();
        String panelistToken = mGuestPassword.getText().toString();
        String attendeeToken = mWatchPassword.getText().toString();

        if (TextUtils.isEmpty(organizerToken)) {
            ToastUtil.showToast(mThis, "组织密码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(panelistToken)) {
            ToastUtil.showToast(mThis, "嘉宾密码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(attendeeToken)) {
            ToastUtil.showToast(mThis, "观看密码不能为空");
            return false;
        }

        if (organizerToken.length() != 6 || !TextUtils.isDigitsOnly(organizerToken)) {
            ToastUtil.showToast(mThis, "组织密码不为6位数字");
            return false;
        }

        if (organizerToken.length() != 6 || !TextUtils.isDigitsOnly(organizerToken)) {
            ToastUtil.showToast(mThis, "嘉宾密码不为6位数字");
            return false;
        }

        if (attendeeToken.length() != 6 || !TextUtils.isDigitsOnly(attendeeToken)) {
            ToastUtil.showToast(mThis, "观看密码不为6位数字");
            return false;
        }


        if (organizerToken.equals(panelistToken) || organizerToken.equals(attendeeToken) || panelistToken.equals(attendeeToken)) {
            ToastUtil.showToast(mThis, "密码不能相同");
            return false;
        }

        return true;

    }


    private int compareDate() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);


        String[] date = mMeetingDate.getText().toString().split("-");
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.clear();
        selectedCalendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));

        return selectedCalendar.compareTo(currentCalendar);

    }

    private boolean isBeforeTime(int hour1, int minute1, int hour2, int minute2) {
        if (hour1 < hour2) {
            return true;
        } else if (hour1 > hour2) {
            return false;
        } else {
            return minute1 <= minute2 ? true : false;
        }
    }


}
