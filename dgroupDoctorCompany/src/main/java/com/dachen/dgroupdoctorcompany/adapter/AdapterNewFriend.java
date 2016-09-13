package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.NewFriendsEntity;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/3/7.
 */
public class AdapterNewFriend extends BaseCustomAdapter<NewFriendsEntity.Data.FriendInfo> {

    Context context;
    String id = "";
    public AdapterNewFriend(Context context, int resId, List<NewFriendsEntity.Data.FriendInfo> objects) {
        super(context, resId, objects);
        this.context = context;
        id = UserInfo.getInstance(context).getId();
    }


    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHoder();
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        NewFriendsEntity.Data.FriendInfo friendInfo = getItem(position);
        ViewHoder hoder = (ViewHoder) baseViewHolder;
        String url = "";
        String name = "";
       /* if (id.equals(friendInfo.toUserId)){
            url = friendInfo.toHeadPicFileName;
            name = friendInfo.toUserName;
        } else {*/
            url =  friendInfo.toHeadPicFileName;
            name = friendInfo.toUserName;
        //}
//        ImageLoader.getInstance().displayImage(url, hoder.iv_headicon);
        CustomImagerLoader.getInstance().loadImage(hoder.iv_headicon,url);
        hoder.tv_name.setText(name);

        //1等待验证 2,已通过，3.已拒绝
        if (friendInfo.status.equals("1")){
            hoder.tv_frendstate.setText("等待验证");
            hoder.tv_frendstate.setTextColor(context.getResources().getColor(R.color.waitfriendarg));
        }else if (friendInfo.status.equals("2")){
            hoder.tv_frendstate.setTextColor(context.getResources().getColor(R.color.color_aaaaaa));
            hoder.tv_frendstate.setText("已通过");
        }else if (friendInfo.status.equals("3")){
            hoder.tv_frendstate.setTextColor(context.getResources().getColor(R.color.color_aaaaaa));
            hoder.tv_frendstate.setText("已拒绝");
        }
        hoder.tv_customervisit.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(friendInfo.toUserTitle)){
            hoder.tv_customervisit.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(friendInfo.toUserTitle)){
            hoder.tv_customervisit.setText(friendInfo.toUserTitle);
        }else {
            hoder.tv_customervisit.setText("");
        }
        if (!TextUtils.isEmpty(friendInfo.toUserHospital)){
            hoder.tv_frenddes.setText(friendInfo.toUserHospital);
        }else {
            hoder.tv_frenddes.setText("");
        }

        if (!TextUtils.isEmpty(friendInfo.toUserDepartment)){
            hoder.tv_depart.setText(friendInfo.toUserDepartment);
        }else {
            hoder.tv_depart.setText("");
        }

    }

    public class ViewHoder extends BaseCustomAdapter.BaseViewHolder {
        @Nullable
        @Bind(R.id.iv_headicon)
        ImageView iv_headicon;
        @Nullable
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Nullable
        @Bind(R.id.tv_customervisit)
        TextView tv_customervisit;
        @Nullable
        @Bind(R.id.tv_frenddes)
        TextView tv_frenddes;
        @Nullable
        @Bind(R.id.tv_frendstate)
        TextView tv_frendstate;
        @Nullable
        @Bind(R.id.tv_depart)
        TextView tv_depart;
    }
}