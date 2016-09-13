package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.dachen.common.adapter.CommonAdapter;
import com.dachen.common.adapter.ViewHolder;
import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.entity.Meeting;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author gaozhuo
 * @date 2016/3/10
 */
public class MeetingListAdapter extends CommonAdapter<Meeting> {

    public MeetingListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public MeetingListAdapter(Context context, List<Meeting> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void bind(ViewHolder holder, Meeting data) {
        holder.setText(R.id.meeting_title, data.subject);
        holder.setText(R.id.meeting_date, TimeUtils.s_long_2_str(data.startDate));
        holder.setText(R.id.meeting_time, TimeUtils.chat_long_2_str(data.startTime) + "-" + TimeUtils.chat_long_2_str(data.endTime));
        ImageLoader.getInstance().displayImage(data.headPicFileName, (ImageView) holder.getView(R.id.head_image), CompanyApplication.mAvatarCircleImageOptions);
        holder.setText(R.id.name, "发起人:" + data.createUserName);
        if (data.status == 1) {
            holder.setText(R.id.state, "未开始");
            holder.setBackgroundRes(R.id.state, R.color.gray_d6d6d6);
        } else {
            holder.setText(R.id.state, "进行中");
            holder.setBackgroundRes(R.id.state, R.color.blue_3cbaff);
        }

    }
}
