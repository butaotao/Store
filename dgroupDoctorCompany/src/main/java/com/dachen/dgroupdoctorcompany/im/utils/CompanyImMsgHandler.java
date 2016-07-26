package com.dachen.dgroupdoctorcompany.im.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.dachen.dgroupdoctorcompany.activity.WebActivityForCompany;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.ImgTextMsgV2;
import com.dachen.imsdk.entity.MultiMpt;
import com.dachen.imsdk.out.ImMsgHandler;

import java.util.List;

/**
 * Created by Mcp on 2016/3/12.
 */
public class CompanyImMsgHandler extends ImMsgHandler{
    protected DoctorDao mDoctorDao;
    protected CompanyContactDao mCompanyDao;
    public CompanyImMsgHandler(ChatActivityV2 mContext) {
        super(mContext);
        mDoctorDao=new DoctorDao(mContext);
        mCompanyDao=new CompanyContactDao(mContext);
    }

    @Override
    public void onClickNewMpt16(ChatMessagePo chatMessage, ChatAdapterV2 adapter, View v) {
        MultiMpt multi= JSON.parseObject(chatMessage.param, MultiMpt.class);
        ImgTextMsgV2 mpt = null;
        List<ImgTextMsgV2> mptList = multi.list;
        if (mptList != null && mptList.size() > 0) {
            mpt = mptList.get(0);
        }
        if (mpt == null) {
            return;
        }
        final String url = mpt.url;
        if(TextUtils.isEmpty(url))return;
        Intent intent = new Intent(mContext, WebActivityForCompany.class);
        intent.putExtra("url", url).putExtra(WebActivityForCompany.INTENT_NO_CACHE, true).putExtra(WebActivityForCompany.INTENT_SHOW_TITLE,true);
        intent.putExtra("title","玄关健康团队");
//        intent.putExtra(WebActivity.INTENT_CHECK_404,true);
        mContext.startActivity(intent);
    }

    @Override
    public boolean menuHasForward() {
        return true;
    }

    @Override
    public boolean menuHasRetract() {
        return true;
    }
}
