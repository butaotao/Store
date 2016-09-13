package com.dachen.dgroupdoctorcompany.im.utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.dachen.dgroupdoctorcompany.activity.ChatShareMsgActivity;
import com.dachen.dgroupdoctorcompany.activity.ColleagueDetailActivity;
import com.dachen.dgroupdoctorcompany.activity.WebActivityForCompany;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.imsdk.activities.ChatActivityV2;
import com.dachen.imsdk.adapter.ChatAdapterV2;
import com.dachen.imsdk.adapter.MsgMenuAdapter;
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
    public void onClickMyself(ChatMessagePo chatMessage, ChatAdapterV2 adapter) {
        List<CompanyContactListEntity> l=mCompanyDao.queryByUserId(chatMessage.fromUserId);
        if(l!=null&&l.size()>0){
            CompanyContactListEntity e=l.get(0);
            Intent intent = new Intent(mContext, ColleagueDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("peopledes",e);
            intent.putExtra("peopledes", bundle);
            mContext.startActivity(intent);
        }
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
    public void onClickNewMpt17(ChatMessagePo chatMessage, ChatAdapterV2 adapter, View v) {
        ImgTextMsgV2 mpt = getImgTextMsgV2(chatMessage);
        if (mpt == null) {
            return;
        }
        final String url = mpt.url;
        Intent intent = new Intent(mContext, WebActivityForCompany.class);
        intent.putExtra("url", url).putExtra(WebActivityForCompany.INTENT_SHOW_TITLE,true);
        intent.putExtra("title","玄关健康团队");
//        intent.putExtra(WebActivity.INTENT_CHECK_404,true);
        intent.putExtra(WebActivityForCompany.INTENT_NO_CACHE, true);
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

    @Override
    public boolean onForwardMessage(String msgId) {
        // TODO: 2016/8/1
        Intent intent = new Intent(mContext, ChatShareMsgActivity.class);
        intent.putExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID, msgId);
        mContext.startActivity(intent);
        return true;
    }
}
